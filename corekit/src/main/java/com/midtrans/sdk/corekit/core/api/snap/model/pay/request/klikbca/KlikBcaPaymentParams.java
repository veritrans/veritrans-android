package com.midtrans.sdk.corekit.core.api.snap.model.pay.request.klikbca;

import com.google.gson.annotations.SerializedName;

public class KlikBcaPaymentParams {
    @SerializedName("user_id")
    private String userId;

    public KlikBcaPaymentParams(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}