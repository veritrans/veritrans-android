package com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick;

import com.google.gson.annotations.SerializedName;

public class MandiriClickpayParams {
    @SerializedName("mandiri_card_no")
    private String mandiriCardNumber;
    private String input3;
    @SerializedName("token_response")
    private String tokenResponse;

    public MandiriClickpayParams(String mandiriCardNumber, String input3, String tokenResponse) {
        this.mandiriCardNumber = mandiriCardNumber;
        this.input3 = input3;
        this.tokenResponse = tokenResponse;
    }

    public String getMandiriCardNumber() {
        return mandiriCardNumber;
    }

    public String getInput3() {
        return input3;
    }

    public String getTokenResponse() {
        return tokenResponse;
    }
}