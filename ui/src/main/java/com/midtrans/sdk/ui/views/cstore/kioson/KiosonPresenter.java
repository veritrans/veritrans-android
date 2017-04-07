package com.midtrans.sdk.ui.views.cstore.kioson;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/6/17.
 */

public class KiosonPresenter extends BasePaymentPresenter {
    private final KiosonView view;
    private KiosonPaymentResponse response;

    public KiosonPresenter(KiosonView view) {
        this.view = view;
    }

    public void startPayment() {
        MidtransCore.getInstance().paymentUsingKioson(MidtransUi.getInstance().getTransaction().token, new MidtransCoreCallback<KiosonPaymentResponse>() {
            @Override
            public void onSuccess(KiosonPaymentResponse object) {
                response = object;
                view.onKiosonPaymentSuccess(object);
            }

            @Override
            public void onFailure(KiosonPaymentResponse object) {
                view.onKiosonPaymentFailure("Failed to pay using Kioson");
            }

            @Override
            public void onError(Throwable throwable) {
                view.onKiosonPaymentFailure(throwable.getMessage());
            }
        });
    }

    public PaymentResult<KiosonPaymentResponse> getPaymentResult() {
        return new PaymentResult<>(response);
    }
}
