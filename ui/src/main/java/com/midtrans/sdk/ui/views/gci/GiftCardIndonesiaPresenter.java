package com.midtrans.sdk.ui.views.gci;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by rakawm on 4/6/17.
 */

public class GiftCardIndonesiaPresenter extends BasePaymentPresenter {

    private final GiftCardIndonesiaView view;
    private PaymentResult<GiftCardPaymentResponse> paymentResult;

    public GiftCardIndonesiaPresenter(GiftCardIndonesiaView view) {
        this.view = view;
    }

    public void startPayment(String cardNumber, String password) {
        MidtransCore.getInstance().paymentUsingGiftCard(MidtransUi.getInstance().getTransaction().token, cardNumber, password, new MidtransCoreCallback<GiftCardPaymentResponse>() {
            @Override
            public void onSuccess(GiftCardPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onGiftCardIndonesiaSuccess(object);
            }

            @Override
            public void onFailure(GiftCardPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                view.onGiftCardIndonesiaFailure(object);
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                view.onGiftCardIndonesiaError(throwable.getMessage());
            }
        });
    }

    public PaymentResult<GiftCardPaymentResponse> getPaymentResult() {
        return paymentResult;
    }
}
