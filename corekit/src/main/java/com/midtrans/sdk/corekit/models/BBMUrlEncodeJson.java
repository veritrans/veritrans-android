package com.midtrans.sdk.corekit.models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 12/4/15.
 */
public class BBMUrlEncodeJson {

    @SerializedName("reference")
    private String reference;
    @SerializedName("callback_url")
    private BBMCallBackUrl callbackUrl;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BBMCallBackUrl getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(BBMCallBackUrl callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
