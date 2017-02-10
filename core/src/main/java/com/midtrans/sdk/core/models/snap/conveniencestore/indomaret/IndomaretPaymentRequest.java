package com.midtrans.sdk.core.models.snap.conveniencestore.indomaret;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;

/**
 * Created by rakawm on 1/26/17.
 */

public class IndomaretPaymentRequest extends BasePaymentRequest {
    public static final String TYPE = "indomaret";

    public IndomaretPaymentRequest() {
        super(TYPE);
    }
}
