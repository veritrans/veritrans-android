package com.midtrans.sdk.uikit.views.status;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;

/**
 * Rather generic class for providing access to tracking
 * for classes that previously didn't have presenter
 * Created by Fajar on 08/11/17.
 */

public class PaymentStatusPresenter extends BasePaymentPresenter<BasePaymentView> {

    public PaymentStatusPresenter() {
        super();
    }

    public PaymentStatusPresenter(TransactionResponse response) {
        super();
        if (response != null) {
            this.transactionResponse = response;
        }
    }

}
