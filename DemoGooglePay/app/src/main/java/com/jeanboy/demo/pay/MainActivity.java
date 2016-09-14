package com.jeanboy.demo.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jeanboy.demo.pay.util.IabBroadcastReceiver;
import com.jeanboy.demo.pay.util.IabHelper;
import com.jeanboy.demo.pay.util.IabResult;
import com.jeanboy.demo.pay.util.Inventory;
import com.jeanboy.demo.pay.util.Purchase;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();


    boolean hadProduct = false;

    // The helper object
    IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;


    private String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuIjwOzaqmcWF9bUpPcxiVgIXcKcZxG7OAAbLqrxh6ljYEJryiCRgBNlSw122MqpeiELKZsqXZSVZF3YVZOMLVOVP+t1nSRqOBtnhMlgp0tjKFoHur/Qt6eGmQIZ7U0SmPE8R7q4fXyzznDJK3UBsP9srLLRG9UZgeDfUv4KrdSm5H5LMSQp5WnpNrTAfexvdASF3KHEX2ikQNXxXCfOBl598IY9B07lyFKibw+XzTVffqKQyoMfRF3TxvleR0MVZuB8GkVa9JFiWqKfjTGf10NAajGUVJrSucfpJPD3dVFQInfRBYgtX4VlhSpPgc0BcjbC5hv1wMKQVAA+rS016XQIDAQAB";

    static final int RC_REQUEST = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.e(TAG, "Problem setting up In-app Billing: " + result);
                }

                if (mHelper == null) return;

                //查询购买商品
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

    }

    public void toLogin(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }


//    private List<String> productNameList = new ArrayList<>();
    static String PRODUCT_ONE = "product_one";

    public void toBuy(View v) {
//        productNameList.add(PRODUCT_ONE);
//        mHelper.queryInventoryAsync(true, productNameList, new IabHelper.QueryInventoryFinishedListener() {
//            @Override
//            public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
//                if (mHelper == null) return;
//                if (result.isFailure()) {
//                    Log.e(TAG, "**** TrivialDrive Error: Failed to query inventory: " + result);
//                    return;
//                }
//
//                Log.d(TAG, "Query inventory was successful.");
//                //                String price = inventory.getSkuDetails(PRODUCT_ONE).getPrice();
//
//                // TODO: for security, generate your payload here for verification. See the comments on
//                String payload = "";
//                mHelper.launchPurchaseFlow(MainActivity.this, PRODUCT_ONE, RC_REQUEST, mPurchaseFinishedListener, payload);
//
//            }
//        });
//        if (hadProduct) {
//            Toast.makeText(this, "已经购买过无需重复购买！", Toast.LENGTH_SHORT).show();
//            return;
//        }
        // TODO: for security, generate your payload here for verification. See the comments on
        String payload = "";
        mHelper.launchPurchaseFlow(MainActivity.this, PRODUCT_ONE, RC_REQUEST, mPurchaseFinishedListener, payload);


    }


    //购买回调
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            if (mHelper == null) return;

            if (result.isFailure()) {
                Log.e(TAG, "**** 购买失败 Error: Error purchasing: " + result);
                // TODO: 2016/9/14 update ui
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                Log.e(TAG, "**** 验证失败 Error: Error purchasing. Authenticity verification failed. ");
                // TODO: 2016/9/14 update ui
                return;
            }

            Log.d(TAG, "购买成功 Purchase successful.");

            if (purchase.getSku().equals(PRODUCT_ONE)) {
                hadProduct = true;
                // TODO: 2016/9/14 update ui
            }
        }
    };

    //查询购买记录回调
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");
            if (mHelper == null) return;
            if (result.isFailure()) {
                Log.e(TAG, "**** 查询购买记录 Error: Failed to query inventory: " + result);
                return;
            }

            Purchase purchase = inventory.getPurchase(PRODUCT_ONE);
            hadProduct = (purchase != null && verifyDeveloperPayload(purchase));


            Log.e(TAG, "**** user has product ****hadProduct" + hadProduct);

            // TODO: 2016/9/14 update ui
        }
    };

    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            mHelper.dispose();
        }
        mHelper = null;
    }
}
