package com.midtrans.sdk.corekit.core.snap.model.pay.request.va;

import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.base.model.BasePaymentRequest;

import java.io.Serializable;

public class BankTransferPaymentRequest extends BasePaymentRequest implements Serializable {

    @SerializedName("customer_details")
    private CustomerDetailRequest customerDetails;

    public BankTransferPaymentRequest(String paymentType,
                                      CustomerDetailRequest customerDetails) {
        super(paymentType);
        this.customerDetails = customerDetails;
    }

    public CustomerDetailRequest getCustomerDetails() {
        return customerDetails;
    }
}