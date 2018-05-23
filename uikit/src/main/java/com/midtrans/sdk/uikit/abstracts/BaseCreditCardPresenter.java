package com.midtrans.sdk.uikit.abstracts;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
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
 * Created by ziahaqi on 7/28/17.
 */

public class BaseCreditCardPresenter<V extends BaseView> extends BasePaymentPresenter<V> {

    protected CreditCardTransaction creditCardTransaction;

    protected void initCreditCardTransaction(Context context) {
        CreditCard creditCard = getMidtransSDK().getCreditCard();
        if (creditCard != null) {
            List<BankBinsResponse> bankBins = SdkUIFlowUtil.getBankBins(context);
            this.creditCardTransaction.setProperties(creditCard, new ArrayList<>(bankBins));
        }
    }

    public String getBankByCardBin(String cardBin) {
        return creditCardTransaction.getBankByBin(cardBin);
    }

    public boolean isSavedCardEnabled() {
        TransactionRequest request = getMidtransSDK().getTransactionRequest();
        boolean saveCard = getMidtransSDK().getCreditCard().isSaveCard();
        if (request != null) {
            String cardClickType = request.getCardClickType();
            if (!TextUtils.isEmpty(cardClickType)) {
                if (cardClickType.equals(CreditCardType.ONE_CLICK) ||
                        cardClickType.equals(CreditCardType.TWO_CLICKS)) {
                    saveCard = true;
                }
            }
        }

        return saveCard;
    }

    protected void deleteSavedCard(SaveCardRequest savedCard, BaseCreditCardPaymentView view) {
        if (getMidtransSDK().isEnableBuiltInTokenStorage()) {
            deleteCardFromTokenStorage(savedCard, view);
        } else {
            List<SavedToken> savedTokens = getMidtransSDK().getCreditCard().getSavedTokens();
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

            deleteCardFromMerchantServer(cardList, savedCard.getMaskedCard(), view);
        }
    }

    private void deleteCardFromMerchantServer(ArrayList<SaveCardRequest> cardList, final String maskedCard, final BaseCreditCardPaymentView view) {
        UserDetail userDetail = LocalDataHandler.readObject(UiKitConstants.KEY_USER_DETAILS, UserDetail.class);
        if (userDetail != null) {
            getMidtransSDK().saveCards(userDetail.getUserId(), cardList, new SaveCardCallback() {
                @Override
                public void onSuccess(SaveCardResponse response) {
                    view.onDeleteCardSuccess(maskedCard);
                }

                @Override
                public void onFailure(String reason) {
                    view.onDeleteCardFailure();
                }

                @Override
                public void onError(Throwable error) {
                    view.onDeleteCardFailure();
                }
            });
        } else {
            view.onDeleteCardFailure();
        }
    }

    protected void deleteCardFromTokenStorage(final SaveCardRequest savedCard, final BaseCreditCardPaymentView view) {

        getMidtransSDK().deleteCard(getMidtransSDK().readAuthenticationToken(), savedCard.getMaskedCard(), new DeleteCardCallback() {
            @Override
            public void onSuccess(Void object) {
                view.onDeleteCardSuccess(savedCard.getMaskedCard());
            }

            @Override
            public void onFailure(Void object) {
                view.onDeleteCardFailure();
            }

            @Override
            public void onError(Throwable throwable) {
                view.onDeleteCardFailure();
            }
        });
    }
}
