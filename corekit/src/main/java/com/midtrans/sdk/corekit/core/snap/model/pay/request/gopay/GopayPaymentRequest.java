package com.midtrans.sdk.corekit.core.snap.model.pay.request.gopay;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.BasePaymentRequest;

public class GopayPaymentRequest extends BasePaymentRequest {
    @SerializedName("payment_params")
    private GopayPaymentParams gopayPaymentParams;

    public GopayPaymentRequest(String paymentType, String gopayAccountNumber) {
        super(paymentType);
        this.gopayPaymentParams = new GopayPaymentParams(gopayAccountNumber);
    }

    public String getGopayAccountNumber() {
        return gopayPaymentParams.getAccountNumber();
    }
}
