package com.midtrans.sdk.uikit.views.creditcard.details;

import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;

import java.util.List;

/**
 * Created by ziahaqi on 7/12/17.
 */

public interface CreditCardDetailsView {

    boolean isBankPointEnabled();

    void onGetCardTokenSuccess(TokenDetailsResponse response);

    void onGetCardTokenFailed();

    void onGetBankPointSuccess(BanksPointResponse response);

    void onGetBankPointFailed();

    void onPaymentSuccess(TransactionResponse response);

    void onPaymentFailed(TransactionResponse response);

    void onPaymentError(Throwable error);

    void onCardDeletionSuccess(String maskedCardNumber);

    void onCardDeletionFailed();

    void onGetTransactionStatusError(Throwable error);

    void onGetTransactionStatusFailed(TransactionResponse transactionResponse);

    void onGetTransactionStatusSuccess(TransactionResponse transactionResponse);
}
