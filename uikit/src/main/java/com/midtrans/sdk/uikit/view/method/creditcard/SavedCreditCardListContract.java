package com.midtrans.sdk.uikit.view.method.creditcard;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;
import com.midtrans.sdk.uikit.base.contract.BaseCreditCardContract;

import java.util.List;

public interface SavedCreditCardListContract extends BaseCreditCardContract {

    void onGetSavedCardsSuccess(List<SaveCardRequest> savedCards);

    void onGetSavedCardTokenFailure();
}