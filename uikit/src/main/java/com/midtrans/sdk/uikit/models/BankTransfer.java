package com.midtrans.sdk.uikit.models;

/**
 * Created by ziahaqi on 8/11/17.
 */

public class BankTransfer {

    private String bankType;
    private String bankName;
    private int image;
    private Integer priority = 0;
    private String description;
    private String status;

    public BankTransfer(String bankType, String bankName, int image, Integer priority, String description, String status) {
        this.bankType = bankType;
        this.bankName = bankName;
        this.image = image;
        this.priority = priority;
        this.description = description;
        this.status = status;
    }

    public String getBankType() {
        return bankType;
    }

    public String getBankName() {
        return bankName;
    }

    public int getImage() {
        return image;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }
}
