package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by chetan on 04/12/15.
 */
public class CardResponse {
    @SerializedName("status_message")
    private String status;
    @SerializedName("status_code")
    private int code;
    private ArrayList<SaveCardRequest> data;

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

    public ArrayList<SaveCardRequest> getData() {
        return data;
    }

    public void setData(ArrayList<SaveCardRequest> data) {
        this.data = data;
    }
}
