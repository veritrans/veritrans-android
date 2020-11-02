package com.midtrans.sdk.uikit.views.shopeepay.payment;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;

public interface ShopeePayPaymentView extends BasePaymentView {

    void onGetTransactionStatusError(Throwable error);

    void onGetTransactionStatusFailure(TransactionResponse transactionResponse);

    void onGetTransactionStatusSuccess(TransactionResponse transactionResponse);
}
