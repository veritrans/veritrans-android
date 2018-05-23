package com.midtrans.sdk.uikit.views.gopay.payment;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;

/**
 * Created by ziahaqi on 9/7/17.
 */

public interface GoPayPaymentView extends BasePaymentView {

    void onGetTransactionStatusError(Throwable error);

    void onGetTransactionStatusFailure(TransactionResponse transactionResponse);

    void onGetTransactionStatusSuccess(TransactionResponse transactionResponse);

}
