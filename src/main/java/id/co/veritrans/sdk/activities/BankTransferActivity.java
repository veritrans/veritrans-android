package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.Subscribe;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.fragments.BankTransactionStatusFragment;
import id.co.veritrans.sdk.fragments.BankTransferFragment;
import id.co.veritrans.sdk.fragments.BankTransferPaymentFragment;
import id.co.veritrans.sdk.fragments.MandiriBillPayFragment;
import id.co.veritrans.sdk.models.TransactionDetails;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.utilities.Utils;

/**
 * Created to show and handle bank transfer and mandiri bill pay details.
 * To handle these two payments method we have created a fragment and depending upon the payment
 * method selected
 * in previous screen we set respective fragment to handle that payment flow.
 * <p/>
 * <p/>
 * It has -
 * {@link BankTransferFragment} home fragment - an initial fragment which contains an instruction.
 * {@link MandiriBillPayFragment} - used to handle mandiri bill payment
 * {@link BankTransferPaymentFragment} - used to handle bank transfer
 * {@link BankTransactionStatusFragment} - used to display status of transaction.
 * <p/>
 * <p/>
 * <p/>
 * It displays order id and amount in co-ordinated layout below action bar.
 * It contains an edit Text to take email id as input from user, instruction about payment flow and
 * a button to confirm payment.
 * <p/>
 * Created by shivam on 10/26/15.
 */
public class BankTransferActivity extends BaseActivity implements View.OnClickListener, TransactionBusCallback {

    public static final String HOME_FRAGMENT = "home";
    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";

    private TextView mTextViewOrderId = null;
    private TextView mTextViewAmount = null;
    private Button mButtonConfirmPayment = null;
    private AppBarLayout mAppBarLayout = null;
    private TextView mTextViewTitle = null;

    private VeritransSDK mVeritransSDK = null;
    private Toolbar mToolbar = null;

    private BankTransferFragment bankTransferFragment = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private CollapsingToolbarLayout mCollapsingToolbarLayout = null;

    private int position = Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT;
    private int RESULT_CODE = RESULT_CANCELED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer);

        mVeritransSDK = VeritransSDK.getVeritransSDK();

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(getString(R.string.position), Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT);
        } else {
            SdkUtil.showSnackbar(BankTransferActivity.this, getString(R.string.error_something_wrong));
            finish();
        }

        initializeView();
        bindDataToView();

        initializeTheme();
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        if (toolbarLayout != null) {
            toolbarLayout.setContentScrimColor(mVeritransSDK.getThemeColor());
        }
        setUpHomeFragment();
        if(!VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        if(VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().unregister(this);
        }
        super.onDestroy();
    }


    /**
     * set up {@link BankTransferFragment} to display payment instructions.
     */
    private void setUpHomeFragment() {
        // setup home fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        bankTransferFragment = new BankTransferFragment();
        Bundle bundle = new Bundle();
        if (position == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT) {
            bundle.putString(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_MANDIRI_BILL);
            bankTransferFragment.setArguments(bundle);
        } else if (position == Constants.BANK_TRANSFER_PERMATA){
            bundle.putString(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_PERMATA);
            bankTransferFragment.setArguments(bundle);
        } else if(position == Constants.BANK_TRANSFER_BCA) {
            bundle.putString(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_BCA);
            bankTransferFragment.setArguments(bundle);

        }
        fragmentTransaction.add(R.id.bank_transfer_container,
                bankTransferFragment, HOME_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
    }

    @Override
    public void onBackPressed() {

        //enable this code if wants to travers backward in fragment back stack

        /*FragmentManager fragmentManager = getSupportFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if (count > 1) {
            fragmentManager.popBackStack();
        } else {
            finish();
        }*/

        setResultAndFinish();
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

        mTextViewOrderId = (TextView) findViewById(R.id.text_order_id);
        mTextViewAmount = (TextView) findViewById(R.id.text_amount);
        mTextViewTitle = (TextView) findViewById(R.id.text_title);
        mButtonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);


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

            mTextViewAmount.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(mVeritransSDK.getTransactionRequest().getAmount())));
            mTextViewOrderId.setText("" + mVeritransSDK.getTransactionRequest().getOrderId());
            if (mVeritransSDK.getSemiBoldText() != null) {
                mButtonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mVeritransSDK.getSemiBoldText()));
            }
            mButtonConfirmPayment.setOnClickListener(this);

            if (position == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT) {
                mTextViewTitle.setText(getResources().getString(R.string.mandiri_bill_payment));
            } else if(position == Constants.BANK_TRANSFER_BCA){
                mTextViewTitle.setText(getResources().getString(R.string.activity_bank_transfer_bca));
            } else if( position == Constants.BANK_TRANSFER_PERMATA) {
                mTextViewTitle.setText(getResources().getString(R.string.activity_bank_transfer_permata));
            }

        } else {
            SdkUtil.showSnackbar(BankTransferActivity.this, getString(R.string.error_something_wrong));
            Logger.e(BankTransferActivity.class.getSimpleName(), Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
    }


    /**
     * Handles the click of confirm payment button based on following 3 conditions.
     * <p/>
     * 1) if current fragment is home fragment then it will start payment execution.
     * 2) if current fragment is payment fragment then it will display transaction status details.
     * 3) if current fragment is status fragment  then it will send result back to {@link
     * PaymentMethodsActivity}.
     *
     * @param view  clicked view
     */
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {

            if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {

                performTransaction();

            } else if (currentFragment.equalsIgnoreCase(PAYMENT_FRAGMENT)) {

                mAppBarLayout.setExpanded(true);

                if (transactionResponse != null) {
                    setUpTransactionStatusFragment(transactionResponse);
                } else {
                    RESULT_CODE = RESULT_OK;
                    SdkUtil.showSnackbar(BankTransferActivity.this, SOMETHING_WENT_WRONG);
                    onBackPressed();
                }
            } else {
                RESULT_CODE = RESULT_OK;
                onBackPressed();
            }

        }
    }


    /**
     * Displays status of transaction from {@link TransactionResponse} object.
     *
     * @param transactionResponse   response of the transaction call
     */
    private void setUpTransactionStatusFragment(final TransactionResponse
                                                        transactionResponse) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        currentFragment = STATUS_FRAGMENT;
        mButtonConfirmPayment.setText(R.string.done);

        mCollapsingToolbarLayout.setVisibility(View.GONE);
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        setSupportActionBar(mToolbar);

        BankTransactionStatusFragment bankTransactionStatusFragment =
                BankTransactionStatusFragment.newInstance(transactionResponse, position);

        // setup transaction status fragment
        fragmentTransaction.replace(R.id.bank_transfer_container,
                bankTransactionStatusFragment, STATUS_FRAGMENT);
        fragmentTransaction.addToBackStack(STATUS_FRAGMENT);
        fragmentTransaction.commit();
    }


    /**
     * If selected payment method is mandiri bill pay then it will set {@link
     * MandiriBillPayFragment} to handle it.
     * if selected payment method is bank transfer then it will set {@link
     * BankTransferPaymentFragment} to manage it.
     *
     * @param transactionResponse   response of the transaction call
     */
    private void setUpTransactionFragment(final TransactionResponse
                                                  transactionResponse) {
        if (transactionResponse != null) {
            // setup transaction fragment
            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (position == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT) {
                MandiriBillPayFragment bankTransferPaymentFragment =
                        MandiriBillPayFragment.newInstance(transactionResponse);
                fragmentTransaction.replace(R.id.bank_transfer_container,
                        bankTransferPaymentFragment, PAYMENT_FRAGMENT);
            } else if (position == Constants.BANK_TRANSFER_PERMATA){
                BankTransferPaymentFragment bankTransferPaymentFragment = BankTransferPaymentFragment.newInstance(transactionResponse, BankTransferInstructionActivity.TYPE_PERMATA);
                fragmentTransaction.replace(R.id.bank_transfer_container, bankTransferPaymentFragment, PAYMENT_FRAGMENT);
            } else if(position == Constants.BANK_TRANSFER_BCA) {
                BankTransferPaymentFragment bankTransferPaymentFragment = BankTransferPaymentFragment.newInstance(transactionResponse, BankTransferInstructionActivity.TYPE_BCA);
                fragmentTransaction.replace(R.id.bank_transfer_container,
                        bankTransferPaymentFragment, PAYMENT_FRAGMENT);

            }

            fragmentTransaction.addToBackStack(PAYMENT_FRAGMENT);
            fragmentTransaction.commit();

            currentFragment = PAYMENT_FRAGMENT;
            mButtonConfirmPayment.setText(R.string.complete_payment_at_atm);

        } else {
            SdkUtil.showSnackbar(BankTransferActivity.this, SOMETHING_WENT_WRONG);
            onBackPressed();
        }
    }


    /**
     * Performs the validation and if satisfies the required condition then it will either start
     * mandiri bill pay flow or bank transfer flow depending on selected payment method.
     *
     * {@see }
     *
     */
    private void performTransaction() {

        // for sending instruction on email only if email-Id is entered.
        if (bankTransferFragment != null && !bankTransferFragment.isDetached()) {

            String emailId = bankTransferFragment.getEmailId();
            if (!TextUtils.isEmpty(emailId) && SdkUtil.isEmailValid(emailId)) {
                mVeritransSDK.getTransactionRequest().getCustomerDetails().setEmail(emailId.trim());
            } else if (!TextUtils.isEmpty(emailId) && emailId.trim().length() > 0) {
                SdkUtil.showSnackbar(BankTransferActivity.this, getString(R.string.error_invalid_email_id));
                return;
            }
        }


        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            //transaction details
            TransactionDetails transactionDetails =
                    new TransactionDetails("" + mVeritransSDK.getTransactionRequest().getAmount(),
                            mVeritransSDK.getTransactionRequest().getOrderId());

            SdkUtil.showProgressDialog(BankTransferActivity.this, getString(R.string.processing_payment), false);

            if (position == Constants.BANK_TRANSFER_PERMATA) {
                bankTransferTransaction(veritransSDK);
            } else if(position == Constants.BANK_TRANSFER_BCA) {
                bcaBankTransferTransaction(veritransSDK);
            } else {
                mandiriBillPayTransaction(veritransSDK);
            }


        } else {
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
    }

    /**
     * it performs BCA bank transfer and in onSuccess() of callback method it will call {@link
     * #setUpTransactionFragment(TransactionResponse)} to set appropriate fragment.
     *
     * @param veritransSDK  Veritrans SDK instance
     */
    private void bcaBankTransferTransaction(VeritransSDK veritransSDK) {
        veritransSDK.paymentUsingBcaBankTransfer();
    }


    /**
     * it performs bank transfer and in onSuccess() of callback method it will call {@link
     * #setUpTransactionFragment(TransactionResponse)} to set appropriate fragment.
     *
     * @param veritransSDK  Veritrans SDK instance
     */
    private void bankTransferTransaction(VeritransSDK veritransSDK) {
        veritransSDK.paymentUsingPermataBank();
    }


    /**
     * It execute mandiri bill payment transaction and in onSuccess() of callback method it will
     * call {@link #setUpTransactionFragment(TransactionResponse)} to set appropriate fragment.
     *
     * @param veritransSDK  Veritrans SDK instance
     */
    private void mandiriBillPayTransaction(VeritransSDK veritransSDK) {
        veritransSDK.paymentUsingMandiriBillPay();
    }

    /**
     * @return position of selected payment method.
     */
    public int getPosition() {
        return position;
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
        data.putExtra(getString(R.string.transaction_response), transactionResponse);
        data.putExtra(getString(R.string.error_transaction), errorMessage);
        setResult(RESULT_CODE, data);
        finish();
    }

    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent event) {
        SdkUtil.hideProgressDialog();
        if (event.getResponse() != null) {
            transactionResponse = event.getResponse();
            mAppBarLayout.setExpanded(true);
            setUpTransactionFragment(event.getResponse());
        } else {
            onBackPressed();
        }
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent event) {
        BankTransferActivity.this.errorMessage = event.getMessage();
        SdkUtil.hideProgressDialog();
        SdkUtil.showSnackbar(BankTransferActivity.this, "" + errorMessage);
    }

    @Subscribe
    @Override
    public void onEvent(TransactionFailedEvent event) {
        try {
            BankTransferActivity.this.errorMessage = event.getMessage();
            BankTransferActivity.this.transactionResponse = event.getResponse();

            SdkUtil.hideProgressDialog();
            SdkUtil.showSnackbar(BankTransferActivity.this, "" + errorMessage);
        } catch (NullPointerException ex) {
            Logger.e("transaction error is " + errorMessage);
        }
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent event) {
        BankTransferActivity.this.errorMessage = getString(R.string.no_network_msg);
        SdkUtil.hideProgressDialog();
        SdkUtil.showSnackbar(BankTransferActivity.this, "" + errorMessage);
    }
}