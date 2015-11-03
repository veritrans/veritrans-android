package id.co.veritrans.sdk.models;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by chetan on 30/10/15.
 */
public class TransactionResponse implements Serializable {
    /**
     * status_code : 200
     * status_message : Success, Credit Card 3D Secure transaction is successful
     * transaction_id : 49ab48d4-93e1-4b52-a706-2f7a746b99d0
     * saved_token_id : 48111119d4a368-602b-4352-a1ac-23bad256741d
     * masked_card : 481111-1114
     * order_id : 109380dv0
     * gross_amount : 10000.00
     * payment_type : credit_card
     * transaction_time : 2015-10-30 19:57:33
     * transaction_status : capture
     * fraud_status : accept
     * saved_token_id_expired_at : 2025-10-30 19:57:36
     * approval_code : 1446209855742
     * secure_token : true
     * bank : bni
     * eci : 05
     */

    @SerializedName("status_code")
    private String statusCode;
    @SerializedName("status_message")
    private String statusMessage;
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("saved_token_id")
    private String savedTokenId;
    @SerializedName("masked_card")
    private String maskedCard;

    @SerializedName("order_id")
    private String orderId;
    @SerializedName("gross_amount")
    private String grossAmount;
    @SerializedName("payment_type")
    private String paymentType;
    @SerializedName("transaction_time")
    private String transactionTime;
    @SerializedName("transaction_status")
    private String transactionStatus;

    @SerializedName("fraud_status")
    private String fraudStatus;
    @SerializedName("saved_token_id_expired_at")
    private String savedTokenIdExpiredAt;
    @SerializedName("approval_code")
    private String approvalCode;
    @SerializedName("secure_token")
    private boolean secureToken;
    @SerializedName("permata_va_number")
    private String permataVANumber;

    private String bank;
    private String eci;


    //for mandiri bill pay
    @SerializedName("bill_key")
    private String billKey;
    @SerializedName("biller_code")
    private String billCode;



    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setSavedTokenId(String savedTokenId) {
        this.savedTokenId = savedTokenId;
    }

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
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

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    public void setSavedTokenIdExpiredAt(String savedTokenIdExpiredAt) {
        this.savedTokenIdExpiredAt = savedTokenIdExpiredAt;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public void setSecureToken(boolean secureToken) {
        this.secureToken = secureToken;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setEci(String eci) {
        this.eci = eci;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getSavedTokenId() {
        return savedTokenId;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public String getPaymentType() {
        return TextUtils.isEmpty(paymentType) ? "" :paymentType;
    }

    public String getTransactionTime() {
        return TextUtils.isEmpty(transactionTime) ? "" : transactionTime;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public String getSavedTokenIdExpiredAt() {
        return savedTokenIdExpiredAt;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public boolean isSecureToken() {
        return secureToken;
    }

    public String getBank() {
        return bank;
    }

    public String getEci() {
        return eci;
    }

    public String getPermataVANumber() {
        return permataVANumber;
    }

    public void setPermataVANumber(String permataVANumber) {
        this.permataVANumber = permataVANumber;
    }
}
