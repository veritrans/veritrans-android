package com.midtrans.sdk.ui.abtracts;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.utils.Utils;

/**
 * Created by ziahaqi on 2/22/17.
 */

public abstract class BasePresenter {
    protected MidtransUi midtransUiSdk;


    public String getFormattedTotalAmount() {
        return Utils.getFormattedAmount(midtransUiSdk.getTransaction().transactionDetails.grossAmount);
    }

}
