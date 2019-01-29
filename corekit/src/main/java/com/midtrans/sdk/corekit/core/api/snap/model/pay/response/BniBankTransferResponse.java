package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.va.VaNumber;

import java.util.List;

public class BniBankTransferResponse extends BasePaymentResponse {

    public String getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public List<VaNumber> getVaNumber() {
        return vaNumbersList;
    }

    public void setVaNumbersList(List<VaNumber> vaNumbersList) {
        this.vaNumbersList = vaNumbersList;
    }

    public String getBniVaNumber() {
        return bniVaNumber;
    }

    public void setBniVaNumber(String bniVaNumber) {
        this.bniVaNumber = bniVaNumber;
    }

    public String getBniExpiration() {
        return bniExpiration;
    }

    public void setBniExpiration(String bniExpiration) {
        this.bniExpiration = bniExpiration;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

}