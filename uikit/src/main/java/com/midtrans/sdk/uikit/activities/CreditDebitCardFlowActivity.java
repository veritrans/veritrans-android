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
import android.support.v4.app.FragmentManager;
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

import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.BankDetail;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CardTransfer;
import com.midtrans.sdk.corekit.models.CreditCardFromScanner;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.Installment;
import com.midtrans.sdk.corekit.models.snap.SavedToken;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.adapters.CardPagerAdapter;
import com.midtrans.sdk.uikit.fragments.AddCardDetailsFragment;
import com.midtrans.sdk.uikit.fragments.PaymentTransactionStatusFragment;
import com.midtrans.sdk.uikit.fragments.SavedCardFragment;
import com.midtrans.sdk.uikit.fragments.WebviewFragment;
import com.midtrans.sdk.uikit.scancard.ExternalScanner;
import com.midtrans.sdk.uikit.scancard.ScannerModel;
import com.midtrans.sdk.uikit.utilities.ReadBankDetailTask;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.CirclePageIndicator;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.MorphingButton;

import java.util.ArrayList;
import java.util.List;

import static com.midtrans.sdk.uikit.utilities.ReadBankDetailTask.ReadBankDetailCallback;


public class CreditDebitCardFlowActivity extends BaseActivity implements ReadBankDetailCallback {
    public static final String PAYMENT_CREDIT_CARD = "cc";
    public static final int SCAN_REQUEST_CODE = 101;
    private static final int PAYMENT_WEB_INTENT = 100;
    private static final String KEY_SCAN_SUCCESS_EVENT = "Scan Card Success";
    private static final String KEY_SCAN_FAILED_EVENT = "Scan Card Failed";
    private static final String KEY_SCAN_CANCELLED_EVENT = "Scan Card Cancelled";
    private static final int PAY_USING_CARD = 51;
    private static final String TAG = "CreditCardActivity";
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private MidtransSDK midtransSDK;
    private float cardWidth;
    private UserDetail userDetail;
    private TokenDetailsResponse tokenDetailsResponse;
    private CardTokenRequest cardTokenRequest;
    private CardTransfer cardTransfer;
    private ArrayList<SaveCardRequest> creditCards = new ArrayList<>();
    private RelativeLayout processingLayout;
    private ArrayList<BankDetail> bankDetails;
    private ReadBankDetailTask readBankDetailTask;

    //for setResult
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private CardPagerAdapter cardPagerAdapter;
    private CirclePageIndicator circlePageIndicator;
    private TextView emptyCardsTextView;
    private TextView titleHeaderTextView;
    private DefaultTextView textOrderId, textTotalAmount;
    private ImageView logo;
    private int fabHeight;
    private MorphingButton btnMorph;
    private boolean saveCard = false;
    private String cardType;
    private boolean removeExistCard = false;
    private String maskedCardNumber;
    private boolean isNewCard = true;
    private ArrayList<BankBinsResponse> bankBins = new ArrayList<>();
    private String installmentBank;
    private ArrayList<Integer> installmentTerms = new ArrayList<>();
    private int installmentTermSelected;

    public MorphingButton getBtnMorph() {
        return btnMorph;
    }

    public TextView getTitleHeaderTextView() {
        return titleHeaderTextView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_debit_card_flow);
        processingLayout = (RelativeLayout) findViewById(R.id.processing_layout);
        midtransSDK = MidtransSDK.getInstance();
        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        titleHeaderTextView = (TextView) findViewById(R.id.text_title);
        btnMorph = (MorphingButton) findViewById(R.id.btnMorph1);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        textOrderId = (DefaultTextView) findViewById(R.id.text_order_id);
        textTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);

        initializeTheme();
        morphToCircle(0);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calculateScreenWidth();
        if (midtransSDK != null) {
            initBankBins();
            if (!midtransSDK.getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_none))) {
                getCreditCards();
            } else {
                AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance();
                replaceFragment(addCardDetailsFragment, R.id.card_container, true, false);
                titleHeaderTextView.setText(getString(R.string.card_details));
            }
            textOrderId.setText(midtransSDK.getTransactionRequest().getOrderId());
            textTotalAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(midtransSDK.getTransactionRequest().getAmount())));
        }
        readBankDetails();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void morphToCircle(int time) {
        MorphingButton.Params circle = morphCicle(time);
        btnMorph.morph(circle);
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
        SdkUIFlowUtil.hideKeyboard(this);
        if (!TextUtils.isEmpty(currentFragmentName) && currentFragmentName.equalsIgnoreCase(PaymentTransactionStatusFragment.class
                .getName())) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
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
            CreditDebitCardFlowActivity.this.cardTokenRequest.setBank(tokenDetailsResponse.getBank());
        }
        CreditDebitCardFlowActivity.this.tokenDetailsResponse = tokenDetailsResponse;

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
            payUsingCard();
        }
    }

    /**
     * start credit card payment
     */
    public void payUsingCard() {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        processingLayout.setVisibility(View.VISIBLE);
        titleHeaderTextView.setText(getString(R.string.processing_payment));

        CreditCardPaymentModel paymentModel;
        if (midtransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase(getString(R.string.card_click_type_one_click))
                && !isNewCard && this.maskedCardNumber != null) {
            //using one click
            paymentModel = new CreditCardPaymentModel(this.maskedCardNumber);
        } else if (tokenDetailsResponse != null && !TextUtils.isEmpty(tokenDetailsResponse.getTokenId())) {
            //using normal payment & twoclick & oneclick first payment
            paymentModel = new CreditCardPaymentModel(tokenDetailsResponse.getTokenId(), saveCard);
        } else {
            SdkUIFlowUtil.showSnackbar(this, getString(R.string.message_payment_not_completed));
            processingLayout.setVisibility(View.GONE);
            SdkUIFlowUtil.hideProgressDialog();

            return;
        }

        if (installmentTermSelected > 0) {
            paymentModel.setInstallment(installmentBank + "_" + installmentTermSelected);
        }

        midtransSDK.paymentUsingCard(midtransSDK.readAuthenticationToken(),
                paymentModel, new TransactionCallback() {
                    @Override
                    public void onSuccess(TransactionResponse response) {
                        actionPaymentSuccess(response);
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
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
                        SdkUIFlowUtil.hideProgressDialog();
                        initPaymentStatus(transactionResponse, errorMessage, true);
                        titleHeaderTextView.setText(getString(R.string.title_payment_status));
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
        initPaymentStatus(transactionResponse, null, true);
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
        requests.add(new SaveCardRequest(creditCard.getSavedTokenId(), creditCard.getMaskedCard(), cardType));
        saveCreditCards(requests, false);
    }

    public void saveCreditCards(ArrayList<SaveCardRequest> requests, boolean isRemoveCard) {
        this.removeExistCard = isRemoveCard;
        midtransSDK.saveCards(userDetail.getUserId(), requests, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {
                if (removeExistCard) {
                    SdkUIFlowUtil.hideProgressDialog();
                    removeExistCard = false;
                    Fragment currentFragment = getCurrentFagment(SavedCardFragment.class);
                    if (currentFragment != null) {
                        Logger.d("Delete card success");
                        ((SavedCardFragment) currentFragment).onDeleteCardSuccess();
                    }
                }
            }

            @Override
            public void onFailure(String reason) {
                if (removeExistCard) {
                    SdkUIFlowUtil.hideProgressDialog();
                    SdkUIFlowUtil.showSnackbar(CreditDebitCardFlowActivity.this, reason);
                    removeExistCard = false;
                }
            }

            @Override
            public void onError(Throwable error) {
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
                payUsingCard();
            } else if (requestCode == SCAN_REQUEST_CODE) {
                if (data != null && data.hasExtra(ExternalScanner.EXTRA_SCAN_DATA)) {
                    // track scan event success
                    midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SCAN_SUCCESS_EVENT, PAYMENT_CREDIT_CARD, null);

                    ScannerModel scanData = (ScannerModel) data.getSerializableExtra(ExternalScanner.EXTRA_SCAN_DATA);
                    Logger.i(String.format("Card Number: %s, Card Expire: %s/%d", scanData.getCardNumber(), scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                    updateCreditCardData(Utils.getFormattedCreditCardNumber(scanData.getCardNumber()), scanData.getCvv(), String.format("%s/%d", scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                } else {
                    // track scan event failed
                    midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SCAN_FAILED_EVENT, PAYMENT_CREDIT_CARD, null);

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
                initPaymentStatus(transactionResponse, errorMessage, true);
                titleHeaderTextView.setText(getString(R.string.title_payment_status));
            } else if (requestCode == SCAN_REQUEST_CODE) {
                // track scan cancelled
                midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(KEY_SCAN_CANCELLED_EVENT, PAYMENT_CREDIT_CARD, null);
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
                            AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance();
                            replaceFragment(addCardDetailsFragment, R.id.card_container, true, false);
                            titleHeaderTextView.setText(getString(R.string.card_details));
                        }
                    });
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    public void oneClickPayment(String maskedCardNumber) {
        this.isNewCard = false;
        this.maskedCardNumber = maskedCardNumber;
        payUsingCard();
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

    private void initInstallmentProperties(CardTokenRequest cardTokenRequest) {
        Log.d("installment:selected", "term:" + installmentTermSelected);
        if (installmentTermSelected > 0) {
            cardTokenRequest.setInstallment(true);
            cardTokenRequest.setInstalmentTerm(installmentTermSelected);
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
            Logger.i("bankdetail getter onnext" + bankDetails.size());
        }
    }

    public void setResultAndFinish() {
        setResultAndFinish(transactionResponse, errorMessage);
    }

    public void setAdapterViews(CardPagerAdapter cardPagerAdapter, CirclePageIndicator circlePageIndicator, TextView emptyCardsTextView) {
        this.cardPagerAdapter = cardPagerAdapter;
        this.circlePageIndicator = circlePageIndicator;
        this.emptyCardsTextView = emptyCardsTextView;
    }

    public int getFabHeight() {
        return fabHeight;
    }

    public void setFabHeight(int fabHeight) {
        Logger.i("fab_height:" + fabHeight);
        this.fabHeight = fabHeight;
    }

    public void morphingAnimation() {
        Logger.i("morphingAnimation");
        //Logger.i("64dp:"+ Utils.dpToPx(56));
        btnMorph.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MorphingButton.Params square = MorphingButton.Params.create()
                        .duration((int) Constants.CARD_ANIMATION_TIME)
                        .cornerRadius(dimen(R.dimen.mb_corner_radius_2))
                        .width((int) cardWidth)
                        .height(Utils.dpToPx(Constants.FAB_HEIGHT_DP))
                        .text(getString(R.string.pay_now))
                        .colorPressed(color(R.color.colorAccent))
                        .color(color(R.color.colorAccent));
                btnMorph.morph(square);
            }
        }, 50);
    }

    public MorphingButton.Params morphCicle(int time) {
        return MorphingButton.Params.create()
                .cornerRadius(Utils.dpToPx(Constants.FAB_HEIGHT_DP))
                .width(Utils.dpToPx(Constants.FAB_HEIGHT_DP))
                .height(Utils.dpToPx(Constants.FAB_HEIGHT_DP))
                .duration(time)
                .colorPressed(color(R.color.colorAccent))
                .color(color(R.color.colorAccent));
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
        this.cardType = cardType;
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
            if (cardPagerAdapter != null && circlePageIndicator != null) {
                cardPagerAdapter.notifyDataSetChanged();
                circlePageIndicator.notifyDataSetChanged();
            }
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
            AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance();
            replaceFragment(addCardDetailsFragment, R.id.card_container, true, false);
            titleHeaderTextView.setText(getString(R.string.card_details));
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
                        filteredCards.add(card);
                    } else if (midtransSDK.getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_two_click))
                            && card.getType().equals(getString(R.string.saved_card_two_click))) {
                        filteredCards.add(card);
                    }
                }
            } else {
                //if token storage on merchantserver then saved cards can be used just for two click
                filteredCards.addAll(cards);
            }
        }

        return filteredCards;
    }


    public void initBankBins() {
        ArrayList<BankBinsResponse> loadedBankBins = SdkUIFlowUtil.getBankBins(this);
        if (loadedBankBins != null && !loadedBankBins.isEmpty()) {
            bankBins.clear();
            bankBins.addAll(loadedBankBins);
        }
    }


    public ArrayList<Integer> getInstallmentTerms(String cardBin) {
        if (isInWhiteList(cardBin)) {
            String bank = getBankByBin(cardBin);
            if (TextUtils.isEmpty(bank)) {
                bank = getString(R.string.bank_installment_offline);
            }

            Installment installment = midtransSDK.getCreditCard().getInstallment();
            if (installment != null && installment.getTerms() != null && !installment.getTerms().isEmpty()) {
                for (String key : installment.getTerms().keySet()) {
                    if (key.equals(bank)) {
                        this.installmentBank = bank;
                        this.installmentTerms.clear();
                        this.installmentTerms.add(0, 0);
                        this.installmentTerms.addAll(installment.getTerms().get(key));

                        return this.installmentTerms;
                    }
                }
            }
        }

        return null;
    }

    private String getBankByBin(String cardBin) {
        for (BankBinsResponse savedBankBin : bankBins) {
            if (savedBankBin.getBins() != null && !savedBankBin.getBins().isEmpty()) {
                String bankBin = findBankByCardBin(savedBankBin, cardBin);
                if (bankBin != null) {
                    return bankBin;
                }
            }
        }
        return null;
    }

    private String findBankByCardBin(BankBinsResponse savedBankBin, String cardBin) {
        for (String savedBin : savedBankBin.getBins()) {
            if (savedBin.equals(cardBin)) {
                return savedBankBin.getBank();
            }
        }
        return null;
    }

    private boolean isInWhiteList(String cardBin) {
        ArrayList<String> whiteListBins = midtransSDK.getCreditCard().getWhitelistBins();
        if (whiteListBins != null && !whiteListBins.isEmpty()) {
            if (whiteListBins.contains(cardBin)) {
                return true;
            }
        }
        return false;
    }

    public Integer getInstallmentTerm(int currentPosition) {
        return this.installmentTerms.get(currentPosition);
    }

    public void setInstallment(int termPosition) {
        this.installmentTermSelected = this.installmentTerms.size() == 0 ? 0 : this.installmentTerms.get(termPosition);
    }
}
