package com.midtrans.sdk.uikit.views.telkomsel_cash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

/**
 * Created by Fajar on 30/10/17.
 */

public class TelkomselCashPaymentActivity extends BasePaymentActivity implements BasePaymentView {

    private final String PAGE_NAME = "Telkomsel Cash Overview";
    private final String BUTTON_CONFIRM_NAME = "Confirm Payment Telkomsel Cash";

    private AppCompatEditText tCashToken;
    private TextInputLayout containerTCashToken;
    private FancyButton buttonPayment;
    private SemiBoldTextView textTitle;

    private TelkomselCashPaymentPresenter presenter;

    private int attempt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telkomsel_cash);
        initProperties();
        initActionButton();
        bindData();
    }

    private void initActionButton() {
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SdkUIFlowUtil.hideKeyboard(TelkomselCashPaymentActivity.this);
                presenter.trackButtonClick(BUTTON_CONFIRM_NAME, PAGE_NAME);
                String token = tCashToken.getText().toString().trim();
                if (isValidUserId(token)) {
                    showProgressLayout(getString(R.string.processing_payment));
                    presenter.startPayment(token);
                }
            }
        });
    }

    private boolean isValidUserId(String userId) {
        if (TextUtils.isEmpty(userId)) {
            containerTCashToken.setError(getString(R.string.error_empty_tcash_token_field));
            return false;
        }
        containerTCashToken.setError("");
        return true;
    }

    private void bindData() {
        buttonPayment.setText(getString(R.string.confirm_payment));
        textTitle.setText(getString(R.string.telkomsel_cash));
        buttonPayment.setTextBold();

        //track page view after page properly loaded
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);
    }

    private void initProperties() {
        this.presenter = new TelkomselCashPaymentPresenter(this);
    }

    @Override
    public void bindViews() {
        buttonPayment = (FancyButton) findViewById(R.id.button_primary);
        tCashToken = (AppCompatEditText) findViewById(R.id.telkomsel_token_et);
        containerTCashToken = (TextInputLayout) findViewById(R.id.telkomsel_token_til);
        textTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonPayment);
        setBackgroundTintList(tCashToken);
        setTextInputlayoutFilter(containerTCashToken);
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
            SdkUIFlowUtil.showToast(TelkomselCashPaymentActivity.this, getString(R.string.error_message_invalid_input_telkomsel));
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
