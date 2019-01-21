package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.base.enums.BankType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.midtrans.sdk.corekit.utilities.Helper.mappingHashmapToBankTypeForGet;
import static com.midtrans.sdk.corekit.utilities.Helper.mappingHashmapToBankTypeForSet;

public class Installment implements Serializable {
    private boolean required;

    @SerializedName("terms")
    @Expose
    private Map<String, List<Integer>> terms;

    public Installment(boolean required, Map<BankType, List<Integer>> terms) {
        this.required = required;
        this.terms = mappingHashmapToBankTypeForSet(terms);
    }

    public boolean isRequired() {
        return required;
    }

    public Map<BankType, List<Integer>> getTerms() {
        return mappingHashmapToBankTypeForGet(terms);
    }
}