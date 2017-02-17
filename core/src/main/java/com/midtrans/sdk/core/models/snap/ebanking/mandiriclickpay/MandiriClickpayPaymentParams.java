package com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay;

/**
 * Created by rakawm on 1/25/17.
 */

public class MandiriClickpayPaymentParams {
    public final String mandiriCardNo;
    public final String input3;
    public final String tokenResponse;

    public MandiriClickpayPaymentParams(String mandiriCardNo, String input3, String tokenResponse) {
        this.mandiriCardNo = mandiriCardNo;
        this.input3 = input3;
        this.tokenResponse = tokenResponse;
    }
}
