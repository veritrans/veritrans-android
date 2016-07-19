package id.co.veritrans.sdk.coreflow.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rakawm on 7/19/16.
 */
public class SnapTransactionDetails {
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("gross_amount")
    private double grossAmount;

    public SnapTransactionDetails(String orderId, double grossAmount) {
        setOrderId(orderId);
        setGrossAmount(grossAmount);
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(double grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
