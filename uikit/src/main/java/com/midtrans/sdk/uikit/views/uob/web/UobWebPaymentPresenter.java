package com.midtrans.sdk.uikit.views.uob.web;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

class UobWebPaymentPresenter extends BasePaymentPresenter<UobWebPaymentView> {
    UobWebPaymentPresenter(UobWebPaymentView view) {
        super();
        this.view = view;
    }

    Boolean isProductionBuild() {
        return BuildConfig.FLAVOR.equals(UiKitConstants.ENVIRONMENT_PRODUCTION);
    }

    void startUobEzpayPayment() {
        String snapToken = getMidtransSDK().readAuthenticationToken();
        startUobEzpayPayment(snapToken);
    }

    private void startUobEzpayPayment(String snapToken) {
        getMidtransSDK().paymentUsingUobEzpay(snapToken, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                transactionResponse = response;
                view.onPaymentSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                transactionResponse = response;
                view.onPaymentFailure(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onPaymentError(error);
            }
        });
    }
}
