package com.midtrans.sdk.ui.views.ewallet.indosatdompetku;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentResponse;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by ziahaqi on 4/10/17.
 */

public class IndosatDompetkuPresenter extends BasePaymentPresenter {

    private IndosatDompetkuView view;
    private PaymentResult<IndosatDompetkuPaymentResponse> paymentResult;

    public IndosatDompetkuPresenter(IndosatDompetkuView view) {
        this.view = view;
    }


    public void startPayment(String phoneNumber) {
        MidtransCore.getInstance().paymentUsingIndosatDompetku(midtransUi.getCheckoutToken(), phoneNumber, new MidtransCoreCallback<IndosatDompetkuPaymentResponse>() {
            @Override
            public void onSuccess(IndosatDompetkuPaymentResponse response) {
                paymentResult = new PaymentResult<>(response);
                view.onPaymentSuccess(response);

                trackEvent(AnalyticsEventName.PAGE_STATUS_SUCCESS);
            }

            @Override
            public void onFailure(IndosatDompetkuPaymentResponse response) {
                paymentResult = new PaymentResult<>(response);
                view.onPaymentFailure(response);

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                view.onPaymentError(throwable.getMessage());

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }
        });
    }


    public PaymentResult<IndosatDompetkuPaymentResponse> getPaymentResult() {
        return paymentResult;
    }

    public boolean isShowPaymentStatus() {
        return midtransUi.getCustomSetting().isShowPaymentStatus();
    }
}
