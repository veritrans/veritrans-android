package com.midtrans.sdk.core.models.snap.bins;

import java.util.ArrayList;

/**
 * Created by rakawm on 1/23/17.
 */

public class BankBinsResponse {
    public final String bank;
    public final ArrayList<String> bins;

    public BankBinsResponse(String bank, ArrayList<String> bins) {
        this.bank = bank;
        this.bins = bins;
    }
}
