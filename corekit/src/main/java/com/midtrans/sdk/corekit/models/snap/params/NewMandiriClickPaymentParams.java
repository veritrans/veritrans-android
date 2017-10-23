package com.midtrans.sdk.corekit.models.snap.params;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 10/23/17.
 */

public class NewMandiriClickPaymentParams {

    private String input3;
    private String token;
    @SerializedName("token_id")
    private String tokenId;

    public NewMandiriClickPaymentParams(String tokenId, String token, String input3) {
        this.input3 = input3;
        this.token = token;
        this.tokenId = tokenId;
    }
}
