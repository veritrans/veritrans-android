package com.midtrans.sdk.corekit.models.promo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 2/9/18.
 */

public class PromoDetails {

    private ArrayList<Promo> promos;

    public List<Promo> getPromos() {
        return promos;
    }

    public void setPromos(ArrayList<Promo> promos) {
        this.promos = promos;
    }

    @Override
    public String toString() {
        return "PromoDetails{" +
                "promos=" + promos +
                '}';
    }
}
