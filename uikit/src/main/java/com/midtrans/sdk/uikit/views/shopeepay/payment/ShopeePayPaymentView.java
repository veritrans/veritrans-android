package com.midtrans.sdk.uikit.views.shopeepay.payment;

import com.midtrans.sdk.corekit.models.TransactionResponse;

public interface ShopeePayPaymentView {
    void onGetTransactionStatusError(Throwable error);

    void onGetTransactionStatusFailure(TransactionResponse transactionResponse);

    void onGetTransactionStatusSuccess(TransactionResponse transactionResponse);
}
