package com.midtrans.sdk.uikit.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.fragments.BankTransferFragment;
import com.midtrans.sdk.uikit.fragments.InstructionKiosonFragment;
import com.midtrans.sdk.uikit.fragments.KiosonPaymentFragment;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 8/26/16.
 */
public class KiosonActivity extends BaseActivity implements View.OnClickListener {
    public static final String HOME_FRAGMENT = "home";
    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "transaction_status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public String currentFragment = "home";

    private TextView textViewAmount = null;
    private FancyButton buttonConfirmPayment = null;
    private TextView textViewTitle = null;
    private MidtransSDK midtransSDK = null;
    private Toolbar toolbar = null;
    private InstructionKiosonFragment instructionKiosonFragment = null;
    private TransactionResponse transactionResponse = null;
    private String errorMessage = null;

    private int position = Constants.PAYMENT_METHOD_KIOSON;
    private java.lang.String TAG = "KiosonActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kioson);

        midtransSDK = MidtransSDK.getInstance();

        // get position of selected payment method
        Intent data = getIntent();
        if (data != null) {
            position = data.getIntExtra(getString(R.string.position), Constants
                    .PAYMENT_METHOD_KIOSON);
        } else {
            SdkUIFlowUtil.showToast(KiosonActivity.this, getString(R.string.error_something_wrong));
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
        //track page kioson
        midtransSDK.trackEvent(AnalyticsEventName.PAGE_KIOSON);

        // setup home fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        instructionKiosonFragment = new InstructionKiosonFragment();

        fragmentTransaction.add(R.id.instruction_container, instructionKiosonFragment, HOME_FRAGMENT);
        fragmentTransaction.commit();

        currentFragment = HOME_FRAGMENT;
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

        try {
            MidtransSDK midtransSDK = MidtransSDK.getInstance();

            if (midtransSDK != null && midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                drawable.setColorFilter(
                        midtransSDK.getColorTheme().getPrimaryDarkColor(),
                        PorterDuff.Mode.SRC_ATOP);
            }
        } catch (Exception e) {
            Log.d(TAG, "render toolbar:" + e.getMessage());
        }

        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragment.equals(STATUS_FRAGMENT) || currentFragment.equals(PAYMENT_FRAGMENT)) {
                    setResultCode(RESULT_OK);
                    setResultAndFinish();
                } else {
                    onBackPressed();
                }
            }
        });
    }

    private void bindDataToView() {
        textViewTitle.setText(getString(R.string.kioson));
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
            if (currentFragment.equalsIgnoreCase(HOME_FRAGMENT)) {
                performTransaction();
            } else {
                setResultCode(RESULT_OK);
                setResultAndFinish();
            }
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
        buttonConfirmPayment.setText(getString(R.string.done));

        initPaymentStatus(transactionResponse, errorMessage, Constants.PAYMENT_METHOD_KIOSON, false);
    }

    private void setUpTransactionFragment(final TransactionResponse
                                                  transactionResponse) {

        if (!midtransSDK.getUIKitCustomSetting().isShowPaymentStatus()) {
            setResultCode(RESULT_OK);
            setResultAndFinish();
            return;
        }

        if (transactionResponse != null) {
            // setup transaction fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            KiosonPaymentFragment kiosonPaymentFragment =
                    KiosonPaymentFragment.newInstance(transactionResponse);
            fragmentTransaction.replace(R.id.instruction_container,
                    kiosonPaymentFragment, PAYMENT_FRAGMENT);
            fragmentTransaction.addToBackStack(PAYMENT_FRAGMENT);
            fragmentTransaction.commit();
            buttonConfirmPayment.setText(getString(R.string.complete_payment_kioson));
            ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
            if (merchantLogo != null) {
                merchantLogo.setVisibility(View.INVISIBLE);
            }
            currentFragment = PAYMENT_FRAGMENT;
        } else {
            SdkUIFlowUtil.showToast(KiosonActivity.this, SOMETHING_WENT_WRONG);
            onBackPressed();
        }
    }

    private void performTransaction() {
        //track kioson confirm payment
        midtransSDK.trackEvent(AnalyticsEventName.BTN_CONFIRM_PAYMENT);

        SdkUIFlowUtil.showProgressDialog(KiosonActivity.this, getString(R.string.processing_payment),
                false);

        //Execute transaction
        midtransSDK.paymentUsingKiosan(midtransSDK.readAuthenticationToken(), new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                //track page status pending
                MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_PENDING);

                SdkUIFlowUtil.hideProgressDialog();

                if (response != null) {
                    KiosonActivity.this.transactionResponse = response;
                    setUpTransactionFragment(response);
                } else {
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                //track page status failed
                MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);

                KiosonActivity.this.errorMessage = getString(R.string.message_payment_failed);
                KiosonActivity.this.transactionResponse = response;
                SdkUIFlowUtil.hideProgressDialog();

                try {
                    if (response != null && response.getStatusCode().equals(getString(R.string.failed_code_400))) {
                        setUpTransactionStatusFragment(response);
                    } else {
                        SdkUIFlowUtil.showToast(KiosonActivity.this, "" + errorMessage);
                    }
                } catch (NullPointerException ex) {
                    Logger.e(TAG, "transaction error is " + ex.getMessage());
                }
            }

            @Override
            public void onError(Throwable error) {
                //track page status failed
                MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_STATUS_FAILED);

                SdkUIFlowUtil.hideProgressDialog();

                try {
                    KiosonActivity.this.errorMessage = getString(R.string.message_payment_failed);
                    SdkUIFlowUtil.showToast(KiosonActivity.this, "" + errorMessage);
                } catch (NullPointerException ex) {
                    Logger.e(TAG, "transaction error is " + ex.getMessage());
                }
            }
        });
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
            return;
        }
        super.onBackPressed();
    }
}
