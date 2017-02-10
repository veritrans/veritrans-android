package com.midtrans.sdk.core.models.snap.bank.bca;

/**
 * Created by rakawm on 10/19/16.
 */

public class BcaVaNumber {
    public final String bank;
    public final String vaNumber;

    public BcaVaNumber(String bank, String vaNumber) {
        this.bank = bank;
        this.vaNumber = vaNumber;
    }
}
