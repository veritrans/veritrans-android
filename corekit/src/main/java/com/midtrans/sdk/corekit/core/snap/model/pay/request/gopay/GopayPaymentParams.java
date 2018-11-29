package com.midtrans.sdk.corekit.core.snap.model.pay.request.gopay;

import com.google.gson.annotations.SerializedName;

public class GopayPaymentParams {
    @SerializedName("account_number")
    private String account_number;

    public GopayPaymentParams(String account_number) {
        this.account_number = account_number;
    }

    public String getAccountNumber() {
        return account_number;
    }
}
