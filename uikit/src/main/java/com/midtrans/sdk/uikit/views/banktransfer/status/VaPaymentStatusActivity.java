package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
 * Created by ziahaqi on 8/14/17.
 */

public class VaPaymentStatusActivity extends BaseVaPaymentStatusActivity {

    private DefaultTextView textVaNumber;
    private DefaultTextView textValidity;

    private FancyButton buttonCopyVa;
    private FancyButton buttonInstruction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_status);
        initActionButton();
        initData();
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
                SdkUIFlowUtil.startWebIntent(VaPaymentStatusActivity.this, instructionUrl);
            }
        });

    }

    private void copyVaNumber() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.message_virtual_account), textVaNumber.getText().toString());
        clipboard.setPrimaryClip(clip);
        SdkUIFlowUtil.showToast(this, getString(R.string.copied_to_clipboard));
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
    public void bindViews() {
        super.bindViews();

        buttonInstruction = (FancyButton) findViewById(R.id.button_download_instruction);
        buttonCopyVa = (FancyButton) findViewById(R.id.button_copy_va);

        textValidity = (DefaultTextView) findViewById(R.id.text_validity);
        textVaNumber = (DefaultTextView) findViewById(R.id.text_virtual_account_number);

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
    public void onBackPressed() {
        finishPaymentStatus();
    }
}
