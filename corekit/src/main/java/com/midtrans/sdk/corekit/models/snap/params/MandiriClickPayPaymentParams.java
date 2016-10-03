package com.midtrans.sdk.corekit.models.snap.params;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 10/3/16.
 */

public class MandiriClickPayPaymentParams {
    @SerializedName("mandiri_card_no")
    private String mandiriCardNumber;
    private String input3;
    @SerializedName("token_response")
    private String tokenResponse;

    public MandiriClickPayPaymentParams(String mandiriCardNumber, String input3, String tokenResponse) {
        this.mandiriCardNumber = mandiriCardNumber;
        this.input3 = input3;
        this.tokenResponse = tokenResponse;
    }
}
