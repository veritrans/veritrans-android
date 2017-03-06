package com.midtrans.sdk.ui.views.creditcard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Payment;
import com.midtrans.sdk.ui.models.CreditCardDetails;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.thirdparty.ExternalScanner;
import com.midtrans.sdk.ui.thirdparty.ScannerModel;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.utils.Utils;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;

/**
 * Created by ziahaqi on 2/22/17.
 */

public class CreditCardActivity extends BaseActivity {

    private CreditCardPresenter presenter;

    private RelativeLayout layoutProcessing;
    private TextView tvHeaderTitle;
    private DefaultTextView tvTotalAmount;
    private FancyButton buttonBack;
    private ImageView ivDeleteSavedCard;

//    private boolean saveCard = false;
//    private boolean removeExistCard = false;
//    private String maskedCardNumber;
//    private boolean isNewCard = true;
//    private FancyButton buttonback;
//    private ImageView imageSavedCardDelete;
//    private boolean fromSavedCard;
//    private String discountToken;
//    private CreditCardTransaction creditCardTransaction = new CreditCardTransaction();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        initProperties();
        bindView();
        initThemeColor();
        setupView();
        initDefaultState();
    }

    private void initDefaultState() {
        setViewTotalAmount(MidtransUi.getInstance().getCheckoutTokenRequest().transactionDetails.grossAmount);

        if (presenter.isNormalMode()) {
            showCreditCardDetailFragment(null);
        } else {
            presenter.getSavedCards();
        }
    }


    private void setupView() {

    }

    private void bindView() {
        layoutProcessing = (RelativeLayout) findViewById(R.id.processing_layout);
        layoutTotalAmount = (RelativeLayout) findViewById(R.id.layout_total_amount);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        tvHeaderTitle = (TextView) findViewById(R.id.text_title);
        tvTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);
        buttonBack = (FancyButton) findViewById(R.id.btn_back);
        ivDeleteSavedCard = (ImageView) findViewById(R.id.image_saved_card_delete);
    }

    private void initProperties() {
        presenter = new CreditCardPresenter(this);
    }

    private void showCreditCardDetailFragment(CreditCardDetails model) {
        if (model != null && model.hasSavedToken()) {
            showDeleteCardIcon(true);
        } else {
            showDeleteCardIcon(false);
        }
//        else {
//            setFromSavedCard(true);
//        }

        CreditCardDetailsFragment addCreditCardDetailsFragment = CreditCardDetailsFragment.newInstance(model);
        replaceFragment(addCreditCardDetailsFragment, R.id.layout_container, true, false);

        presenter.setCardDetailView(addCreditCardDetailsFragment);
        setHeaderTitle(getString(R.string.card_details));
    }

    public void showDeleteCardIcon(boolean show) {
        if (show) {
            ivDeleteSavedCard.setVisibility(View.VISIBLE);
        } else {
            ivDeleteSavedCard.setVisibility(View.INVISIBLE);
        }
    }


    private void showSavedCreditCardFragment() {
        SavedCreditCardsFragment savedCreditCardsFragment = SavedCreditCardsFragment.newInstance();
        replaceFragment(savedCreditCardsFragment, R.id.layout_container, true, false);

        presenter.setSavedCreditCardsView(savedCreditCardsFragment);
        setHeaderTitle(getString(R.string.saved_card));
    }

    public void setHeaderTitle(String headerTitle) {
        this.tvHeaderTitle.setText(headerTitle);
    }


    ////////


//    public static final String PAYMENT_CREDIT_CARD = "cc";
//    public static final int SCAN_REQUEST_CODE = 101;
//    private static final int PAYMENT_WEB_INTENT = 100;
//    private static final String KEY_SCAN_SUCCESS_EVENT = "Scan Card Success";
//    private static final String KEY_SCAN_FAILED_EVENT = "Scan Card Failed";
//    private static final String KEY_SCAN_CANCELLED_EVENT = "Scan Card Cancelled";
//    private static final int PAY_USING_CARD = 51;
//    private static final int MAX_ATTEMPT = 3;
//    private static final String TAG = "CreditCardActivity";
//    private int attempt = 0;
//    private float cardWidth;
//    private UserDetail userDetail;
//    private TokenDetailsResponse tokenDetailsResponse;
//    private CardTokenRequest cardTokenRequest;
//    private ArrayList<SaveCardRequest> creditCards = new ArrayList<>();
//    private RelativeLayout processingLayout;
//    private ArrayList<BankDetail> bankDetails;
//    private ReadBankDetailTask readBankDetailTask;
//    //for setResult
//    private TransactionResponse transactionResponse = null;
//    private String errorMessage = null;

    //    private boolean saveCard = false;
//    private boolean removeExistCard = false;
//    private String maskedCardNumber;
//    private boolean isNewCard = true;
//
//    private boolean fromSavedCard;
//    private String discountToken;
//    private CreditCardTransaction  creditCardTransaction = new CreditCardTransaction();
//
//
//    public TextView getTvHeaderTitle() {
//        return tvHeaderTitle;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_credit_debit_card_flow);
//        processingLayout = (RelativeLayout) findViewById(R.id.processing_layout);
//        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
//        tvHeaderTitle = (TextView) findViewById(R.id.text_title);
//        tvTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);
//        buttonBack = (FancyButton) findViewById(R.id.btn_back);
//        ivDeleteSavedCard = (ImageView) findViewById(R.id.image_saved_card_delete);
//
//        initializeTheme();
//        setSupportActionBar(toolbar);
//        calculateScreenWidth();
//        if (midtransSDK != null) {
//            creditCardTransaction.setProperties(midtransSDK.getCreditCard(), UiUtils.getBankBins(this));
//
//            initBankBins();
//            if (!midtransSDK.getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_none))) {
//                getCreditCards();
//            } else {
//                showCardDetailFragment(null, null);
//            }
//            setViewTotalAmount(midtransSDK.getTransactionRequest().getAmount());
//        }
//
//        buttonBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//        ivDeleteSavedCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Fragment currentFragment = getCurrentFagment(AddCardDetailsFragment.class);
//                if (currentFragment != null && ivDeleteSavedCard.getVisibility() == View.VISIBLE) {
//                    UiUtils.showProgressDialog(CreditDebitCardFlowActivity.this, getString(R.string.processing_delete), false);
//                    setFromSavedCard(true);
//                    SaveCardRequest savedCard = ((AddCardDetailsFragment) currentFragment).getSavedCard();
//                    deleteSavedCard(savedCard);
//                }
//            }
//        });
//        readBankDetails();
//    }
//
//    private void deleteSavedCard(SaveCardRequest savedCard) {
//        if (savedCard != null) {
//            ArrayList<SaveCardRequest> cardList = new ArrayList<>();
//            if (creditCards != null && !creditCards.isEmpty()) {
//                cardList.addAll(creditCards);
//                for (int i = 0; i < cardList.size(); i++) {
//                    if (cardList.get(i).getSavedTokenId().equalsIgnoreCase(savedCard.getSavedTokenId())) {
//                        cardList.remove(cardList.get(i));
//                    }
//                }
//            }
//            saveCreditCards(cardList, true);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        Log.d("backpress", "transaksi:" + currentFragmentName + "  | is : " + fromSavedCard);
//        UiUtils.hideKeyboard(this);
//        if (!TextUtils.isEmpty(currentFragmentName) && currentFragmentName.equalsIgnoreCase(PaymentTransactionStatusFragment.class
//                .getName())) {
//            setResultCode(RESULT_OK);
//            completePayment();
//        } else if (!TextUtils.isEmpty(currentFragmentName) && currentFragmentName.equalsIgnoreCase(AddCardDetailsFragment.class.getName())
//                && fromSavedCard) {
//            super.onBackPressed();
//            setFromSavedCard(false);
//        } else {
//            setResultCode(RESULT_CANCELED);
//            completePayment();
//        }
//    }
//
//    /**
//     * @param cardTokenRequest
//     */
//    public void normalPayment(CardTokenRequest cardTokenRequest) {
//        this.isNewCard = true;
//        initInstallmentProperties(cardTokenRequest);
//        initAcquiringBank(cardTokenRequest);
//        this.cardTokenRequest = cardTokenRequest;
//        if (midtransSDK.getCreditCard().getType() != null
//                && !TextUtils.isEmpty(midtransSDK.getCreditCard().getType())) {
//            this.cardTokenRequest.setType(midtransSDK.getCreditCard().getType());
//        }
//        getCardToken(cardTokenRequest);
//    }
//
//    private void initAcquiringBank(CardTokenRequest cardTokenRequest) {
//        // Set acquiring bank and channel if available
//        if (midtransSDK.getTransactionRequest().getCreditCard() != null) {
//            String bank = midtransSDK.getTransactionRequest().getCreditCard().getBank();
//            cardTokenRequest.setBank(bank);
//            String channel = midtransSDK.getTransactionRequest().getCreditCard().getChannel();
//            cardTokenRequest.setChannel(channel);
//        }
//    }
//
//    public void getCardToken(CardTokenRequest cardTokenRequest) {
//        UiUtils.showProgressDialog(this, getString(R.string.processing_payment), false);
//        midtransSDK.getCardToken(cardTokenRequest, new CardTokenCallback() {
//            @Override
//            public void onSuccess(TokenDetailsResponse response) {
//                actionGetCardTokenSuccess(response);
//            }
//
//            @Override
//            public void onFailure(TokenDetailsResponse response, String reason) {
//                actionGetCardTokenFailure(response, reason);
//            }
//
//            @Override
//            public void onError(Throwable error) {
//                actionGetCardTokenError(error);
//            }
//        });
//    }
//
//    private void actionGetCardTokenError(Throwable error) {
//        UiUtils.hideProgressDialog();
//        UiUtils.showApiFailedMessage(CreditDebitCardFlowActivity.this, getString(R.string.message_getcard_token_failed));
//    }
//
//    private void actionGetCardTokenSuccess(TokenDetailsResponse response) {
//        TokenDetailsResponse tokenDetailsResponse = response;
//        if (tokenDetailsResponse != null) {
//            CreditDebitCardFlowActivity.this.cardTokenRequest.setBank(tokenDetailsResponse.getBank());
//        }
//        CreditDebitCardFlowActivity.this.tokenDetailsResponse = tokenDetailsResponse;
//
//        if (midtransSDK.getTransactionRequest().isSecureCard()) {
//            UiUtils.hideProgressDialog();
//            if (tokenDetailsResponse != null) {
//                if (!TextUtils.isEmpty(tokenDetailsResponse.getRedirectUrl())) {
//                    Intent intentPaymentWeb = new Intent(CreditDebitCardFlowActivity.this, PaymentWebActivity.class);
//                    intentPaymentWeb.putExtra(Constants.WEBURL, tokenDetailsResponse.getRedirectUrl());
//                    intentPaymentWeb.putExtra(Constants.TYPE, MidtransWebviewFragment.TYPE_CREDIT_CARD);
//                    startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
//                }
//            }
//        } else {
//            UiUtils.showProgressDialog(CreditDebitCardFlowActivity.this, getString(R.string.processing_payment), false);
//            payUsingCard();
//        }
//    }
//
//    /**
//     * start credit card payment
//     */
//    public void payUsingCard() {
//        UiUtils.showProgressDialog(this, getString(R.string.processing_payment), false);
//
//        CreditCardPaymentModel paymentModel;
//        if (midtransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase(getString(R.string.card_click_type_one_click))
//                && !isNewCard && this.maskedCardNumber != null) {
//            //using one click
//            paymentModel = new CreditCardPaymentModel(this.maskedCardNumber);
//        } else if (tokenDetailsResponse != null && !TextUtils.isEmpty(tokenDetailsResponse.getTokenId())) {
//            //using normal payment & twoclick & oneclick first payment
//            paymentModel = new CreditCardPaymentModel(tokenDetailsResponse.getTokenId(), saveCard);
//        } else {
//            UiUtils.showToast(this, getString(R.string.message_payment_not_completed));
//            processingLayout.setVisibility(View.GONE);
//            UiUtils.hideProgressDialog();
//
//            return;
//        }
//
//        // set installment properties
//        int installmentTermSelected = creditCardTransaction.getInstallmentTermSelected();
//        String installmentBankSeleted = creditCardTransaction.getInstallmentBankSelected();
//        if (installmentTermSelected > 0) {
//            paymentModel.setInstallment(installmentBankSeleted + "_" + installmentTermSelected);
//        }
//
//        if (discountToken != null && !TextUtils.isEmpty(discountToken)) {
//            midtransSDK.paymentUsingCard(midtransSDK.readAuthenticationToken(), discountToken,
//                    paymentModel, new TransactionCallback() {
//                        @Override
//                        public void onSuccess(TransactionResponse response) {
//                            actionPaymentSuccess(response);
//                        }
//
//                        @Override
//                        public void onFailure(TransactionResponse response, String reason) {
//
//                            UiUtils.hideProgressDialog();
//                            if (attempt < MAX_ATTEMPT) {
//                                attempt += 1;
//                                UiUtils.showApiFailedMessage(CreditDebitCardFlowActivity.this, getString(R.string.message_payment_failed));
//                            } else {
//                                TransactionResponse transactionResponse = response;
//                                Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        processingLayout.setVisibility(View.GONE);
//                                    }
//                                }, 200);
//                                CreditDebitCardFlowActivity.this.transactionResponse = transactionResponse;
//                                CreditDebitCardFlowActivity.this.errorMessage = getString(R.string.message_payment_failed);
//                                initPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT, true);
//                                tvHeaderTitle.setText(getString(R.string.title_payment_status));
//                            }
//                            if(response != null && response.getStatusCode().equals(getString(R.string.failed_code_400))){
//                                Log.d("3dserror", "1>400:" + response.getValidationMessages().get(0));
//
//                                if(response.getValidationMessages() != null && response.getValidationMessages().get(0) != null){
//                                    if(response.getValidationMessages().get(0).contains("3d")){
//                                        //track page bca va overview
//                                        midtransSDK.trackEvent(AnalyticsEventName.CREDIT_CARD_3DS_ERROR);
//                                    }
//                                }
//                            }
//
//                            //track page status failed
//                            MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
//                        }
//
//                        @Override
//                        public void onError(Throwable error) {
//                            //track page status failed
//                            MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
//
//
//
//                            UiUtils.hideProgressDialog();
//                            showErrorMessage(getString(R.string.message_payment_failed));
//                        }
//                    });
//        } else {
//            midtransSDK.paymentUsingCard(midtransSDK.readAuthenticationToken(),
//                    paymentModel, new TransactionCallback() {
//                        @Override
//                        public void onSuccess(TransactionResponse response) {
//                            actionPaymentSuccess(response);
//                        }
//
//                        @Override
//                        public void onFailure(TransactionResponse response, String reason) {
//                            UiUtils.hideProgressDialog();
//                            if (attempt < MAX_ATTEMPT) {
//                                attempt += 1;
//                                UiUtils.showApiFailedMessage(CreditDebitCardFlowActivity.this, getString(R.string.message_payment_failed));
//                            } else {
//                                TransactionResponse transactionResponse = response;
//                                Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        processingLayout.setVisibility(View.GONE);
//                                    }
//                                }, 200);
//                                CreditDebitCardFlowActivity.this.transactionResponse = transactionResponse;
//                                CreditDebitCardFlowActivity.this.errorMessage = getString(R.string.message_payment_failed);
//                                initPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT, true);
//                                tvHeaderTitle.setText(getString(R.string.title_payment_status));
//                            }
//
//                            if(response != null && response.getStatusCode().equals(getString(R.string.failed_code_400))){
//                                Log.d("3dserror", "400:" + response.getValidationMessages().get(0));
//                                if(response.getValidationMessages() != null && response.getValidationMessages().get(0) != null){
//                                    if(response.getValidationMessages().get(0).contains("3d")){
//                                        //track page bca va overview
//                                        midtransSDK.trackEvent(AnalyticsEventName.CREDIT_CARD_3DS_ERROR);
//                                    }
//                                }
//                            }
//
//                            //track page status failed
//                            MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
//                        }
//
//                        @Override
//                        public void onError(Throwable error) {
//                            //track page status failed
//                            MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);
//
//                            UiUtils.hideProgressDialog();
//                            showErrorMessage(getString(R.string.message_payment_failed));
//
//                        }
//                    });
//        }
//    }
//
//    private void actionPaymentSuccess(TransactionResponse response) {
//        //track page status success
//        MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_SUCCESS);
//
//        TransactionResponse cardPaymentResponse = response;
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                processingLayout.setVisibility(View.GONE);
//            }
//        }, 200);
//
//        Logger.i(TAG, "paymentResponse:" + cardPaymentResponse.getStatusCode());
//
//        if (cardPaymentResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200)) ||
//                cardPaymentResponse.getStatusCode().equalsIgnoreCase(midtransSDK.getContext().getString(R.string.success_code_201))) {
//
//            transactionResponse = cardPaymentResponse;
//            tvHeaderTitle.setText(getString(R.string.title_payment_status));
//
//            if (cardTokenRequest != null && cardTokenRequest.isSaved()) {
//                if (!midtransSDK.isEnableBuiltInTokenStorage()) {
//                    if (!creditCards.isEmpty()) {
//                        int position = -1;
//                        for (int i = 0; i < creditCards.size(); i++) {
//                            SaveCardRequest card = creditCards.get(i);
//                            if (card.getSavedTokenId().equalsIgnoreCase(cardTokenRequest.getSavedTokenId())) {
//                                position = i;
//                                break;
//                            }
//                        }
//
//                        if (position >= 0) {
//                            creditCards.remove(position);
//                        }
//                    }
//                    cardTokenRequest.setCardCVV("0");
//                    cardTokenRequest.setClientKey("");
//                    cardTokenRequest.setGrossAmount(0);
//
//                    if (cardTokenRequest.isSaved()) {
//                        if (!TextUtils.isEmpty(cardPaymentResponse.getSavedTokenId())) {
//                            cardTokenRequest.setSavedTokenId(cardPaymentResponse.getSavedTokenId());
//                        }
//                    }
//                    Logger.i(TAG, "Card:" + cardTokenRequest.getString());
//
//                    SaveCardRequest saveCardRequest = new SaveCardRequest();
//                    saveCardRequest.setSavedTokenId(cardTokenRequest.getSavedTokenId());
//                    String firstPart = cardTokenRequest.getCardNumber().substring(0, 6);
//                    String secondPart = cardTokenRequest.getCardNumber().substring(12);
//                    saveCardRequest.setMaskedCard(firstPart + "-" + secondPart);
//                    prepareSaveCard(saveCardRequest);
//                    creditCards.add(saveCardRequest);
//                }
//            }
//        }
//        UiUtils.hideProgressDialog();
//        initPaymentStatus(transactionResponse, null, Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT, true);
//    }
//
//    public MidtransSDK getMidtransSDK() {
//        return midtransSDK;
//    }
//
//    public UserDetail getUserDetail() {
//        return userDetail;
//    }
//
//    public Toolbar getToolbar() {
//        return toolbar;
//    }
//
//    private void calculateScreenWidth() {
//        Display display = getWindowManager().getDefaultDisplay();
//        DisplayMetrics outMetrics = new DisplayMetrics();
//        display.getMetrics(outMetrics);
//        float density = getResources().getDisplayMetrics().density;
//        cardWidth = outMetrics.widthPixels;
//        cardWidth = cardWidth - ((2 * getResources().getDimension(R.dimen.sixteen_dp)) / density);
//    }
//
//    public float getScreenWidth() {
//        return cardWidth;
//    }
//
//
//    public void prepareSaveCard(SaveCardRequest creditCard) {
//        ArrayList<SaveCardRequest> requests = new ArrayList<>();
//        requests.addAll(getCreditCardList());
//        String cardType = midtransSDK.getTransactionRequest().getCardClickType();
//        SaveCardRequest savedCard = findCardByMaskedNumber(creditCard.getMaskedCard(), requests);
//        if (savedCard != null) {
//            requests.remove(savedCard);
//        }
//        requests.add(new SaveCardRequest(creditCard.getSavedTokenId(), creditCard.getMaskedCard(), cardType));
//        saveCreditCards(requests, false);
//    }
//
//    private SaveCardRequest findCardByMaskedNumber(String maskedCard, ArrayList<SaveCardRequest> savedCards) {
//        for (SaveCardRequest card : savedCards) {
//            if (card.getMaskedCard().equals(maskedCard)) {
//                return card;
//            }
//        }
//        return null;
//    }
//
//    public void saveCreditCards(ArrayList<SaveCardRequest> requests, boolean isRemoveCard) {
//        Log.d(TAG, "savecards>isremoveCard:" + isRemoveCard);
//        this.removeExistCard = isRemoveCard;
//        midtransSDK.saveCards(userDetail.getUserId(), requests, new SaveCardCallback() {
//            @Override
//            public void onSuccess(SaveCardResponse response) {
//                Log.d(TAG, "savecards>success");
//
//                if (removeExistCard) {
//                    UiUtils.hideProgressDialog();
//                    Fragment currentFragment = getCurrentFagment(AddCardDetailsFragment.class);
//                    if (currentFragment != null) {
//                        Logger.d(TAG, "Delete card success");
//                        onBackPressed();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(String reason) {
//                Log.d(TAG, "savecredicards>failure");
//
//                if (removeExistCard) {
//                    UiUtils.hideProgressDialog();
//                    UiUtils.showToast(CreditDebitCardFlowActivity.this, reason);
//                    removeExistCard = false;
//                }
//            }
//
//            @Override
//            public void onError(Throwable error) {
//                Log.d(TAG, "savecredicards>error");
//
//                if (removeExistCard) {
//                    UiUtils.hideProgressDialog();
//                }
//                Log.e(TAG, error.getMessage(), error);
//            }
//        });
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Logger.i(TAG, "reqCode:" + requestCode + ",res:" + resultCode);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == PAYMENT_WEB_INTENT) {
//                payUsingCard();
//            } else if (requestCode == SCAN_REQUEST_CODE) {
//                if (data != null && data.hasExtra(ExternalScanner.EXTRA_SCAN_DATA)) {
//
//                    ScannerModel scanData = (ScannerModel) data.getSerializableExtra(ExternalScanner.EXTRA_SCAN_DATA);
//                    Logger.i(String.format("Card Number: %s, Card Expire: %s/%d", scanData.getCardNumber(), scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
//                    updateCreditCardData(Utils.getFormattedCreditCardNumber(scanData.getCardNumber()), scanData.getCvv(), String.format("%s/%d", scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
//                } else {
//                    Logger.d("No result");
//                }
//            } else {
//                Logger.d("Not available");
//            }
//        } else if (resultCode == RESULT_CANCELED) {
//            if (requestCode == PAYMENT_WEB_INTENT) {
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        processingLayout.setVisibility(View.GONE);
//                    }
//                }, 200);
//                CreditDebitCardFlowActivity.this.errorMessage = getString(R.string.payment_canceled);
//                UiUtils.hideProgressDialog();
//                initPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT, true);
//                tvHeaderTitle.setText(getString(R.string.title_payment_status));
//            } else if (requestCode == SCAN_REQUEST_CODE) {
//                Logger.d(TAG, "scancard:canceled");
//            }
//        }
//    }
//
//    private void updateCreditCardData(String cardNumber, String cvv, String expired) {
//        // Update credit card data in AddCardDetailsFragment
//        Fragment fragment = getCurrentFagment(AddCardDetailsFragment.class);
//        if (fragment != null) {
//            ((AddCardDetailsFragment) fragment).updateFromScanCardEvent(new CreditCardFromScanner(cardNumber, cvv, expired));
//        }
//    }
//
//    public ArrayList<SaveCardRequest> getCreditCards() {
//        if (creditCards == null || creditCards.isEmpty()) {
//            fetchCreditCards();
//        }
//        return creditCards;
//    }
//
//    public ArrayList<SaveCardRequest> getCreditCardList() {
//        return creditCards;
//    }
//
//    public void fetchCreditCards() {
//        try {
//            UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
//            if (userDetail != null) {
//                if (midtransSDK.isEnableBuiltInTokenStorage()) {
//                    List<SavedToken> tokens = midtransSDK.getCreditCard().getSavedTokens();
//                    bindSavedCards(tokens != null ? convertSavedToken(tokens) : new ArrayList<SaveCardRequest>());
//                } else {
//                    UiUtils.showProgressDialog(this, getString(R.string.fetching_cards), true);
//                    midtransSDK.getCards(userDetail.getUserId(), new GetCardCallback() {
//                        @Override
//                        public void onSuccess(ArrayList<SaveCardRequest> response) {
//                            ArrayList<SaveCardRequest> cardResponse = response;
//                            UiUtils.hideProgressDialog();
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    processingLayout.setVisibility(View.GONE);
//                                }
//                            }, 200);
//                            Logger.i("cards api successful" + cardResponse);
//                            filterMultipleSavedCard(cardResponse);
//                            bindSavedCards(cardResponse);
//                        }
//
//                        @Override
//                        public void onFailure(String reason) {
//                            UiUtils.hideProgressDialog();
//                            Logger.i(TAG, "card fetching failed :" + reason);
//                            processingLayout.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onError(Throwable error) {
//                            UiUtils.hideProgressDialog();
//                            showCardDetailFragment(null, null);
//                        }
//                    });
//                }
//            }
//        } catch (Exception e) {
//            Logger.e(TAG, e.getMessage());
//        }
//    }
//
//    private void filterMultipleSavedCard(ArrayList<SaveCardRequest> savedCards) {
//        Collections.reverse(savedCards);
//        Set<String> maskedCardSet = new HashSet<>();
//        for (Iterator<SaveCardRequest> it = savedCards.iterator(); it.hasNext(); ) {
//            if (!maskedCardSet.add(it.next().getMaskedCard())) {
//                it.remove();
//            }
//        }
//    }
//
//    public void oneClickPayment(String maskedCardNumber) {
//        this.isNewCard = false;
//        this.maskedCardNumber = maskedCardNumber;
//        payUsingCard();
//    }
//
//    public void twoClickPayment(CardTokenRequest cardDetail) {
//        this.isNewCard = false;
//        CardTokenRequest cardTokenRequest = new CardTokenRequest();
//        cardTokenRequest.setSavedTokenId(cardDetail.getSavedTokenId());
//        cardTokenRequest.setCardCVV(cardDetail.getCardCVV());
//        cardTokenRequest.setGrossAmount(cardDetail.getGrossAmount());
//        cardTokenRequest.setTwoClick(true);
//        cardTokenRequest.setSecure(midtransSDK.getTransactionRequest().isSecureCard());
//        cardTokenRequest.setBank(midtransSDK.getTransactionRequest().getCreditCard().getBank());
//        cardTokenRequest.setClientKey(midtransSDK.getClientKey());
//        if (midtransSDK.getCreditCard().getType() != null
//                && !TextUtils.isEmpty(midtransSDK.getCreditCard().getType())) {
//            cardTokenRequest.setType(midtransSDK.getCreditCard().getType());
//        }
//        initInstallmentProperties(cardTokenRequest);
//        initAcquiringBank(cardTokenRequest);
//        this.cardTokenRequest = cardTokenRequest;
//        getCardToken(cardTokenRequest);
//    }
//
//    private void initInstallmentProperties(CardTokenRequest cardTokenRequest) {
//        int termSelected = creditCardTransaction.getInstallmentTermSelected();
//        Log.d(TAG, "term:" + termSelected);
//        if (termSelected > 0) {
//            cardTokenRequest.setInstallment(true);
//            cardTokenRequest.setInstalmentTerm(termSelected);
//        }
//    }
//
//    private void actionGetCardTokenFailure(TokenDetailsResponse response, String reason) {
//        UiUtils.hideProgressDialog();
//        UiUtils.showApiFailedMessage(CreditDebitCardFlowActivity.this, getString(R.string.message_getcard_token_failed));
//    }
//
//    public ArrayList<BankDetail> getBankDetails() {
//        if (bankDetails != null && !bankDetails.isEmpty()) {
//            return bankDetails;
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (readBankDetailTask != null && readBankDetailTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
//            readBankDetailTask.cancel(true);
//        }
//    }
//
//    private void readBankDetails() {
//        if (bankDetails == null || bankDetails.isEmpty()) {
//            readBankDetailTask = new ReadBankDetailTask(this, this);
//            readBankDetailTask.execute();
//        }
//    }
//
//    @Override
//    public void onReadBankDetailsFinish(ArrayList<BankDetail> bankDetails, UserDetail userDetail) {
//        if (userDetail != null) {
//            this.userDetail = userDetail;
//        }
//        if (this.bankDetails != null) {
//            this.bankDetails = bankDetails;
//            Logger.i(TAG, "bank>size:" + bankDetails.size());
//        }
//    }
//
//    public void completePayment() {
//        completePayment(transactionResponse, errorMessage);
//    }
//
//    public int dimen(@DimenRes int resId) {
//        return (int) getResources().getDimension(resId);
//    }
//
//    public int color(@ColorRes int resId) {
//        return ContextCompat.getColor(this, resId);
//    }
//
//    public int integer(@IntegerRes int resId) {
//        return getResources().getInteger(resId);
//    }
//
//    private void showErrorMessage(String errorMessage) {
//        AlertDialog alert = new AlertDialog.Builder(this)
//                .setMessage(errorMessage)
//                .setNegativeButton(R.string.btn_close, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                        completePayment();
//                    }
//                })
//                .create();
//        alert.show();
//    }
//
//    public void setSavedCardInfo(boolean saveCard, String cardType) {
//        this.saveCard = saveCard;
//    }
//
//    private ArrayList<SaveCardRequest> convertSavedToken(List<SavedToken> savedTokens) {
//        ArrayList<SaveCardRequest> saveCardRequests = new ArrayList<>();
//        for (SavedToken savedToken : savedTokens) {
//            saveCardRequests.add(new SaveCardRequest(savedToken.getToken(), savedToken.getMaskedCard(), savedToken.getTokenType()));
//        }
//        return saveCardRequests;
//    }
//
//    private void bindSavedCards(ArrayList<SaveCardRequest> cards) {
//        ArrayList<SaveCardRequest> filteredCards = filterCardsByClickType(cards);
//
//        if (!filteredCards.isEmpty()) {
//            creditCards.clear();
//
//            creditCards.addAll(filteredCards);
//            //processingLayout.setVisibility(View.GONE);
//            if (emptyCardsTextView != null) {
//                if (!creditCards.isEmpty()) {
//                    emptyCardsTextView.setVisibility(View.GONE);
//                } else {
//                    emptyCardsTextView.setVisibility(View.VISIBLE);
//                }
//            }
//
//            SavedCardFragment savedCardFragment = SavedCardFragment.newInstance();
//            //getSupportActionBar().setTitle(getString(R.string.saved_card));
//            tvHeaderTitle.setText(getString(R.string.saved_card));
//            replaceFragment(savedCardFragment, R.id.card_container, true, false);
//
//        } else {
//            showCardDetailFragment(null, null);
//        }
//    }
//
//    private ArrayList<SaveCardRequest> filterCardsByClickType(ArrayList<SaveCardRequest> cards) {
//
//        ArrayList<SaveCardRequest> filteredCards = new ArrayList<>();
//        if (cards != null && !cards.isEmpty()) {
//            if (midtransSDK.isEnableBuiltInTokenStorage()) {
//                for (SaveCardRequest card : cards) {
//                    if (midtransSDK.getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_one_click))
//                            && card.getType().equals(getString(R.string.saved_card_one_click))) {
//                        filteredCards.add(card);
//                    } else if (midtransSDK.getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_two_click))
//                            && card.getType().equals(getString(R.string.saved_card_two_click))) {
//                        filteredCards.add(card);
//                    }
//                }
//            } else {
//                //if token storage on merchantserver then saved cards can be used just for two click
//                String clickType = midtransSDK.getTransactionRequest().getCardClickType();
//                if (!TextUtils.isEmpty(clickType) && clickType.equals(getString(R.string.card_click_type_two_click))) {
//                    filteredCards.addAll(cards);
//                }
//            }
//        }
//
//        return filteredCards;
//    }
//
//
//    public void initBankBins() {
//        MidtransSDK.getInstance().getBankBins(new BankBinsCallback() {
//            @Override
//            public void onSuccess(ArrayList<BankBinsResponse> response) {
//                creditCardTransaction.setBankBins(response);
//            }
//
//            @Override
//            public void onFailure(String reason) {
//                Log.i(TAG, "bankbins>failure:" + reason);
//            }
//
//            @Override
//            public void onError(Throwable error) {
//                Log.i(TAG, "bankbins>error:" + error.getMessage());
//
//            }
//        });
//    }
//
//    public boolean isCardBinValid(String cardBin) {
//        return creditCardTransaction.isInWhiteList(cardBin);
//    }
//
//
//    public ArrayList<Integer> getInstallmentTerms(String cardBin) {
//        return creditCardTransaction.getInstallmentTerms(cardBin);
//    }
//
//    public String getBankByBin(String cardBin) {
//        return creditCardTransaction.getBankByBin(cardBin);
//    }
//
//    public int getInstallmentTerm(int currentPosition) {
//        return creditCardTransaction.getInstallmentTerm(currentPosition);
//    }
//
//    public void setInstallment(int termPosition) {
//        this.creditCardTransaction.setInstallment(termPosition);
//    }
//
//    public boolean isWhiteListBinsAvailable() {
//        return creditCardTransaction.isWhiteListBinsAvailable();
//    }
//
//    public void setFromSavedCard(boolean fromSavedCard) {
//        this.fromSavedCard = fromSavedCard;
//    }
//
//    public void showAddCardDetailFragment(SaveCardRequest card, PromoResponse promoResponse) {
//        showCardDetailFragment(card, promoResponse);
//    }
//
//    public void showAddCardDetailFragment(SaveCardRequest card) {
//        showCardDetailFragment(card);
//    }
//
//    private void showCardDetailFragment(SaveCardRequest card, PromoResponse promoResponse) {
//        if (card == null) {
//            showDeleteCardIcon(false);
//        } else {
//            setFromSavedCard(true);
//        }
//        AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance(card, promoResponse);
//        replaceFragment(addCardDetailsFragment, R.id.card_container, true, false);
//        tvHeaderTitle.setText(getString(R.string.card_details));
//    }
//
//    private void showCardDetailFragment(SaveCardRequest card) {
//        if (card == null) {
//            showDeleteCardIcon(false);
//        } else {
//            setFromSavedCard(true);
//        }
//        AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance(card);
//        replaceFragment(addCardDetailsFragment, R.id.card_container, true, false);
//        tvHeaderTitle.setText(getString(R.string.card_details));
//    }
//
//    public void showDeleteCardIcon(boolean show) {
//        if (show) {
//            ivDeleteSavedCard.setVisibility(View.VISIBLE);
//        } else {
//            ivDeleteSavedCard.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    public boolean isRemoveExistCard() {
//        return removeExistCard;
//    }
//
//    public void setRemoveExistCard(boolean removeExistCard) {
//        this.removeExistCard = removeExistCard;
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    public boolean isInstallmentAvailable() {
//        return creditCardTransaction.isInstallmentAvailable();
//    }
//
//
//    public boolean isValidInstallment() {
//        return creditCardTransaction.isInstallmentValid();
//    }
//
//    public void setInstallmentAvailableStatus(boolean installmentStatus) {
//        creditCardTransaction.setInstallmentAvailableStatus(installmentStatus);
//    }
//
//    public void setDiscountToken(String discountToken) {
//        this.discountToken = discountToken;
//    }
//
    public void setViewTotalAmount(double amount) {
        tvTotalAmount.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(amount)));
    }


    public void showHideProcessingLayout(boolean show) {
        if (show) {
            layoutProcessing.setVisibility(View.VISIBLE);
        } else {
            layoutProcessing.setVisibility(View.GONE);
        }
    }

    public CreditCardPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(TAG, "reqCode:" + requestCode + " | res:" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.IntentCode.PAYMENT_WEB_INTENT) {
                presenter.payUsingCard();
            } else if (requestCode == Constants.IntentCode.SCAN_REQUEST_CODE) {
                if (data != null && data.hasExtra(ExternalScanner.EXTRA_SCAN_DATA)) {

                    ScannerModel scanData = (ScannerModel) data.getSerializableExtra(ExternalScanner.EXTRA_SCAN_DATA);
                    Logger.d(TAG, String.format("Card Number: %s, Card Expire: %s/%d", scanData.getCardNumber(), scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                    updateCreditCardData(Utils.getFormattedCreditCardNumber(scanData.getCardNumber()), scanData.getCvv(), String.format("%s/%d", scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                } else {
                    Logger.d(TAG, "No result");
                }
            } else {
                Logger.d(TAG, "scancard:Not available");
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == Constants.IntentCode.PAYMENT_WEB_INTENT) {
                UiUtils.hideProgressDialog();
                String errorMessage = getString(R.string.payment_canceled);
                initPaymentResult(new PaymentResult(errorMessage), Payment.Type.CREDIT_CARD);
            } else if (requestCode == Constants.IntentCode.SCAN_REQUEST_CODE) {
                Logger.d(TAG, "scancard:canceled");
            }
        }
    }

    private void updateCreditCardData(String cardNumber, String cvv, String expired) {
        // Update credit card data in AddCardDetailsFragment
        presenter.setCardDetailFromScanner(cardNumber, cvv, expired);
    }
}
