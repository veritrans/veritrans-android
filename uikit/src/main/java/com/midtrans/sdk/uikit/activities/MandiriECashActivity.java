package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.fragments.InstructionMandiriECashFragment;
import com.midtrans.sdk.uikit.fragments.WebviewFragment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Ankit on 11/30/15.
 */
public class MandiriECashActivity extends BaseActivity implements View.OnClickListener {

    private static final int PAYMENT_WEB_INTENT = 152;
    private static final String STATUS_FRAGMENT = "status";
    private static final String HOME_FRAGMENT = "home";
    private static final String TAG = "MandiriECashActivity";
    private InstructionMandiriECashFragment mandiriECashFragment = null;
    private FancyButton buttonConfirmPayment = null;
    private Toolbar mToolbar = null;
    private DefaultTextView textTitle, textTotalAmount;

    private MidtransSDK mMidtransSDK = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;

    private String currentFragmentName = HOME_FRAGMENT;
    private TransactionResponse transactionResponseFromMerchant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandiri_e_cash);
        mMidtransSDK = MidtransSDK.getInstance();

        if (mMidtransSDK == null) {
            SdkUIFlowUtil.showToast(MandiriECashActivity.this, Constants
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
        buttonConfirmPayment = (FancyButton) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        textTitle = (DefaultTextView) findViewById(R.id.text_title);
        textTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);

        initializeTheme();
        if (mMidtransSDK != null) {
            if (mMidtransSDK.getSemiBoldText() != null) {
                buttonConfirmPayment.setCustomTextFont(mMidtransSDK.getSemiBoldText());
            }
            textTotalAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(mMidtransSDK.getTransactionRequest().getAmount())));
        }
        textTitle.setText(getString(R.string.mandiri_e_cash));
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        prepareToolbar();
        buttonConfirmPayment.setOnClickListener(this);
    }

    private void prepareToolbar() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_back);

        try {
            MidtransSDK midtransSDK = MidtransSDK.getInstance();
            if (midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                drawable.setColorFilter(
                        midtransSDK.getColorTheme().getPrimaryDarkColor(),
                        PorterDuff.Mode.SRC_ATOP);
            }
        } catch (Exception e) {
            Log.e("themes", "render>toolbar:" + e.getMessage());
        }

        mToolbar.setNavigationIcon(drawable);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setUpFragment() {
        //track page mandiri ecash
        mMidtransSDK.trackEvent(AnalyticsEventName.PAGE_MANDIRI_ECASH);

        // setup  fragment
        mandiriECashFragment = new InstructionMandiriECashFragment();
        replaceFragment(mandiriECashFragment, R.id.instruction_container, false, false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_confirm_payment) {
            makeTransaction();
        }
    }

    private void makeTransaction() {
        //track mandiri ecash confirm payment
        mMidtransSDK.trackEvent(AnalyticsEventName.BTN_CONFIRM_PAYMENT);

        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);

        mMidtransSDK.paymentUsingMandiriEcash(mMidtransSDK.readAuthenticationToken(), new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                //track page status pending
                MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_PENDING);

                SdkUIFlowUtil.hideProgressDialog();

                if (response != null &&
                        !TextUtils.isEmpty(response.getRedirectUrl())) {
                    MandiriECashActivity.this.transactionResponse = response;
                    Intent intentPaymentWeb = new Intent(MandiriECashActivity.this, PaymentWebActivity.class);
                    intentPaymentWeb.putExtra(Constants.WEBURL, response.getRedirectUrl());
                    intentPaymentWeb.putExtra(Constants.TYPE, WebviewFragment.TYPE_MANDIRI_ECASH);
                    startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
                    if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                            && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    }
                } else {
                    SdkUIFlowUtil.showApiFailedMessage(MandiriECashActivity.this, getString(R.string
                            .empty_transaction_response));
                }
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                //track page status failed
                MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);

                SdkUIFlowUtil.hideProgressDialog();
                MandiriECashActivity.this.errorMessage = getString(R.string.message_payment_failed);
                MandiriECashActivity.this.transactionResponse = response;

                if (response != null && response.equals(getString(R.string.failed_code_400))) {
                    initPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_MANDIRI_ECASH, false);
                } else {
                    SdkUIFlowUtil.showToast(MandiriECashActivity.this, "" + errorMessage);
                }
            }

            @Override
            public void onError(Throwable error) {
                //track page status failed
                MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);

                SdkUIFlowUtil.hideProgressDialog();
                MandiriECashActivity.this.errorMessage = getString(R.string.message_payment_failed);
                SdkUIFlowUtil.showToast(MandiriECashActivity.this, "" + errorMessage);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("reqCode:" + requestCode + ",res:" + resultCode);
        Drawable closeIcon = ContextCompat.getDrawable(this, R.drawable.ic_close);

        if (closeIcon != null) {
            closeIcon.setColorFilter(ContextCompat.getColor(this, R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        }

        if (resultCode == RESULT_OK) {
            currentFragmentName = STATUS_FRAGMENT;
            mToolbar.setNavigationIcon(closeIcon);
            setSupportActionBar(mToolbar);
            transactionResponseFromMerchant = new TransactionResponse("200", "Transaction Success", UUID.randomUUID().toString(),
                    mMidtransSDK.getTransactionRequest().getOrderId(), String.valueOf(mMidtransSDK.getTransactionRequest().getAmount()), getString(R.string.payment_mandiri_ecash), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), getString(R.string.settlement));
            RESULT_CODE = RESULT_OK;
            setResultAndFinish();
            buttonConfirmPayment.setVisibility(View.GONE);
        } else if (resultCode == RESULT_CANCELED) {
            currentFragmentName = STATUS_FRAGMENT;
            mToolbar.setNavigationIcon(closeIcon);
            setSupportActionBar(mToolbar);
            RESULT_CODE = RESULT_OK;
            setResultAndFinish();
            buttonConfirmPayment.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        if (currentFragmentName.equals(STATUS_FRAGMENT)) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
            return;
        }
        super.onBackPressed();
    }
}

