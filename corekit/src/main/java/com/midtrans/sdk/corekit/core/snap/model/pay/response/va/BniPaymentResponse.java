package com.midtrans.sdk.corekit.core.snap.model.pay.response.va;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BniPaymentResponse extends BaseBankTransferVa implements Serializable {
    @SerializedName("bni_va_number")
    @Expose
    private String bniVaNumber;
    @SerializedName("bni_expiration")
    @Expose
    private String bniExpiration;
    @SerializedName("va_numbers")
    @Expose
    private List<VaNumber> vaNumbersList;

    public String getBniVaNumber() {
        return bniVaNumber;
    }

    public void setBniVaNumber(String bcaVaNumber) {
        this.bniVaNumber = bcaVaNumber;
    }

    public String getBniExpiration() {
        return bniExpiration;
    }

    public void setBniExpiration(String bniExpiration) {
        this.bniExpiration = bniExpiration;
    }

    public List<VaNumber> getVaNumbersList() {
        return vaNumbersList;
    }

    public void setVaNumbersList(List<VaNumber> vaNumbersList) {
        this.vaNumbersList = vaNumbersList;
    }
}
