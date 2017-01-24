package com.midtrans.sdk.uikit.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.callback.BNIPointsCallback;
import com.midtrans.sdk.corekit.callback.BankBinsCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.BankDetail;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CreditCardFromScanner;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.BankPointsResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.SavedToken;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.AddCardDetailsFragment;
import com.midtrans.sdk.uikit.fragments.BanksPointFragment;
import com.midtrans.sdk.uikit.fragments.PaymentTransactionStatusFragment;
import com.midtrans.sdk.uikit.fragments.SavedCardFragment;
import com.midtrans.sdk.uikit.fragments.WebviewFragment;
import com.midtrans.sdk.uikit.models.CreditCardTransaction;
import com.midtrans.sdk.uikit.scancard.ExternalScanner;
import com.midtrans.sdk.uikit.scancard.ScannerModel;
import com.midtrans.sdk.uikit.utilities.ReadBankDetailTask;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.midtrans.sdk.uikit.utilities.ReadBankDetailTask.ReadBankDetailCallback;


public class CreditDebitCardFlowActivity extends BaseActivity implements ReadBankDetailCallback {
    public static final String PAYMENT_CREDIT_CARD = "cc";
    public static final int SCAN_REQUEST_CODE = 101;
    private static final int PAYMENT_WEB_INTENT = 100;
    private static final String KEY_SCAN_SUCCESS_EVENT = "Scan Card Success";
    private static final String KEY_SCAN_FAILED_EVENT = "Scan Card Failed";
    private static final String KEY_SCAN_CANCELLED_EVENT = "Scan Card Cancelled";
    private static final int PAY_USING_CARD = 51;
    private static final int MAX_ATTEMPT = 3;
    private static final String TAG = "CreditCardActivity";
    private int attempt = 0;
    private Toolbar toolbar;
    private MidtransSDK midtransSDK;
    private float cardWidth;
    private UserDetail userDetail;
    private TokenDetailsResponse tokenDetailsResponse;
    private CardTokenRequest cardTokenRequest;
    private ArrayList<SaveCardRequest> creditCards = new ArrayList<>();
    private RelativeLayout processingLayout;
    private ArrayList<BankDetail> bankDetails;
    private ReadBankDetailTask readBankDetailTask;
    //for setResult
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private TextView emptyCardsTextView;
    private TextView titleHeaderTextView;
    private DefaultTextView textTotalAmount;
    private boolean saveCard = false;
    private boolean removeExistCard = false;
    private String maskedCardNumber;
    private boolean isNewCard = true;
    private FancyButton buttonback;
    private ImageView imageSavedCardDelete;
    private boolean fromSavedCard;
    private CreditCardTransaction creditCardTransaction = new CreditCardTransaction();

    public TextView getTitleHeaderTextView() {
        return titleHeaderTextView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_debit_card_flow);
        processingLayout = (RelativeLayout) findViewById(R.id.processing_layout);
        midtransSDK = MidtransSDK.getInstance();
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        titleHeaderTextView = (TextView) findViewById(R.id.text_title);
        textTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);
        buttonback = (FancyButton) findViewById(R.id.btn_back);
        imageSavedCardDelete = (ImageView) findViewById(R.id.image_saved_card_delete);

        initializeTheme();
        setSupportActionBar(toolbar);
        calculateScreenWidth();
        if (midtransSDK != null) {
            creditCardTransaction.setProperties(midtransSDK.getCreditCard(), SdkUIFlowUtil.getBankBins(this));

            initBankBins();
            if (!midtransSDK.getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_none))) {
                getCreditCards();
            } else {
                showCardDetailFragment(null);
            }
            textTotalAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(midtransSDK.getTransactionRequest().getAmount())));
        }

        buttonback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imageSavedCardDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment currentFragment = getCurrentFagment(AddCardDetailsFragment.class);
                if (currentFragment != null && imageSavedCardDelete.getVisibility() == View.VISIBLE) {
                    SdkUIFlowUtil.showProgressDialog(CreditDebitCardFlowActivity.this, getString(R.string.processing_delete), false);
                    fromSavedCard = true;
                    SaveCardRequest savedCard = ((AddCardDetailsFragment) currentFragment).getSavedCard();
                    deleteSavedCard(savedCard);
                }
            }
        });
        readBankDetails();
    }

    private void deleteSavedCard(SaveCardRequest savedCard) {
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
            saveCreditCards(cardList, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Log.d("backpress", "transaksi:" + currentFragmentName + "  | is : " + fromSavedCard);
        SdkUIFlowUtil.hideKeyboard(this);
        if (!TextUtils.isEmpty(currentFragmentName) && currentFragmentName.equalsIgnoreCase(PaymentTransactionStatusFragment.class
                .getName())) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
        } else if (!TextUtils.isEmpty(currentFragmentName) && currentFragmentName.equalsIgnoreCase(AddCardDetailsFragment.class.getName())
                && fromSavedCard) {
            super.onBackPressed();
            fromSavedCard = false;
        } else {
            setResultCode(RESULT_CANCELED);
            setResultAndFinish();
        }
    }

    /**
     * @param cardTokenRequest
     */
    public void normalPayment(CardTokenRequest cardTokenRequest) {
        this.isNewCard = true;
        initInstallmentProperties(cardTokenRequest);
        initAcquiringBank(cardTokenRequest);
        this.cardTokenRequest = cardTokenRequest;
        getCardToken(cardTokenRequest);
    }

    private void initAcquiringBank(CardTokenRequest cardTokenRequest) {
        // Set acquiring bank and channel if available
        if (midtransSDK.getTransactionRequest().getCreditCard() != null) {
            String bank = midtransSDK.getTransactionRequest().getCreditCard().getBank();
            cardTokenRequest.setBank(bank);
            String channel = midtransSDK.getTransactionRequest().getCreditCard().getChannel();
            cardTokenRequest.setChannel(channel);
        }
    }

    public void getCardToken(CardTokenRequest cardTokenRequest) {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        midtransSDK.getCardToken(cardTokenRequest, new CardTokenCallback() {
            @Override
            public void onSuccess(TokenDetailsResponse response) {
                actionGetCardTokenSuccess(response);
            }

            @Override
            public void onFailure(TokenDetailsResponse response, String reason) {
                actionGetCardTokenFailure(response, reason);
            }

            @Override
            public void onError(Throwable error) {
                actionGetCardTokenError(error);
            }
        });
    }

    private void actionGetCardTokenError(Throwable error) {
        SdkUIFlowUtil.hideProgressDialog();
        SdkUIFlowUtil.showApiFailedMessage(CreditDebitCardFlowActivity.this, getString(R.string.message_getcard_token_failed));
    }

    private void actionGetCardTokenSuccess(TokenDetailsResponse response) {
        TokenDetailsResponse tokenDetailsResponse = response;
        if (tokenDetailsResponse != null) {
            this.cardTokenRequest.setBank(tokenDetailsResponse.getBank());
        }
        this.tokenDetailsResponse = tokenDetailsResponse;

        if (midtransSDK.getTransactionRequest().isSecureCard()) {
            SdkUIFlowUtil.hideProgressDialog();
            if (tokenDetailsResponse != null) {
                if (!TextUtils.isEmpty(tokenDetailsResponse.getRedirectUrl())) {
                    Intent intentPaymentWeb = new Intent(CreditDebitCardFlowActivity.this, PaymentWebActivity.class);
                    intentPaymentWeb.putExtra(Constants.WEBURL, tokenDetailsResponse.getRedirectUrl());
                    intentPaymentWeb.putExtra(Constants.TYPE, WebviewFragment.TYPE_CREDIT_CARD);
                    startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
                }
            }
        } else {
            SdkUIFlowUtil.showProgressDialog(CreditDebitCardFlowActivity.this, getString(R.string.processing_payment), false);
            preCreditCardpayment();
        }
    }

    private void preCreditCardpayment() {
        creditCardTransaction.setBankPointStatus(true);
        if (creditCardTransaction.isBankPointEnabled()) {
            MidtransSDK.getInstance().getBNIPoints(this.tokenDetailsResponse.getTokenId(), new BNIPointsCallback() {
                @Override
                public void onSuccess(BankPointsResponse response) {
                    SdkUIFlowUtil.hideProgressDialog();
                    creditCardTransaction.setBankPoint(response, BankType.BNI);
                    showBankPointsFragment(response.getPointBalance(), BankType.BNI);
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

                    creditCardTransaction.setBankPoint(null, null);
                    SdkUIFlowUtil.hideProgressDialog();
                }
            });

        } else {
            payUsingCard();
        }
    }

    private BankPointsResponse getMockPoint() {
        BankPointsResponse response = new BankPointsResponse("200", "anu", null, 100000l, "time");
        return response;
    }

    /**
     * start credit card payment
     */
    public void payUsingCard() {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);

        CreditCardPaymentModel paymentModel;
        if (midtransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase(getString(R.string.card_click_type_one_click))
                && !isNewCard && this.maskedCardNumber != null) {
            //using one click
            paymentModel = new CreditCardPaymentModel(this.maskedCardNumber);
        } else if (tokenDetailsResponse != null && !TextUtils.isEmpty(tokenDetailsResponse.getTokenId())) {
            //using normal payment & twoclick & oneclick first payment
            paymentModel = new CreditCardPaymentModel(tokenDetailsResponse.getTokenId(), saveCard);
        } else {
            SdkUIFlowUtil.showToast(this, getString(R.string.message_payment_not_completed));
            processingLayout.setVisibility(View.GONE);
            SdkUIFlowUtil.hideProgressDialog();

            return;
        }

        // set installment properties
        int installmentTermSelected = creditCardTransaction.getInstallmentTermSelected();
        String installmentBankSeleted = creditCardTransaction.getInstallmentBankSelected();
        if (installmentTermSelected > 0) {
            paymentModel.setInstallment(installmentBankSeleted + "_" + installmentTermSelected);
        }

        if (this.creditCardTransaction.isBankPointValid()) {
            paymentModel.setPointRedeemed(this.creditCardTransaction.getBankPointRedeemed());
        }

        midtransSDK.paymentUsingCard(midtransSDK.readAuthenticationToken(),
                paymentModel, new TransactionCallback() {
                    @Override
                    public void onSuccess(TransactionResponse response) {
                        actionPaymentSuccess(response);
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
                        SdkUIFlowUtil.hideProgressDialog();
                        if (attempt < MAX_ATTEMPT) {
                            attempt += 1;
                            SdkUIFlowUtil.showApiFailedMessage(CreditDebitCardFlowActivity.this, getString(R.string.message_payment_failed));
                        } else {
                            TransactionResponse transactionResponse = response;
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    processingLayout.setVisibility(View.GONE);
                                }
                            }, 200);
                            CreditDebitCardFlowActivity.this.transactionResponse = transactionResponse;
                            CreditDebitCardFlowActivity.this.errorMessage = getString(R.string.message_payment_failed);
                            initPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT, true);
                            titleHeaderTextView.setText(getString(R.string.title_payment_status));
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        SdkUIFlowUtil.hideProgressDialog();
                        showErrorMessage(getString(R.string.message_payment_failed));
                    }
                });
    }

    private void actionPaymentSuccess(TransactionResponse response) {
        TransactionResponse cardPaymentResponse = response;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                processingLayout.setVisibility(View.GONE);
            }
        }, 200);

        Logger.i(TAG, "paymentResponse:" + cardPaymentResponse.getStatusCode());

        if (cardPaymentResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200)) ||
                cardPaymentResponse.getStatusCode().equalsIgnoreCase(midtransSDK.getContext().getString(R.string.success_code_201))) {

            transactionResponse = cardPaymentResponse;
            titleHeaderTextView.setText(getString(R.string.title_payment_status));

            if (cardTokenRequest != null && cardTokenRequest.isSaved()) {
                if (!midtransSDK.isEnableBuiltInTokenStorage()) {
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
                    cardTokenRequest.setGrossAmount(0);

                    if (cardTokenRequest.isSaved()) {
                        if (!TextUtils.isEmpty(cardPaymentResponse.getSavedTokenId())) {
                            cardTokenRequest.setSavedTokenId(cardPaymentResponse.getSavedTokenId());
                        }
                    }
                    Logger.i(TAG, "Card:" + cardTokenRequest.getString());

                    SaveCardRequest saveCardRequest = new SaveCardRequest();
                    saveCardRequest.setSavedTokenId(cardTokenRequest.getSavedTokenId());
                    String firstPart = cardTokenRequest.getCardNumber().substring(0, 6);
                    String secondPart = cardTokenRequest.getCardNumber().substring(12);
                    saveCardRequest.setMaskedCard(firstPart + "-" + secondPart);
                    prepareSaveCard(saveCardRequest);
                    creditCards.add(saveCardRequest);
                }
            }
        }
        SdkUIFlowUtil.hideProgressDialog();
        initPaymentStatus(transactionResponse, null, Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT, true);
    }

    public MidtransSDK getMidtransSDK() {
        return midtransSDK;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    private void calculateScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        cardWidth = outMetrics.widthPixels;
        cardWidth = cardWidth - ((2 * getResources().getDimension(R.dimen.sixteen_dp)) / density);
    }

    public float getScreenWidth() {
        return cardWidth;
    }


    public void prepareSaveCard(SaveCardRequest creditCard) {
        ArrayList<SaveCardRequest> requests = new ArrayList<>();
        requests.addAll(getCreditCardList());
        String cardType = midtransSDK.getTransactionRequest().getCardClickType();
        SaveCardRequest savedCard = findCardByMaskedNumber(creditCard.getMaskedCard(), requests);
        if (savedCard != null) {
            requests.remove(savedCard);
        }
        requests.add(new SaveCardRequest(creditCard.getSavedTokenId(), creditCard.getMaskedCard(), cardType));
        saveCreditCards(requests, false);
    }

    private SaveCardRequest findCardByMaskedNumber(String maskedCard, ArrayList<SaveCardRequest> savedCards) {
        for (SaveCardRequest card : savedCards) {
            if (card.getMaskedCard().equals(maskedCard)) {
                return card;
            }
        }
        return null;
    }

    public void saveCreditCards(ArrayList<SaveCardRequest> requests, boolean isRemoveCard) {
        Log.d(TAG, "savecards>isremoveCard:" + isRemoveCard);
        this.removeExistCard = isRemoveCard;
        midtransSDK.saveCards(userDetail.getUserId(), requests, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {
                Log.d(TAG, "savecards>success");

                if (removeExistCard) {
                    SdkUIFlowUtil.hideProgressDialog();
                    Fragment currentFragment = getCurrentFagment(AddCardDetailsFragment.class);
                    if (currentFragment != null) {
                        Logger.d(TAG, "Delete card success");
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onFailure(String reason) {
                Log.d(TAG, "savecredicards>failure");

                if (removeExistCard) {
                    SdkUIFlowUtil.hideProgressDialog();
                    SdkUIFlowUtil.showToast(CreditDebitCardFlowActivity.this, reason);
                    removeExistCard = false;
                }
            }

            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "savecredicards>error");

                if (removeExistCard) {
                    SdkUIFlowUtil.hideProgressDialog();
                }
                Log.e(TAG, error.getMessage(), error);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i(TAG, "reqCode:" + requestCode + ",res:" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == PAYMENT_WEB_INTENT) {
                preCreditCardpayment();
            } else if (requestCode == SCAN_REQUEST_CODE) {
                if (data != null && data.hasExtra(ExternalScanner.EXTRA_SCAN_DATA)) {
                    // track scan event success
                    midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(MidtransSDK.getInstance().readAuthenticationToken(), KEY_SCAN_SUCCESS_EVENT, PAYMENT_CREDIT_CARD, null);

                    ScannerModel scanData = (ScannerModel) data.getSerializableExtra(ExternalScanner.EXTRA_SCAN_DATA);
                    Logger.i(String.format("Card Number: %s, Card Expire: %s/%d", scanData.getCardNumber(), scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                    updateCreditCardData(Utils.getFormattedCreditCardNumber(scanData.getCardNumber()), scanData.getCvv(), String.format("%s/%d", scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                } else {
                    // track scan event failed
                    midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(MidtransSDK.getInstance().readAuthenticationToken(), KEY_SCAN_FAILED_EVENT, PAYMENT_CREDIT_CARD, null);

                    Logger.d("No result");
                }
            } else {
                Logger.d("Not available");
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == PAYMENT_WEB_INTENT) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        processingLayout.setVisibility(View.GONE);
                    }
                }, 200);
                CreditDebitCardFlowActivity.this.errorMessage = getString(R.string.payment_canceled);
                SdkUIFlowUtil.hideProgressDialog();
                initPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT, true);
                titleHeaderTextView.setText(getString(R.string.title_payment_status));
            } else if (requestCode == SCAN_REQUEST_CODE) {
                // track scan cancelled
                midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(MidtransSDK.getInstance().readAuthenticationToken(), KEY_SCAN_CANCELLED_EVENT, PAYMENT_CREDIT_CARD, null);
            }
        }
    }

    private void updateCreditCardData(String cardNumber, String cvv, String expired) {
        // Update credit card data in AddCardDetailsFragment
        Fragment fragment = getCurrentFagment(AddCardDetailsFragment.class);
        if (fragment != null) {
            ((AddCardDetailsFragment) fragment).updateFromScanCardEvent(new CreditCardFromScanner(cardNumber, cvv, expired));
        }
    }

    public ArrayList<SaveCardRequest> getCreditCards() {
        if (creditCards == null || creditCards.isEmpty()) {
            fetchCreditCards();
        }
        return creditCards;
    }

    public ArrayList<SaveCardRequest> getCreditCardList() {
        return creditCards;
    }

    public void fetchCreditCards() {
        try {
            UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
            if (userDetail != null) {
                if (midtransSDK.isEnableBuiltInTokenStorage()) {
                    List<SavedToken> tokens = midtransSDK.getCreditCard().getSavedTokens();
                    bindSavedCards(tokens != null ? convertSavedToken(tokens) : new ArrayList<SaveCardRequest>());
                } else {
                    SdkUIFlowUtil.showProgressDialog(this, getString(R.string.fetching_cards), true);
                    midtransSDK.getCards(userDetail.getUserId(), new GetCardCallback() {
                        @Override
                        public void onSuccess(ArrayList<SaveCardRequest> response) {
                            ArrayList<SaveCardRequest> cardResponse = response;
                            SdkUIFlowUtil.hideProgressDialog();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    processingLayout.setVisibility(View.GONE);
                                }
                            }, 200);
                            Logger.i("cards api successful" + cardResponse);
                            filterMultipleSavedCard(cardResponse);
                            bindSavedCards(cardResponse);
                        }

                        @Override
                        public void onFailure(String reason) {
                            SdkUIFlowUtil.hideProgressDialog();
                            Logger.i(TAG, "card fetching failed :" + reason);
                            processingLayout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable error) {
                            SdkUIFlowUtil.hideProgressDialog();
                            showCardDetailFragment(null);
                        }
                    });
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    private void filterMultipleSavedCard(ArrayList<SaveCardRequest> savedCards) {
        Collections.reverse(savedCards);
        Set<String> maskedCardSet = new HashSet<>();
        for (Iterator<SaveCardRequest> it = savedCards.iterator(); it.hasNext(); ) {
            if (!maskedCardSet.add(it.next().getMaskedCard())) {
                it.remove();
            }
        }
    }

    public void oneClickPayment(String maskedCardNumber) {
        this.isNewCard = false;
        this.maskedCardNumber = maskedCardNumber;

        preCreditCardpayment();
    }

    public void twoClickPayment(CardTokenRequest cardDetail) {
        this.isNewCard = false;
        CardTokenRequest cardTokenRequest = new CardTokenRequest();
        cardTokenRequest.setSavedTokenId(cardDetail.getSavedTokenId());
        cardTokenRequest.setCardCVV(cardDetail.getCardCVV());
        cardTokenRequest.setTwoClick(true);
        cardTokenRequest.setSecure(midtransSDK.getTransactionRequest().isSecureCard());
        cardTokenRequest.setGrossAmount(midtransSDK.getTransactionRequest().getAmount());
        cardTokenRequest.setBank(midtransSDK.getTransactionRequest().getCreditCard().getBank());
        cardTokenRequest.setClientKey(midtransSDK.getClientKey());
        initInstallmentProperties(cardTokenRequest);
        initAcquiringBank(cardTokenRequest);
        this.cardTokenRequest = cardTokenRequest;

        getCardToken(cardTokenRequest);
    }

    private void getBNIPoints() {
    }

    private void initInstallmentProperties(CardTokenRequest cardTokenRequest) {
        int termSelected = creditCardTransaction.getInstallmentTermSelected();
        Log.d(TAG, "term:" + termSelected);
        if (termSelected > 0) {
            cardTokenRequest.setInstallment(true);
            cardTokenRequest.setInstalmentTerm(termSelected);
        }
    }

    private void actionGetCardTokenFailure(TokenDetailsResponse response, String reason) {
        SdkUIFlowUtil.hideProgressDialog();
        SdkUIFlowUtil.showApiFailedMessage(CreditDebitCardFlowActivity.this, getString(R.string.message_getcard_token_failed));
    }

    public ArrayList<BankDetail> getBankDetails() {
        if (bankDetails != null && !bankDetails.isEmpty()) {
            return bankDetails;
        } else {
            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (readBankDetailTask != null && readBankDetailTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            readBankDetailTask.cancel(true);
        }
    }

    private void readBankDetails() {
        if (bankDetails == null || bankDetails.isEmpty()) {
            readBankDetailTask = new ReadBankDetailTask(this, this);
            readBankDetailTask.execute();
        }
    }

    @Override
    public void onReadBankDetailsFinish(ArrayList<BankDetail> bankDetails, UserDetail userDetail) {
        if (userDetail != null) {
            this.userDetail = userDetail;
        }
        if (this.bankDetails != null) {
            this.bankDetails = bankDetails;
            Logger.i(TAG, "bank>size:" + bankDetails.size());
        }
    }

    public void setResultAndFinish() {
        setResultAndFinish(transactionResponse, errorMessage);
    }

    public int dimen(@DimenRes int resId) {
        return (int) getResources().getDimension(resId);
    }

    public int color(@ColorRes int resId) {
        return getResources().getColor(resId);
    }

    public int integer(@IntegerRes int resId) {
        return getResources().getInteger(resId);
    }

    private void showErrorMessage(String errorMessage) {
        AlertDialog alert = new AlertDialog.Builder(this)
                .setMessage(errorMessage)
                .setNegativeButton(R.string.btn_close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        setResultAndFinish();
                    }
                })
                .create();
        alert.show();
    }

    public void setSavedCardInfo(boolean saveCard, String cardType) {
        this.saveCard = saveCard;
    }

    private ArrayList<SaveCardRequest> convertSavedToken(List<SavedToken> savedTokens) {
        ArrayList<SaveCardRequest> saveCardRequests = new ArrayList<>();
        for (SavedToken savedToken : savedTokens) {
            saveCardRequests.add(new SaveCardRequest(savedToken.getToken(), savedToken.getMaskedCard(), savedToken.getTokenType()));
        }
        return saveCardRequests;
    }

    private void bindSavedCards(ArrayList<SaveCardRequest> cards) {
        ArrayList<SaveCardRequest> filteredCards = filterCardsByClickType(cards);

        if (!filteredCards.isEmpty()) {
            creditCards.clear();

            creditCards.addAll(filteredCards);
            //processingLayout.setVisibility(View.GONE);
            if (emptyCardsTextView != null) {
                if (!creditCards.isEmpty()) {
                    emptyCardsTextView.setVisibility(View.GONE);
                } else {
                    emptyCardsTextView.setVisibility(View.VISIBLE);
                }
            }

            SavedCardFragment savedCardFragment = SavedCardFragment.newInstance();
            //getSupportActionBar().setTitle(getString(R.string.saved_card));
            titleHeaderTextView.setText(getString(R.string.saved_card));
            replaceFragment(savedCardFragment, R.id.card_container, true, false);

        } else {
            showCardDetailFragment(null);
        }
    }

    private ArrayList<SaveCardRequest> filterCardsByClickType(ArrayList<SaveCardRequest> cards) {

        ArrayList<SaveCardRequest> filteredCards = new ArrayList<>();
        if (cards != null && !cards.isEmpty()) {
            if (midtransSDK.isEnableBuiltInTokenStorage()) {
                for (SaveCardRequest card : cards) {
                    if (midtransSDK.getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_one_click))
                            && card.getType().equals(getString(R.string.saved_card_one_click))) {
                        filteredCards.add(card);
                    } else if (midtransSDK.getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_two_click))
                            && card.getType().equals(getString(R.string.saved_card_two_click))) {
                        filteredCards.add(card);
                    }
                }
            } else {
                //if token storage on merchantserver then saved cards can be used just for two click
                String clickType = midtransSDK.getTransactionRequest().getCardClickType();
                if (!TextUtils.isEmpty(clickType) && clickType.equals(getString(R.string.card_click_type_two_click))) {
                    filteredCards.addAll(cards);
                }
            }
        }

        return filteredCards;
    }


    public void initBankBins() {
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

    public boolean isCardBinValid(String cardBin) {
        return creditCardTransaction.isInWhiteList(cardBin);
    }


    public ArrayList<Integer> getInstallmentTerms(String cardBin) {
        return creditCardTransaction.getInstallmentTerms(cardBin);
    }

    public String getBankByBin(String cardBin) {
        return creditCardTransaction.getBankByBin(cardBin);
    }

    public int getInstallmentTerm(int currentPosition) {
        return creditCardTransaction.getInstallmentTerm(currentPosition);
    }

    public void setInstallment(int termPosition) {
        this.creditCardTransaction.setInstallment(termPosition);
    }

    public boolean isWhiteListBinsAvailable() {
        return creditCardTransaction.isWhiteListBinsAvailable();
    }

    public void showAddCardDetailFragment(SaveCardRequest card) {
        showCardDetailFragment(card);
    }

    private void showCardDetailFragment(SaveCardRequest card) {
        if (card == null) {
            showDeleteCardIcon(false);
        } else {
            fromSavedCard = true;
        }
        AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance(card);
        replaceFragment(addCardDetailsFragment, R.id.card_container, true, false);
        titleHeaderTextView.setText(getString(R.string.card_details));
    }

    private void showBankPointsFragment(long balance, String bankType) {
        BanksPointFragment fragment = BanksPointFragment.newInstance(balance, bankType);
        replaceFragment(fragment, R.id.card_container, true, false);
    }

    public void showDeleteCardIcon(boolean show) {
        if (show) {
            imageSavedCardDelete.setVisibility(View.VISIBLE);
        } else {
            imageSavedCardDelete.setVisibility(View.INVISIBLE);
        }
    }

    public boolean isRemoveExistCard() {
        return removeExistCard;
    }

    public void setRemoveExistCard(boolean removeExistCard) {
        this.removeExistCard = removeExistCard;
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public boolean isInstallmentAvailable() {
        return creditCardTransaction.isInstallmentAvailable();
    }


    public boolean isValidInstallment() {
        return creditCardTransaction.isInstallmentValid();
    }

    public void setInstallmentAvailableStatus(boolean installmentStatus) {
        creditCardTransaction.setInstallmentAvailableStatus(installmentStatus);
    }

    public void setBankPointStatus(boolean bankPointEnableStatus) {
        creditCardTransaction.setBankPointStatus(bankPointEnableStatus);
    }

    public void payWithBankPoint(long pointRedeemed) {
        creditCardTransaction.setBankPointRedeemed(pointRedeemed);
        payUsingCard();
    }
}
