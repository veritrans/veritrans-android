package com.midtrans.sdk.corekit.core.api.snap.model.pay.response.va;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VaNumber implements Serializable {
    private String bank;
    @SerializedName("va_number")
    private String accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}