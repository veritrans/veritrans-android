package com.midtrans.sdk.corekit.core.snap.model.pay.response.va;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PermataPaymentResponse extends BaseBankTransferVa<PermataPaymentResponse> {
    @SerializedName("permata_va_number")
    @Expose
    private String permataVaNumber;
    @SerializedName("permata_expiration")
    @Expose
    private String permataExpiration;

    public String getPermataVaNumber() {
        return permataVaNumber;
    }

    public void setPermataVaNumber(String permataVaNumber) {
        this.permataVaNumber = permataVaNumber;
    }

    public String getPermataExpiration() {
        return permataExpiration;
    }

    public void setPermataExpiration(String permataExpiration) {
        this.permataExpiration = permataExpiration;
    }

}
