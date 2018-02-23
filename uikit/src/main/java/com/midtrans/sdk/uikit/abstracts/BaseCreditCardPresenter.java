package com.midtrans.sdk.uikit.abstracts;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.SavedToken;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.uikit.models.CreditCardTransaction;
import com.midtrans.sdk.uikit.models.CreditCardType;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

import java.io.InputStream;
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
            // todo blacklist
            //todo blacklist
            initblacklist(creditCard, context);

            List<BankBinsResponse> bankBins = SdkUIFlowUtil.getBankBins(context);
            this.creditCardTransaction.setProperties(creditCard, new ArrayList<>(bankBins));
        }
    }

    private void initblacklist(CreditCard creditCard, Context context) {
        String data;
        List<String> mockblacklist = null;
        List<String> mockwhitelist = null;
        try {
            InputStream is = context.getAssets().open("mock_blacklist.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            data = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            mockblacklist = gson.fromJson(data, Transaction.class).getCreditCard().getBlacklistBins();
            mockwhitelist = gson.fromJson(data, Transaction.class).getCreditCard().getWhitelistBins();
            Logger.i("bankDetails>fromfile:" + data);


        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }

        if (mockblacklist != null && !mockblacklist.isEmpty()) {
            creditCard.setBlacklistBins(mockblacklist);
        } else {
            Log.d("xbl", "mockempty");
        }

        if (mockwhitelist != null && !mockwhitelist.isEmpty()) {
            creditCard.setWhiteListBins(new ArrayList<>(mockwhitelist));
        }
    }

    public String getBankByCardBin(String cardBin) {
        return creditCardTransaction.getBankByBin(cardBin);
    }

    public boolean isSavedCardEnabled() {
        TransactionRequest request = getMidtransSDK().getTransactionRequest();
        if (request != null) {
            String cardClickType = request.getCardClickType();
            if (TextUtils.isEmpty(cardClickType)) {
                if (getMidtransSDK().getCreditCard().isSaveCard()) {
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
