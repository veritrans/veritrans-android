package com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.BasePaymentRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.promo.PromoDetails;

public class CreditCardPaymentRequest extends BasePaymentRequest {
    @SerializedName("payment_params")
    private CreditCardPaymentParams paymentParams;
    @SerializedName("promo_details")
    private PromoDetails promoDetails;
    @SerializedName("customer_details")
    private CustomerDetailPayRequest customerDetails;

    public CreditCardPaymentRequest(String paymentType, CreditCardPaymentParams paymentParams, CustomerDetailPayRequest customerDetails) {
        super(paymentType);
        this.paymentParams = paymentParams;
        this.customerDetails = customerDetails;
    }

    public PromoDetails getPromoDetails() {
        return this.promoDetails;
    }

    public void setPromoDetails(PromoDetails promoDetails) {
        this.promoDetails = promoDetails;
    }

    public CreditCardPaymentParams getPaymentParams() {
        return this.paymentParams;
    }

    public CustomerDetailPayRequest getCustomerDetails() {
        return this.customerDetails;
    }
}