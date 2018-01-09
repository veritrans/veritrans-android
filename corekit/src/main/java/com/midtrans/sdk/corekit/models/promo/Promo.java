package com.midtrans.sdk.corekit.models.promo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ziahaqi on 12/22/17.
 */

public class Promo {

    @SerializedName("id")
    private long id;

    @SerializedName("bins")
    private List<String> bins;

    @SerializedName("discount_amount")
    private Long discountAmount;

    @SerializedName("discount_type")
    private String discountType;

    @SerializedName("max_discount_amount")
    private Long maxDiscountAmount;

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


    public long getId() {
        return id;
    }

    public List<String> getBins() {
        return bins;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public Long getMaxDiscountAmount() {
        return maxDiscountAmount;
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
}
