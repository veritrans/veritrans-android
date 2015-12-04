package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.fragments.BankTransactionStatusFragment;
import id.co.veritrans.sdk.fragments.BankTransferFragment;
import id.co.veritrans.sdk.fragments.BankTransferPaymentFragment;
import id.co.veritrans.sdk.fragments.MandiriBillPayFragment;
import id.co.veritrans.sdk.models.TransactionDetails;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.widgets.TextViewFont;

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
public class BankTransferActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String HOME_FRAGMENT = "home";
    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";

    private TextViewFont mTextViewOrderId = null;
    private TextViewFont mTextViewAmount = null;
    private Button mButtonConfirmPayment = null;
    private AppBarLayout mAppBarLayout = null;
    private TextViewFont mTextViewTitle = null;

    private VeritransSDK mVeritransSDK = null;
    private Toolbar mToolbar = null;

    private BankTransferFragment bankTransferFragment = null;
    private TransactionResponse mTransactionResponse = null;
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
            position = data.getIntExtra(Constants.POSITION, Constants
                    .PAYMENT_METHOD_MANDIRI_BILL_PAYMENT);
        } else {
            SdkUtil.showSnackbar(BankTransferActivity.this, Constants.ERROR_SOMETHING_WENT_WRONG);
            finish();
        }

        initializeView();
        bindDataToView();

        setUpHomeFragment();
    }


    /**
     * set up {@link BankTransferFragment} to display payment instructions.
     */
    private void setUpHomeFragment() {
        // setup home fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        bankTransferFragment = new BankTransferFragment();

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

        mTextViewOrderId = (TextViewFont) findViewById(R.id.text_order_id);
        mTextViewAmount = (TextViewFont) findViewById(R.id.text_amount);
        mTextViewTitle = (TextViewFont) findViewById(R.id.text_title);
        mButtonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);


        //setup tool bar
        mToolbar.setTitle(""); // disable default Text
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    /**
     * set data to views.
     */
    private void bindDataToView() {

        if (mVeritransSDK != null) {

            mTextViewAmount.setText(Constants.CURRENCY_PREFIX + " " + mVeritransSDK
                    .getTransactionRequest().getAmount());
            mTextViewOrderId.setText("" + mVeritransSDK.getTransactionRequest().getOrderId());
            mButtonConfirmPayment.setTypeface(mVeritransSDK.getTypefaceOpenSansSemiBold());
            mButtonConfirmPayment.setOnClickListener(this);

            if (position == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT) {
                mTextViewTitle.setText(getResources().getString(R.string.mandiri_bill_payment));
            } else {
                mTextViewTitle.setText(getResources().getString(R.string.bank_transfer));
            }

        } else {
            SdkUtil.showSnackbar(BankTransferActivity.this, Constants.ERROR_SOMETHING_WENT_WRONG);
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
     * @param view
     */
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {

            if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {

                performTrsansaction();

            } else if (currentFragment.equalsIgnoreCase(PAYMENT_FRAGMENT)) {

                mAppBarLayout.setExpanded(true);

                if (mTransactionResponse != null) {
                    setUpTransactionStatusFragment(mTransactionResponse);
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
     * @param transactionResponse
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
     * @param transactionResponse
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
            } else {

                BankTransferPaymentFragment bankTransferPaymentFragment =
                        BankTransferPaymentFragment.newInstance(transactionResponse);
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
    private void performTrsansaction() {

        // for sending instruction on email only if email-Id is entered.
        if (bankTransferFragment != null && !bankTransferFragment.isDetached()) {

            String emailId = bankTransferFragment.getEmailId();
            if (!TextUtils.isEmpty(emailId) && SdkUtil.isEmailValid(emailId)) {
                mVeritransSDK.getTransactionRequest().getCustomerDetails().setEmail(emailId.trim());
            } else if (!TextUtils.isEmpty(emailId) && emailId.trim().length() > 0) {
                SdkUtil.showSnackbar(BankTransferActivity.this, Constants
                        .ERROR_INVALID_EMAIL_ID);
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

            if (position == Constants.PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER) {
                bankTransferTransaction(veritransSDK);
            } else {
                mandiriBillPayTransaction(veritransSDK);
            }


        } else {
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
    }


    /**
     * it performs bank transfer and in onSuccess() of callback method it will call {@link
     * #setUpTransactionFragment(TransactionResponse)} to set appropriate fragment.
     *
     * @param veritransSDK
     */
    private void bankTransferTransaction(VeritransSDK veritransSDK) {


        veritransSDK.paymentUsingPermataBank(BankTransferActivity.this, new TransactionCallback() {


            @Override
            public void onSuccess(TransactionResponse
                                          permataBankTransferResponse) {

                SdkUtil.hideProgressDialog();

                if (permataBankTransferResponse != null) {
                    mTransactionResponse = permataBankTransferResponse;
                    mAppBarLayout.setExpanded(true);
                    setUpTransactionFragment(permataBankTransferResponse);
                } else {
                    onBackPressed();
                }

            }

            @Override
            public void onFailure(String errorMessage, TransactionResponse transactionResponse) {

                try {
                    BankTransferActivity.this.errorMessage = errorMessage;
                    mTransactionResponse = transactionResponse;

                    SdkUtil.hideProgressDialog();
                    SdkUtil.showSnackbar(BankTransferActivity.this, "" + errorMessage);
                } catch (NullPointerException ex) {
                    Logger.e("transaction error is " + errorMessage);
                }
            }
        });
    }


    /**
     * It execute mandiri bill payment transaction and in onSuccess() of callback method it will
     * call {@link #setUpTransactionFragment(TransactionResponse)} to set appropriate fragment.
     *
     * @param veritransSDK
     */
    private void mandiriBillPayTransaction(VeritransSDK veritransSDK) {

        veritransSDK.paymentUsingMandiriBillPay(BankTransferActivity.this, new
                TransactionCallback() {

                    @Override
                    public void onSuccess(TransactionResponse
                                                  mandiriBillPayTransferResponse) {

                        SdkUtil.hideProgressDialog();

                        if (mandiriBillPayTransferResponse != null) {
                            mTransactionResponse = mandiriBillPayTransferResponse;
                            mAppBarLayout.setExpanded(true);
                            setUpTransactionFragment(mandiriBillPayTransferResponse);
                        } else {
                            onBackPressed();
                        }

                    }

                    @Override
                    public void onFailure(String errorMessage, TransactionResponse
                            transactionResponse) {
                        try {
                            SdkUtil.hideProgressDialog();
                            SdkUtil.showSnackbar(BankTransferActivity.this, "" + errorMessage);
                        } catch (NullPointerException ex) {
                            Logger.e("transaction error is " + errorMessage);
                        }
                    }
                });
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
        data.putExtra(Constants.TRANSACTION_RESPONSE, mTransactionResponse);
        data.putExtra(Constants.TRANSACTION_ERROR_MESSAGE, errorMessage);
        setResult(RESULT_CODE, data);
        finish();
    }
}