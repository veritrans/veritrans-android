package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class BasePaymentRequest {

    @SerializedName("payment_type")
    protected String paymentType;

    public BasePaymentRequest(String paymentType) {
        this.paymentType = paymentType;
    }
}
