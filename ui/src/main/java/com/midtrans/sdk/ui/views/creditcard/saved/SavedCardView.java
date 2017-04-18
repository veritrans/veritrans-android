package com.midtrans.sdk.ui.views.creditcard.saved;

import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;

/**
 * Created by rakawm on 4/3/17.
 */

public interface SavedCardView {
    void onDeleteCardSuccess(SavedToken savedToken);
    void onDeleteCardFailure(String errorMessage);

    void onCreditCardPaymentSuccess(CreditCardPaymentResponse response);

    void onCreditCardPaymentFailure(CreditCardPaymentResponse response);

    void onCreditCardPaymentError(String errorMessage);
}
