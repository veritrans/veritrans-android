package com.midtrans.sdk.uikit.views.indomaret.status;

import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.status.PaymentStatusPresenter;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

/**
 * Created by Fajar on 26/10/17.
 */

public class IndomaretStatusActivity extends BasePaymentActivity {
    public static final String EXTRA_PAYMENT_STATUS = "extra.status";
    private static final String LABEL_PAYMENT_CODE = "Payment Code";

    private final String PAGE_NAME = "Indomaret Payment Code";
    private final String BUTTON_CONFIRM_NAME = "Done Indomaret";
    private final String TAG = IndomaretStatusActivity.class.getSimpleName();

    private DefaultTextView textExpiry;
    private SemiBoldTextView textTitle;
    private DefaultTextView textCode;
    private LinearLayout instructionLayout;
    private ImageView barcodeContainer;

    private AppCompatButton buttonInstruction;
    private FancyButton buttonFinish;
    private FancyButton buttonCopyVa;

    private PaymentStatusPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indomaret_status);
        SdkUIFlowUtil.showProgressDialog(this, false);
        TransactionResponse response = (TransactionResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_STATUS);
        presenter = new PaymentStatusPresenter(response);
        initActionButton();
        bindData();
    }

    private void initActionButton() {

        buttonCopyVa.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean copySuccess = copyToClipboard(LABEL_PAYMENT_CODE, presenter.getTransactionResponse().getPaymentCodeResponse());
                SdkUIFlowUtil.showToast(IndomaretStatusActivity.this, copySuccess ? getString(R.string.copied_to_clipboard) : getString(R.string.failed_to_copy));
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.trackButtonClick(BUTTON_CONFIRM_NAME, PAGE_NAME);
                finish();
            }
        });

        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToggleInstructionVisibility();
            }
        });
    }

    private void bindData() {
        TransactionResponse response = presenter.getTransactionResponse();
        if (response != null) {
            if (!TextUtils.isEmpty(response.getStatusCode()) && (response.getStatusCode().equals(
                UiKitConstants.STATUS_CODE_200) || response.getStatusCode().equals(UiKitConstants.STATUS_CODE_201))) {
                textExpiry.setText(getString(R.string.text_format_valid_until, response.getIndomaretExpireTime()));
                if (response.getPaymentCodeResponse() != null) {
                    String formattedCode = getGroupedPaymentCode();

                    ((DefaultTextView) findViewById(R.id.payment_code)).setText(formattedCode);
                    setBarcode(response.getPaymentCodeResponse());
                    textCode.setText(formattedCode);
                }
            } else {
                textExpiry.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_offer_failure));
                textExpiry.setText(getString(R.string.payment_failed));
            }
        }
        buttonFinish.setText(getString(R.string.complete_payment_indomaret));
        buttonFinish.setTextBold();
        textTitle.setText(getString(R.string.indomaret));

        //track page view after page properly loaded
        presenter.trackPageView(PAGE_NAME, false);
        SdkUIFlowUtil.hideProgressDialog();
    }

    @Override
    public void bindViews() {
        barcodeContainer = findViewById(R.id.barcode_container);
        buttonCopyVa = findViewById(R.id.btn_copy_va);
        buttonFinish = findViewById(R.id.button_primary);
        buttonInstruction = findViewById(R.id.instruction_toggle);
        instructionLayout = findViewById(R.id.instruction_layout);
        textExpiry = findViewById(R.id.text_validity);
        textTitle = findViewById(R.id.text_page_title);
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
            Logger.e(e.getMessage());
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
                Logger.e(TAG, "changeToggleInstructionVisibility" + e.getMessage());
            }
        }
    }

    /**
     * Add space after 4 chars to make the payment code easier to read
     * @return
     */
    private String getGroupedPaymentCode() {
        String code = presenter.getTransactionResponse().getPaymentCodeResponse();
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
        setPrimaryBackgroundColor(buttonFinish);
        setTextColor(buttonInstruction);
        setBorderColor(buttonCopyVa);
        setTextColor(buttonCopyVa);
    }

    @Override
    public void onBackPressed() {
        if (presenter != null) {
            presenter.trackBackButtonClick(PAGE_NAME);
        }
        super.onBackPressed();
    }
}
