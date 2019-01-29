package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class CimbClicksResponse extends BasePaymentResponse {

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

}