package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.BankTransferFragment;
import com.midtrans.sdk.uikit.fragments.InstructionIndosatFragment;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

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
 * Deprecated, please use {@link com.midtrans.sdk.uikit.views.indosat_dompetku.IndosatDompetkuPaymentActivity} instead
 */
@Deprecated
public class IndosatDompetkuActivity extends BaseActivity implements View.OnClickListener {

    public static final String HOME_FRAGMENT = "home";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    private static final String TAG = "IndosatDompetkuActivity";
    public String currentFragment = "home";

    private FancyButton mButtonConfirmPayment = null;
    private SemiBoldTextView textTitle;

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
        // setup home fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mIndosatFragment = new InstructionIndosatFragment();

        fragmentTransaction.add(R.id.instruction_container, mIndosatFragment, HOME_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
    }

    /**
     * initialize all views
     */
    private void initializeView() {

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mButtonConfirmPayment = (FancyButton) findViewById(R.id.button_primary);
        textTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
        initializeTheme();
        //setup tool bar
        setSupportActionBar(mToolbar);
        prepareToolbar();
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
            Logger.d(TAG, "render toolbar:" + e.getMessage());
        }

        mToolbar.setNavigationIcon(drawable);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragment.equals(STATUS_FRAGMENT)) {
                    setResultCode(RESULT_OK);
                    setResultAndFinish();
                } else {
                    onBackPressed();
                }
            }
        });
        adjustToolbarSize();
    }

    /**
     * set data to views.
     */
    private void bindDataToView() {
        textTitle.setText(getString(R.string.indosat_dompetku));
        mButtonConfirmPayment.setText(getString(R.string.confirm_payment));
        mButtonConfirmPayment.setTextBold();
        if (mMidtransSDK != null) {
            mButtonConfirmPayment.setOnClickListener(this);
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

        if (view.getId() == R.id.button_primary) {

            if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {
                performTransaction();
            } else {
                setResultCode(RESULT_OK);
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
                            String message = MessageUtil.createPaymentErrorMessage(IndosatDompetkuActivity.this, error.getMessage(), null);

                            errorMessage = message;
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
        mButtonConfirmPayment.setText(getString(R.string.done));

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
        if (isDetailShown) {
            displayOrHideItemDetails();
        } else if (currentFragment.equals(STATUS_FRAGMENT)) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
            return;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS)
            if (resultCode == RESULT_CANCELED || resultCode == RESULT_OK) {
                setResultAndFinish();
            }
    }

}