package com.midtrans.sdk.corekit.core.snap.model.pay.response.va;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OtherPaymentResponse extends BaseBankTransferVa<OtherPaymentResponse> {
    @SerializedName("bni_va_number")
    @Expose
    private String otherVaNumber;
    @SerializedName("bni_expiration")
    @Expose
    private String otherExpiration;
    @SerializedName("va_numbers")
    @Expose
    private List<VaNumber> vaNumbersList;

    public String getOtherVaNumber() {
        return otherVaNumber;
    }

    public void setOtherVaNumber(String otherVaNumber) {
        this.otherVaNumber = otherVaNumber;
    }

    public String getOtherExpiration() {
        return otherExpiration;
    }

    public void setOtherExpiration(String otherExpiration) {
        this.otherExpiration = otherExpiration;
    }

    public List<VaNumber> getVaNumbersList() {
        return vaNumbersList;
    }

    public void setVaNumbersList(List<VaNumber> vaNumbersList) {
        this.vaNumbersList = vaNumbersList;
    }
}