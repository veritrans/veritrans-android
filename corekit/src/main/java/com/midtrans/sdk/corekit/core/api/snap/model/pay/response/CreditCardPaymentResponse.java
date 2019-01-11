package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

public class CreditCardPaymentResponse extends BasePaymentResponse {

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getMaskedCard() {
        return maskedCard;
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

    public String getApprovalCode() {
        return approvalCode;
    }

    public String getBank() {
        return bank;
    }

    public String getCardType() {
        return cardType;
    }

    public String getFinishRedirectUrl() {
        return finishRedirectUrl;
    }
}