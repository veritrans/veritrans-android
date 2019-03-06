package com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick;

import com.google.gson.annotations.SerializedName;

public class MandiriClickpayParams {
    private String token;
    private String input3;
    @SerializedName("token_id")
    private String tokenId;

    public MandiriClickpayParams(String tokenId, String token, String input3) {
        this.token = token;
        this.input3 = input3;
        this.tokenId = tokenId;
    }

    public String getMandiriCardNumber() {
        return token;
    }

    public String getInput3() {
        return input3;
    }

    public String getTokenResponse() {
        return tokenId;
    }
}