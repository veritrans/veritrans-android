package com.midtrans.sdk.ui.views.cstore.kioson;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/6/17.
 */

public class KiosonPresenter extends BasePaymentPresenter {
    private final KiosonView view;
    private PaymentResult<KiosonPaymentResponse> paymentResult;

    public KiosonPresenter(KiosonView view) {
        this.view = view;
    }

    public void startPayment() {
        MidtransCore.getInstance().paymentUsingKioson(MidtransUi.getInstance().getTransaction().token, new MidtransCoreCallback<KiosonPaymentResponse>() {
            @Override
            public void onSuccess(KiosonPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onKiosonPaymentSuccess(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_PENDING);
            }

            @Override
            public void onFailure(KiosonPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onKiosonPaymentFailure(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                view.onKiosonPaymentError(throwable.getMessage());

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }
        });
    }

    public PaymentResult<KiosonPaymentResponse> getPaymentResult() {
        return paymentResult;
    }
}
