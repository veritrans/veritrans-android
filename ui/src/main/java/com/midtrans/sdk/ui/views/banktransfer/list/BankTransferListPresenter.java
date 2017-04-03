package com.midtrans.sdk.ui.views.banktransfer.list;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.abtracts.BasePresenter;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.views.transaction.TransactionPresenter;

/**
 * Created by ziahaqi on 3/31/17.
 */
public class BankTransferListPresenter extends BasePresenter {

    public BankTransferListPresenter() {
        midtransUiSdk = MidtransUi.getInstance();
    }

}
