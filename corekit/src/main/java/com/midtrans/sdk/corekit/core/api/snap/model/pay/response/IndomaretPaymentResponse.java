package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class IndomaretPaymentResponse extends BasePaymentResponse {

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getIndomaretExpireTime() {
        return indomaretExpireTime;
    }

    public void setIndomaretExpireTime(String indomaretExpireTime) {
        this.indomaretExpireTime = indomaretExpireTime;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

}