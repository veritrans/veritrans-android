package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class AlfamartPaymentResponse extends BasePaymentResponse {

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

    public String getAlfamartExpireTime() {
        return alfamartExpireTime;
    }

    public void setAlfamartExpireTime(String alfamartExpireTime) {
        this.alfamartExpireTime = alfamartExpireTime;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

}