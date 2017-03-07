package com.midtrans.sdk.ui.models;

import com.midtrans.sdk.core.models.snap.promo.PromoResponse;

/**
 * Created by rakawm on 1/2/17.
 */

public class PromoData {
    private PromoResponse promoResponse;
    private int grossAmount;

    public PromoData(PromoResponse promoResponse, int grossAmount) {
        setPromoResponse(promoResponse);
        setGrossAmount(grossAmount);
    }

    public PromoResponse getPromoResponse() {
        return promoResponse;
    }

    public void setPromoResponse(PromoResponse promoResponse) {
        this.promoResponse = promoResponse;
    }

    public double getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(int grossAmount) {
        this.grossAmount = grossAmount;
    }
}