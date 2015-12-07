package id.co.veritrans.sdk.activities;

import android.content.Intent;
import android.os.Bundle;
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
import id.co.veritrans.sdk.fragments.MandiriClickPayFragment;
import id.co.veritrans.sdk.models.MandiriClickPayModel;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by shivam on 11/3/15.
 */
public class MandiriClickPayActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String DENY = "202";
    private MandiriClickPayFragment mMandiriClickPayFragment = null;
    private Button mButtonConfirmPayment = null;
    private Toolbar mToolbar = null;
    private TextViewFont mTextViewAppli = null;
    private TextViewFont mTextViewInput1 = null;
    private TextViewFont mTextViewInput2 = null;
    private TextViewFont mTextViewInput3 = null;
    private CollapsingToolbarLayout mCollapsingToolbarLayout = null;
    private VeritransSDK mVeritransSDK = null;


    public static final String HOME_FRAGMENT = "home";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";


    // for result
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private int RESULT_CODE = RESULT_CANCELED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mandiri_clickpay);

        mVeritransSDK = VeritransSDK.getVeritransSDK();

        if (mVeritransSDK == null) {
            SdkUtil.showSnackbar(MandiriClickPayActivity.this, Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }


        initializeViews();
        bindDataToView();
        setUpHomeFragment();

    }


    private void initializeViews() {

        mButtonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        mTextViewInput1 = (TextViewFont) findViewById(R.id.text_input_1);
        mTextViewInput2 = (TextViewFont) findViewById(R.id.text_input_2);
        mTextViewInput3 = (TextViewFont) findViewById(R.id.text_input_3);

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mButtonConfirmPayment.setOnClickListener(this);
    }

    private void bindDataToView() {
        mTextViewInput1.setText("");
        mTextViewInput2.setText("" + mVeritransSDK.getTransactionRequest().getAmount());
        mTextViewInput3.setText("" + SdkUtil.generateRandomNumber());
    }

    private void setUpHomeFragment() {

        // setup  fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mMandiriClickPayFragment = new MandiriClickPayFragment();

        fragmentTransaction.add(R.id.mandiri_clickpay_container,
                mMandiriClickPayFragment);
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

    public void onChangeOfDebitCardNumer(CharSequence text) {

        if (text != null && text.length() > 0) {
            String cardNumber = text.toString().trim().replace(" ", "");

            if (cardNumber.length() > 10) {
                mTextViewInput1.setText(cardNumber.substring(cardNumber.length() - 10,
                        cardNumber.length()));
            } else {
                mTextViewInput1.setText(cardNumber);
            }
        } else {
            mTextViewInput1.setText("");
        }
    }

    /**
     * Handles the click of confirm payment button based on following 2 conditions.
     * <p/>
     * 1) if current fragment is home fragment then it will start payment execution.
     * 2) if current fragment is status fragment  then it will send result back to {@link
     * PaymentMethodsActivity}.
     *
     * @param view
     */
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {

            if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {

                validateInformation();

            } else {
                if(mButtonConfirmPayment.getText().toString().equalsIgnoreCase(getString(R.string.retry))) {
                    finish();
                }else {
                    RESULT_CODE = RESULT_OK;
                    onBackPressed();
                }
            }
        }
    }


    private void validateInformation() {

        if (mMandiriClickPayFragment != null && !mMandiriClickPayFragment.isDetached()) {

            String challengeToken = mMandiriClickPayFragment.getChallengeToken();
            String debitCardNumber = mMandiriClickPayFragment.getDebitCardNumber();

            if (!TextUtils.isEmpty(challengeToken) && !TextUtils.isEmpty(debitCardNumber)) {

                debitCardNumber = debitCardNumber.replace(" ", "");

                if (debitCardNumber.length() < 16 || !SdkUtil.isValidCardNumber(debitCardNumber)) {
                    SdkUtil.showSnackbar(MandiriClickPayActivity.this,
                            getString(R.string.validation_message_invalid_card_no));
                } else if (challengeToken.trim().length() != 6) {
                    SdkUtil.showSnackbar(MandiriClickPayActivity.this,
                            getString(R.string.validation_message_invalid_token_no));
                } else {

                    MandiriClickPayModel mandiriClickPayModel = new MandiriClickPayModel();
                    mandiriClickPayModel.setCardNumber(debitCardNumber);
                    mandiriClickPayModel.setInput1(mTextViewInput1.getText().toString());
                    mandiriClickPayModel.setInput2(mTextViewInput2.getText().toString());
                    mandiriClickPayModel.setInput3(mTextViewInput3.getText().toString());
                    mandiriClickPayModel.setToken(challengeToken);

                    SdkUtil.showProgressDialog(MandiriClickPayActivity.this, getString(R.string.processing_payment), false);
                    makeTransaction(mandiriClickPayModel);

                }

            } else {
                SdkUtil.showSnackbar(
                        MandiriClickPayActivity.this,
                        "Please fill up information properly."
                );
            }

        } else {
            SdkUtil.showSnackbar(MandiriClickPayActivity.this, Constants
                    .ERROR_SOMETHING_WENT_WRONG);
            finish();
        }

    }

    /**
     * execute payment code and on success set status fragment to show payment information.
     * and in onFailure displays error message.
     *
     * @param mandiriClickPayModel
     */
    private void makeTransaction(MandiriClickPayModel mandiriClickPayModel) {


        mVeritransSDK.paymentUsingMandiriClickPay(MandiriClickPayActivity.this,
                mandiriClickPayModel, new TransactionCallback() {

                    @Override
                    public void onFailure(String errorMessage, TransactionResponse
                            transactionResponse) {

                        MandiriClickPayActivity.this.transactionResponse = transactionResponse;
                        MandiriClickPayActivity.this.errorMessage = errorMessage;


                        Logger.e("Error is ", "" + errorMessage);

                        if (transactionResponse != null
                                && transactionResponse.getStatusCode().contains(DENY)) {
                            setUpTransactionStatusFragment(transactionResponse);
                        }


                        SdkUtil.hideProgressDialog();
                        if (errorMessage != null) {
                            SdkUtil.showSnackbar(MandiriClickPayActivity.this, errorMessage);
                        }
                    }

                    @Override
                    public void onSuccess(TransactionResponse transactionResponse) {
                        SdkUtil.hideProgressDialog();

                        MandiriClickPayActivity.this.transactionResponse = transactionResponse;

                        if (transactionResponse != null) {
                            setUpTransactionStatusFragment(transactionResponse);
                        } else {
                            SdkUtil.showSnackbar(MandiriClickPayActivity.this,
                                    SOMETHING_WENT_WRONG);
                        }

                    }
                });

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
                BankTransactionStatusFragment.newInstance(transactionResponse,
                        Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY);

        // setup transaction status fragment
        fragmentTransaction.replace(R.id.mandiri_clickpay_container,
                bankTransactionStatusFragment, STATUS_FRAGMENT);
        fragmentTransaction.addToBackStack(STATUS_FRAGMENT);
        fragmentTransaction.commit();
    }


    /**
     * send result back to  {@link PaymentMethodsActivity} and finish current activity.
     */
    private void setResultAndFinish() {
        Intent data = new Intent();
        data.putExtra(Constants.TRANSACTION_RESPONSE, transactionResponse);
        data.putExtra(Constants.TRANSACTION_ERROR_MESSAGE, errorMessage);
        setResult(RESULT_CODE, data);
        finish();
    }


    /**
     * in case of transaction failure it will change the text of confirm payment button to 'RETRY'
     */
    public void activateRetry() {

        if (mButtonConfirmPayment != null) {
            mButtonConfirmPayment.setText(getResources().getString(R.string.retry));
        }
    }
}