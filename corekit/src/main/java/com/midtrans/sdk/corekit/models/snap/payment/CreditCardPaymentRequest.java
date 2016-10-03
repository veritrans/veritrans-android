package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.snap.params.CreditCardPaymentParams;

/**
 * @author rakawm
 */
public class CreditCardPaymentRequest extends BasePaymentRequest {
    @SerializedName("payment_params")
    private CreditCardPaymentParams paymentParams;

    @SerializedName("customer_details")
    private CustomerDetailRequest customerDetails;

    public CreditCardPaymentRequest(String paymentType, CreditCardPaymentParams paymentParams, CustomerDetailRequest customerDetails) {
        super(paymentType);
        this.paymentParams = paymentParams;
        this.customerDetails = customerDetails;
    }

    public CreditCardPaymentParams getPaymentParams() {
        return paymentParams;
    }

    public CustomerDetailRequest getCustomerDetails() {
        return customerDetails;
    }
}
