package com.midtrans.sdk.corekit.models;

/**
 * Created by chetan on 10/11/15.
 */

import java.io.Serializable;
import java.util.ArrayList;

public class BankDetailArray implements Serializable {
    private ArrayList<BankDetail> bankDetails;

    public ArrayList<BankDetail> getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(ArrayList<BankDetail> bankDetails) {
        this.bankDetails = bankDetails;
    }
}
