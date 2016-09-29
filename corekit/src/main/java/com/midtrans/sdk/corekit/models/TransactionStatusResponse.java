
package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * contains information about payment charge api call like, </p> status message, status code,
 * transaction id, transaction status etc.
 *
 * Created by shivam on 10/26/15.
 */
public class TransactionStatusResponse {


    /**
     * statusMessage : Success, transaction found transactionId : 39b690a3-d626-4577-a6ab-14e29a1c74ac
     * fraudStatus : accept approvaCode : 1444915962534 transactionStatus : cancel statusCode : 200
     * signatureKey : 2abb6348769ef9873b4a2a737a6822a13e9af66d24e1dac952a58e7c5a8dc7253a3b90e67db6726639d9cd60a7b2e6bcdbc7d09b31f31222183138cd95e94fcf
     * grossAmount : 100.00 paymentType : credit_card bank : bni maskedCard : 481111-1114
     * transactionTime : 2015-10-15 20:32:34 orderId : 10938010
     */


    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("transaction_id")
    private String transactionId;

    @SerializedName("fraud_status")
    private String fraudStatus;

    @SerializedName("approval_code")
    private String approvaCode;

    @SerializedName("transaction_status")
    private String transactionStatus;

    @SerializedName("signature_key")
    private String signatureKey;

    @SerializedName("gross_amount")
    private String grossAmount;

    @SerializedName("payment_type")
    private String paymentType;

    private String bank;

    @SerializedName("masked_card")
    private String maskedCard;

    @SerializedName("transaction_time")
    private String transactionTime;

    @SerializedName("order_id")
    private String orderId;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public String getApprovaCode() {
        return approvaCode;
    }

    public void setApprovaCode(String approvaCode) {
        this.approvaCode = approvaCode;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getSignatureKey() {
        return signatureKey;
    }

    public void setSignatureKey(String signatureKey) {
        this.signatureKey = signatureKey;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
