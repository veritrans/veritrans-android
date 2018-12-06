package com.midtrans.sdk.corekit.core.snap.model.pay.response.va;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;

import java.io.Serializable;
import java.util.List;

public class BcaPaymentResponse extends BasePaymentResponse implements Serializable {
    @SerializedName("bca_va_number")
    @Expose
    private String bcaVaNumber;
    @SerializedName("bca_expiration")
    @Expose
    private String bcaExpiration;
    @SerializedName("va_numbers")
    @Expose
    private List<VaNumber> vaNumbersList;

    public String getBcaVaNumber() {
        return bcaVaNumber;
    }

    public void setBcaVaNumber(String bcaVaNumber) {
        this.bcaVaNumber = bcaVaNumber;
    }

    public String getBcaExpiration() {
        return bcaExpiration;
    }

    public void setBcaExpiration(String bcaExpiration) {
        this.bcaExpiration = bcaExpiration;
    }

    public List<VaNumber> getVaNumbersList() {
        return vaNumbersList;
    }

    public void setVaNumbersList(List<VaNumber> vaNumbersList) {
        this.vaNumbersList = vaNumbersList;
    }
}
