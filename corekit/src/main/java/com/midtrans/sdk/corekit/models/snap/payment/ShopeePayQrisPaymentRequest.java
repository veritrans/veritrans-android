package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ShopeePayQrisPaymentRequest{
    @SerializedName("payment_type")
    protected String paymentType;

    @SerializedName("acquirer")
    protected List<String> acquirer;

    public ShopeePayQrisPaymentRequest(String paymentType, List<String> acquirer) {
        this.paymentType = paymentType;
        this.acquirer = acquirer;
    }
}
