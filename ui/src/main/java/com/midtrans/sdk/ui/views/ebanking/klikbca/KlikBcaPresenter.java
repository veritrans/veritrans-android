package com.midtrans.sdk.ui.views.ebanking.klikbca;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/7/17.
 */

public class KlikBcaPresenter extends BasePaymentPresenter {
    private final KlikBcaView view;
    private PaymentResult<KlikBcaPaymentResponse> paymentResult;

    public KlikBcaPresenter(KlikBcaView view) {
        this.view = view;
    }

    public void startPayment(String userId) {
        MidtransCore.getInstance().paymentUsingKlikBca(MidtransUi.getInstance().getTransaction().token, userId, new MidtransCoreCallback<KlikBcaPaymentResponse>() {
            @Override
            public void onSuccess(KlikBcaPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onKlikBcaSuccess(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_PENDING);
            }

            @Override
            public void onFailure(KlikBcaPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onKlikBcaFailure(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                view.onKlikBcaError(throwable.getMessage());

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }
        });
    }

    public PaymentResult<KlikBcaPaymentResponse> getPaymentResult() {
        return paymentResult;
    }
}
