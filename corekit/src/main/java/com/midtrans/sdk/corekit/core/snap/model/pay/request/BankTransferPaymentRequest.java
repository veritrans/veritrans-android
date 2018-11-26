package com.midtrans.sdk.corekit.core.snap.model.pay.request;

import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.base.model.BasePaymentRequest;

public class BankTransferPaymentRequest extends BasePaymentRequest {

    @SerializedName("customer_details")
    private CustomerDetailRequest customerDetails;

    public BankTransferPaymentRequest(String paymentType, CustomerDetailRequest customerDetails) {
        super(paymentType);
        this.customerDetails = customerDetails;
    }

    public CustomerDetailRequest getCustomerDetails() {
        return customerDetails;
    }
}