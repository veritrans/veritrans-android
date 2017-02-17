package com.midtrans.sdk.core.models.snap.gci;

/**
 * Created by rakawm on 1/26/17.
 */

public class GiftCardPaymentParams {
    public final String cardNumber;
    public final String pin;

    public GiftCardPaymentParams(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }
}
