package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.snap.params.GCIPaymentParams;

/**
 * Created by ziahaqi on 12/7/16.
 */

public class GCIPaymentRequest {

    @SerializedName("payment_params")
    private GCIPaymentParams paymentParams;

    @SerializedName("payment_type")
    private String paymentType;

    public GCIPaymentRequest(GCIPaymentParams paymentParams, String paymentType) {
        this.paymentParams = paymentParams;
        this.paymentType = paymentType;
    }

    public GCIPaymentParams getPaymentParams() {
        return paymentParams;
    }
}
