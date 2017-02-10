package com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;

/**
 * Created by rakawm on 1/25/17.
 */

public class MandiriClickpayPaymentRequest extends BasePaymentRequest {
    public static final String TYPE = "mandiri_clickpay";

    public final MandiriClickpayPaymentParams paymentParams;

    public MandiriClickpayPaymentRequest(MandiriClickpayPaymentParams paymentParams) {
        super(TYPE);
        this.paymentParams = paymentParams;
    }
}
