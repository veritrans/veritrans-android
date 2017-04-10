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
    private EpayBriPaymentResponse paymentResponse;

    public EpayBriPresenter(EpayBriView view) {
        this.view = view;
    }

    public void startPayment() {
        MidtransCore.getInstance().paymentUsingEpayBri(MidtransUi.getInstance().getTransaction().token, new MidtransCoreCallback<EpayBriPaymentResponse>() {
            @Override
            public void onSuccess(EpayBriPaymentResponse object) {
                paymentResponse = object;
                view.onEpayBriSuccess(object);
            }

            @Override
            public void onFailure(EpayBriPaymentResponse object) {
                view.onEpayBriFailure("Failed to pay using Epay BRI.");
            }

            @Override
            public void onError(Throwable throwable) {
                view.onEpayBriFailure(throwable.getMessage());
            }
        });
    }

    public PaymentResult<EpayBriPaymentResponse> getPaymentResult() {
        return new PaymentResult<>(paymentResponse);
    }
}
