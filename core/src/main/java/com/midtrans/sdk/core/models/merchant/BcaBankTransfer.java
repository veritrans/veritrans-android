package com.midtrans.sdk.core.models.merchant;

import java.io.Serializable;

/**
 * Created by rakawm on 5/11/17.
 */

public class BcaBankTransfer extends BankTransfer implements Serializable {
    private FreeText freeText;

    public FreeText getFreeText() {
        return freeText;
    }

    public void setFreeText(FreeText freeText) {
        this.freeText = freeText;
    }
}
