package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class AuthModel {
    @SerializedName("X-Auth")
    private String xAuth;

    public String getxAuth() {
        return xAuth;
    }

    public void setxAuth(String xAuth) {
        this.xAuth = xAuth;
    }
}
