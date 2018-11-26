package com.midtrans.sdk.corekit.core.snap.model.pay.request;

import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.base.model.BasePaymentRequest;

import java.io.Serializable;

public class PaymentRequest extends BasePaymentRequest implements Serializable {
    @SerializedName("customer_details")
    private CustomerDetailPayRequest customerDetails;

    public PaymentRequest(String paymentType,
                          CustomerDetailPayRequest customerDetails) {
        super(paymentType);
        this.customerDetails = customerDetails;
    }

    public CustomerDetailPayRequest getCustomerDetails() {
        return customerDetails;
    }
}