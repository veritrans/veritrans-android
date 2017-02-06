package com.midtrans.sdk.uikit.activities;

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

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.BankTransferFragment;
import com.midtrans.sdk.uikit.fragments.InstructionIndosatFragment;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created to show and handle bank transfer and mandiri bill pay details. To handle these two
 * payments method we have created a fragment and depending upon the payment method selected in
 * previous screen we set respective fragment to handle that payment flow.
 * <p/>
 * <p/>
 * It has - {@link InstructionIndosatFragment} home fragment - an initial fragment which contains an
 * instruction. {@link com.midtrans.sdk.uikit.fragments.PaymentTransactionStatusFragment} - used to display status of transaction.
 * <p/>
 * <p/>
 * <p/>
 * It also contains an edit Text to take phone number as input from user and a button to confirm
 * payment.
 * <p/>
 * Created by shivam on 11/30/15.
 */
public class IndosatDompetkuActivity extends BaseActivity implements View.OnClickListener {

    public static final String HOME_FRAGMENT = "home";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";

    private Button mButtonConfirmPayment = null;
    private DefaultTextView textTitle, textTotalAmount;
    private FancyButton buttonBack;

    private MidtransSDK mMidtransSDK = null;
    private Toolbar mToolbar = null;
    private InstructionIndosatFragment mIndosatFragment = null;
    private TransactionResponse mTransactionResponse = null;
    private String errorMessage = null;

    private String phoneNumber = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indosat);

        mMidtransSDK = MidtransSDK.getInstance();

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
        //track page indosat dompetku
        mMidtransSDK.trackEvent(AnalyticsEventName.PAGE_INDOSAT_DOMPETKU);

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
                setResultCode(RESULT_OK);
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
        textTitle = (DefaultTextView) findViewById(R.id.text_title);
        textTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);
        buttonBack = (FancyButton) findViewById(R.id.btn_back);
        initializeTheme();
        //setup tool bar
        setSupportActionBar(mToolbar);
    }

    /**
     * set data to views.
     */
    private void bindDataToView() {
        textTitle.setText(getString(R.string.indosat_dompetku));
        if (mMidtransSDK != null) {
            if (mMidtransSDK.getSemiBoldText() != null) {
                mButtonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mMidtransSDK.getSemiBoldText()));
            }
            mButtonConfirmPayment.setOnClickListener(this);
            buttonBack.setOnClickListener(this);
            textTotalAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(mMidtransSDK.getTransactionRequest().getAmount())));
        } else {
            SdkUIFlowUtil.showToast(IndosatDompetkuActivity.this, getString(R.string.error_something_wrong));
            Logger.e(IndosatDompetkuActivity.class.getSimpleName(), Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
    }


    /**
     * Handles the click of confirm payment button based on following 2 conditions.
     * <p/>
     * 1) if current fragment is home fragment then it will start payment execution. 2) if current
     * fragment is status fragment  then it will send result back to {@link
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
                setResultCode(RESULT_OK);
                setResultAndFinish();
            }

        }else if(view.getId() == R.id.btn_back){
            onBackPressed();
        }
    }


    /**
     * Performs the validation and if satisfies the required condition then it will either start
     * indosat dompetku payment procedure.
     */
    private void performTransaction() {
        //track indosat dompetku confirm payment
        mMidtransSDK.trackEvent(AnalyticsEventName.BTN_CONFIRM_PAYMENT);

        if (mIndosatFragment != null && !mIndosatFragment.isDetached()) {

            phoneNumber = mIndosatFragment.getPhoneNumber();

            if (!TextUtils.isEmpty(phoneNumber) && SdkUIFlowUtil.isPhoneNumberValid(phoneNumber)) {
                Logger.i("setting phone number " + phoneNumber);
                mMidtransSDK.getTransactionRequest().getCustomerDetails().setPhone(phoneNumber.trim());
            } else {
                SdkUIFlowUtil.showToast(IndosatDompetkuActivity.this, getString(R.string.error_invalid_phone_number));
                return;
            }
        }


        final MidtransSDK midtransSDK = MidtransSDK.getInstance();

        if (midtransSDK != null) {

            SdkUIFlowUtil.showProgressDialog(IndosatDompetkuActivity.this, getString(R.string.processing_payment), false);
            transactionUsingIndosat(midtransSDK);

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
     * @param midtransSDK Veritrans SDK instance
     */
    private void transactionUsingIndosat(final MidtransSDK midtransSDK) {
        midtransSDK.paymentUsingIndosatDompetku(midtransSDK.readAuthenticationToken(),
                phoneNumber, new TransactionCallback() {
                    @Override
                    public void onSuccess(TransactionResponse response) {
                        SdkUIFlowUtil.hideProgressDialog();
                        mTransactionResponse = response;
                        if (response != null) {
                            setUpTransactionStatusFragment(response);
                        } else {
                            SdkUIFlowUtil.showToast(IndosatDompetkuActivity.this, SOMETHING_WENT_WRONG);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
                        mTransactionResponse = response;
                        IndosatDompetkuActivity.this.errorMessage = getString(R.string.message_payment_denied);
                        SdkUIFlowUtil.hideProgressDialog();

                        try {
                            if (response != null && response.getStatusCode().equals(getString(R.string.failed_code_400))) {
                                setUpTransactionStatusFragment(mTransactionResponse);
                            } else {
                                SdkUIFlowUtil.showToast(IndosatDompetkuActivity.this, "" + errorMessage);
                            }
                        } catch (NullPointerException ex) {
                            Logger.e("transaction error is " + errorMessage);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        try {
                            IndosatDompetkuActivity.this.errorMessage = getString(R.string.message_payment_failed);
                            SdkUIFlowUtil.hideProgressDialog();
                            SdkUIFlowUtil.showToast(IndosatDompetkuActivity.this, "" + errorMessage);

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
        currentFragment = STATUS_FRAGMENT;
        mButtonConfirmPayment.setText(R.string.done);

        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        buttonBack.setIconResource(closeIcon);

        initPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_INDOSAT_DOMPETKU, false);
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
        setResultAndFinish(this.mTransactionResponse, errorMessage);
    }

    @Override
    public void onBackPressed() {
        if (currentFragment.equals(STATUS_FRAGMENT)) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
            return;
        }
        super.onBackPressed();
    }
}