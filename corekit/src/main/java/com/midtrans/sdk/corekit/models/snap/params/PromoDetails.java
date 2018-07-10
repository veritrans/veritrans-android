package com.midtrans.sdk.corekit.models.snap.params;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 12/29/17.
 */

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
