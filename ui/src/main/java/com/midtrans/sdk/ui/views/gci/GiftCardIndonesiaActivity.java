package com.midtrans.sdk.ui.views.gci;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BasePaymentActivity;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.views.status.PaymentStatusActivity;

/**
 * Created by rakawm on 4/6/17.
 */

public class GiftCardIndonesiaActivity extends BasePaymentActivity implements GiftCardIndonesiaView {
    private MidtransUi midtransUi;

    private AppCompatEditText cardNumberField;
    private AppCompatEditText passwordField;
    private TextInputLayout cardNumberTextInput;
    private TextInputLayout passwordTextInput;

    private GiftCardIndonesiaPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card);
        initMidtransUi();
        initPresenter();
        initViews();
        initThemes();
        initCardNumberField();
    }

    private void initMidtransUi() {
        midtransUi = MidtransUi.getInstance();
    }

    private void initPresenter() {
        presenter = new GiftCardIndonesiaPresenter(this);
    }

    private void initViews() {
        cardNumberField = (AppCompatEditText) findViewById(R.id.card_number_field);
        passwordField = (AppCompatEditText) findViewById(R.id.password_field);
        cardNumberTextInput = (TextInputLayout) findViewById(R.id.card_number_text_input);
        passwordTextInput = (TextInputLayout) findViewById(R.id.password_text_input);
    }

    private void initThemes() {
        // Set color theme for edit text field
        setTextInputLayoutColorFilter(cardNumberTextInput);
        setTextInputLayoutColorFilter(passwordTextInput);
        setEditTextCompatBackgroundTintColor(cardNumberField);
        setEditTextCompatBackgroundTintColor(passwordField);
    }

    private void initCardNumberField() {
        cardNumberField.addTextChangedListener(new TextWatcher() {
            private static final char SPACE = ' ';

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                cardNumberTextInput.setError(null);

                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (SPACE == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {

                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a SPACE we insert a SPACE
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf
                            (SPACE)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(SPACE));
                    }

                }

                // Move to next input
                if (s.length() >= 19) {
                    if (checkCardNumberValidity()) {
                        passwordField.requestFocus();
                    }
                }
            }
        });
    }

    private void startPaymentStatus(GiftCardPaymentResponse paymentResponse) {
        Intent intent = new Intent(this, PaymentStatusActivity.class);
        intent.putExtra(Constants.PAYMENT_RESULT, paymentResponse);
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

    private void finishPayment(int resultCode, PaymentResult<GiftCardPaymentResponse> paymentResult) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void onGiftCardIndonesiaSuccess(GiftCardPaymentResponse response) {
        dismissProgressDialog();
        if (midtransUi.getCustomSetting().isShowPaymentStatus()) {
            startPaymentStatus(response);
        } else {
            finishPayment(RESULT_OK, presenter.getPaymentResult());
        }
    }

    @Override
    public void onGiftCardIndonesiaFailure(GiftCardPaymentResponse response) {
        dismissProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_failed_general));
    }

    @Override
    public void onGiftCardIndonesiaError(String message) {
        dismissProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_failed_general));
    }

    public boolean checkFormValidity() {
        return checkCardNumberValidity() && checkPasswordValidity();
    }

    private boolean checkCardNumberValidity() {
        boolean isValid = true;

        String cardNumber = cardNumberField.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(cardNumber)) {
            cardNumberTextInput.setError(getString(R.string.validation_message_card_number));
            isValid = false;
        } else {
            cardNumberTextInput.setError(null);
        }

        if (cardNumber.length() != 16) {
            cardNumberTextInput.setError(getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            cardNumberTextInput.setError(null);
        }
        return isValid;
    }

    public boolean checkPasswordValidity() {
        boolean valid = true;
        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordTextInput.setError(getString(R.string.error_password_empty));
            valid = false;
        } else if (password.length() < 3) {
            passwordTextInput.setError(getString(R.string.error_password_invalid));
            valid = false;
        } else {
            passwordTextInput.setError(null);
        }
        return valid;
    }

    @Override
    protected void startPayment() {
        String cardNumber = cardNumberField.getText().toString().trim().replace(" ", "");
        String password = passwordField.getText().toString().trim();
        showProgressDialog(getString(R.string.processing_payment));
        presenter.startPayment(cardNumber, password);
    }

    @Override
    protected boolean validatePayment() {
        return checkFormValidity();
    }
}
