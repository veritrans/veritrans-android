package com.midtrans.sdk.uikit.views.bca_klikbca.payment;

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
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.bca_klikbca.status.KlikBcaStatusActivity;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 9/18/17.
 */

public class KlikBcaPaymentActivity extends BasePaymentActivity implements BasePaymentView {
    private TextInputEditText fieldUserId;
    private TextInputLayout containerUserId;
    private FancyButton buttonPayment;
    private DefaultTextView textTitle;

    private KlikBcaPaymentPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bca_klikbca);
        initProperties();
        initActionButton();
        bindData();
    }

    private void initActionButton() {
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SdkUIFlowUtil.hideKeyboard(KlikBcaPaymentActivity.this);
                String userId = fieldUserId.getText().toString().trim();
                if (isValidUserId(userId)) {
                    showProgressLayout(getString(R.string.processing_payment));
                    presenter.startPayment(userId);
                }

                //track event
                presenter.trackEvent(AnalyticsEventName.BTN_CONFIRM_PAYMENT);
            }
        });
    }

    private boolean isValidUserId(String userId) {
        if (TextUtils.isEmpty(userId)) {
            containerUserId.setError(getString(R.string.error_user_id));
            return false;
        }
        containerUserId.setError("");
        return true;
    }

    private void bindData() {
        buttonPayment.setText(getString(R.string.confirm_payment));
        textTitle.setText(getString(R.string.klik_bca));
    }

    private void initProperties() {
        this.presenter = new KlikBcaPaymentPresenter(this);
    }

    @Override
    public void bindViews() {
        buttonPayment = (FancyButton) findViewById(R.id.button_primary);
        fieldUserId = (TextInputEditText) findViewById(R.id.edit_user_id);
        containerUserId = (TextInputLayout) findViewById(R.id.container_user_id);
        textTitle = (DefaultTextView) findViewById(R.id.text_page_title);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonPayment);
        setBackgroundTintList(fieldUserId);
        setTextInputlayoutFilter(containerUserId);
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        hideProgressLayout();
        if (isActivityRunning()) {
            Intent intent = new Intent(this, KlikBcaStatusActivity.class);
            intent.putExtra(KlikBcaStatusActivity.EXTRA_PAYMENT_STATUS, response);
            startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT_STATUS);
        } else {
            finishPayment(RESULT_OK, presenter.getTransactionResponse());
        }
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
        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
            finishPayment(RESULT_OK, presenter.getTransactionResponse());
        }
    }
}
