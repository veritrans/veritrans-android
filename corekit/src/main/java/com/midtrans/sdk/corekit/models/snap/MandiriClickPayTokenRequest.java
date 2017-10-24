package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 10/20/17.
 */

public class MandiriClickPayTokenRequest {
    @SerializedName("card_number")
    public String cardNumber;
    @SerializedName("client_key")
    public String clientKey;

    public MandiriClickPayTokenRequest(String cardNumber, String clientKey) {
        this.cardNumber = cardNumber;
        this.clientKey = clientKey;
    }
}
