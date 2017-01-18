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
    private Integer priority = 0;
    private String description;

    public BankTransferModel(String bankName, int image, boolean isSelected, int priority, String description) {
        setBankName(bankName);
        setImage(image);
        setIsSelected(isSelected);
        setPriority(priority);
        setDescription(description);
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
