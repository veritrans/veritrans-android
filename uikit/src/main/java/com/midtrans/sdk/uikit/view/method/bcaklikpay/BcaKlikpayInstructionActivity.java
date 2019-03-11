package com.midtrans.sdk.uikit.view.method.bcaklikpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaKlikPayResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;

import androidx.annotation.Nullable;

public class BcaKlikpayInstructionActivity extends BasePaymentActivity implements BasePaymentContract {

    private BcaKlikpayInstructionPresenter presenter;
    private BcaKlikPayResponse bcaKlikPayResponse;

    private boolean isAlreadyGotResponse = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_online_debit_bca_klikpay_instruction);
        initToolbarAndView();
        initProperties();
        initActionButton();
    }

    private void initActionButton() {
        buttonCompletePayment.setOnClickListener(v -> {
            if (isAlreadyGotResponse && bcaKlikPayResponse != null) {
                showWebViewPaymentPage(PaymentType.BCA_KLIKPAY, bcaKlikPayResponse.getRedirectUrl());
            } else {
                showProgressLayout(getString(R.string.processing_payment));
                presenter.startBcaKlikpayPayment(paymentInfoResponse.getToken());
            }
        });
        buttonCompletePayment.setText(getString(R.string.confirm_payment));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.bca_klik));
    }

    private void setCallbackOrSendToStatusPage() {
        if (presenter.isShowPaymentStatusPage()) {
            startResultActivity(Constants.INTENT_CODE_PAYMENT_RESULT, PaymentListHelper.convertTransactionStatus(bcaKlikPayResponse));
        } else {
            finishPayment(RESULT_OK, bcaKlikPayResponse);
        }
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        presenter = new BcaKlikpayInstructionPresenter(this, paymentInfoResponse);
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        hideProgressLayout();
        bcaKlikPayResponse = (BcaKlikPayResponse) response;
        if (bcaKlikPayResponse != null) {
            if (bcaKlikPayResponse.getStatusCode().equals(Constants.STATUS_CODE_400)) {
                setCallbackOrSendToStatusPage();
            } else {
                showWebViewPaymentPage(PaymentType.BCA_KLIKPAY, bcaKlikPayResponse.getRedirectUrl());
            }
        } else {
            finishPayment(RESULT_OK, bcaKlikPayResponse);
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
            finishPayment(RESULT_OK, bcaKlikPayResponse);
        }
    }
}