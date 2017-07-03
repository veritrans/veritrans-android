package com.midtrans.sdk.uikit.models;

import com.midtrans.sdk.corekit.models.snap.EnabledPayment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ziahaqi on 6/19/17.
 */

public class EnabledPayments implements Serializable {
    private List<EnabledPayment> enabledPayments;

    public EnabledPayments(List<EnabledPayment> enabledPayments) {
        this.enabledPayments = enabledPayments;
    }

    public List<EnabledPayment> getEnabledPayments() {
        return enabledPayments;
    }
}
