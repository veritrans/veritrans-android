package com.midtrans.sdk.ui.models;

import com.midtrans.sdk.core.models.snap.CreditCard;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;

/**
 * Created by ziahaqi on 2/20/17.
 */

public class TransactionOption {

    private final CreditCard creditCard;

    public TransactionOption(SnapTransaction snapTransaction) {
        this.creditCard = snapTransaction.creditCard;

    }
}
