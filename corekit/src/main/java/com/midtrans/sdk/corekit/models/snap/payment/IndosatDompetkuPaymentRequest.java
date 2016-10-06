package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.snap.params.IndosatDompetkuPaymentParams;

/**
 * Created by ziahaqi on 8/1/16.
 */
public class IndosatDompetkuPaymentRequest extends BasePaymentRequest {

    @SerializedName("payment_params")
    IndosatDompetkuPaymentParams paymentParams;

    public IndosatDompetkuPaymentRequest(String paymentType, IndosatDompetkuPaymentParams paymentParams) {
        super(paymentType);
        this.paymentParams = paymentParams;
    }
}
