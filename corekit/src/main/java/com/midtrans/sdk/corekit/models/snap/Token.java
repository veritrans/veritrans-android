package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author rakawm
 */
public class Token {
    @SerializedName("token")
    private String tokenId;
    @SerializedName("error_messages")
    private ArrayList<String> errorMessages;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public ArrayList<String> getErrorMessage() {
        return errorMessages;
    }
}
