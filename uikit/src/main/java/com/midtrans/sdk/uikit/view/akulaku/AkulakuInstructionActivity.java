package com.midtrans.sdk.uikit.view.akulaku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AkulakuResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CimbClicksResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.NetworkHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.view.cimbclicks.CimbClicksInstructionPresenter;

import androidx.annotation.Nullable;

public class AkulakuInstructionActivity extends BasePaymentActivity implements BasePaymentContract {

    private AkulakuInstructionPresenter presenter;
    private AkulakuResponse akulakuResponse;

    private boolean isAlreadyGotResponse = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_cardless_credit_akulaku_instruction);
        initToolbarAndView();
        initProperties();
        initActionButton();
    }

    private void initActionButton() {
        buttonCompletePayment.setOnClickListener(v -> {
            if (isAlreadyGotResponse && akulakuResponse != null) {
                showWebViewPaymentPage(PaymentType.AKULAKU, akulakuResponse.getRedirectUrl());
            } else {
                showProgressLayout(getString(R.string.processing_payment));
                presenter.startAkulakuPayment(paymentInfoResponse.getToken());
            }
        });
        buttonCompletePayment.setText(getString(R.string.confirm_payment));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.akulaku));
    }

    private void setCallbackOrSendToStatusPage() {
        if (presenter.isShowPaymentStatusPage()) {
            startResultActivity(Constants.INTENT_CODE_PAYMENT_RESULT, PaymentListHelper.convertTransactionStatus(akulakuResponse));
        } else {
            finishPayment(RESULT_OK, akulakuResponse);
        }
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        presenter = new AkulakuInstructionPresenter(this, paymentInfoResponse);
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        hideProgressLayout();
        akulakuResponse = (AkulakuResponse) response;
        if (akulakuResponse != null) {
            if (NetworkHelper.isPaymentSuccess(akulakuResponse)) {
                showWebViewPaymentPage(PaymentType.AKULAKU, akulakuResponse.getRedirectUrl());
            } else {
                setCallbackOrSendToStatusPage();
            }
        } else {
            finishPayment(RESULT_OK, akulakuResponse);
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
            finishPayment(RESULT_OK, akulakuResponse);
        }
    }
}