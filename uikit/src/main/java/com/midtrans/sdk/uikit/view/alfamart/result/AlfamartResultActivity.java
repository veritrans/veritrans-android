package com.midtrans.sdk.uikit.view.alfamart.result;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AlfamartPaymentResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.model.PaymentResponse;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.ContextHelper;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

public class AlfamartResultActivity extends BasePaymentActivity {

    private static final String LABEL_PAYMENT_CODE = "Payment Code";

    private final String TAG = AlfamartResultActivity.class.getSimpleName();

    private DefaultTextView textExpiry;
    private DefaultTextView textCode;
    private LinearLayout instructionLayout;

    private AppCompatButton buttonInstruction;
    private FancyButton buttonCopyVa;

    private PaymentResponse paymentResponse;
    private AlfamartPaymentResponse alfamartPaymentResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_convenience_store_alfamart_result);
        showProgressLayout("");
        initProperties();
        bindData();
        initActionButton();
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        alfamartPaymentResponse = (AlfamartPaymentResponse) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_PAYMENT_STATUS);
        paymentResponse = PaymentListHelper.convertTransactionStatus(alfamartPaymentResponse);
    }

    private void initActionButton() {
        buttonCopyVa.setOnClickListener(v -> {
            boolean copySuccess = ContextHelper.copyToClipboard(this, LABEL_PAYMENT_CODE, alfamartPaymentResponse.getPaymentCode());
            MessageHelper.showToast(AlfamartResultActivity.this, copySuccess ? getString(R.string.copied_to_clipboard) : getString(R.string.failed_to_copy));
        });

        buttonCompletePayment.setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            finish();
        });

        buttonInstruction.setOnClickListener(v -> changeToggleInstructionVisibility());
    }

    private void bindData() {
        if (alfamartPaymentResponse != null) {
            if (!TextUtils.isEmpty(alfamartPaymentResponse.getStatusCode()) && (alfamartPaymentResponse.getStatusCode().equals(
                    Constants.STATUS_CODE_200) || alfamartPaymentResponse.getStatusCode().equals(Constants.STATUS_CODE_201))) {
                textExpiry.setText(getString(R.string.text_format_valid_until, alfamartPaymentResponse.getAlfamartExpireTime()));
                if (alfamartPaymentResponse.getPaymentCode() != null) {
                    String formattedCode = getGroupedPaymentCode();

                    ((DefaultTextView) findViewById(R.id.text_payment_code)).setText(formattedCode);
                    textCode.setText(formattedCode);
                }
            } else {
                textExpiry.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_offer_failure));
                textExpiry.setText(getString(R.string.payment_failed));
            }
        }
        buttonCompletePayment.setText(getString(R.string.complete_payment_alfamart));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.alfamart));
        //track page view after page properly loaded
        hideProgressLayout();
    }

    @Override
    public void initToolbarAndView() {
        super.initToolbarAndView();
        buttonCopyVa = findViewById(R.id.btn_copy_va);
        buttonInstruction = findViewById(R.id.instruction_toggle);
        instructionLayout = findViewById(R.id.instruction_layout);
        textExpiry = findViewById(R.id.text_validity);
        textCode = findViewById(R.id.text_payment_code);
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

    /**
     * Add space after 4 chars to make the payment code easier to read
     */
    private String getGroupedPaymentCode() {
        String code = alfamartPaymentResponse.getPaymentCode();
        StringBuilder builder = new StringBuilder();
        if (!TextUtils.isEmpty(code)) {
            for (int index = 0; index < code.length(); index = index + 4) {
                if (index + 4 < code.length()) {
                    builder.append(code.substring(index, index + 4)).append(" ");
                } else {
                    builder.append(code.substring(index));
                }
            }
        }
        return builder.toString();
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
        setTextColor(buttonInstruction);
        setBorderColor(buttonCopyVa);
        setTextColor(buttonCopyVa);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK);
    }
}