package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ziahaqi on 7/29/17.
 */

public class TransactionStatusResponse {

    private String token;
    @SerializedName("masked_card")
    private String maskedCard;
    @SerializedName("approval_code")
    private String approvalCode;
    private String bank;
    @SerializedName("saved_token_id")
    private String savedTokenId;
    @SerializedName("saved_token_id_expired_at")
    private String savedTokenIdExpiredAt;
    @SerializedName("transaction_time")
    private String transactionTime;
    @SerializedName("gross_amount")
    private String grossAmount;
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("payment_type")
    private String paymentType;
    @SerializedName("status_code")
    private String statusCode;
    @SerializedName("transaction_status")
    private String transactionStatus;
    @SerializedName("fraud_status")
    private String fraudStatus;
    @SerializedName("status_message")
    private String statusMessage;
    @SerializedName("error_messages")
    private List<String> errorMessages;

    public String getToken() {
        return token;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public String getBank() {
        return bank;
    }

    public String getSavedTokenId() {
        return savedTokenId;
    }

    public String getSavedTokenIdExpiredAt() {
        return savedTokenIdExpiredAt;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
