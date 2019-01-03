package com.midtrans.sdk.corekit.core.midtrans.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author rakawm
 */
public class CardRegistrationResponse {
    @SerializedName("status_code")
    private String statusCode;
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("saved_token_id")
    private String savedTokenId;
    @SerializedName("masked_card")
    private String maskedCard;
    @SerializedName("status_message")
    private String statusMessage;
    @SerializedName("validation_messages")
    private List<String> validationMessage;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSavedTokenId() {
        return savedTokenId;
    }

    public void setSavedTokenId(String savedTokenId) {
        this.savedTokenId = savedTokenId;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
