package com.midtrans.sdk.corekit.core.api.snap.model.promo;

import com.google.gson.annotations.SerializedName;

public class PromoDetails {
    @SerializedName("promo_id")
    private Long promoId;

    @SerializedName("discounted_gross_amount")
    private Double discountedGrossAmount;

    public PromoDetails(Long promoId, Double discountedGrossAmount) {
        this.promoId = promoId;
        this.discountedGrossAmount = discountedGrossAmount;
    }

}