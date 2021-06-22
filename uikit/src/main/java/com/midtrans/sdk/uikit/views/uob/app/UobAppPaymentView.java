package com.midtrans.sdk.uikit.views.uob.app;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;

public interface UobAppPaymentView extends BasePaymentView {

    void onGetTransactionStatusError(Throwable error);

    void onGetTransactionStatusFailure(TransactionResponse transactionResponse);

    void onGetTransactionStatusSuccess(TransactionResponse transactionResponse);
}
