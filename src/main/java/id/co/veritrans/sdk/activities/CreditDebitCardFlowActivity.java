package id.co.veritrans.sdk.activities;

import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.adapters.CardPagerAdapter;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.LocalDataHandler;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.StorageDataHandler;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.eventbus.callback.GetCardBusCallback;
import id.co.veritrans.sdk.eventbus.callback.SaveCardBusCallback;
import id.co.veritrans.sdk.eventbus.callback.TokenBusCallback;
import id.co.veritrans.sdk.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.eventbus.events.GetCardFailedEvent;
import id.co.veritrans.sdk.eventbus.events.GetCardsSuccessEvent;
import id.co.veritrans.sdk.eventbus.events.GetTokenFailedEvent;
import id.co.veritrans.sdk.eventbus.events.GetTokenSuccessEvent;
import id.co.veritrans.sdk.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.eventbus.events.SaveCardFailedEvent;
import id.co.veritrans.sdk.eventbus.events.SaveCardSuccessEvent;
import id.co.veritrans.sdk.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.fragments.AddCardDetailsFragment;
import id.co.veritrans.sdk.fragments.PaymentTransactionStatusFragment;
import id.co.veritrans.sdk.fragments.SavedCardFragment;
import id.co.veritrans.sdk.models.BankDetail;
import id.co.veritrans.sdk.models.BankDetailArray;
import id.co.veritrans.sdk.models.BillingAddress;
import id.co.veritrans.sdk.models.CardPaymentDetails;
import id.co.veritrans.sdk.models.CardResponse;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.models.CardTransfer;
import id.co.veritrans.sdk.models.CustomerDetails;
import id.co.veritrans.sdk.models.ShippingAddress;
import id.co.veritrans.sdk.models.TokenDetailsResponse;
import id.co.veritrans.sdk.models.TransactionDetails;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.models.UserAddress;
import id.co.veritrans.sdk.models.UserDetail;
import id.co.veritrans.sdk.utilities.Utils;
import id.co.veritrans.sdk.widgets.CirclePageIndicator;
import id.co.veritrans.sdk.widgets.MorphingButton;
import id.co.veritrans.sdk.widgets.TextViewFont;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreditDebitCardFlowActivity extends AppCompatActivity implements TransactionBusCallback, TokenBusCallback, SaveCardBusCallback, GetCardBusCallback {
    private static final int PAYMENT_WEB_INTENT = 100;
    private static final int PAY_USING_CARD = 51;
    private int RESULT_CODE = RESULT_CANCELED;
    private Toolbar toolbar;
    private String currentFragmentName;
    private FragmentManager fragmentManager;
    private VeritransSDK veritransSDK;
    private float cardWidth;
    private UserDetail userDetail;
    private TokenDetailsResponse tokenDetailsResponse;
    private CardTokenRequest cardTokenRequest;
    private CardTransfer cardTransfer;
    private StorageDataHandler storageDataHandler;
    private ArrayList<CardTokenRequest> creditCards = new ArrayList<>();
    private RelativeLayout processingLayout;
    private ArrayList<BankDetail> bankDetails;
    private Subscription subscription;

    //for setResult
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private CardPagerAdapter cardPagerAdapter;
    private CirclePageIndicator circlePageIndicator;
    private TextViewFont emptyCardsTextViewFont;
    private TextViewFont titleHeaderTextViewFont;
    private int fabHeight;
    private MorphingButton btnMorph;

    public MorphingButton getBtnMorph() {
        return btnMorph;
    }

    /*public void setBtnMorph(MorphingButton btnMorph) {
        this.btnMorph = btnMorph;
    }*/

    public TextViewFont getTitleHeaderTextViewFont() {
        return titleHeaderTextViewFont;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_debit_card_flow);
        storageDataHandler = new StorageDataHandler();
        processingLayout = (RelativeLayout) findViewById(R.id.processing_layout);
        veritransSDK = VeritransSDK.getVeritransSDK();
        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleHeaderTextViewFont = (TextViewFont) findViewById(R.id.title_header);
        btnMorph = (MorphingButton) findViewById(R.id.btnMorph1);
        morphToCircle(0);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        calculateScreenWidth();
        getCreditCards();
        readBankDetails();

        if (!VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if (VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().unregister(this);
        }
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
        SdkUtil.hideKeyboard(this);
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
        SdkUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        this.cardTokenRequest = cardTokenRequest;
        Logger.i("isSecure:" + this.cardTokenRequest.isSecure());
        veritransSDK.getToken(cardTokenRequest);
    }

    /*public void getToken() {
        if (cardTokenRequest != null) {
            this.cardTokenRequest.setGrossAmount(veritransSDK.getTransactionRequest().getAmount());
            SdkUtil.showProgressDialog(this, false);
            veritransSDK.getToken(CreditDebitCardFlowActivity.this, this.cardTokenRequest, this);
        } else {
            SdkUtil.showSnackbar(this, getString(R.string.card_details_error_message));
        }
    }*/

    public void payUsingCard() {

        SdkUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        processingLayout.setVisibility(View.VISIBLE);
        //getSupportActionBar().setTitle(getString(R.string.processing_payment));
        titleHeaderTextViewFont.setText(getString(R.string.processing_payment));
        CustomerDetails customerDetails = new CustomerDetails(userDetail.getUserFullName(), "",
                userDetail.getEmail(), userDetail.getPhoneNumber());

        ArrayList<UserAddress> userAddresses = userDetail.getUserAddresses();
        TransactionDetails transactionDetails = new TransactionDetails("" + veritransSDK.
                getTransactionRequest().getAmount(),
                veritransSDK.getTransactionRequest().getOrderId());
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
                cardPaymentDetails = new CardPaymentDetails(cardTokenRequest.getBank(),
                        cardTokenRequest.getSavedTokenId(), cardTokenRequest.isSaved());
            } else if (tokenDetailsResponse != null) {
                Logger.i("tokenDetailsResponse.getTokenId():" + tokenDetailsResponse.getTokenId());
                cardPaymentDetails = new CardPaymentDetails(cardTokenRequest.getBank(),
                        tokenDetailsResponse.getTokenId(), cardTokenRequest.isSaved());
            } else {
                SdkUtil.hideProgressDialog();
                processingLayout.setVisibility(View.GONE);
                return;
            }
            cardTransfer = new CardTransfer(cardPaymentDetails, transactionDetails,
                    null, billingAddresses, shippingAddresses, customerDetails);
            //currentApiCallNumber = PAY_USING_CARD;

        }

        veritransSDK.paymentUsingCard(cardTransfer);

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

    public void replaceFragment(Fragment fragment, boolean addToBackStack, boolean clearBackStack) {
        if (fragment != null) {
            Logger.i("replace freagment");
            boolean fragmentPopped = false;
            String backStateName = fragment.getClass().getName();

            if (clearBackStack) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                fragmentPopped = fragmentManager.popBackStackImmediate(backStateName, 0);
            }

            if (!fragmentPopped) { //fragment not in back stack, create it.
                Logger.i("fragment not in back stack, create it");
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.card_container, fragment, backStateName);
                if (addToBackStack) {
                    ft.addToBackStack(backStateName);
                }
                ft.commit();
                currentFragmentName = backStateName;
                //currentFragment = fragment;
            }
        }
    }

    public void saveCreditCards(CardTokenRequest creditCard) {
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
            }
        }
    }

    public ArrayList<CardTokenRequest> getCreditCards() {
        if (creditCards == null || creditCards.isEmpty()) {
            fetchCreditCards();
        }
        return creditCards;
    }

    public ArrayList<CardTokenRequest> getCreditCardList() {
             return creditCards;
    }

    public void fetchCreditCards() {
        SdkUtil.showProgressDialog(this, getString(R.string.fetching_cards), true);
      //  processingLayout.setVisibility(View.VISIBLE);
        veritransSDK.getSavedCard();
    }

    public void oneClickPayment(CardTokenRequest cardDetail) {
        this.cardTokenRequest = cardDetail;
        payUsingCard();
    }

    public void twoClickPayment(CardTokenRequest cardDetail) {
        SdkUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        this.cardTokenRequest = cardDetail;
        this.cardTokenRequest.setTwoClick(true);
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
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private void readBankDetails() {

        if (bankDetails == null || bankDetails.isEmpty()) {
            rx.Observable<List<BankDetail>> banksObservable = rx.Observable.create(
                    new rx.Observable.OnSubscribe<List<BankDetail>>() {
                        @Override
                        public void call(Subscriber<? super List<BankDetail>> sub) {
                            try {
                                userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
                                Logger.i("userDetail:"+userDetail.getUserFullName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            ArrayList<BankDetail> bankDetails = new ArrayList<>();
                            try {
                                bankDetails = LocalDataHandler.readObject(getString(R.string.bank_details), BankDetailArray.class).getBankDetails();
                                Logger.i("bankDetails:" + bankDetails.size());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (bankDetails.isEmpty()) {
                                String json = null;
                                try {
                                    InputStream is = CreditDebitCardFlowActivity.this.getAssets().open("bank_details.json");
                                    int size = is.available();
                                    byte[] buffer = new byte[size];
                                    is.read(buffer);
                                    is.close();
                                    json = new String(buffer, "UTF-8");
                                    Logger.i("json:" + json);
                                } catch (IOException ex) {
                                    ex.printStackTrace();

                                }

                                try {
                                    Gson gson = new Gson();
                                    bankDetails = gson.fromJson(json, BankDetailArray.class).getBankDetails();
                                    Logger.i("bankDetails:" + bankDetails.size());
                                    LocalDataHandler.saveObject(getString(R.string.bank_details), bankDetails);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            sub.onNext(bankDetails);
                            sub.onCompleted();
                        }
                    }
            );
            Subscriber<List<BankDetail>> subscriber = new Subscriber<List<BankDetail>>() {

                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    Logger.i("error:" + e.getMessage());
                }

                @Override
                public void onNext(List<BankDetail> bankDetails) {
                    CreditDebitCardFlowActivity.this.bankDetails = (ArrayList<BankDetail>) bankDetails;
                    Logger.i("bankdetail getter onnext" + bankDetails.size());
                }
            };
            subscription = banksObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);

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



    public void setAdapterViews(CardPagerAdapter cardPagerAdapter, CirclePageIndicator circlePageIndicator, TextViewFont emptyCardsTextViewFont) {
        this.cardPagerAdapter = cardPagerAdapter;
        this.circlePageIndicator = circlePageIndicator;
        this.emptyCardsTextViewFont = emptyCardsTextViewFont;
    }

    public int getFabHeight() {
        return fabHeight;
    }

    public void setFabHeight(int fabHeight) {
        Logger.i("fab_height:" + fabHeight);
        this.fabHeight = fabHeight;
    }
    public void morphingAnimation(){
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

    @Override
    public void onEvent(SaveCardSuccessEvent event) {
        Logger.i("card saved");

    }

    @Override
    public void onEvent(SaveCardFailedEvent event) {

    }

    @Override
    public void onEvent(GetTokenSuccessEvent event) {
        TokenDetailsResponse tokenDetailsResponse = event.getResponse();
        if (tokenDetailsResponse != null) {
            cardTokenRequest.setBank(tokenDetailsResponse.getBank());
        }
        this.tokenDetailsResponse = tokenDetailsResponse;
        Logger.i("token suc:" + tokenDetailsResponse.getTokenId() + ","
                + veritransSDK.getTransactionRequest().isSecureCard() + "," + tokenDetailsResponse.getBank());
        if (veritransSDK.getTransactionRequest().isSecureCard()) {
            SdkUtil.hideProgressDialog();
            if (!TextUtils.isEmpty(tokenDetailsResponse.getRedirectUrl())) {
                Intent intentPaymentWeb = new Intent(this, PaymentWebActivity.class);
                intentPaymentWeb.putExtra(Constants.WEBURL, tokenDetailsResponse.getRedirectUrl());
                startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
            }
        } else {
            SdkUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
            payUsingCard();
        }
    }

    @Override
    public void onEvent(GetTokenFailedEvent event) {
        SdkUtil.hideProgressDialog();
        SdkUtil.showApiFailedMessage(this, event.getMessage());
    }

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

        SdkUtil.hideProgressDialog();
        Logger.i("cardPaymentResponse:" + cardPaymentResponse.getStatusCode());

        if (cardPaymentResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200)) ||
                cardPaymentResponse.getStatusCode().equalsIgnoreCase(VeritransSDK.getVeritransSDK().getContext().getString(R.string.success_code_201))) {

            transactionResponse = cardPaymentResponse;

            PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                    PaymentTransactionStatusFragment.newInstance(cardPaymentResponse);
            replaceFragment(paymentTransactionStatusFragment, true, false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            //getSupportActionBar().setTitle(getString(R.string.title_payment_successful));
            titleHeaderTextViewFont.setText(getString(R.string.title_payment_status));

            if (cardTokenRequest.isSaved()) {
                if (!creditCards.isEmpty()) {
                    int position = -1;
                    for (int i = 0; i < creditCards.size(); i++) {
                        CardTokenRequest card = creditCards.get(i);
                        if (card.getCardNumber().equalsIgnoreCase(cardTokenRequest.getCardNumber
                                ())) {
                            position = i;
                            break;
                        }
                    }
                    if (position >= 0) {
                        creditCards.remove(position);
                    }
                }
                cardTokenRequest.setCardCVV(0);
                cardTokenRequest.setClientKey("");
                cardTokenRequest.setGrossAmount(0);

                if (cardTokenRequest.isSaved() && !TextUtils.isEmpty(cardPaymentResponse
                        .getSavedTokenId())) {
                    cardTokenRequest.setSavedTokenId(cardPaymentResponse.getSavedTokenId());
                }
                Logger.i("Card:" + cardTokenRequest.getString());
                creditCards.add(cardTokenRequest);
                saveCreditCards(cardTokenRequest);
            }
        }
    }

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
        SdkUtil.hideProgressDialog();
        PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                PaymentTransactionStatusFragment.newInstance(transactionResponse);
        replaceFragment(paymentTransactionStatusFragment, true, false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        //getSupportActionBar().setTitle(getString(R.string.title_payment_failed));
        titleHeaderTextViewFont.setText(getString(R.string.title_payment_status));
    }

    @Override
    public void onEvent(NetworkUnavailableEvent event) {
        SdkUtil.hideProgressDialog();
        SdkUtil.showApiFailedMessage(this, getString(R.string.no_network_msg));
    }

    @Override
    public void onEvent(GeneralErrorEvent event) {
        SdkUtil.hideProgressDialog();
        SdkUtil.showApiFailedMessage(this, event.getMessage());
    }

    @Override
    public void onEvent(GetCardsSuccessEvent event) {
        CardResponse cardResponse = event.getResponse();
        SdkUtil.hideProgressDialog();
        //
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                processingLayout.setVisibility(View.GONE);
            }
        }, 200);
        Logger.i("cards api successful" + cardResponse);
        if (cardResponse != null && !cardResponse.getCreditCards().isEmpty()) {

            creditCards.clear();
            creditCards.addAll(cardResponse.getCreditCards());
            if (cardPagerAdapter != null && circlePageIndicator != null) {
                cardPagerAdapter.notifyDataSetChanged();
                circlePageIndicator.notifyDataSetChanged();
            }
            //processingLayout.setVisibility(View.GONE);
            if (emptyCardsTextViewFont != null) {
                if (!creditCards.isEmpty()) {
                    emptyCardsTextViewFont.setVisibility(View.GONE);
                } else {
                    emptyCardsTextViewFont.setVisibility(View.VISIBLE);
                }

            }
            SavedCardFragment savedCardFragment = SavedCardFragment.newInstance();
            //getSupportActionBar().setTitle(getString(R.string.saved_card));
            titleHeaderTextViewFont.setText(getString(R.string.saved_card));
            replaceFragment(savedCardFragment, true, false);

        } else {
            AddCardDetailsFragment addCardDetailsFragment = AddCardDetailsFragment
                    .newInstance();
            replaceFragment
                    (addCardDetailsFragment, true, false);
            //getSupportActionBar().setTitle(getString(R.string.card_details));
            titleHeaderTextViewFont.setText(getString(R.string.card_details));

        }
    }

    @Override
    public void onEvent(GetCardFailedEvent event) {
        SdkUtil.hideProgressDialog();
        Logger.i("card fetching failed :" + event.getMessage());
        processingLayout.setVisibility(View.GONE);
    }
}
