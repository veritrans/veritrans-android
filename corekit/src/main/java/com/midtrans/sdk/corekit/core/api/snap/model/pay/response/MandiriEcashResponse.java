package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class MandiriEcashResponse extends BasePaymentResponse {

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}