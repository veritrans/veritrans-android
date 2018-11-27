package com.midtrans.sdk.corekit.core.snap.model.pay.request.klikbca;

import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.core.snap.model.pay.request.BasePaymentRequest;

public class KlikBcaPaymentRequest extends BasePaymentRequest {
    @SerializedName("payment_params")
    private KlikBcaPaymentParams klikBcaPaymentParams;

    public KlikBcaPaymentRequest(String paymentType, String klikBcaUserId) {
        super(paymentType);
        this.klikBcaPaymentParams = new KlikBcaPaymentParams(klikBcaUserId);
    }

    public String getKlikBcaUserId() {
        return klikBcaPaymentParams.getUserId();
    }
}
