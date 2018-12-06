package com.midtrans.sdk.corekit.core.snap.model.pay.response.va;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.BasePaymentResponse;

import java.io.Serializable;
import java.util.List;

public class BniPaymentResponse extends BasePaymentResponse implements Serializable {


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
