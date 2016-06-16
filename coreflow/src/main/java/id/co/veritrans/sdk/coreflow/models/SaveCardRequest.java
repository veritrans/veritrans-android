package id.co.veritrans.sdk.coreflow.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author rakawm
 */
public class SaveCardRequest implements Serializable {

    @SerializedName("token_id")
    private int tokenId;
    @SerializedName("status_code")
    private String code;
    @SerializedName("saved_token_id")
    private String savedTokenId;
    @SerializedName("transaction_id")
    private String transactionId;
    @SerializedName("masked_card")
    private String maskedCard;

    public String getSavedTokenId() {
        return savedTokenId;
    }

    public void setSavedTokenId(String savedTokenId) {
        this.savedTokenId = savedTokenId;
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
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
}
