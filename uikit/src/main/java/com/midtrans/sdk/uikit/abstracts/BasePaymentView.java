package com.midtrans.sdk.uikit.abstracts;

import com.midtrans.sdk.corekit.models.TransactionResponse;

/**
 * Created by ziahaqi on 8/9/17.
 */

public interface BasePaymentView extends BaseView {

    void onPaymentSuccess(TransactionResponse response);

    void onPaymentFailure(TransactionResponse response);

    void onPaymentError(Throwable error);

}
