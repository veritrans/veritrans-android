package com.midtrans.sdk.uikit.views.banktransfer.list;

import android.content.Context;

import com.midtrans.sdk.corekit.models.BankTransferModel;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.PaymentMethods;
import com.midtrans.sdk.uikit.models.BankTransfer;
import com.midtrans.sdk.uikit.models.EnabledPayments;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 8/10/17.
 */

public class BankTransferListPresenter {
    private final Context context;
    private final List<EnabledPayment> bankList;

    public BankTransferListPresenter(Context context, EnabledPayments enabledPayments) {
        this.context = context;
        this.bankList = new ArrayList<>();

        if (enabledPayments != null) {
            this.bankList.addAll(enabledPayments.getEnabledPayments());
        }
    }

    public List<BankTransfer> getBankList() {
        List<BankTransfer> banks = new ArrayList<>();
        if (bankList != null && !bankList.isEmpty()) {
            for (EnabledPayment bank : bankList) {
                BankTransfer model = PaymentMethods.createBankTransferModel(context, bank.getType(), bank.getStatus());
                if (model != null) {
                    banks.add(model);
                }
            }
        }

        SdkUIFlowUtil.sortBankTransferMethodsByPriority(banks);

        return banks;
    }
}
