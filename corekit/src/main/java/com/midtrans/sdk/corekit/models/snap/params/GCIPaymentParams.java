package com.midtrans.sdk.corekit.models.snap.params;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 12/7/16.
 */

public class GCIPaymentParams {

    @SerializedName("card_number")
    private String cardNumber;
    @SerializedName("pin")
    private String password;

    public GCIPaymentParams(String cardNumber, String password) {
        this.cardNumber = cardNumber;
        this.password = password;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPassword() {
        return password;
    }
}
