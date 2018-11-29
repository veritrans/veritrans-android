package com.midtrans.sdk.corekit.core.snap.model.pay.request.telkomsel;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.BasePaymentRequest;

public class TelkomselCashPaymentRequest extends BasePaymentRequest {
    @SerializedName("payment_params")
    public TelkomselPaymentParams telkomselPaymentParams;

    public TelkomselCashPaymentRequest(String paymentType, String customerNumber) {
        super(paymentType);
        this.telkomselPaymentParams = new TelkomselPaymentParams(customerNumber);
    }

    public String getCustomerNumber(){
        return telkomselPaymentParams.getCustomerNumber();
    }
}
