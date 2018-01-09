package com.midtrans.sdk.corekit.models.promo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 12/27/17.
 */

public class HoldPromoResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("sponsor_name")
    private String sponsorName;

    @SerializedName("sponsor_message_en")
    private String sponsorMessageEn;

    @SerializedName("sponsor_message_id")
    private String sponsorMessageId;

    @SerializedName("discount_token")
    private String discountToken;

    @SerializedName("promo_code")
    private String promoCode;

    @SerializedName("expires_at")
    private String expiresAt;

    @SerializedName("discount_amount")
    private Integer discountAmount;

    @SerializedName("discount_type")
    private String discountType;

    @SerializedName("max_discount_amount")
    private Long maxDiscountAmount;

    @SerializedName("discount")
    private Long discount;

    @SerializedName("payment_amount_after_discount")
    private Long paymentAmountAfterDiscount;

    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public String getSponsorMessageEn() {
        return sponsorMessageEn;
    }

    public String getSponsorMessageId() {
        return sponsorMessageId;
    }

    public String getDiscountToken() {
        return discountToken;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public Integer getDiscountAmount() {
        return discountAmount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public Long getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public Long getDiscount() {
        return discount;
    }

    public Long getPaymentAmountAfterDiscount() {
        return paymentAmountAfterDiscount;
    }

    public String getMessage() {
        return message;
    }
}
