package com.midtrans.sdk.uikit.views.kioson.status;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.activities.KiosonInstructionActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.status.PaymentStatusPresenter;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

/**
 * Created by Fajar on 26/10/17.
 */

public class KiosonStatusActivity extends BasePaymentActivity {

    public static final String EXTRA_PAYMENT_STATUS = "extra.status";
    private static final String LABEL_PAYMENT_CODE = "Payment Code";
    private final String PAGE_NAME = "Kioson Payment Code";
    private final String BUTTON_CONFIRM_NAME = "Done Kioson";

    private SemiBoldTextView textExpiry;
    private SemiBoldTextView textTitle;
    private DefaultTextView textCode;

    private FancyButton buttonInstruction;
    private FancyButton buttonFinish;
    private FancyButton buttonCopyVa;

    private PaymentStatusPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kioson_status);
        presenter = new PaymentStatusPresenter();
        initActionButton();
        bindData();
    }

    private void initActionButton() {

        buttonCopyVa.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean copySuccess = copyToClipboard(LABEL_PAYMENT_CODE, textCode.getText().toString());
                SdkUIFlowUtil.showToast(KiosonStatusActivity.this, copySuccess ? getString(R.string.copied_to_clipboard) : getString(R.string.failed_to_copy));
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
                if (isActivityRunning()) {
                    Intent intent = new Intent(KiosonStatusActivity.this, KiosonInstructionActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void bindData() {
        TransactionResponse response = (TransactionResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_STATUS);
        if (response != null) {
            if (!TextUtils.isEmpty(response.getStatusCode()) && (response.getStatusCode().equals(UiKitConstants.STATUS_CODE_200) || response.getStatusCode().equals(UiKitConstants.STATUS_CODE_201))) {
                textExpiry.setText(response.getKiosonExpireTime());
                if (response.getPaymentCodeResponse() != null) {
                    textCode.setText(response.getPaymentCodeResponse());
                }
            }
        }
        buttonFinish.setText(getString(R.string.complete_payment_kioson));
        buttonFinish.setTextBold();
        textTitle.setText(getString(R.string.kioson));

        //track page view after page properly loaded
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);
    }

    @Override
    public void bindViews() {
        buttonCopyVa = (FancyButton) findViewById(R.id.btn_copy_va);
        buttonFinish = (FancyButton) findViewById(R.id.button_primary);
        buttonInstruction = (FancyButton) findViewById(R.id.button_instruction);
        textExpiry = (SemiBoldTextView) findViewById(R.id.text_validity);
        textTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
        textCode = (DefaultTextView) findViewById(R.id.text_payment_code);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonFinish);
        setTextColor(buttonInstruction);
        setIconColorFilter(buttonInstruction);
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
