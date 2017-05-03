package com.midtrans.demo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rakawm on 5/2/17.
 */

public interface SelectPaymentMethodListener extends Serializable {
    void onCanceled();

    void onSelectPaymentMethod(List<SelectPaymentMethodViewModel> enabledPayments);

    void onSelectAll(List<SelectPaymentMethodViewModel> enabledPayments);
}
