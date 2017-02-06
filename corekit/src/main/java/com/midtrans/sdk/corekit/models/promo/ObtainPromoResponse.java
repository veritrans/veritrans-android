package com.midtrans.sdk.corekit.models.promo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rakawm on 2/3/17.
 */

public class ObtainPromoResponse {
    private boolean success;
    private String message;
    @SerializedName("sponsor_name")
    private String sponsorName;
    @SerializedName("discount_token")
    private String discountToken;
    @SerializedName("promo_code")
    private String promoCode;
    @SerializedName("expires_at")
    private String expiresAt;
    @SerializedName("discount_amount")
    private int discountAmount;
    @SerializedName("payment_amount")
    private int paymentAmount;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getDiscountToken() {
        return discountToken;
    }

    public void setDiscountToken(String discountToken) {
        this.discountToken = discountToken;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }
}
