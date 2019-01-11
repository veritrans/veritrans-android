package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class TelkomselCashPaymentResponse extends BasePaymentResponse {
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

    public String getSettlementStatus() {
        return settlementTime;
    }

    public String getFinishRedirectUrl() {
        return finishRedirectUrl;
    }
}
