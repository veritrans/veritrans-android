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
import com.midtrans.sdk.uikit.fragments.InstructionTelkomselCashFragment;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * @author rakawm
 */
public class TelkomselCashActivity extends BaseActivity implements View.OnClickListener {

    public static final String HOME_FRAGMENT = "home";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";

    private Button mButtonConfirmPayment = null;

    private MidtransSDK mMidtransSDK = null;
    private Toolbar mToolbar = null;
    private DefaultTextView textTitle, textTotalAmount;
    private FancyButton buttonBack;
    private InstructionTelkomselCashFragment telkomselCashFragment = null;
    private TransactionResponse mTransactionResponse = null;
    private String errorMessage = null;
    private String telkomselToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telkomsel);

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
        //track page telkomsel cash
        mMidtransSDK.trackEvent(AnalyticsEventName.PAGE_TCASH);

        // setup home fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        telkomselCashFragment = new InstructionTelkomselCashFragment();

        fragmentTransaction.add(R.id.instruction_container, telkomselCashFragment, HOME_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
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
        mToolbar.setTitle(""); // disable default Text
        setSupportActionBar(mToolbar);
    }

    /**
     * set data to views.
     */
    private void bindDataToView() {
        textTitle.setText(R.string.telkomsel_cash);
        if (mMidtransSDK != null) {
            if (mMidtransSDK.getSemiBoldText() != null) {
                mButtonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mMidtransSDK.getSemiBoldText()));
            }
            mButtonConfirmPayment.setOnClickListener(this);
            buttonBack.setOnClickListener(this);
            textTotalAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(mMidtransSDK.getTransactionRequest().getAmount())));
        } else {
            SdkUIFlowUtil.showToast(TelkomselCashActivity.this, getString(R.string.error_something_wrong));
            finish();
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {

            if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {
                performTransaction();

            } else {
                setResultCode(RESULT_OK);
                setResultAndFinish();
            }

        } else if (view.getId() == R.id.btn_back) {
            onBackPressed();
        }
    }

    /**
     * Performs the validation and if satisfies the required condition then it will either start
     * Telkomsel Cash payment procedure.
     */
    private void performTransaction() {
        //track telkomsel cash confirm payment
        mMidtransSDK.trackEvent(AnalyticsEventName.PAGE_TCASH);

        if (telkomselCashFragment != null && !telkomselCashFragment.isDetached()) {

            telkomselToken = telkomselCashFragment.getTelkomselToken();

            if (TextUtils.isEmpty(telkomselToken)) {
                SdkUIFlowUtil.showToast(TelkomselCashActivity.this, getString(R.string.error_empty_tcash_token_field));
            }
        }


        final MidtransSDK midtransSDK = MidtransSDK.getInstance();

        if (midtransSDK != null) {

            SdkUIFlowUtil.showProgressDialog(TelkomselCashActivity.this, getString(R.string.processing_payment), false);
            transactionUsingTelkomsel(midtransSDK);

        } else {
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
    }

    private void transactionUsingTelkomsel(final MidtransSDK midtransSDK) {
        midtransSDK.paymentUsingTelkomselEcash(midtransSDK.readAuthenticationToken(),
                telkomselToken, new TransactionCallback() {
                    @Override
                    public void onSuccess(TransactionResponse response) {
                        SdkUIFlowUtil.hideProgressDialog();
                        mTransactionResponse = response;

                        if (response != null) {
                            setUpTransactionStatusFragment(response);
                        } else {
                            SdkUIFlowUtil.showToast(TelkomselCashActivity.this, SOMETHING_WENT_WRONG);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
                        SdkUIFlowUtil.hideProgressDialog();
                        mTransactionResponse = response;
                        errorMessage = getString(R.string.error_message_invalid_input_telkomsel);

                        if (response != null && response.getStatusCode().equals(getString(R.string.failed_code_400))) {
                            setUpTransactionStatusFragment(response);
                        } else {
                            SdkUIFlowUtil.showToast(TelkomselCashActivity.this, getString(R.string.error_message_invalid_input_telkomsel));
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        SdkUIFlowUtil.hideProgressDialog();
                        errorMessage = getString(R.string.error_message_invalid_input_telkomsel);
                        SdkUIFlowUtil.showToast(TelkomselCashActivity.this, errorMessage);
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

        initPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_TELKOMSEL_CASH, false);
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
        setResultAndFinish(this.mTransactionResponse, this.errorMessage);
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
