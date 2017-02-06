package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rakawm on 1/2/17.
 */

public class PromoResponse implements Serializable {
    private int id;
    private List<String> bins;
    @SerializedName("discount_amount")
    private int discountAmount;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("discount_type")
    private String discountType;
    @SerializedName("promo_code")
    private String promoCode;
    @SerializedName("sponsor_name")
    private String sponsorName;
    @SerializedName("sponsor_message_en")
    private String sponsorMessageEn;
    @SerializedName("sponsor_message_id")
    private String sponsorMessageId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getBins() {
        return bins;
    }

    public void setBins(List<String> bins) {
        this.bins = bins;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getSponsorName() {
        return sponsorName;
    }

    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    public String getSponsorMessageEn() {
        return sponsorMessageEn;
    }

    public void setSponsorMessageEn(String sponsorMessageEn) {
        this.sponsorMessageEn = sponsorMessageEn;
    }

    public String getSponsorMessageId() {
        return sponsorMessageId;
    }

    public void setSponsorMessageId(String sponsorMessageId) {
        this.sponsorMessageId = sponsorMessageId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}