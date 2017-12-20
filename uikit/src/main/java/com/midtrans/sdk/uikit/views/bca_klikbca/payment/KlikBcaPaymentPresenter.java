package com.midtrans.sdk.uikit.views.bca_klikbca.payment;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;

/**
 * Created by ziahaqi on 9/18/17.
 */

public class KlikBcaPaymentPresenter extends BasePaymentPresenter<BasePaymentView> {

    public KlikBcaPaymentPresenter(BasePaymentView view) {
        super();
        this.view = view;
    }

    public void startPayment(String userId) {
        getMidtransSDK().paymentUsingKlikBCA(getMidtransSDK().readAuthenticationToken(), userId,
                new TransactionCallback() {
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
