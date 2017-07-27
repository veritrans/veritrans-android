package com.midtrans.sdk.uikit.views.creditcard.saved;

import com.midtrans.sdk.corekit.models.SaveCardRequest;

import java.util.List;

/**
 * Created by ziahaqi on 7/23/17.
 */

public interface SavedCreditCardView {
    void onGetSavedCardsSuccess(List<SaveCardRequest> savedCards);

    void onGetSavedCardTokenFailed();
}
