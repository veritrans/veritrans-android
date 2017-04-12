package com.midtrans.sdk.ui.views.ewallet.xltunai;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentResponse;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by ziahaqi on 4/7/17.
 */

public class XlTunaiPresenter extends BasePaymentPresenter {

    private final XLTunaiPaymentView view;
    private PaymentResult<XlTunaiPaymentResponse> paymentResult;

    public XlTunaiPresenter(XLTunaiPaymentView view) {
        this.view = view;
    }


    public void startPayment() {
        MidtransCore.getInstance().paymentUsingXlTunai(midtransUiSdk.getCheckoutToken(), new MidtransCoreCallback<XlTunaiPaymentResponse>() {
            @Override
            public void onSuccess(XlTunaiPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onPaymentSuccess(object);
            }

            @Override
            public void onFailure(XlTunaiPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onPaymentFailure(object);
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                view.onPaymentError(throwable.getMessage());
            }
        });
    }

    public PaymentResult<XlTunaiPaymentResponse> getPaymentResult() {
        return paymentResult;
    }
}
