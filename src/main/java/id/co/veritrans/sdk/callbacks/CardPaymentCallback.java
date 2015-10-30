package id.co.veritrans.sdk.callbacks;

import id.co.veritrans.sdk.models.CardPaymentResponse;

public interface CardPaymentCallback extends Callback {
    public void onSuccess(CardPaymentResponse cardPaymentResponse);
}