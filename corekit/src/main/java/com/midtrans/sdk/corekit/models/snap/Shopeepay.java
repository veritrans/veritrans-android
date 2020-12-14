package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Shopeepay implements Serializable {

    @SerializedName("callback_url")
    private String callbackUrl;

    public Shopeepay(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
