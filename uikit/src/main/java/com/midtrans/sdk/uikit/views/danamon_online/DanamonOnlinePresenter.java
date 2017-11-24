package com.midtrans.sdk.uikit.views.danamon_online;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;

/**
 * Created by ziahaqi on 9/13/17.
 */

public class DanamonOnlinePresenter extends BasePaymentPresenter<BasePaymentView> {

    public DanamonOnlinePresenter(BasePaymentView view) {
        super();
        this.view = view;
    }

    public void startPayment() {
        String snapToken = getMidtransSDK().readAuthenticationToken();
        getMidtransSDK().paymentUsingDanamonOnline(snapToken, new TransactionCallback() {
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
