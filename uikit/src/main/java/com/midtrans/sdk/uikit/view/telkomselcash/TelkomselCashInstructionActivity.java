package com.midtrans.sdk.uikit.view.telkomselcash;

import com.google.android.material.textfield.TextInputLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.TelkomselCashResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.Helper;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.utilities.NetworkHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

public class TelkomselCashInstructionActivity extends BasePaymentActivity implements BasePaymentContract {

    private TelkomselCashInstructionPresenter presenter;
    private TelkomselCashResponse telkomselCashResponse;

    private AppCompatEditText telkomselCustomerNumber;
    private TextInputLayout containerTCashToken;
    private int attempt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_ewallet_telkomsel_cash_instruction);
        initToolbarAndView();
        initProperties();
        initActionButton();
    }

    @Override
    protected void initToolbarAndView() {
        super.initToolbarAndView();
        telkomselCustomerNumber = findViewById(R.id.telkomsel_token_et);
        containerTCashToken = findViewById(R.id.telkomsel_token_til);
    }

    private void initActionButton() {
        buttonCompletePayment.setOnClickListener(v -> {
            Helper.hideKeyboard(this);
            String customerNumber = telkomselCustomerNumber.getText().toString().trim();
            if (isValidUserId(customerNumber)) {
                showProgressLayout(getString(R.string.processing_payment));
                presenter.startTelkomselCashPayment(paymentInfoResponse.getToken(), customerNumber);
            }
        });
        buttonCompletePayment.setText(getString(R.string.confirm_payment));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.telkomsel_cash));
    }

    private boolean isValidUserId(String userId) {
        if (TextUtils.isEmpty(userId)) {
            containerTCashToken.setError(getString(R.string.error_empty_tcash_token_field));
            return false;
        }
        containerTCashToken.setError("");
        return true;
    }

    private void setCallbackOrSendToStatusPage() {
        if (presenter.isShowPaymentStatusPage()) {
            startResultActivity(Constants.INTENT_CODE_PAYMENT_RESULT, PaymentListHelper.convertTransactionStatus(telkomselCashResponse));
        } else {
            finishPayment(RESULT_OK, telkomselCashResponse);
        }
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
        setBackgroundTintList(telkomselCustomerNumber);
        setTextInputlayoutFilter(containerTCashToken);
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        presenter = new TelkomselCashInstructionPresenter(this, paymentInfoResponse);
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        hideProgressLayout();
        telkomselCashResponse = (TelkomselCashResponse) response;
        if (telkomselCashResponse != null) {
            if (NetworkHelper.isPaymentSuccess(telkomselCashResponse)) {
                setCallbackOrSendToStatusPage();
            } else {
                if (attempt < Constants.MAX_ATTEMPT) {
                    attempt += 1;
                    MessageHelper.showToast(this, getString(R.string.error_message_invalid_input_telkomsel));
                } else {
                    startResultActivity(
                            Constants.INTENT_CODE_PAYMENT_RESULT,
                            PaymentListHelper.newErrorPaymentResponse(
                                    PaymentType.TELKOMSEL_CASH,
                                    telkomselCashResponse.getStatusCode()
                            )
                    );
                }
            }
        } else {
            finishPayment(RESULT_OK, telkomselCashResponse);
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
        if (requestCode == Constants.INTENT_CODE_PAYMENT_RESULT && resultCode == Activity.RESULT_OK) {
            finishPayment(RESULT_OK, telkomselCashResponse);
        }
    }
}