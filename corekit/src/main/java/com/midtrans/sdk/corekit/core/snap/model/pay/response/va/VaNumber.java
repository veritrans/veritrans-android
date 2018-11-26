package com.midtrans.sdk.corekit.core.snap.model.pay.response.va;

import java.io.Serializable;

public class VaNumber implements Serializable {
    private String bank;
    @com.google.gson.annotations.SerializedName("va_number")
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