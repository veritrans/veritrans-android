package com.midtrans.sdk.uikit.views.uob.app;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.callback.GetTransactionStatusCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.TransactionStatusResponse;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

class UobAppPaymentPresenter extends BasePaymentPresenter<UobAppPaymentView> {

    UobAppPaymentPresenter(UobAppPaymentView view) {
        super();
        this.view = view;
    }

    Boolean isProductionBuild() {
        return BuildConfig.FLAVOR.equals(UiKitConstants.ENVIRONMENT_PRODUCTION);
    }

    void startUobEzpayPayment() {
        String snapToken = getMidtransSDK().readAuthenticationToken();

        startUobEzpayDeeplinkPayment(snapToken);
    }

    private void startUobEzpayDeeplinkPayment(String snapToken) {
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

    void getPaymentStatus() {
        String snapToken = getMidtransSDK().readAuthenticationToken();
        getMidtransSDK().getTransactionStatus(snapToken, new GetTransactionStatusCallback() {
            @Override
            public void onSuccess(TransactionStatusResponse response) {
                if (response != null && !isPaymentPending(response)) {
                    transactionResponse = convertTransactionStatus(response);
                    view.onGetTransactionStatusSuccess(transactionResponse);
                }
            }

            @Override
            public void onFailure(TransactionStatusResponse response, String reason) {
                //do nothing
            }

            @Override
            public void onError(Throwable error) {
                view.onGetTransactionStatusError(error);
            }
        });
    }

    private boolean isPaymentPending(TransactionStatusResponse response) {
        String statusCode = response.getStatusCode();
        String transactionStatus = response.getTransactionStatus();

        return (!TextUtils.isEmpty(statusCode) && statusCode.equals(UiKitConstants.STATUS_CODE_201)
                || !TextUtils.isEmpty(transactionStatus) && transactionStatus.equalsIgnoreCase(UiKitConstants.STATUS_PENDING));
    }
}
