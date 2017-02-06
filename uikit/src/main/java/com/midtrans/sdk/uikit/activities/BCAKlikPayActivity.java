package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.BCAKlikPayInstructionFragment;
import com.midtrans.sdk.uikit.fragments.WebviewFragment;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

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
    private MidtransSDK mMidtransSDK = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;

    private String currentFragmentName = "";
    private TransactionResponse transactionResponseFromMerchant;
    private DefaultTextView textTitle, textTotalAmount;
    private FancyButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bca_klikpay);
        mMidtransSDK = MidtransSDK.getInstance();

        if (mMidtransSDK == null) {
            SdkUIFlowUtil.showToast(BCAKlikPayActivity.this, Constants
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
        textTitle = (DefaultTextView) findViewById(R.id.text_title);
        textTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);
        buttonBack = (FancyButton) findViewById(R.id.btn_back);
        initializeTheme();
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        buttonConfirmPayment.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        bindData();
    }

    private void bindData() {
        textTitle.setText(getString(R.string.bca_klik));
        if (mMidtransSDK != null) {
            if (mMidtransSDK.getSemiBoldText() != null) {
                buttonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mMidtransSDK.getSemiBoldText()));
            }
            textTotalAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(mMidtransSDK.getTransactionRequest().getAmount())));
        }
    }

    private void setUpFragment() {

        // setup  fragment
        bcaKlikPayInstructionFragment = new BCAKlikPayInstructionFragment();
        replaceFragment(bcaKlikPayInstructionFragment, R.id.instruction_container, false, false);

        //track page BCA Klik Pay
        mMidtransSDK.trackEvent(AnalyticsEventName.PAGE_BCA_KLIKPAY);
    }


    @Override
    public void onBackPressed() {
        if (currentFragmentName.equals(STATUS_FRAGMENT)) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
            return;
        }
        super.onBackPressed();
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
        } else if (view.getId() == R.id.btn_back) {
            onBackPressed();
        }
    }

    private void makeTransaction() {
        //track page BCA Klik pay
        mMidtransSDK.trackEvent(AnalyticsEventName.BTN_CONFIRM_PAYMENT);

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

                    if (response != null && response.getStatusCode().equals(getString(R.string.failed_code_400))) {
                        transactionResponseFromMerchant = response;
                        setResultCode(RESULT_OK);
                        setResultAndFinish();
                    } else {
                        SdkUIFlowUtil.showToast(BCAKlikPayActivity.this, "" + errorMessage);
                    }
                } catch (NullPointerException ex) {
                    SdkUIFlowUtil.showApiFailedMessage(BCAKlikPayActivity.this, getString(R.string.empty_transaction_response));
                }
            }

            @Override
            public void onError(Throwable error) {
                BCAKlikPayActivity.this.errorMessage = getString(R.string.message_payment_failed);
                SdkUIFlowUtil.hideProgressDialog();
                SdkUIFlowUtil.showToast(BCAKlikPayActivity.this, "" + errorMessage);
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
