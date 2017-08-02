package com.midtrans.sdk.uikit.views.creditcard.details;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.callback.BankBinsCallback;
import com.midtrans.sdk.corekit.callback.BanksPointCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.GetTransactionStatusCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.SavedToken;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionStatusResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.models.CreditCardTransaction;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 7/12/17.
 */

public class CreditCardDetailsPresenter {
    private static final String TAG = CreditCardDetailsPresenter.class.getSimpleName();
    private CreditCardDetailsView view;
    private Context context;
    CreditCardTransaction creditCardTransaction;
    private TokenDetailsResponse creditCardToken;
    private TransactionResponse transactionResponse;
    private CardTokenRequest cardTokenRequest;

    private int installmentTotalPositions;
    private int installmentCurrentPosition;

    public CreditCardDetailsPresenter(Context context, CreditCardDetailsView view) {
        this.view = view;
        this.creditCardTransaction = new CreditCardTransaction();
        initCreditCardTransaction(context);
    }

    public void initCreditCardTransaction(Context context) {
        CreditCard creditCard = MidtransSDK.getInstance().getCreditCard();
        List<BankBinsResponse> bankBins = SdkUIFlowUtil.getBankBins(context);
        this.context = context;
        this.creditCardTransaction.setProperties(creditCard, new ArrayList<>(bankBins));
        fetchBankBins();
    }


    private void fetchBankBins() {
        MidtransSDK.getInstance().getBankBins(new BankBinsCallback() {
            @Override
            public void onSuccess(ArrayList<BankBinsResponse> response) {
                creditCardTransaction.setBankBins(response);
            }

            @Override
            public void onFailure(String reason) {
                // do nothing
            }

            @Override
            public void onError(Throwable error) {
                // do nothing
            }
        });
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
                if (cardClickType.equals(context.getString(R.string.card_click_type_one_click)) ||
                        cardClickType.equals(context.getString(R.string.card_click_type_two_click))) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean isSecurePayment() {
        TransactionRequest request = MidtransSDK.getInstance().getTransactionRequest();
        if (request != null) {
            String cardClickType = request.getCardClickType();
            if (TextUtils.isEmpty(cardClickType)) {
                if (MidtransSDK.getInstance().getCreditCard().isSecure()) {
                    return true;
                }
            } else if (request.isSecureCard()) {
                return true;
            }
        }

        return false;
    }


    public String getBankByCardBin(String cardBin) {
        return creditCardTransaction.getBankByBin(cardBin);
    }

    public boolean isMandiriDebitCard(String cardBin) {
        return creditCardTransaction.isMandiriCardDebit(cardBin);
    }

    public void trackEvent(String eventName, String cardPaymentMode) {
        MidtransSDK.getInstance().trackEvent(eventName, cardPaymentMode);
    }

    public boolean isWhitelistBinsAvailable() {
        return creditCardTransaction.isWhiteListBinsAvailable();
    }

    public boolean isCardBinLockingValid(String cardNumber) {
        return creditCardTransaction.checkCardBinValidity(cardNumber);
    }

    public boolean isInstallmentValid() {
        return creditCardTransaction.isInstallmentValid();
    }


    public void getCardToken(String cardNumber, String month, String year, String cvv, boolean savedCard) {

    }


    public Integer getGrossAmount() {
        Transaction transacton = MidtransSDK.getInstance().getTransaction();
        if (transacton != null) {
            return transacton.getTransactionDetails().getAmount();
        }
        return 0;
    }


    public void startGettingCardToken(String cardNumber, String month, String year, String cvv, boolean saveCard) {
        CardTokenRequest cardTokenRequest = new CardTokenRequest(
                cardNumber,
                cvv,
                month,
                year,
                MidtransSDK.getInstance().getClientKey());

        cardTokenRequest.setIsSaved(saveCard);
        cardTokenRequest.setSecure(isSecurePayment());
        cardTokenRequest.setGrossAmount(getGrossAmount());

        //apply installment, acquiring bank and bank point
        applyTokenizationProperties(cardTokenRequest);
        getCardToken(cardTokenRequest);
    }

    private void applyTokenizationProperties(CardTokenRequest cardTokenRequest) {
        applyInstallmentProperties(cardTokenRequest);
        cardTokenRequest.setPoint(view.isBankPointEnabled());
        CreditCard creditCard = MidtransSDK.getInstance().getCreditCard();
        if (creditCard != null) {
            applyAcquiringBank(cardTokenRequest, creditCard);
            applyTokenizationType(cardTokenRequest, creditCard);
        }
    }

    private void applyAcquiringBank(CardTokenRequest cardTokenRequest, CreditCard creditCard) {
        // Set acquiring bank and channel if available
        String bank = creditCard.getBank();
        String channel = creditCard.getChannel();
        cardTokenRequest.setBank(bank);
        cardTokenRequest.setChannel(channel);
    }

    private void applyTokenizationType(CardTokenRequest cardTokenRequest, CreditCard creditCard) {
        cardTokenRequest.setType(creditCard.getType());
    }

    private void applyInstallmentProperties(CardTokenRequest cardTokenRequest) {
        int termSelected = creditCardTransaction.getInstallmentTermSelected();
        Logger.d(TAG, "applyInstallmentProperties()>term:" + termSelected);

        if (termSelected > 0) {
            cardTokenRequest.setInstallment(true);
            cardTokenRequest.setInstalmentTerm(termSelected);
        }
    }

    public void startNormalPayment(boolean saveCard) {
        CreditCardPaymentModel model = new CreditCardPaymentModel(creditCardToken.getTokenId(), saveCard);
        applyPaymentProperties(model);
        startCreditCardPayment(model);
    }


    private void saveCrediCard(TransactionResponse response) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (!midtransSDK.isEnableBuiltInTokenStorage()) {
            if (this.cardTokenRequest != null && this.cardTokenRequest.isSaved()) {
                List<SavedToken> savedTokens = midtransSDK.getCreditCard().getSavedTokens();
                List<SaveCardRequest> savedCards = SdkUIFlowUtil.convertSavedTokens(savedTokens);

                String cardType = context.getString(R.string.card_click_type_two_click);
                String maskedCard = createMaskedCard(cardTokenRequest.getCardNumber());
                SaveCardRequest oldSavedCard = findCardByMaskedNumber(maskedCard, savedCards);

                if (oldSavedCard != null) {
                    savedCards.remove(oldSavedCard);
                }

                SaveCardRequest saveCardRequest = new SaveCardRequest(response.getSavedTokenId(),
                        maskedCard, cardType);
                savedCards.add(saveCardRequest);
                SdkUIFlowUtil.filterMultipleSavedCard(savedCards);
                startSavingCreditCards(savedCards);
            }
        }
    }

    private String createMaskedCard(String cardNumber) {
        if (!TextUtils.isEmpty(cardNumber)) {
            String firstPart = cardTokenRequest.getCardNumber().replace(" ", "").substring(0, 6);
            String secondPart = cardTokenRequest.getCardNumber().replace(" ", "").substring(12);
            return firstPart + "-" + secondPart;
        }

        return null;
    }

    private SaveCardRequest findCardByMaskedNumber(String maskedCard, List<SaveCardRequest> savedCards) {
        if (savedCards != null && !savedCards.isEmpty()) {
            for (SaveCardRequest card : savedCards) {
                if (card.getMaskedCard().equals(maskedCard)) {
                    return card;
                }
            }
        }
        return null;
    }

    private void applyPaymentProperties(CreditCardPaymentModel paymentModel) {
        // set installment properties
        int installmentTermSelected = creditCardTransaction.getInstallmentTermSelected();
        String installmentBankSeleted = creditCardTransaction.getInstallmentBankSelected();
        if (installmentTermSelected > 0) {
            paymentModel.setInstallment(installmentBankSeleted + "_" + installmentTermSelected);
        }

        //set bank point
        if (creditCardTransaction.getBankPointRedeemed() != 0.0f) {
            paymentModel.setPointRedeemed(creditCardTransaction.getBankPointRedeemed());
        }
    }

    public boolean isShowPaymentStatus() {
        return MidtransSDK.getInstance().getUIKitCustomSetting().isShowPaymentStatus();
    }


    public TransactionResponse getTransactionResponse() {
        return this.transactionResponse;
    }

    public boolean isSaveCardOptionChecked() {
        return MidtransSDK.getInstance().getUIKitCustomSetting().isSaveCardChecked();
    }

    public void startOneClickPayment(String maskedCard) {
        CreditCardPaymentModel paymentModel = new CreditCardPaymentModel(maskedCard);
        startCreditCardPayment(paymentModel);
    }

    public void startGettingCardToken(String savedCardTokenId, String cvv) {
        CardTokenRequest request = new CardTokenRequest();
        request.setSavedTokenId(savedCardTokenId);
        request.setCardCVV(cvv);
        request.setGrossAmount(getGrossAmount());
        request.setSecure(isSecurePayment());
        request.setTwoClick(true);
        request.setSecure(isSecurePayment());
        request.setClientKey(MidtransSDK.getInstance().getClientKey());

        applyTokenizationProperties(request);
        getCardToken(request);
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
        midtransSDK.saveCards(userDetail.getUserId(), cardList, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {
                SdkUIFlowUtil.hideProgressDialog();
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
    }

    private void deleteCardFromTokenStorage(final SaveCardRequest savedCard) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        midtransSDK.deleteCard(midtransSDK.readAuthenticationToken(), savedCard.getMaskedCard(), new DeleteCardCallback() {
            @Override
            public void onSuccess(Void object) {
                SdkUIFlowUtil.hideProgressDialog();
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

    private void startSavingCreditCards(List<SaveCardRequest> saveCardRequest) {
        UserDetail userDetail = LocalDataHandler.readObject(Constants.KEY_USER_DETAILS, UserDetail.class);
        MidtransSDK.getInstance().saveCards(userDetail.getUserId(), new ArrayList<>(saveCardRequest),
                new SaveCardCallback() {
                    @Override
                    public void onSuccess(SaveCardResponse response) {
                        Log.d(TAG, "savecards:success");
                    }

                    @Override
                    public void onFailure(String reason) {
                        Log.d(TAG, "savecards:failed");
                    }

                    @Override
                    public void onError(Throwable error) {
                        Log.d(TAG, "savecards:error");
                    }
                });
    }

    private void startCreditCardPayment(CreditCardPaymentModel model) {
        String authenticationToken = MidtransSDK.getInstance().readAuthenticationToken();
        MidtransSDK.getInstance().paymentUsingCard(authenticationToken, model, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                transactionResponse = response;
                saveCrediCard(response);
                view.onPaymentSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                transactionResponse = response;
                view.onPaymentFailed(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onPaymentError(error);
            }
        });
    }


    private void getCardToken(CardTokenRequest cardTokenRequest) {
        this.cardTokenRequest = cardTokenRequest;
        MidtransSDK.getInstance().getCardToken(cardTokenRequest, new CardTokenCallback() {
            @Override
            public void onSuccess(TokenDetailsResponse response) {
                creditCardToken = response;
                view.onGetCardTokenSuccess(response);
            }

            @Override
            public void onFailure(TokenDetailsResponse response, String reason) {
                view.onGetCardTokenFailed();
            }

            @Override
            public void onError(Throwable error) {
                view.onGetCardTokenFailed();
            }
        });
    }


    public void getBankPoint(final String bankType) {
        MidtransSDK.getInstance().getBanksPoint(creditCardToken.getTokenId(), new BanksPointCallback() {
            @Override
            public void onSuccess(BanksPointResponse response) {
                creditCardTransaction.setBankPoint(response, bankType);
                view.onGetBankPointSuccess(response);
            }

            @Override
            public void onFailure(String reason) {
                view.onGetBankPointFailed();
            }

            @Override
            public void onError(Throwable error) {
                view.onGetBankPointFailed();
            }
        });
    }

    public void trackEvent(String eventName) {
        MidtransSDK.getInstance().trackEvent(eventName);
    }

    public boolean isInstallmentAvailable() {
        return creditCardTransaction.isInstallmentAvailable();
    }

    public boolean isCardBinValidForBankChecking(String cardNumber) {
        return !TextUtils.isEmpty(cardNumber) && cardNumber.length() > 5;
    }

    public ArrayList<Integer> getInstallmentTermsByCardBin(String cardBin) {
        return creditCardTransaction.getInstallmentTerms(cardBin);
    }

    public void initInstallmentTerms(ArrayList<Integer> installmentTerms) {
        installmentCurrentPosition = 0;
        installmentTotalPositions = installmentTerms.size() - 1;
    }

    public int getCurrentInstallmentTerm() {
        return creditCardTransaction.getInstallmentTerm(installmentCurrentPosition);
    }

    public void setCurrentInstallment(int currentInstallmentPosition) {
        this.installmentCurrentPosition = currentInstallmentPosition;
        creditCardTransaction.setInstallment(currentInstallmentPosition);
    }

    public int getInstallmentCurrentPosition() {
        return installmentCurrentPosition;
    }

    public int getInstallmentTotalPositions() {
        return installmentTotalPositions;
    }

    public boolean isBankPointAvailable(String cardBin) {
        String bank = creditCardTransaction.getBankByBin(cardBin);
        Transaction transaction = MidtransSDK.getInstance().getTransaction();

        return !TextUtils.isEmpty(bank)
                && transaction.getMerchantData().getPointBanks().contains(bank)
                && bank.equals(BankType.BNI);
    }

    public void startBankPointsPayment(float redeemedPoint, boolean isSaveCard) {
        creditCardTransaction.setBankPointRedeemed(redeemedPoint);
        startNormalPayment(isSaveCard);
    }

    public boolean isCardBinInWhiteList(String cardBin) {
        return creditCardTransaction.isInWhiteList(cardBin);
    }

    public boolean isRbaAuthentication(TransactionResponse response) {
        if (response != null) {
            String transactionStatus = response.getTransactionStatus();
            if (!TextUtils.isEmpty(transactionStatus) && transactionStatus.equals(UiKitConstants.STATUS_PENDING)) {
                return true;
            }
        }
        return false;
    }

    public void getPaymentStatus() {
        String snapToken = MidtransSDK.getInstance().readAuthenticationToken();
        MidtransSDK.getInstance().getTransactionStatus(snapToken, new GetTransactionStatusCallback() {
            @Override
            public void onSuccess(TransactionStatusResponse response) {

                TransactionResponse transactionResponse = convertTransactionStatus(response);
                CreditCardDetailsPresenter.this.transactionResponse = transactionResponse;

                view.onGetTransactionStatusSuccess(transactionResponse);
            }

            @Override
            public void onFailure(TransactionStatusResponse response, String reason) {

                TransactionResponse transactionResponse = convertTransactionStatus(response);
                CreditCardDetailsPresenter.this.transactionResponse = transactionResponse;
                view.onGetTransactionStatusFailed(transactionResponse);
            }

            @Override
            public void onError(Throwable error) {
                view.onGetTransactionStatusError(error);
            }
        });
    }

    private TransactionResponse convertTransactionStatus(TransactionStatusResponse response) {
        TransactionResponse transactionResponse = new TransactionResponse(
                response.getStatusCode(), response.getStatusMessage(), response.getTransactionId(),
                response.getOrderId(), response.getGrossAmount(), response.getPaymentType(),
                response.getTransactionTime(), response.getTransactionStatus());
        this.transactionResponse = transactionResponse;
        return transactionResponse;
    }
}
