package com.midtrans.sdk.uikit.views.gopay.payment;

import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.callback.GetTransactionStatusCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.TransactionStatusResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

/**
 * Created by ziahaqi on 9/7/17.
 */

public class GopayPaymentPresenter extends BasePaymentPresenter<GoPayPaymentView> {

    public GopayPaymentPresenter(GoPayPaymentView view) {
        super();
        this.view = view;
    }

    public void startGoPayPayment() {
        String snapToken = getMidtransSDK().readAuthenticationToken();
        getMidtransSDK().paymentUsingGoPay(snapToken, new TransactionCallback() {
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

    public void getPaymentStatus() {
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
                // do nothing
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
