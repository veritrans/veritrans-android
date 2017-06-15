package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.snap.BankTransferRequestModel;

/**
 * Created by ziahaqi on 5/29/17.
 */

public class BcaBankTransferRequestModel extends BankTransferRequestModel {

    @SerializedName("free_text")
    private FreeText freeText;

    public BcaBankTransferRequestModel(String vaNumber) {
        super(vaNumber);
    }

    public BcaBankTransferRequestModel(String vaNumber, FreeText freeText) {
        super(vaNumber);
        this.freeText = freeText;
    }
}
