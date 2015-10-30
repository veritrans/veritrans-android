package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivam on 10/29/15.
 */
public class TransactionDetails {
    /**
     * grossAmount : 100
     * orderId : 10938011
     */

    @SerializedName("grossAmount")
    private String grossAmount;

    @SerializedName("order_id")
    private String orderId;

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public String getOrderId() {
        return orderId;
    }
}
