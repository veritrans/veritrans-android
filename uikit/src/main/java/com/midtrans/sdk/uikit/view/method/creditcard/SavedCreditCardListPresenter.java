package com.midtrans.sdk.uikit.view.method.creditcard;

import android.content.Context;

import com.midtrans.sdk.corekit.MidtransSdk;
import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.CreditCard;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.SavedToken;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.payment.CreditCardCharge;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.MidtransKit;
import com.midtrans.sdk.uikit.base.composer.BaseCreditCardPresenter;
import com.midtrans.sdk.uikit.utilities.Helper;
import com.midtrans.sdk.uikit.view.method.creditcard.model.CreditCardTransaction;

import java.util.ArrayList;
import java.util.List;

public class SavedCreditCardListPresenter extends BaseCreditCardPresenter<SavedCreditCardListContract> {

    private List<SaveCardRequest> creditCards;

    SavedCreditCardListPresenter(Context context, SavedCreditCardListContract view, PaymentInfoResponse paymentInfoResponse) {
        super(paymentInfoResponse);
        this.view = view;
        this.creditCards = new ArrayList<>();
        creditCardTransaction = new CreditCardTransaction();
        initCreditCardTransaction(context);
        initSavedCards();
    }

    private void initSavedCards() {
        if (isBuiltInTokenStorage()) {
            CreditCard creditCard = paymentInfoResponse.getCreditCard();
            if (creditCard != null) {
                List<SavedToken> savedTokens = creditCard.getSavedTokens();
                if (savedTokens != null && !savedTokens.isEmpty()) {
                    List<SaveCardRequest> savedCards = Helper.convertSavedTokens(savedTokens);
                    this.creditCards.addAll(savedCards);
                }
            }
        }
    }

    public boolean isBuiltInTokenStorage() {
        return MidtransKit.getInstance().isBuiltinStorageEnabled();
    }

    public void fetchSavedCards() {
        String userId = MidtransSdk.getInstance().getCheckoutTransaction().getUserId();
        CreditCardCharge.getCards(userId, new MidtransCallback<List<SaveCardRequest>>() {
            @Override
            public void onSuccess(List<SaveCardRequest> data) {
                if (data != null && !data.isEmpty()) {
                    List<SaveCardRequest> filteredSavedCards = Helper.filterMultipleSavedCard(data);
                    // Update credit cards
                    creditCards.clear();
                    creditCards.addAll(filteredSavedCards);
                    setSavedCardsToSdk(creditCards);
                }
                view.onGetSavedCardsSuccess(creditCards);
            }

            @Override
            public void onFailed(Throwable throwable) {
                Logger.error(TAG, "getCards:" + throwable.getMessage());
                view.onGetSavedCardTokenFailure();
            }
        });
    }

    private void setSavedCardsToSdk(List<SaveCardRequest> creditCards) {
        CreditCard creditCard = MidtransSdk.getInstance().getCheckoutTransaction().getCreditCard();
        List<SavedToken> savedTokens = Helper.convertSavedCards(creditCards);
        updateCreditCardAndCheckoutTransaction(creditCard, savedTokens);
    }

    private void updateCreditCardAndCheckoutTransaction(CreditCard creditCard, List<SavedToken> savedTokens) {
        CheckoutTransaction checkoutTransaction = MidtransSdk.getInstance().getCheckoutTransaction();
        CreditCard newCreditCard = CreditCard
                .builder()
                .setNewCreditCardTokens(
                        creditCard,
                        savedTokens
                )
                .build();
        CheckoutTransaction newCheckoutTransaction = CheckoutTransaction
                .builder(
                        checkoutTransaction.getTransactionDetails().getOrderId(),
                        checkoutTransaction.getTransactionDetails().getGrossAmount()
                )
                .setNewCreditCardObject(
                        newCreditCard,
                        checkoutTransaction
                )
                .build();
        MidtransSdk.getInstance().setCheckoutTransaction(newCheckoutTransaction);
    }

    public boolean isSavedCardsAvailable() {
        return creditCards != null && !creditCards.isEmpty();
    }

    public List<SaveCardRequest> getSavedCards() {
        return creditCards;
    }


    public void updateSavedCardsInstance(String maskedCardNumber) {
        CreditCard creditCard = paymentInfoResponse.getCreditCard();

        List<SavedToken> savedTokens = creditCard.getSavedTokens();

        SavedToken savedToken = findSavedCardByMaskedNumber(savedTokens, maskedCardNumber);
        if (savedToken != null) {
            savedTokens.remove(savedTokens.indexOf(savedToken));
            updateCreditCardAndCheckoutTransaction(creditCard, savedTokens);
        }

        List<SaveCardRequest> updatedCards = Helper.convertSavedTokens(savedTokens);

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

    public void deleteSavedCard(String token, SaveCardRequest request) {
        deleteSavedCard(token, request, view);
    }

}