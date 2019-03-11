package com.midtrans.sdk.uikit.view.method.mandiriclickpay;

import com.google.android.material.textfield.TextInputLayout;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails.TokenDetailsResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriClickpayResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.Helper;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.utilities.NetworkHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.widget.DefaultTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

public class MandiriClickpayInstructionActivity extends BasePaymentActivity implements MandiriClickpayInstructionContract {

    private static final String TAG = MandiriClickpayInstructionActivity.class.getSimpleName();

    private MandiriClickpayInstructionPresenter presenter;
    private MandiriClickpayResponse mandiriClickpayResponse;

    private DefaultTextView textInput1;
    private DefaultTextView textInput2;
    private DefaultTextView textInput3;

    private TextInputLayout containerCardNumber;
    private TextInputLayout containerChallengeToken;

    private AppCompatEditText editCardNumber;
    private AppCompatEditText editChallengeToken;

    private AppCompatButton toggleInstruction;

    private LinearLayout containerInstruction;

    private String input3;
    private String challengeToken;
    private int attempt = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_direct_debit_mandiri_clickpay_instruction);
        initToolbarAndView();
        initProperties();
        initEditCardTextWatcher();
        initActionButton();
        initData();
    }

    private void initActionButton() {
        buttonCompletePayment.setOnClickListener(v -> {
            actionPayment();
        });
        toggleInstruction.setOnClickListener(v -> changeToggleInstructionVisibility());
        buttonCompletePayment.setText(getString(R.string.pay_now));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.mandiri_click_pay));
    }

    private void initData() {
        if (paymentInfoResponse != null) {
            textInput2.setText(Helper.formatDouble(paymentInfoResponse.getTransactionDetails().getGrossAmount()));
        }
        textInput3.setText(String.valueOf(Helper.generateRandomNumber()));
        toggleInstruction.setText(getString(R.string.payment_instruction, getString(R.string.mandiri_click_pay)));
    }

    private void initEditCardTextWatcher() {
        editCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                onChangeOfDebitCardNumber(text);
            }

            @Override
            public void afterTextChanged(Editable test) {
                try {
                    char space = ' ';
                    if (test.length() > 0 && (test.length() % 5) == 0) {
                        final char c = test.charAt(test.length() - 1);
                        if (space == c) {
                            test.delete(test.length() - 1, test.length());
                        }
                    }
                    // Insert char where needed.
                    if (test.length() > 0 && (test.length() % 5) == 0) {
                        char c = test.charAt(test.length() - 1);
                        // Only if its a digit where there should be a space we insert a space
                        if (Character.isDigit(c) && TextUtils.split(test.toString(), String.valueOf
                                (space)).length <= 3) {
                            test.insert(test.length() - 1, String.valueOf(space));
                        }
                    }
                } catch (RuntimeException e) {
                    Logger.error(TAG, "editCardNumber:" + e.getMessage());
                }
            }
        });
    }

    private void setCallbackOrSendToStatusPage() {
        if (presenter.isShowPaymentStatusPage()) {
            startResultActivity(Constants.INTENT_CODE_PAYMENT_RESULT, PaymentListHelper.convertTransactionStatus(mandiriClickpayResponse));
        } else {
            finishPayment(RESULT_OK, mandiriClickpayResponse);
        }
    }

    private void changeToggleInstructionVisibility() {
        int colorPrimary = getPrimaryColor();

        if (colorPrimary != 0) {
            Drawable drawable;
            if (containerInstruction.getVisibility() == View.VISIBLE) {
                drawable = ContextCompat.getDrawable(this, R.drawable.ic_expand_less);
                containerInstruction.setVisibility(View.GONE);
            } else {
                drawable = ContextCompat.getDrawable(this, R.drawable.ic_expand_more);
                containerInstruction.setVisibility(View.VISIBLE);
            }

            try {
                drawable.setColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN);
                toggleInstruction.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            } catch (RuntimeException e) {
                Logger.error(TAG, "changeToggleInstructionVisibility" + e.getMessage());
            }
        }
    }

    private void onChangeOfDebitCardNumber(CharSequence text) {

        if (text != null && text.length() > 0) {
            String cardNumber = text.toString().trim().replace(" ", "");

            if (cardNumber.length() > 10) {
                textInput1.setText(cardNumber.substring(cardNumber.length() - 10, cardNumber.length()));
            } else {
                textInput1.setText(cardNumber);
            }
        } else {
            textInput1.setText("");
        }
    }

    private void actionPayment() {
        String cardNumber = editCardNumber.getText().toString().trim();
        String challengeToken = editChallengeToken.getText().toString().trim();

        if (validData(cardNumber, challengeToken)) {
            Helper.hideKeyboard(this);
            showProgressLayout(getString(R.string.processing_payment));

            String cleanCardNumber = getCleanCardNumber(cardNumber);
            this.challengeToken = challengeToken;
            this.input3 = textInput3.getText().toString().trim();
            presenter.getMandiriClickpayToken(cleanCardNumber);
        }
    }

    private boolean validData(String cardNumber, String challengeToken) {
        boolean valid = true;

        if (TextUtils.isEmpty(cardNumber)) {
            containerCardNumber.setError(getString(R.string.empty_card_number));
            valid = false;
        } else {
            String cleanCardNumber = cardNumber.replace(" ", "");
            if (cleanCardNumber.length() < 16 || !Helper.isValidCardNumber(cleanCardNumber)) {
                containerCardNumber.setError(getString(R.string.validation_message_invalid_card_no));
                valid = false;
            } else {
                containerCardNumber.setError("");
            }
        }

        if (TextUtils.isEmpty(challengeToken)) {
            containerChallengeToken.setError(getString(R.string.empty_challenge_token));
            valid = false;
        } else {
            if (challengeToken.trim().length() != 6) {
                containerChallengeToken.setError(getString(R.string.validation_message_invalid_token_no));
                valid = false;
            } else {
                containerChallengeToken.setError("");
            }
        }

        return valid;
    }

    private String getCleanCardNumber(String cardNumber) {
        return cardNumber.replace(" ", "");
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
        setBackgroundTintList(editCardNumber);
        setBackgroundTintList(editChallengeToken);
        setTextInputlayoutFilter(containerCardNumber);
        setTextInputlayoutFilter(containerChallengeToken);
        setTextColor(toggleInstruction);
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        presenter = new MandiriClickpayInstructionPresenter(this, paymentInfoResponse);
    }

    @Override
    public void onTokenizeSuccess(TokenDetailsResponse response) {
        if (response != null) {
            if (NetworkHelper.isPaymentSuccess(response)) {
                presenter.startMandiriClickpayPayment(paymentInfoResponse.getToken(), response.getTokenId(), challengeToken, input3);
            } else {
                setCallbackOrSendToStatusPage();
            }
        } else {
            finishPayment(RESULT_OK, mandiriClickpayResponse);
        }
    }

    @Override
    public void onTokenizeError(Throwable error) {
        hideProgressLayout();
        MessageHelper.showToast(this, getString(R.string.message_getcard_token_failed));
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        hideProgressLayout();
        mandiriClickpayResponse = (MandiriClickpayResponse) response;
        if (mandiriClickpayResponse != null) {
            if (NetworkHelper.isPaymentSuccess(mandiriClickpayResponse)) {
                setCallbackOrSendToStatusPage();
            } else {
                if (attempt < Constants.MAX_ATTEMPT) {
                    attempt += 1;
                    MessageHelper.showToast(this, getString(R.string.message_payment_failed));
                } else {
                    startResultActivity(
                            Constants.INTENT_CODE_PAYMENT_RESULT,
                            PaymentListHelper.newErrorPaymentResponse(
                                    PaymentType.MANDIRI_CLICKPAY,
                                    mandiriClickpayResponse.getStatusCode()
                            )
                    );
                }
            }
        } else {
            finishPayment(RESULT_OK, mandiriClickpayResponse);
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
            finishPayment(RESULT_OK, mandiriClickpayResponse);
        }
    }

    @Override
    protected void initToolbarAndView() {
        super.initToolbarAndView();
        containerCardNumber = findViewById(R.id.container_card_number);
        containerChallengeToken = findViewById(R.id.container_challenge_token);
        containerInstruction = findViewById(R.id.instruction_layout);

        editCardNumber = findViewById(R.id.edit_card_number);
        editChallengeToken = findViewById(R.id.edit_challenge_token);

        textInput1 = findViewById(R.id.text_input_1);
        textInput2 = findViewById(R.id.text_input_2);
        textInput3 = findViewById(R.id.text_input_3);

        toggleInstruction = findViewById(R.id.instruction_toggle);
    }
}