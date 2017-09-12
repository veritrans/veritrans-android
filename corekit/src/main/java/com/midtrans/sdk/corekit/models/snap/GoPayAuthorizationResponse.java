package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ziahaqi on 9/12/17.
 */

public class GoPayAuthorizationResponse {
    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("error_messages")
    private List<String> errorMessages;

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
