package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.snap.params.TelkomselCashPaymentParams;

/**
 * Created by ziahaqi on 8/1/16.
 */
public class TelkomselEcashPaymentRequest extends BasePaymentRequest {

    @SerializedName("payment_params")
    private TelkomselCashPaymentParams paymentParams;

    public TelkomselEcashPaymentRequest(String paymentType, TelkomselCashPaymentParams paymentParams) {
        super(paymentType);
        this.paymentParams = paymentParams;
    }
}
