package com.midtrans.sdk.core.models.snap.card;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by ziahaqi on 8/5/16.
 */
public class Installment implements Serializable{
    public final boolean required;
    public final Map<String, List<Integer>> terms;

    public Installment(boolean required, Map<String, List<Integer>> terms) {
        this.required = required;
        this.terms = terms;
    }
}
