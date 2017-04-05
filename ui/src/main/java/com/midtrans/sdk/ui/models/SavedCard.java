package com.midtrans.sdk.ui.models;

import com.midtrans.sdk.core.models.snap.SavedToken;

import java.io.Serializable;

/**
 * Created by rakawm on 4/4/17.
 */

public class SavedCard implements Serializable {
    private SavedToken savedToken;
    private String bank;

    public SavedToken getSavedToken() {
        return savedToken;
    }

    public void setSavedToken(SavedToken savedToken) {
        this.savedToken = savedToken;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
