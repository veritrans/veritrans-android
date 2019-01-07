package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CheckoutWithTransactionResponse {
    @SerializedName("token")
    private String tokenId;
    @SerializedName("error_messages")
    private ArrayList<String> errorMessages;

    public String getSnapToken() {
        return tokenId;
    }

    public void setSnapToken(String tokenId) {
        this.tokenId = tokenId;
    }

    public ArrayList<String> getErrorMessage() {
        return errorMessages;
    }
}
