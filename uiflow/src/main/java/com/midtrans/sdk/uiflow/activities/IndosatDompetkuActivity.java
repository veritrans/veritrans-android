package com.midtrans.sdk.uiflow.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.midtrans.sdk.coreflow.callback.TransactionCallback;
import com.midtrans.sdk.coreflow.core.Constants;
import com.midtrans.sdk.coreflow.core.Logger;
import com.midtrans.sdk.coreflow.core.VeritransSDK;
import com.midtrans.sdk.coreflow.models.TransactionResponse;
import com.midtrans.sdk.coreflow.utilities.Utils;
import com.midtrans.sdk.uiflow.fragments.BankTransactionStatusFragment;
import com.midtrans.sdk.uiflow.fragments.BankTransferFragment;
import com.midtrans.sdk.uiflow.fragments.InstructionIndosatFragment;
import com.midtrans.sdk.uiflow.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uiflow.widgets.DefaultTextView;

import com.midtrans.sdk.uiflow.R;

/**
 * Created to show and handle bank transfer and mandiri bill pay details.
 * To handle these two payments method we have created a fragment and depending upon the payment
 * method selected
 * in previous screen we set respective fragment to handle that payment flow.
 * <p/>
 * <p/>
 * It has -
 * {@link InstructionIndosatFragment} home fragment - an initial
 * fragment which contains an instruction.
 * {@link BankTransactionStatusFragment} - used to display status of transaction.
 * <p/>
 * <p/>
 * <p/>
 * It also contains an edit Text to take phone number as input from user and
 * a button to confirm payment.
 * <p/>
 * Created by shivam on 11/30/15.
 */
public class IndosatDompetkuActivity extends BaseActivity implements View.OnClickListener {

    public static final String HOME_FRAGMENT = "home";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";

    private Button mButtonConfirmPayment = null;
    private DefaultTextView textTitle, textOrderId, textTotalAmount;


    private VeritransSDK mVeritransSDK = null;
    private Toolbar mToolbar = null;
    private ImageView logo = null;
    private InstructionIndosatFragment mIndosatFragment = null;
    private TransactionResponse mTransactionResponse = null;
    private String errorMessage = null;
    private int RESULT_CODE = RESULT_CANCELED;

    private String phoneNumber = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indosat);

        mVeritransSDK = VeritransSDK.getInstance();

        initializeView();
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
        mIndosatFragment = new InstructionIndosatFragment();

        fragmentTransaction.add(R.id.instruction_container, mIndosatFragment, HOME_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (currentFragment.equals(STATUS_FRAGMENT)) {
                RESULT_CODE = RESULT_OK;
                setResultAndFinish();
            } else {
                onBackPressed();
            }
        }

        return false;
    }


    /**
     * initialize all views
     */
    private void initializeView() {

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mButtonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        textTitle = (DefaultTextView) findViewById(R.id.text_title);
        textOrderId = (DefaultTextView) findViewById(R.id.text_order_id);
        textTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);

        initializeTheme();
        //setup tool bar
        mToolbar.setTitle(""); // disable default Text
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * set data to views.
     */
    private void bindDataToView() {
        textTitle.setText(getString(R.string.indosat_dompetku));
        if (mVeritransSDK != null) {
            if (mVeritransSDK.getSemiBoldText() != null) {
                mButtonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mVeritransSDK.getSemiBoldText()));
            }
            mButtonConfirmPayment.setOnClickListener(this);
            textOrderId.setText(mVeritransSDK.getTransactionRequest().getOrderId());
            textTotalAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(mVeritransSDK.getTransactionRequest().getAmount())));
        } else {
            SdkUIFlowUtil.showSnackbar(IndosatDompetkuActivity.this, getString(R.string.error_something_wrong));
            Logger.e(IndosatDompetkuActivity.class.getSimpleName(), Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
    }


    /**
     * Handles the click of confirm payment button based on following 2 conditions.
     * <p/>
     * 1) if current fragment is home fragment then it will start payment execution.
     * 2) if current fragment is status fragment  then it will send result back to {@link
     * PaymentMethodsActivity}.
     *
     * @param view clicked view
     */
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {

            if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {
                performTransaction();

            } else {
                RESULT_CODE = RESULT_OK;
                setResultAndFinish();
            }

        }
    }


    /**
     * Performs the validation and if satisfies the required condition then it will either start
     * indosat dompetku payment procedure.
     */
    private void performTransaction() {

        if (mIndosatFragment != null && !mIndosatFragment.isDetached()) {

            phoneNumber = mIndosatFragment.getPhoneNumber();

            if (!TextUtils.isEmpty(phoneNumber) && SdkUIFlowUtil.isPhoneNumberValid(phoneNumber)) {
                Logger.i("setting phone number " + phoneNumber);
                mVeritransSDK.getTransactionRequest().getCustomerDetails().setPhone(phoneNumber.trim());
            } else {
                SdkUIFlowUtil.showSnackbar(IndosatDompetkuActivity.this, getString(R.string.error_invalid_phone_number));
                return;
            }
        }


        final VeritransSDK veritransSDK = VeritransSDK.getInstance();

        if (veritransSDK != null) {

            SdkUIFlowUtil.showProgressDialog(IndosatDompetkuActivity.this, getString(R.string.processing_payment), false);
            transactionUsingIndosat(veritransSDK);

        } else {
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
    }


    /**
     * It execute mandiri bill payment transaction and in onSuccess() of callback method it will
     * call {@link #setUpTransactionStatusFragment(TransactionResponse)}  to display appropriate
     * message.
     *
     * @param veritransSDK Veritrans SDK instance
     */
    private void transactionUsingIndosat(final VeritransSDK veritransSDK) {
        veritransSDK.snapPaymentUsingIndosatDompetku(veritransSDK.readAuthenticationToken(),
                phoneNumber, new TransactionCallback() {
                    @Override
                    public void onSuccess(TransactionResponse response) {
                        SdkUIFlowUtil.hideProgressDialog();
                        mTransactionResponse = response;
                        if (response != null) {
                            setUpTransactionStatusFragment(response);
                        } else {
                            SdkUIFlowUtil.showSnackbar(IndosatDompetkuActivity.this, SOMETHING_WENT_WRONG);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
                        mTransactionResponse = response;
                        IndosatDompetkuActivity.this.errorMessage = reason;
                        try {
                            SdkUIFlowUtil.hideProgressDialog();
                            SdkUIFlowUtil.showSnackbar(IndosatDompetkuActivity.this, "" + errorMessage);
                        } catch (NullPointerException ex) {
                            Logger.e("transaction error is " + errorMessage);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        try {
                            IndosatDompetkuActivity.this.errorMessage = error.getMessage();
                            SdkUIFlowUtil.hideProgressDialog();
                            SdkUIFlowUtil.showSnackbar(IndosatDompetkuActivity.this, "" + errorMessage);

                        } catch (NullPointerException e) {
                            Logger.e("transaction error is " + e.getMessage());
                        }
                    }
                });
    }


    /**
     * Displays status of transaction from {@link TransactionResponse} object.
     *
     * @param transactionResponse response of transaction call
     */
    private void setUpTransactionStatusFragment(final TransactionResponse
                                                        transactionResponse) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        currentFragment = STATUS_FRAGMENT;
        mButtonConfirmPayment.setText(R.string.done);

        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        mToolbar.setNavigationIcon(closeIcon);
        setSupportActionBar(mToolbar);

        BankTransactionStatusFragment bankTransactionStatusFragment =
                BankTransactionStatusFragment.newInstance(transactionResponse,
                        Constants.PAYMENT_METHOD_INDOSAT_DOMPETKU);

        // setup transaction status fragment
        fragmentTransaction.replace(R.id.instruction_container,
                bankTransactionStatusFragment, STATUS_FRAGMENT);
        fragmentTransaction.addToBackStack(STATUS_FRAGMENT);
        fragmentTransaction.commit();
    }

    /**
     * in case of transaction failure it will change the text of confirm payment button to 'RETRY'
     */
    public void activateRetry() {

        if (mButtonConfirmPayment != null) {
            mButtonConfirmPayment.setText(getResources().getString(R.string.retry));
        }
    }

    /**
     * send result back to  {@link PaymentMethodsActivity} and finish current activity.
     */
    private void setResultAndFinish() {
        Intent data = new Intent();
        data.putExtra(getString(R.string.transaction_response), mTransactionResponse);
        data.putExtra(getString(R.string.error_transaction), errorMessage);
        setResult(RESULT_CODE, data);
        finish();
    }
}