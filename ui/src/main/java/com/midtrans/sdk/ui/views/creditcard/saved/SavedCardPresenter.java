package com.midtrans.sdk.ui.views.creditcard.saved;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakawm on 3/29/17.
 */

public class SavedCardPresenter extends BasePaymentPresenter {

    private SavedCardView savedCardView;

    public List<SavedToken> getSavedCards() {
        return MidtransUi.getInstance().getTransaction().creditCard != null
                && MidtransUi.getInstance().getTransaction().creditCard.savedTokens != null
                && !MidtransUi.getInstance().getTransaction().creditCard.savedTokens.isEmpty() ?
                filterCards() : null;
    }

    public boolean isOneClick() {
        return MidtransUi.getInstance().getTransaction().creditCard != null
                && MidtransUi.getInstance().getTransaction().creditCard.saveCard
                && MidtransUi.getInstance().getTransaction().creditCard.secure;
    }

    public void sendPaymentResult(PaymentResult result) {
        Utils.sendPaymentResult(result);
    }

    private List<SavedToken> filterCards() {
        List<SavedToken> filteredCards = new ArrayList<>();
        for (SavedToken savedToken : MidtransUi.getInstance().getTransaction().creditCard.savedTokens) {
            if (isOneClick()) {
                if (isOneClickToken(savedToken)) {
                    filteredCards.add(savedToken);
                }
            } else {
                if (isTwoClicksToken(savedToken)) {
                    filteredCards.add(savedToken);
                }
            }
        }
        return filteredCards;
    }

    private boolean isOneClickToken(SavedToken savedToken) {
        return savedToken.tokenType.equalsIgnoreCase(SavedToken.ONE_CLICK);
    }

    private boolean isTwoClicksToken(SavedToken savedToken) {
        return savedToken.tokenType.equalsIgnoreCase(SavedToken.TWO_CLICKS);
    }

    public void deleteCard(final SavedToken savedToken) {
        MidtransCore.getInstance().deleteCard(MidtransUi.getInstance().getTransaction().token, savedToken.maskedCard, new MidtransCoreCallback<Void>() {
            @Override
            public void onSuccess(Void object) {
                UiUtils.removeCardFromMidtransTransaction(savedToken);
                savedCardView.onDeleteCardSuccess(savedToken);
            }

            @Override
            public void onFailure(Void object) {
                savedCardView.onDeleteCardFailure("Failed to delete card");
            }

            @Override
            public void onError(Throwable throwable) {
                savedCardView.onDeleteCardFailure(throwable.getMessage());
            }
        });
    }

    public SavedCardView getSavedCardView() {
        return savedCardView;
    }

    public void setSavedCardView(SavedCardView savedCardView) {
        this.savedCardView = savedCardView;
    }
}
