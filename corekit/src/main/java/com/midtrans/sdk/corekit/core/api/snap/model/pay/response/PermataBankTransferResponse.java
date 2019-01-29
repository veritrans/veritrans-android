package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class PermataBankTransferResponse extends BasePaymentResponse {

    public String getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public String getPermataVaNumber() {
        return permataVaNumber;
    }

    public void setPermataVaNumber(String permataVaNumber) {
        this.permataVaNumber = permataVaNumber;
    }

    public String getPermataExpiration() {
        return permataExpiration;
    }

    public void setPermataExpiration(String permataExpiration) {
        this.permataExpiration = permataExpiration;
    }

    public String getAtmChannel() {
        return atmChannel;
    }

    public void setAtmChannel(String atmChannel) {
        this.atmChannel = atmChannel;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

}