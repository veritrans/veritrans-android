package com.midtrans.sdk.ui.views.creditcard.details;

import com.midtrans.sdk.core.models.papi.CardTokenResponse;
import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;

/**
 * Created by rakawm on 3/27/17.
 */

public interface CreditCardDetailsView {

    void onGetCardTokenSuccess(CardTokenResponse response);

    void onGetCardTokenFailure(CardTokenResponse response);

    void onGetCardTokenError(String message);

    void onCreditCardPaymentError(Throwable throwable);

    void onCreditCardPaymentFailure(CreditCardPaymentResponse response);

    void onCreditCardPaymentSuccess(CreditCardPaymentResponse response);

    void onDeleteCardSuccess(SavedToken savedToken);

    void onDeleteCardFailure(String message);

    String getMaskedCardNumber();

    boolean isSaveEnabled();
}
