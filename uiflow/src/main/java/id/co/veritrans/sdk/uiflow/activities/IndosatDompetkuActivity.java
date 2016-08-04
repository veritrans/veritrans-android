package id.co.veritrans.sdk.uiflow.activities;

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
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.fragments.BankTransactionStatusFragment;
import id.co.veritrans.sdk.uiflow.fragments.BankTransferFragment;
import id.co.veritrans.sdk.uiflow.fragments.InstructionIndosatFragment;
import id.co.veritrans.sdk.uiflow.utilities.SdkUIFlowUtil;

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
public class IndosatDompetkuActivity extends BaseActivity implements View.OnClickListener, TransactionBusCallback {

    public static final String HOME_FRAGMENT = "home";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";

    private Button mButtonConfirmPayment = null;
    private TextView mTextViewTitle = null;

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

        mVeritransSDK = VeritransSDK.getVeritransSDK();

        initializeView();
        bindDataToView();

        setUpHomeFragment();
        if (!VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().unregister(this);
        }
    }


    /**
     * set up {@link BankTransferFragment} to display payment instructions.
     */
    private void setUpHomeFragment() {
        // setup home fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mIndosatFragment = new InstructionIndosatFragment();

        fragmentTransaction.add(R.id.indosat_container, mIndosatFragment, HOME_FRAGMENT);
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
        mTextViewTitle = (TextView) findViewById(R.id.text_title);
        mButtonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        logo = (ImageView) findViewById(R.id.merchant_logo);
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

        if (mVeritransSDK != null) {
            if (mVeritransSDK.getSemiBoldText() != null) {
                mButtonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mVeritransSDK.getSemiBoldText()));
            }
            mButtonConfirmPayment.setOnClickListener(this);
            mTextViewTitle.setText(getResources().getString(R.string.indosat_dompetku));

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
     * @param view  clicked view
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


        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

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
     * @param veritransSDK  Veritrans SDK instance
     */
    private void transactionUsingIndosat(final VeritransSDK veritransSDK) {
        veritransSDK.snapPaymentUsingIndosatDompetku(veritransSDK.readAuthenticationToken(), phoneNumber);
    }


    /**
     * Displays status of transaction from {@link TransactionResponse} object.
     *
     * @param transactionResponse   response of transaction call
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
        fragmentTransaction.replace(R.id.indosat_container,
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

    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent event) {
        SdkUIFlowUtil.hideProgressDialog();
        mTransactionResponse = event.getResponse();

        if (event.getResponse() != null) {
            setUpTransactionStatusFragment(event.getResponse());
        } else {
            SdkUIFlowUtil.showSnackbar(IndosatDompetkuActivity.this, SOMETHING_WENT_WRONG);
            finish();
        }
    }

    @Subscribe
    @Override
    public void onEvent(TransactionFailedEvent event) {
        mTransactionResponse = event.getResponse();
        IndosatDompetkuActivity.this.errorMessage = event.getMessage();
        try {
            SdkUIFlowUtil.hideProgressDialog();
            SdkUIFlowUtil.showSnackbar(IndosatDompetkuActivity.this, "" + errorMessage);
        } catch (NullPointerException ex) {
            Logger.e("transaction error is " + errorMessage);
        }
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent event) {
        IndosatDompetkuActivity.this.errorMessage = getString(R.string.no_network_msg);
        try {
            SdkUIFlowUtil.hideProgressDialog();
            SdkUIFlowUtil.showSnackbar(IndosatDompetkuActivity.this, "" + errorMessage);
        } catch (NullPointerException ex) {
            Logger.e("transaction error is " + errorMessage);
        }
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent event) {
        IndosatDompetkuActivity.this.errorMessage = event.getMessage();
        try {
            SdkUIFlowUtil.hideProgressDialog();
            SdkUIFlowUtil.showSnackbar(IndosatDompetkuActivity.this, "" + errorMessage);
        } catch (NullPointerException ex) {
            Logger.e("transaction error is " + errorMessage);
        }
    }
}