package com.midtrans.sdk.uikit.views.gopay.authorization;

import com.midtrans.sdk.corekit.callback.GoPayResendAuthorizationCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.models.GoPayResendAuthorizationResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;

/**
 * Created by ziahaqi on 9/11/17.
 */

public class GoPayAuthorizationPresenter extends BasePaymentPresenter<GoPayAuthorizationView> {

    public GoPayAuthorizationPresenter(GoPayAuthorizationView view) {
        super();
        this.view = view;
    }

    public void authorizePayment(final String verificationCode) {
        String snapToken = getMidtransSDK().readAuthenticationToken();
        getMidtransSDK().authorizeGoPayPayment(snapToken, verificationCode, new TransactionCallback() {
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
        String snapToken = getMidtransSDK().readAuthenticationToken();

        getMidtransSDK().resendGopayAuthorization(snapToken, new GoPayResendAuthorizationCallback() {
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
