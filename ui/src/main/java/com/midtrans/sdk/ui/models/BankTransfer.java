package com.midtrans.sdk.ui.models;

/**
 * Created by ziahaqi on 3/31/17.
 */

public class BankTransfer {
    private String bankName;
    private int image;
    private String description;

    public BankTransfer(String bankName, int image, String description) {
        this.bankName = bankName;
        this.image = image;
        this.description = description;
    }

    public String getBankName() {
        return bankName;
    }

    public int getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }
}
