package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.snap.params.KlikBcaPaymentParams;

/**
 * @author rakawm
 */
public class KlikBCAPaymentRequest extends BasePaymentRequest {
    @SerializedName("payment_params")
    private KlikBcaPaymentParams paymentParams;

    public KlikBCAPaymentRequest(String paymentType, KlikBcaPaymentParams paymentParams) {
        super(paymentType);
        this.paymentParams = paymentParams;
    }

    public KlikBcaPaymentParams getPaymentParams() {
        return paymentParams;
    }
}
