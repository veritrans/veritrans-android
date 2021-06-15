package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UobEzpay implements Serializable {

    @SerializedName("callback_url")
    private String callbackUrl;

    public UobEzpay(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
