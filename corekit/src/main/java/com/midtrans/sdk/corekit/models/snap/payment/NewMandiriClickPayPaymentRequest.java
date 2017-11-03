package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.snap.params.NewMandiriClickPaymentParams;

/**
 * Created by ziahaqi on 10/23/17.
 */

public class NewMandiriClickPayPaymentRequest extends BasePaymentRequest {

    @SerializedName("payment_params")
    private NewMandiriClickPaymentParams paymentParams;

    public NewMandiriClickPayPaymentRequest(String paymentType, NewMandiriClickPaymentParams paymentParams) {
        super(paymentType);
        this.paymentParams = paymentParams;
    }
}
