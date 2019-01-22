package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.base.enums.AcquiringBankType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.midtrans.sdk.corekit.utilities.Helper.mappingMapToBankTypeForGet;
import static com.midtrans.sdk.corekit.utilities.Helper.mappingMapToBankTypeForSet;

public class Installment implements Serializable {
    private boolean required;

    @SerializedName("terms")
    @Expose
    private Map<String, List<Integer>> terms;

    public Installment(boolean required, Map<AcquiringBankType, List<Integer>> terms) {
        this.required = required;
        this.terms = mappingMapToBankTypeForSet(terms);
    }

    public boolean isRequired() {
        return required;
    }

    public Map<AcquiringBankType, List<Integer>> getTerms() {
        return mappingMapToBankTypeForGet(terms);
    }
}