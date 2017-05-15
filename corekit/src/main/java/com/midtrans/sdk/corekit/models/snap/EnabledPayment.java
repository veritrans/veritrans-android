package com.midtrans.sdk.corekit.models.snap;

/**
 * Created by ziahaqi on 10/13/16.
 */

public class EnabledPayment {

    private String type;
    private String category;

    public EnabledPayment(String type, String category) {
        this.type = type;
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
