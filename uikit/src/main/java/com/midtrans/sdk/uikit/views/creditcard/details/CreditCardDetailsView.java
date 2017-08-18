package com.midtrans.sdk.uikit.views.creditcard.details;

import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;
import com.midtrans.sdk.uikit.abstracts.BaseCreditCardPaymentView;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;

/**
 * Created by ziahaqi on 7/12/17.
 */

public interface CreditCardDetailsView extends BasePaymentView, BaseCreditCardPaymentView {

    boolean isBankPointEnabled();

    void onGetCardTokenSuccess(TokenDetailsResponse response);

    void onGetCardTokenFailure();

    void onGetBankPointSuccess(BanksPointResponse response);

    void onGetBankPointFailure();

    void onGetTransactionStatusError(Throwable error);

    void onGetTransactionStatusFailure(TransactionResponse transactionResponse);

    void onGetTransactionStatusSuccess(TransactionResponse transactionResponse);
}
