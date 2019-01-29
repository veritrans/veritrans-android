package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class KlikBcaResponse extends BasePaymentResponse {

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getKlikBcaExpireTime() {
        return klikBcaExpireTime;
    }

    public void setKlikBcaExpireTime(String klikBcaExpireTime) {
        this.klikBcaExpireTime = klikBcaExpireTime;
    }

}