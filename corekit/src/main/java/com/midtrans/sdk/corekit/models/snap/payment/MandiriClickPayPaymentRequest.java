package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.snap.params.MandiriClickPayPaymentParams;

/**
 * Created by ziahaqi on 8/1/16.
 */
public class MandiriClickPayPaymentRequest extends BasePaymentRequest {

    @SerializedName("payment_params")
    private MandiriClickPayPaymentParams paymentParams;

    public MandiriClickPayPaymentRequest(String paymentType, MandiriClickPayPaymentParams paymentParams) {
        super(paymentType);
        this.paymentParams = paymentParams;
    }
}
