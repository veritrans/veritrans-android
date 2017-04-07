package com.midtrans.sdk.ui.views.cstore.indomaret;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/6/17.
 */

public class IndomaretPresenter extends BasePaymentPresenter {

    private final IndomaretView view;
    private IndomaretPaymentResponse response;

    public IndomaretPresenter(IndomaretView view) {
        this.view = view;
    }

    public void startPayment() {
        MidtransCore.getInstance().paymentUsingIndomaret(MidtransUi.getInstance().getTransaction().token, new MidtransCoreCallback<IndomaretPaymentResponse>() {
            @Override
            public void onSuccess(IndomaretPaymentResponse object) {
                response = object;
                view.onIndomaretPaymentSuccess(object);
            }

            @Override
            public void onFailure(IndomaretPaymentResponse object) {
                view.onIndomaretPaymentFailure("Failed to pay using Indomaret");
            }

            @Override
            public void onError(Throwable throwable) {
                view.onIndomaretPaymentFailure(throwable.getMessage());
            }
        });
    }

    public PaymentResult<IndomaretPaymentResponse> getPaymentResult() {
        return new PaymentResult<>(response);
    }
}
