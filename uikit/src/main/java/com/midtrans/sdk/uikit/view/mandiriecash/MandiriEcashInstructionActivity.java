package com.midtrans.sdk.uikit.view.mandiriecash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriEcashResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.NetworkHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;

import androidx.annotation.Nullable;

public class MandiriEcashInstructionActivity extends BasePaymentActivity implements BasePaymentContract {

    private MandiriEcashInstructionPresenter presenter;
    private MandiriEcashResponse mandiriEcashResponse;

    private boolean isAlreadyGotResponse = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_online_debit_cimb_clicks_instruction);
        initToolbarAndView();
        initProperties();
        initActionButton();
    }

    private void initActionButton() {
        buttonCompletePayment.setOnClickListener(v -> {
            if (isAlreadyGotResponse && mandiriEcashResponse != null) {
                showWebViewPaymentPage(PaymentType.MANDIRI_ECASH, mandiriEcashResponse.getRedirectUrl());
            } else {
                showProgressLayout(getString(R.string.processing_payment));
                presenter.startMandiriEcashPayment(paymentInfoResponse.getToken());
            }
        });
        buttonCompletePayment.setText(getString(R.string.confirm_payment));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.payment_method_mandiri_ecash));
    }

    private void setCallbackOrSendToStatusPage() {
        if (presenter.isShowPaymentStatusPage()) {
            startResultActivity(Constants.INTENT_CODE_PAYMENT_RESULT, PaymentListHelper.convertTransactionStatus(mandiriEcashResponse));
        } else {
            finishPayment(RESULT_OK, mandiriEcashResponse);
        }
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        presenter = new MandiriEcashInstructionPresenter(this, paymentInfoResponse);
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        hideProgressLayout();
        mandiriEcashResponse = (MandiriEcashResponse) response;
        if (mandiriEcashResponse != null) {
            if (NetworkHelper.isPaymentSuccess(mandiriEcashResponse)) {
                showWebViewPaymentPage(PaymentType.MANDIRI_ECASH, mandiriEcashResponse.getRedirectUrl());
            } else {
                setCallbackOrSendToStatusPage();
            }
        } else {
            finishPayment(RESULT_OK, mandiriEcashResponse);
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
        if (requestCode == Constants.INTENT_WEBVIEW_PAYMENT && resultCode == Activity.RESULT_OK) {
            finishPayment(RESULT_OK, mandiriEcashResponse);
        }
    }
}