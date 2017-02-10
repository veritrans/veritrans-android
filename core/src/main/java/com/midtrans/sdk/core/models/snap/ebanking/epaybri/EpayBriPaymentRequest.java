package com.midtrans.sdk.core.models.snap.ebanking.epaybri;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;

/**
 * Created by rakawm on 1/25/17.
 */

public class EpayBriPaymentRequest extends BasePaymentRequest {
    public static final String TYPE = "bri_epay";

    public EpayBriPaymentRequest() {
        super(TYPE);
    }
}
