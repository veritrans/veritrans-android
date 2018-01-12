package com.midtrans.sdk.corekit.models.promo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ziahaqi on 12/22/17.
 */

public class ObtainPromosResponse {

    @SerializedName("status_code")
    private String statusCode;

    @SerializedName("status_message")
    private String statusMessage;

    @SerializedName("promos")
    private List<Promo> promos;

    public String getStatusCode() {
        return statusCode;
    }

    public List<Promo> getPromos() {
        return promos;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
