package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 10/26/15.
 */
public class PermataBankTransferResponse {


    /**
     * statusMessage : Success, PERMATA VA transaction is successful
     * transactionId : f7772483-413b-4ad1-bda2-e7137ae65700
     * paymentType : bank_transfer
     * transactionStatus : pending
     * statusCode : 201
     * permataVANumber : 8778006020203963
     * transactionTime : 2015-10-26 16:43:43
     * grossAmount : 100.00
     * orderId : 10938011
     */
    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("transaction_id")
    private String transactionId;

    @SerializedName("payment_type")
    private String paymentType;

    @SerializedName("transactionStatus")
    private String transactionStatus;

    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("permata_va_number")
    private String permataVANumber;

    @SerializedName("transaction_time")
    private String transactionTime;

    @SerializedName("gross_amount")
    private String grossAmount;

    @SerializedName("order_id")
    private String orderId;

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setPermataVANumber(String permataVANumber) {
        this.permataVANumber = permataVANumber;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getPermataVANumber() {
        return permataVANumber;
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
}
