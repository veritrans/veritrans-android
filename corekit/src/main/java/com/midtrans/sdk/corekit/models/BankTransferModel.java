package com.midtrans.sdk.corekit.models;

/**
 * Model class to hold information about bank transfer provided by Veritrans.
 *
 * @author rakawm
 */
public class BankTransferModel {
    private String bankName;
    private int image;
    private boolean isSelected;

    public BankTransferModel(String bankName, int image, boolean isSelected) {
        setBankName(bankName);
        setImage(image);
        setIsSelected(isSelected);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
