package com.midtrans.sdk.ui.views.ebanking.mandiri_ecash;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/10/17.
 */

public class MandiriEcashPresenter extends BasePaymentPresenter {
    private final MandiriEcashView view;
    private MandiriECashPaymentResponse paymentResponse;

    public MandiriEcashPresenter(MandiriEcashView view) {
        this.view = view;
    }

    public void startPayment() {
        MidtransCore.getInstance().paymentUsingMandiriECash(MidtransUi.getInstance().getTransaction().token, new MidtransCoreCallback<MandiriECashPaymentResponse>() {
            @Override
            public void onSuccess(MandiriECashPaymentResponse object) {
                paymentResponse = object;
                view.onMandiriEcashSuccess(object);
            }

            @Override
            public void onFailure(MandiriECashPaymentResponse object) {
                view.onMandiriEcashFailure("Failed to pay using Mandiri E Cash");
            }

            @Override
            public void onError(Throwable throwable) {
                view.onMandiriEcashFailure(throwable.getMessage());
            }
        });
    }

    public PaymentResult<MandiriECashPaymentResponse> getPaymentResult() {
        return new PaymentResult<>(paymentResponse);
    }
}
