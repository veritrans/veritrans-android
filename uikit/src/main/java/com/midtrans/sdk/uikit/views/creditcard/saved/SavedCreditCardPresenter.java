package com.midtrans.sdk.uikit.views.creditcard.saved;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.SavedToken;
import com.midtrans.sdk.uikit.models.CreditCardTransaction;
import com.midtrans.sdk.uikit.models.CreditCardType;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 7/23/17.
 */

public class SavedCreditCardPresenter {
    private static final String TAG = SavedCreditCardPresenter.class.getSimpleName();
    private SavedCreditCardView view;
    private List<SaveCardRequest> creditCards;
    private CreditCardTransaction creditCardTransaction;


    public SavedCreditCardPresenter(Context context, SavedCreditCardView view) {
        this.view = view;
        this.creditCards = new ArrayList<>();
        creditCardTransaction = new CreditCardTransaction();
        initCreditCardTransaction(context);
        initSavedCards();
    }

    private void initCreditCardTransaction(Context context) {
        CreditCard creditCard = MidtransSDK.getInstance().getCreditCard();
        if (creditCard != null) {
            List<BankBinsResponse> bankBins = SdkUIFlowUtil.getBankBins(context);
            this.creditCardTransaction.setProperties(creditCard, new ArrayList<>(bankBins));
        }
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

    public boolean isSavedCardEnabled() {
        TransactionRequest request = MidtransSDK.getInstance().getTransactionRequest();
        if (request != null) {
            String cardClickType = request.getCardClickType();
            if (TextUtils.isEmpty(cardClickType)) {
                if (MidtransSDK.getInstance().getCreditCard().isSaveCard()) {
                    return true;
                }
            } else {
                if (cardClickType.equals(CreditCardType.ONE_CLICK) ||
                        cardClickType.equals(CreditCardType.TWO_CLICKS)) {
                    return true;
                }
            }
        }

        return false;
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
                view.onGetSavedCardTokenFailed();
                Log.d(TAG, "getCards:" + reason);
            }

            @Override
            public void onError(Throwable error) {
                view.onGetSavedCardTokenFailed();
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

    public String getBankByCardBin(String cardBin) {
        return creditCardTransaction.getBankByBin(cardBin);
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


    public void deleteSavedCard(SaveCardRequest savedCard) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK.isEnableBuiltInTokenStorage()) {
            deleteCardFromTokenStorage(savedCard);
        } else {
            List<SavedToken> savedTokens = midtransSDK.getCreditCard().getSavedTokens();
            List<SaveCardRequest> savedCards = SdkUIFlowUtil.convertSavedTokens(savedTokens);

            ArrayList<SaveCardRequest> cardList = new ArrayList<>();
            if (savedCards != null && !savedCards.isEmpty()) {
                cardList.addAll(savedCards);
                for (int i = 0; i < cardList.size(); i++) {
                    SaveCardRequest saveCard = cardList.get(i);
                    if (saveCard != null) {
                        if (!TextUtils.isEmpty(saveCard.getMaskedCard()) && saveCard.getMaskedCard().equalsIgnoreCase(savedCard.getMaskedCard())) {
                            cardList.remove(cardList.get(i));
                        }
                    }
                }
            }

            deleteCardFromMerchantServer(cardList, savedCard.getMaskedCard());
        }
    }

    private void deleteCardFromMerchantServer(ArrayList<SaveCardRequest> cardList, final String maskedCard) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        UserDetail userDetail = LocalDataHandler.readObject(UiKitConstants.KEY_USER_DETAILS, UserDetail.class);
        if (userDetail != null) {
            midtransSDK.saveCards(userDetail.getUserId(), cardList, new SaveCardCallback() {
                @Override
                public void onSuccess(SaveCardResponse response) {
                    view.onCardDeletionSuccess(maskedCard);
                }

                @Override
                public void onFailure(String reason) {
                    view.onCardDeletionFailed();
                }

                @Override
                public void onError(Throwable error) {
                    view.onCardDeletionFailed();
                }
            });
        } else {
            view.onCardDeletionFailed();
        }
    }

    private void deleteCardFromTokenStorage(final SaveCardRequest savedCard) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        midtransSDK.deleteCard(midtransSDK.readAuthenticationToken(), savedCard.getMaskedCard(), new DeleteCardCallback() {
            @Override
            public void onSuccess(Void object) {
                view.onCardDeletionSuccess(savedCard.getMaskedCard());
            }

            @Override
            public void onFailure(Void object) {
                view.onCardDeletionFailed();
            }

            @Override
            public void onError(Throwable throwable) {
                view.onCardDeletionFailed();
            }
        });
    }
}
