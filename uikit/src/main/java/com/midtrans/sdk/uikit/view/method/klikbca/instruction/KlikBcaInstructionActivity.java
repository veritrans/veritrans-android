package com.midtrans.sdk.uikit.view.method.klikbca.instruction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.KlikBcaResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.NetworkHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.view.PaymentListActivity;
import com.midtrans.sdk.uikit.view.method.klikbca.result.KlikBcaResultActivity;

import androidx.annotation.Nullable;

public class KlikBcaInstructionActivity extends BasePaymentActivity implements BasePaymentContract {

    private KlikBcaInstructionPresenter presenter;
    private KlikBcaResponse klikBcaResponse;

    private TextInputEditText fieldUserId;
    private TextInputLayout containerUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_direct_debit_klikbca_instruction);
        initToolbarAndView();
        initProperties();
        initActionButton();
    }

    private void initActionButton() {
        buttonCompletePayment.setOnClickListener(v -> {
            String userId = fieldUserId.getText().toString().trim();
            if (isValidUserId(userId)) {
                showProgressLayout(getString(R.string.processing_payment));
                presenter.startKlikBcaPayment(paymentInfoResponse.getToken(), userId);
            }
        });
        buttonCompletePayment.setText(getString(R.string.confirm_payment));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.klik_bca));
    }

    private boolean isValidUserId(String userId) {
        if (TextUtils.isEmpty(userId)) {
            containerUserId.setError(getString(R.string.error_user_id));
            return false;
        }
        containerUserId.setError("");
        return true;
    }

    private void setCallbackOrSendToStatusPage() {
        if (presenter.isShowPaymentStatusPage()) {
            startResultActivity(Constants.INTENT_CODE_PAYMENT_RESULT, PaymentListHelper.convertTransactionStatus(klikBcaResponse));
        } else {
            finishPayment(RESULT_OK, klikBcaResponse);
        }
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        presenter = new KlikBcaInstructionPresenter(this, paymentInfoResponse);
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        hideProgressLayout();
        klikBcaResponse = (KlikBcaResponse) response;
        if (klikBcaResponse != null) {
            if (NetworkHelper.isPaymentSuccess(klikBcaResponse)) {
                Intent intent = new Intent(this, KlikBcaResultActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_PAYMENT_STATUS, klikBcaResponse);
                intent.putExtra(PaymentListActivity.EXTRA_PAYMENT_INFO, paymentInfoResponse);
                startActivityForResult(intent, Constants.INTENT_CODE_PAYMENT_RESULT);
            } else {
                setCallbackOrSendToStatusPage();
            }
        } else {
            finishPayment(RESULT_OK, klikBcaResponse);
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
        if (requestCode == Constants.INTENT_CODE_PAYMENT_RESULT) {
            finishPayment(RESULT_OK, klikBcaResponse);
        }
    }

    @Override
    protected void initToolbarAndView() {
        super.initToolbarAndView();
        fieldUserId = findViewById(R.id.edit_user_id);
        containerUserId = findViewById(R.id.container_user_id);
    }
}