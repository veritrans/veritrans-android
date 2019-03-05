package com.midtrans.sdk.uikit.view.danamononline;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DanamonOnlineResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.NetworkHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

public class DanamonOnlineInstructionActivity extends BasePaymentActivity implements BasePaymentContract {

    private final String TAG = DanamonOnlineInstructionActivity.class.getSimpleName();

    private DanamonOnlineInstructionPresenter presenter;
    private DanamonOnlineResponse danamonOnlineResponse;

    private AppCompatButton buttonInstruction;
    private LinearLayout instructionLayout;

    private boolean isAlreadyGotResponse = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_online_debit_danamon_online_instruction);
        initToolbarAndView();
        initProperties();
        initActionButton();
    }

    private void initActionButton() {
        buttonCompletePayment.setOnClickListener(v -> {
            if (isAlreadyGotResponse && danamonOnlineResponse != null) {
                showWebViewPaymentPage(PaymentType.DANAMON_ONLINE, danamonOnlineResponse.getRedirectUrl());
            } else {
                showProgressLayout(getString(R.string.processing_payment));
                presenter.startCimbClicksPayment(paymentInfoResponse.getToken());
            }
        });
        buttonCompletePayment.setText(getString(R.string.confirm_payment));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.payment_method_danamon_online));

        buttonInstruction.setOnClickListener(v -> showHideInstruction());
    }

    private void showHideInstruction() {
        int colorPrimary = getPrimaryColor();

        if (colorPrimary != 0) {
            Drawable drawable;
            if (instructionLayout.getVisibility() == View.VISIBLE) {
                drawable = ContextCompat.getDrawable(this, R.drawable.ic_expand_more);
                instructionLayout.setVisibility(View.GONE);
            } else {
                drawable = ContextCompat.getDrawable(this, R.drawable.ic_expand_less);
                instructionLayout.setVisibility(View.VISIBLE);
            }

            try {
                drawable.setColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN);
                buttonInstruction.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            } catch (RuntimeException e) {
                Logger.error(TAG, "changeToggleInstructionVisibility" + e.getMessage());
            }
        }
    }

    private void setCallbackOrSendToStatusPage() {
        if (presenter.isShowPaymentStatusPage()) {
            startResultActivity(Constants.INTENT_CODE_PAYMENT_RESULT, PaymentListHelper.convertTransactionStatus(danamonOnlineResponse));
        } else {
            finishPayment(RESULT_OK, danamonOnlineResponse);
        }
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        presenter = new DanamonOnlineInstructionPresenter(this, paymentInfoResponse);
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        hideProgressLayout();
        danamonOnlineResponse = (DanamonOnlineResponse) response;
        if (danamonOnlineResponse != null) {
            if (NetworkHelper.isPaymentSuccess(danamonOnlineResponse)) {
                showWebViewPaymentPage(PaymentType.DANAMON_ONLINE, danamonOnlineResponse.getRedirectUrl());
            } else {
                setCallbackOrSendToStatusPage();
            }
        } else {
            finishPayment(RESULT_OK, danamonOnlineResponse);
        }
    }

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        showOnErrorPaymentStatusMessage(error);
    }

    @Override
    public void onNullInstanceSdk() {
        setResult(Constants.RESULT_SDK_NOT_AVAILABLE);
        super.onBackPressed();
    }

    @Override
    protected void initToolbarAndView() {
        super.initToolbarAndView();
        buttonInstruction = findViewById(R.id.instruction_toggle);
        instructionLayout = findViewById(R.id.container_instruction);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_WEBVIEW_PAYMENT && resultCode == Activity.RESULT_OK) {
            finishPayment(RESULT_OK, danamonOnlineResponse);
        }
    }
}