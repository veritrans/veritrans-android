package com.midtrans.sdk.uikit.view.klikbca.result;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.KlikBcaResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.model.MessageInfo;
import com.midtrans.sdk.uikit.base.model.PaymentResponse;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.widget.DefaultTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

public class KlikBcaResultActivity extends BasePaymentActivity {

    private final String TAG = KlikBcaResultActivity.class.getSimpleName();

    private DefaultTextView textExpiry;
    private DefaultTextView textStatusFailed;
    private LinearLayout instructionLayout;

    private AppCompatButton buttonInstruction;

    private PaymentResponse paymentResponse;
    private KlikBcaResponse klikBcaResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_direct_debit_klikbca_result);
        showProgressLayout("");
        initProperties();
        bindData();
        initActionButton();
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        klikBcaResponse = (KlikBcaResponse) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_PAYMENT_STATUS);
        paymentResponse = PaymentListHelper.convertTransactionStatus(klikBcaResponse);
    }

    private void initActionButton() {
        buttonCompletePayment.setOnClickListener(v -> {
            onBackPressed();
        });

        buttonInstruction.setOnClickListener(v -> changeToggleInstructionVisibility());
    }

    private void bindData() {
        if (klikBcaResponse != null) {
            if (!TextUtils.isEmpty(klikBcaResponse.getStatusCode()) && (klikBcaResponse.getStatusCode().equals(
                    Constants.STATUS_CODE_200) || klikBcaResponse.getStatusCode().equals(Constants.STATUS_CODE_201))) {
                textExpiry.setText(getString(R.string.text_format_valid_until, klikBcaResponse.getKlikBcaExpireTime()));
            } else {
                textExpiry.setVisibility(View.GONE);
                MessageInfo messageInfo = MessageHelper.createPaymentFailedMessage(this, paymentResponse);
                textStatusFailed.setText(messageInfo.getDetailsMessage());
            }
        }
        buttonCompletePayment.setText(getString(R.string.complete_payment_at_klik_bca));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.klik_bca));
        //track page view after page properly loaded
        hideProgressLayout();
    }

    @Override
    public void initToolbarAndView() {
        super.initToolbarAndView();
        buttonInstruction = findViewById(R.id.instruction_toggle);
        instructionLayout = findViewById(R.id.instruction_layout);
        textExpiry = findViewById(R.id.text_expiry);
        textStatusFailed = findViewById(R.id.text_status_failed);
    }

    private void changeToggleInstructionVisibility() {
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

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
        setTextColor(buttonInstruction);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}