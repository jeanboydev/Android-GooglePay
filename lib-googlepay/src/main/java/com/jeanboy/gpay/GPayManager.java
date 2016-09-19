package com.jeanboy.gpay;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.jeanboy.gpay.util.IabHelper;
import com.jeanboy.gpay.util.IabResult;
import com.jeanboy.gpay.util.Inventory;
import com.jeanboy.gpay.util.MD5;
import com.jeanboy.gpay.util.Purchase;

/**
 * Created by jeanboy on 2016/9/19.
 */
public class GPayManager {

    private final String TAG = GPayManager.class.getSimpleName();

    private Activity context;

    private String publicKey;

    static final int RC_REQUEST = 10001;

    private IabHelper mHelper;
    private boolean isHelperSetupSucceed = false;

    private static GPayManager instance;

    private GPayManager() {
    }

    public static GPayManager getInstance() {
        if (instance == null) {
            synchronized (GPayManager.class) {
                if (instance == null) {
                    instance = new GPayManager();
                }
            }
        }
        return instance;
    }

    public void build(Activity context, String publicKey, boolean isDebug) {
        this.context = context;
        this.publicKey = publicKey;
        mHelper = new IabHelper(context, publicKey);
        mHelper.enableDebugLogging(isDebug, TAG);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.e(TAG, "Problem setting up In-app Billing: " + result);
                    return;
                }
                if (mHelper == null) return;
                isHelperSetupSucceed = true;
            }
        });
    }

    /**
     * onActivityResult处理回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            return;
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /**
     * 取消支付服务
     */
    public void onDestroy() {
        if (mHelper != null) {
            mHelper.dispose();
        }
        mHelper = null;
        isHelperSetupSucceed = false;
    }

    /**
     * 购买商品入口
     *
     * @param productId
     * @param payload
     * @param isAutoConsume
     * @param inventoryListener
     */
    public void toBuy(final String productId, final String payload, final boolean isAutoConsume,
                      final InventoryListener inventoryListener) {
        queryInventory(productId, payload, new QueryInventoryListener() {
            @Override
            public void finish(Purchase purchase, boolean hadProduct) {
                if (hadProduct) {//已经拥有
                    if (isAutoConsume) {//是否自动消耗
                        consumeProduct(purchase, new ConsumeListener() {
                            @Override
                            public void success() {
                                toPurchase(productId, payload, inventoryListener);
                            }

                            @Override
                            public void error(String msg) {
                                if (inventoryListener != null) {
                                    inventoryListener.error(productId, msg);
                                }
                            }
                        });
                    } else {
                        Log.e(TAG, "已经购买过无需重复购买！");
                        if (inventoryListener != null) {
                            inventoryListener.finish(productId);
                        }
                    }
                } else {
                    toPurchase(productId, payload, inventoryListener);
                }
            }

            @Override
            public void error(String productId, String msg) {
                if (inventoryListener != null) {
                    inventoryListener.error(productId, msg);
                }
            }
        });
    }

    /**
     * 查询购买的商品
     *
     * @param productId
     * @param payload
     * @param queryInventoryListener
     */
    public void queryInventory(final String productId, final String payload,
                               final QueryInventoryListener queryInventoryListener) {
        if (isEnable()) {
            mHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
                @Override
                public void onQueryInventoryFinished(IabResult result, final Inventory inventory) {
                    Log.d(TAG, "Query inventory finished.");
                    if (mHelper == null) return;
                    if (result.isFailure()) {
                        Log.e(TAG, "**** 查询购买记录 Error: Failed to query inventory: " + result);
                        return;
                    }

                    final Purchase purchase = inventory.getPurchase(productId);
                    boolean hadProduct = (purchase != null && verifyDeveloperPayload(purchase, payload));
                    if (queryInventoryListener != null) {
                        queryInventoryListener.finish(purchase, hadProduct);
                    }
                }
            });
        } else {
            Log.e(TAG, "支付环境异常 IabHelper setup not completely successful!");
            if (queryInventoryListener != null) {
                queryInventoryListener.error(productId, "IabHelper setup not completely successful!");
            }
        }
    }

    /**
     * 生成校验字符串，此操作最好在服务器端处理
     *
     * @param productId
     * @return
     */
    public String generatePayLoad(String productId) {
        return MD5.string2MD5(productId + publicKey);
    }

    /**
     * 校验payload是否一致，此操作最好在服务器端处理
     *
     * @param p
     * @param payload
     * @return
     */
    private boolean verifyDeveloperPayload(Purchase p, String payload) {
        String nowPayload = p.getDeveloperPayload();
        return payload.equals(nowPayload);
    }

    /**
     * 判断是否初始化完成
     *
     * @return
     */
    private boolean isEnable() {
        return isHelperSetupSucceed && mHelper != null;
    }

    /**
     * 消耗商品处理
     *
     * @param purchase
     * @param consumeListener
     */
    private void consumeProduct(Purchase purchase, final ConsumeListener consumeListener) {
        mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
                if (mHelper == null) return;

                if (result.isSuccess()) {
                    Log.d(TAG, "商品消耗成功 Consumption successful. Provisioning.");
                    if (consumeListener != null) {
                        consumeListener.success();
                    }
                } else {
                    Log.e(TAG, "商品消耗失败 Error while consuming: " + result);
                    if (consumeListener != null) {
                        consumeListener.error("Error while consuming: " + result);
                    }
                }
            }
        });
    }

    /**
     * 购买商品处理
     *
     * @param productId
     * @param payload
     * @param inventoryListener
     */
    private void toPurchase(final String productId, final String payload, final InventoryListener inventoryListener) {
        mHelper.launchPurchaseFlow(context, productId, RC_REQUEST, new IabHelper.OnIabPurchaseFinishedListener() {
            @Override
            public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
                if (mHelper == null) return;

                if (result.isFailure()) {
                    Log.e(TAG, "**** 购买失败 Error: Error purchasing: " + result);
                    if (inventoryListener != null) {
                        inventoryListener.error(productId, "Error: Error purchasing: " + result.getMessage());
                    }
                    return;
                }
                if (!verifyDeveloperPayload(purchase, payload)) {
                    Log.e(TAG, "**** 验证失败 Error: Error purchasing. Authenticity verification failed. ");
                    if (inventoryListener != null) {
                        inventoryListener.error(productId, "Error: Error purchasing. Authenticity verification failed.");
                    }
                    return;
                }

                Log.d(TAG, "购买成功 Purchase successful.");

                if (purchase.getSku().equals(productId)) {
                    if (inventoryListener != null) {
                        inventoryListener.finish(productId);
                    }
                }
            }
        }, payload);
    }

    public interface QueryInventoryListener {

        void finish(Purchase purchase, boolean hadProduct);

        void error(String productId, String msg);
    }

    public interface InventoryListener {

        void finish(String productId);

        void error(String productId, String msg);
    }

    public interface ConsumeListener {

        void success();

        void error(String msg);
    }
}
