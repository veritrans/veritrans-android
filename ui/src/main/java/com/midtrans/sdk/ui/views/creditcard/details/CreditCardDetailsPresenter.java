package com.midtrans.sdk.ui.views.creditcard.details;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.papi.CardTokenRequest;
import com.midtrans.sdk.core.models.papi.CardTokenResponse;
import com.midtrans.sdk.core.models.snap.CreditCard;
import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.core.models.snap.bins.BankBinsResponse;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentParams;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.abtracts.BasePaymentPresenter;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;

import java.util.List;

/**
 * Created by rakawm on 3/27/17.
 */

public class CreditCardDetailsPresenter extends BasePaymentPresenter {

    private MidtransCoreCallback<CardTokenResponse> getCardTokenCallback;
    private MidtransCoreCallback<CreditCardPaymentResponse> paymentCallback;
    private MidtransCoreCallback<Void> deleteCardCallback;
    private List<BankBinsResponse> bankBins;

    private CreditCardDetailsView cardDetailsView;
    private String cardToken;
    private PaymentResult<CreditCardPaymentResponse> paymentResult;
    private SavedToken savedToken;
    private boolean saveCard;
    private List<String> whitelistBins;

    public void init(Context context, CreditCardDetailsView view) {
        this.cardDetailsView = view;
        initCallbacks();
        initBankBins(context);
        initWhitelistBins();
    }

    private void initBankBins(Context context) {
        // Set from local
        bankBins = MidtransUi.getInstance().getBankBinsFromLocal(context);
        // Fetch from Snap API
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

    private void initWhitelistBins() {
        CreditCard creditCard = MidtransUi.getInstance().getTransaction().creditCard;
        if (creditCard != null) {
            whitelistBins = creditCard.whitelistBins;
        }
    }

    void startTokenize(String cardNumber, String expiryMonth, String expiryYear, String cvv) {
        SnapTransaction transaction = MidtransUi.getInstance().getTransaction();
        CreditCard creditCard = transaction.creditCard;
        if (creditCard.secure) {
            CardTokenRequest cardTokenRequest = CardTokenRequest.newNormalSecureCard(
                    cardNumber,
                    cvv,
                    expiryMonth,
                    expiryYear,
                    true,
                    MidtransUi.getInstance().getTransaction().transactionDetails.grossAmount);
            MidtransCore.getInstance().getCardToken(cardTokenRequest, getCardTokenCallback);
        } else {
            CardTokenRequest cardTokenRequest = CardTokenRequest.newNormalCard(
                    cardNumber,
                    cvv,
                    expiryMonth,
                    expiryYear
            );
            MidtransCore.getInstance().getCardToken(cardTokenRequest, getCardTokenCallback);
        }
    }

    void startTokenize(String cvv) {
        SnapTransaction snapTransaction = MidtransUi.getInstance().getTransaction();
        CreditCard creditCard = snapTransaction.creditCard;
        CardTokenRequest cardTokenRequest = CardTokenRequest.newNormalTwoClicksCard(
                savedToken.token,
                cvv,
                true,
                creditCard.secure,
                snapTransaction.transactionDetails.grossAmount
        );
        MidtransCore.getInstance().getCardToken(cardTokenRequest, getCardTokenCallback);
    }

    void startPayment() {
        MidtransCore.getInstance().paymentUsingCreditCard(
                MidtransUi.getInstance().getTransaction().token,
                isOneClickMode() ? buildOneClickCreditCardPaymentParams() : buildCreditCardPaymentParams(),
                UiUtils.buildCustomerDetails(),
                paymentCallback);
    }

    void deleteCard(final SavedToken savedToken) {
        MidtransCore.getInstance().deleteCard(
                MidtransUi.getInstance().getTransaction().token,
                savedToken.maskedCard,
                new MidtransCoreCallback<Void>() {
                    @Override
                    public void onSuccess(Void object) {
                        UiUtils.removeCardFromMidtransTransaction(savedToken);
                        cardDetailsView.onDeleteCardSuccess(savedToken);
                    }

                    @Override
                    public void onFailure(Void object) {
                        cardDetailsView.onDeleteCardFailure("Failed to delete card");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        cardDetailsView.onDeleteCardFailure(throwable.getMessage());
                    }
                }
        );
    }

    private void initCallbacks() {
        getCardTokenCallback = new MidtransCoreCallback<CardTokenResponse>() {
            @Override
            public void onSuccess(CardTokenResponse object) {
                setCardToken(object.tokenId);
                cardDetailsView.onGetCardTokenSuccess(object);
            }

            @Override
            public void onFailure(CardTokenResponse object) {
                cardDetailsView.onGetCardTokenFailure(object);
            }

            @Override
            public void onError(Throwable throwable) {
                cardDetailsView.onGetCardTokenError(throwable.getMessage());
            }
        };

        paymentCallback = new MidtransCoreCallback<CreditCardPaymentResponse>() {
            @Override
            public void onSuccess(CreditCardPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                cardDetailsView.onCreditCardPaymentSuccess(object);
            }

            @Override
            public void onFailure(CreditCardPaymentResponse object) {
                paymentResult = new PaymentResult<>(object);
                cardDetailsView.onCreditCardPaymentFailure(object);
            }

            @Override
            public void onError(Throwable throwable) {
                paymentResult = new PaymentResult<>(throwable.getMessage());
                cardDetailsView.onCreditCardPaymentError(throwable);
            }
        };
    }

    public String getCardToken() {
        return cardToken;
    }

    public void setCardToken(String cardToken) {
        this.cardToken = cardToken;
    }

    public PaymentResult<CreditCardPaymentResponse> getPaymentResult() {
        return paymentResult;
    }

    public SavedToken getSavedToken() {
        return savedToken;
    }

    public void setSavedToken(SavedToken savedToken) {
        this.savedToken = savedToken;
    }

    public boolean isSaveCard() {
        return saveCard;
    }

    public void setSaveCard(boolean saveCard) {
        this.saveCard = saveCard;
    }

    private CreditCardPaymentParams buildCreditCardPaymentParams() {
        CreditCardPaymentParams creditCardPaymentParams = CreditCardPaymentParams.newBasicPaymentParams(cardToken);
        creditCardPaymentParams.setSaveCard(saveCard);
        return creditCardPaymentParams;
    }

    private CreditCardPaymentParams buildOneClickCreditCardPaymentParams() {
        return CreditCardPaymentParams.newOneClickPaymentParams(savedToken.maskedCard);
    }

    public boolean isTwoClicksMode() {
        CreditCard creditCard = MidtransUi.getInstance().getTransaction().creditCard;
        return creditCard != null && creditCard.saveCard;
    }

    public boolean isOneClickMode() {
        CreditCard creditCard = MidtransUi.getInstance().getTransaction().creditCard;
        return isTwoClicksMode() && creditCard.secure;
    }

    public List<BankBinsResponse> getBankBins() {
        return bankBins;
    }

    public List<String> getWhitelistBins() {
        return whitelistBins;
    }

    public boolean isWhitelistBinsAvailable() {
        return whitelistBins != null
                && !whitelistBins.isEmpty();
    }

    public boolean isCardBinLockingValid(String cardNumber) {
        return !TextUtils.isEmpty(cardNumber)
                && cardNumber.length() >= 7
                && isCardBinValid(cardNumber);
    }

    private boolean isCardBinValid(String cardNumber) {
        String cardBin = cardNumber.replace(" ", "").substring(0, 6);
        return isInWhiteList(cardBin);
    }

    private boolean isInWhiteList(String cardBin) {
        CreditCard creditCard = MidtransUi.getInstance().getTransaction().creditCard;
        return creditCard.whitelistBins.contains(cardBin);
    }
}
