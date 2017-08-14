package com.midtrans.sdk.uikit.views.banktransfer.payment;

import com.midtrans.sdk.uikit.abstracts.BasePaymentView;

/**
 * Created by ziahaqi on 8/9/17.
 */

interface BankTransferPaymentView extends BasePaymentView {

    void onBankTranferPaymentUnavailable(String bankType);
}
