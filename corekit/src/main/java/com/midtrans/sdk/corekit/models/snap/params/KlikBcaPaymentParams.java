package com.midtrans.sdk.corekit.models.snap.params;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 9/30/16.
 */

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
