package id.co.veritrans.sdk.models;

/**
 * Created by shivam on 10/19/15.
 */
public class PaymentMethodsModel {

    private String name;
    private int imageId;

    public PaymentMethodsModel(String name, int imageId) {
        this.imageId = imageId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}