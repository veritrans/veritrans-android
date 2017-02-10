package com.midtrans.sdk.core.models.snap;

/**
 * Created by rakawm on 10/19/16.
 */

public class BasePaymentRequest {

    public final String paymentType;

    public BasePaymentRequest(String paymentType) {
        this.paymentType = paymentType;
    }
}
