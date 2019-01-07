package com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo;

import java.io.Serializable;
import java.util.List;

public class PromoDetails implements Serializable {

    private List<Promo> promos;

    public List<Promo> getPromos() {
        return promos;
    }

    public void setPromos(List<Promo> promos) {
        this.promos = promos;
    }
}