package com.midtrans.sdk.uikit.views.xl_tunai.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.xl_tunai.status.XlTunaiStatusActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

/**
 * Created by Fajar on 30/10/17.
 */

public class XlTunaiPaymentActivity extends BasePaymentActivity implements BasePaymentView {

    private final String PAGE_NAME = "XL Tunai Overview";

    private FancyButton buttonPayment;
    private SemiBoldTextView textTitle;

    private XlTunaiPaymentPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xl_tunai_payment);
        initProperties();
        initActionButton();
        bindData();
    }

    private void initActionButton() {
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SdkUIFlowUtil.hideKeyboard(XlTunaiPaymentActivity.this);
                showProgressLayout(getString(R.string.processing_payment));
                presenter.startPayment();
            }
        });
    }

    private void bindData() {
        buttonPayment.setText(getString(R.string.confirm_payment));
        textTitle.setText(getString(R.string.xl_tunai));
        buttonPayment.setTextBold();

        //track page view after page properly loaded
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);
    }

    private void initProperties() {
        this.presenter = new XlTunaiPaymentPresenter(this);
    }

    @Override
    public void bindViews() {
        buttonPayment = (FancyButton) findViewById(R.id.button_primary);
        textTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonPayment);
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        hideProgressLayout();
        if (isActivityRunning()) {
            Intent intent = new Intent(this, XlTunaiStatusActivity.class);
            intent.putExtra(XlTunaiStatusActivity.EXTRA_PAYMENT_STATUS, response);
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

    @Override
    public void onBackPressed() {
        if (presenter != null) {
            presenter.trackBackButtonClick(PAGE_NAME);
        }
        super.onBackPressed();
    }
}
