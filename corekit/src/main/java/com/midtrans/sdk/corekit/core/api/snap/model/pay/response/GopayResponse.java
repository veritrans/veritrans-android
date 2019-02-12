package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class GopayResponse extends BasePaymentResponse {

    public String getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getDeeplinkUrl() {
        return deeplinkUrl;
    }

    public void setDeeplinkUrl(String deeplinkUrl) {
        this.deeplinkUrl = deeplinkUrl;
    }

    public String getGopayExpiration() {
        return gopayExpiration;
    }

    public void setGopayExpiration(String gopayExpiration) {
        this.gopayExpiration = gopayExpiration;
    }

    public String getGopayExpirationRaw() {
        return gopayExpirationRaw;
    }

    public void setGopayExpirationRaw(String gopayExpirationRaw) {
        this.gopayExpirationRaw = gopayExpirationRaw;
    }

}