package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ankit on 12/10/15.
 */
public class OffersResponseModel {

    @SerializedName("discount")
    private ArrayList<OffersListModel> binpromo = new ArrayList<>();
    @SerializedName("installment")
    private ArrayList<OffersListModel> installments = new ArrayList<>();

    /**
     * @return The binpromo
     */
    public ArrayList<OffersListModel> getBinpromo() {
        return binpromo;
    }

    /**
     * @param binpromo The binpromo
     */
    public void setBinpromo(ArrayList<OffersListModel> binpromo) {
        this.binpromo = binpromo;
    }

    /**
     * @return The installments
     */
    public ArrayList<OffersListModel> getInstallments() {
        return installments;
    }

    /**
     * @param installments The installments
     */
    public void setInstallments(ArrayList<OffersListModel> installments) {
        this.installments = installments;
    }

}
