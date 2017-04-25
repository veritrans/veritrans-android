package com.midtrans.sdk.ui.views.ebanking.cimb_clicks;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/10/17.
 */

public class CimbClicksPresenter extends BasePaymentPresenter {
    private final CimbClicksView view;
    private PaymentResult<CimbClicksPaymentResponse> paymentResult;

    public CimbClicksPresenter(CimbClicksView view) {
        this.view = view;
    }

    public void startPayment() {
        MidtransCore.getInstance().paymentUsingCimbClicks(MidtransUi.getInstance().getTransaction().token, new MidtransCoreCallback<CimbClicksPaymentResponse>() {
            @Override
            public void onSuccess(CimbClicksPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onCimbClicksSuccess(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_PENDING);
            }

            @Override
            public void onFailure(CimbClicksPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onCimbClicksFailure(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                view.onCimbClicksError(throwable.getMessage());

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }
        });
    }

    public PaymentResult<CimbClicksPaymentResponse> getPaymentResult() {
        return paymentResult;
    }
}
