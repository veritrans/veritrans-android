package com.midtrans.sdk.ui.views.ewallet.tcash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;

import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentResponse;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BasePaymentActivity;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.views.status.PaymentStatusActivity;

/**
 * Created by ziahaqi on 4/7/17.
 */

public class TelkomselCashActivity extends BasePaymentActivity implements TelkomselCashView {

    private TextInputLayout tokenTextInput;
    private AppCompatEditText tokenField;

    private TelkomselCashPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telkomsel_cash);
        initPresenter();
        trackPage();
        initViews();
        initThemes();
    }

    private void initPresenter() {
        presenter = new TelkomselCashPresenter(this);
    }

    private void trackPage() {
        presenter.trackEvent(AnalyticsEventName.PAGE_TCASH);
    }

    private void initThemes() {
        setTextInputLayoutColorFilter(tokenTextInput);
        setEditTextCompatBackgroundTintColor(tokenField);
    }


    private void initViews() {
        tokenTextInput = (TextInputLayout) findViewById(R.id.til_tcash_token);
        tokenField = (AppCompatEditText) findViewById(R.id.edit_tcash_token);
    }

    @Override
    public void onTelkomselCashPaymentSuccess(TelkomselCashPaymentResponse response) {
        dismissProgressDialog();
        if (presenter.isShowPaymentStatus()) {
            startTelkomselCashStatus(response);
        } else {
            finishPayment(RESULT_OK, presenter.getPaymentResult());
        }
    }

    private void startTelkomselCashStatus(TelkomselCashPaymentResponse response) {
        Intent intent = new Intent(this, PaymentStatusActivity.class);
        intent.putExtra(Constants.PAYMENT_RESULT, new PaymentResult<>(response));
        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }

    @Override
    public void onTelkomselCashPaymentFailure(TelkomselCashPaymentResponse response) {
        dismissProgressDialog();

        if (response != null && response.statusCode.equals(getString(R.string.status_code_400))) {
            UiUtils.showToast(this, getString(R.string.message_payment_cannot_proccessed));
        } else {
            UiUtils.showToast(this, getString(R.string.error_message_invalid_input_telkomsel));
        }
    }

    @Override
    public void onTelkomselCashPaymentError(String message) {
        dismissProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_cannot_proccessed));
    }

    private void showProgressDialog(String message) {
        UiUtils.showProgressDialog(this, message, false);
    }

    private void dismissProgressDialog() {
        UiUtils.hideProgressDialog();
    }

    private void finishPayment(int resultCode, PaymentResult<TelkomselCashPaymentResponse> paymentResult) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        setResult(resultCode, data);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == STATUS_REQUEST_CODE) {
                finishPayment(resultCode, presenter.getPaymentResult());
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void startPayment() {
        String tCashToken = tokenField.getText().toString().trim();
        tokenTextInput.setError(null);
        showProgressDialog(getString(R.string.processing_payment));
        presenter.startPayment(tCashToken);
    }

    @Override
    protected boolean validatePayment() {
        // start payment with Telkomsel Cash
        String token = tokenField.getText().toString().trim();
        if (TextUtils.isEmpty(token)) {
            tokenTextInput.setError(getString(R.string.error_tcash_token_empty));
            return false;
        } else {
            return true;
        }
    }
}
