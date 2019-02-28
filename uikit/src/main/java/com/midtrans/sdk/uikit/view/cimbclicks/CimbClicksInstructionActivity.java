package com.midtrans.sdk.uikit.view.cimbclicks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CimbClicksResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;

import androidx.annotation.Nullable;

public class CimbClicksInstructionActivity extends BasePaymentActivity implements BasePaymentContract {

    private CimbClicksInstructionPresenter presenter;
    private CimbClicksResponse cimbClicksResponse;

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
            if (isAlreadyGotResponse && cimbClicksResponse != null) {
                showWebViewPaymentPage(PaymentType.CIMB_CLICKS, cimbClicksResponse.getRedirectUrl());
            } else {
                showProgressLayout(getString(R.string.processing_payment));
                presenter.startCimbClicksPayment(paymentInfoResponse.getToken());
            }
        });
        buttonCompletePayment.setText(getString(R.string.confirm_payment));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.cimb_clicks));
    }

    private void setCallbackOrSendToStatusPage() {
        if (presenter.isShowPaymentStatusPage()) {
            startResultActivity(Constants.INTENT_CODE_PAYMENT_RESULT, PaymentListHelper.convertTransactionStatus(cimbClicksResponse));
        } else {
            finishPayment(RESULT_OK, cimbClicksResponse);
        }
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        presenter = new CimbClicksInstructionPresenter(this, paymentInfoResponse);
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        hideProgressLayout();
        cimbClicksResponse = (CimbClicksResponse) response;
        if (cimbClicksResponse != null) {
            if (cimbClicksResponse.getStatusCode().equals(Constants.STATUS_CODE_400)) {
                setCallbackOrSendToStatusPage();
            } else {
                showWebViewPaymentPage(PaymentType.CIMB_CLICKS, cimbClicksResponse.getRedirectUrl());
            }
        } else {
            finishPayment(RESULT_OK, cimbClicksResponse);
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
            finishPayment(RESULT_OK, cimbClicksResponse);
        }
    }
}