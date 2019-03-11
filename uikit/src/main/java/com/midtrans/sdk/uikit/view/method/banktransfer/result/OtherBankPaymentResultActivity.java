package com.midtrans.sdk.uikit.view.method.banktransfer.result;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class OtherBankPaymentResultActivity extends BaseBankTransferResultActivity {

    private DefaultTextView textVaNumber;
    private DefaultTextView textValidity;
    private DefaultTextView textVaBankCode;

    private FancyButton buttonCopyVa;
    private FancyButton buttonInstruction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_bank_transfer_result_other);
        initActionButton();
        initData();
    }

    private void initActionButton() {
        buttonCopyVa.setOnClickListener(v -> copyVaNumber());

        buttonInstruction.setOnClickListener(v -> {
            String instructionUrl = presenter.getInstructionUrl();
            startWebIntent(this, instructionUrl);
        });
    }

    private void copyVaNumber() {
        boolean copySuccess = copyToClipboard(getString(R.string.message_virtual_account), textVaNumber.getText().toString());
        MessageHelper.showToast(this, copySuccess ? getString(R.string.copied_to_clipboard) : getString(R.string.failed_to_copy));
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
    public void initToolbarAndView() {
        super.initToolbarAndView();
        buttonInstruction = findViewById(R.id.button_download_instruction);
        textValidity = findViewById(R.id.text_validity);
        buttonCompletePayment = findViewById(R.id.button_primary);
        buttonInstruction = findViewById(R.id.button_download_instruction);
        textValidity = findViewById(R.id.text_validity);
        buttonCopyVa = findViewById(R.id.button_copy_va);
        textVaNumber = findViewById(R.id.text_virtual_account_number);
        textVaBankCode = findViewById(R.id.text_va_bank_code);
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
        buttonCompletePayment.setOnClickListener(v -> finishPaymentStatus());
    }
}