package com.midtrans.sdk.core.models.snap.bank;

import java.io.Serializable;

/**
 * Created by rakawm on 10/19/16.
 */

public class VaNumber implements Serializable {
    public final String bank;
    public final String vaNumber;

    public VaNumber(String bank, String vaNumber) {
        this.bank = bank;
        this.vaNumber = vaNumber;
    }
}
