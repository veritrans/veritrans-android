package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BaseVaPaymentStatusActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 8/15/17.
 */

public class MandiriBillStatusActivity extends BaseVaPaymentStatusActivity {

    private static final String LABEL_BILL_CODE = "Bill Code Number";
    private static final String LABEL_COMPANY_CODE = "Company Code Number";
    private final String PAGE_NAME = "Bank Transfer Mandiri Charge";
    private final String CONFIRM_BUTTON_NAME = "Done Bank Transfer Mandiri";

    private DefaultTextView textBillPayCode;
    private DefaultTextView textCompanyCode;
    private DefaultTextView textValidity;

    private FancyButton buttonCopyBillPayCode;
    private FancyButton buttonCopyCompanyCode;
    private FancyButton buttonInstruction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandiri_bill_status);
        initActionButton();
        initData();
    }

    private void initData() {
        textBillPayCode.setText(presenter.getVaNumber());
        textCompanyCode.setText(presenter.getCompanyCode());
        textValidity.setText(getString(R.string.text_format_valid_until, presenter.getMandiriBillExpiration()));


        if (TextUtils.isEmpty(presenter.getInstructionUrl())) {
            buttonInstruction.setVisibility(View.GONE);
        }

        initStatusPayment();

        //track page view after page properly loaded
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);
    }

    private void initStatusPayment() {
        if (presenter.isPaymentFailed()) {
            textValidity.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_offer_failure));
            textValidity.setText(getString(R.string.payment_failed));
            textBillPayCode.setText("");
            textCompanyCode.setText("");
            buttonCopyBillPayCode.setEnabled(false);
            buttonCopyCompanyCode.setEnabled(false);
            buttonInstruction.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initCompletePaymentButton() {
        buttonCompletePayment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.trackButtonClick(CONFIRM_BUTTON_NAME, PAGE_NAME);
                finishPaymentStatus();
            }
        });
    }

    private void initActionButton() {
        buttonCopyBillPayCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean copyBillCodeSuccess = copyToClipboard(LABEL_BILL_CODE, textBillPayCode.getText().toString());
                SdkUIFlowUtil.showToast(MandiriBillStatusActivity.this, copyBillCodeSuccess ? getString(R.string.copied_bill_code) : getString(R.string.failed_to_copy));
            }
        });

        buttonCopyCompanyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean copyCompanyCodeSuccess = copyToClipboard(LABEL_COMPANY_CODE, textCompanyCode.getText().toString());
                SdkUIFlowUtil.showToast(MandiriBillStatusActivity.this, copyCompanyCodeSuccess ? getString(R.string.copied_company_code) : getString(R.string.failed_to_copy));
            }
        });

        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instructionUrl = presenter.getInstructionUrl();
                SdkUIFlowUtil.startWebIntent(MandiriBillStatusActivity.this, instructionUrl);
            }
        });

    }

    @Override
    public void bindViews() {
        super.bindViews();
        buttonCopyCompanyCode = (FancyButton) findViewById(R.id.button_copy_company_code);
        buttonCopyBillPayCode = (FancyButton) findViewById(R.id.button_copy_bill_pay);
        buttonInstruction = (FancyButton) findViewById(R.id.button_download_instruction);


        textBillPayCode = (DefaultTextView) findViewById(R.id.text_bill_pay_code);
        textCompanyCode = (DefaultTextView) findViewById(R.id.text_company_code);
        textValidity = (DefaultTextView) findViewById(R.id.text_validity);
    }

    @Override
    public void initTheme() {
        super.initTheme();
        setBorderColor(buttonCopyBillPayCode);
        setTextColor(buttonCopyBillPayCode);
        setBorderColor(buttonCopyCompanyCode);
        setTextColor(buttonCopyCompanyCode);
        setTextColor(buttonInstruction);
        setIconColorFilter(buttonInstruction);
        setPrimaryBackgroundColor(buttonCompletePayment);
    }

    @Override
    public void onBackPressed() {
        if (presenter != null) {
            presenter.trackBackButtonClick(PAGE_NAME);
        }
        finishPaymentStatus();
    }
}
