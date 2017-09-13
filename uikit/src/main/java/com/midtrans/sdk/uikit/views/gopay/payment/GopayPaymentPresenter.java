package com.midtrans.sdk.uikit.views.gopay.payment;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;

/**
 * Created by ziahaqi on 9/7/17.
 */

public class GopayPaymentPresenter extends BasePaymentPresenter<GoPayPaymentView> {

    private TransactionResponse transactionResponse;

    public GopayPaymentPresenter(GoPayPaymentView view) {
        this.view = view;
    }

    public void startGoPayPayment(String phoneNumber) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        String snapToken = midtransSDK.readAuthenticationToken();
        midtransSDK.paymentUsingGoPay(snapToken, phoneNumber, new TransactionCallback() {
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

    public TransactionResponse getTransactionResponse() {
        return transactionResponse;
    }
}
