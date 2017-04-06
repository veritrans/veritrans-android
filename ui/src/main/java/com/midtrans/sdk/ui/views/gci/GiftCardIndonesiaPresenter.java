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
    private GiftCardPaymentResponse response;

    public GiftCardIndonesiaPresenter(GiftCardIndonesiaView view) {
        this.view = view;
    }

    public void startPayment(String cardNumber, String password) {
        MidtransCore.getInstance().paymentUsingGiftCard(MidtransUi.getInstance().getTransaction().token, cardNumber, password, new MidtransCoreCallback<GiftCardPaymentResponse>() {
            @Override
            public void onSuccess(GiftCardPaymentResponse object) {
                response = object;
                view.onGiftCardIndonesiaSuccess(object);
            }

            @Override
            public void onFailure(GiftCardPaymentResponse object) {
                view.onGiftCardIndonesiaFailure("Failed to pay using Gift Card");
            }

            @Override
            public void onError(Throwable throwable) {
                view.onGiftCardIndonesiaFailure(throwable.getMessage());
            }
        });
    }

    public PaymentResult<GiftCardPaymentResponse> getPaymentResult() {
        return new PaymentResult<>(response);
    }
}
