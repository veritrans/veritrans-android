package com.midtrans.sdk.uikit.utilities;

import com.midtrans.sdk.uikit.models.PromoData;

import java.util.Comparator;

/**
 * Created by rakawm on 1/2/17.
 */

public class PromoComparator implements Comparator<PromoData> {

    @Override
    public int compare(PromoData promoData, PromoData promoData2) {
        return Double.compare(calculatePromoAmount(promoData2), calculatePromoAmount(promoData));
    }

    private double calculatePromoAmount(PromoData promoData) {
        if (promoData.getPromoResponse().getDiscountType().equalsIgnoreCase("FIXED")) {
            return promoData.getPromoResponse().getDiscountAmount();
        } else {
            return promoData.getPromoResponse().getDiscountAmount() * promoData.getGrossAmount() / 100;
        }
    }
}