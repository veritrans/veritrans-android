package com.midtrans.sdk.uikit.base.composer;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.MidtransSdk;
import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.CreditCard;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.SavedToken;
import com.midtrans.sdk.corekit.core.api.merchant.model.savecard.SaveCardResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.bins.BankBinsResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.payment.CreditCardCharge;
import com.midtrans.sdk.uikit.MidtransKit;
import com.midtrans.sdk.uikit.base.contract.BaseContract;
import com.midtrans.sdk.uikit.base.contract.BaseCreditCardContract;
import com.midtrans.sdk.uikit.utilities.Helper;
import com.midtrans.sdk.uikit.view.method.creditcard.model.CreditCardTransaction;

import java.util.ArrayList;
import java.util.List;

public class BaseCreditCardPresenter<V extends BaseContract> extends BasePaymentPresenter<V> {

    protected CreditCardTransaction creditCardTransaction;
    protected PaymentInfoResponse paymentInfoResponse;

    public BaseCreditCardPresenter(PaymentInfoResponse paymentInfoResponse) {
        this.paymentInfoResponse = paymentInfoResponse;
    }

    protected void initCreditCardTransaction(Context context) {
        CreditCard creditCard = paymentInfoResponse.getCreditCard();
        if (creditCard != null) {
            List<BankBinsResponse> bankBins = Helper.getBankBins(context);
            this.creditCardTransaction.setProperties(creditCard, new ArrayList<>(bankBins));
        }
    }

    public String getBankByCardBin(String cardBin) {
        return creditCardTransaction.getBankByBin(cardBin);
    }

    /**
     * ONE_CLICK = Authentication.AUTH_3DS && saved card true
     * TWO_CLICK = saved card true
     * So it can simplified with only checking saved card
     */
    public boolean isSavedCardEnabled() {
        boolean saveCard = false;
        if (paymentInfoResponse != null) {
            saveCard = paymentInfoResponse.getCreditCard().isSaveCard();
        }
        return saveCard;
    }

    protected void deleteSavedCard(String token, SaveCardRequest savedCard, BaseCreditCardContract view) {
        if (MidtransKit.getInstance().isBuiltinStorageEnabled()) {
            deleteCardFromTokenStorage(token, savedCard, view);
        } else {
            List<SavedToken> savedTokens = MidtransSdk.getInstance().getCheckoutTransaction().getCreditCard().getSavedTokens();
            List<SaveCardRequest> savedCards = Helper.convertSavedTokens(savedTokens);

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

    private void deleteCardFromMerchantServer(ArrayList<SaveCardRequest> cardList, final String maskedCard, final BaseCreditCardContract view) {
        String userId = MidtransSdk.getInstance().getCheckoutTransaction().getUserId();
        if (userId != null) {
            CreditCardCharge.saveCards(userId, cardList, new MidtransCallback<SaveCardResponse>() {
                @Override
                public void onSuccess(SaveCardResponse data) {
                    view.onDeleteCardSuccess(maskedCard);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    view.onDeleteCardFailure();
                }
            });
        } else {
            view.onDeleteCardFailure();
        }
    }

    protected void deleteCardFromTokenStorage(final String token, final SaveCardRequest savedCard, final BaseCreditCardContract view) {
        CreditCardCharge.deleteCard(token, savedCard.getMaskedCard(), new MidtransCallback<Void>() {
            @Override
            public void onSuccess(Void data) {
                view.onDeleteCardSuccess(savedCard.getMaskedCard());
            }

            @Override
            public void onFailed(Throwable throwable) {
                view.onDeleteCardFailure();
            }
        });
    }

}