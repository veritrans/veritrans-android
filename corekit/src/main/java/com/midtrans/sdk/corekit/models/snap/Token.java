package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class Token {
    @SerializedName("token_id")
    private String tokenId;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
}
