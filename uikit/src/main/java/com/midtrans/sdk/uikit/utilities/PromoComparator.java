package com.midtrans.sdk.uikit.utilities;

import com.midtrans.sdk.uikit.models.PromoData;

import java.util.Comparator;

/**
 * Created by rakawm on 1/2/17.
 */

public class PromoComparator implements Comparator<PromoData> {

    @Override
    public int compare(PromoData promoData, PromoData promoData2) {
        return calculatePromoAmount(promoData) - calculatePromoAmount(promoData2);
    }

    private int calculatePromoAmount(PromoData promoData) {
        if (promoData.getPromoResponse().getDiscountType().equalsIgnoreCase("FIXED")) {
            return (int) (promoData.getGrossAmount() - promoData.getPromoResponse().getDiscountAmount());
        } else {
            return (int) (promoData.getGrossAmount() - promoData.getPromoResponse().getDiscountAmount() / 100 * promoData.getGrossAmount());
        }
    }
}