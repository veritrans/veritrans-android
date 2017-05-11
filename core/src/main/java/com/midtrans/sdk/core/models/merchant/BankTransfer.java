package com.midtrans.sdk.core.models.merchant;

import java.io.Serializable;

/**
 * Created by rakawm on 5/11/17.
 */

public class BankTransfer implements Serializable {
    private String vaNumber;

    public String getVaNumber() {
        return vaNumber;
    }

    public void setVaNumber(String vaNumber) {
        this.vaNumber = vaNumber;
    }
}
