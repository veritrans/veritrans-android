package com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick;

import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.BasePaymentRequest;

public class MandiriClickpayPaymentRequest extends BasePaymentRequest {
    @SerializedName("payment_params")
    private MandiriClickpayParams paymentParams;

    public MandiriClickpayPaymentRequest(String paymentType, MandiriClickpayParams paymentParams) {
        super(paymentType);
        this.paymentParams = paymentParams;
    }

    public MandiriClickpayParams getPaymentParams() {
        return paymentParams;
    }
}