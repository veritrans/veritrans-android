package com.midtrans.sdk.core.models.snap.ewallet.xltunai;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;

/**
 * Created by rakawm on 1/26/17.
 */

public class XlTunaiPaymentRequest extends BasePaymentRequest {
    public static final String TYPE = "xl_tunai";

    public XlTunaiPaymentRequest() {
        super(TYPE);
    }
}
