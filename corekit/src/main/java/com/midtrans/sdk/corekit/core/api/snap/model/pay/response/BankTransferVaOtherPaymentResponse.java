package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.va.VaNumber;

import java.util.List;

public class BankTransferVaOtherPaymentResponse extends BasePaymentResponse {

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public List<VaNumber> getVaNumber() {
        return vaNumbersList;
    }

    public String getBniVaNumber() {
        return bniVaNumber;
    }

    public String getBniExpiration() {
        return bniExpiration;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public String getFinishRedirectUrl() {
        return finishRedirectUrl;
    }

    public void setStatusCode(String statusCode){
        this.statusCode = statusCode;
    }

    public void setStatusMessage(String statusMessage){
        this.statusMessage = statusMessage;
    }

    public void setTransactionId(String transactionId){
        this.transactionId = transactionId;
    }

    public void setOrderId(String orderId){
        this.orderId = orderId;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public void setFinishRedirectUrl(String finishRedirectUrl) {
        this.finishRedirectUrl = finishRedirectUrl;
    }

    public void setBniVaNumber(String bniVaNumber) {
        this.bniVaNumber = bniVaNumber;
    }

    public void setBniExpiration(String bniExpiration) {
        this.bniExpiration = bniExpiration;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

}