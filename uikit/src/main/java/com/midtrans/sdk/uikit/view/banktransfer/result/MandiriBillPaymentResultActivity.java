package com.midtrans.sdk.uikit.view.banktransfer.result;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class MandiriBillPaymentResultActivity extends BaseBankTransferResultActivity {

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
        setContentView(R.layout.activity_payment_bank_transfer_result_mandiri_bill);
        initActionButton();
        initData();
    }

    private void initData() {
        textBillPayCode.setText(presenter.getVaNumber());
        textCompanyCode.setText(presenter.getCompanyCode());
        textValidity.setText(getString(R.string.text_format_valid_until, presenter.getMandiriResponse().getBillPaymentExpiration()));
        if (TextUtils.isEmpty(presenter.getInstructionUrl())) {
            buttonInstruction.setVisibility(View.GONE);
        }
        initStatusPayment();
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
        buttonCompletePayment.setOnClickListener(v -> finishPaymentStatus());
    }

    private void initActionButton() {
        buttonCopyBillPayCode.setOnClickListener(v -> {
            boolean copyBillCodeSuccess = copyToClipboard(LABEL_BILL_CODE, textBillPayCode.getText().toString());
            MessageHelper.showToast(this, copyBillCodeSuccess ? getString(R.string.copied_bill_code) : getString(R.string.failed_to_copy));
        });

        buttonCopyCompanyCode.setOnClickListener(v -> {
            boolean copyCompanyCodeSuccess = copyToClipboard(LABEL_COMPANY_CODE, textCompanyCode.getText().toString());
            MessageHelper.showToast(this, copyCompanyCodeSuccess ? getString(R.string.copied_company_code) : getString(R.string.failed_to_copy));
        });

        buttonInstruction.setOnClickListener(v -> {
            String instructionUrl = presenter.getInstructionUrl();
            startWebIntent(this, instructionUrl);
        });
    }

    @Override
    public void initToolbarAndView() {
        super.initToolbarAndView();
        buttonInstruction = findViewById(R.id.button_download_instruction);
        textValidity = findViewById(R.id.text_validity);
        buttonCompletePayment = findViewById(R.id.button_primary);
        buttonCopyCompanyCode = findViewById(R.id.button_copy_company_code);
        buttonCopyBillPayCode = findViewById(R.id.button_copy_bill_pay);
        buttonInstruction = findViewById(R.id.button_download_instruction);
        textBillPayCode = findViewById(R.id.text_bill_pay_code);
        textCompanyCode = findViewById(R.id.text_company_code);
        textValidity = findViewById(R.id.text_validity);
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

}