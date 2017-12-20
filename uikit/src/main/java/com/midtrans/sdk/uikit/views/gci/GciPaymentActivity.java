package com.midtrans.sdk.uikit.views.gci;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by Fajar on 27/10/17.
 */

public class GciPaymentActivity extends BasePaymentActivity implements BasePaymentView {

    private final String PAGE_NAME = "GCI";

    private FancyButton buttonPrimary;
    private AppCompatEditText giftCardNumber;
    private AppCompatEditText cardPin;
    private TextInputLayout containerGiftCardNumber;
    private TextInputLayout containerCardPin;

    private GciPaymentPresenter presenter;

    private int retryNumber = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gci_payment);
        initProperties();
        initActionButton();
        bindData();
    }

    private void bindData() {
        setPageTitle(getString(R.string.payment_method_gci));
        buttonPrimary.setText(getString(R.string.confirm_payment));
        buttonPrimary.setTextBold();

        giftCardNumber.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                giftCardNumber.setError(null);
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {

                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf
                        (space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }

                }

                // Move to next input
                if (s.length() >= 19) {
                    cardPin.requestFocus();
                }
            }
        });

        //track page view after page properly loaded
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);
    }

    private void initActionButton() {
        buttonPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFormValid()) {
                    showProgressLayout(getString(R.string.processing_payment));
                    presenter.startPayment(getCardNumber(), getPin());
                }
            }
        });
    }

    private void initProperties() {
        this.presenter = new GciPaymentPresenter(this);
    }


    @Override
    public void bindViews() {
        buttonPrimary = (FancyButton) findViewById(R.id.button_primary);
        giftCardNumber = (AppCompatEditText) findViewById(R.id.edit_gci_card_number);
        cardPin = (AppCompatEditText) findViewById(R.id.edit_gci_password);
        containerGiftCardNumber = (TextInputLayout) findViewById(R.id.card_number_container);
        containerCardPin = (TextInputLayout) findViewById(R.id.password_container);
    }

    @Override
    public void initTheme() {
        setBackgroundTintList(giftCardNumber);
        setBackgroundTintList(cardPin);
        setTextInputlayoutFilter(containerGiftCardNumber);
        setTextInputlayoutFilter(containerCardPin);
        setPrimaryBackgroundColor(buttonPrimary);
    }

    /**
     * Check password
     *
     * @return if text was empty return false else return true
     */
    private boolean checkPasswordValidity() {
        boolean valid = true;
        String password = cardPin.getText().toString();
        if (TextUtils.isEmpty(password)) {
            containerCardPin.setError(getString(R.string.error_password_empty));
            valid = false;
        } else if (password.length() < 3) {
            containerCardPin.setError(getString(R.string.error_password_invalid));
            valid = false;
        } else {
            containerCardPin.setError(null);
        }
        return valid;
    }

    private boolean isFormValid() {
        return checkCardNumberValidity() && checkPasswordValidity();
    }

    private String getPin() {
        return cardPin.getText().toString();
    }

    private String getCardNumber() {
        return giftCardNumber.getText().toString().trim().replace(" ", "");
    }

    private boolean checkCardNumberValidity() {
        boolean isValid = true;

        String cardNumber = giftCardNumber.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(cardNumber)) {
            containerGiftCardNumber.setError(getString(R.string.validation_message_card_number));
            isValid = false;
        } else {
            containerGiftCardNumber.setError(null);
        }

        if (cardNumber.length() != 16 ) {
            containerGiftCardNumber.setError(getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            containerGiftCardNumber.setError(null);
        }
        return isValid;
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        hideProgressLayout();
        showPaymentStatusPage(response, presenter.isShowPaymentStatusPage());
    }

    @Override
    public void onPaymentFailure(TransactionResponse response) {
        hideProgressLayout();
        SdkUIFlowUtil.hideProgressDialog();
        if (retryNumber > 0) {
            SdkUIFlowUtil.showToast(this, "" + getString(R.string.message_payment_failed));
            --retryNumber;
        } else {
            showPaymentStatusPage(response, presenter.isShowPaymentStatusPage());
        }
    }

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        showOnErrorPaymentStatusmessage(error);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS || requestCode == UiKitConstants.INTENT_WEBVIEW_PAYMENT) {
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
