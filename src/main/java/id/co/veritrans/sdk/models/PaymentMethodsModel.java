package id.co.veritrans.sdk.models;

/**
 * Created by shivam on 10/19/15.
 */
public class PaymentMethodsModel {

    private String name;
    private int imageId;
    private int paymentType;
    private boolean isSelected;
    public PaymentMethodsModel(String name, int imageId, int paymentType) {
        this.imageId = imageId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}