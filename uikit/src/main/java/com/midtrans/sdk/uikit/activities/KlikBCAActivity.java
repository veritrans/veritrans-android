package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.BankTransactionStatusFragment;
import com.midtrans.sdk.uikit.fragments.KlikBCAFragment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

/**
 * @author rakawm
 */
public class KlikBCAActivity extends BaseActivity {

    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = PAYMENT_FRAGMENT;
    private TransactionResponse transactionResponse;
    private String errorMessage;
    private TextView mTextViewOrderId;
    private TextView mTextViewAmount;
    private Button mButtonConfirmPayment;
    private AppBarLayout mAppBarLayout;
    private ImageView logo;
    private TextView mTextViewTitle;
    private LinearLayout containerCollapsing;

    private KlikBCAFragment klikBCAFragment;

    private MidtransSDK mMidtransSDK;
    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klik_bca);

        // Get Veritrans SDK instance
        mMidtransSDK = MidtransSDK.getInstance();

        // Initialize views
        mTextViewOrderId = (TextView) findViewById(R.id.text_order_id);
        mTextViewAmount = (TextView) findViewById(R.id.text_amount);
        mTextViewTitle = (TextView) findViewById(R.id.text_title);
        mButtonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        containerCollapsing = (LinearLayout) findViewById(R.id.container_collapsing);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        initializeTheme();
        // Setup toolbar
        mToolbar.setTitle(""); // disable default Text
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindData();
    }

    private void bindData() {
        // Set title
        mTextViewTitle.setText(R.string.klik_bca);
        // Set transaction details
        mTextViewAmount.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(mMidtransSDK.getTransactionRequest().getAmount())));
        mTextViewOrderId.setText(mMidtransSDK.getTransactionRequest().getOrderId());


        // Set custom font if available
        if (mMidtransSDK.getSemiBoldText() != null) {
            mButtonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mMidtransSDK.getSemiBoldText()));
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
                        // Do the payment
                        SdkUIFlowUtil.showProgressDialog(KlikBCAActivity.this, getString(R.string.processing_payment), false);
                        mMidtransSDK.paymentUsingKlikBCA(mMidtransSDK.readAuthenticationToken(),
                                klikBCAFragment.getUserId(), new TransactionCallback() {
                                    @Override
                                    public void onSuccess(TransactionResponse response) {
                                        SdkUIFlowUtil.hideProgressDialog();
                                        transactionResponse = response;
                                        errorMessage = response.getStatusMessage();
                                        setUpTransactionStatusFragment(response);
                                    }

                                    @Override
                                    public void onFailure(TransactionResponse response, String reason) {
                                        errorMessage = getString(R.string.message_payment_cannot_proccessed);
                                        SdkUIFlowUtil.hideProgressDialog();
                                        SdkUIFlowUtil.showSnackbar(KlikBCAActivity.this, errorMessage);
                                    }

                                    @Override
                                    public void onError(Throwable error) {
                                        errorMessage = error.getMessage();
                                        SdkUIFlowUtil.hideProgressDialog();
                                        SdkUIFlowUtil.showSnackbar(KlikBCAActivity.this, error.getMessage());
                                    }
                                });
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Displays status of transaction from {@link TransactionResponse} object.
     *
     * @param transactionResponse response of the transaction call
     */
    private void setUpTransactionStatusFragment(final TransactionResponse
                                                        transactionResponse) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        currentFragment = STATUS_FRAGMENT;
        mButtonConfirmPayment.setText(R.string.done);

        mAppBarLayout.setExpanded(false, false);
        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        mToolbar.setNavigationIcon(closeIcon);
        setSupportActionBar(mToolbar);

        BankTransactionStatusFragment bankTransactionStatusFragment =
                BankTransactionStatusFragment.newInstance(transactionResponse, Constants.PAYMENT_METHOD_KLIKBCA);

        // setup transaction status fragment
        fragmentTransaction.replace(R.id.instruction_container,
                bankTransactionStatusFragment, STATUS_FRAGMENT);
        fragmentTransaction.addToBackStack(STATUS_FRAGMENT);
        fragmentTransaction.commit();

        mButtonConfirmPayment.setText(R.string.complete_payment_at_klik_bca);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
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

        return false;
    }
}
