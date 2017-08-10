package com.midtrans.sdk.uikit.views.banktransfer.list;

import android.content.Context;

import com.midtrans.sdk.corekit.models.BankTransferModel;
import com.midtrans.sdk.uikit.PaymentMethods;

import java.util.List;

/**
 * Created by ziahaqi on 8/10/17.
 */

public class BankTransferListPresenter {
    private final Context context;
    private final List<String> bankList;

    public BankTransferListPresenter(Context context, List<String> bankList) {
        midtransUi = MidtransUi.getInstance();
        this.bankList = bankList;
        this.context = context;
    }

    public List<BankTransferModel> getBankList() {
        return PaymentMethods.getBankTransferModel(context);
    }
}
