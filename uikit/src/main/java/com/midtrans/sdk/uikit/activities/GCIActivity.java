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
import android.widget.TextView;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.GCIPaymentFragment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

/**
 * Created by ziahaqi on 12/7/16.
 */

public class GCIActivity extends BaseActivity implements View.OnClickListener {

    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public String currentFragment = "home";

    private TextView textViewOrderId = null;
    private TextView textViewAmount = null;
    private Button buttonConfirmPayment = null;
    private AppBarLayout appBarLayout = null;
    private TextView textViewTitle = null;
    private ImageView logo = null;
    private GCIPaymentFragment paymentFragment;
    private MidtransSDK midtransSDK = null;
    private Toolbar toolbar = null;

    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private int position = Constants.PAYMENT_METHOD_INDOMARET;
    private int retryNumber = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gci);

        midtransSDK = MidtransSDK.getInstance();

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(getString(R.string.position), Constants
                    .PAYMENT_METHOD_INDOMARET);
        } else {
            SdkUIFlowUtil.showSnackbar(GCIActivity.this, getString(R.string.error_something_wrong));
            finish();
        }

        initializeView();
        bindDataToView();
        setUpHomeFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * set up {@link } to display payment instructions.
     */
    private void setUpHomeFragment() {
        // setup home fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        paymentFragment = new GCIPaymentFragment();
        fragmentTransaction.add(R.id.instruction_container, paymentFragment, PAYMENT_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = PAYMENT_FRAGMENT;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return false;
    }

    private void initializeView() {
        textViewOrderId = (TextView) findViewById(R.id.text_order_id);
        textViewAmount = (TextView) findViewById(R.id.text_amount);
        textViewTitle = (TextView) findViewById(R.id.text_title);
        buttonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        initializeTheme();
        //setup tool bar
        toolbar.setTitle(""); // disable default Text
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindDataToView() {
        textViewTitle.setText(getString(R.string.title_gci));
        if (midtransSDK != null) {
            textViewAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(midtransSDK.getTransactionRequest().getAmount())));
            textViewOrderId.setText("" + midtransSDK.getTransactionRequest().getOrderId());
            if (midtransSDK.getSemiBoldText() != null) {
                buttonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), midtransSDK.getSemiBoldText()));
            }
            buttonConfirmPayment.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {
            if (currentFragment.equalsIgnoreCase(STATUS_FRAGMENT)) {
                appBarLayout.setExpanded(true);
                setResultCode(RESULT_OK);
                setResultAndFinish();
            } else {
                performTransaction();
            }
        }
    }

    private void setUpTransactionStatusFragment(final TransactionResponse
                                                        transactionResponse) {

        currentFragment = STATUS_FRAGMENT;
        buttonConfirmPayment.setText(R.string.done);

        appBarLayout.setExpanded(false, false);
        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        toolbar.setNavigationIcon(closeIcon);
        setSupportActionBar(toolbar);
        buttonConfirmPayment.setText(R.string.complete_payment_at_klik_bca);

        initBankTransferPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_GCI, STATUS_FRAGMENT);
    }


    private void performTransaction() {

        if (paymentFragment.checkFormValidity()) {
            SdkUIFlowUtil.showProgressDialog(GCIActivity.this, getString(R.string.processing_payment),
                    false);

            String cardNumber = paymentFragment.getCardNumber();
            String password = paymentFragment.getPassword();

            //Execute transaction
            midtransSDK.paymentUsingGCI(midtransSDK.readAuthenticationToken(), cardNumber, password, new TransactionCallback() {
                @Override
                public void onSuccess(TransactionResponse response) {
                    SdkUIFlowUtil.hideProgressDialog();
                    if (response != null) {
                        GCIActivity.this.transactionResponse = response;
                        appBarLayout.setExpanded(true);
                        setUpTransactionStatusFragment(response);
                    } else {
                        onBackPressed();
                    }
                }

                @Override
                public void onFailure(TransactionResponse response, String reason) {
                    GCIActivity.this.errorMessage = getString(R.string.message_payment_failed);
                    GCIActivity.this.transactionResponse = response;
                    SdkUIFlowUtil.hideProgressDialog();
                    if (retryNumber > 0) {
                        buttonConfirmPayment.setText(getString(R.string.retry));
                        SdkUIFlowUtil.showSnackbar(GCIActivity.this, "" + errorMessage);
                        --retryNumber;
                    } else {
                        setUpTransactionStatusFragment(response);
                    }
                }

                @Override
                public void onError(Throwable error) {
                    SdkUIFlowUtil.hideProgressDialog();
                    GCIActivity.this.errorMessage = getString(R.string.message_payment_failed);
                    SdkUIFlowUtil.showSnackbar(GCIActivity.this, "" + errorMessage);
                }
            });
        }

    }

    public int getPosition() {
        return position;
    }

    /**
     * in case of transaction failure it will change the text of confirm payment button to 'RETRY'
     */
    public void activateRetry() {

        if (buttonConfirmPayment != null) {
            buttonConfirmPayment.setText(getResources().getString(R.string.retry));
        }
    }

    /**
     * send result back to  {@link PaymentMethodsActivity} and finish current activity.
     */
    private void setResultAndFinish() {
        setResultAndFinish(transactionResponse, errorMessage);
    }

    @Override
    public void onBackPressed() {
        if (currentFragment.equals(STATUS_FRAGMENT)) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
        } else {
            super.onBackPressed();
        }
    }

}
