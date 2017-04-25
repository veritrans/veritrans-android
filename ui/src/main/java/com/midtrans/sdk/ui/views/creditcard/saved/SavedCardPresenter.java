package com.midtrans.sdk.ui.views.creditcard.saved;

import android.content.Context;

import com.midtrans.sdk.analytics.MidtransAnalytics;
import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.CreditCard;
import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.core.models.snap.bins.BankBinsResponse;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentParams;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.models.SavedCard;
import com.midtrans.sdk.ui.utils.CreditCardUtils;
import com.midtrans.sdk.ui.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakawm on 3/29/17.
 */

public class SavedCardPresenter extends BasePaymentPresenter {
    private PaymentResult<CreditCardPaymentResponse> paymentResult;

    private SavedCardView savedCardView;
    private List<BankBinsResponse> bankBins;

    public SavedCardPresenter(Context context) {
        initBankBins(context);
    }

    private void initBankBins(Context context) {
        bankBins = MidtransUi.getInstance().getBankBinsFromLocal(context);
        MidtransCore.getInstance().getBankBins(new MidtransCoreCallback<List<BankBinsResponse>>() {
            @Override
            public void onSuccess(List<BankBinsResponse> object) {
                if (object != null && !object.isEmpty()) {
                    bankBins = object;
                }
            }

            @Override
            public void onFailure(List<BankBinsResponse> object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    public List<SavedCard> getSavedCards() {
        CreditCard creditCard = MidtransUi.getInstance().getTransaction().creditCard;
        if (creditCard != null) {
            if (creditCard.savedTokens != null && !creditCard.savedTokens.isEmpty()) {
                return includeBankIntoSavedCard();
            }
        }
        return null;
    }

    private List<SavedCard> includeBankIntoSavedCard() {
        List<SavedToken> savedTokens = filterCards();
        List<SavedCard> savedCards = new ArrayList<>();
        for (SavedToken savedToken : savedTokens) {
            SavedCard savedCard = new SavedCard();
            savedCard.setSavedToken(savedToken);
            String cardBin = savedToken.maskedCard.substring(0, 6);
            savedCard.setBank(CreditCardUtils.getBankByBin(bankBins, cardBin));

            savedCards.add(savedCard);
        }
        return savedCards;
    }

    public boolean isOneClick() {
        return MidtransUi.getInstance().getTransaction().creditCard != null
                && MidtransUi.getInstance().getTransaction().creditCard.saveCard
                && MidtransUi.getInstance().getTransaction().creditCard.secure;
    }

    public void startPayment(final SavedCard savedCard) {
        String checkoutToken = MidtransUi.getInstance().getTransaction().token;
        MidtransCore.getInstance().paymentUsingCreditCard(checkoutToken, buildOneClickCreditCardPaymentParams(savedCard.getSavedToken()), UiUtils.buildCustomerDetails(), new MidtransCoreCallback<CreditCardPaymentResponse>() {
            @Override
            public void onSuccess(CreditCardPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                savedCardView.onCreditCardPaymentSuccess(object);

                trackEvent(AnalyticsEventName.PAGE_STATUS_SUCCESS, MidtransAnalytics.CARD_MODE_ONE_CLICK);
            }

            @Override
            public void onFailure(CreditCardPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                savedCardView.onCreditCardPaymentFailure(object);

                // track 3DS validation error
                CreditCardUtils.trackCreditCardFailure(object, savedCard.getSavedToken());
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                savedCardView.onCreditCardPaymentError(throwable.getMessage());
            }
        });
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

    public List<BankBinsResponse> getBankBins() {
        return bankBins;
    }

    public void setBankBins(List<BankBinsResponse> bankBins) {
        this.bankBins = bankBins;
    }

    private CreditCardPaymentParams buildOneClickCreditCardPaymentParams(SavedToken savedToken) {
        return CreditCardPaymentParams.newOneClickPaymentParams(savedToken.maskedCard);
    }

    public PaymentResult<CreditCardPaymentResponse> getPaymentResult() {
        return paymentResult;
    }
}
