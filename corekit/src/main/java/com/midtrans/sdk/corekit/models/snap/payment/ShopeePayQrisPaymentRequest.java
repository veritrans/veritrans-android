package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;

public class ShopeePayQrisPaymentRequest{
    @SerializedName("payment_type")
    protected String paymentType;

    @SerializedName("payment_params")
    protected QrisPaymentParameter paymentParam;

    public ShopeePayQrisPaymentRequest(String paymentType, QrisPaymentParameter paymentParam) {
        this.paymentType = paymentType;
        this.paymentParam = paymentParam;
    }
}
