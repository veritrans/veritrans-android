package com.midtrans.sdk.uikit.views.indosat_dompetku;

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
 * Created by Fajar on 31/10/17.
 */

public class IndosatDompetkuPaymentActivity extends BasePaymentActivity implements BasePaymentView {

    private final String PAGE_NAME = "Indosat Dompetku";

    private AppCompatEditText indosatNumber;
    private TextInputLayout containerIndosatNumber;
    private FancyButton buttonPayment;
    private SemiBoldTextView textTitle;

    private IndosatDompetkuPaymentPresenter presenter;

    private int attempt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indosat_dompetku);
        initProperties();
        initActionButton();
        bindData();
    }

    private void initActionButton() {
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SdkUIFlowUtil.hideKeyboard(IndosatDompetkuPaymentActivity.this);
                String number = indosatNumber.getText().toString().trim();
                if (isValidNumber(number)) {
                    showProgressLayout(getString(R.string.processing_payment));
                    presenter.startPayment(number);
                }
            }
        });
    }

    private boolean isValidNumber(String userId) {
        if (TextUtils.isEmpty(userId)) {
            containerIndosatNumber.setError(getString(R.string.indosat_error_empty_number));
            return false;
        } else if (userId.length() < 10) {
            containerIndosatNumber.setError(getString(R.string.error_invalid_phone_number));
            return false;
        }
        containerIndosatNumber.setError("");
        return true;
    }

    private void bindData() {
        buttonPayment.setText(getString(R.string.confirm_payment));
        textTitle.setText(getString(R.string.indosat_dompetku));
        buttonPayment.setTextBold();

        //track page view after page properly loaded
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);
    }

    private void initProperties() {
        this.presenter = new IndosatDompetkuPaymentPresenter(this);
    }

    @Override
    public void bindViews() {
        buttonPayment = (FancyButton) findViewById(R.id.button_primary);
        indosatNumber = (AppCompatEditText) findViewById(R.id.et_indosat_phone_number);
        containerIndosatNumber = (TextInputLayout) findViewById(R.id.til_indosat_phone_number);
        textTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonPayment);
        setBackgroundTintList(indosatNumber);
        setTextInputlayoutFilter(containerIndosatNumber);
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
            SdkUIFlowUtil.showToast(IndosatDompetkuPaymentActivity.this, getString(R.string.error_something_wrong));
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
