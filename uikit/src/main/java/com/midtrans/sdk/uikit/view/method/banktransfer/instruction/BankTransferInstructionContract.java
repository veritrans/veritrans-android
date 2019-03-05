package com.midtrans.sdk.uikit.view.method.banktransfer.instruction;

import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;

public interface BankTransferInstructionContract extends BasePaymentContract {

    void onBankTransferPaymentUnavailable(String bankType);
}
