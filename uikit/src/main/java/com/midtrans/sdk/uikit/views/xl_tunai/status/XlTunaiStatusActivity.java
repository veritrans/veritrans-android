package com.midtrans.sdk.uikit.views.xl_tunai.status;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.xl_tunai.XlTunaiInstructionActivity;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

/**
 * Created by Fajar on 30/10/17.
 */

public class XlTunaiStatusActivity extends BasePaymentActivity {
    public static final String EXTRA_PAYMENT_STATUS = "extra.status";
    private static final String LABEL_ORDER_ID = "Order ID";
    private static final String LABEL_MERCHANT_CODE = "Merchant Code";

    private SemiBoldTextView textExpiry;
    private SemiBoldTextView textTitle;
    private DefaultTextView textOrderId;
    private DefaultTextView textMerchantCode;

    private FancyButton buttonInstruction;
    private FancyButton buttonFinish;
    private FancyButton buttonCopyOrderId;
    private FancyButton buttonCopyMerchantCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xl_tunai_status);
        initActionButton();
        bindData();
    }

    private void initActionButton() {

        buttonCopyOrderId.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean copySuccess = copyToClipboard(LABEL_ORDER_ID, textOrderId.getText().toString());
                SdkUIFlowUtil.showToast(XlTunaiStatusActivity.this, copySuccess ? getString(R.string.copied_to_clipboard) : getString(R.string.failed_to_copy));
            }
        });

        buttonCopyMerchantCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean copySuccess = copyToClipboard(LABEL_MERCHANT_CODE, textMerchantCode.getText().toString());
                SdkUIFlowUtil.showToast(XlTunaiStatusActivity.this, copySuccess ? getString(R.string.copied_to_clipboard) : getString(R.string.failed_to_copy));
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActivityRunning()) {
                    Intent intent = new Intent(XlTunaiStatusActivity.this, XlTunaiInstructionActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void bindData() {
        TransactionResponse response = (TransactionResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_STATUS);
        if (response != null) {
            if (!TextUtils.isEmpty(response.getStatusCode()) && (response.getStatusCode().equals(
                UiKitConstants.STATUS_CODE_200) || response.getStatusCode().equals(UiKitConstants.STATUS_CODE_201))) {
                textExpiry.setText(response.getXlTunaiExpiration());
                textOrderId.setText(response.getXlTunaiOrderId());
                textMerchantCode.setText(response.getXlTunaiMerchantId());
            }
        }
        buttonFinish.setText(getString(R.string.complete_payment_via_xl_tunai));
        buttonFinish.setTextBold();
        textTitle.setText(getString(R.string.xl_tunai));
    }

    @Override
    public void bindViews() {
        buttonCopyOrderId = (FancyButton) findViewById(R.id.btn_copy_order_id);
        buttonCopyMerchantCode = (FancyButton) findViewById(R.id.btn_copy_merchant_code);
        buttonFinish = (FancyButton) findViewById(R.id.button_primary);
        buttonInstruction = (FancyButton) findViewById(R.id.button_instruction);
        textExpiry = (SemiBoldTextView) findViewById(R.id.text_validity);
        textTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
        textOrderId = (DefaultTextView) findViewById(R.id.text_order_id);
        textMerchantCode = (DefaultTextView) findViewById(R.id.text_merchant_code);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonFinish);
        setTextColor(buttonInstruction);
        setIconColorFilter(buttonInstruction);
        setBorderColor(buttonCopyOrderId);
        setTextColor(buttonCopyOrderId);
        setBorderColor(buttonCopyMerchantCode);
        setTextColor(buttonCopyMerchantCode);
    }
}
