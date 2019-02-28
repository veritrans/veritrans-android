package com.midtrans.sdk.uikit.view.indomaret.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.IndomaretPaymentResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.model.PaymentResponse;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.ContextHelper;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.view.status.PaymentStatusPresenter;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

public class IndomaretResultActivity extends BasePaymentActivity {

    private static final String LABEL_PAYMENT_CODE = "Payment Code";

    private final String PAGE_NAME = "Indomaret Payment Code";
    private final String BUTTON_CONFIRM_NAME = "Done Indomaret";
    private final String TAG = IndomaretResultActivity.class.getSimpleName();

    private DefaultTextView textExpiry;
    private DefaultTextView textCode;
    private LinearLayout instructionLayout;
    private ImageView barcodeContainer;

    private AppCompatButton buttonInstruction;
    private FancyButton buttonCopyVa;

    private PaymentResponse paymentResponse;
    private IndomaretPaymentResponse indomaretPaymentResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_convenience_store_indomaret_result);
        showProgressLayout("");
        initProperties();
        bindData();
        initActionButton();
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        indomaretPaymentResponse = (IndomaretPaymentResponse) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_PAYMENT_STATUS);
        paymentResponse = PaymentListHelper.convertTransactionStatus(indomaretPaymentResponse);
    }

    private void initActionButton() {
        buttonCopyVa.setOnClickListener(v -> {
            boolean copySuccess = ContextHelper.copyToClipboard(this, LABEL_PAYMENT_CODE, indomaretPaymentResponse.getPaymentCode());
            MessageHelper.showToast(IndomaretResultActivity.this, copySuccess ? getString(R.string.copied_to_clipboard) : getString(R.string.failed_to_copy));
        });

        buttonCompletePayment.setOnClickListener(v -> {
            setResult(Activity.RESULT_OK);
            finish();
        });

        buttonInstruction.setOnClickListener(v -> changeToggleInstructionVisibility());
    }

    private void bindData() {
        if (indomaretPaymentResponse != null) {
            if (!TextUtils.isEmpty(indomaretPaymentResponse.getStatusCode()) && (indomaretPaymentResponse.getStatusCode().equals(
                    Constants.STATUS_CODE_200) || indomaretPaymentResponse.getStatusCode().equals(Constants.STATUS_CODE_201))) {
                textExpiry.setText(getString(R.string.text_format_valid_until, indomaretPaymentResponse.getIndomaretExpireTime()));
                if (indomaretPaymentResponse.getPaymentCode() != null) {
                    String formattedCode = getGroupedPaymentCode();

                    ((DefaultTextView) findViewById(R.id.payment_code)).setText(formattedCode);
                    setBarcode(indomaretPaymentResponse.getPaymentCode());
                    textCode.setText(formattedCode);
                }
            } else {
                textExpiry.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_offer_failure));
                textExpiry.setText(getString(R.string.payment_failed));
            }
        }
        buttonCompletePayment.setText(getString(R.string.complete_payment_indomaret));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.indomaret));
        //track page view after page properly loaded
        hideProgressLayout();
    }

    @Override
    public void initToolbarAndView() {
        super.initToolbarAndView();
        barcodeContainer = findViewById(R.id.barcode_container);
        buttonCopyVa = findViewById(R.id.btn_copy_va);
        buttonInstruction = findViewById(R.id.instruction_toggle);
        instructionLayout = findViewById(R.id.instruction_layout);
        textExpiry = findViewById(R.id.text_validity);
        textCode = findViewById(R.id.text_payment_code);
    }

    private void setBarcode(String paymentCode) {
        MultiFormatWriter formatWriter = new MultiFormatWriter();
        try {
            int width = getResources().getDimensionPixelSize(R.dimen.barcode_width);
            int height = getResources().getDimensionPixelSize(R.dimen.barcode_height);
            BitMatrix bitMatrix = formatWriter.encode(paymentCode, BarcodeFormat.CODE_39, width, height);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);
            if (barcodeContainer != null) {
                barcodeContainer.setImageBitmap(bitmap);
                barcodeContainer.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Logger.error(e.getMessage());
        }
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
        String code = indomaretPaymentResponse.getPaymentCode();
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