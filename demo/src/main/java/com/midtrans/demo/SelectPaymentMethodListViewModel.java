package com.midtrans.demo;

import com.midtrans.sdk.corekit.models.snap.EnabledPayment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rakawm on 5/2/17.
 */

public class SelectPaymentMethodListViewModel implements Serializable {
    private List<EnabledPayment> enabledPayments;

    public SelectPaymentMethodListViewModel(List<EnabledPayment> enabledPayments) {
        this.enabledPayments = enabledPayments;
    }

    public List<EnabledPayment> getEnabledPayments() {
        return enabledPayments;
    }

    public void setEnabledPayments(List<EnabledPayment> enabledPayments) {
        this.enabledPayments = enabledPayments;
    }
}
