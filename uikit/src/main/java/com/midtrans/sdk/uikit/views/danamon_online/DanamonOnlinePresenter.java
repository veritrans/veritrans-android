package com.midtrans.sdk.uikit.views.danamon_online;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;

/**
 * Created by ziahaqi on 9/13/17.
 */

public class DanamonOnlinePresenter extends BasePaymentPresenter<BasePaymentView> {

    private TransactionResponse transactionResponse;

    public DanamonOnlinePresenter(BasePaymentView view) {
        this.view = view;
    }

    public void startPayment() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        String snapToken = midtransSDK.readAuthenticationToken();
        midtransSDK.paymentUsingDanamonOnline(snapToken, new TransactionCallback() {
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
        return this.transactionResponse;
    }
}
