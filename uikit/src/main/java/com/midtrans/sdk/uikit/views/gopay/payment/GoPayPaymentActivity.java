package com.midtrans.sdk.uikit.views.gopay.payment;

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
import com.midtrans.sdk.uikit.views.gopay.authorization.GoPayAuthorizationActivitiy;
import com.midtrans.sdk.uikit.views.status.PaymentStatusActivity;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 9/7/17.
 */

public class GoPayPaymentActivity extends BasePaymentActivity implements GoPayPaymentView {

    private static final String TAG = GoPayPaymentActivity.class.getSimpleName();

    private TextInputLayout containerCountryCode;
    private TextInputLayout containerPhoneNumber;

    private TextInputEditText fieldCountryCode;
    private TextInputEditText fieldPhoneNumber;

    private DefaultTextView textNotificationInfo;
    private DefaultTextView textTitle;

    private FancyButton buttonContinue;


    private GopayPaymentPresenter presenter;
    private String fullPhoneNumber = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gopay_payment);
        initProperties();
        initActionButton();
        initData();
    }

    private void initData() {
        textTitle.setText(R.string.gopay);
    }

    private void initActionButton() {
        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoPayPayment();
            }
        });
    }

    private void startGoPayPayment() {
        SdkUIFlowUtil.hideKeyboard(this);

        String countryCode = fieldCountryCode.getText().toString().trim();
        String phoneNumber = fieldPhoneNumber.getText().toString().trim();
        if (phoneNumberValid(countryCode, phoneNumber)) {
            showProgressLayout();
            fullPhoneNumber = countryCode + phoneNumber;
            presenter.startGoPayPayment(fullPhoneNumber);
        }
    }

    private boolean phoneNumberValid(String countryCode, String phoneNumber) {
        boolean valid = true;

        if (TextUtils.isEmpty(countryCode)) {
            containerCountryCode.setError(getString(R.string.validation_country_code_empty));
            valid = false;
        } else {
            containerCountryCode.setError("");
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            containerPhoneNumber.setError(getString(R.string.validation_phone_no_empty));
            valid = false;
        } else if (!SdkUIFlowUtil.isPhoneNumberValid(countryCode + phoneNumber)) {
            containerPhoneNumber.setError(getString(R.string.error_invalid_phone_number));
            valid = false;
        } else {
            containerPhoneNumber.setError("");
        }
        return valid;
    }

    @Override
    public void bindViews() {
        containerCountryCode = (TextInputLayout) findViewById(R.id.container_input_country_code);
        containerPhoneNumber = (TextInputLayout) findViewById(R.id.container_input_phone_number);

        fieldCountryCode = (TextInputEditText) findViewById(R.id.edit_country_code);
        fieldPhoneNumber = (TextInputEditText) findViewById(R.id.edit_phone_number);

        textTitle = (DefaultTextView) findViewById(R.id.text_page_title);
        buttonContinue = (FancyButton) findViewById(R.id.button_primary);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonContinue);
        setBackgroundTintList(fieldCountryCode);
        setBackgroundTintList(fieldPhoneNumber);
        setTextInputlayoutFilter(containerCountryCode);
        setTextInputlayoutFilter(containerPhoneNumber);
    }

    private void initProperties() {
        presenter = new GopayPaymentPresenter(this);
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        hideProgressLayout();
        if (isActivityRunning()) {
            Intent intent = new Intent(this, GoPayAuthorizationActivitiy.class);
            intent.putExtra(GoPayAuthorizationActivitiy.EXTRA_PHONE_NUMBER, fullPhoneNumber);
            startActivityForResult(intent, UiKitConstants.INTENT_VERIFICATION);
        }
    }

    @Override
    public void onPaymentFailure(TransactionResponse response) {
        hideProgressLayout();
        showPaymentStatusPage(response);
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

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        MessageInfo messageInfo = MessageUtil.createMessageOnError(this, error, null);
        SdkUIFlowUtil.showToast(this, messageInfo.detailsMessage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
                finishPayment(RESULT_OK);
            } else if (requestCode == UiKitConstants.INTENT_VERIFICATION) {
                finishPayment(RESULT_OK, data);
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
                finishPayment(RESULT_OK);
            }
        }
    }

    private void finishPayment(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }

    private void finishPayment(int resultCode) {
        Intent data = new Intent();
        data.putExtra(getString(R.string.transaction_response), presenter.getTransactionResponse());
        setResult(resultCode, data);
        finish();
    }
}
