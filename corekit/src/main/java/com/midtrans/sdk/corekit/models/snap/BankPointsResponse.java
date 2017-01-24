package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 1/13/17.
 */

public class BankPointsResponse {

    @SerializedName("status_code")
    private String statusCode;
    @SerializedName("status_message")
    private String statusMessage;
    @SerializedName("validation_messages")
    private ArrayList<String> validationMessages;
    @SerializedName("point_balance")
    private Long pointBalance;
    @SerializedName("transaction_time")
    private String transactionTime;

    public BankPointsResponse(String statusCode, String statusMessage, ArrayList<String> validationMessages, Long pointBalance, String transactionTime) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.validationMessages = validationMessages;
        this.pointBalance = pointBalance;
        this.transactionTime = transactionTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public ArrayList<String> getValidationMessages() {
        return validationMessages;
    }

    public Long getPointBalance() {
        return pointBalance;
    }

    public String getTransactionTime() {
        return transactionTime;
    }
}
