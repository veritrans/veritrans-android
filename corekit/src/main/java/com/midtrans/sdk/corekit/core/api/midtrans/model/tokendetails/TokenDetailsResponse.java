package com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TokenDetailsResponse {

    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("validation_messages")
    private List<String> validationMessages;

    @SerializedName("bank")
    private String bank = null;

    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("token_id")
    private String tokenId;

    @SerializedName("redirect_url")
    private String redirectUrl;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String status_message) {
        this.statusMessage = status_message;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String status_code) {
        this.statusCode = status_code;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String token_id) {
        this.tokenId = token_id;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirect_url) {
        this.redirectUrl = redirect_url;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(List<String> validationMessages) {
        this.validationMessages = validationMessages;
    }
}