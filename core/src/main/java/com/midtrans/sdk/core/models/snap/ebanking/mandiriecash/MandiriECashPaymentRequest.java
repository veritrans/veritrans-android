package com.midtrans.sdk.core.models.snap.ebanking.mandiriecash;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;

/**
 * Created by rakawm on 1/25/17.
 */

public class MandiriECashPaymentRequest extends BasePaymentRequest {
    public static final String TYPE = "mandiri_ecash";

    public MandiriECashPaymentRequest() {
        super(TYPE);
    }
}
