package com.midtrans.sdk.corekit.models.promo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ziahaqi on 12/22/17.
 */

public class Promo implements Cloneable{

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("bins")
    private List<String> bins;

    @SerializedName("payment_types")
    private List<String> paymentTypes;

    @SerializedName("calculated_discount_amount")
    private Long calculatedDiscountAmount;

    @SerializedName("discounted_gross_amount")
    private Long discountedGrossAmount;

    @SerializedName("discount_type")
    private String discountType;

    @SerializedName("start_date")
    private String startDate;

    @SerializedName("end_date")
    private String endDate;

    @SerializedName("promo_code")
    private String promoCode;

    @SerializedName("sponsor_name")
    private String sponsorName;

    @SerializedName("sponsor_message_en")
    private String sponsorMessageEn;

    @SerializedName("sponsor_message_id")
    private String sponsorMessageId;

    @SerializedName("promo_token")
    private String promoToken;
    private boolean selected;

    public long getId() {
        return id;
    }

    public List<String> getBins() {
        return bins;
    }

    public String getDiscountType() {
        return discountType;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getPromoCode() {
        return promoCode;
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

    public String getPromoToken() {
        return promoToken;
    }

    public List<String> getPaymentTypes() {
        return paymentTypes;
    }

    public String getName() {
        return name;
    }

    public Long getCalculatedDiscountAmount() {
        return calculatedDiscountAmount;
    }

    public Long getDiscountedGrossAmount() {
        return discountedGrossAmount;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
