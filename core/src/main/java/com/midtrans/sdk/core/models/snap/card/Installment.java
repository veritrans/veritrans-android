package com.midtrans.sdk.core.models.snap.card;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ziahaqi on 8/5/16.
 */
public class Installment {
    public boolean required;
    @Expose
    public Map<String, ArrayList<Integer>> terms;

}
