package com.midtrans.sdk.coreflow.models.snap.payment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 8/1/16.
 */
public class IndosatDompetkuPaymentRequest extends BasePaymentRequest {

    @SerializedName("msisdn")
    private String msisdn;

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }


    public IndosatDompetkuPaymentRequest(String token, String msisdn, String paymentType) {
        this.transactionId = token;
        this.msisdn = msisdn;
        this.paymentType = paymentType;
    }
}
