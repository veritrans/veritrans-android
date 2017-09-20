package com.midtrans.sdk.uikit.views.creditcard.saved;

import android.content.Context;
import android.util.Log;

import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.SavedToken;
import com.midtrans.sdk.uikit.abstracts.BaseCreditCardPresenter;
import com.midtrans.sdk.uikit.models.CreditCardTransaction;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 7/23/17.
 */

public class SavedCreditCardPresenter extends BaseCreditCardPresenter<SavedCreditCardView> {
    private static final String TAG = SavedCreditCardPresenter.class.getSimpleName();
    private List<SaveCardRequest> creditCards;


    public SavedCreditCardPresenter(Context context, SavedCreditCardView view) {
        super();
        this.view = view;
        this.creditCards = new ArrayList<>();
        creditCardTransaction = new CreditCardTransaction();
        initCreditCardTransaction(context);
        initSavedCards();
    }



    private void initSavedCards() {
        if (isBuiltInTokenStorage()) {
            CreditCard creditCard = MidtransSDK.getInstance().getCreditCard();
            if (creditCard != null) {
                List<SavedToken> savedTokens = creditCard.getSavedTokens();
                if (savedTokens != null && !savedTokens.isEmpty()) {
                    List<SaveCardRequest> savedCards = SdkUIFlowUtil.convertSavedTokens(savedTokens);
                    this.creditCards.addAll(savedCards);
                }
            }
        }
    }

    public boolean isBuiltInTokenStorage() {
        return MidtransSDK.getInstance().isEnableBuiltInTokenStorage();
    }


    public void fetchSavedCards() {
        UserDetail userDetail = LocalDataHandler.readObject(UiKitConstants.KEY_USER_DETAILS, UserDetail.class);
        MidtransSDK.getInstance().getCards(userDetail.getUserId(), new GetCardCallback() {
            @Override
            public void onSuccess(ArrayList<SaveCardRequest> response) {
                if (response != null && !response.isEmpty()) {
                    List<SaveCardRequest> filteredSavedCards = SdkUIFlowUtil.filterMultipleSavedCard(response);
                    // Update credit cards
                    creditCards.clear();
                    creditCards.addAll(filteredSavedCards);
                    setSavedCardsToSdk(creditCards);
                }
                view.onGetSavedCardsSuccess(creditCards);
            }

            @Override
            public void onFailure(String reason) {
                view.onGetSavedCardTokenFailure();
                Log.d(TAG, "getCards:" + reason);
            }

            @Override
            public void onError(Throwable error) {
                view.onGetSavedCardTokenFailure();
                Log.e(TAG, "getCards:" + error.getMessage());
            }
        });
    }

    private void setSavedCardsToSdk(List<SaveCardRequest> creditCards) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        CreditCard creditCard = midtransSDK.getCreditCard();
        List<SavedToken> savedTokens = SdkUIFlowUtil.convertSavedCards(creditCards);
        creditCard.setSavedTokens(savedTokens);
    }

    public boolean isSavedCardsAvailable() {
        return creditCards != null && !creditCards.isEmpty();
    }

    public List<SaveCardRequest> getSavedCards() {
        return creditCards;
    }


    public void updateSavedCardsInstance(String maskedCardNumber) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();

        CreditCard creditCard = MidtransSDK.getInstance().getCreditCard();

        List<SavedToken> savedTokens = creditCard.getSavedTokens();

        SavedToken savedToken = findSavedCardByMaskedNumber(savedTokens, maskedCardNumber);
        if (savedToken != null) {
            savedTokens.remove(savedTokens.indexOf(savedToken));
            creditCard.setSavedTokens(savedTokens);
            midtransSDK.setCreditCard(creditCard);
        }

        List<SaveCardRequest> updatedCards = SdkUIFlowUtil.convertSavedTokens(savedTokens);

        this.creditCards.clear();
        this.creditCards.addAll(updatedCards);
    }


    private SavedToken findSavedCardByMaskedNumber(List<SavedToken> savedTokens, String maskedCard) {
        for (SavedToken savedToken : savedTokens) {
            if (savedToken.getMaskedCard().equals(maskedCard)) {
                return savedToken;
            }
        }
        return null;
    }


    public void deleteSavedCard(SaveCardRequest request) {
        deleteSavedCard(request, view);
    }
}
