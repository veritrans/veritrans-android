package com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;

/**
 * Created by rakawm on 1/26/17.
 */

public class IndosatDompetkuPaymentRequest extends BasePaymentRequest {
    public static final String TYPE = "indosat_dompetku";

    public final IndosatDompetkuPaymentParams paymentParams;

    public IndosatDompetkuPaymentRequest(IndosatDompetkuPaymentParams paymentParams) {
        super(TYPE);
        this.paymentParams = paymentParams;
    }
}
