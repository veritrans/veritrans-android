package com.midtrans.sdk.core.models.snap.ebanking.klikbca;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;

/**
 * Created by rakawm on 1/24/17.
 */

public class KlikBcaPaymentRequest extends BasePaymentRequest {
    public final static String TYPE = "bca_klikbca";

    public final KlikBcaPaymentParams paymentParams;

    public KlikBcaPaymentRequest(KlikBcaPaymentParams paymentParams) {
        super(TYPE);
        this.paymentParams = paymentParams;
    }
}
