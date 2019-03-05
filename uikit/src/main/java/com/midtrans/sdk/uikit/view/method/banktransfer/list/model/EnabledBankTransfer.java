package com.midtrans.sdk.uikit.view.method.banktransfer.list.model;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.enablepayment.EnabledPayment;

import java.io.Serializable;
import java.util.List;

public class EnabledBankTransfer implements Serializable {
    private List<EnabledPayment> enabledPayments;

    public EnabledBankTransfer(List<EnabledPayment> enabledPayments) {
        this.enabledPayments = enabledPayments;
    }

    public List<EnabledPayment> getEnabledPayments() {
        return enabledPayments;
    }
}