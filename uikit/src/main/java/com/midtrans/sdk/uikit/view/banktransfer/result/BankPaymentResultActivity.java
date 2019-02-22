package com.midtrans.sdk.uikit.view.banktransfer.result;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class BankPaymentResultActivity extends BaseBankTransferResultActivity {

    private DefaultTextView textVaNumber;
    private DefaultTextView textValidity;
    private FancyButton buttonCopyVa;
    private FancyButton buttonInstruction;

    private String pageName, buttonName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_bank_transfer_result);
        initializeTheme();
        initProperties();
        trackPage();
        initData();
        initActionButton();
        initData();
    }

    private void trackPage() {
        String bankType = presenter.getBankType();
        switch (bankType) {
            case PaymentType.BCA_VA:
                buttonName = "Done Bank Transfer BCA";
                pageName = "Bank Transfer BCA Charge";
                break;
            case PaymentType.PERMATA_VA:
                buttonName = "Done Bank Transfer Permata";
                pageName = "Bank Transfer Permata Charge";
                break;
            case PaymentType.BNI_VA:
                buttonName = "Done Bank Transfer BNI";
                pageName = "Bank Transfer BNI Charge";
                break;
            case PaymentType.OTHER_VA:
                buttonName = "Done Bank Transfer All Bank";
                pageName = "Bank Transfer Other Charge";
        }
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

            if (TextUtils.isEmpty(presenter.getInstructionUrl())) {
                buttonInstruction.setVisibility(View.GONE);
            }
        }
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
        buttonCompletePayment.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(pageName) && !TextUtils.isEmpty(buttonName)) {
                finishPaymentStatus();
            }
        });
    }

    @Override
    public void initToolbarAndView() {
        super.initToolbarAndView();
        buttonInstruction = findViewById(R.id.button_download_instruction);
        buttonCopyVa = findViewById(R.id.button_copy_va);
        textValidity = findViewById(R.id.text_validity);
        textVaNumber = findViewById(R.id.text_virtual_account_number);
        buttonCompletePayment = findViewById(R.id.button_primary);
    }
}