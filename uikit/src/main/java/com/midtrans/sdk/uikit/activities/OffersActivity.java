package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.midtrans.sdk.corekit.models.BillingAddress;
import com.midtrans.sdk.corekit.models.CardPaymentDetails;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CardTransfer;
import com.midtrans.sdk.corekit.models.CreditCardFromScanner;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.OffersListModel;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionDetails;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.adapters.CardPagerAdapter;
import com.midtrans.sdk.uikit.fragments.OffersAddCardDetailsFragment;
import com.midtrans.sdk.uikit.fragments.OffersListFragment;
import com.midtrans.sdk.uikit.fragments.PaymentTransactionStatusFragment;
import com.midtrans.sdk.uikit.scancard.ExternalScanner;
import com.midtrans.sdk.uikit.scancard.ScannerModel;
import com.midtrans.sdk.uikit.utilities.ReadBankDetailTask;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.CirclePageIndicator;
import com.midtrans.sdk.uikit.widgets.MorphingButton;

import java.util.ArrayList;

/**
 * Created by Ankit on 12/7/15.
 */
public class OffersActivity extends BaseActivity implements ReadBankDetailTask.ReadBankDetailCallback {

    public static final int SCAN_REQUEST_CODE = 101;
    public static final String OFFERS_FRAGMENT = "offersList";
    public static final String ADD_CARD_FRAGMENT = "addCard";
    public static final String OFFER_POSITION = "offer_position";
    public static final String OFFER_NAME = "offer_name";
    public static final String OFFER_TYPE = "offer_type";
    public static final String OFFER_TYPE_BINPROMO = "offer_type_binpromo";
    public static final String OFFER_TYPE_INSTALMENTS = "offer_type_instalments";
    private static final int PAYMENT_WEB_INTENT = 200;
    private static final int PAY_USING_CARD = 61;
    private static final String TAG = "OffersActivity";
    public String currentFragment = "offersList";
    public ArrayList<OffersListModel> offersListModels = new ArrayList<>();
    public ArrayList<SaveCardRequest> creditCards = new ArrayList<>();
    private Toolbar toolbar = null;
    private TextView textViewTitleOffers = null;
    private MidtransSDK midtransSDK = null;
    private TextView textViewTitleCardDetails = null;
    private TextView textViewOfferName = null;
    private OffersListFragment offersListFragment = null;
    private int position = Constants.PAYMENT_METHOD_OFFERS;
    private OffersListModel selectedOffer;
    private int RESULT_CODE = RESULT_CANCELED;
    private String currentFragmentName;
    private FragmentManager fragmentManager;
    private float cardWidth;
    private UserDetail userDetail;
    private TokenDetailsResponse tokenDetailsResponse;
    private CardTokenRequest cardTokenRequest;
    private CardTransfer cardTransfer;
    private RelativeLayout processingLayout;
    private ArrayList<BankDetail> bankDetails;
    //for setResult
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private CardPagerAdapter cardPagerAdapter;
    private CirclePageIndicator circlePageIndicator;
    private TextView emptyCardsTextView;
    private MorphingButton btnMorph;
    private ReadBankDetailTask readBankDetailTask;
    private ImageView logo = null;

    public OffersListModel getSelectedOffer() {
        return selectedOffer;
    }

    public void setSelectedOffer(OffersListModel selectedOffer) {
        this.selectedOffer = selectedOffer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        midtransSDK = MidtransSDK.getInstance();
        fragmentManager = getSupportFragmentManager();
        btnMorph = (MorphingButton) findViewById(R.id.btnMorph1);
        morphToCircle(0);

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(getString(R.string.position), Constants
                    .PAYMENT_METHOD_OFFERS);
        } else {
            SdkUIFlowUtil.showSnackbar(OffersActivity.this, getString(R.string.error_something_wrong));
            finish();
        }

        initializeView();
        OffersListFragment offersListFragment = new OffersListFragment();
        replaceFragment(offersListFragment, R.id.offers_container, true, false);
        calculateScreenWidth();
        // Only fetch credit card when card click type is two click or one click
        if (!midtransSDK.getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_none))) {
            getCreditCards();
        }
        readBankDetails();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initializeView() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        textViewTitleOffers = (TextView) findViewById(R.id.text_title);
        textViewTitleCardDetails = (TextView) findViewById(R.id.text_title_card_details);
        textViewOfferName = (TextView) findViewById(R.id.text_title_offer_name);
        initializeTheme();
        //setup tool bar
        toolbar.setTitle(""); // disable default Text
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        processingLayout = (RelativeLayout) findViewById(R.id.processing_layout);

        textViewTitleCardDetails.setVisibility(View.GONE);
        textViewOfferName.setVisibility(View.GONE);
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
        if (fragmentManager.getBackStackEntryCount() == 1) {
            setResultAndFinish();
        } else {
            if (!TextUtils.isEmpty(currentFragmentName) && currentFragmentName.equalsIgnoreCase(PaymentTransactionStatusFragment.class
                    .getName())) {
                setResultAndFinish();
            } else {

                super.onBackPressed();
            }
        }
    }

    public void getToken(CardTokenRequest cardTokenRequest) {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        this.cardTokenRequest = cardTokenRequest;
        Logger.i("isSecure:" + this.cardTokenRequest.isSecure());
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


    public void payUsingCard() {

        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        processingLayout.setVisibility(View.VISIBLE);
        textViewTitleOffers.setText(getString(R.string.processing_payment));
        CustomerDetails customerDetails = new CustomerDetails(userDetail.getUserFullName(), "",
                userDetail.getEmail(), userDetail.getPhoneNumber());

        ArrayList<UserAddress> userAddresses = userDetail.getUserAddresses();
        TransactionDetails transactionDetails = new TransactionDetails("" + midtransSDK.
                getTransactionRequest().getAmount(),
                midtransSDK.getTransactionRequest().getOrderId());
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        if (midtransSDK.getTransactionRequest().getItemDetails() != null && midtransSDK.getTransactionRequest().getItemDetails().size() > 0) {
            itemDetailsArrayList = midtransSDK.getTransactionRequest().getItemDetails();
        }
        if (userAddresses != null && !userAddresses.isEmpty()) {
            ArrayList<BillingAddress> billingAddresses = new ArrayList<>();
            ArrayList<ShippingAddress> shippingAddresses = new ArrayList<>();

            for (int i = 0; i < userAddresses.size(); i++) {
                UserAddress userAddress = userAddresses.get(i);
                if (userAddress.getAddressType() == Constants.ADDRESS_TYPE_BILLING ||
                        userAddress.getAddressType() == Constants.ADDRESS_TYPE_BOTH) {
                    BillingAddress billingAddress;
                    billingAddress = new BillingAddress();
                    billingAddress.setCity(userAddress.getCity());
                    billingAddress.setFirstName(userDetail.getUserFullName());
                    billingAddress.setLastName("");
                    billingAddress.setPhone(userDetail.getPhoneNumber());
                    billingAddress.setCountryCode(userAddress.getCountry());
                    billingAddress.setPostalCode(userAddress.getZipcode());
                    billingAddresses.add(billingAddress);
                }
                if (userAddress.getAddressType() == Constants.ADDRESS_TYPE_SHIPPING ||
                        userAddress.getAddressType() == Constants.ADDRESS_TYPE_BOTH) {
                    ShippingAddress shippingAddress;
                    shippingAddress = new ShippingAddress();
                    shippingAddress.setCity(userAddress.getCity());
                    shippingAddress.setFirstName(userDetail.getUserFullName());
                    shippingAddress.setLastName("");
                    shippingAddress.setPhone(userDetail.getPhoneNumber());
                    shippingAddress.setCountryCode(userAddress.getCountry());
                    shippingAddress.setPostalCode(userAddress.getZipcode());
                    shippingAddresses.add(shippingAddress);
                }

            }

            CardPaymentDetails cardPaymentDetails = null;
            try {
                Logger.i("savetokenid:" + cardTokenRequest.getSavedTokenId());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            //for one click
            if (midtransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase(getString(R.string.card_click_type_one_click))
                    && !TextUtils.isEmpty(cardTokenRequest.getSavedTokenId())) {
                if (cardTokenRequest.isInstalment()) {
                    cardPaymentDetails = new CardPaymentDetails(cardTokenRequest.getBank(),
                            cardTokenRequest.getSavedTokenId(), cardTokenRequest.isSaved(),
                            cardTokenRequest.getFormattedInstalmentTerm(), cardTokenRequest.getBins());
                } else {
                    cardPaymentDetails = new CardPaymentDetails(cardTokenRequest.getBank(),
                            cardTokenRequest.getSavedTokenId(), cardTokenRequest.isSaved());
                    cardPaymentDetails.setBinsArray(cardTokenRequest.getBins());
                }
                if (cardPaymentDetails.getBank().equals("")) {
                    cardPaymentDetails.setBank(null);
                }
                cardPaymentDetails.setRecurring(true);
            } else if (tokenDetailsResponse != null) {

                if (cardTokenRequest.isInstalment()) {
                    Logger.i("tokenDetailsResponse.getTokenId():" + tokenDetailsResponse.getTokenId());
                    cardPaymentDetails = new CardPaymentDetails(cardTokenRequest.getBank(),
                            tokenDetailsResponse.getTokenId(), cardTokenRequest.isSaved(),
                            cardTokenRequest.getFormattedInstalmentTerm(), cardTokenRequest.getBins());
                } else {
                    Logger.i("tokenDetailsResponse.getTokenId():" + tokenDetailsResponse.getTokenId());
                    cardPaymentDetails = new CardPaymentDetails(cardTokenRequest.getBank(),
                            tokenDetailsResponse.getTokenId(), cardTokenRequest.isSaved());
                    cardPaymentDetails.setBinsArray(cardTokenRequest.getBins());
                }


            } else {
                SdkUIFlowUtil.hideProgressDialog();
                processingLayout.setVisibility(View.GONE);
                return;
            }
            cardTransfer = new CardTransfer(cardPaymentDetails, transactionDetails,
                    itemDetailsArrayList, billingAddresses, shippingAddresses, customerDetails);

        }
        midtransSDK.paymentUsingCard(cardTokenRequest.getSavedTokenId(),
                midtransSDK.readAuthenticationToken(), false, new TransactionCallback() {
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
                        SdkUIFlowUtil.hideProgressDialog();
                        SdkUIFlowUtil.showSnackbar(OffersActivity.this, error.getMessage());
                    }
                });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("reqCode:" + requestCode + ",res:" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == PAYMENT_WEB_INTENT) {
                payUsingCard();
            } else if (requestCode == SCAN_REQUEST_CODE) {
                if (data != null && data.hasExtra(ExternalScanner.EXTRA_SCAN_DATA)) {
                    ScannerModel scanData = (ScannerModel) data.getSerializableExtra(ExternalScanner.EXTRA_SCAN_DATA);
                    Logger.i(String.format("Card Number: %s, Card Expire: %s/%d", scanData.getCardNumber(), scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                    updateCreditCardData(Utils.getFormattedCreditCardNumber(scanData.getCardNumber()), scanData.getCvv(), String.format("%s/%d", scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                } else {
                    Logger.d("No result");
                }
            }
        }
    }

    private void updateCreditCardData(String cardNumber, String cvv, String expired) {
        // Update credit card data in OffersAddCardDetailsFragment
        Fragment fragment = getCurrentFagment(OffersAddCardDetailsFragment.class);
        if (fragment != null) {
            ((OffersAddCardDetailsFragment) fragment).updateFromScanCardEvent(new CreditCardFromScanner(cardNumber, cvv, expired));
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
        textViewTitleOffers.setText(getString(R.string.fetching_cards));
        UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        midtransSDK.getCards(userDetail.getUserId(), new GetCardCallback() {
            @Override
            public void onSuccess(ArrayList<SaveCardRequest> response) {
                ArrayList<SaveCardRequest> cardResponse = response;
                //  SdkUIFlowUtil.hideProgressDialog();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        processingLayout.setVisibility(View.GONE);
                    }
                }, 200);

                Logger.i(TAG, "get cards success:" + cardResponse);
                if (cardResponse != null && !cardResponse.isEmpty()) {

                    creditCards.clear();
                    creditCards.addAll(cardResponse);
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
                }
            }

            @Override
            public void onFailure(String reason) {
                SdkUIFlowUtil.hideProgressDialog();
                Logger.i(TAG, "get card failed:" + reason);
                processingLayout.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable error) {
                SdkUIFlowUtil.hideProgressDialog();
                processingLayout.setVisibility(View.GONE);
                Logger.i(TAG, "get cards error:" + error.getMessage());
            }
        });
    }

    public void oneClickPayment(CardTokenRequest cardDetail) {
        this.cardTokenRequest = cardDetail;
        payUsingCard();
    }

    public void twoClickPayment(CardTokenRequest cardDetail) {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        this.cardTokenRequest = cardDetail;
        this.cardTokenRequest.setSavedTokenId(cardDetail.getSavedTokenId());
        this.cardTokenRequest.setCardCVV(cardDetail.getCardCVV());
        this.cardTokenRequest.setTwoClick(true);
        this.cardTokenRequest.setSecure(midtransSDK.getTransactionRequest().isSecureCard());
        this.cardTokenRequest.setGrossAmount(midtransSDK.getTransactionRequest().getAmount());
        this.cardTokenRequest.setBank("");
        this.cardTokenRequest.setClientKey(midtransSDK.getClientKey());
        midtransSDK.getCardToken(cardTokenRequest, new CardTokenCallback() {
            @Override
            public void onSuccess(TokenDetailsResponse response) {
                TokenDetailsResponse tokenDetailsResponse = response;
                if (tokenDetailsResponse != null) {
                    cardTokenRequest.setBank(tokenDetailsResponse.getBank());
                }
                OffersActivity.this.tokenDetailsResponse = tokenDetailsResponse;
                if (tokenDetailsResponse != null) {
                    Logger.i("token suc:" + tokenDetailsResponse.getTokenId() + ","
                            + midtransSDK.getTransactionRequest().isSecureCard());
                    if (midtransSDK.getTransactionRequest().isSecureCard()) {
                        SdkUIFlowUtil.hideProgressDialog();
                        if (tokenDetailsResponse.getRedirectUrl() != null &&
                                !tokenDetailsResponse.getRedirectUrl().equals("")) {
                            Intent intentPaymentWeb = new Intent(OffersActivity.this, PaymentWebActivity.class);
                            intentPaymentWeb.putExtra(Constants.WEBURL, tokenDetailsResponse.getRedirectUrl());
                            startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
                        }
                    } else {
                        SdkUIFlowUtil.showProgressDialog(OffersActivity.this, getString(R.string.processing_payment), false);
                        payUsingCard();
                    }
                }
            }

            @Override
            public void onFailure(TokenDetailsResponse response, String reason) {
                SdkUIFlowUtil.hideProgressDialog();
                SdkUIFlowUtil.showApiFailedMessage(OffersActivity.this, reason);
                Logger.i(TAG, "get card token error:" + reason);
            }

            @Override
            public void onError(Throwable error) {
                Logger.i(TAG, "get card token error:" + error.getMessage());
            }
        });
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
        if (this.readBankDetailTask != null && this.readBankDetailTask.getStatus().equals(AsyncTask.Status.RUNNING)) {
            this.readBankDetailTask.cancel(true);
        }
    }

    private void readBankDetails() {
        if (bankDetails == null || bankDetails.isEmpty()) {
            this.readBankDetailTask = new ReadBankDetailTask(this, this);
            readBankDetailTask.execute();
        }
    }


    @Override
    public void onReadBankDetailsFinish(ArrayList<BankDetail> bankDetails, UserDetail userDetail) {
        if (userDetail != null) {
            this.userDetail = userDetail;
        }
        if (bankDetails != null) {
            this.bankDetails = bankDetails;
        }
    }

    public void setResultAndFinish() {
        Intent data = new Intent();
        data.putExtra(getString(R.string.transaction_response), transactionResponse);
        data.putExtra(getString(R.string.error_transaction), errorMessage);
        setResult(RESULT_CODE, data);
        finish();
    }

    public void setResultCode(int resultCode) {
        this.RESULT_CODE = resultCode;
    }

    public void setAdapterViews(CardPagerAdapter cardPagerAdapter, CirclePageIndicator circlePageIndicator, TextView emptyCardsTextView) {
        this.cardPagerAdapter = cardPagerAdapter;
        this.circlePageIndicator = circlePageIndicator;
        this.emptyCardsTextView = emptyCardsTextView;
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

    public MorphingButton getBtnMorph() {
        return btnMorph;
    }

    public void morphToCircle(int time) {
        MorphingButton.Params circle = morphCicle(time);
        btnMorph.morph(circle);
    }

    private void actionGetCardTokenSuccess(TokenDetailsResponse response) {
        TokenDetailsResponse tokenDetailsResponse = response;
        if (tokenDetailsResponse != null) {
            cardTokenRequest.setBank(tokenDetailsResponse.getBank());
        }
        this.tokenDetailsResponse = tokenDetailsResponse;
        if (tokenDetailsResponse != null) {
            Logger.i("token suc:" + tokenDetailsResponse.getTokenId() + ","
                    + midtransSDK.getTransactionRequest().isSecureCard());
            if (midtransSDK.getTransactionRequest().isSecureCard()) {
                SdkUIFlowUtil.hideProgressDialog();
                if (tokenDetailsResponse.getRedirectUrl() != null &&
                        !tokenDetailsResponse.getRedirectUrl().equals("")) {
                    Intent intentPaymentWeb = new Intent(this, PaymentWebActivity.class);
                    intentPaymentWeb.putExtra(Constants.WEBURL, tokenDetailsResponse.getRedirectUrl());
                    startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
                }
            } else {
                SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
                payUsingCard();
            }
        }
    }


    private void actionGetCardTokenFailure(TokenDetailsResponse response, String reason) {
        SdkUIFlowUtil.hideProgressDialog();
        Logger.i("card fetching failed :" + reason);
        processingLayout.setVisibility(View.GONE);
    }


    private void actionGetCardTokenError(Throwable error) {
        SdkUIFlowUtil.showApiFailedMessage(OffersActivity.this, error.getMessage());
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

        SdkUIFlowUtil.hideProgressDialog();
        Logger.i("cardPaymentResponse:" + cardPaymentResponse.getStatusCode());

        if (cardPaymentResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200)) ||
                cardPaymentResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_201))) {

            transactionResponse = cardPaymentResponse;

            PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                    PaymentTransactionStatusFragment.newInstance(cardPaymentResponse);
            replaceFragment(paymentTransactionStatusFragment, R.id.offers_container, true, false);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            //getSupportActionBar().setTitle(getString(R.string.title_payment_successful));
            textViewTitleOffers.setText(getString(R.string.title_payment_status));
            if (cardTokenRequest.isSaved()) {
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

                if (bankDetails != null && !bankDetails.isEmpty()) {
                    String firstSix = cardTokenRequest.getCardNumber().substring(0, 6);
                    for (BankDetail bankDetail : bankDetails) {
                        if (bankDetail.getBin().equalsIgnoreCase(firstSix)) {
                            cardTokenRequest.setBank(bankDetail.getIssuing_bank());
                            cardTokenRequest.setCardType(bankDetail.getCard_association());
                            break;
                        }
                    }
                }

                if (cardTokenRequest.isSaved() && !TextUtils.isEmpty(cardPaymentResponse.getSavedTokenId())) {
                    cardTokenRequest.setSavedTokenId(cardPaymentResponse.getSavedTokenId());
                }
                Logger.i("Card:" + cardTokenRequest.getString());

                SaveCardRequest saveCardRequest = new SaveCardRequest();
                saveCardRequest.setSavedTokenId(cardTokenRequest.getSavedTokenId());
                saveCardRequest.setCode("200");
                String firstPart = cardTokenRequest.getCardNumber().substring(0, 6);
                String secondPart = cardTokenRequest.getCardNumber().substring(12);
                saveCardRequest.setMaskedCard(firstPart + "-" + secondPart);
                saveCreditCards(saveCardRequest);
                creditCards.add(saveCardRequest);
            }
        }
    }

    private void actionPaymentFailure(TransactionResponse response, String reason) {
        TransactionResponse transactionResponse = response;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                processingLayout.setVisibility(View.GONE);
            }
        }, 200);
        OffersActivity.this.transactionResponse = transactionResponse;
        OffersActivity.this.errorMessage = reason;
        SdkUIFlowUtil.hideProgressDialog();
        PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                PaymentTransactionStatusFragment.newInstance(transactionResponse);
        replaceFragment(paymentTransactionStatusFragment, R.id.offers_container, true, false);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setTitle(getString(R.string.title_payment_failed));
        textViewTitleOffers.setText(getString(R.string.title_payment_status));
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


    public void saveCreditCards(SaveCardRequest creditCard) {
        ArrayList<SaveCardRequest> cardRequests = new ArrayList<>(getCreditCardList());
        cardRequests.add(creditCard);
        midtransSDK.saveCards(userDetail.getUserId(), cardRequests, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {
                Logger.i(TAG, "save cards success");

            }

            @Override
            public void onFailure(String reason) {
                Logger.i(TAG, "save cards failed");

            }

            @Override
            public void onError(Throwable error) {
                Logger.i(TAG, "save cards error");
            }
        });
    }
}

