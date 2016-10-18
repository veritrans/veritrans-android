package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author rakawm
 */
public class SaveCardRequest implements Serializable {

    private String type;
    @SerializedName("status_code")
    private String code;
    @SerializedName("token_id")
    private String savedTokenId;
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("cardhash")
    private String maskedCard;

    public SaveCardRequest() {
    }

    public SaveCardRequest(String savedTokenId, String maskedCard, String type) {
        this.type = type;
        this.savedTokenId = savedTokenId;
        this.maskedCard = maskedCard;
    }

    public String getSavedTokenId() {
        return savedTokenId;
    }

    public void setSavedTokenId(String savedTokenId) {
        this.savedTokenId = savedTokenId;
    }


    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }
}
