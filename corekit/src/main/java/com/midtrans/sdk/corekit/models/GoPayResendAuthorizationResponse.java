package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ziahaqi on 9/12/17.
 */

public class GoPayResendAuthorizationResponse {

    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("error_messages")
    private List<String> errorMessages;

    @SerializedName("finish_redirect_url")
    private String finishRedirectUrl;

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public String getFinishRedirectUrl() {
        return finishRedirectUrl;
    }
}
