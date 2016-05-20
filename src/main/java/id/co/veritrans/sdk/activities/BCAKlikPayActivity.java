package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.fragments.BCAKlikPayInstructionFragment;
import id.co.veritrans.sdk.fragments.PaymentTransactionStatusFragment;
import id.co.veritrans.sdk.models.BCAKlikPayDescriptionModel;
import id.co.veritrans.sdk.models.TransactionResponse;

/**
 * @author rakawm
 */
public class BCAKlikPayActivity extends BaseActivity implements View.OnClickListener, TransactionBusCallback {

    private static final int PAYMENT_WEB_INTENT = 152;
    private BCAKlikPayInstructionFragment bcaKlikPayInstructionFragment = null;
    private Button buttonConfirmPayment = null;
    private Toolbar mToolbar = null;
    private VeritransSDK mVeritransSDK = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private int RESULT_CODE = RESULT_CANCELED;

    private FragmentManager fragmentManager;
    private String currentFragmentName = "";
    private TransactionResponse transactionResponseFromMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_bca_klikpay);
        mVeritransSDK = VeritransSDK.getVeritransSDK();

        if (mVeritransSDK == null) {
            SdkUtil.showSnackbar(BCAKlikPayActivity.this, Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
        initializeViews();
        initializeTheme();
        setUpFragment();
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

    private void initializeViews() {
        buttonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonConfirmPayment.setOnClickListener(this);
        if (mVeritransSDK != null && mVeritransSDK.getSemiBoldText() != null) {
            buttonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mVeritransSDK.getSemiBoldText()));
        }
    }

    private void setUpFragment() {

        // setup  fragment
        bcaKlikPayInstructionFragment = new BCAKlikPayInstructionFragment();
        replaceFragment(bcaKlikPayInstructionFragment, true, false);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_confirm_payment) {
            makeTransaction();
        }
    }

    private void makeTransaction(){
        SdkUtil.showProgressDialog(this, getString(R.string.processing_payment), false);

        BCAKlikPayDescriptionModel descriptionModel = new BCAKlikPayDescriptionModel("Any description");
        mVeritransSDK.paymentUsingBCAKlikPay(descriptionModel);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("reqCode:" + requestCode + ",res:" + resultCode);
        if (resultCode == RESULT_OK && data != null ) {
            String responseStr = data.getStringExtra(getString(R.string.payment_response));
            if(TextUtils.isEmpty(responseStr)){
                return;
            }
            Gson gson = new Gson();
            transactionResponseFromMerchant = gson.fromJson(responseStr, TransactionResponse.class);
            PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                    PaymentTransactionStatusFragment.newInstance(transactionResponseFromMerchant);
            replaceFragment(paymentTransactionStatusFragment, true, false);
            buttonConfirmPayment.setVisibility(View.GONE);
        }
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
                ft.replace(R.id.bca_klik_pay_container, fragment, backStateName);
                if (addToBackStack) {
                    ft.addToBackStack(backStateName);
                }
                ft.commit();
                currentFragmentName = backStateName;
            }
        }
    }

    public void setResultAndFinish(){
        Intent data = new Intent();
        if (transactionResponseFromMerchant != null) {
            data.putExtra(getString(R.string.transaction_response), transactionResponseFromMerchant);
        }
        data.putExtra(getString(R.string.error_transaction), errorMessage);
        setResult(RESULT_CODE, data);
        finish();
    }

    public void setResultCode(int resultCode) {
        this.RESULT_CODE = resultCode;
    }

    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent event) {
        SdkUtil.hideProgressDialog();

        if (event.getResponse() != null &&
                !TextUtils.isEmpty(event.getResponse().getRedirectUrl())) {
            BCAKlikPayActivity.this.transactionResponse = event.getResponse();
            Intent intentPaymentWeb = new Intent(BCAKlikPayActivity.this, PaymentWebActivity.class);
            intentPaymentWeb.putExtra(Constants.WEBURL, event.getResponse().getRedirectUrl());
            startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
        } else {
            SdkUtil.showApiFailedMessage(BCAKlikPayActivity.this, getString(R.string
                    .empty_transaction_response));
        }
    }

    @Subscribe
    @Override
    public void onEvent(TransactionFailedEvent event) {
        try {
            BCAKlikPayActivity.this.errorMessage = event.getMessage();
            BCAKlikPayActivity.this.transactionResponse = event.getResponse();

            SdkUtil.hideProgressDialog();
            SdkUtil.showSnackbar(BCAKlikPayActivity.this, "" + errorMessage);
        } catch (NullPointerException ex) {
            SdkUtil.showApiFailedMessage(BCAKlikPayActivity.this, getString(R.string.empty_transaction_response));
        }
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent event) {
        BCAKlikPayActivity.this.errorMessage = getString(R.string.no_network_msg);
        SdkUtil.hideProgressDialog();
        SdkUtil.showSnackbar(BCAKlikPayActivity.this, "" + errorMessage);
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent event) {
        BCAKlikPayActivity.this.errorMessage = event.getMessage();
        SdkUtil.hideProgressDialog();
        SdkUtil.showSnackbar(BCAKlikPayActivity.this, "" + errorMessage);
    }
}
