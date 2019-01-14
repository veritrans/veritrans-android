package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.va.VaNumber;

import java.util.List;

public class BankTransferVaBcaPaymentResponse extends BasePaymentResponse {

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

    public String getBcaVaNumber() {
        return bcaVaNumber;
    }

    public String getBcaExpiration() {
        return bcaExpiration;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public String getFinishRedirectUrl() {
        return finishRedirectUrl;
    }
}