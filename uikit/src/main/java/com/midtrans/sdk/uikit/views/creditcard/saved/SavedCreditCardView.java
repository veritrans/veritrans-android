package com.midtrans.sdk.uikit.views.creditcard.saved;

import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.uikit.abstracts.BaseCreditCardPaymentView;

import java.util.List;

/**
 * Created by ziahaqi on 7/23/17.
 */

public interface SavedCreditCardView extends BaseCreditCardPaymentView {
    void onGetSavedCardsSuccess(List<SaveCardRequest> savedCards);

    void onGetSavedCardTokenFailure();

}
