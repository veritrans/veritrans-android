package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 8/5/16.
 */
public class CardsResponse {
    @SerializedName("token_id")
    private String tokenId;
    @SerializedName("cardhash")
    private String cardHash;
    private String type;

    public CardsResponse(String tokenId, String cardHash, String type) {
        this.tokenId = tokenId;
        this.cardHash = cardHash;
        this.type = type;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getCardHash() {
        return cardHash;
    }

    public void setCardHash(String cardHash) {
        this.cardHash = cardHash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
