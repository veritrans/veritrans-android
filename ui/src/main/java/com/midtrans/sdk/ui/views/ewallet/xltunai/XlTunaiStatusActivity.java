package com.midtrans.sdk.ui.views.ewallet.xltunai;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseStatusActivity;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;

/**
 * Created by ziahaqi on 4/10/17.
 */

public class XlTunaiStatusActivity extends BaseStatusActivity {
    public static final String EXTRA_MERCHANT_CODE = "merchant.code";
    public static final String EXTRA_ORDER_ID = "order.id";
    public static final String EXTRA_EXPIRATION = "expiration";
    private static final String LABEL_ORDER_ID = "Order ID";
    private static final String LABEL_MERCHANT_CODE = "Merchant Code";

    private DefaultTextView textOrderId;
    private DefaultTextView textMerchantCode;
    private DefaultTextView textValidUntil;
    private FancyButton buttonCopyOrderId;
    private FancyButton buttonCopyMerchantCode;

    private String orderId;
    private String expirationTime;
    private String merchantCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xl_tunai_status);
        initViews();
        initProperties();
        initCopyButton();
        initValues();
    }

    private void initProperties() {
        orderId = getIntent().getStringExtra(EXTRA_ORDER_ID);
        merchantCode = getIntent().getStringExtra(EXTRA_MERCHANT_CODE);
        expirationTime = getIntent().getStringExtra(EXTRA_EXPIRATION);
    }


    private void initValues() {
        //set merchant data
        textMerchantCode.setText(merchantCode);
        textOrderId.setText(orderId);
        textValidUntil.setText(expirationTime);
    }

    private void initViews() {
        textOrderId = (DefaultTextView) findViewById(R.id.text_order_id);
        textMerchantCode = (DefaultTextView) findViewById(R.id.text_merchant_code);
        textValidUntil = (DefaultTextView) findViewById(R.id.text_validaty);
        buttonCopyOrderId = (FancyButton) findViewById(R.id.btn_copy_order_id);
        buttonCopyMerchantCode = (FancyButton) findViewById(R.id.btn_copy_merchant_code);
    }

    @Override
    public void onBackPressed() {
        finishPayment();
    }

    private void copyOrderId() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_ORDER_ID, textOrderId.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(this, R.string.copied_order_id, Toast.LENGTH_SHORT).show();
    }

    private void copyMerchantCode() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_MERCHANT_CODE, textMerchantCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(this, R.string.copied_merchant_code, Toast.LENGTH_SHORT).show();
    }

    private void initCopyButton() {
        buttonCopyOrderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyOrderId();
            }
        });

        buttonCopyMerchantCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyMerchantCode();
            }
        });
    }

    @Override
    protected void finishPayment() {
        setResult(RESULT_OK);
        finish();
    }
}
