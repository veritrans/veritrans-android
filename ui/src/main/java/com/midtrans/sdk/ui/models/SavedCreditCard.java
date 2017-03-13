package com.midtrans.sdk.ui.models;

import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.core.models.snap.promo.PromoResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ziahaqi on 3/6/17.
 */

public class SavedCreditCard implements Serializable {
    private List<SavedToken> savedCards;
    private List<PromoResponse> promos;

    public SavedCreditCard(List<SavedToken> savedCards, List<PromoResponse> promos) {
        this.savedCards = savedCards;
        this.promos = promos;
    }


    public boolean haveSavedCards() {
        return savedCards != null && !savedCards.isEmpty();
    }

    public List<SavedToken> getSavedCards() {
        return savedCards;
    }
}
