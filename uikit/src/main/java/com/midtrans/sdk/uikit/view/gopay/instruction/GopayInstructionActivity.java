package com.midtrans.sdk.uikit.view.gopay.instruction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.RelativeLayout;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.payment.PaymentStatusResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.ActivityHelper;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.utilities.MidtransKitHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.view.PaymentListActivity;
import com.midtrans.sdk.uikit.view.gopay.result.GopayResultActivity;
import com.midtrans.sdk.uikit.widget.FancyButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class GopayInstructionActivity extends BasePaymentActivity implements GopayInstructionContract {

    private static final String TAG = GopayInstructionActivity.class.getSimpleName();
    private final String GOJEK_PACKAGE_NAME = "com.gojek.app";

    private FancyButton buttonDownload;
    private View buttonPrimaryLayout;

    private GopayInstructionPresenter presenter;

    private boolean isTablet, isGojekInstalled, isAlreadyGotResponse;
    private Boolean isGojekInstalledWhenPaused;
    private int attempt;
    private int goPayIntentCode;
    private GopayResponse gopayResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_ewallet_gopay_instruction);
        showProgressLayout();
        initProperties();
        initLayout();
        initActionButton();
        initTheme();
        hideProgressLayout();
    }

    private void initLayout() {
        ViewStub stub = findViewById(R.id.gopay_layout_stub);
        if (isTablet) {
            stub.setLayoutResource(R.layout.layout_payment_gopay_tablet);
        } else {
            stub.setLayoutResource(isGojekInstalled ? R.layout.layout_payment_gopay : R.layout.layout_payment_gopay_install);
        }
        stub.inflate();
        buttonPrimaryLayout = findViewById(R.id.layout_primary_button);
        setTitle(getString(R.string.gopay));
    }

    private void initActionButton() {
        if (isGojekInstalled || isTablet) {
            buttonCompletePayment.setOnClickListener(v -> {
                if (isAlreadyGotResponse) {
                    openDeepLink(gopayResponse.getDeeplinkUrl());
                } else {
                    startGoPayPayment();
                }
            });
            buttonCompletePayment.setTextBold();
            buttonCompletePayment.setText(getString(R.string.gopay_confirm_button));
            buttonCompletePayment.setIconResource(R.drawable.ic_gopay_white);
            buttonCompletePayment.setIconPosition(FancyButton.POSITION_RIGHT);
        } else {
            //hide confirm button and adjust item details to bottom of screen
            buttonPrimaryLayout.setVisibility(View.GONE);
            findViewById(R.id.primary_button_separator).setVisibility(View.GONE);
            View itemDetail = findViewById(R.id.container_item_details);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            itemDetail.setLayoutParams(layoutParams);
            buttonDownload = findViewById(R.id.button_download_gojek);
            setTextColor(buttonDownload);
            setIconColorFilter(buttonDownload);
            buttonDownload.setOnClickListener(v -> MidtransKitHelper.openAppInPlayStore(GopayInstructionActivity.this, GOJEK_PACKAGE_NAME));
        }
    }

    private void openDeepLink(String deepLinkUrl) {
        MessageHelper.showToast(this, getString(R.string.redirecting_to_gopay));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(deepLinkUrl));
        startActivityForResult(intent, Constants.INTENT_CODE_GOPAY);
    }

    private void startGoPayPayment() {
        showProgressLayout();
        presenter.startGopayEwalletPayment(paymentInfoResponse.getToken());
    }

    private void startGettingPaymentResult() {
        presenter.getPaymentStatus(paymentInfoResponse.getToken());
    }

    private void showConfirmationDialog(String message) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(GopayInstructionActivity.this, R.style.AlertDialogCustom)
                    .setPositiveButton(R.string.text_yes, (dialog1, which) -> {
                        if (!GopayInstructionActivity.this.isFinishing()) {
                            dialog1.dismiss();
                            finishPayment(RESULT_OK, gopayResponse);
                        }
                    })
                    .setNegativeButton(R.string.text_no, (dialog12, which) -> {
                        if (!GopayInstructionActivity.this.isFinishing()) {
                            dialog12.dismiss();
                        }
                    })
                    .setTitle(R.string.cancel_transaction)
                    .setMessage(message)
                    .create();
            dialog.show();
        } catch (Exception e) {
            Logger.error(TAG, "showDialog:" + e.getMessage());
        }
    }

    /**
     * Check whether the transaction response from server is valid or not
     * Valid if both deeplink URL and qr code URL aren't empty, or at least one of them is not,
     * depending on which one that will be used
     *
     * @param response transaction response
     * @return validity of response
     */
    private boolean isResponseValid(GopayResponse response) {
        if (response == null) {
            return false;
        } else {
            if (TextUtils.isEmpty(response.getDeeplinkUrl()) && !isTablet) {
                return false;
            }
            if (TextUtils.isEmpty(response.getQrCodeUrl()) && isTablet) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
        setPrimaryBackgroundColor(buttonDownload);
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        isTablet = ActivityHelper.getDeviceType(this).equals(ActivityHelper.TYPE_TABLET);
        isGojekInstalled = MidtransKitHelper.isAppInstalled(this, GOJEK_PACKAGE_NAME);
        presenter = new GopayInstructionPresenter(this, paymentInfoResponse);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGojekInstalled = MidtransKitHelper.isAppInstalled(this, GOJEK_PACKAGE_NAME);
        if (isGojekInstalledWhenPaused != null && isGojekInstalledWhenPaused != isGojekInstalled) {
            recreate();
        }
        if (goPayIntentCode == Constants.INTENT_CODE_GOPAY) {
            startGettingPaymentResult();
        }
    }

    @Override
    protected void onPause() {
        isGojekInstalledWhenPaused = isGojekInstalled;
        super.onPause();
    }

    @Override
    public void onGetPaymentStatusError(Throwable throwable) {
        hideProgressLayout();
        showOnErrorPaymentStatusMessage(throwable);
    }

    @Override
    public void onGetPaymentStatusSuccess(PaymentStatusResponse response) {
        gopayResponse.setStatusCode(response.getStatusCode());
        gopayResponse.setStatusMessage(response.getStatusMessage());
        gopayResponse.setTransactionStatus(response.getTransactionStatus());
        gopayResponse.setTransactionTime(response.getTransactionTime());
        setCallbackOrSendToStatusPage();
    }

    private void setCallbackOrSendToStatusPage() {
        if (isShowPaymentStatusView()) {
            startResultActivity(Constants.INTENT_CODE_PAYMENT_RESULT, PaymentListHelper.convertTransactionStatus(gopayResponse));
        } else {
            finishPayment(RESULT_OK, gopayResponse);
        }
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        hideProgressLayout();
        gopayResponse = (GopayResponse) response;
        if (isResponseValid(gopayResponse)) {
            if (gopayResponse.getStatusCode().equals(Constants.STATUS_CODE_400)) {
                if (attempt < Constants.MAX_ATTEMPT) {
                    attempt += 1;
                    MessageHelper.showToast(this, getString(R.string.error_gopay_transaction));
                } else {
                    setCallbackOrSendToStatusPage();
                }
            } else {
                if (isTablet) {
                    Intent intent = new Intent(this, GopayResultActivity.class);
                    intent.putExtra(GopayResultActivity.EXTRA_PAYMENT_STATUS, gopayResponse);
                    intent.putExtra(PaymentListActivity.EXTRA_PAYMENT_INFO, paymentInfoResponse);
                    startActivityForResult(intent, Constants.INTENT_CODE_PAYMENT_STATUS);
                } else {
                    isAlreadyGotResponse = true;
                    openDeepLink(gopayResponse.getDeeplinkUrl());
                }
            }
        } else {
            finishPayment(RESULT_OK, gopayResponse);
        }
    }

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        showOnErrorPaymentStatusMessage(error);
    }

    @Override
    public void onNullInstanceSdk() {
        setResult(Constants.RESULT_SDK_NOT_AVAILABLE);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_CODE_PAYMENT_STATUS && resultCode == RESULT_OK) {
            startGettingPaymentResult();
        } else if (requestCode == Constants.INTENT_CODE_GOPAY) {
            this.goPayIntentCode = requestCode;
        } else if (requestCode == Constants.INTENT_CODE_PAYMENT_RESULT) {
            finishPayment(RESULT_OK, gopayResponse);
        }
    }

    @Override
    public void onBackPressed() {
        if (isDetailShown) {
            displayOrHideItemDetails();
        } else if (isAlreadyGotResponse) {
            showConfirmationDialog(getString(R.string.confirm_gopay_deeplink));
        } else {
            super.onBackPressed();
        }
    }
}