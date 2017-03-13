package com.midtrans.sdk.core.models.snap.promo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rakawm on 1/2/17.
 */

public class PromoResponse {
    public int id;
    public List<String> bins;
    public int discountAmount;
    public String startDate;
    public String endDate;
    public String discountType;
    public String promoCode;
    public String sponsorName;
    public String sponsorMessageEn;
    public String sponsorMessageId;
}