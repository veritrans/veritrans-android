package com.midtrans.sdk.uikit.views.danamon_online;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.abstracts.BasePaymentView;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 9/13/17.
 */

public class DanamonOnlineActivity extends BasePaymentActivity implements BasePaymentView {

    private FancyButton buttonPrimary;
    private FancyButton buttonInstruction;
    private LinearLayout containerInstruction;

    private DanamonOnlinePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danamon_online);
        initProperties();
        initActionButton();
        bindData();
    }

    private void bindData() {
        setPageTitle(getString(R.string.payment_method_danamon_online));
        buttonPrimary.setText(getString(R.string.confirm_payment));
    }

    private void initActionButton() {
        buttonPrimary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressLayout(getString(R.string.processing_payment));
                presenter.startPayment();
            }
        });

        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHideInstruction();
            }
        });
    }

    private void showHideInstruction() {
        if (containerInstruction.getVisibility() == View.VISIBLE) {
            containerInstruction.setVisibility(View.GONE);
            buttonInstruction.setText(getString(R.string.show_instruction));
            buttonInstruction.setIconResource(R.drawable.ic_view);
        } else {
            containerInstruction.setVisibility(View.VISIBLE);
            buttonInstruction.setText(getString(R.string.hide_instruction));
            buttonInstruction.setIconResource(R.drawable.ic_hide);
        }
        setIconColorFilter(buttonInstruction);
    }

    private void initProperties() {
        this.presenter = new DanamonOnlinePresenter(this);
    }


    @Override
    public void bindViews() {
        buttonPrimary = (FancyButton) findViewById(R.id.button_primary);
        buttonInstruction = (FancyButton) findViewById(R.id.button_toggle_instruction);
        containerInstruction = (LinearLayout) findViewById(R.id.container_instruction);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonPrimary);
        setTextColor(buttonInstruction);
        setIconColorFilter(buttonInstruction);
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        hideProgressLayout();
        showWebViewPaymentPage(response, PaymentType.DANAMON_ONLINE);
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
