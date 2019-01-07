package com.midtrans.sdk.corekit.core.api.snap.model.pay.request.telkomsel;

import com.google.gson.annotations.SerializedName;

public class TelkomselPaymentParams {
    @SerializedName("customer")
    public String customerNumber;

    public TelkomselPaymentParams(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }
}