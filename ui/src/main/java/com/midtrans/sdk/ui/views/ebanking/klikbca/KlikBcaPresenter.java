package com.midtrans.sdk.ui.views.ebanking.klikbca;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/7/17.
 */

public class KlikBcaPresenter extends BasePaymentPresenter {
    private final KlikBcaView view;
    private KlikBcaPaymentResponse paymentResponse;

    public KlikBcaPresenter(KlikBcaView view) {
        this.view = view;
    }

    public void startPayment(String userId) {
        MidtransCore.getInstance().paymentUsingKlikBca(MidtransUi.getInstance().getTransaction().token, userId, new MidtransCoreCallback<KlikBcaPaymentResponse>() {
            @Override
            public void onSuccess(KlikBcaPaymentResponse object) {
                paymentResponse = object;
                view.onKlikBcaSuccess(paymentResponse);
            }

            @Override
            public void onFailure(KlikBcaPaymentResponse object) {
                view.onKlikBcaFailure("Failed to pay using KlikBCA");
            }

            @Override
            public void onError(Throwable throwable) {
                view.onKlikBcaFailure(throwable.getMessage());
            }
        });
    }

    public PaymentResult<KlikBcaPaymentResponse> getPaymentResult() {
        return new PaymentResult<>(paymentResponse);
    }
}
