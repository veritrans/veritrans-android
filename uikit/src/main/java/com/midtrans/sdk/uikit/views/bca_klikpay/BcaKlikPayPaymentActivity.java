package com.midtrans.sdk.uikit.views.bca_klikpay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 9/19/17.
 */

public class BcaKlikPayPaymentActivity extends BasePaymentActivity implements BasePaymentView {

    private FancyButton buttonPayment;
    private BcaKlikPayPaymentPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bca_klikpay);
        initProperties();
        initActionButton();
        bindData();
    }

    private void bindData() {
        setPageTitle(getString(R.string.bca_klik));
        buttonPayment.setText(getString(R.string.confirm_payment));
    }

    private void initActionButton() {
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressLayout();
                presenter.startPayment();
                presenter.trackEvent(AnalyticsEventName.BTN_CONFIRM_PAYMENT);
            }
        });
    }

    private void initProperties() {
        this.presenter = new BcaKlikPayPaymentPresenter(this);
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
        showWebViewPaymentPage(response, PaymentType.BCA_KLIKPAY);
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
}
