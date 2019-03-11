package com.midtrans.sdk.uikit.view.method.creditcard.adapter;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.PromoResponse;

public class PromoData {
    private PromoResponse promoResponse;
    private double grossAmount;

    public PromoData(PromoResponse promoResponse, double grossAmount) {
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

    public void setGrossAmount(double grossAmount) {
        this.grossAmount = grossAmount;
    }
}