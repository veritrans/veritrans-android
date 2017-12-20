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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkUtil;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.BankTransferFragment;
import com.midtrans.sdk.uikit.fragments.BankTransferPaymentFragment;
import com.midtrans.sdk.uikit.fragments.MandiriBillPayFragment;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created to show and handle bank transfer and mandiri bill pay details. To handle these two
 * payments method we have created a fragment and depending upon the payment method selected in
 * previous screen we set respective fragment to handle that payment flow.
 * <p/>
 * <p/>
 * It has - {@link BankTransferFragment} home fragment - an initial fragment which contains an
 * instruction. {@link MandiriBillPayFragment} - used to handle mandiri bill payment {@link
 * BankTransferPaymentFragment} - used to handle bank transfer {@link com.midtrans.sdk.uikit.fragments.PaymentTransactionStatusFragment}
 * - used to display status of transaction.
 * <p/>
 * <p/>
 * <p/>
 * It displays order id and amount in co-ordinated layout below action bar. It contains an edit Text
 * to take email id as input from user, instruction about payment flow and a button to confirm
 * payment.
 * <p/>
 * Created by shivam on 10/26/15.
 */
public class BankTransferActivity extends BaseActivity implements View.OnClickListener {

    public static final String HOME_FRAGMENT = "home";
    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    private static final String TAG = "BankTransferActivity";
    public String currentFragment = "home";

    private TextView mTextViewAmount = null;
    private FancyButton mButtonConfirmPayment = null;
    private TextView mTextViewTitle = null;
    private MidtransSDK mMidtransSDK = null;
    private Toolbar mToolbar = null;

    private BankTransferFragment bankTransferFragment = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private ImageView logo = null;

    private int position = Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer);

        mMidtransSDK = MidtransSDK.getInstance();

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(getString(R.string.position), Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT);
        } else {
            SdkUIFlowUtil.showToast(BankTransferActivity.this, getString(R.string.error_something_wrong));
            finish();
        }

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
        if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
            fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in_back, R.anim.slide_out_back);
        }
        if (position == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT) {
            bankTransferFragment = BankTransferFragment.newInstance(BankTransferInstructionActivity.TYPE_MANDIRI_BILL, 0);
        } else if (position == Constants.BANK_TRANSFER_PERMATA) {
            bankTransferFragment = BankTransferFragment.newInstance(BankTransferInstructionActivity.TYPE_PERMATA, 0);
        } else if (position == Constants.BANK_TRANSFER_BCA) {
            bankTransferFragment = BankTransferFragment.newInstance(BankTransferInstructionActivity.TYPE_BCA, 0);
        } else if (position == Constants.BANK_TRANSFER_BNI) {
            bankTransferFragment = BankTransferFragment.newInstance(BankTransferInstructionActivity.TYPE_BNI, 0);
        } else if (position == Constants.PAYMENT_METHOD_BANK_TRANSFER_ALL_BANK) {
            bankTransferFragment = BankTransferFragment.newInstance(BankTransferInstructionActivity.TYPE_ALL_BANK, 0);
        }
        fragmentTransaction.add(R.id.instruction_container,
                bankTransferFragment, HOME_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
    }

    @Override
    public void onBackPressed() {
        if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {
            super.onBackPressed();
        } else {
            if (currentFragment.equalsIgnoreCase(STATUS_FRAGMENT)) {
                setResultCode(RESULT_OK);
            }
            setResultAndFinish();
        }
    }

    /**
     * initialize all views
     */
    private void initializeView() {

        mTextViewAmount = (TextView) findViewById(R.id.text_amount);
        mTextViewTitle = (TextView) findViewById(R.id.text_title);
        mButtonConfirmPayment = (FancyButton) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        logo = (ImageView) findViewById(R.id.merchant_logo);

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
            Log.d(TAG, e.getMessage());
        }

        mToolbar.setNavigationIcon(drawable);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    /**
     * set data to views.
     */
    private void bindDataToView() {

        if (mMidtransSDK != null) {

            mTextViewAmount.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(mMidtransSDK.getTransactionRequest().getAmount())));
            if (mMidtransSDK.getSemiBoldText() != null) {
                mButtonConfirmPayment.setCustomTextFont(mMidtransSDK.getSemiBoldText());
            }
            mButtonConfirmPayment.setOnClickListener(this);

            if (position == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT) {
                mTextViewTitle.setText(getString(R.string.mandiri_bill_transfer));
            } else if (position == Constants.BANK_TRANSFER_BCA) {
                mTextViewTitle.setText(getString(R.string.bank_bca_transfer));
            } else if (position == Constants.BANK_TRANSFER_PERMATA) {
                mTextViewTitle.setText(getString(R.string.bank_permata_transfer));
            } else if (position == Constants.PAYMENT_METHOD_BANK_TRANSFER_ALL_BANK) {
                mTextViewTitle.setText(getString(R.string.other_bank_transfer));
            } else if (position == Constants.BANK_TRANSFER_BNI) {
                mTextViewTitle.setText(getString(R.string.bank_bni_transfer));
            }

        } else {
            SdkUIFlowUtil.showToast(BankTransferActivity.this, getString(R.string.error_something_wrong));
            Logger.e(BankTransferActivity.class.getSimpleName(), Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }
    }

    /**
     * Handles the click of confirm payment button based on following 3 conditions.
     * <p/>
     * 1) if current fragment is home fragment then it will start payment execution. 2) if current
     * fragment is payment fragment then it will display transaction status details. 3) if current
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
                onBackPressed();
            }
        }
    }

    /**
     * Displays status of transaction from {@link TransactionResponse} object.
     *
     * @param transactionResponse response of the transaction call
     */
    private void setUpTransactionStatusFragment(final TransactionResponse
                                                        transactionResponse) {
        if (!mMidtransSDK.getUIKitCustomSetting().isShowPaymentStatus()) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
            return;
        }
        currentFragment = STATUS_FRAGMENT;
        mButtonConfirmPayment.setText(getString(R.string.done));

        initPaymentStatus(transactionResponse, errorMessage, position, false);

    }


    /**
     * If selected payment method is mandiri bill pay then it will set {@link
     * MandiriBillPayFragment} to handle it. if selected payment method is bank transfer then it
     * will set {@link BankTransferPaymentFragment} to manage it.
     *
     * @param transactionResponse response of the transaction call
     */
    private void setUpTransactionFragment(final TransactionResponse
                                                  transactionResponse) {
        if (transactionResponse != null) {
            // setup transaction fragment
            FragmentManager fragmentManager = getSupportFragmentManager();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                fragmentTransaction.setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.slide_in_back, R.anim.slide_out_back);
            }
            if (position == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT) {
                MandiriBillPayFragment bankTransferPaymentFragment = MandiriBillPayFragment.newInstance(transactionResponse);
                fragmentTransaction.replace(R.id.instruction_container, bankTransferPaymentFragment, PAYMENT_FRAGMENT);
            } else if (position == Constants.BANK_TRANSFER_PERMATA) {
                BankTransferPaymentFragment bankTransferPaymentFragment = BankTransferPaymentFragment.newInstance(transactionResponse, BankTransferInstructionActivity.TYPE_PERMATA);
                fragmentTransaction.replace(R.id.instruction_container, bankTransferPaymentFragment, PAYMENT_FRAGMENT);
            } else if (position == Constants.BANK_TRANSFER_BCA) {
                BankTransferPaymentFragment bankTransferPaymentFragment = BankTransferPaymentFragment.newInstance(transactionResponse, BankTransferInstructionActivity.TYPE_BCA);
                fragmentTransaction.replace(R.id.instruction_container, bankTransferPaymentFragment, PAYMENT_FRAGMENT);
            } else if (position == Constants.PAYMENT_METHOD_BANK_TRANSFER_ALL_BANK) {
                BankTransferPaymentFragment bankTransferPaymentFragment = BankTransferPaymentFragment.newInstance(transactionResponse, BankTransferInstructionActivity.TYPE_ALL_BANK);
                fragmentTransaction.replace(R.id.instruction_container, bankTransferPaymentFragment, PAYMENT_FRAGMENT);
            } else if (position == Constants.BANK_TRANSFER_BNI) {
                BankTransferPaymentFragment bankTransferPaymentFragment = BankTransferPaymentFragment.newInstance(transactionResponse, BankTransferInstructionActivity.TYPE_BNI);
                fragmentTransaction.replace(R.id.instruction_container, bankTransferPaymentFragment, PAYMENT_FRAGMENT);
            }

            fragmentTransaction.addToBackStack(PAYMENT_FRAGMENT);
            fragmentTransaction.commit();

            currentFragment = PAYMENT_FRAGMENT;
            mButtonConfirmPayment.setText(getString(R.string.complete_payment_at_atm));

        } else {
            SdkUIFlowUtil.showToast(BankTransferActivity.this, SOMETHING_WENT_WRONG);
            onBackPressed();
        }
    }


    /**
     * Performs the validation and if satisfies the required condition then it will either start
     * mandiri bill pay flow or bank transfer flow depending on selected payment method.
     * <p>
     * {@see }
     */
    private void performTransaction() {

        // for sending instruction on email only if email-Id is entered.
        if (bankTransferFragment != null && !bankTransferFragment.isDetached()) {

            String emailId = bankTransferFragment.getEmailId();
            if (!TextUtils.isEmpty(emailId) && SdkUIFlowUtil.isEmailValid(emailId)) {
                mMidtransSDK.getTransactionRequest().getCustomerDetails().setEmail(emailId.trim());
            } else if (!TextUtils.isEmpty(emailId) && emailId.trim().length() > 0) {
                SdkUIFlowUtil.showToast(BankTransferActivity.this, getString(R.string.error_invalid_email_id));
                return;
            }
        }


        final MidtransSDK midtransSDK = MidtransSDK.getInstance();

        if (midtransSDK != null) {
            //transaction details
            SdkUIFlowUtil.showProgressDialog(BankTransferActivity.this, getString(R.string.processing_payment), false);

            if (position == Constants.BANK_TRANSFER_PERMATA) {
                permataBankPayTransaction(midtransSDK);
            } else if (position == Constants.BANK_TRANSFER_BCA) {
                bcaBankTransferTransaction(midtransSDK);
            } else if (position == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT) {
                mandiriBillPayTransaction(midtransSDK);
            } else if (position == Constants.BANK_TRANSFER_BNI) {
                bniBankTransferTransaction(midtransSDK);
            } else {
                otherBankTransaction(midtransSDK);
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
     * @param midtransSDK Veritrans SDK instance
     */
    private void bcaBankTransferTransaction(MidtransSDK midtransSDK) {
        midtransSDK.paymentUsingBankTransferBCA(midtransSDK.readAuthenticationToken(),
                midtransSDK.getTransactionRequest().getCustomerDetails().getEmail(), new TransactionCallback() {
                    @Override
                    public void onSuccess(TransactionResponse response) {
                        actionPaymentSuccess(response);
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
                        actionPaymentFailure(response, reason);
                    }

                    @Override
                    public void onError(Throwable error) {
                        actionPaymentError(error);
                    }
                });
    }

    /**
     * it performs bank transfer and in onSuccess() of callback method it will call {@link
     * #setUpTransactionFragment(TransactionResponse)} to set appropriate fragment.
     *
     * @param midtransSDK Veritrans SDK instance
     */
    private void permataBankPayTransaction(MidtransSDK midtransSDK) {
        midtransSDK.paymentUsingBankTransferPermata(midtransSDK.readAuthenticationToken(),
                midtransSDK.getTransactionRequest().getCustomerDetails().getEmail(), new TransactionCallback() {
                    @Override
                    public void onSuccess(TransactionResponse response) {
                        actionPaymentSuccess(response);
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
                        actionPaymentFailure(response, reason);
                    }

                    @Override
                    public void onError(Throwable error) {
                        actionPaymentError(error);
                    }
                });
    }

    /**
     * It execute mandiri bill payment transaction and in onSuccess() of callback method it will
     * call {@link #setUpTransactionFragment(TransactionResponse)} to set appropriate fragment.
     *
     * @param midtransSDK Veritrans SDK instance
     */
    private void mandiriBillPayTransaction(MidtransSDK midtransSDK) {
        midtransSDK.paymentUsingMandiriBillPay(midtransSDK.readAuthenticationToken(),
                midtransSDK.getTransactionRequest().getCustomerDetails().getEmail(), new TransactionCallback() {
                    @Override
                    public void onSuccess(TransactionResponse response) {
                        actionPaymentSuccess(response);
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
                        actionPaymentFailure(response, reason);
                    }

                    @Override
                    public void onError(Throwable error) {
                        actionPaymentError(error);
                    }
                });
    }

    private void otherBankTransaction(MidtransSDK midtransSDK) {
        midtransSDK.paymentUsingBankTransferAllBank(midtransSDK.readAuthenticationToken(),
                SdkUtil.getEmailAddress(midtransSDK.getTransactionRequest()), new TransactionCallback() {
                    @Override
                    public void onSuccess(TransactionResponse response) {
                        actionPaymentSuccess(response);
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
                        actionPaymentFailure(response, reason);
                    }

                    @Override
                    public void onError(Throwable error) {
                        actionPaymentError(error);
                    }
                });
    }

    /**
     * it performs BNI bank transfer and in onSuccess() of callback method it will call {@link
     * #setUpTransactionFragment(TransactionResponse)} to set appropriate fragment.
     *
     * @param midtransSDK Veritrans SDK instance
     */
    private void bniBankTransferTransaction(MidtransSDK midtransSDK) {
        midtransSDK.paymentUsingBankTransferBni(midtransSDK.readAuthenticationToken(),
                midtransSDK.getTransactionRequest().getCustomerDetails().getEmail(), new TransactionCallback() {
                    @Override
                    public void onSuccess(TransactionResponse response) {
                        actionPaymentSuccess(response);
                    }

                    @Override
                    public void onFailure(TransactionResponse response, String reason) {
                        actionPaymentFailure(response, reason);
                    }

                    @Override
                    public void onError(Throwable error) {
                        actionPaymentError(error);
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
        if (transactionResponse != null) {
            data.putExtra(getString(R.string.transaction_response), transactionResponse);
        }
        if (errorMessage != null && !errorMessage.equals("")) {
            data.putExtra(getString(R.string.error_transaction), errorMessage);
        }
        setResult(RESULT_CODE, data);
        finish();
    }

    private void actionPaymentError(Throwable error) {
        SdkUIFlowUtil.hideProgressDialog();

        String errorMessage = MessageUtil.createPaymentErrorMessage(this, error.getMessage(), null);
        BankTransferActivity.this.errorMessage = errorMessage;
        SdkUIFlowUtil.showToast(BankTransferActivity.this, "" + errorMessage);
    }

    private void actionPaymentSuccess(TransactionResponse response) {
        SdkUIFlowUtil.hideProgressDialog();
        if (response != null) {
            transactionResponse = response;
            setUpTransactionFragment(response);
        } else {
            onBackPressed();
        }
    }

    private void actionPaymentFailure(TransactionResponse response, String reason) {
        SdkUIFlowUtil.hideProgressDialog();
        try {
            BankTransferActivity.this.errorMessage = getString(R.string.message_payment_cannot_proccessed);
            BankTransferActivity.this.transactionResponse = response;
            if (response != null && response.getStatusCode().equals(getString(R.string.failed_code_400))) {
                setUpTransactionStatusFragment(response);
            } else {
                SdkUIFlowUtil.showToast(BankTransferActivity.this, "" + errorMessage);
            }
        } catch (NullPointerException ex) {
            Logger.e("transaction error is " + errorMessage);
        }
    }

    public FancyButton getmButtonConfirmPayment() {
        return mButtonConfirmPayment;
    }
}