package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author rakawm
 */
public class BCAVANumber implements Serializable {
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
