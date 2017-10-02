package com.midtrans.sdk.corekit.models.snap.payment;

/**
 * Created by ziahaqi on 9/12/17.
 */

public class GoPayAuthorizationRequest {

    private String otp;

    public GoPayAuthorizationRequest(String otp) {
        this.otp = otp;
    }

    public String getOtp() {
        return otp;
    }
}
