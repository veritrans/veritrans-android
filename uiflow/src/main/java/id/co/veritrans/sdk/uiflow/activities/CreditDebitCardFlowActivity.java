package id.co.veritrans.sdk.uiflow.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
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

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.UUID;

import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.GetCardBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.SaveCardBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TokenBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.Events;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetCardsSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.UpdateCreditCardDataFromScanEvent;
import id.co.veritrans.sdk.coreflow.models.BankDetail;
import id.co.veritrans.sdk.coreflow.models.BillingAddress;
import id.co.veritrans.sdk.coreflow.models.CardPaymentDetails;
import id.co.veritrans.sdk.coreflow.models.CardResponse;
import id.co.veritrans.sdk.coreflow.models.CardTokenRequest;
import id.co.veritrans.sdk.coreflow.models.CardTransfer;
import id.co.veritrans.sdk.coreflow.models.CustomerDetails;
import id.co.veritrans.sdk.coreflow.models.ItemDetails;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.coreflow.models.ShippingAddress;
import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;
import id.co.veritrans.sdk.coreflow.models.TransactionDetails;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.models.UserAddress;
import id.co.veritrans.sdk.coreflow.models.UserDetail;
import id.co.veritrans.sdk.coreflow.utilities.Utils;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.adapters.CardPagerAdapter;
import id.co.veritrans.sdk.uiflow.fragments.AddCardDetailsFragment;
import id.co.veritrans.sdk.uiflow.fragments.PaymentTransactionStatusFragment;
import id.co.veritrans.sdk.uiflow.fragments.SavedCardFragment;
import id.co.veritrans.sdk.uiflow.fragments.WebviewFragment;
import id.co.veritrans.sdk.uiflow.scancard.ExternalScanner;
import id.co.veritrans.sdk.uiflow.scancard.ScannerModel;
import id.co.veritrans.sdk.uiflow.utilities.ReadBankDetailTask;
import id.co.veritrans.sdk.uiflow.utilities.SdkUIFlowUtil;
import id.co.veritrans.sdk.uiflow.widgets.CirclePageIndicator;
import id.co.veritrans.sdk.uiflow.widgets.MorphingButton;

import static id.co.veritrans.sdk.uiflow.utilities.ReadBankDetailTask.ReadBankDetailCallback;


public class CreditDebitCardFlowActivity extends BaseActivity implements ReadBankDetailCallback,TransactionBusCallback, TokenBusCallback, SaveCardBusCallback, GetCardBusCallback {

    public static final int SCAN_REQUEST_CODE = 101;
    private static final int PAYMENT_WEB_INTENT = 100;
    private static final int PAY_USING_CARD = 51;
    private int RESULT_CODE = RESULT_CANCELED;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private VeritransSDK veritransSDK;
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
    private ImageView logo;
    private int fabHeight;
    private MorphingButton btnMorph;

    public MorphingButton getBtnMorph() {
        return btnMorph;
    }

    /*public void setBtnMorph(MorphingButton btnMorph) {
        this.btnMorph = btnMorph;
    }*/

    public TextView getTitleHeaderTextView() {
        return titleHeaderTextView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().register(this);
        }
        setContentView(R.layout.activity_credit_debit_card_flow);
        processingLayout = (RelativeLayout) findViewById(R.id.processing_layout);
        veritransSDK = VeritransSDK.getVeritransSDK();
        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleHeaderTextView = (TextView) findViewById(R.id.title_header);
        btnMorph = (MorphingButton) findViewById(R.id.btnMorph1);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        initializeTheme();
        morphToCircle(0);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calculateScreenWidth();
        if (veritransSDK != null) {

            if (!veritransSDK.getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_none))) {
                getCreditCards();
            } else {
                AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance();
                replaceFragment(addCardDetailsFragment, R.id.card_container, true, false);
                titleHeaderTextView.setText(getString(R.string.card_details));
            }
        }
        readBankDetails();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().unregister(this);
        }
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
        veritransSDK.getToken(cardTokenRequest);
    }

    public void payUsingCard() {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        processingLayout.setVisibility(View.VISIBLE);
        //getSupportActionBar().setTitle(getString(R.string.processing_payment));
        titleHeaderTextView.setText(getString(R.string.processing_payment));
        CustomerDetails customerDetails = new CustomerDetails(userDetail.getUserFullName(), "", userDetail.getEmail(), userDetail.getPhoneNumber());

        ArrayList<UserAddress> userAddresses = userDetail.getUserAddresses();
        TransactionDetails transactionDetails = new TransactionDetails("" + veritransSDK.
                getTransactionRequest().getAmount(),
                veritransSDK.getTransactionRequest().getOrderId());
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        if (veritransSDK.getTransactionRequest().getItemDetails() != null && veritransSDK.getTransactionRequest().getItemDetails().size() > 0) {
            itemDetailsArrayList = veritransSDK.getTransactionRequest().getItemDetails();
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
            if (veritransSDK.getTransactionRequest().getCardClickType().equalsIgnoreCase
                    (getString(R.string.card_click_type_one_click)) &&
                    !TextUtils.isEmpty(cardTokenRequest.getSavedTokenId())) {
                cardPaymentDetails = new CardPaymentDetails("",
                        cardTokenRequest.getSavedTokenId(), true);
                if (cardPaymentDetails.getBank().equals("")) {
                    cardPaymentDetails.setBank(null);
                }
                cardPaymentDetails.setRecurring(true);
            } else if (tokenDetailsResponse != null) {
                Logger.i("tokenDetailsResponse.getTokenId():" + tokenDetailsResponse.getTokenId());
                cardPaymentDetails = new CardPaymentDetails(cardTokenRequest.getBank(),
                        tokenDetailsResponse.getTokenId(), cardTokenRequest.isSaved());
            } else {
                SdkUIFlowUtil.hideProgressDialog();
                processingLayout.setVisibility(View.GONE);
                return;
            }
            cardTransfer = new CardTransfer(cardPaymentDetails, transactionDetails,
                    itemDetailsArrayList, billingAddresses, shippingAddresses, customerDetails);

        }

        veritransSDK.snapPaymentUsingCard(veritransSDK.readAuthenticationToken(), tokenDetailsResponse.getTokenId(), false);
    }

    public VeritransSDK getVeritransSDK() {
        return veritransSDK;
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
        /*try {
            storageDataHandler.writeObject(this, Constants.USERS_SAVED_CARD, creditCards);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        veritransSDK.saveCards(creditCard);
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
                PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                        PaymentTransactionStatusFragment.newInstance(transactionResponse);
                replaceFragment(paymentTransactionStatusFragment, R.id.card_container, true, false);
                if (getSupportActionBar() != null)
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                titleHeaderTextView.setText(getString(R.string.title_payment_status));
            }
        }
    }

    private void updateCreditCardData(String cardNumber, String cvv, String expired) {
        // Update credit card data in AddCardDetailsFragment
        VeritransBusProvider.getInstance().post(new UpdateCreditCardDataFromScanEvent(cardNumber, cvv, expired));
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
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.fetching_cards), true);
        //  processingLayout.setVisibility(View.VISIBLE);
        veritransSDK.getSavedCard();
    }

    public void oneClickPayment(CardTokenRequest cardDetail) {
        this.cardTokenRequest = cardDetail;
        payUsingCard();
    }

    public void twoClickPayment(CardTokenRequest cardDetail) {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        this.cardTokenRequest = new CardTokenRequest();
        this.cardTokenRequest.setSavedTokenId(cardDetail.getSavedTokenId());
        this.cardTokenRequest.setCardCVV(cardDetail.getCardCVV());
        this.cardTokenRequest.setTwoClick(true);
        this.cardTokenRequest.setSecure(veritransSDK.getTransactionRequest().isSecureCard());
        this.cardTokenRequest.setGrossAmount(veritransSDK.getTransactionRequest().getAmount());
        this.cardTokenRequest.setBank("");
        this.cardTokenRequest.setClientKey(veritransSDK.getClientKey());
        veritransSDK.getToken(cardTokenRequest);
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
        if(readBankDetailTask != null && readBankDetailTask.getStatus().equals(AsyncTask.Status.RUNNING)){
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
        if(userDetail != null){
            this.userDetail = userDetail;
        }
        if(this.bankDetails != null){
            this.bankDetails = bankDetails;
            Logger.i("bankdetail getter onnext" + bankDetails.size());
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

    @Subscribe
    @Override
    public void onEvent(SaveCardSuccessEvent event) {
        Logger.i("card saved");

    }

    @Subscribe
    @Override
    public void onEvent(SaveCardFailedEvent event) {

    }

    @Subscribe
    @Override
    public void onEvent(GetTokenSuccessEvent event) {
        TokenDetailsResponse tokenDetailsResponse = event.getResponse();
        if (tokenDetailsResponse != null) {
            cardTokenRequest.setBank(tokenDetailsResponse.getBank());
        }
        this.tokenDetailsResponse = tokenDetailsResponse;

        if (veritransSDK.getTransactionRequest().isSecureCard()) {
            SdkUIFlowUtil.hideProgressDialog();
            if (tokenDetailsResponse != null) {
                if (!TextUtils.isEmpty(tokenDetailsResponse.getRedirectUrl())) {
                    Intent intentPaymentWeb = new Intent(this, PaymentWebActivity.class);
                    intentPaymentWeb.putExtra(Constants.WEBURL, tokenDetailsResponse.getRedirectUrl());
                    intentPaymentWeb.putExtra(Constants.TYPE, WebviewFragment.TYPE_CREDIT_CARD);
                    startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
                }
            }
        } else {
            SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
            payUsingCard();
        }
    }

    @Subscribe
    @Override
    public void onEvent(GetTokenFailedEvent event) {
        SdkUIFlowUtil.hideProgressDialog();
        SdkUIFlowUtil.showApiFailedMessage(this, event.getMessage());
    }

    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent event) {
        TransactionResponse cardPaymentResponse = event.getResponse();
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
                cardPaymentResponse.getStatusCode().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {

            transactionResponse = cardPaymentResponse;

            PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                    PaymentTransactionStatusFragment.newInstance(cardPaymentResponse);
            replaceFragment(paymentTransactionStatusFragment, R.id.card_container, true, false);
            if (getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            //getSupportActionBar().setTitle(getString(R.string.title_payment_successful));
            titleHeaderTextView.setText(getString(R.string.title_payment_status));

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

                if (cardTokenRequest.isSaved()) {
                    if (!TextUtils.isEmpty(cardPaymentResponse.getSavedTokenId())) {
                        cardTokenRequest.setSavedTokenId(cardPaymentResponse.getSavedTokenId());
                    }
                }
                Logger.i("Card:" + cardTokenRequest.getString());

                SaveCardRequest saveCardRequest = new SaveCardRequest();
                saveCardRequest.setSavedTokenId(cardTokenRequest.getSavedTokenId());
                String firstPart = cardTokenRequest.getCardNumber().substring(0, 6);
                String secondPart = cardTokenRequest.getCardNumber().substring(12);
                saveCardRequest.setMaskedCard(firstPart + "-" + secondPart);
                saveCardRequest.setCode("200");
                saveCardRequest.setTransactionId(UUID.randomUUID().toString());
                saveCreditCards(saveCardRequest);
                creditCards.add(saveCardRequest);
            }
        }
    }

    @Subscribe
    @Override
    public void onEvent(TransactionFailedEvent event) {
        TransactionResponse transactionResponse = event.getResponse();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                processingLayout.setVisibility(View.GONE);
            }
        }, 200);
        CreditDebitCardFlowActivity.this.transactionResponse = transactionResponse;
        CreditDebitCardFlowActivity.this.errorMessage = event.getMessage();
        SdkUIFlowUtil.hideProgressDialog();
        PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                PaymentTransactionStatusFragment.newInstance(transactionResponse);
        replaceFragment(paymentTransactionStatusFragment, R.id.card_container, true, false);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setTitle(getString(R.string.title_payment_failed));
        titleHeaderTextView.setText(getString(R.string.title_payment_status));
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent event) {
        SdkUIFlowUtil.hideProgressDialog();
        SdkUIFlowUtil.showApiFailedMessage(this, getString(R.string.no_network_msg));
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent event) {
        SdkUIFlowUtil.hideProgressDialog();
        SdkUIFlowUtil.showApiFailedMessage(this, event.getMessage());

        if (event.getSource().equals(Events.GET_CARD)) {
            AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment.newInstance();
            replaceFragment(addCardDetailsFragment, R.id.card_container, true, false);
            titleHeaderTextView.setText(getString(R.string.card_details));
        }
    }

    @Subscribe
    @Override
    public void onEvent(GetCardsSuccessEvent event) {
        CardResponse cardResponse = event.getResponse();
        SdkUIFlowUtil.hideProgressDialog();
        //
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                processingLayout.setVisibility(View.GONE);
            }
        }, 200);
        Logger.i("cards api successful" + cardResponse);
        if (cardResponse != null && !cardResponse.getData().isEmpty()) {

            creditCards.clear();
            creditCards.addAll(cardResponse.getData());
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

    @Subscribe
    @Override
    public void onEvent(GetCardFailedEvent event) {
        SdkUIFlowUtil.hideProgressDialog();
        Logger.i("card fetching failed :" + event.getMessage());
        processingLayout.setVisibility(View.GONE);
    }


}
