package com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay;

import java.io.Serializable;

/**
 * Created by rakawm on 1/24/17.
 */

public class BcaKlikpayRedirectData implements Serializable {
    public final String url;
    public final String method;
    public final BcaKlikpayRedirectParams params;

    public BcaKlikpayRedirectData(String url, String method, BcaKlikpayRedirectParams params) {
        this.url = url;
        this.method = method;
        this.params = params;
    }
}
