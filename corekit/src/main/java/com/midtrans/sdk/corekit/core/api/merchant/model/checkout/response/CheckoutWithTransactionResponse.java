package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CheckoutWithTransactionResponse {
    @SerializedName("token")
    private String token;
    @SerializedName("error_messages")
    private ArrayList<String> errorMessages;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(ArrayList<String> errorMessages) {
        this.errorMessages = errorMessages;
    }
}