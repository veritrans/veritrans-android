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
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.MandiriClickPayModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.BankTransactionStatusFragment;
import com.midtrans.sdk.uikit.fragments.MandiriClickPayFragment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

/**
 * Created by shivam on 11/3/15.
 */
public class MandiriClickPayActivity extends BaseActivity implements View.OnClickListener {

    public static final String DENY = "202";
    public static final String HOME_FRAGMENT = "home";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    private static final String TAG = "MandiriClickPayActivity";
    public String currentFragment = "home";
    private MandiriClickPayFragment mMandiriClickPayFragment = null;
    private Button mButtonConfirmPayment = null;
    private Toolbar mToolbar = null;
    private TextView mTextViewAppli = null;
    private TextView mTextViewInput1 = null;
    private TextView mTextViewInput2 = null;
    private TextView mTextViewInput3 = null;
    private DefaultTextView mTextTitle, mTextTotalAmount, mTextOrderId;
    private AppBarLayout appBar = null;
    private ImageView logo = null;
    private CollapsingToolbarLayout mCollapsingToolbarLayout = null;
    private MidtransSDK mMidtransSDK = null;
    // for result
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private int RESULT_CODE = RESULT_CANCELED;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mandiri_clickpay);

        mMidtransSDK = MidtransSDK.getInstance();

        if (mMidtransSDK == null) {
            SdkUIFlowUtil.showSnackbar(MandiriClickPayActivity.this, Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }


        initializeViews();
        bindDataToView();
        setUpHomeFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initializeViews() {

        mButtonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        mTextViewInput1 = (TextView) findViewById(R.id.text_input_1);
        mTextViewInput2 = (TextView) findViewById(R.id.text_input_2);
        mTextViewInput3 = (TextView) findViewById(R.id.text_input_3);
        mTextTitle = (DefaultTextView) findViewById(R.id.text_title);
        mTextOrderId = (DefaultTextView) findViewById(R.id.text_order_id);
        mTextTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);
        logo = (ImageView) findViewById(R.id.merchant_logo);
        appBar = (AppBarLayout) findViewById(R.id.main_appbar);
        initializeTheme();

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mButtonConfirmPayment.setOnClickListener(this);

    }

    private void bindDataToView() {
        mTextTitle.setText(getString(R.string.mandiri_click_pay));
        if (mMidtransSDK != null) {
            if (mMidtransSDK.getSemiBoldText() != null) {
                mButtonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), mMidtransSDK.getSemiBoldText()));
            }
            mTextOrderId.setText(mMidtransSDK.getTransactionRequest().getOrderId());
            mTextTotalAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(mMidtransSDK.getTransactionRequest().getAmount())));
        }
        mTextViewInput1.setText("");
        mTextViewInput2.setText("" + mMidtransSDK.getTransactionRequest().getAmount());
        mTextViewInput3.setText("" + SdkUIFlowUtil.generateRandomNumber());
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
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (currentFragment.equals(STATUS_FRAGMENT)) {
                if (mButtonConfirmPayment.getText().toString().equalsIgnoreCase(getString(R.string.retry))) {
                    //finish();
                    Logger.i("on retry pressed");
                    setResultAndFinish();
                } else {
                    RESULT_CODE = RESULT_OK;
                    setResultAndFinish();
                }
            } else {
                onBackPressed();
            }
        }
        return true;
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

                validateInformation();

            } else {
                if (mButtonConfirmPayment.getText().toString().equalsIgnoreCase(getString(R.string.retry))) {
                    //finish();
                    Logger.i("on retry pressed");
                    setResultAndFinish();
                } else {
                    RESULT_CODE = RESULT_OK;
                    setResultAndFinish();
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

                if (debitCardNumber.length() < 16 || !SdkUIFlowUtil.isValidCardNumber(debitCardNumber)) {
                    SdkUIFlowUtil.showSnackbar(MandiriClickPayActivity.this,
                            getString(R.string.validation_message_invalid_card_no));
                } else if (challengeToken.trim().length() != 6) {
                    SdkUIFlowUtil.showSnackbar(MandiriClickPayActivity.this,
                            getString(R.string.validation_message_invalid_token_no));
                } else {

                    MandiriClickPayModel mandiriClickPayModel = new MandiriClickPayModel();
                    mandiriClickPayModel.setCardNumber(debitCardNumber);
                    mandiriClickPayModel.setInput1(mTextViewInput1.getText().toString());
                    mandiriClickPayModel.setInput2(mTextViewInput2.getText().toString());
                    mandiriClickPayModel.setInput3(mTextViewInput3.getText().toString());
                    mandiriClickPayModel.setToken(challengeToken);

                    SdkUIFlowUtil.showProgressDialog(MandiriClickPayActivity.this, getString(R.string.processing_payment), false);
                    makeTransaction(mandiriClickPayModel);

                }

            } else {
                SdkUIFlowUtil.showSnackbar(
                        MandiriClickPayActivity.this, getString(R.string.validation_message_please_fillup_form)
                );
            }

        } else {
            SdkUIFlowUtil.showSnackbar(MandiriClickPayActivity.this, getString(R.string.error_something_wrong));
            finish();
        }

    }

    /**
     * execute payment code and on success set status fragment to show payment information. and in
     * onFailure displays error message.
     *
     * @param mandiriClickPayModel Mandiri click pay request object
     */
    private void makeTransaction(MandiriClickPayModel mandiriClickPayModel) {
        mMidtransSDK.paymentUsingMandiriClickPay(mMidtransSDK.readAuthenticationToken(),
                mandiriClickPayModel.getCardNumber(), mandiriClickPayModel.getToken(),
                mandiriClickPayModel.getInput3(), new TransactionCallback() {
                    @Override
                    public void onSuccess(TransactionResponse response) {
                        SdkUIFlowUtil.hideProgressDialog();

                        MandiriClickPayActivity.this.transactionResponse = response;
                        if (transactionResponse != null) {
                            setUpTransactionStatusFragment(transactionResponse);
                        } else {
                            SdkUIFlowUtil.showSnackbar(MandiriClickPayActivity.this,
                                    SOMETHING_WENT_WRONG);
                        }
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
                        SdkUIFlowUtil.hideProgressDialog();
                        MandiriClickPayActivity.this.transactionResponse = response;
                        MandiriClickPayActivity.this.errorMessage = reason;

                        if (transactionResponse != null
                                && transactionResponse.getStatusCode().contains(DENY)) {
                            setUpTransactionStatusFragment(transactionResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        SdkUIFlowUtil.hideProgressDialog();
                        MandiriClickPayActivity.this.errorMessage = error.getMessage();
                        Logger.e(TAG, "Error is" + errorMessage);
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
        appBar.setExpanded(false, false);
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
        data.putExtra(getString(R.string.transaction_response), transactionResponse);
        data.putExtra(getString(R.string.error_transaction), errorMessage);
        setResult(RESULT_OK, data);
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