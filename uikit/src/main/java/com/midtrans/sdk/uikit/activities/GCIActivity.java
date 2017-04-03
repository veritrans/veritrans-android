package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.fragments.GCIPaymentFragment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 12/7/16.
 */

public class GCIActivity extends BaseActivity implements View.OnClickListener {

    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public String currentFragment = "home";

    private TextView textViewAmount = null;
    private FancyButton buttonConfirmPayment = null;
    private TextView textViewTitle = null;
    private GCIPaymentFragment paymentFragment;
    private MidtransSDK midtransSDK = null;
    private Toolbar toolbar = null;

    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;

    private int position = Constants.PAYMENT_METHOD_INDOMARET;
    private int retryNumber = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gci);

        midtransSDK = MidtransSDK.getInstance();

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(getString(R.string.position), Constants
                    .PAYMENT_METHOD_GCI);
        } else {
            SdkUIFlowUtil.showToast(this, getString(R.string.error_something_wrong));
            finish();
        }

        initializeView();
        bindDataToView();
        setUpHomeFragment();
    }

    /**
     * set up {@link } to display payment instructions.
     */
    private void setUpHomeFragment() {
        //track page telkomsel cash
        midtransSDK.trackEvent(AnalyticsEventName.PAGE_GCI);

        // setup home fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        paymentFragment = new GCIPaymentFragment();
        fragmentTransaction.add(R.id.instruction_container, paymentFragment, PAYMENT_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = PAYMENT_FRAGMENT;
    }

    private void initializeView() {
        textViewAmount = (TextView) findViewById(R.id.text_amount);
        textViewTitle = (TextView) findViewById(R.id.text_title);
        buttonConfirmPayment = (FancyButton) findViewById(R.id.btn_confirm_payment);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        initializeTheme();
        //setup tool bar
        toolbar.setTitle(""); // disable default Text
        setSupportActionBar(toolbar);
        prepareToolbar();
    }

    private void prepareToolbar() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_back);
        MidtransSDK midtransSDK =MidtransSDK.getInstance();
        if (midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
            drawable.setColorFilter(
                    midtransSDK.getColorTheme().getPrimaryDarkColor(),
                    PorterDuff.Mode.SRC_ATOP);
        }
        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void bindDataToView() {
        textViewTitle.setText(getString(R.string.title_gci));
        if (midtransSDK != null) {
            textViewAmount.setText(getString(R.string.prefix_money,
                    Utils.getFormattedAmount(midtransSDK.getTransactionRequest().getAmount())));
            if (midtransSDK.getSemiBoldText() != null) {
                buttonConfirmPayment.setCustomTextFont(midtransSDK.getSemiBoldText());
            }
            buttonConfirmPayment.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {
            if (currentFragment.equalsIgnoreCase(STATUS_FRAGMENT)) {
                setResultCode(RESULT_OK);
                setResultAndFinish();
            } else {
                performTransaction();
            }
        }
    }

    private void setUpTransactionStatusFragment(final TransactionResponse
                                                        transactionResponse) {

        currentFragment = STATUS_FRAGMENT;
        buttonConfirmPayment.setText(getString(R.string.done));

        initPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_GCI, false);
    }


    private void performTransaction() {
        //track page telkomsel cash
        midtransSDK.trackEvent(AnalyticsEventName.BTN_CONFIRM_PAYMENT);


        if (paymentFragment.checkFormValidity()) {
            SdkUIFlowUtil.showProgressDialog(this, getString(R.string.processing_payment),
                    false);

            String cardNumber = paymentFragment.getCardNumber();
            String password = paymentFragment.getPassword();

            //Execute transaction
            midtransSDK.paymentUsingGCI(midtransSDK.readAuthenticationToken(), cardNumber, password, new TransactionCallback() {
                @Override
                public void onSuccess(TransactionResponse response) {
                    //track page status success
                    MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_SUCCESS);

                    SdkUIFlowUtil.hideProgressDialog();
                    if (response != null) {
                        transactionResponse = response;
                        setUpTransactionStatusFragment(response);
                    } else {
                        onBackPressed();
                    }
                }

                @Override
                public void onFailure(TransactionResponse response, String reason) {
                    //track page status failed
                    MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);

                    errorMessage = getString(R.string.message_payment_failed);
                    transactionResponse = response;
                    SdkUIFlowUtil.hideProgressDialog();
                    if (retryNumber > 0) {
                        buttonConfirmPayment.setText(getString(R.string.retry));
                        SdkUIFlowUtil.showToast(GCIActivity.this, "" + errorMessage);
                        --retryNumber;
                    } else {
                        setUpTransactionStatusFragment(response);
                    }
                }

                @Override
                public void onError(Throwable error) {
                    //track page status failed
                    MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);

                    SdkUIFlowUtil.hideProgressDialog();
                    errorMessage = getString(R.string.message_payment_failed);
                    SdkUIFlowUtil.showToast(GCIActivity.this, "" + errorMessage);
                }
            });
        }

    }

    public int getPosition() {
        return position;
    }

    /**
     * in case of transaction failure it will change the text of confirm payment button to 'RETRY'
     */
    public void activateRetry() {

        if (buttonConfirmPayment != null) {
            buttonConfirmPayment.setText(getResources().getString(R.string.retry));
        }
    }

    /**
     * send result back to  {@link PaymentMethodsActivity} and finish current activity.
     */
    private void setResultAndFinish() {
        setResultAndFinish(transactionResponse, errorMessage);
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
