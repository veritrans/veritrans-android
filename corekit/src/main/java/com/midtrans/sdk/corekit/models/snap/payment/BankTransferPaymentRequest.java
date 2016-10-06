package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
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
