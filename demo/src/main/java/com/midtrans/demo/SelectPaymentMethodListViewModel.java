package com.midtrans.demo;

import com.midtrans.sdk.core.models.snap.transaction.SnapEnabledPayment;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rakawm on 5/2/17.
 */

public class SelectPaymentMethodListViewModel implements Serializable {
    private List<SelectPaymentMethodViewModel> enabledPayments;

    public SelectPaymentMethodListViewModel(List<SelectPaymentMethodViewModel> enabledPayments) {
        this.enabledPayments = enabledPayments;
    }

    public List<SelectPaymentMethodViewModel> getEnabledPayments() {
        return enabledPayments;
    }

    public void setEnabledPayments(List<SelectPaymentMethodViewModel> enabledPayments) {
        this.enabledPayments = enabledPayments;
    }
}
