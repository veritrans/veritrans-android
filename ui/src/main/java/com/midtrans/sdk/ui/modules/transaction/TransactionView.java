package com.midtrans.sdk.ui.modules.transaction;

import com.midtrans.sdk.ui.models.PaymentMethodModel;

import java.util.List;

/**
 * Created by ziahaqi on 2/20/17.
 */

public interface TransactionView {
    void showProgressContainer(boolean show);

    void showConfirmationDialog(String errorMessage);

    void showPaymentMethods(List<PaymentMethodModel> paymentMethodsModel);
}
