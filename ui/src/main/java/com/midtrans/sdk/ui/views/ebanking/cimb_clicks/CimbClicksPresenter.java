package com.midtrans.sdk.ui.views.ebanking.cimb_clicks;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/10/17.
 */

public class CimbClicksPresenter extends BasePaymentPresenter {
    private final CimbClicksView view;
    private CimbClicksPaymentResponse paymentResponse;

    public CimbClicksPresenter(CimbClicksView view) {
        this.view = view;
    }

    public void startPayment() {
        MidtransCore.getInstance().paymentUsingCimbClicks(MidtransUi.getInstance().getTransaction().token, new MidtransCoreCallback<CimbClicksPaymentResponse>() {
            @Override
            public void onSuccess(CimbClicksPaymentResponse object) {
                paymentResponse = object;
                view.onCimbClicksSuccess(object);
            }

            @Override
            public void onFailure(CimbClicksPaymentResponse object) {
                view.onCimbClicksFailure("Failed to pay using CIMB Clicks.");
            }

            @Override
            public void onError(Throwable throwable) {
                view.onCimbClicksFailure(throwable.getMessage());
            }
        });
    }

    public PaymentResult<CimbClicksPaymentResponse> getPaymentResult() {
        return new PaymentResult<CimbClicksPaymentResponse>(paymentResponse);
    }
}
