package com.midtrans.sdk.corekit.models.snap.params;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 12/29/17.
 */

public class PromoDetails {
    @SerializedName("discount_token")
    private String dicountToken;

    @SerializedName("discount_amount")
    private Long discountAmount;

    public PromoDetails(String dicountToken, Long discountAmount) {
        this.dicountToken = dicountToken;
        this.discountAmount = discountAmount;
    }

    public String getDicountToken() {
        return dicountToken;
    }

    public void setDicountToken(String dicountToken) {
        this.dicountToken = dicountToken;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }
}
