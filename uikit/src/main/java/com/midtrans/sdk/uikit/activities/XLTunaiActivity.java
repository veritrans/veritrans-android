package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.InstructionXLTunaiFragment;
import com.midtrans.sdk.uikit.fragments.XLTunaiPaymentFragment;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * @author rakawm
 */
public class XLTunaiActivity extends BaseActivity implements View.OnClickListener {
    public static final String HOME_FRAGMENT = "home";
    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";

    public String currentFragment = "home";

    private TextView textViewAmount = null;
    private Button buttonConfirmPayment = null;
    private TextView textViewTitle = null;
    private FancyButton buttonBack;

    private MidtransSDK midtransSDK = null;
    private Toolbar toolbar = null;

    private InstructionXLTunaiFragment instructionXLTunaiFragment = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;
    private int position = Constants.PAYMENT_METHOD_XL_TUNAI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xl_tunai);

        midtransSDK = MidtransSDK.getInstance();

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(getString(R.string.position), Constants.PAYMENT_METHOD_XL_TUNAI);
        } else {
            SdkUIFlowUtil.showToast(XLTunaiActivity.this, getString(R.string.error_something_wrong));
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

    private void setUpHomeFragment() {
        //track page xl tunai
        midtransSDK.trackEvent(AnalyticsEventName.PAGE_XL_TUNAI);

        // setup home fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        instructionXLTunaiFragment = new InstructionXLTunaiFragment();

        fragmentTransaction.add(R.id.instruction_container, instructionXLTunaiFragment, HOME_FRAGMENT);
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

    private void initializeView() {
        textViewAmount = (TextView) findViewById(R.id.text_amount);
        textViewTitle = (TextView) findViewById(R.id.text_title);
        buttonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        buttonBack = (FancyButton) findViewById(R.id.btn_back);

        initializeTheme();

        toolbar.setTitle(""); // disable default Text
        setSupportActionBar(toolbar);
    }

    private void bindDataToView() {
        textViewTitle.setText(getString(R.string.xl_tunai));
        if (midtransSDK != null) {
            textViewAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(midtransSDK.getTransactionRequest().getAmount())));
            if (midtransSDK.getSemiBoldText() != null) {
                buttonConfirmPayment.setTypeface(Typeface.createFromAsset(getAssets(), midtransSDK.getSemiBoldText()));
            }
            buttonConfirmPayment.setOnClickListener(this);
            buttonBack.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {
            if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {

                performTransaction();

            } else if (currentFragment.equalsIgnoreCase(PAYMENT_FRAGMENT)) {
                if (transactionResponse != null) {
                    setUpTransactionStatusFragment(transactionResponse);
                } else {
                    setResultCode(RESULT_OK);
                    SdkUIFlowUtil.showToast(XLTunaiActivity.this, SOMETHING_WENT_WRONG);
                    setResultAndFinish();
                }
            } else {
                setResultCode(RESULT_OK);
                setResultAndFinish();
            }
        } else if (view.getId() == R.id.btn_back) {
            onBackPressed();
        }
    }

    private void setUpTransactionStatusFragment(final TransactionResponse
                                                        transactionResponse) {

        if (!midtransSDK.getUIKitCustomSetting().isShowPaymentStatus()) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
            return;
        }

        currentFragment = STATUS_FRAGMENT;
        buttonConfirmPayment.setText(R.string.done);

        Drawable closeIcon = getResources().getDrawable(R.drawable.ic_close);
        closeIcon.setColorFilter(getResources().getColor(R.color.dark_gray), PorterDuff.Mode.MULTIPLY);
        buttonBack.setIconResource(closeIcon);

        initPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_XL_TUNAI, false);
    }

    private void setUpTransactionFragment(final TransactionResponse
                                                  transactionResponse) {
        if (transactionResponse != null) {
            // setup transaction fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            XLTunaiPaymentFragment xlTunaiPaymentFragment =
                    XLTunaiPaymentFragment.newInstance(transactionResponse);
            fragmentTransaction.replace(R.id.instruction_container,
                    xlTunaiPaymentFragment, PAYMENT_FRAGMENT);
            fragmentTransaction.addToBackStack(PAYMENT_FRAGMENT);
            fragmentTransaction.commit();
            currentFragment = PAYMENT_FRAGMENT;
        } else {
            SdkUIFlowUtil.showToast(XLTunaiActivity.this, getString(R.string.error_something_wrong));
            onBackPressed();
        }
    }

    private void performTransaction() {
        //track xl tunai overview
        midtransSDK.trackEvent(AnalyticsEventName.BTN_CONFIRM_PAYMENT);

        SdkUIFlowUtil.showProgressDialog(XLTunaiActivity.this, getString(R.string.processing_payment), false);
        //Execute transaction
        midtransSDK.paymentUsingXLTunai(midtransSDK.readAuthenticationToken(), new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                SdkUIFlowUtil.hideProgressDialog();

                if (response != null) {
                    transactionResponse = response;
                    setUpTransactionFragment(response);
                } else {
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                SdkUIFlowUtil.hideProgressDialog();
                errorMessage = getString(R.string.message_payment_failed);
                transactionResponse = response;
                if (response != null && response.getStatusCode().equals(getString(R.string.failed_code_400))) {
                    setUpTransactionStatusFragment(response);
                } else {
                    SdkUIFlowUtil.showToast(XLTunaiActivity.this, "" + errorMessage);
                }
            }

            @Override
            public void onError(Throwable error) {
                SdkUIFlowUtil.hideProgressDialog();
                errorMessage = getString(R.string.message_payment_failed);
                SdkUIFlowUtil.showToast(XLTunaiActivity.this, "" + errorMessage);
            }
        });
    }

    public int getPosition() {
        return position;
    }

    public void activateRetry() {

        if (buttonConfirmPayment != null) {
            buttonConfirmPayment.setText(getResources().getString(R.string.retry));
        }
    }

    private void setResultAndFinish() {
        setResultAndFinish(transactionResponse, errorMessage);
    }

    @Override
    public void onBackPressed() {
        if (currentFragment.equals(STATUS_FRAGMENT) || currentFragment.equals(PAYMENT_FRAGMENT)) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
            return;
        }
        super.onBackPressed();
    }
}
