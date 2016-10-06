package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.InstructionEpayBriFragment;
import com.midtrans.sdk.uikit.fragments.WebviewFragment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

public class EpayBriActivity extends BaseActivity implements View.OnClickListener {

    private static final int PAYMENT_WEB_INTENT = 150;
    private static final String STATUS_FRAGMENT = "status";
    private static final String HOME_FRAGMENT = "home";
    private static int RESULT_CODE = RESULT_CANCELED;
    private Button btConfirmPayment = null;
    private Toolbar toolbar = null;
    private MidtransSDK midtransSDK = null;
    private ImageView logo = null;
    private InstructionEpayBriFragment instructionEpayBriFragment;
    private TransactionResponse transactionResponse;
    private FragmentManager fragmentManager;
    private String currentFragmentName = HOME_FRAGMENT;
    private String errorMessage;
    private TransactionResponse transactionResponseFromMerchant;
    private DefaultTextView textTitle, textOrderId, textTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_epay_bri);
        midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK == null) {
            SdkUIFlowUtil.showSnackbar(EpayBriActivity.this, Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }

        initializeViews();
        setUpFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initializeViews() {
        btConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        textTitle = (DefaultTextView) findViewById(R.id.text_title);
        textOrderId = (DefaultTextView) findViewById(R.id.text_order_id);
        textTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);

        initializeTheme();
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btConfirmPayment.setVisibility(View.VISIBLE);
        btConfirmPayment.setOnClickListener(this);
        bindData();
    }

    private void bindData() {
        textTitle.setText(getString(R.string.epay_bri));
        if (midtransSDK != null) {
            if (midtransSDK.getSemiBoldText() != null) {
                btConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), midtransSDK.getSemiBoldText()));
            }
            textOrderId.setText(midtransSDK.getTransactionRequest().getOrderId());
            textTotalAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(midtransSDK.getTransactionRequest().getAmount())));
        }
    }

    private void setUpFragment() {

        // setup  fragment
        instructionEpayBriFragment = new InstructionEpayBriFragment();
        replaceFragment(instructionEpayBriFragment, R.id.instruction_container, false, false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (currentFragmentName.equals(
                    STATUS_FRAGMENT)) {
                setResultCode(RESULT_OK);
                setResultAndFinish();
            } else {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {
            makeTransaction();
        }
    }

    private void makeTransaction() {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        midtransSDK.paymentUsingEpayBRI(midtransSDK.readAuthenticationToken(), new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                SdkUIFlowUtil.hideProgressDialog();
                if (response != null &&
                        !TextUtils.isEmpty(response.getRedirectUrl())) {
                    transactionResponse = response;
                    Intent intentPaymentWeb = new Intent(EpayBriActivity.this, PaymentWebActivity.class);
                    intentPaymentWeb.putExtra(Constants.WEBURL, response.getRedirectUrl());
                    intentPaymentWeb.putExtra(Constants.TYPE, WebviewFragment.TYPE_EPAY_BRI);
                    startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
                } else {
                    SdkUIFlowUtil.showApiFailedMessage(EpayBriActivity.this, getString(R.string.empty_transaction_response));
                }
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                SdkUIFlowUtil.hideProgressDialog();
                EpayBriActivity.this.errorMessage = getString(R.string.message_payment_failed);
                SdkUIFlowUtil.showApiFailedMessage(EpayBriActivity.this, errorMessage);
            }

            @Override
            public void onError(Throwable error) {
                SdkUIFlowUtil.hideProgressDialog();
                EpayBriActivity.this.errorMessage = error.getMessage();
                SdkUIFlowUtil.showApiFailedMessage(EpayBriActivity.this, error.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("reqCode:" + requestCode + ",res:" + resultCode);
        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        if (resultCode == RESULT_OK) {
            currentFragmentName = STATUS_FRAGMENT;
            transactionResponseFromMerchant = transactionResponse;
            RESULT_CODE = RESULT_OK;
            setResultAndFinish();
        } else if (resultCode == RESULT_CANCELED) {
            currentFragmentName = STATUS_FRAGMENT;
            RESULT_CODE = RESULT_OK;
            transactionResponseFromMerchant = transactionResponse;
            setResultAndFinish();
        }
    }

    public void setResultAndFinish() {
        Intent data = new Intent();
        if (transactionResponseFromMerchant != null) {
            Logger.i("transactionResponseFromMerchant:" + transactionResponseFromMerchant.getString());
            data.putExtra(getString(R.string.transaction_response), transactionResponseFromMerchant);
        }
        data.putExtra(getString(R.string.error_transaction), errorMessage);
        setResult(RESULT_CODE, data);
        finish();
    }

    public void setResultCode(int resultCode) {
        RESULT_CODE = resultCode;
    }

}