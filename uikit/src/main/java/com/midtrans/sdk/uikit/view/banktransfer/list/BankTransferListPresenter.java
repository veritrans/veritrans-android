package com.midtrans.sdk.uikit.view.banktransfer.list;

import android.app.Activity;
import android.content.Context;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.enablepayment.EnabledPayment;
import com.midtrans.sdk.uikit.base.model.BankTransfer;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.view.banktransfer.list.model.EnabledBankTransfer;

import java.util.ArrayList;
import java.util.List;

class BankTransferListPresenter {
    private final Context context;
    private final List<EnabledPayment> bankList;

    BankTransferListPresenter(Activity context, EnabledBankTransfer enabledPayments) {
        this.context = context;
        this.bankList = new ArrayList<>();

        if (enabledPayments != null) {
            this.bankList.addAll(enabledPayments.getEnabledPayments());
        }
    }

    List<BankTransfer> getBankList() {
        List<BankTransfer> banks = new ArrayList<>();
        if (bankList != null && !bankList.isEmpty()) {
            for (EnabledPayment bank : bankList) {
                BankTransfer model = PaymentListHelper.createBankTransferModel(context, bank.getType(), bank.getStatus());
                if (model != null) {
                    banks.add(model);
                }
            }
        }
        PaymentListHelper.sortBankTransferMethodsByPriority(banks);
        return banks;
    }

}