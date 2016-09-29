package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class BasePaymentRequest {
    @SerializedName("transaction_id")
    protected String transactionId;
    protected String authenticityToken;
    @SerializedName("payment_type")
    protected String paymentType;

    public BasePaymentRequest() {

    }

    public BasePaymentRequest(String transactionId, String paymentType) {
        setPaymentType(paymentType);
        setTransactionId(transactionId);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAuthenticityToken() {
        return authenticityToken;
    }

    public void setAuthenticityToken(String authenticityToken) {
        this.authenticityToken = authenticityToken;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
