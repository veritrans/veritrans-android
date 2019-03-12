package com.midtrans.sdk.uikit.view.method.creditcard.details;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.BankType;
import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.mandatory.TransactionDetails;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.CreditCard;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.SavedToken;
import com.midtrans.sdk.corekit.core.api.merchant.model.savecard.SaveCardResponse;
import com.midtrans.sdk.corekit.core.api.midtrans.model.cardtoken.CardTokenRequest;
import com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails.TokenDetailsResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.bins.BankBinsResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CreditCardResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.payment.PaymentStatusResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.Promo;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.PromoDetails;
import com.midtrans.sdk.corekit.core.api.snap.model.point.PointResponse;
import com.midtrans.sdk.corekit.core.payment.CreditCardCharge;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.MidtransKitConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BaseCreditCardPresenter;
import com.midtrans.sdk.uikit.base.model.CreditCardPaymentModel;
import com.midtrans.sdk.uikit.base.model.PaymentResponse;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.Helper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.view.method.creditcard.model.CreditCardTransaction;

import java.util.ArrayList;
import java.util.List;

public class CreditCardDetailsPresenter extends BaseCreditCardPresenter<CreditCardDetailsContract> {

    private static final String TAG = CreditCardDetailsPresenter.class.getSimpleName();
    private Context context;
    private TokenDetailsResponse creditCardToken;
    private PaymentResponse transactionResponse;
    private CardTokenRequest cardTokenRequest;

    private int installmentTotalPositions;
    private int installmentCurrentPosition;
    private List<Promo> promos;

    public CreditCardDetailsPresenter(
            Context context,
            CreditCardDetailsContract view,
            PaymentInfoResponse paymentInfoResponse
    ) {
        super(paymentInfoResponse);
        this.view = view;
        this.context = context;
        creditCardTransaction = new CreditCardTransaction();
        initCreditCardTransaction(context);
        initPromoDetails();
        fetchBankBins();
    }

    private void initPromoDetails() {
        PromoDetails promoDetails = paymentInfoResponse.getPromoDetails();

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
                        Logger.error("CloneNotSupportedException:" + e.getMessage());
                    }

                    this.promos.add(newPromo);
                }
            }
        }
    }

    public void setSelectedPromo(Promo seletedPromo) {
        creditCardTransaction.setSelectedPromo(seletedPromo);
        if (paymentInfoResponse != null) {
            paymentInfoResponse.setPromoSelected(seletedPromo);
        }
    }

    private void fetchBankBins() {
        try {
            CreditCardCharge.getBankBins(new MidtransCallback<List<BankBinsResponse>>() {
                @Override
                public void onSuccess(List<BankBinsResponse> data) {
                    creditCardTransaction.setBankBins(data);
                }

                @Override
                public void onFailed(Throwable throwable) {

                }
            });
        } catch (RuntimeException e) {
            Logger.debug(TAG, "fetchBankBins" + e.getMessage());
        }
    }

    public boolean isSecurePayment() {
        CheckoutTransaction request = getMidtransSdk().getCheckoutTransaction();
        if (request != null) {
            return request.getCreditCard().isSecure();
        }
        return false;
    }

    public int getCcBadge() {
        return PaymentListHelper.getCreditCardIconType(paymentInfoResponse);
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

        TransactionDetails transactionDetails = getMidtransSdk().getCheckoutTransaction().getTransactionDetails();
        if (transactionDetails != null) {
            return transactionDetails.getGrossAmount();
        }
        return 0D;
    }

    public void startGettingCardToken(String cardNumber, String month, String year, String cvv, boolean saveCard) {
        CardTokenRequest cardTokenRequest = new CardTokenRequest(
                cardNumber,
                cvv,
                month,
                year,
                getMidtransSdk().getMerchantClientId());

        cardTokenRequest.setSaved(saveCard);
        cardTokenRequest.setSecure(isSecurePayment());
        cardTokenRequest.setGrossAmount(getGrossAmount());
        //apply installment, acquiring bank and bank point
        applyTokenizationProperties(cardTokenRequest);
        getCardToken(cardTokenRequest);
    }

    private void applyTokenizationProperties(CardTokenRequest cardTokenRequest) {
        applyInstallmentProperties(cardTokenRequest);
        cardTokenRequest.setPoint(view.isBankPointEnabled());

        CreditCard creditCard = getMidtransSdk().getCheckoutTransaction().getCreditCard();

        if (creditCard != null) {
            applyAcquiringBank(cardTokenRequest, creditCard);
            applyTokenizationType(cardTokenRequest, creditCard);
        }

        TransactionDetails transactionDetails = getMidtransSdk().getCheckoutTransaction().getTransactionDetails();
        if (transactionDetails != null) {
            cardTokenRequest.setCurrency(transactionDetails.getCurrency());
        }
    }

    private void applyAcquiringBank(CardTokenRequest cardTokenRequest, CreditCard creditCard) {
        // Set acquiring bank and channel if available
        String bank = creditCard.getAcquiringBank();
        String channel = creditCard.getAcquiringChannel();
        cardTokenRequest.setBank(bank);
        cardTokenRequest.setChannel(channel);
    }

    private void applyTokenizationType(CardTokenRequest cardTokenRequest, CreditCard creditCard) {
        cardTokenRequest.setType(creditCard.getType());
    }

    private void applyInstallmentProperties(CardTokenRequest cardTokenRequest) {
        int termSelected = creditCardTransaction.getInstallmentTermSelected();
        Logger.debug(TAG, "applyInstallmentProperties()>term:" + termSelected);

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


    private void saveCrediCard(PaymentResponse response) {
        if (!getMidtransKit().isBuiltinStorageEnabled()) {
            if (this.cardTokenRequest != null && this.cardTokenRequest.isSaved()) {
                List<SavedToken> savedTokens = paymentInfoResponse.getCreditCard().getSavedTokens();
                List<SaveCardRequest> savedCards = Helper.convertSavedTokens(savedTokens);

                String cardType = context.getString(R.string.card_click_type_two_click);
                String maskedCard = createMaskedCard(cardTokenRequest.getCardNumber());
                SaveCardRequest oldSavedCard = findCardByMaskedNumber(maskedCard, savedCards);

                if (oldSavedCard != null) {
                    savedCards.remove(oldSavedCard);
                }

                SaveCardRequest saveCardRequest = new SaveCardRequest(response.getSavedTokenId(), maskedCard, cardType);
                savedCards.add(saveCardRequest);
                Helper.filterMultipleSavedCard(savedCards);
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
        return getMidtransKit().getMidtransKitConfig().isShowPaymentStatus();
    }

    public boolean isSaveCardOptionChecked() {
        return getMidtransKit().getMidtransKitConfig().isSaveCardChecked();
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
        request.setClientKey(getMidtransSdk().getMerchantClientId());

        applyTokenizationProperties(request);
        getCardToken(request);
    }

    public void deleteSavedCard(SaveCardRequest request) {
        deleteSavedCard(paymentInfoResponse.getToken(), request, view);
    }

    private void startSavingCreditCards(List<SaveCardRequest> saveCardRequest) {
        CreditCardCharge.saveCards(getMidtransSdk().getCheckoutTransaction().getUserId(), saveCardRequest, new MidtransCallback<SaveCardResponse>() {
            @Override
            public void onSuccess(SaveCardResponse data) {

            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });
    }

    private void startCreditCardPayment(CreditCardPaymentModel model) {
        CreditCardCharge.paymentUsingCard(
                paymentInfoResponse.getToken(),
                Helper.convertToCreditCardPaymentParam(model),
                Helper.initializePaymentDetails(paymentInfoResponse),
                Helper.getPromoDetails(model),
                new MidtransCallback<CreditCardResponse>() {
                    @Override
                    public void onSuccess(CreditCardResponse data) {
                        view.onPaymentSuccess(data);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        view.onPaymentError(throwable);
                    }
                }
        );
    }


    private void getCardToken(CardTokenRequest cardTokenRequest) {
        this.cardTokenRequest = cardTokenRequest;
        CreditCardCharge.getCardToken(cardTokenRequest, new MidtransCallback<TokenDetailsResponse>() {
            @Override
            public void onSuccess(TokenDetailsResponse data) {
                creditCardToken = data;
                view.onGetCardTokenSuccess(data);
            }

            @Override
            public void onFailed(Throwable throwable) {
                view.onGetCardTokenFailure();
            }
        });
    }


    public void getBankPoint(final String bankType) {
        if (creditCardToken != null) {
            CreditCardCharge.getBanksPoint(paymentInfoResponse.getToken(), creditCardToken.getTokenId(), new MidtransCallback<PointResponse>() {
                @Override
                public void onSuccess(PointResponse data) {
                    creditCardTransaction.setBankPoint(data, bankType);
                    view.onGetBankPointSuccess(data);
                }

                @Override
                public void onFailed(Throwable throwable) {
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
        List<String> bankPoints = paymentInfoResponse.getMerchantData().getPointBanks();

        return !TextUtils.isEmpty(bank)
                && bankPoints != null
                && bankPoints.contains(bank)
                && bank.equals(BankType.BNI);
    }

    public boolean isMandiriPointAvailable(String cardBin) {
        String bank = creditCardTransaction.getBankByBin(cardBin);
        List<String> bankPoints = paymentInfoResponse.getMerchantData().getPointBanks();

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

    public boolean isRbaAuthentication(CreditCardResponse response) {
        if (response != null) {
            String transactionStatus = response.getTransactionStatus();
            if (!TextUtils.isEmpty(transactionStatus) && transactionStatus.equals(Constants.STATUS_PENDING)) {
                return true;
            }
        }
        return false;
    }

    public void getPaymentStatus() {
        getMidtransSdk().getPaymentStatus(paymentInfoResponse.getToken(), new MidtransCallback<PaymentStatusResponse>() {
            @Override
            public void onSuccess(PaymentStatusResponse data) {
                PaymentResponse transactionResponse = convertTransactionStatus(data);
                CreditCardDetailsPresenter.this.transactionResponse = transactionResponse;
                view.onGetTransactionStatusSuccess(transactionResponse);
            }

            @Override
            public void onFailed(Throwable throwable) {
                view.onGetTransactionStatusError(throwable);
            }
        });
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

    public Item createTransactionItem(Promo promo) {
        if (promo != null) {
            int quantity = 1;
            double price = promo.getCalculatedDiscountAmount() * -1;
            return new Item(
                    Constants.PROMO_ID,
                    price,
                    quantity,
                    promo.getName()
            );
        }

        return null;
    }

    public boolean isShowEmailForm() {
        MidtransKitConfig customSetting = getMidtransKit().getMidtransKitConfig();
        if (customSetting != null) {
            return customSetting.isShowEmailInCcForm();
        }
        return false;
    }

    public boolean isEmailValid(String email) {
        return !(!TextUtils.isEmpty(email) && !Helper.isEmailValid(email));
    }

    public String getUserEmail() {
        String userEmail = "";
        if (paymentInfoResponse != null) {
            userEmail = paymentInfoResponse.getCustomerDetails().getEmail();
        }
        return userEmail;
    }

    public String getUserPhone() {
        String phoneNumber = "";
        if (paymentInfoResponse != null) {
            phoneNumber = paymentInfoResponse.getCustomerDetails().getPhone();
        }
        return phoneNumber;
    }

    public boolean isInstallmentOptionRequired() {
        return (creditCardTransaction != null && creditCardTransaction.isInstallmentOptionRequired());
    }
}
