package com.midtrans.sdk.uikit.views.creditcard.details;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.callback.BankBinsCallback;
import com.midtrans.sdk.corekit.callback.BanksPointCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.GetTransactionStatusCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.PaymentDetails;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.promo.Promo;
import com.midtrans.sdk.corekit.models.promo.PromoDetails;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.SavedToken;
import com.midtrans.sdk.corekit.models.snap.TransactionDetails;
import com.midtrans.sdk.corekit.models.snap.TransactionStatusResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BaseCreditCardPresenter;
import com.midtrans.sdk.uikit.models.CreditCardTransaction;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 7/12/17.
 */

public class CreditCardDetailsPresenter extends BaseCreditCardPresenter<CreditCardDetailsView> {
    private static final String TAG = CreditCardDetailsPresenter.class.getSimpleName();
    private Context context;
    private TokenDetailsResponse creditCardToken;
    private TransactionResponse transactionResponse;
    private CardTokenRequest cardTokenRequest;

    private int installmentTotalPositions;
    private int installmentCurrentPosition;
    private List<Promo> promos;

    public CreditCardDetailsPresenter(Context context, CreditCardDetailsView view) {
        super();
        this.view = view;
        this.creditCardTransaction = new CreditCardTransaction();
        this.context = context;
        initCreditCardTransaction(context);
        initPromoDetails();
        fetchBankBins();
    }

    private void initPromoDetails() {
        PromoDetails promoDetails = getMidtransSDK().getTransaction().getPromoDetails();

        if (promoDetails != null) {
            List<Promo> promos = promoDetails.getPromos();
            this.promos = new ArrayList<>();

            if (promos != null && !promos.isEmpty()) {
                for (Promo promo : promos) {
                    Promo newPromo;
                    try {
                        newPromo = (Promo) promo.clone();
                    } catch (CloneNotSupportedException e) {
                        newPromo = promo;
                        Logger.e("CloneNotSupportedException:" + e.getMessage());
                    }

                    this.promos.add(newPromo);
                }
            }
        }
    }

    private void fetchBankBins() {
        try {
            getMidtransSDK().getBankBins(new BankBinsCallback() {
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

        } catch (RuntimeException e) {
            Logger.d(TAG, "fetchBankBins" + e.getMessage());
        }
    }

    public boolean isSecurePayment() {
        TransactionRequest request = getMidtransSDK().getTransactionRequest();
        if (request != null) {
            String cardClickType = request.getCardClickType();
            if (TextUtils.isEmpty(cardClickType)) {
                if (getMidtransSDK().getCreditCard().isSecure()) {
                    return true;
                }
            } else if (request.isSecureCard()) {
                return true;
            }
        }

        return false;
    }

    public int getCcBadge() {
        return SdkUIFlowUtil.getCreditCardIconType();
    }


    public boolean isMandiriDebitCard(String cardBin) {
        return creditCardTransaction.isMandiriCardDebit(cardBin);
    }

    public boolean isInstallmentValid() {
        return creditCardTransaction.isInstallmentValid();
    }

    public Double getGrossAmount() {
        //use discounted gross amount if available
        if (creditCardTransaction.isSelectedPromoAvailable()) {
            return creditCardTransaction.getSelectedPromo().getDiscountedGrossAmount();
        }

        TransactionDetails transactionDetails = getMidtransSDK().getTransaction().getTransactionDetails();
        if (transactionDetails != null) {
            return transactionDetails.getAmount();
        }

        return 0D;
    }


    public void startGettingCardToken(String cardNumber, String month, String year, String cvv, boolean saveCard) {
        CardTokenRequest cardTokenRequest = new CardTokenRequest(
                cardNumber,
                cvv,
                month,
                year,
                getMidtransSDK().getClientKey());

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

        MidtransSDK midtransSDK = getMidtransSDK();
        CreditCard creditCard = midtransSDK.getCreditCard();

        if (creditCard != null) {
            applyAcquiringBank(cardTokenRequest, creditCard);
            applyTokenizationType(cardTokenRequest, creditCard);
        }

        TransactionDetails transactionDetails = midtransSDK.getTransaction().getTransactionDetails();
        if (transactionDetails != null) {
            cardTokenRequest.setCurrency(transactionDetails.getCurrency());
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

    public void startNormalPayment(boolean saveCard, boolean fromBankPoint) {
        if (creditCardToken != null) {
            CreditCardPaymentModel model = new CreditCardPaymentModel(creditCardToken.getTokenId(), saveCard);
            model.setFromBankPoint(fromBankPoint);
            applyPaymentProperties(model);
            startCreditCardPayment(model);
        } else {
            view.onPaymentError(new Throwable(context.getString(R.string.message_payment_failed)));
        }
    }


    private void saveCrediCard(TransactionResponse response) {
        if (!getMidtransSDK().isEnableBuiltInTokenStorage()) {
            if (this.cardTokenRequest != null && this.cardTokenRequest.isSaved()) {
                List<SavedToken> savedTokens = getMidtransSDK().getCreditCard().getSavedTokens();
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

        //set bank point and bank name (if Mandiri Point is selected)
        paymentModel.setPointRedeemed(creditCardTransaction.getBankPointRedeemed());
        if (creditCardTransaction.getBankName() != null && creditCardTransaction.getBankName().equalsIgnoreCase(BankType.MANDIRI)) {
            paymentModel.setBank(creditCardTransaction.getBankName());
        }

        //set promo
        paymentModel.setPromoSelected(creditCardTransaction.getSelectedPromo());
    }

    public boolean isShowPaymentStatus() {
        return getMidtransSDK().getUIKitCustomSetting().isShowPaymentStatus();
    }


    public TransactionResponse getTransactionResponse() {
        return this.transactionResponse;
    }

    public boolean isSaveCardOptionChecked() {
        return getMidtransSDK().getUIKitCustomSetting().isSaveCardChecked();
    }

    public void startOneClickPayment(String maskedCard) {
        CreditCardPaymentModel paymentModel = new CreditCardPaymentModel(maskedCard);
        applyPaymentProperties(paymentModel);
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
        request.setClientKey(getMidtransSDK().getClientKey());

        applyTokenizationProperties(request);
        getCardToken(request);
    }

    public void deleteSavedCard(SaveCardRequest request) {
        deleteSavedCard(request, view);
    }

    private void startSavingCreditCards(List<SaveCardRequest> saveCardRequest) {
        UserDetail userDetail = LocalDataHandler.readObject(UiKitConstants.KEY_USER_DETAILS, UserDetail.class);
        getMidtransSDK().saveCards(userDetail.getUserId(), new ArrayList<>(saveCardRequest),
                new SaveCardCallback() {
                    @Override
                    public void onSuccess(SaveCardResponse response) {
                        Logger.d(TAG, "savecards:success");
                    }

                    @Override
                    public void onFailure(String reason) {
                        Logger.d(TAG, "savecards:failed");
                    }

                    @Override
                    public void onError(Throwable error) {
                        Logger.d(TAG, "savecards:error");
                    }
                });
    }

    private void startCreditCardPayment(CreditCardPaymentModel model) {
        String authenticationToken = getMidtransSDK().readAuthenticationToken();
        getMidtransSDK().paymentUsingCard(authenticationToken, model, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                transactionResponse = response;
                saveCrediCard(response);
                view.onPaymentSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                transactionResponse = response;
                view.onPaymentFailure(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onPaymentError(error);
            }
        });
    }


    private void getCardToken(CardTokenRequest cardTokenRequest) {
        this.cardTokenRequest = cardTokenRequest;
        getMidtransSDK().getCardToken(cardTokenRequest, new CardTokenCallback() {
            @Override
            public void onSuccess(TokenDetailsResponse response) {
                creditCardToken = response;
                view.onGetCardTokenSuccess(response);
            }

            @Override
            public void onFailure(TokenDetailsResponse response, String reason) {
                view.onGetCardTokenFailure();
            }

            @Override
            public void onError(Throwable error) {
                view.onGetCardTokenFailure();
            }
        });
    }


    public void getBankPoint(final String bankType) {
        if (creditCardToken != null) {
            getMidtransSDK().getBanksPoint(creditCardToken.getTokenId(), new BanksPointCallback() {
                @Override
                public void onSuccess(BanksPointResponse response) {
                    creditCardTransaction.setBankPoint(response, bankType);
                    view.onGetBankPointSuccess(response);
                }

                @Override
                public void onFailure(String reason) {
                    view.onGetBankPointFailure();
                }

                @Override
                public void onError(Throwable error) {
                    view.onGetBankPointFailure();
                }
            });
        } else {
            view.onGetBankPointFailure();
        }
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

    public boolean isBniPointAvailable(String cardBin) {
        String bank = creditCardTransaction.getBankByBin(cardBin);
        List<String> bankPoints = getMidtransSDK().getBanksPointEnabled();

        return !TextUtils.isEmpty(bank)
                && bankPoints != null
                && bankPoints.contains(bank)
                && bank.equals(BankType.BNI);
    }

    public boolean isMandiriPointAvailable(String cardBin) {
        String bank = creditCardTransaction.getBankByBin(cardBin);
        List<String> bankPoints = getMidtransSDK().getBanksPointEnabled();

        return !TextUtils.isEmpty(bank)
                && bankPoints != null
                && bankPoints.contains(bank)
                && isSecurePayment()
                && bank.equals(BankType.MANDIRI);
    }

    public void startBankPointsPayment(float redeemedPoint, boolean isSaveCard) {
        creditCardTransaction.setBankPointRedeemed(redeemedPoint);
        startNormalPayment(isSaveCard, true);
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
        String snapToken = getMidtransSDK().readAuthenticationToken();
        getMidtransSDK().getTransactionStatus(snapToken, new GetTransactionStatusCallback() {
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
                view.onGetTransactionStatusFailure(transactionResponse);
            }

            @Override
            public void onError(Throwable error) {
                view.onGetTransactionStatusError(error);
            }
        });
    }

    public void startScanCard(Activity activity, int intentRequestScanCard) {
        if (isCardScannerAvailable()) {
            getMidtransSDK().getExternalScanner().startScan(activity, intentRequestScanCard);
        }
    }

    public boolean isCardScannerAvailable() {
        return getMidtransSDK().getExternalScanner() != null;
    }

    public boolean isCardBinBlocked(String cardNumber) {
        return creditCardTransaction.isCardBinBlocked(cardNumber);
    }

    public List<Promo> getCreditCardPromos(String cardNumber, boolean firstTime) {
        if (promos != null && !promos.isEmpty()) {
            return getValidCreditCardPromo(cardNumber, promos, firstTime);
        }
        return null;
    }

    private List<Promo> getValidCreditCardPromo(String cardNumber, List<Promo> promos, boolean firstTime) {
        List<Promo> cardPromos = new ArrayList<>();

        for (Promo promo : promos) {
            if (promo.getPaymentTypes() != null && promo.getPaymentTypes().contains(PaymentType.CREDIT_CARD)) {
                if (TextUtils.isEmpty(cardNumber)) {
                    cardPromos.add(promo);
                } else {
                    if (promo.getBins() != null && !promo.getBins().isEmpty()) {
                        for (String cardBin : promo.getBins()) {
                            if (cardNumber.startsWith(cardBin)) {
                                cardPromos.add(promo);
                            }
                        }
                    } else {
                        cardPromos.add(promo);
                    }
                }
            }
        }

        if (cardPromos.size() == 1 && firstTime) {
            promos.get(0).setSelected(true);
        } else if (cardPromos.isEmpty() && !firstTime) {
            resetPromos();
        }

        return cardPromos;
    }

    private void resetPromos() {
        for (Promo promo : promos) {
            if (promo.isSelected()) {
                promo.setSelected(false);
            }
        }
    }

    public ItemDetails createTransactionItem(Promo promo) {
        if (promo != null) {
            int quantity = 1;
            double price = promo.getCalculatedDiscountAmount() * -1;

            ItemDetails itemDetails = new ItemDetails(
                    UiKitConstants.PROMO_ID,
                    promo.getName(),
                    price, quantity);

            return itemDetails;
        }

        return null;
    }

    public void setSelectedPromo(Promo seletedPromo) {
        creditCardTransaction.setSelectedPromo(seletedPromo);
        PaymentDetails paymentDetails = getMidtransSDK().getPaymentDetails();
        if (paymentDetails != null) {
            paymentDetails.setPromoSelected(seletedPromo);
        }
    }

    public boolean isShowEmailForm() {
        UIKitCustomSetting customSetting = getMidtransSDK().getUIKitCustomSetting();
        if (customSetting != null) {
            return customSetting.isShowEmailInCcForm();
        }
        return false;
    }

    public boolean isEmailValid(String email) {
        return !(!TextUtils.isEmpty(email) && !SdkUIFlowUtil.isEmailValid(email));
    }

    public String getUserEmail() {
        String userEmail = "";
        UserDetail userDetail = LocalDataHandler.readObject(UiKitConstants.KEY_USER_DETAILS, UserDetail.class);
        if (userDetail != null) {
            userEmail = userDetail.getEmail();
        }
        return userEmail;
    }

    public String getUserPhone() {
        String userEmail = "";
        UserDetail userDetail = LocalDataHandler.readObject(UiKitConstants.KEY_USER_DETAILS, UserDetail.class);
        if (userDetail != null) {
            userEmail = userDetail.getPhoneNumber();
        }
        return userEmail;
    }

    public void saveUserDetail(@NonNull String email, @NonNull String phone) {
        UserDetail userDetail = LocalDataHandler.readObject(UiKitConstants.KEY_USER_DETAILS, UserDetail.class);
        if (userDetail == null) {
            userDetail = new UserDetail();
        }
        userDetail.setEmail(email);
        userDetail.setPhoneNumber(phone);
        LocalDataHandler.saveObject(UiKitConstants.KEY_USER_DETAILS, userDetail);
    }

    public boolean isInstallmentOptionRequired() {
        return (creditCardTransaction != null && creditCardTransaction.isInstallmentOptionRequired());
    }
}
