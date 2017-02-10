package com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;

/**
 * Created by rakawm on 1/24/17.
 */

public class BcaKlikpayPaymentRequest extends BasePaymentRequest {
    public final static String TYPE = "bca_klikpay";

    public BcaKlikpayPaymentRequest() {
        super(TYPE);
    }
}
