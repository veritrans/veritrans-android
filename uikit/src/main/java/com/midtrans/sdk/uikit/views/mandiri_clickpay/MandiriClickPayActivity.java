package com.midtrans.sdk.uikit.views.mandiri_clickpay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 10/16/17.
 */

public class MandiriClickPayActivity extends BasePaymentActivity implements MandiriClickPayView {

    private DefaultTextView textInput1;
    private DefaultTextView textInput2;
    private DefaultTextView textInput3;

    private TextInputLayout containerCardNumber;
    private TextInputLayout containerChallangeToken;

    private AppCompatEditText editCardNumber;
    private AppCompatEditText editChallangeToken;

    private FancyButton buttonPayment;

    private MandiriClickPayPresenter presenter;
    private String input3;
    private String challangeToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_mandiri_clickpay);
        initProperties();
        initActionButton();
    }

    private void initActionButton() {
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionPayment();
            }
        });
    }

    private void actionPayment() {
        String cardNumber = editCardNumber.getText().toString().trim();
        String challangeToken = editChallangeToken.getText().toString().trim();

        if (validData(cardNumber, challangeToken)) {
            showProgressLayout(getString(R.string.processing_payment));

            String cleanCardNumber = getCleanCardNumber(cardNumber);
            this.challangeToken = challangeToken;
            this.input3 = textInput3.getText().toString().trim();
            presenter.getCardToken(cleanCardNumber);
        }
    }

    private String getCleanCardNumber(String cardNumber) {
        return cardNumber.replace(" ", "");
    }

    private boolean validData(String cardNumber, String challangeToken) {
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

        if (TextUtils.isEmpty(challangeToken)) {
            containerChallangeToken.setError(getString(R.string.empty_challange_token));
            valid = false;
        } else {
            if (challangeToken.trim().length() != 6) {
                containerChallangeToken.setError(getString(R.string.validation_message_invalid_token_no));
                valid = false;
            } else {
                containerChallangeToken.setError("");
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
        containerChallangeToken = (TextInputLayout) findViewById(R.id.container_challange_token);

        editCardNumber = (AppCompatEditText) findViewById(R.id.edit_card_number);
        editChallangeToken = (AppCompatEditText) findViewById(R.id.edit_challange_token);

        textInput1 = (DefaultTextView) findViewById(R.id.text_input_1);
        textInput2 = (DefaultTextView) findViewById(R.id.text_input_2);
        textInput3 = (DefaultTextView) findViewById(R.id.text_input_3);

        buttonPayment = (FancyButton) findViewById(R.id.button_primary);
    }

    @Override
    public void initTheme() {

    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        hideProgressLayout();
        showPaymentStatusPage(response, presenter.isShowPaymentStatusPage());
    }

    @Override
    public void onPaymentFailure(TransactionResponse response) {
        hideProgressLayout();
        showPaymentStatusPage(response, presenter.isShowPaymentStatusPage());
    }

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        showOnErrorPaymentStatusmessage(error);
    }

    @Override
    public void onGetCardTokenSuccess(TokenDetailsResponse response) {
        if (isActivityRunning()) {
            presenter.startPayment(response.getTokenId(), challangeToken, input3);
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
}
