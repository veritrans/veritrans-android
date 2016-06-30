package id.co.veritrans.sdk.uiflow.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.BCAKlikPayDescriptionModel;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.fragments.BCAKlikPayInstructionFragment;
import id.co.veritrans.sdk.uiflow.fragments.PaymentTransactionStatusFragment;
import id.co.veritrans.sdk.uiflow.fragments.WebviewFragment;
import id.co.veritrans.sdk.uiflow.utilities.SdkUIFlowUtil;

/**
 * @author rakawm
 */
public class BCAKlikPayActivity extends BaseActivity implements View.OnClickListener, TransactionBusCallback {

    private static final int PAYMENT_WEB_INTENT = 152;
    private BCAKlikPayInstructionFragment bcaKlikPayInstructionFragment = null;
    private Button buttonConfirmPayment = null;
    private Toolbar mToolbar = null;
    private ImageView logo = null;
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
            SdkUIFlowUtil.showSnackbar(BCAKlikPayActivity.this, Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
        initializeViews();
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
        logo = (ImageView) findViewById(R.id.merchant_logo);
        initializeTheme();
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonConfirmPayment.setOnClickListener(this);
        if (mVeritransSDK != null) {
            if (mVeritransSDK.getSemiBoldText() != null) {
                buttonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mVeritransSDK.getSemiBoldText()));
            }
        }
    }

    private void setUpFragment() {

        // setup  fragment
        bcaKlikPayInstructionFragment = new BCAKlikPayInstructionFragment();
        replaceFragment(bcaKlikPayInstructionFragment, R.id.bca_klik_pay_container, true, false);
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
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);

        BCAKlikPayDescriptionModel descriptionModel = new BCAKlikPayDescriptionModel("Any description");
        mVeritransSDK.paymentUsingBCAKlikPay(descriptionModel);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("reqCode:" + requestCode + ",res:" + resultCode);
        if (resultCode == RESULT_OK) {
            transactionResponseFromMerchant = new TransactionResponse("200", "Transaction Success", UUID.randomUUID().toString(),
                    mVeritransSDK.getTransactionRequest().getOrderId(), String.valueOf(mVeritransSDK.getTransactionRequest().getAmount()), getString(R.string.payment_bca_click), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), getString(R.string.settlement));
            PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                    PaymentTransactionStatusFragment.newInstance(transactionResponseFromMerchant);
            replaceFragment(paymentTransactionStatusFragment, R.id.bca_klik_pay_container, true, false);
            buttonConfirmPayment.setVisibility(View.GONE);
        } else if (resultCode == RESULT_CANCELED) {
            PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                    PaymentTransactionStatusFragment.newInstance(transactionResponseFromMerchant);
            replaceFragment(paymentTransactionStatusFragment, R.id.bca_klik_pay_container, true, false);
            buttonConfirmPayment.setVisibility(View.GONE);
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
        SdkUIFlowUtil.hideProgressDialog();

        if (event.getResponse() != null &&
                !TextUtils.isEmpty(event.getResponse().getRedirectUrl())) {
            BCAKlikPayActivity.this.transactionResponse = event.getResponse();
            Intent intentPaymentWeb = new Intent(BCAKlikPayActivity.this, PaymentWebActivity.class);
            intentPaymentWeb.putExtra(Constants.WEBURL, event.getResponse().getRedirectUrl());
            intentPaymentWeb.putExtra(Constants.TYPE, WebviewFragment.TYPE_BCA_KLIKPAY);
            startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
        } else {
            SdkUIFlowUtil.showApiFailedMessage(BCAKlikPayActivity.this, getString(R.string
                    .empty_transaction_response));
        }
    }

    @Subscribe
    @Override
    public void onEvent(TransactionFailedEvent event) {
        try {
            BCAKlikPayActivity.this.errorMessage = event.getMessage();
            BCAKlikPayActivity.this.transactionResponse = event.getResponse();

            SdkUIFlowUtil.hideProgressDialog();
            SdkUIFlowUtil.showSnackbar(BCAKlikPayActivity.this, "" + errorMessage);
        } catch (NullPointerException ex) {
            SdkUIFlowUtil.showApiFailedMessage(BCAKlikPayActivity.this, getString(R.string.empty_transaction_response));
        }
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent event) {
        BCAKlikPayActivity.this.errorMessage = getString(R.string.no_network_msg);
        SdkUIFlowUtil.hideProgressDialog();
        SdkUIFlowUtil.showSnackbar(BCAKlikPayActivity.this, "" + errorMessage);
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent event) {
        BCAKlikPayActivity.this.errorMessage = event.getMessage();
        SdkUIFlowUtil.hideProgressDialog();
        SdkUIFlowUtil.showSnackbar(BCAKlikPayActivity.this, "" + errorMessage);
    }
}
