package com.midtrans.sdk.corekit.base.model;

import com.google.gson.annotations.SerializedName;

public class BasePaymentRequest {
    @SerializedName("payment_type")
    String paymentType;

    public BasePaymentRequest(String paymentType) {
        this.paymentType = paymentType;
    }
}