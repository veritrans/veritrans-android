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
import com.midtrans.sdk.uikit.fragments.BCAKlikPayInstructionFragment;
import com.midtrans.sdk.uikit.fragments.PaymentTransactionStatusFragment;
import com.midtrans.sdk.uikit.fragments.WebviewFragment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author rakawm
 */
public class BCAKlikPayActivity extends BaseActivity implements View.OnClickListener {

    private static final int PAYMENT_WEB_INTENT = 152;
    private static final java.lang.String TAG = "BCAKlikPayActivity";
    private static final String STATUS_FRAGMENT = "status";
    private BCAKlikPayInstructionFragment bcaKlikPayInstructionFragment = null;
    private Button buttonConfirmPayment = null;
    private Toolbar mToolbar = null;
    private ImageView logo = null;
    private MidtransSDK mMidtransSDK = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private int RESULT_CODE = RESULT_CANCELED;

    private FragmentManager fragmentManager;
    private String currentFragmentName = "";
    private TransactionResponse transactionResponseFromMerchant;
    private DefaultTextView textTitle, textOrderId, textTotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_bca_klikpay);
        mMidtransSDK = MidtransSDK.getInstance();

        if (mMidtransSDK == null) {
            SdkUIFlowUtil.showSnackbar(BCAKlikPayActivity.this, Constants
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
        buttonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        textTitle = (DefaultTextView) findViewById(R.id.text_title);
        textOrderId = (DefaultTextView) findViewById(R.id.text_order_id);
        textTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);

        initializeTheme();
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buttonConfirmPayment.setOnClickListener(this);
        bindData();
    }

    private void bindData() {
        textTitle.setText(getString(R.string.bca_klik));
        if (mMidtransSDK != null) {
            if (mMidtransSDK.getSemiBoldText() != null) {
                buttonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mMidtransSDK.getSemiBoldText()));
            }
            textOrderId.setText(mMidtransSDK.getTransactionRequest().getOrderId());
            textTotalAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(mMidtransSDK.getTransactionRequest().getAmount())));
        }
    }

    private void setUpFragment() {

        // setup  fragment
        bcaKlikPayInstructionFragment = new BCAKlikPayInstructionFragment();
        replaceFragment(bcaKlikPayInstructionFragment, R.id.instruction_container, true, false);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
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

    private void makeTransaction() {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        mMidtransSDK.paymentUsingBCAKlikpay(mMidtransSDK.readAuthenticationToken(), new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                SdkUIFlowUtil.hideProgressDialog();

                if (response != null &&
                        !TextUtils.isEmpty(response.getRedirectUrl())) {
                    BCAKlikPayActivity.this.transactionResponse = response;
                    Intent intentPaymentWeb = new Intent(BCAKlikPayActivity.this, PaymentWebActivity.class);
                    intentPaymentWeb.putExtra(Constants.WEBURL, response.getRedirectUrl());
                    intentPaymentWeb.putExtra(Constants.TYPE, WebviewFragment.TYPE_BCA_KLIKPAY);
                    startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
                } else {
                    SdkUIFlowUtil.showApiFailedMessage(BCAKlikPayActivity.this, getString(R.string
                            .empty_transaction_response));
                }
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                try {
                    BCAKlikPayActivity.this.errorMessage = getString(R.string.message_payment_failed);
                    BCAKlikPayActivity.this.transactionResponse = response;

                    SdkUIFlowUtil.hideProgressDialog();
                    SdkUIFlowUtil.showSnackbar(BCAKlikPayActivity.this, "" + errorMessage);
                } catch (NullPointerException ex) {
                    SdkUIFlowUtil.showApiFailedMessage(BCAKlikPayActivity.this, getString(R.string.empty_transaction_response));
                }
            }

            @Override
            public void onError(Throwable error) {
                BCAKlikPayActivity.this.errorMessage = error.getMessage();
                SdkUIFlowUtil.hideProgressDialog();
                SdkUIFlowUtil.showSnackbar(BCAKlikPayActivity.this, "" + errorMessage);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i(TAG, "reqCode:" + requestCode + ",res:" + resultCode);
        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        if (resultCode == RESULT_OK) {
            currentFragmentName = STATUS_FRAGMENT;
            mToolbar.setNavigationIcon(closeIcon);
            setSupportActionBar(mToolbar);
            transactionResponseFromMerchant = new TransactionResponse("200", "Transaction Success", UUID.randomUUID().toString(),
                    mMidtransSDK.getTransactionRequest().getOrderId(), String.valueOf(mMidtransSDK.getTransactionRequest().getAmount()), getString(R.string.payment_bca_click), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), getString(R.string.settlement));
            PaymentTransactionStatusFragment paymentTransactionStatusFragment =
                    PaymentTransactionStatusFragment.newInstance(transactionResponseFromMerchant);
            replaceFragment(paymentTransactionStatusFragment, R.id.instruction_container, true, false);
            buttonConfirmPayment.setVisibility(View.GONE);
        } else if (resultCode == RESULT_CANCELED) {
            RESULT_CODE = RESULT_OK;
            transactionResponseFromMerchant = transactionResponse;
            setResultAndFinish();
        }
    }


    public void setResultAndFinish() {
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

}
