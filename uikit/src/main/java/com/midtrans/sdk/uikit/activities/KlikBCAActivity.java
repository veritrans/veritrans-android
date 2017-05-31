package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.fragments.KlikBCAFragment;
import com.midtrans.sdk.uikit.fragments.KlikBCAStatusFragment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * @author rakawm
 */
public class KlikBCAActivity extends BaseActivity {

    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    private static final String TAG = "KlikBCAActivity";
    public String currentFragment = PAYMENT_FRAGMENT;
    private TransactionResponse transactionResponse;
    private String errorMessage;
    private TextView mTextViewAmount;
    private FancyButton mButtonConfirmPayment;
    private TextView mTextViewTitle;

    private KlikBCAFragment klikBCAFragment;
    private MidtransSDK mMidtransSDK;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klik_bca);

        // Get Veritrans SDK instance
        mMidtransSDK = MidtransSDK.getInstance();

        // Initialize views
        mTextViewAmount = (TextView) findViewById(R.id.text_amount);
        mTextViewTitle = (TextView) findViewById(R.id.text_title);
        mButtonConfirmPayment = (FancyButton) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        initializeTheme();
        // Setup toolbar
        mToolbar.setTitle(""); // disable default Text
        setSupportActionBar(mToolbar);
        prepareToolbar();
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
                if (currentFragment.equals(STATUS_FRAGMENT)) {
                    Intent data = new Intent();
                    if (transactionResponse != null) {
                        data.putExtra(getString(R.string.transaction_response), transactionResponse);
                    }
                    if (errorMessage != null && !errorMessage.equals("")) {
                        data.putExtra(getString(R.string.error_transaction), errorMessage);
                    }
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    onBackPressed();
                }
            }
        });
    }

    private void bindData() {
        // Set title
        mTextViewTitle.setText(R.string.klik_bca);
        // Set transaction details
        mTextViewAmount.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(mMidtransSDK.getTransactionRequest().getAmount())));

        // Set custom font if available
        if (mMidtransSDK.getSemiBoldText() != null) {
            mButtonConfirmPayment.setCustomTextFont(mMidtransSDK.getSemiBoldText());
        }

        // Initialize fragment
        klikBCAFragment = new KlikBCAFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.instruction_container, klikBCAFragment).commit();

        mButtonConfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment.equals(STATUS_FRAGMENT)) {
                    Intent data = new Intent();
                    if (transactionResponse != null) {
                        data.putExtra(getString(R.string.transaction_response), transactionResponse);
                    }
                    if (errorMessage != null && !errorMessage.equals("")) {
                        data.putExtra(getString(R.string.error_transaction), errorMessage);
                    }
                    setResult(RESULT_OK, data);
                    finish();
                } else {
                    // Check klik BCA user ID
                    if (klikBCAFragment.checkUserId()) {

                        //track btn confirm
                        mMidtransSDK.trackEvent(AnalyticsEventName.BTN_CONFIRM_PAYMENT);

                        // Do the payment
                        SdkUIFlowUtil.showProgressDialog(KlikBCAActivity.this, getString(R.string.processing_payment), false);
                        mMidtransSDK.paymentUsingKlikBCA(mMidtransSDK.readAuthenticationToken(),
                                klikBCAFragment.getUserId(), new TransactionCallback() {
                                    @Override
                                    public void onSuccess(TransactionResponse response) {
                                        //track page status pending
                                        MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_PENDING);

                                        SdkUIFlowUtil.hideProgressDialog();
                                        transactionResponse = response;
                                        errorMessage = response.getStatusMessage();
                                        setUpTransactionStatusFragment(response);
                                    }

                                    @Override
                                    public void onFailure(TransactionResponse response, String reason) {
                                        //track page status failed
                                        MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);

                                        errorMessage = getString(R.string.message_payment_cannot_proccessed);
                                        transactionResponse = response;
                                        SdkUIFlowUtil.hideProgressDialog();

                                        if (response != null && response.getStatusCode().equals(getString(R.string.failed_code_400))) {
                                            setUpTransactionStatusFragment(response);
                                        } else {
                                            SdkUIFlowUtil.showToast(KlikBCAActivity.this, errorMessage);
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable error) {
                                        //track page status failed
                                        MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);

                                        errorMessage = getString(R.string.message_payment_failed);
                                        SdkUIFlowUtil.hideProgressDialog();
                                        SdkUIFlowUtil.showToast(KlikBCAActivity.this, errorMessage);
                                    }
                                });
                    }
                }
            }
        });

        //track page Klik BCA
        mMidtransSDK.trackEvent(AnalyticsEventName.PAGE_BCA_KLIKBCA);
    }

    /**
     * Displays status of transaction from {@link TransactionResponse} object.
     *
     * @param transactionResponse response of the transaction call
     */
    private void setUpTransactionStatusFragment(final TransactionResponse
                                                        transactionResponse) {

        currentFragment = STATUS_FRAGMENT;

        mButtonConfirmPayment.setText(getString(R.string.complete_payment_at_klik_bca));

        KlikBCAStatusFragment klikBCAStatusFragment =
                KlikBCAStatusFragment.newInstance(transactionResponse);
        getSupportFragmentManager().beginTransaction().replace(R.id.instruction_container, klikBCAStatusFragment).commit();
    }

    @Override
    public void onBackPressed() {

        if (currentFragment.equals(STATUS_FRAGMENT)) {
            setResultCode(RESULT_OK);
            setResultAndFinish(transactionResponse, errorMessage);
        }
        super.onBackPressed();
    }
}
