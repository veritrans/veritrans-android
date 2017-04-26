package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ziahaqi on 8/5/16.
 */
public class Installment {
    private boolean required;
    @SerializedName("terms")
    @Expose
    private Map<String, ArrayList<Integer>> terms;

    public boolean isRequired() {
        return required;
    }

    public Map<String, ArrayList<Integer>> getTerms() {
        return terms;
    }

    public void setTerms(Map<String, ArrayList<Integer>> terms) {
        this.terms = terms;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
