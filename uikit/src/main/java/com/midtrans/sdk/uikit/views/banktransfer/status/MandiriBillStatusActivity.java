package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BaseBankTransferStatusActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 8/15/17.
 */

public class MandiriBillStatusActivity extends BaseBankTransferStatusActivity {

    private static final String LABEL_BILL_CODE = "Bill Code Number";
    private static final String LABEL_COMPANY_CODE = "Company Code Number";

    private DefaultTextView textBillPayCode;
    private DefaultTextView textCompanyCode;

    private FancyButton buttonCopyBillPayCode;
    private FancyButton buttonCopyCompanyCode;
    private FancyButton buttonInstruction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandiri_bill_status);
        initActionButton();
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
        buttonCopyCompanyCode = (FancyButton) findViewById(R.id.button_copy_company_code);
        buttonCopyBillPayCode = (FancyButton) findViewById(R.id.button_copy_bill_pay);
        buttonInstruction = (FancyButton) findViewById(R.id.button_download_instruction);


        textBillPayCode = (DefaultTextView) findViewById(R.id.text_bill_pay_code);
        textCompanyCode = (DefaultTextView) findViewById(R.id.text_company_code);
    }

    @Override
    public void initTheme() {
        super.initTheme();
        setBorderColor(buttonCopyBillPayCode);
        setTextColor(buttonCopyCompanyCode);
        setBorderColor(buttonInstruction);
        setTextColor(buttonInstruction);
        setPrimaryBackgroundColor(buttonCompletePayment);
    }
}
