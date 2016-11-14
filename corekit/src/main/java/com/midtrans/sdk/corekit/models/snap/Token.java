package com.midtrans.sdk.corekit.models.snap;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * @author rakawm
 */
public class Token {
    @SerializedName("token")
    private String tokenId;
    @SerializedName("token_id")
    private String token_id;
    @SerializedName("error_messages")
    private ArrayList<String> errorMessages;

    public String getTokenId() {
        return TextUtils.isEmpty(tokenId) ? token_id : tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public ArrayList<String> getErrorMessage() {
        return errorMessages;
    }
}
