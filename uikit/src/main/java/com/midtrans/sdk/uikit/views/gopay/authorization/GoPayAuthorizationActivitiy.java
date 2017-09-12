package com.midtrans.sdk.uikit.views.gopay.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.models.MessageInfo;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.status.PaymentStatusActivity;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 9/11/17.
 */

public class GoPayAuthorizationActivitiy extends BasePaymentActivity implements GoPayAuthorizationView {

    public static final String EXTRA_PHONE_NUMBER = "extra.number";

    private TextInputLayout containerVerificationCode;
    private TextInputEditText fieldVerificationCode;
    private DefaultTextView textInfo;
    private FancyButton buttonContinue;

    private GoPayAuthorizationPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gopay_authorization);
        initProperties();
        initActionButton();
    }

    private void initProperties() {
        presenter = new GoPayAuthorizationPresenter(this);
        String phoneNumber = getIntent().getStringExtra(EXTRA_PHONE_NUMBER);
        textInfo.setText(getString(R.string.info_gopay_auth, phoneNumber));
    }

    private void initActionButton() {
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authorizePayment();
            }
        });
    }

    private void authorizePayment() {
        SdkUIFlowUtil.hideKeyboard(this);

        String verificationCode = fieldVerificationCode.getText().toString().trim();
        if (verificationCodeValid(verificationCode)) {
            showProgressLayout();
            presenter.authorizePayment(verificationCode);
        }
    }

    private void showPaymentStatusPage(TransactionResponse response) {
        if (isActivityRunning()) {
            if (presenter.isShowPaymentStatusPage()) {
                Intent intent = new Intent(this, PaymentStatusActivity.class);
                intent.putExtra(PaymentStatusActivity.EXTRA_PAYMENT_RESULT, response);
                startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT_STATUS);
            } else {
                finishPayment(RESULT_OK);
            }
        } else {
            finish();
        }
    }

    private void finishPayment(int resultCode) {
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.transaction_response), presenter.getTransactionResponse());
        setResult(resultCode, intent);
        finish();
    }


    @Override
    public void bindViews() {
        containerVerificationCode = (TextInputLayout) findViewById(R.id.container_verification_number);
        fieldVerificationCode = (TextInputEditText) findViewById(R.id.edit_verification_number);
        buttonContinue = (FancyButton) findViewById(R.id.button_primary);
        textInfo = (DefaultTextView) findViewById(R.id.text_athorization_info);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonContinue);
        setBackgroundTintList(fieldVerificationCode);
        setTextInputlayoutFilter(containerVerificationCode);
    }

    @Override
    public void onVerificationCodeSuccess(TransactionResponse response) {
        hideProgressLayout();
        showPaymentStatusPage(response);
    }

    @Override
    public void onVerificationCodeFailure(TransactionResponse response) {
        hideProgressLayout();
        showPaymentStatusPage(response);
    }

    @Override
    public void onVerificationCodeError(Throwable error) {
        if (isActivityRunning()) {
            MessageInfo messageInfo = MessageUtil.createMessageOnError(this, error, null);
            SdkUIFlowUtil.showToast(this, messageInfo.detailsMessage);
        }
    }

    @Override
    public void onResendSuccess() {

    }

    @Override
    public void onResendFailure() {

    }

    @Override
    public void onResenError() {

    }

    private boolean verificationCodeValid(String verificationCode) {
        if (TextUtils.isEmpty(verificationCode)) {
            containerVerificationCode.setError(getString(R.string.verificaton_code_empty));
            return false;
        }
        containerVerificationCode.setError("");
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
            finishPayment(RESULT_OK);
        }
    }
}
