package com.midtrans.sdk.uikit.views.gopay.authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
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
        String phoneNumber = getIntent().getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        textInfo.setText(getString(R.string.info_gopay_auth, phoneNumber));
    }

    private void initActionButton() {
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verificationCode = fieldVerificationCode.getText().toString().trim();
                if (verificationCodeValid(verificationCode)) {
                    showProgressLayout();
                    presenter.authorizePayment(verificationCode);
                }
            }
        });
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
    public void onVerificationCodeSuccess() {
        hideProgressLayout();
        showPaymentStatusPage();
    }

    private void showPaymentStatusPage() {

    }

    @Override
    public void onVerificationCodeFailure() {
        hideProgressLayout();
        showPaymentStatusPage();
    }

    @Override
    public void onVerificationCodeError() {

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
}
