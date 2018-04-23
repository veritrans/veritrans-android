package com.midtrans.sdk.uikit.views.gopay.payment;

import com.midtrans.sdk.corekit.callback.GetTransactionStatusCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.TransactionStatusResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;

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

                TransactionResponse newTransactionResponse = convertTransactionStatus(response);
                transactionResponse = newTransactionResponse;

                view.onGetTransactionStatusSuccess(transactionResponse);
            }

            @Override
            public void onFailure(TransactionStatusResponse response, String reason) {

                TransactionResponse newTransactionResponse = convertTransactionStatus(response);
                transactionResponse = newTransactionResponse;

                view.onGetTransactionStatusFailure(transactionResponse);
            }

            @Override
            public void onError(Throwable error) {
                view.onGetTransactionStatusError(error);
            }
        });
    }
}
