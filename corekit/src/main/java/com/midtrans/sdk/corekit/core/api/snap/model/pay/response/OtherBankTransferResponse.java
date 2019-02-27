package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.va.VaNumber;
import com.midtrans.sdk.corekit.core.payment.BaseGroupPayment;

import java.util.List;

public class OtherBankTransferResponse extends BasePaymentResponse {

    public String getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public List<VaNumber> getVaNumbersList() {
        return vaNumbersList;
    }

    public void setVaNumbersList(List<VaNumber> vaNumbersList) {
        this.vaNumbersList = vaNumbersList;
    }

    public String getBcaVaNumber() {
        return bcaVaNumber;
    }

    public void setBcaVaNumber(String bcaVaNumber) {
        this.bcaVaNumber = bcaVaNumber;
    }

    public String getBcaExpiration() {
        return bcaExpiration;
    }

    public void setBcaExpiration(String bcaExpiration) {
        this.bcaExpiration = bcaExpiration;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
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

    public String getBillKey() {
        return billKey;
    }

    public void setBillKey(String billKey) {
        this.billKey = billKey;
    }

    public String getBillerCode() {
        return billerCode;
    }

    public void setBillerCode(String billerCode) {
        this.billerCode = billerCode;
    }

    public String getBillPaymentExpiration() {
        return billPaymentExpiration;
    }

    public void setBillPaymentExpiration(String billPaymentExpiration) {
        this.billPaymentExpiration = billPaymentExpiration;
    }

}
