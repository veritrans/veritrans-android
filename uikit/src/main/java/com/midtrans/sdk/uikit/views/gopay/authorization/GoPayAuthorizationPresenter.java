package com.midtrans.sdk.uikit.views.gopay.authorization;

import com.midtrans.sdk.corekit.callback.GoPayResendAuthorizationCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.GoPayResendAuthorizationResponse;
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

    public TransactionResponse getTransactionResponse() {
        return this.transactionResponse;
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

    public void resendVerificationCode() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        String snapToken = midtransSDK.readAuthenticationToken();

        midtransSDK.resendGopayAuthorization(snapToken, new GoPayResendAuthorizationCallback() {
            @Override
            public void onSuccess(GoPayResendAuthorizationResponse response) {
                view.onResendSuccess(response);
            }

            @Override
            public void onFailure(GoPayResendAuthorizationResponse response, String reason) {
                view.onResendFailure(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onResendError(error);
            }
        });

    }
}
