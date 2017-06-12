package com.midtrans.sdk.uikit.activities;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.MandiriClickPayModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.fragments.MandiriClickPayFragment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

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
    private FancyButton mButtonConfirmPayment = null;
    private Toolbar mToolbar = null;
    private DefaultTextView mTextTitle, mTextTotalAmount;
    private MidtransSDK mMidtransSDK = null;
    // for result
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mandiri_clickpay);

        mMidtransSDK = MidtransSDK.getInstance();

        if (mMidtransSDK == null) {
            SdkUIFlowUtil.showToast(MandiriClickPayActivity.this, Constants
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

        mButtonConfirmPayment = (FancyButton) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTextTitle = (DefaultTextView) findViewById(R.id.text_title);
        mTextTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);
        initializeTheme();

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        prepareToolbar();
        mButtonConfirmPayment.setOnClickListener(this);
    }

    private void prepareToolbar() {
        try {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_back);
            MidtransSDK midtransSDK = MidtransSDK.getInstance();
            if (midtransSDK != null && midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                drawable.setColorFilter(
                        midtransSDK.getColorTheme().getPrimaryDarkColor(),
                        PorterDuff.Mode.SRC_ATOP);
            }
            mToolbar.setNavigationIcon(drawable);
        } catch (Exception e) {
            Log.e(TAG, "rendering theme:" + e.getMessage());
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void bindDataToView() {
        mTextTitle.setText(getString(R.string.mandiri_click_pay));
        if (mMidtransSDK != null) {
            if (mMidtransSDK.getSemiBoldText() != null) {
                mButtonConfirmPayment.setCustomTextFont(mMidtransSDK.getSemiBoldText());
            }
            mTextTotalAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(mMidtransSDK.getTransactionRequest().getAmount())));
        }
    }

    private void setUpHomeFragment() {

        //track page mandiri click pay
        mMidtransSDK.trackEvent(AnalyticsEventName.PAGE_MANDIRI_CLICKPAY);

        // setup  fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mMandiriClickPayFragment = new MandiriClickPayFragment();

        fragmentTransaction.add(R.id.instruction_container,
                mMandiriClickPayFragment);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
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

                //track Mandiri Clickpay confirm payment
                mMidtransSDK.trackEvent(AnalyticsEventName.BTN_CONFIRM_PAYMENT);

                validateInformation();

            } else {
                if (mButtonConfirmPayment.getText().toString().equalsIgnoreCase(getString(R.string.retry))) {
                    //finish();
                    Logger.i("on retry pressed");
                    setResultAndFinish();
                } else {
                    setResultCode(RESULT_OK);
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
                    SdkUIFlowUtil.showToast(MandiriClickPayActivity.this,
                            getString(R.string.validation_message_invalid_card_no));
                } else if (challengeToken.trim().length() != 6) {
                    SdkUIFlowUtil.showToast(MandiriClickPayActivity.this,
                            getString(R.string.validation_message_invalid_token_no));
                } else {

                    MandiriClickPayModel mandiriClickPayModel = new MandiriClickPayModel();
                    mandiriClickPayModel.setCardNumber(debitCardNumber);
                    mandiriClickPayModel.setInput1(mMandiriClickPayFragment.getInput1());
                    mandiriClickPayModel.setInput2(mMandiriClickPayFragment.getInput2());
                    mandiriClickPayModel.setInput3(mMandiriClickPayFragment.getInput3());
                    mandiriClickPayModel.setToken(challengeToken);

                    SdkUIFlowUtil.showProgressDialog(MandiriClickPayActivity.this, getString(R.string.processing_payment), false);
                    makeTransaction(mandiriClickPayModel);

                }

            } else {
                SdkUIFlowUtil.showToast(
                        MandiriClickPayActivity.this, getString(R.string.validation_message_please_fillup_form)
                );
            }

        } else {
            SdkUIFlowUtil.showToast(MandiriClickPayActivity.this, getString(R.string.error_something_wrong));
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
                        //track page status success
                        MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_SUCCESS);

                        SdkUIFlowUtil.hideProgressDialog();

                        MandiriClickPayActivity.this.transactionResponse = response;
                        if (transactionResponse != null) {
                            setUpTransactionStatusFragment(transactionResponse);
                        } else {
                            SdkUIFlowUtil.showToast(MandiriClickPayActivity.this,
                                    SOMETHING_WENT_WRONG);
                        }
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
                        //track page status failed
                        MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);

                        SdkUIFlowUtil.hideProgressDialog();
                        MandiriClickPayActivity.this.transactionResponse = response;
                        MandiriClickPayActivity.this.errorMessage = getString(R.string.message_payment_failed);

                        if (transactionResponse != null && (transactionResponse.getStatusCode().contains(DENY)
                                || transactionResponse.getStatusCode().equals(getString(R.string.failed_code_400))
                        )) {
                            setUpTransactionStatusFragment(transactionResponse);
                        }
                    }

                    @Override
                    public void onError(Throwable error) {
                        //track page status failed
                        MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);

                        SdkUIFlowUtil.hideProgressDialog();
                        MandiriClickPayActivity.this.errorMessage = getString(R.string.message_payment_failed);
                        Logger.e(TAG, "Error is" + error.getMessage());
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

        Drawable closeIcon = ContextCompat.getDrawable(this, R.drawable.ic_close);
        closeIcon.setColorFilter(ContextCompat.getColor(this, R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        mToolbar.setNavigationIcon(closeIcon);
        setSupportActionBar(mToolbar);

        initPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY, false);
    }


    /**
     * send result back to  {@link PaymentMethodsActivity} and finish current activity.
     */
    private void setResultAndFinish() {
        setResultAndFinish(transactionResponse, errorMessage);
    }


    /**
     * in case of transaction failure it will change the text of confirm payment button to 'RETRY'
     */
    public void activateRetry() {

        if (mButtonConfirmPayment != null) {
            mButtonConfirmPayment.setText(getResources().getString(R.string.retry));
        }
    }

    @Override
    public void onBackPressed() {
        if (currentFragment.equals(STATUS_FRAGMENT)) {
            if (mButtonConfirmPayment.getText().toString().equalsIgnoreCase(getString(R.string.retry))) {
                Logger.i("on retry pressed");
                setResultAndFinish();
            } else {
                setResultCode(RESULT_OK);
                setResultAndFinish();
            }
        } else {
            super.onBackPressed();
        }
    }
}