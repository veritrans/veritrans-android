package com.midtrans.sdk.corekit.core.api.snap.model.pay.response;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.bcaklikpay.BcaKlikPayDataResponse;

public class OnlineDebitBcaKlikpayPaymentResponse extends BasePaymentResponse {

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getRedirectUrl() {
        return redirectUrl;
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

    public String getFinishRedirectUrl() {
        return finishRedirectUrl;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public BcaKlikPayDataResponse getRedirectData() {
        return dataResponse;
    }
}