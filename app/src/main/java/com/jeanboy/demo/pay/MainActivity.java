package com.jeanboy.demo.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;


public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();


    private String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuIjwOzaqmcWF9bUpPcxiVgIXcKcZxG7OAAbLqrxh6ljYEJryiCRgBNlSw122MqpeiELKZsqXZSVZF3YVZOMLVOVP+t1nSRqOBtnhMlgp0tjKFoHur/Qt6eGmQIZ7U0SmPE8R7q4fXyzznDJK3UBsP9srLLRG9UZgeDfUv4KrdSm5H5LMSQp5WnpNrTAfexvdASF3KHEX2ikQNXxXCfOBl598IY9B07lyFKibw+XzTVffqKQyoMfRF3TxvleR0MVZuB8GkVa9JFiWqKfjTGf10NAajGUVJrSucfpJPD3dVFQInfRBYgtX4VlhSpPgc0BcjbC5hv1wMKQVAA+rS016XQIDAQAB";


    BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        GPayManager.getInstance().build(this, base64EncodedPublicKey, true);

        bp = new BillingProcessor(this, base64EncodedPublicKey, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, TransactionDetails details) {

            }

            @Override
            public void onPurchaseHistoryRestored() {

            }

            @Override
            public void onBillingError(int errorCode, Throwable error) {

            }

            @Override
            public void onBillingInitialized() {

            }
        });

    }

    public void toLogin(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }


    //    private List<String> productNameList = new ArrayList<>();
    static String PRODUCT_ONE = "product_one";
    static String PRODUCT_THREE = "product_three";//订阅
    static String PRODUCT_FOUR = "product_four";

    public void toBuy(View v) {

        bp.subscribe(this, PRODUCT_ONE);
//        String payload = GPayManager.getInstance().generatePayLoad(PRODUCT_ONE);
//        GPayManager.getInstance().toBuy(PRODUCT_ONE, payload, false, new GPayManager.InventoryListener() {
//            @Override
//            public void finish(String productId) {
//                Toast.makeText(MainActivity.this, "购买成功！" + PRODUCT_ONE, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void error(String productId, String msg) {
//                Toast.makeText(MainActivity.this, "购买失败！" + PRODUCT_ONE + "::" + msg, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void toBuyWithAutoConsume(View v) {
//        String payload = GPayManager.getInstance().generatePayLoad(PRODUCT_ONE);
//        GPayManager.getInstance().toBuy(PRODUCT_ONE, payload, true, new GPayManager.InventoryListener() {
//            @Override
//            public void finish(String productId) {
//                Toast.makeText(MainActivity.this, "购买成功！" + PRODUCT_ONE, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void error(String productId, String msg) {
//                Toast.makeText(MainActivity.this, "购买失败！" + PRODUCT_ONE + "::" + msg, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    public void toQuery(View v) {
//        String payload = GPayManager.getInstance().generatePayLoad(PRODUCT_ONE);
//        GPayManager.getInstance().queryInventory(PRODUCT_ONE, payload, new GPayManager.QueryInventoryListener() {
//
//            @Override
//            public void finish(Purchase purchase, SkuDetails skuDetails, boolean hadProduct) {
//                Toast.makeText(MainActivity.this, "查询成功==hadProduct==" + hadProduct + "====price===" + skuDetails.getPrice() + "===" + PRODUCT_ONE, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void error(String productId, String msg) {
//                Toast.makeText(MainActivity.this, "查询失败！" + PRODUCT_ONE + "::" + msg, Toast.LENGTH_SHORT).show();
//            }
//        });

        SkuDetails skuDetails = bp.getSubscriptionListingDetails(PRODUCT_THREE);
        Log.e(MainActivity.class.getName(), "======" + skuDetails.toString());
    }

    public void toBuy4(View v) {

        bp.subscribe(this, "test_subscription");
//        String payload = GPayManager.getInstance().generatePayLoad(PRODUCT_FOUR);
//        GPayManager.getInstance().toBuy(PRODUCT_FOUR, payload, false, new GPayManager.InventoryListener() {
//            @Override
//            public void finish(String productId) {
//                Toast.makeText(MainActivity.this, "购买成功！" + PRODUCT_FOUR, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void error(String productId, String msg) {
//                Toast.makeText(MainActivity.this, "购买失败！" + PRODUCT_FOUR + "::" + msg, Toast.LENGTH_SHORT).show();
//            }
//        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        GPayManager.getInstance().onActivityResult(requestCode, resultCode, data);
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
//        GPayManager.getInstance().onDestroy();
    }
}
