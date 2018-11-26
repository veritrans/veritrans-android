package com.midtrans.sdk.corekit.base.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BasePaymentRequest implements Serializable {
    @SerializedName("payment_type")
    String paymentType;

    public BasePaymentRequest(String paymentType) {
        this.paymentType = paymentType;
    }
}