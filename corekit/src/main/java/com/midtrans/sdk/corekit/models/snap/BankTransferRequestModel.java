package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rakawm on 4/25/17.
 */

public class BankTransferRequestModel implements Serializable {
    @SerializedName("va_number")
    private String vaNumber;

    public BankTransferRequestModel() {}

    public BankTransferRequestModel(String vaNumber) {
        setVaNumber(vaNumber);
    }

    public String getVaNumber() {
        return vaNumber;
    }

    public void setVaNumber(String vaNumber) {
        this.vaNumber = vaNumber;
    }
}
