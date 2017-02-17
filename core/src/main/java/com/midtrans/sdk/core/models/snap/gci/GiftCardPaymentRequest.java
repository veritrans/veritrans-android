package com.midtrans.sdk.core.models.snap.gci;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;

/**
 * Created by rakawm on 1/26/17.
 */

public class GiftCardPaymentRequest extends BasePaymentRequest {
    public static final String TYPE = "gci";

    public final GiftCardPaymentParams paymentParams;

    public GiftCardPaymentRequest(GiftCardPaymentParams paymentParams) {
        super(TYPE);
        this.paymentParams = paymentParams;
    }
}
