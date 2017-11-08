package com.midtrans.sdk.uikit.views.xl_tunai.payment;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;

/**
 * Created by Fajar on 30/10/17.
 */

public class XlTunaiPaymentPresenter extends BasePaymentPresenter<BasePaymentView> {
    public XlTunaiPaymentPresenter(BasePaymentView view) {
        super();
        this.view = view;
    }

    public void startPayment() {
        getMidtransSDK().paymentUsingXLTunai(getMidtransSDK().readAuthenticationToken(),
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
