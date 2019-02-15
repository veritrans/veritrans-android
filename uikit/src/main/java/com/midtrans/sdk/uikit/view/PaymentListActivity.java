package com.midtrans.sdk.uikit.view;

import com.google.android.material.appbar.AppBarLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.corekit.MidtransSdk;
import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.utilities.Constants;
import com.midtrans.sdk.uikit.MidtransKitFlow;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.BaseActivity;
import com.midtrans.sdk.uikit.base.callback.PaymentResult;
import com.midtrans.sdk.uikit.base.model.MessageInfo;
import com.midtrans.sdk.uikit.utilities.ActivityHelper;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.widget.BoldTextView;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

public class KitLandingActivity extends BaseActivity {

    private PaymentResult<PaymentResponse> callback;
    private boolean isCreditCardOnly = false;
    private boolean isBankTransferOnly = false;
    private boolean isBCAKlikpay = false;
    private boolean isKlikBca = false;
    private boolean isMandiriClickPay = false;
    private boolean isMandiriEcash = false;
    private boolean isCimbClicks = false;
    private boolean isBriEpay = false;
    private boolean isTelkomselCash = false;
    private boolean isIndomaret = false;
    private boolean isGopay = false;
    private boolean isDanamonOnline = false;
    private boolean isAkulaku = false;
    private boolean isAlfamart = false;
    private boolean isBcaVa = false;
    private boolean isBniVa = false;
    private boolean isPermataVa = false;
    private boolean isOtherVa = false;
    private String token = null;
    private CheckoutTransaction checkoutTransaction = null;

    @PaymentType
    private String paymentType;
    private boolean isThrowableFromNetworkRequest = false;
    private Throwable throwableFromNetworkRequest;

    private AlertDialog alertDialog;

    private LinearLayout progressContainer;
    private ImageView progressImage;
    private TextView progressMessage;
    private LinearLayout maintenanceContainer;
    private BoldTextView maintenanceTitle;
    private DefaultTextView maintenanceMessage;
    private FancyButton maintenanceButton;
    private Toolbar toolbar;
    private AppBarLayout appbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kit_landing);

        initToolbarAndView();
        initializeTheme();
        getIntentDataFromMidtransKitFlow();
        checkDataBeforeStartMidtransSdk();
    }

    private void initToolbarAndView() {
        toolbar = findViewById(R.id.toolbar);
        appbar = findViewById(R.id.main_appbar);
        progressContainer = findViewById(R.id.progress_container);
        progressImage = findViewById(R.id.progress_bar_image);
        progressMessage = findViewById(R.id.progress_bar_message);
        maintenanceContainer = findViewById(R.id.maintenance_container);
        maintenanceTitle = findViewById(R.id.text_maintenance_title);
        maintenanceMessage = findViewById(R.id.text_maintenance_message);
        maintenanceButton = findViewById(R.id.button_maintenance_retry);

        setSupportActionBar(toolbar);
        Ion.with(progressImage)
                .load(ActivityHelper.getImagePath(this) + R.drawable.midtrans_loader);
    }

    @SuppressWarnings("unchecked")
    private void getIntentDataFromMidtransKitFlow() {
        callback = (PaymentResult<PaymentResponse>) getIntent().getSerializableExtra(MidtransKitFlow.INTENT_EXTRA_CALLBACK);
        checkoutTransaction = (CheckoutTransaction) getIntent().getSerializableExtra(MidtransKitFlow.INTENT_EXTRA_TRANSACTION);
        paymentType = getIntent().getStringExtra(MidtransKitFlow.INTENT_EXTRA_DIRECT);
        token = getIntent().getStringExtra(MidtransKitFlow.INTENT_EXTRA_TOKEN);

        isCreditCardOnly = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_CREDIT_CARD_ONLY, false);
        isBankTransferOnly = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_ONLY, false);
        isGopay = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_GOPAY, false);
        isBCAKlikpay = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BCA_KLIKPAY, false);
        isKlikBca = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_KLIK_BCA, false);
        isMandiriClickPay = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_MANDIRI_CLICKPAY, false);
        isMandiriEcash = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_MANDIRI_ECASH, false);
        isCimbClicks = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_CIMB_CLICKS, false);
        isBriEpay = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BRI_EPAY, false);
        isTelkomselCash = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_TELKOMSEL_CASH, false);
        isIndomaret = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_INDOMARET, false);
        isDanamonOnline = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_DANAMON_ONLINE, false);
        isAkulaku = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_AKULAKU, false);
        isAlfamart = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_ALFAMART, false);
        isBcaVa = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_BCA, false);
        isBniVa = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_BNI, false);
        isPermataVa = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_PERMATA, false);
        isOtherVa = getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_OTHER, false);
    }

    private void checkDataBeforeStartMidtransSdk() {
        showProgress();
        if (checkoutTransaction == null && token != null) {
            startGettingPaymentInfoWithMidtransSdk(token);
        } else if (checkoutTransaction != null && token == null) {
            startCheckoutWithMidtransSdk(checkoutTransaction);
        } else {
            setOnFailedCallback(new Throwable("Please check your input data, MidtransKit is not started."));
        }
    }

    private void startCheckoutWithMidtransSdk(CheckoutTransaction checkoutTransaction) {
        MidtransSdk
                .getInstance()
                .checkoutWithTransaction(checkoutTransaction, new MidtransCallback<CheckoutWithTransactionResponse>() {
                    @Override
                    public void onSuccess(CheckoutWithTransactionResponse data) {
                        if (data != null) {
                            if (data.getToken() != null) {
                                startGettingPaymentInfoWithMidtransSdk(data.getToken());
                            } else {
                                if (data.getErrorMessages().get(0) != null) {
                                    String errorMessage = MessageHelper.createMessageWhenCheckoutFailed(KitLandingActivity.this, data.getErrorMessages());
                                    showErrorMessage(errorMessage, true);
                                } else {
                                    showErrorMessage(Constants.MESSAGE_ERROR_FAILURE_RESPONSE, true);
                                }
                            }
                        } else {
                            showErrorMessage(Constants.MESSAGE_ERROR_FAILURE_RESPONSE, true);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        showFallbackErrorPage(throwable, true);
                    }
                });
    }

    private void startGettingPaymentInfoWithMidtransSdk(String token) {
        this.token = token;
        MidtransSdk
                .getInstance()
                .getPaymentInfo(this.token, new MidtransCallback<PaymentInfoResponse>() {
                    @Override
                    public void onSuccess(PaymentInfoResponse data) {
                        if (data != null) {

                        } else {
                            showErrorMessage(null, false);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        showFallbackErrorPage(throwable, false);
                    }
                });
    }

    private void showErrorMessage(String message, boolean isCheckout) {
        if (!isFinishing()) {
            if (!isCheckout) {
                message = getString(R.string.error_snap_transaction_details);
            }
            String finalMessage = message;
            alertDialog = new AlertDialog
                    .Builder(this)
                    .setMessage(finalMessage)
                    .setPositiveButton(R.string.btn_retry, (dialog, which) -> {
                        if (isCheckout) {
                            startCheckoutWithMidtransSdk(checkoutTransaction);
                        } else {
                            startGettingPaymentInfoWithMidtransSdk(token);
                        }
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.btn_cancel, (dialog, which) -> {
                        isThrowableFromNetworkRequest = true;
                        throwableFromNetworkRequest = new Throwable(finalMessage);
                        dialog.dismiss();
                        onBackPressed();
                    })
                    .create();
            alertDialog.show();
        }
    }

    private void showFallbackErrorPage(Throwable error, boolean isCheckout) {
        throwableFromNetworkRequest = error;
        isThrowableFromNetworkRequest = true;
        MessageInfo messageInfo = MessageHelper.createMessageOnError(error, this);

        maintenanceTitle.setText(messageInfo.getTitleMessage());
        maintenanceMessage.setText(messageInfo.getDetailsMessage());
        maintenanceButton.setText(getString(R.string.try_again));
        maintenanceButton.setOnClickListener(v -> {
            if (isCheckout) {
                checkDataBeforeStartMidtransSdk();
            } else {
                startGettingPaymentInfoWithMidtransSdk(token);
            }
            showMaintenance(false);
        });
        showMaintenance(true);
    }

    private void showMaintenance(boolean show) {
        if (show) {
            appbar.setVisibility(View.GONE);
            maintenanceContainer.setVisibility(View.VISIBLE);
        } else {
            appbar.setVisibility(View.VISIBLE);
            maintenanceContainer.setVisibility(View.GONE);
        }
    }

    private void showProgress() {
        progressContainer.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressContainer.setVisibility(View.GONE);
    }

    private void setOnFailedCallback(Throwable throwable) {
        callback.onFailed(throwable);
        if (isThrowableFromNetworkRequest) {
            onBackPressed();
            isThrowableFromNetworkRequest = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (isThrowableFromNetworkRequest) {
            setOnFailedCallback(throwableFromNetworkRequest);
        }
        super.onBackPressed();
    }
}