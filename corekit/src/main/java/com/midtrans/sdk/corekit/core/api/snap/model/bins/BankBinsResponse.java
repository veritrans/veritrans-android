package com.midtrans.sdk.corekit.core.api.snap.model.bins;

import java.util.ArrayList;

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