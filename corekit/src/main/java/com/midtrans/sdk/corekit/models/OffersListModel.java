package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * model class created for offers list recycler view. it contains offer title and offers ending
 * date
 *
 * Created by Ankit on 12/7/15.
 */
public class OffersListModel implements Serializable {

    @SerializedName("title")
    private String offerName;
    private String description;
    @SerializedName("discount_percentage")
    private int discountPercentage;
    @SerializedName("installment_terms")
    private List<String> duration;
    @SerializedName("bins")
    private ArrayList<String> bins;

    /**
     * @return The offerName
     */
    public String getOfferName() {
        return offerName;
    }

    /**
     * @param offerName The offer name
     */
    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    /**
     * @return The duration
     */
    public List<String> getDuration() {
        return duration;
    }

    /**
     * @param duration The duration
     */
    public void setDuration(List<String> duration) {
        this.duration = duration;
    }

    /**
     * @return The bins
     */
    public ArrayList<String> getBins() {
        return bins;
    }

    /**
     * @param bins The bins
     */
    public void setBins(ArrayList<String> bins) {
        this.bins = bins;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description promo description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return discount percentage
     */
    public int getDiscountPercentage() {
        return discountPercentage;
    }

    /**
     * @param discountPercentage discount percentage
     */
    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}