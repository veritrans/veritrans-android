package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.models.snap.BankTransferRequestModel;

/**
 * Created by Fajar on 18/12/17.
 */

public class PermataBankTransferRequestModel extends BankTransferRequestModel {
    @SerializedName("recipient_name")
    private String recipientName;

    public PermataBankTransferRequestModel() {
        super();
    }

    public PermataBankTransferRequestModel(String vaNumber) {
        super(vaNumber);
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
