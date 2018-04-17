package com.midtrans.sdk.uikit.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.midtrans.sdk.corekit.callback.BankBinsCallback;
import com.midtrans.sdk.corekit.callback.BanksPointCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CreditCardFromScanner;
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
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.BanksPointFragment;
import com.midtrans.sdk.uikit.fragments.CardDetailsFragment;
import com.midtrans.sdk.uikit.fragments.PaymentTransactionStatusFragment;
import com.midtrans.sdk.uikit.fragments.SavedCardListFragment;
import com.midtrans.sdk.uikit.fragments.WebviewFragment;
import com.midtrans.sdk.uikit.models.CreditCardTransaction;
import com.midtrans.sdk.uikit.scancard.ExternalScanner;
import com.midtrans.sdk.uikit.scancard.ScannerModel;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Deprecated, please see {@link com.midtrans.sdk.uikit.views.creditcard.details.CreditCardDetailsActivity}
 * Created by rakawm on 3/7/17.
 */

@Deprecated
public class CreditCardFlowActivity extends BaseActivity {
    public static final int SCAN_REQUEST_CODE = 101;
    private static final String TAG = CreditCardFlowActivity.class.getSimpleName();
    private static final int PAYMENT_WEB_INTENT = 100;
    private static final int MAX_ATTEMPT = 2;
    private TransactionResponse transactionResponse;
    private String errorMessage;

    private Toolbar toolbar;
    private TextView titleHeaderTextView;
    private TextView textTotalAmount;
    private String discountToken;
    private TokenDetailsResponse tokenDetailsResponse;
    private boolean saveCard = false;
    private boolean isNewCard = true;
    private String maskedCardNumber;
    private String savedCardClickType;
    private int attempt = 0;
    private CardTokenRequest cardTokenRequest;
    private ArrayList<SaveCardRequest> creditCards = new ArrayList<>();

    private CreditCardTransaction creditCardTransaction = new CreditCardTransaction();

    public void setCreditCards(ArrayList<SaveCardRequest> creditCards) {
        this.creditCards = creditCards;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_debit_card_flow);
        bindViews();
        initToolbar();
        initializeTheme();
        initCreditCard();

    }

    public void deleteSavedCard(SaveCardRequest savedCard) {
        if (MidtransSDK.getInstance().isEnableBuiltInTokenStorage()) {
            deleteCardFromTokenStorage(savedCard);
        } else {
            if (savedCard != null) {
                ArrayList<SaveCardRequest> cardList = new ArrayList<>();
                if (creditCards != null && !creditCards.isEmpty()) {
                    cardList.addAll(creditCards);
                    for (int i = 0; i < cardList.size(); i++) {
                        if (cardList.get(i).getSavedTokenId().equalsIgnoreCase(savedCard.getSavedTokenId())) {
                            cardList.remove(cardList.get(i));
                        }
                    }
                }
                deleteCardFromMerchantServer(cardList);
            }
        }
    }

    private void deleteCardFromMerchantServer(final ArrayList<SaveCardRequest> cardList) {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_delete), false);
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        midtransSDK.saveCards(userDetail.getUserId(), cardList, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {
                SdkUIFlowUtil.hideProgressDialog();
                if (cardList.isEmpty()) {
                    clearBackStackFragments();
                    titleHeaderTextView.setText(R.string.card_details);
                    getSupportFragmentManager().beginTransaction().replace(R.id.card_container, CardDetailsFragment.newInstance()).commit();
                } else {
                    onBackPressed();
                    updateSavedCards();
                }
            }

            @Override
            public void onFailure(String reason) {
                SdkUIFlowUtil.hideProgressDialog();
            }

            @Override
            public void onError(Throwable error) {
                SdkUIFlowUtil.hideProgressDialog();
            }
        });
    }


    private void initCreditCard() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        creditCardTransaction.setProperties(midtransSDK.getCreditCard(), SdkUIFlowUtil.getBankBins(this));

        initBankBins();

        if (isClickPayment()) {
            getCreditCards();

        } else {
            showAddCardDetailFragment();

        }
        setTextTotalAmount(MidtransSDK.getInstance().getTransactionRequest().getAmount());
    }

    private void showAddCardDetailFragment() {
        titleHeaderTextView.setText(R.string.card_details);
        getSupportFragmentManager().beginTransaction().replace(R.id.card_container, CardDetailsFragment.newInstance(), CardDetailsFragment.class.getSimpleName()).commit();
    }

    private void getCreditCards() {
        UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        if (MidtransSDK.getInstance().isEnableBuiltInTokenStorage()) {
            List<SavedToken> tokens = MidtransSDK.getInstance().getCreditCard().getSavedTokens();
            if (tokens != null && !tokens.isEmpty()) {
                List<SaveCardRequest> savedCard = SdkUIFlowUtil.convertSavedToken(tokens);
                if (!savedCard.isEmpty()) {
                    showSavedCardFragment(savedCard);
                } else {
                    showAddCardDetailFragment();
                }
            } else {
                showAddCardDetailFragment();
            }
        } else {
            SdkUIFlowUtil.showProgressDialog(this, getString(R.string.fetching_cards), false);
            MidtransSDK.getInstance().getCards(userDetail.getUserId(), new GetCardCallback() {
                @Override
                public void onSuccess(ArrayList<SaveCardRequest> response) {
                    SdkUIFlowUtil.hideProgressDialog();
                    List<SaveCardRequest> filteredSavedCards = SdkUIFlowUtil.filterMultipleSavedCard(response);
                    // Update credit cards
                    creditCards.clear();
                    creditCards.addAll(filteredSavedCards);
                    showSavedCardFragment(filteredSavedCards);
                }

                @Override
                public void onFailure(String reason) {
                    SdkUIFlowUtil.hideProgressDialog();
                    showAddCardDetailFragment();
                }

                @Override
                public void onError(Throwable error) {
                    SdkUIFlowUtil.hideProgressDialog();
                    showAddCardDetailFragment();
                }
            });
        }
    }

    public void setTextTotalAmount(double amount) {
        textTotalAmount.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(amount)));
    }

    private void initBankBins() {
        MidtransSDK.getInstance().getBankBins(new BankBinsCallback() {
            @Override
            public void onSuccess(ArrayList<BankBinsResponse> response) {
                creditCardTransaction.setBankBins(response);
            }

            @Override
            public void onFailure(String reason) {
                Log.i(TAG, "bankbins>failure:" + reason);
            }

            @Override
            public void onError(Throwable error) {
                Log.i(TAG, "bankbins>error:" + error.getMessage());

            }
        });
    }

    private void bindViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        titleHeaderTextView = (TextView) findViewById(R.id.text_title);
        textTotalAmount = (TextView) findViewById(R.id.text_amount);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        prepareToolbar();
    }

    private void prepareToolbar() {
        try {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_back);
            MidtransSDK midtransSDK = MidtransSDK.getInstance();
            if (midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                if (drawable != null) {
                    drawable.setColorFilter(
                            midtransSDK.getColorTheme().getPrimaryDarkColor(),
                            PorterDuff.Mode.SRC_ATOP);
                }
            }
            toolbar.setNavigationIcon(drawable);
        } catch (Exception e) {
            Log.e(TAG, "rendering theme:" + e.getMessage());
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void showSavedCardFragment(List<SaveCardRequest> saveCardRequests) {
        titleHeaderTextView.setText(getString(R.string.saved_card));
        SavedCardListFragment savedCardFragment = SavedCardListFragment.newInstance(saveCardRequests);
        getSupportFragmentManager().beginTransaction().replace(R.id.card_container, savedCardFragment, SavedCardListFragment.class.getSimpleName()).commit();
    }

    public String getBankByBin(String cardBin) {
        return creditCardTransaction.getBankByBin(cardBin);
    }

    public TextView getTitleHeaderTextView() {
        return titleHeaderTextView;
    }

    public boolean isMandiriDebitCard(String cleanCardNumber) {
        return creditCardTransaction.isMandiriCardDebit(cleanCardNumber);
    }

    public boolean isValidInstallment() {
        return creditCardTransaction.isInstallmentValid();
    }

    public boolean isCardBinValid(String cardBin) {
        return creditCardTransaction.isInWhiteList(cardBin);
    }

    public boolean isWhiteListBinsAvailable() {
        return creditCardTransaction.isWhiteListBinsAvailable();
    }

    public boolean isInstallmentAvailable() {
        return creditCardTransaction.isInstallmentAvailable();
    }

    public ArrayList<Integer> getInstallmentTerms(String cleanCardNumber) {
        return creditCardTransaction.getInstallmentTerms(cleanCardNumber);
    }

    public int getInstallmentTerm(int installmentCurrentPosition) {
        return creditCardTransaction.getInstallmentTerm(installmentCurrentPosition);
    }


    public void setInstallment(int position) {
        this.creditCardTransaction.setInstallment(position);
    }

    public void setDiscountToken(String discountToken) {
        this.discountToken = discountToken;
    }

    public void setInstallmentAvailableStatus(boolean status) {
        creditCardTransaction.setInstallmentAvailableStatus(status);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PAYMENT_WEB_INTENT) {
                preCreditCardPayment();
            } else if (requestCode == SCAN_REQUEST_CODE) {
                if (data != null && data.hasExtra(ExternalScanner.EXTRA_SCAN_DATA)) {
                    ScannerModel scanData = (ScannerModel) data.getSerializableExtra(ExternalScanner.EXTRA_SCAN_DATA);
                    Logger.i(String.format("Card Number: %s, Card Expire: %s/%d", scanData.getCardNumber(), scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                    updateScanCardData(scanData);
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == PAYMENT_WEB_INTENT) {
                preCreditCardPayment();
            }
        }
    }

    public void setSavedCardInfo(boolean saveCard, String cardType) {
        this.saveCard = saveCard;
    }

    private void payUsingCard() {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);

        CreditCardPaymentModel paymentModel;
        if (isOneClickPayment()
                && !isNewCard && this.maskedCardNumber != null) {
            //using one click
            paymentModel = new CreditCardPaymentModel(this.maskedCardNumber);
        } else if (tokenDetailsResponse != null && !TextUtils.isEmpty(tokenDetailsResponse.getTokenId())) {
            //using normal payment & twoclick & oneclick first payment
            paymentModel = new CreditCardPaymentModel(tokenDetailsResponse.getTokenId(), saveCard);
        } else {
            SdkUIFlowUtil.showToast(this, getString(R.string.message_payment_not_completed));
            SdkUIFlowUtil.hideProgressDialog();
            return;
        }

        // set installment properties
        int installmentTermSelected = creditCardTransaction.getInstallmentTermSelected();
        String installmentBankSeleted = creditCardTransaction.getInstallmentBankSelected();
        if (installmentTermSelected > 0) {
            paymentModel.setInstallment(installmentBankSeleted + "_" + installmentTermSelected);
        }

        if (creditCardTransaction.getBankPointRedeemed() != 0.0f) {
            paymentModel.setPointRedeemed(creditCardTransaction.getBankPointRedeemed());
        }

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (discountToken != null && !TextUtils.isEmpty(discountToken)) {

        } else {
            midtransSDK.paymentUsingCard(midtransSDK.readAuthenticationToken(), paymentModel, new TransactionCallback() {
                @Override
                public void onSuccess(TransactionResponse response) {
                    actionPaymentSuccess(response);
                }

                @Override
                public void onFailure(TransactionResponse response, String reason) {
                    actionPaymentFailure(response, reason);
                }

                @Override
                public void onError(Throwable error) {
                    actionPaymentError(error);
                }
            });
        }
    }

    private boolean isOneClickPayment() {
        if (this.savedCardClickType != null) {
            CreditCard creditCard = MidtransSDK.getInstance().getCreditCard();
            if (creditCard.isSaveCard() && creditCard.isSecure()
                    && savedCardClickType.equals(getString(R.string.card_click_type_one_click))) {
                return true;
            }
        }

        return false;
    }

    private void updateScanCardData(ScannerModel scanData) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof CardDetailsFragment) {
                CardDetailsFragment cardDetailsFragment = (CardDetailsFragment) fragment;
                cardDetailsFragment.updateFromScanCardEvent(new CreditCardFromScanner(Utils.getFormattedCreditCardNumber(scanData.getCardNumber()), scanData.getCvv(), String.format("%s/%d", scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000)));
            }
        }
    }

    private void deleteCardFromTokenStorage(final SaveCardRequest savedCard) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        midtransSDK.deleteCard(midtransSDK.readAuthenticationToken(), savedCard.getMaskedCard(), new DeleteCardCallback() {
            @Override
            public void onSuccess(Void object) {
                SdkUIFlowUtil.hideProgressDialog();
                removeFromCreditCardInstance(savedCard.getMaskedCard());
                removeCardFromInstance(savedCard.getMaskedCard());
                if (checkIfCreditCardTokensAvailable()) {
                    onBackPressed();
                    updateSavedCards();
                } else {
                    clearBackStackFragments();
                    titleHeaderTextView.setText(R.string.card_details);
                    getSupportFragmentManager().beginTransaction().replace(R.id.card_container, CardDetailsFragment.newInstance()).commit();
                }
            }

            @Override
            public void onFailure(Void object) {
                SdkUIFlowUtil.hideProgressDialog();
                SdkUIFlowUtil.showToast(CreditCardFlowActivity.this, getString(R.string.error_delete_message));
            }

            @Override
            public void onError(Throwable throwable) {
                SdkUIFlowUtil.hideProgressDialog();
                SdkUIFlowUtil.showToast(CreditCardFlowActivity.this, getString(R.string.error_delete_message));
            }
        });
    }

    private void updateSavedCards() {
        if (MidtransSDK.getInstance().isEnableBuiltInTokenStorage()) {
            List<SavedToken> savedTokens = MidtransSDK.getInstance().getCreditCard().getSavedTokens();
            List<SaveCardRequest> saveCardRequests = SdkUIFlowUtil.convertSavedToken(savedTokens);
            updateSavedCard(saveCardRequests);
        } else {
            UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
            SdkUIFlowUtil.showProgressDialog(this, getString(R.string.fetching_cards), false);
            MidtransSDK.getInstance().getCards(userDetail.getUserId(), new GetCardCallback() {
                @Override
                public void onSuccess(ArrayList<SaveCardRequest> response) {
                    SdkUIFlowUtil.hideProgressDialog();
                    ArrayList<SaveCardRequest> filteredSavedCards = (ArrayList<SaveCardRequest>) SdkUIFlowUtil.filterMultipleSavedCard(response);
                    titleHeaderTextView.setText(getString(R.string.saved_card));

                    creditCards.clear();
                    creditCards.addAll(filteredSavedCards);

                    updateSavedCard(filteredSavedCards);
                }

                @Override
                public void onFailure(String reason) {
                    SdkUIFlowUtil.hideProgressDialog();
                    showAddCardDetailFragment();
                }

                @Override
                public void onError(Throwable error) {
                    SdkUIFlowUtil.hideProgressDialog();
                    showAddCardDetailFragment();
                }
            });
        }
    }

    private void updateSavedCard(List<SaveCardRequest> saveCardRequests) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            if (fragment instanceof SavedCardListFragment) {
                SavedCardListFragment savedCardListFragment = (SavedCardListFragment) fragment;
                savedCardListFragment.updateSavedCardsData(saveCardRequests, true);
            }
        }
    }

    public void normalPayment(CardTokenRequest cardTokenRequest) {
        this.isNewCard = true;

        CardTokenRequest installmentTokenRequest = initInstallmentProperties(cardTokenRequest);
        CardTokenRequest acquiringBankTokenRequest = initAcquiringBank(installmentTokenRequest);

        this.cardTokenRequest = acquiringBankTokenRequest;

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK.getCreditCard().getType() != null
                && !TextUtils.isEmpty(midtransSDK.getCreditCard().getType())) {
            acquiringBankTokenRequest.setType(midtransSDK.getCreditCard().getType());
        }
        getCardToken(cardTokenRequest);
    }

    private void getCardToken(final CardTokenRequest cardTokenRequest) {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        if (creditCardTransaction.isBankPointEnabled()) {
            cardTokenRequest.setPoint(true);
        } else {
            cardTokenRequest.setPoint(false);
        }

        MidtransSDK.getInstance().getCardToken(cardTokenRequest, new CardTokenCallback() {
            @Override
            public void onSuccess(TokenDetailsResponse response) {
                tokenDetailsResponse = response;
                cardTokenRequest.setBank(tokenDetailsResponse.getBank());
                if (isSecurePayment()) {
                    if (!TextUtils.isEmpty(tokenDetailsResponse.getRedirectUrl())) {
                        Intent intentPaymentWeb = new Intent(CreditCardFlowActivity.this, PaymentWebActivity.class);
                        intentPaymentWeb.putExtra(Constants.WEBURL, tokenDetailsResponse.getRedirectUrl());
                        intentPaymentWeb.putExtra(Constants.TYPE, WebviewFragment.TYPE_CREDIT_CARD);
                        startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
                        if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                                && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        }
                    } else {
                        SdkUIFlowUtil.showProgressDialog(CreditCardFlowActivity.this, getString(R.string.processing_payment), false);
                        preCreditCardPayment();
                    }
                } else {
                    SdkUIFlowUtil.showProgressDialog(CreditCardFlowActivity.this, getString(R.string.processing_payment), false);
                    preCreditCardPayment();
                }
            }

            @Override
            public void onFailure(TokenDetailsResponse response, String reason) {
                SdkUIFlowUtil.hideProgressDialog();
                SdkUIFlowUtil.showApiFailedMessage(CreditCardFlowActivity.this, getString(R.string.message_getcard_token_failed));
            }

            @Override
            public void onError(Throwable error) {
                SdkUIFlowUtil.hideProgressDialog();
                SdkUIFlowUtil.showApiFailedMessage(CreditCardFlowActivity.this, getString(R.string.message_getcard_token_failed));
            }
        });
    }

    private CardTokenRequest initInstallmentProperties(CardTokenRequest cardTokenRequest) {
        int termSelected = creditCardTransaction.getInstallmentTermSelected();
        Logger.d(TAG, "term:" + termSelected);
        if (termSelected > 0) {
            cardTokenRequest.setInstallment(true);
            cardTokenRequest.setInstalmentTerm(termSelected);
        }
        return cardTokenRequest;
    }

    private CardTokenRequest initAcquiringBank(CardTokenRequest cardTokenRequest) {
        // Set acquiring bank and channel if available
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK.getTransactionRequest().getCreditCard() != null) {
            String bank = midtransSDK.getTransactionRequest().getCreditCard().getBank();
            cardTokenRequest.setBank(bank);
            String channel = midtransSDK.getTransactionRequest().getCreditCard().getChannel();
            cardTokenRequest.setChannel(channel);
        }
        return cardTokenRequest;
    }

    private void actionPaymentSuccess(TransactionResponse response) {
        this.transactionResponse = response;
        Logger.i(TAG, "paymentResponse:" + response.getStatusCode());

        if (response.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200)) ||
                response.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_201))) {

            titleHeaderTextView.setText(getString(R.string.title_payment_status));

            if (cardTokenRequest != null && cardTokenRequest.isSaved()) {
                if (!MidtransSDK.getInstance().isEnableBuiltInTokenStorage()) {
                    if (!creditCards.isEmpty()) {
                        int position = -1;
                        for (int i = 0; i < creditCards.size(); i++) {
                            SaveCardRequest card = creditCards.get(i);
                            if (card.getSavedTokenId().equalsIgnoreCase(cardTokenRequest.getSavedTokenId())) {
                                position = i;
                                break;
                            }
                        }

                        if (position >= 0) {
                            creditCards.remove(position);
                        }
                    }
                    cardTokenRequest.setCardCVV("0");
                    cardTokenRequest.setClientKey("");
                    cardTokenRequest.setGrossAmount(0L);

                    if (cardTokenRequest.isSaved()) {
                        if (!TextUtils.isEmpty(response.getSavedTokenId())) {
                            cardTokenRequest.setSavedTokenId(response.getSavedTokenId());
                        }
                    }
                    Logger.i(TAG, "Card:" + cardTokenRequest.getString());

                    SaveCardRequest saveCardRequest = new SaveCardRequest();
                    saveCardRequest.setSavedTokenId(cardTokenRequest.getSavedTokenId());
                    String firstPart = cardTokenRequest.getCardNumber().replace(" ", "").substring(0, 6);
                    String secondPart = cardTokenRequest.getCardNumber().replace(" ", "").substring(12);
                    saveCardRequest.setMaskedCard(firstPart + "-" + secondPart);
                    prepareSaveCard(saveCardRequest);
                }
            }
        }
        SdkUIFlowUtil.hideProgressDialog();
        initPaymentStatus(response, null, Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT, true);
    }

    private void actionPaymentFailure(TransactionResponse response, String message) {
        this.transactionResponse = response;
        SdkUIFlowUtil.hideProgressDialog();
        if (attempt < MAX_ATTEMPT) {
            attempt += 1;
            SdkUIFlowUtil.showApiFailedMessage(this, getString(R.string.message_payment_failed));
        } else {
            errorMessage = getString(R.string.message_payment_failed);
            initPaymentStatus(response, errorMessage, Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT, true);
            titleHeaderTextView.setText(getString(R.string.title_payment_status));
        }

        if (response != null && response.getStatusCode().equals(getString(R.string.failed_code_400))) {
            Log.d("3dserror", "400:" + response.getValidationMessages().get(0));
        }
    }

    private void actionPaymentError(Throwable throwable) {
        String errorMessage = MessageUtil.createPaymentErrorMessage(this, throwable.getMessage(), getString(R.string.message_payment_failed));
        SdkUIFlowUtil.hideProgressDialog();
        showErrorMessage(errorMessage);
    }

    private void showErrorMessage(String errorMessage) {
        AlertDialog alert = new AlertDialog.Builder(this)
                .setMessage(errorMessage)
                .setNegativeButton(R.string.btn_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // TODO: set result and finish
                        //setResultAndFinish();
                    }
                })
                .create();
        alert.show();
    }

    public void prepareSaveCard(SaveCardRequest creditCard) {
        String cardType = getString(R.string.card_click_type_two_click);
        SaveCardRequest savedCard = findCardByMaskedNumber(creditCard.getMaskedCard(), creditCards);
        if (savedCard != null) {
            creditCards.remove(savedCard);
        }
        creditCards.add(new SaveCardRequest(creditCard.getSavedTokenId(), creditCard.getMaskedCard(), cardType));
        creditCards = (ArrayList<SaveCardRequest>) SdkUIFlowUtil.filterMultipleSavedCard(creditCards);
        saveCreditCards(creditCards);
    }

    private SaveCardRequest findCardByMaskedNumber(String maskedCard, ArrayList<SaveCardRequest> savedCards) {
        for (SaveCardRequest card : savedCards) {
            if (card.getMaskedCard().equals(maskedCard)) {
                return card;
            }
        }
        return null;
    }

    public void twoClickPayment(CardTokenRequest request) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        this.isNewCard = false;
        CardTokenRequest cardTokenRequest = new CardTokenRequest();
        cardTokenRequest.setSavedTokenId(request.getSavedTokenId());
        cardTokenRequest.setCardCVV(request.getCardCVV());
        cardTokenRequest.setGrossAmount(request.getGrossAmount());
        cardTokenRequest.setTwoClick(true);
        cardTokenRequest.setSecure(isSecurePayment());
        cardTokenRequest.setBank(midtransSDK.getTransactionRequest().getCreditCard().getBank());
        cardTokenRequest.setClientKey(midtransSDK.getClientKey());
        if (midtransSDK.getCreditCard().getType() != null
                && !TextUtils.isEmpty(midtransSDK.getCreditCard().getType())) {
            cardTokenRequest.setType(midtransSDK.getCreditCard().getType());
        }
        CardTokenRequest installmentCardTokenRequest = initInstallmentProperties(cardTokenRequest);
        CardTokenRequest acquiringBankCardTokenReques = initAcquiringBank(installmentCardTokenRequest);
        this.cardTokenRequest = acquiringBankCardTokenReques;
        getCardToken(cardTokenRequest);
    }

    public void oneClickPayment(SaveCardRequest request) {
        this.isNewCard = false;
        this.maskedCardNumber = request.getMaskedCard();
        this.savedCardClickType = request.getType();
        payUsingCard();
    }

    @Override
    public void onBackPressed() {
        SdkUIFlowUtil.hideKeyboard(this);
        if (!TextUtils.isEmpty(currentFragmentName)
                && currentFragmentName.equalsIgnoreCase(PaymentTransactionStatusFragment.class.getName())) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
        } else {
            super.onBackPressed();
        }
    }

    public void setResultAndFinish() {
        setResultAndFinish(transactionResponse, errorMessage);
    }

    private void removeFromCreditCardInstance(String maskedCard) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        CreditCard creditCard = midtransSDK.getCreditCard();
        List<SavedToken> savedTokens = creditCard.getSavedTokens();
        SavedToken savedToken = searchSavedToken(savedTokens, maskedCard);
        if (savedToken != null) {
            savedTokens.remove(savedTokens.indexOf(savedToken));
            creditCard.setSavedTokens(savedTokens);
            midtransSDK.setCreditCard(creditCard);
        }
    }

    private SavedToken searchSavedToken(List<SavedToken> savedTokens, String maskedCard) {
        for (SavedToken savedToken : savedTokens) {
            if (savedToken.getMaskedCard().equals(maskedCard)) {
                return savedToken;
            }
        }
        return null;
    }

    public boolean checkIfCreditCardTokensAvailable() {
        List<SavedToken> savedTokens = MidtransSDK.getInstance().getCreditCard().getSavedTokens();
        return savedTokens != null && !savedTokens.isEmpty();
    }

    private void clearBackStackFragments() {
        getSupportFragmentManager().popBackStackImmediate();
    }

    public void saveCreditCards(ArrayList<SaveCardRequest> requests) {
        UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        MidtransSDK.getInstance().saveCards(userDetail.getUserId(), requests, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {
                SdkUIFlowUtil.hideProgressDialog();
            }

            @Override
            public void onFailure(String reason) {
                SdkUIFlowUtil.hideProgressDialog();
            }

            @Override
            public void onError(Throwable error) {
                SdkUIFlowUtil.hideProgressDialog();
            }
        });
    }

    private void removeCardFromInstance(String card) {
        SaveCardRequest saveCardRequest = searchCardIndex(card);
        if (saveCardRequest != null) {
            creditCards.remove(creditCards.indexOf(saveCardRequest));
        }
    }

    private SaveCardRequest searchCardIndex(String card) {
        for (SaveCardRequest saveCardRequest : creditCards) {
            if (saveCardRequest.getMaskedCard().equals(card)) {
                return saveCardRequest;
            }
        }
        return null;
    }

    public void setBankPointStatus(boolean banksPointActivated) {
        creditCardTransaction.setBankPointStatus(banksPointActivated);
    }

    public void payWithBankPoint(float pointRedeemed) {
        creditCardTransaction.setBankPointRedeemed(pointRedeemed);
        payUsingCard();
    }

    private void preCreditCardPayment() {
        if (creditCardTransaction.isBankPointEnabled()) {

            MidtransSDK.getInstance().getBanksPoint(this.tokenDetailsResponse.getTokenId(), new BanksPointCallback() {
                @Override
                public void onSuccess(BanksPointResponse response) {
                    SdkUIFlowUtil.hideProgressDialog();
                    creditCardTransaction.setBankPoint(response, BankType.BNI);
                    float point = Float.parseFloat(response.getPointBalanceAmount());
                    showBankPointsFragment(point, BankType.BNI);
                }

                @Override
                public void onFailure(String reason) {
                    Log.d(TAG, "bnipoint:onFailure");
                    creditCardTransaction.setBankPoint(null, null);
                    SdkUIFlowUtil.hideProgressDialog();
                }

                @Override
                public void onError(Throwable error) {
                    Log.d(TAG, "bnipoint:onError");
                    SdkUIFlowUtil.hideProgressDialog();

                    creditCardTransaction.setBankPoint(null, null);
                }
            });

        } else {
            payUsingCard();
        }
    }

    private void showBankPointsFragment(float balance, String bankType) {
        BanksPointFragment fragment = BanksPointFragment.newInstance(balance, bankType);
        getSupportFragmentManager().beginTransaction().replace(R.id.card_container, fragment).addToBackStack("").commit();
    }

    public boolean isClickPayment() {
        TransactionRequest request = MidtransSDK.getInstance().getTransactionRequest();
        if (request != null) {
            String cardClickType = request.getCardClickType();
            if (TextUtils.isEmpty(cardClickType)) {
                if (MidtransSDK.getInstance().getCreditCard().isSaveCard()) {
                    return true;
                }
            } else {
                if (cardClickType.equals(getString(R.string.card_click_type_one_click)) ||
                        cardClickType.equals(getString(R.string.card_click_type_two_click))) {
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
}
