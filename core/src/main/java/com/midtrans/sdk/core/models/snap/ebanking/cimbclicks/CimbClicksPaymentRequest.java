package com.midtrans.sdk.core.models.snap.ebanking.cimbclicks;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;

/**
 * Created by rakawm on 1/25/17.
 */

public class CimbClicksPaymentRequest extends BasePaymentRequest {
    public static final String TYPE = "cimb_clicks";

    public CimbClicksPaymentRequest() {
        super(TYPE);
    }
}
