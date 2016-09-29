package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.BBMMoneyPaymentFragment;
import com.midtrans.sdk.uikit.fragments.BBMMoneyPaymentStatusFragment;
import com.midtrans.sdk.uikit.fragments.BankTransferFragment;
import com.midtrans.sdk.uikit.fragments.InstructionBBMMoneyFragment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.MidtransDialog;

/**
 * Created by Ankit on 12/3/15.
 */
public class BBMMoneyActivity extends BaseActivity implements View.OnClickListener {

    public static final String HOME_FRAGMENT = "home";
    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";

    private TextView textViewOrderId = null;
    private TextView textViewAmount = null;
    private Button buttonConfirmPayment = null;
    private AppBarLayout appBarLayout = null;
    private TextView textViewTitle = null;
    private LinearLayout layoutPayWithBBM = null;

    private MidtransSDK midtransSDK = null;
    private Toolbar toolbar = null;

    private InstructionBBMMoneyFragment instructionBBMMoneyFragment = null;
    private BBMMoneyPaymentFragment bbmMoneyPaymentFragment = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private CollapsingToolbarLayout collapsingToolbarLayout = null;

    private int position = Constants.PAYMENT_METHOD_BBM_MONEY;
    private int RESULT_CODE = RESULT_CANCELED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbm_money);

        midtransSDK = MidtransSDK.getInstance();

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(getString(R.string.position), Constants
                    .PAYMENT_METHOD_BBM_MONEY);
        } else {
            SdkUIFlowUtil.showSnackbar(BBMMoneyActivity.this, getString(R.string.error_something_wrong));
            finish();
        }

        initializeView();
        initializeTheme();
        bindDataToView();
        setUpHomeFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * set up {@link BankTransferFragment} to display payment instructions.
     */
    private void setUpHomeFragment() {
        // setup home fragment

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        instructionBBMMoneyFragment = new InstructionBBMMoneyFragment();

        fragmentTransaction.add(R.id.bbm_money_container,
                instructionBBMMoneyFragment, HOME_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
    }

    @Override
    public void onBackPressed() {
        setResultAndFinish();
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
        layoutPayWithBBM = (LinearLayout) findViewById(R.id.layout_pay_with_bbm);

        //setup tool bar
        toolbar.setTitle(""); // disable default Text
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void bindDataToView() {
        if (midtransSDK != null) {
            textViewAmount.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(midtransSDK.getTransactionRequest().getAmount())));
            textViewOrderId.setText("" + midtransSDK.getTransactionRequest().getOrderId());
            if (midtransSDK != null && midtransSDK.getSemiBoldText() != null) {
                buttonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), midtransSDK.getSemiBoldText()));
            }
            buttonConfirmPayment.setOnClickListener(this);
            layoutPayWithBBM.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_confirm_payment) {

            if (SdkUIFlowUtil.isBBMMoneyInstalled(BBMMoneyActivity.this)) {

                if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {

                    performTransaction();

                } else if (currentFragment.equalsIgnoreCase(PAYMENT_FRAGMENT)) {

                    appBarLayout.setExpanded(true);

                    if (transactionResponse == null) {
                        RESULT_CODE = RESULT_OK;
                        SdkUIFlowUtil.showSnackbar(BBMMoneyActivity.this, SOMETHING_WENT_WRONG);
                        onBackPressed();
                    }
                } else {
                    RESULT_CODE = RESULT_OK;
                    onBackPressed();
                }

            } else {
                Logger.i("BBM Not present");
                MidtransDialog midtransDialog = new MidtransDialog(BBMMoneyActivity.this,
                        getString(R.string.title_bbm_not_found),
                        getString(R.string.message_bbm_not_found), getString(R.string.got_it), "");
                midtransDialog.show();
            }
        }

        if (view.getId() == R.id.layout_pay_with_bbm) {

            if (midtransSDK != null && midtransSDK.getBBMCallBackUrl() != null) {

                String encodedUrl = SdkUIFlowUtil.createEncodedUrl(bbmMoneyPaymentFragment.PERMATA_VA,
                        midtransSDK.getBBMCallBackUrl().getCheckStatus(),
                        midtransSDK.getBBMCallBackUrl().getBeforePaymentError(),
                        midtransSDK.getBBMCallBackUrl().getUserCancel());

                String feedUrl = BuildConfig.BBM_PREFIX_URL + encodedUrl;

                if (SdkUIFlowUtil.isBBMMoneyInstalled(BBMMoneyActivity.this)) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(feedUrl)));
                } else {
                    instructionBBMMoneyFragment.openPlayStore();
                }
            }
        }
    }

    private void performTransaction() {
        SdkUIFlowUtil.showProgressDialog(BBMMoneyActivity.this, false);
    }

    private void setUpTransactionStatusFragment(final TransactionResponse transactionResponse) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        currentFragment = STATUS_FRAGMENT;
        buttonConfirmPayment.setVisibility(View.VISIBLE);
        layoutPayWithBBM.setVisibility(View.GONE);
        buttonConfirmPayment.setText(R.string.done);

        collapsingToolbarLayout.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.ic_close);

        BBMMoneyPaymentStatusFragment bbmMoneyPaymentStatusFragment =
                BBMMoneyPaymentStatusFragment.newInstance(transactionResponse, false);

        // setup transaction status fragment
        fragmentTransaction.replace(R.id.bbm_money_container,
                bbmMoneyPaymentStatusFragment, STATUS_FRAGMENT);
        fragmentTransaction.addToBackStack(STATUS_FRAGMENT);
        fragmentTransaction.commit();
    }

    private void setUpTransactionFragment(final TransactionResponse transactionResponse) {
        if (transactionResponse != null) {
            // setup transaction fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            bbmMoneyPaymentFragment =
                    BBMMoneyPaymentFragment.newInstance(transactionResponse);
            fragmentTransaction.replace(R.id.bbm_money_container,
                    bbmMoneyPaymentFragment, PAYMENT_FRAGMENT);
            fragmentTransaction.addToBackStack(PAYMENT_FRAGMENT);
            fragmentTransaction.commit();
            buttonConfirmPayment.setVisibility(View.GONE);
            layoutPayWithBBM.setVisibility(View.VISIBLE);
            currentFragment = PAYMENT_FRAGMENT;
        } else {
            SdkUIFlowUtil.showSnackbar(BBMMoneyActivity.this, SOMETHING_WENT_WRONG);
            onBackPressed();
        }
    }

    public int getPosition() {
        return position;
    }

    public void activateRetry() {

        if (buttonConfirmPayment != null) {
            buttonConfirmPayment.setText(getResources().getString(R.string.retry));
        }
    }

    private void setResultAndFinish() {
        Intent data = new Intent();
        data.putExtra(getString(R.string.transaction_response), transactionResponse);
        data.putExtra(getString(R.string.error_transaction), errorMessage);
        setResult(RESULT_CODE, data);
        finish();
    }

//    public void onEvent(TransactionSuccessEvent event) {
//        SdkUIFlowUtil.hideProgressDialog();
//
//        if (transactionResponse != null) {
//            BBMMoneyActivity.this.transactionResponse = event.getResponse();
//            appBarLayout.setExpanded(true);
//            setUpTransactionFragment(transactionResponse);
//        } else {
//            onBackPressed();
//        }
//    }
//
//    public void onEvent(TransactionFailedEvent event) {
//        try {
//            BBMMoneyActivity.this.errorMessage = event.getMessage();
//            BBMMoneyActivity.this.transactionResponse = event.getResponse();
//
//            SdkUIFlowUtil.hideProgressDialog();
//            SdkUIFlowUtil.showSnackbar(BBMMoneyActivity.this, "" + errorMessage);
//        } catch (NullPointerException ex) {
//            Logger.e("transaction error is " + errorMessage);
//        }
//    }
//
//    public void onEvent(NetworkUnavailableEvent event) {
//        BBMMoneyActivity.this.errorMessage = getString(R.string.no_network_msg);
//
//        SdkUIFlowUtil.hideProgressDialog();
//        SdkUIFlowUtil.showSnackbar(BBMMoneyActivity.this, "" + errorMessage);
//    }
//
//    public void onEvent(GeneralErrorEvent event) {
//        BBMMoneyActivity.this.errorMessage = event.getMessage();
//
//        SdkUIFlowUtil.hideProgressDialog();
//        SdkUIFlowUtil.showSnackbar(BBMMoneyActivity.this, "" + errorMessage);
//    }
}
