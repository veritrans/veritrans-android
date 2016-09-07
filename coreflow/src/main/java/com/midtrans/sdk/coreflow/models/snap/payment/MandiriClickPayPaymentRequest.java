package com.midtrans.sdk.coreflow.models.snap.payment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 8/1/16.
 */
public class MandiriClickPayPaymentRequest extends BasePaymentRequest {

    @SerializedName("mandiri_card_no")
    private String mandiriCardNumber;

    private String input3;

    @SerializedName("token_response")
    private String tokenResponse;

    public String getMandiriCardNumber() {
        return mandiriCardNumber;
    }

    public void setMandiriCardNumber(String mandiriCardNumber) {
        this.mandiriCardNumber = mandiriCardNumber;
    }

    public String getInput3() {
        return input3;
    }

    public void setInput3(String input3) {
        this.input3 = input3;
    }

    public String getTokenResponse() {
        return tokenResponse;
    }

    public void setTokenResponse(String tokenResponse) {
        this.tokenResponse = tokenResponse;
    }
}
