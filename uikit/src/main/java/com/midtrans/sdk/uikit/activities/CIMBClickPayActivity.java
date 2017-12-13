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
import android.widget.ImageView;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.InstructionCIMBFragment;
import com.midtrans.sdk.uikit.fragments.WebviewFragment;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

/**
 * Created by Ankit on 11/26/15.
 * Deprecated, please use {@link com.midtrans.sdk.uikit.views.cimb_click.CimbClickPaymentActivity} instead
 */
@Deprecated
public class CIMBClickPayActivity extends BaseActivity implements View.OnClickListener {

    private static final int PAYMENT_WEB_INTENT = 151;
    private static final String STATUS_FRAGMENT = "status";
    private static final String HOME_FRAGMENT = "home";
    private static final String TAG = "CIMBClickPayActivity";
    private InstructionCIMBFragment cimbClickPayFragment = null;
    private FancyButton buttonConfirmPayment = null;
    private Toolbar mToolbar = null;
    private ImageView logo = null;
    private MidtransSDK mMidtransSDK = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;

    private String currentFragmentName = HOME_FRAGMENT;
    private TransactionResponse transactionResponseFromMerchant;
    private SemiBoldTextView textTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cimb_clickpay);
        mMidtransSDK = MidtransSDK.getInstance();

        if (mMidtransSDK == null) {
            SdkUIFlowUtil.showToast(CIMBClickPayActivity.this, Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
        initializeViews();
        setUpFragment();
    }

    private void initializeViews() {
        buttonConfirmPayment = (FancyButton) findViewById(R.id.button_primary);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        textTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
        initializeTheme();
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        prepareToolbar();
        buttonConfirmPayment.setOnClickListener(this);
        bindData();
    }

    private void prepareToolbar() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_back);

        try {
            MidtransSDK midtransSDK = MidtransSDK.getInstance();

            if (midtransSDK != null && midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                drawable.setColorFilter(
                        midtransSDK.getColorTheme().getPrimaryDarkColor(),
                        PorterDuff.Mode.SRC_ATOP);
            }
        } catch (Exception e) {
            Log.d(TAG, "render toolbar:" + e.getMessage());
        }
        mToolbar.setNavigationIcon(drawable);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragmentName.equals(STATUS_FRAGMENT)) {
                    setResultCode(RESULT_OK);
                    setResultAndFinish();
                } else {
                    onBackPressed();
                }
            }
        });
        adjustToolbarSize();
    }

    private void bindData() {
        textTitle.setText(getString(R.string.cimb_clicks));
        buttonConfirmPayment.setText(getString(R.string.confirm_payment));
        buttonConfirmPayment.setTextBold();
    }

    private void setUpFragment() {
        // setup  fragment
        cimbClickPayFragment = new InstructionCIMBFragment();
        replaceFragment(cimbClickPayFragment, R.id.instruction_container, false, false);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_primary) {
            makeTransaction();
        }
    }

    private void makeTransaction() {
        SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment), false);
        mMidtransSDK.paymentUsingCIMBClick(mMidtransSDK.readAuthenticationToken(), new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                SdkUIFlowUtil.hideProgressDialog();

                if (response != null &&
                        !TextUtils.isEmpty(response.getRedirectUrl())) {
                    transactionResponse = response;
                    Intent intentPaymentWeb = new Intent(CIMBClickPayActivity.this, PaymentWebActivity.class);
                    intentPaymentWeb.putExtra(Constants.WEBURL, response.getRedirectUrl());
                    intentPaymentWeb.putExtra(Constants.TYPE, WebviewFragment.TYPE_CIMB_CLICK);
                    intentPaymentWeb.putExtra(Constants.WEBVIEW_REDIRECT_URL, response.getFinishRedirectUrl());
                    startActivityForResult(intentPaymentWeb, PAYMENT_WEB_INTENT);
                    if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                            && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    }
                } else {
                    SdkUIFlowUtil.showApiFailedMessage(CIMBClickPayActivity.this, getString(R.string
                            .empty_transaction_response));
                }
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                try {
                    CIMBClickPayActivity.this.errorMessage = getString(R.string.message_payment_failed);
                    CIMBClickPayActivity.this.transactionResponse = response;
                    SdkUIFlowUtil.hideProgressDialog();

                    if (response != null && response.getStatusCode().equals(getString(R.string.failed_code_400))) {
                        setResultCode(RESULT_OK);
                        setResultAndFinish();
                    } else {
                        SdkUIFlowUtil.showToast(CIMBClickPayActivity.this, "" + errorMessage);
                    }
                } catch (NullPointerException ex) {
                    SdkUIFlowUtil.showApiFailedMessage(CIMBClickPayActivity.this, getString(R.string.message_payment_failed));
                }
            }

            @Override
            public void onError(Throwable error) {
                String message = MessageUtil.createPaymentErrorMessage(CIMBClickPayActivity.this, error.getMessage(), null);

                errorMessage = message;
                SdkUIFlowUtil.hideProgressDialog();
                SdkUIFlowUtil.showToast(CIMBClickPayActivity.this, "" + errorMessage);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.i("reqCode:" + requestCode + ",res:" + resultCode);
        Drawable closeIcon = ContextCompat.getDrawable(this, R.drawable.ic_close);
        closeIcon.setColorFilter(ContextCompat.getColor(this, R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
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


    @Override
    public void onBackPressed() {
        if (isDetailShown) {
            displayOrHideItemDetails();
        } else {
            super.onBackPressed();
        }
    }
}
