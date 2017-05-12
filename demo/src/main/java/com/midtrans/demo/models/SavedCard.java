package com.midtrans.demo.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ziahaqi on 5/4/17.
 */

public class SavedCard implements Serializable {

    private String type;
    @SerializedName("status_code")
    private String code;
    @SerializedName("token_id")
    private String savedTokenId;
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("cardhash")
    private String maskedCard;

    public SavedCard(String type, String savedTokenId, String transactionId, String maskedCard) {
        this.type = type;
        this.savedTokenId = savedTokenId;
        this.transactionId = transactionId;
        this.maskedCard = maskedCard;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getSavedTokenId() {
        return savedTokenId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getMaskedCard() {
        return maskedCard;
    }
}
