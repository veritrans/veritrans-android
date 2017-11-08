package com.midtrans.sdk.uikit.views.bca_klikpay;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;

/**
 * Created by ziahaqi on 9/19/17.
 */

public class BcaKlikPayPaymentPresenter extends BasePaymentPresenter<BasePaymentView> {

    public BcaKlikPayPaymentPresenter(BasePaymentView view) {
        super();
        this.view = view;
    }

    public void startPayment() {
        getMidtransSDK().paymentUsingBCAKlikpay(getMidtransSDK().readAuthenticationToken(),
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
