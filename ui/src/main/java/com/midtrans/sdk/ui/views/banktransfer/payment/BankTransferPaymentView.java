package com.midtrans.sdk.ui.views.banktransfer.payment;

import com.midtrans.sdk.ui.models.PaymentResult;

/**
 * Created by ziahaqi on 4/3/17.
 */

public interface BankTransferPaymentView {
    void onPaymentError(String error);

    void onPaymentFailure(PaymentResult paymentResult);

    void onPaymentSuccess(PaymentResult paymentResult);
}
