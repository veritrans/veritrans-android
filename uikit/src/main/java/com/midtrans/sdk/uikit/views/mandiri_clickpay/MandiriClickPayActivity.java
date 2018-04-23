package com.midtrans.sdk.uikit.views.mandiri_clickpay;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.TransactionDetails;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 10/16/17.
 */

public class MandiriClickPayActivity extends BasePaymentActivity implements MandiriClickPayView {

    private static final String TAG = MandiriClickPayActivity.class.getSimpleName();
    private final String PAGE_NAME = "Mandiri Clickpay Overview";
    private final String BUTTON_CONFIRM_NAME = "Confirm Payment Mandiri Clickpay";
    private final String BUTTON_RETRY_NAME = "Retry Mandiri Clickpay";

    private DefaultTextView textInput1;
    private DefaultTextView textInput2;
    private DefaultTextView textInput3;

    private TextInputLayout containerCardNumber;
    private TextInputLayout containerChallengeToken;

    private AppCompatEditText editCardNumber;
    private AppCompatEditText editChallengeToken;

    private FancyButton buttonPayment;
    private AppCompatButton toggleInstruction;

    private LinearLayout containerInstruction;

    private MandiriClickPayPresenter presenter;
    private String input3;
    private String challengeToken;
    private int attempt = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_mandiri_clickpay);
        initProperties();
        initEditCardTextWatcher();
        initActionButton();
        initData();
    }

    private void initData() {
        setPageTitle(getString(R.string.mandiri_click_pay));

        TransactionDetails transactionDetails = MidtransSDK.getInstance().getTransaction().getTransactionDetails();
        if (transactionDetails != null) {
            textInput2.setText(Utils.formatDouble(transactionDetails.getAmount()));
        }

        textInput3.setText(String.valueOf(SdkUIFlowUtil.generateRandomNumber()));

        toggleInstruction.setText(getString(R.string.payment_instruction, getString(R.string.mandiri_click_pay)));

        //track page view after page properly loaded
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);
    }

    private void initEditCardTextWatcher() {
        editCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                onChangeOfDebitCardNumer(text);
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
                    Logger.e(TAG, "editCardNumber:" + e.getMessage());
                }
            }
        });
    }

    private void onChangeOfDebitCardNumer(CharSequence text) {

        if (text != null && text.length() > 0) {
            String cardNumber = text.toString().trim().replace(" ", "");

            if (cardNumber.length() > 10) {
                textInput1.setText(cardNumber.substring(cardNumber.length() - 10,
                        cardNumber.length()));
            } else {
                textInput1.setText(cardNumber);
            }
        } else {
            textInput1.setText("");
        }
    }

    private void initActionButton() {
        buttonPayment.setTextBold();
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionPayment();
            }
        });
        toggleInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToggleInstructionVisibility();
            }
        });
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
                Logger.e(TAG, "changeToggleInstructionVisibility" + e.getMessage());
            }
        }
    }

    private void actionPayment() {
        String cardNumber = editCardNumber.getText().toString().trim();
        String challengeToken = editChallengeToken.getText().toString().trim();

        if (validData(cardNumber, challengeToken)) {
            SdkUIFlowUtil.hideKeyboard(this);
            showProgressLayout(getString(R.string.processing_payment));

            String cleanCardNumber = getCleanCardNumber(cardNumber);
            this.challengeToken = challengeToken;
            this.input3 = textInput3.getText().toString().trim();
            presenter.trackButtonClick(attempt == 0 ? BUTTON_CONFIRM_NAME : BUTTON_RETRY_NAME, PAGE_NAME);
            presenter.getCardToken(cleanCardNumber);
        }
    }

    private String getCleanCardNumber(String cardNumber) {
        return cardNumber.replace(" ", "");
    }

    private boolean validData(String cardNumber, String challengeToken) {
        boolean valid = true;

        if (TextUtils.isEmpty(cardNumber)) {
            containerCardNumber.setError(getString(R.string.empty_card_number));
            valid = false;
        } else {
            String cleanCardNumber = cardNumber.replace(" ", "");
            if (cleanCardNumber.length() < 16 || !SdkUIFlowUtil.isValidCardNumber(cleanCardNumber)) {
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

    private void initProperties() {
        this.presenter = new MandiriClickPayPresenter(this);
    }


    @Override
    public void bindViews() {
        containerCardNumber = (TextInputLayout) findViewById(R.id.container_card_number);
        containerChallengeToken = (TextInputLayout) findViewById(R.id.container_challenge_token);
        containerInstruction = (LinearLayout) findViewById(R.id.instruction_layout);

        editCardNumber = (AppCompatEditText) findViewById(R.id.edit_card_number);
        editChallengeToken = (AppCompatEditText) findViewById(R.id.edit_challenge_token);


        textInput1 = (DefaultTextView) findViewById(R.id.text_input_1);
        textInput2 = (DefaultTextView) findViewById(R.id.text_input_2);
        textInput3 = (DefaultTextView) findViewById(R.id.text_input_3);

        buttonPayment = (FancyButton) findViewById(R.id.button_primary);
        toggleInstruction = (AppCompatButton) findViewById(R.id.instruction_toggle);

    }

    @Override
    public void initTheme() {
        setBackgroundTintList(editCardNumber);
        setBackgroundTintList(editChallengeToken);
        setTextInputlayoutFilter(containerCardNumber);
        setTextInputlayoutFilter(containerChallengeToken);
        setPrimaryBackgroundColor(buttonPayment);
        setTextColor(toggleInstruction);
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        hideProgressLayout();
        showPaymentStatusPage(response, presenter.isShowPaymentStatusPage());
    }

    @Override
    public void onPaymentFailure(TransactionResponse response) {
        hideProgressLayout();
        if (attempt < UiKitConstants.MAX_ATTEMPT) {
            attempt += 1;
            SdkUIFlowUtil.showToast(MandiriClickPayActivity.this, getString(R.string.message_payment_failed));
        } else {
            if (response != null) {
                showPaymentStatusPage(response, presenter.isShowPaymentStatusPage());
            }
        }
    }

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        showOnErrorPaymentStatusmessage(error);
    }

    @Override
    public void onGetCardTokenSuccess(TokenDetailsResponse response) {
        if (isActivityRunning()) {
            presenter.startPayment(response.getTokenId(), challengeToken, input3);
        } else {
            hideProgressLayout();
            finish();
        }
    }

    @Override
    public void onGetCardTokenFailure(TokenDetailsResponse response) {
        hideProgressLayout();
        SdkUIFlowUtil.showApiFailedMessage(this, getString(R.string.message_getcard_token_failed));
    }

    @Override
    public void onGetCardTokenError(Throwable error) {
        hideProgressLayout();
        showOnErrorPaymentStatusmessage(error);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
            finishPayment(RESULT_OK, presenter.getTransactionResponse());
        }
    }

    @Override
    public void onBackPressed() {
        if (presenter != null) {
            presenter.trackBackButtonClick(PAGE_NAME);
        }
        super.onBackPressed();
    }
}
