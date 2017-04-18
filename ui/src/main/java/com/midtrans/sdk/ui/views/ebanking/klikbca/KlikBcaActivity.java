package com.midtrans.sdk.ui.views.ebanking.klikbca;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;

import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BasePaymentActivity;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;

/**
 * Created by rakawm on 4/7/17.
 */

public class KlikBcaActivity extends BasePaymentActivity implements KlikBcaView {

    private MidtransUi midtransUi;

    private AppCompatEditText userIdField;
    private TextInputLayout userIdTextInput;

    private KlikBcaPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klikbca);
        initMidtransUi();
        initPresenter();
        initViews();
        initThemes();
    }

    private void initMidtransUi() {
        midtransUi = MidtransUi.getInstance();
    }

    private void initPresenter() {
        presenter = new KlikBcaPresenter(this);
    }

    private void initViews() {
        userIdField = (AppCompatEditText) findViewById(R.id.user_id_field);
        userIdTextInput = (TextInputLayout) findViewById(R.id.user_id_text_input);
    }

    private void initThemes() {
        // Set text field themes
        setTextInputLayoutColorFilter(userIdTextInput);
        setEditTextCompatBackgroundTintColor(userIdField);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void startKlikBcaPaymentStatus(KlikBcaPaymentResponse paymentResponse) {
        Intent intent = new Intent(this, KlikBcaStatusActivity.class);
        intent.putExtra(KlikBcaStatusActivity.EXTRA_VALIDITY, paymentResponse.bcaKlikbcaExpireTime);
        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }

    private void showProgressDialog(String message) {
        UiUtils.showProgressDialog(this, message, false);
    }

    private void dismissProgressDialog() {
        UiUtils.hideProgressDialog();
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

    private void finishPayment(int resultCode, PaymentResult<KlikBcaPaymentResponse> paymentResult) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void onKlikBcaSuccess(KlikBcaPaymentResponse paymentResponse) {
        dismissProgressDialog();
        if (midtransUi.getCustomSetting().isShowPaymentStatus()) {
            startKlikBcaPaymentStatus(paymentResponse);
        } else {
            finishPayment(RESULT_OK, presenter.getPaymentResult());
        }
    }

    @Override
    public void onKlikBcaFailure(KlikBcaPaymentResponse paymentResponse) {
        dismissProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_failed_general));
    }

    @Override
    public void onKlikBcaError(String message) {
        dismissProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_failed_general));
    }

    private boolean isUserIdValid() {
        String userId = userIdField.getText().toString();
        return !TextUtils.isEmpty(userId);
    }

    @Override
    protected void startPayment() {
        userIdTextInput.setError(null);
        showProgressDialog(getString(R.string.processing_payment));
        presenter.startPayment(userIdField.getText().toString());
    }

    @Override
    protected boolean validatePayment() {
        if (isUserIdValid()) {
            return true;
        } else {
            userIdTextInput.setError(getString(R.string.error_user_id));
            return false;
        }
    }
}
