package com.midtrans.sdk.ui.views.cstore.indomaret;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/6/17.
 */

public class IndomaretPresenter extends BasePaymentPresenter {

    private final IndomaretView view;
    private PaymentResult<IndomaretPaymentResponse> paymentResult;

    public IndomaretPresenter(IndomaretView view) {
        this.view = view;
    }

    public void startPayment() {
        MidtransCore.getInstance().paymentUsingIndomaret(MidtransUi.getInstance().getTransaction().token, new MidtransCoreCallback<IndomaretPaymentResponse>() {
            @Override
            public void onSuccess(IndomaretPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onIndomaretPaymentSuccess(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_PENDING);
            }

            @Override
            public void onFailure(IndomaretPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onIndomaretPaymentFailure(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                view.onIndomaretPaymentError(throwable.getMessage());

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }
        });
    }

    public PaymentResult<IndomaretPaymentResponse> getPaymentResult() {
        return paymentResult;
    }
}
