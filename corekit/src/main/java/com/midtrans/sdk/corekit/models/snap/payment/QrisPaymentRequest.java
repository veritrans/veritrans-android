package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;

public class QrisPaymentRequest {
    @SerializedName("payment_type")
    protected String paymentType;

    @SerializedName("payment_params")
    protected QrisPaymentParameter paymentParam;

    public QrisPaymentRequest(String paymentType, QrisPaymentParameter paymentParam) {
        this.paymentType = paymentType;
        this.paymentParam = paymentParam;
    }
}
