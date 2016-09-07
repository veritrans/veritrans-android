package com.midtrans.sdk.coreflow.models.snap.payment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 8/1/16.
 */
public class TelkomselEcashPaymentRequest extends BasePaymentRequest {

    //customer phone number
    @SerializedName("customer")
    private String customer;

    public TelkomselEcashPaymentRequest(String token, String customerPhoneNumber) {
        this.transactionId = token;
        this.customer = customerPhoneNumber;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
