package com.midtrans.sdk.corekit.models.snap;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 11/28/16.
 */

public class BankBinsResponse {

    private String bank;
    private ArrayList<String> bins;

    public String getBank() {
        return bank;
    }

    public ArrayList<String> getBins() {
        return bins;
    }
}
