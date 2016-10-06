package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class DeleteCardResponse {
    @SerializedName("status_message")
    private String message;
    @SerializedName("message")
    private String error;
    @SerializedName("status_code")
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
