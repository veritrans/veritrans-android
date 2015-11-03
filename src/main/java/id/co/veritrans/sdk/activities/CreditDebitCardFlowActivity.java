package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;

import java.io.IOException;
import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.callbacks.TokenCallBack;
import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.StorageDataHandler;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.fragments.PaymentTransactionStatusFragment;
import id.co.veritrans.sdk.fragments.SavedCardFragment;
import id.co.veritrans.sdk.fragments.WebviewFragment;
import id.co.veritrans.sdk.models.BillingAddress;
import id.co.veritrans.sdk.models.CardPaymentDetails;
import id.co.veritrans.sdk.models.CardTransfer;
import id.co.veritrans.sdk.models.CustomerDetails;
import id.co.veritrans.sdk.models.ShippingAddress;
import id.co.veritrans.sdk.models.TokenDetailsResponse;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.models.TransactionDetails;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.models.UserAddress;
import id.co.veritrans.sdk.models.UserDetail;

public class CreditDebitCardFlowActivity extends AppCompatActivity implements TokenCallBack,TransactionCallback {
    private static final int PAYMENT_WEB_INTENT = 100;
    private static final int GET_TOKEN = 50;
    private static final int PAY_USING_CARD = 51;
    private Toolbar toolbar;
    private String currentFragmentName;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;
    private VeritransSDK veritransSDK;
    private float cardWidth;
    private UserDetail userDetail;
    private int currentApiCallNumber;
    private TokenDetailsResponse tokenDetailsResponse;
    private CardTokenRequest cardTokenRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StorageDataHandler storageDataHandler = new StorageDataHandler();
        try {
            userDetail = (UserDetail) storageDataHandler.readObject(this, Constants.USER_DETAILS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_credit_debit_card_flow);
        veritransSDK = VeritransSDK.getVeritransSDK();
        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        calculateScreenWidth();
        SavedCardFragment savedCardFragment = SavedCardFragment.newInstance();
        replaceFragment(savedCardFragment, true, false);
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
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else {
            if (currentFragmentName.equalsIgnoreCase(WebviewFragment.class.getName())) {
                if (((WebviewFragment) currentFragment).webView.canGoBack()) {
                    ((WebviewFragment) currentFragment).webviewBackPressed();
                } else {
                    super.onBackPressed();
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    public void getToken(CardTokenRequest cardTokenRequest) {
        this.cardTokenRequest = cardTokenRequest;
        veritransSDK.getToken(CreditDebitCardFlowActivity.this, cardTokenRequest, this);
    }

    public void payUsingCard(CardTransfer cardTransfer, TransactionCallback cardPaymentTransactionCallback) {
        veritransSDK.paymentUsingCard(this, cardTransfer, cardPaymentTransactionCallback);
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
                currentFragment = fragment;
            }
        }
    }

    //onSuccess for get token
    @Override
    public void onSuccess(TokenDetailsResponse tokenDetailsResponse) {
        this.tokenDetailsResponse = tokenDetailsResponse;
        Logger.i("token suc:" + tokenDetailsResponse.getTokenId());
        //AddCardDetailsFragment.this.tokenDetailsResponse = tokenDetailsResponse;
        /*if (isDetached()) {
            return;
        }*/
        SdkUtil.hideProgressDialog();
        if (!TextUtils.isEmpty(tokenDetailsResponse.getRedirectUrl())) {

            Intent intentPaymentWeb = new Intent(this, PaymentWebActivity.class);
            intentPaymentWeb.putExtra(Constants.WEBURL, tokenDetailsResponse.getRedirectUrl());
            startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);

        }

    }
    //onfailure for both token and transaction api call
    @Override
    public void onFailure(String errorMessage) {
        Logger.i("token fail" + errorMessage);
        SdkUtil.hideProgressDialog();
        switch (currentApiCallNumber){
            case GET_TOKEN:
                SdkUtil.showApiFailedMessage(this, errorMessage);
                break;
            case PAY_USING_CARD:
                PaymentTransactionStatusFragment paymentTransactionStatusFragment = PaymentTransactionStatusFragment.newInstance(null);
                replaceFragment(paymentTransactionStatusFragment, true, false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setTitle(getString(R.string.title_payment_failed));
                break;
        }
    }

    //onSuccess for transaction api call
    @Override
    public void onSuccess(TransactionResponse cardPaymentResponse) {
        SdkUtil.hideProgressDialog();
        Logger.i("cardPaymentResponse:" + cardPaymentResponse.getStatusCode());
        if (cardPaymentResponse.getStatusCode().equalsIgnoreCase(Constants.SUCCESS_CODE_200 )||
                cardPaymentResponse.getStatusCode().equalsIgnoreCase(Constants.SUCCESS_CODE_201)) {
            PaymentTransactionStatusFragment paymentTransactionStatusFragment = PaymentTransactionStatusFragment.newInstance(cardPaymentResponse);
            replaceFragment(paymentTransactionStatusFragment, true, false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(getString(R.string.title_payment_successful));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("reqCode:" + requestCode + ",res:" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == PAYMENT_WEB_INTENT) {
                SdkUtil.showProgressDialog(this, false);
                if (tokenDetailsResponse != null) {
                    CustomerDetails customerDetails = new CustomerDetails(userDetail.getUserFullName(), "",
                            userDetail.getEmail(), userDetail.getPhoneNumber());

                    ArrayList<UserAddress> userAddresses = userDetail.getUserAddresses();
                    TransactionDetails transactionDetails = new TransactionDetails("" + veritransSDK.getAmount(), veritransSDK.getOrderId());
                    if (userAddresses != null && !userAddresses.isEmpty()) {
                        ArrayList<BillingAddress> billingAddresses = new ArrayList<>();
                        ArrayList<ShippingAddress> shippingAddresses = new ArrayList<>();

                        for (int i = 0; i < userAddresses.size(); i++) {
                            UserAddress userAddress = userAddresses.get(i);

                            if (userAddress.getAddressType() == Constants.ADDRESS_TYPE_BILLING) {

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

                        }

                        CardPaymentDetails cardPaymentDetails = new CardPaymentDetails(Constants.BANK_NAME,
                                tokenDetailsResponse.getTokenId(), cardTokenRequest.isSaved());
                        try {
                            CardTransfer cardTransfer = new CardTransfer(cardPaymentDetails, transactionDetails, null, billingAddresses, null, customerDetails);
                            payUsingCard(cardTransfer, this);
                            currentApiCallNumber = PAY_USING_CARD;
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}