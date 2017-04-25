package com.midtrans.sdk.ui.views.ebanking.mandiri_ecash;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/10/17.
 */

public class MandiriEcashPresenter extends BasePaymentPresenter {
    private final MandiriEcashView view;
    private PaymentResult<MandiriECashPaymentResponse> paymentResult;

    public MandiriEcashPresenter(MandiriEcashView view) {
        this.view = view;
    }

    public void startPayment() {
        MidtransCore.getInstance().paymentUsingMandiriECash(MidtransUi.getInstance().getTransaction().token, new MidtransCoreCallback<MandiriECashPaymentResponse>() {
            @Override
            public void onSuccess(MandiriECashPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onMandiriEcashSuccess(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_PENDING);
            }

            @Override
            public void onFailure(MandiriECashPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onMandiriEcashFailure(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                view.onMandiriEcashError(throwable.getMessage());

                trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
            }
        });
    }

    public PaymentResult<MandiriECashPaymentResponse> getPaymentResult() {
        return paymentResult;
    }
}
