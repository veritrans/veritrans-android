package com.midtrans.sdk.core.models.snap.conveniencestore.kioson;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;

/**
 * Created by rakawm on 1/26/17.
 */

public class KiosonPaymentRequest extends BasePaymentRequest {
    public static final String TYPE = "kioson";

    public KiosonPaymentRequest() {
        super(TYPE);
    }
}
