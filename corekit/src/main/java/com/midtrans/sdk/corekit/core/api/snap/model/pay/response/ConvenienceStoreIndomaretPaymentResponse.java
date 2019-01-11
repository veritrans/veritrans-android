package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class ConvenienceStoreIndomaretPaymentResponse extends BasePaymentResponse {

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

    public String getPaymentCode() {
        return paymentCode;
    }

    public String getStore() {
        return store;
    }

    public String getIndomaretExpireTime() {
        return indomaret_expire_time;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public String getFinishRedirectUrl() {
        return finishRedirectUrl;
    }
}