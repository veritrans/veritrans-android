package com.midtrans.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.core.MidtransCoreSdk;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.CheckoutCallback;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.TransactionRequest;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.GopayDeeplink;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.TokenResponse;
import com.midtrans.sdk.corekit.core.snap.model.transaction.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.TransactionOptionsResponse;
import com.midtrans.sdk.corekit.utilities.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MidtransCoreSdk
                .init(this,
                        BuildConfig.CLIENT_KEY,
                        BuildConfig.BASE_URL)
                .setLogEnabled(true)
                .setEnvironment(Environment.SANDBOX)
                .build();


        GopayDeeplink gopayDeeplink = new GopayDeeplink("gopay");
        TransactionRequest transactionRequest = new TransactionRequest("sample_sdk_test_core_00005", 20000);
        transactionRequest.setGopay(gopayDeeplink);

        MidtransCoreSdk.getInstance().setTransactionRequest(transactionRequest);
        MidtransCoreSdk.getInstance().checkout(new CheckoutCallback() {
            @Override
            public void onSuccess(TokenResponse token) {
                Logger.debug("MIDTRANS SDK NEW RETURN SUCCESS >>> " + token.getSnapToken());
                getTransactionOptions(token.getSnapToken());
            }

            @Override
            public void onFailure(TokenResponse token, String reason) {
                Logger.debug("MIDTRANS SDK NEW RETURN FAILURE >>> " + reason);
            }

            @Override
            public void onError(Throwable error) {
                Logger.debug("MIDTRANS SDK NEW RETURN ERROR >>> " + error.getMessage());
            }
        });
    }

    private void getTransactionOptions(String snapToken) {
        MidtransCoreSdk.getInstance().getTransactionOptions(snapToken, new TransactionOptionsCallback() {
            @Override
            public void onSuccess(TransactionOptionsResponse transaction) {
                Logger.debug("MIDTRANS SDK NEW RETURN SUCCESS >>> " + transaction.getToken());

            }

            @Override
            public void onFailure(TransactionOptionsResponse transaction, String reason) {
                Logger.debug("MIDTRANS SDK NEW RETURN FAILURE >>> " + reason);

            }

            @Override
            public void onError(Throwable error) {
                Logger.debug("MIDTRANS SDK NEW RETURN ERROR >>> " + error.getMessage());

            }
        });
    }
}