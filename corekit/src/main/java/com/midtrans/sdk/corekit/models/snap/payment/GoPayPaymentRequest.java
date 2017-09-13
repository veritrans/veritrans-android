package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.snap.params.GoPayPaymentParams;

/**
 * Created by ziahaqi on 9/7/17.
 */

public class GoPayPaymentRequest extends BasePaymentRequest{

    @SerializedName("payment_params")
    private GoPayPaymentParams paymentParams;

    public GoPayPaymentRequest(GoPayPaymentParams paymentParams, String paymentType) {
        super(paymentType);
        this.paymentParams = paymentParams;
    }
}
