package com.midtrans.sdk.uikit.views.gopay.authorization;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;

/**
 * Created by ziahaqi on 9/11/17.
 */

public class GoPayAuthorizationPresenter extends BasePaymentPresenter<GoPayAuthorizationView> {


    private TransactionResponse transactionResponse;

    public GoPayAuthorizationPresenter(GoPayAuthorizationView view) {
        this.view = view;
    }

    public void authorizePayment(final String verificationCode) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        String snapToken = midtransSDK.readAuthenticationToken();
        MidtransSDK.getInstance().authorizeGoPayPayment(snapToken, verificationCode, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                transactionResponse = response;
                view.onVerificationCodeSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                transactionResponse = response;
                view.onVerificationCodeFailure(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onVerificationCodeError(error);
            }
        });
    }

    public TransactionResponse getTransactionResponse() {
        return this.transactionResponse;
    }
}
