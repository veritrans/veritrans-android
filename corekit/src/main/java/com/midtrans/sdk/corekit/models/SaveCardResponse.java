package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class SaveCardResponse {
    @SerializedName("status_message")
    private String status;
    @SerializedName("status_code")
    private int code;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
