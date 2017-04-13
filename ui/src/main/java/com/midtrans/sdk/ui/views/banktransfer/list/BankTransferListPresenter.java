package com.midtrans.sdk.ui.views.banktransfer.list;

import android.content.Context;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentMethodModel;
import com.midtrans.sdk.ui.utils.PaymentMethodUtils;

import java.util.List;

/**
 * Created by ziahaqi on 3/31/17.
 */
public class BankTransferListPresenter extends BasePaymentPresenter {

    private final Context context;
    private final List<String> bankList;

    public BankTransferListPresenter(Context context, List<String> bankList) {
        midtransUiSdk = MidtransUi.getInstance();
        this.bankList = bankList;
        this.context = context;
    }

    public List<PaymentMethodModel> getBankList() {
        return PaymentMethodUtils.createBankPaymentMethods(context, bankList);
    }
}
