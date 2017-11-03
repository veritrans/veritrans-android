package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

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

    private void initActionButton() {
        buttonCopyBillPayCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(LABEL_BILL_CODE, textBillPayCode.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MandiriBillStatusActivity.this, R.string.copied_bill_code, Toast.LENGTH_SHORT).show();
            }
        });

        buttonCopyCompanyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(LABEL_COMPANY_CODE, textCompanyCode.getText().toString());
                clipboard.setPrimaryClip(clip);

                Toast.makeText(MandiriBillStatusActivity.this, R.string.copied_company_code, Toast.LENGTH_SHORT).show();
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
        finishPaymentStatus();
    }
}
