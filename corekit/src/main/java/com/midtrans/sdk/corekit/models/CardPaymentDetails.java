package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by shivam on 10/30/15.
 */
public class CardPaymentDetails {
    /**
     * bank : bni token_id : 481111-1114-7fd8c06e-a612-4f0b-a6d4-4fa2b8918c39 save_token_id : true
     */

    private String bank;
    @SerializedName("token_id")
    private String tokenId;
    @SerializedName("save_token_id")
    private boolean saveTokenId;
    private boolean recurring;

    @SerializedName("installment_term")
    private String instalmentTerm;

    @SerializedName("bins")
    private ArrayList<String> binsArray;


    public CardPaymentDetails(String bank, String tokenId, boolean saveTokenId) {
        this.bank = bank;
        this.tokenId = tokenId;
        this.saveTokenId = saveTokenId;
    }

    public CardPaymentDetails(String bank, String tokenId, boolean saveTokenId, String instalmentTerm, ArrayList<String> binsArray) {
        this.bank = bank;
        this.tokenId = tokenId;
        this.saveTokenId = saveTokenId;
        this.instalmentTerm = instalmentTerm;
        this.binsArray = binsArray;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public boolean getSaveTokenId() {
        return saveTokenId;
    }

    public boolean isSaveTokenId() {
        return saveTokenId;
    }

    public void setSaveTokenId(boolean saveTokenId) {
        this.saveTokenId = saveTokenId;
    }

    public String getInstalmentTerm() {
        return instalmentTerm;
    }

    public void setInstalmentTerm(String instalmentTerm) {
        this.instalmentTerm = instalmentTerm;
    }

    public ArrayList<String> getBinsArray() {
        return binsArray;
    }

    public void setBinsArray(ArrayList<String> binsArray) {
        this.binsArray = binsArray;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }
}