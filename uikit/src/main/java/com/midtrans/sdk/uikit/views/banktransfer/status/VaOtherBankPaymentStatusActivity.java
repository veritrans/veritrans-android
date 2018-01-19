package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BaseVaPaymentStatusActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 1/18/18.
 */

public class VaOtherBankPaymentStatusActivity extends BaseVaPaymentStatusActivity {


    private DefaultTextView textVaNumber;
    private DefaultTextView textValidity;
    private DefaultTextView textVaBankCode;

    private FancyButton buttonCopyVa;
    private FancyButton buttonInstruction;


    private String pageName, buttonName;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_bank_status);
        initActionButton();
        initData();
        trackPage();
    }

    private void initActionButton() {
        buttonCopyVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyVaNumber();
            }
        });

        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String instructionUrl = presenter.getInstructionUrl();
                SdkUIFlowUtil.startWebIntent(VaOtherBankPaymentStatusActivity.this, instructionUrl);
            }
        });

    }

    private void copyVaNumber() {
        boolean copySuccess = copyToClipboard(getString(R.string.message_virtual_account), textVaNumber.getText().toString());
        SdkUIFlowUtil.showToast(this, copySuccess ? getString(R.string.copied_to_clipboard) : getString(R.string.failed_to_copy));
    }

    private void initData() {
        if (presenter.isPaymentFailed()) {
            textValidity.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_offer_failure));
            textValidity.setText(getString(R.string.payment_failed));
            textVaNumber.setText("");
            buttonCopyVa.setEnabled(false);
            buttonInstruction.setVisibility(View.GONE);
        } else {
            textVaNumber.setText(presenter.getVaNumber());
            textValidity.setText(getString(R.string.text_format_valid_until, presenter.getVaExpiration()));
            textVaBankCode.setText(presenter.getBankCode());
            if (TextUtils.isEmpty(presenter.getInstructionUrl())) {
                buttonInstruction.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void bindViews() {
        super.bindViews();
        buttonInstruction = findViewById(R.id.button_download_instruction);
        buttonCopyVa = findViewById(R.id.button_copy_va);

        textValidity = findViewById(R.id.text_validity);
        textVaNumber = findViewById(R.id.text_virtual_account_number);
        textVaBankCode = findViewById(R.id.text_va_bank_code);
    }

    private void trackPage() {
        buttonName = "Done Bank Transfer All Bank";
        pageName = "Bank Transfer Other Charge";
        presenter.trackPageView(pageName, false);
    }

    @Override
    public void initTheme() {
        super.initTheme();
        setBorderColor(buttonCopyVa);
        setTextColor(buttonCopyVa);
        setTextColor(buttonInstruction);
        setIconColorFilter(buttonInstruction);
        setPrimaryBackgroundColor(buttonCompletePayment);
    }

    @Override
    protected void initCompletePaymentButton() {
        buttonCompletePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(pageName) && !TextUtils.isEmpty(buttonName)) {
                    presenter.trackButtonClick(buttonName, pageName);
                    finishPaymentStatus();
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (presenter != null && !TextUtils.isEmpty(pageName)) {
            presenter.trackBackButtonClick(pageName);
        }
        finishPaymentStatus();
    }

}
