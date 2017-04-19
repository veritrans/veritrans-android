package com.midtrans.sdk.ui.views.ebanking.epay_bri;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.ebanking.epaybri.EpayBriPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/10/17.
 */

public class EpayBriPresenter extends BasePaymentPresenter {
    private final EpayBriView view;
    private PaymentResult<EpayBriPaymentResponse> paymentResult;

    public EpayBriPresenter(EpayBriView view) {
        this.view = view;
    }

    public void startPayment() {
        MidtransCore.getInstance().paymentUsingEpayBri(MidtransUi.getInstance().getTransaction().token, new MidtransCoreCallback<EpayBriPaymentResponse>() {
            @Override
            public void onSuccess(EpayBriPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onEpayBriSuccess(object);
            }

            @Override
            public void onFailure(EpayBriPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onEpayBriFailure(object);
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                view.onEpayBriError(throwable.getMessage());
            }
        });
    }

    public PaymentResult<EpayBriPaymentResponse> getPaymentResult() {
        return paymentResult;
    }
}
