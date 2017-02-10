package com.midtrans.sdk.core.models.snap.ewallet.tcash;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;

/**
 * Created by rakawm on 1/26/17.
 */

public class TelkomselCashPaymentRequest extends BasePaymentRequest {
    public static final String TYPE = "telkomsel_cash";

    public final TelkomselCashPaymentParams paymentParams;

    public TelkomselCashPaymentRequest(TelkomselCashPaymentParams paymentParams) {
        super(TYPE);
        this.paymentParams = paymentParams;
    }
}
