package com.midtrans.sdk.uikit.views.danamon_online;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.core.Logger;
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
    private final String TAG = DanamonOnlineActivity.class.getSimpleName();

    private FancyButton buttonPrimary;
    private AppCompatButton buttonInstruction;
    private LinearLayout instructionLayout;

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
        buttonPrimary.setTextBold();
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
                Logger.e(TAG, "changeToggleInstructionVisibility" + e.getMessage());
            }
        }
    }

    private void initProperties() {
        this.presenter = new DanamonOnlinePresenter(this);
    }


    @Override
    public void bindViews() {
        buttonPrimary = findViewById(R.id.button_primary);
        buttonInstruction = findViewById(R.id.instruction_toggle);
        instructionLayout = findViewById(R.id.container_instruction);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonPrimary);
        setTextColor(buttonInstruction);
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
