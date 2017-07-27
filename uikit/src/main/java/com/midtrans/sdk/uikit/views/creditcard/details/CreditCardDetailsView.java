package com.midtrans.sdk.uikit.views.creditcard.details;

import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;

/**
 * Created by ziahaqi on 7/12/17.
 */

public interface CreditCardDetailsView {

    void showProgressDialog();

    void hideProgressDialog();


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
}
