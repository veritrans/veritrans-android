package com.midtrans.sdk.corekit.models.snap.payment;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class QrisPaymentParameter {
    @SerializedName("acquirer")
    protected List<String> acquirer;

    public QrisPaymentParameter(List<String> acquirer) {
        this.acquirer = acquirer;
    }
}
