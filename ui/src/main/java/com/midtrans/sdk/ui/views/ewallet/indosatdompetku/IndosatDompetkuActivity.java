package com.midtrans.sdk.ui.views.ewallet.indosatdompetku;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;

import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentResponse;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BasePaymentActivity;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.views.status.PaymentStatusActivity;

/**
 * Created by ziahaqi on 4/10/17.
 */

public class IndosatDompetkuActivity extends BasePaymentActivity implements IndosatDompetkuView {

    private TextInputLayout layoutIndosatNumber;
    private AppCompatEditText editIndosatNumber;

    private IndosatDompetkuPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indosat_dompetku);
        initPresenter();
        trackPage();
        initViews();
        initThemes();
    }

    private void initPresenter() {
        presenter = new IndosatDompetkuPresenter(this);
    }

    private void trackPage() {
        presenter.trackEvent(AnalyticsEventName.PAGE_INDOSAT_DOMPETKU);
    }

    private void initThemes() {
        setTextInputLayoutColorFilter(layoutIndosatNumber);
        setEditTextCompatBackgroundTintColor(editIndosatNumber);
    }


    private void initViews() {
        layoutIndosatNumber = (TextInputLayout) findViewById(R.id.til_indosat_phone_number);
        editIndosatNumber = (AppCompatEditText) findViewById(R.id.edit_indosat_number);
    }

    private void showProgressDialog(String message) {
        UiUtils.showProgressDialog(this, message, false);
    }

    private void dismissProgressDialog() {
        UiUtils.hideProgressDialog();
    }

    @Override
    public void onPaymentError(String message) {
        dismissProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_failed_general));
    }

    @Override
    public void onPaymentFailure(IndosatDompetkuPaymentResponse response) {
        dismissProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_failed_general));
    }

    @Override
    public void onPaymentSuccess(IndosatDompetkuPaymentResponse response) {
        dismissProgressDialog();
        if (presenter.isShowPaymentStatus()) {
            showPaymentStatus(response);
        } else {
            finishPayment(RESULT_OK, presenter.getPaymentResult());
        }
    }

    private void showPaymentStatus(IndosatDompetkuPaymentResponse response) {
        Intent intent = new Intent(this, PaymentStatusActivity.class);
        intent.putExtra(Constants.PAYMENT_RESULT, new PaymentResult<>(response));
        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }

    private void finishPayment(int resultCode, PaymentResult<IndosatDompetkuPaymentResponse> paymentResult) {
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
        String phoneNumber = editIndosatNumber.getText().toString().trim();
        layoutIndosatNumber.setError(null);
        showProgressDialog(getString(R.string.processing_payment));
        presenter.startPayment(phoneNumber);
    }

    @Override
    protected boolean validatePayment() {
        String phoneNumber = editIndosatNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            layoutIndosatNumber.setError(getString(R.string.validation_phone_no_empty));
            return false;
        }
        return true;
    }
}
