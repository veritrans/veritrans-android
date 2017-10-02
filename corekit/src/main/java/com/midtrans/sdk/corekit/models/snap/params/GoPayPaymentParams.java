package com.midtrans.sdk.corekit.models.snap.params;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 9/7/17.
 */

public class GoPayPaymentParams {

    @SerializedName("account_number")
    private String accountNumber;

    public GoPayPaymentParams(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
