package com.midtrans.sdk.uikit.views.cimb_click;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by Fajar on 25/10/17.
 */

public class CimbClickPaymentActivity extends BasePaymentActivity implements BasePaymentView {

    private final String PAGE_NAME = "CIMB Clicks";
    private final String BUTTON_CONFIRM_NAME = "Confirm Payment CIMB Clicks";
    private FancyButton buttonPayment;
    private CimbClickPaymentPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cimb_click);
        initProperties();
        initActionButton();
        bindData();
    }

    private void bindData() {
        setPageTitle(getString(R.string.cimb_clicks));
        buttonPayment.setText(getString(R.string.confirm_payment));
        buttonPayment.setTextBold();

        //track page view after page properly loaded
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);
    }

    private void initActionButton() {
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressLayout();
                presenter.trackButtonClick(BUTTON_CONFIRM_NAME, PAGE_NAME);
                presenter.startPayment();
            }
        });
    }

    private void initProperties() {
        this.presenter = new CimbClickPaymentPresenter(this);
    }

    @Override
    public void bindViews() {
        buttonPayment = (FancyButton) findViewById(R.id.button_primary);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonPayment);
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        hideProgressLayout();
        showWebViewPaymentPage(response, PaymentType.CIMB_CLICKS);
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
