package com.midtrans.sdk.ui.views.banktransfer.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentResponse;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseStatusActivity;
import com.midtrans.sdk.ui.adapters.InstructionFragmentPagerAdapter;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.midtrans.sdk.ui.widgets.MagicViewPager;

/**
 * Created by rakawm on 4/13/17.
 */

public class BankTransferMandiriStatusActivity extends BaseStatusActivity {
    public static final String EXTRA_RESPONSE = "extra.response";
    private static final int TABS = 2;

    private TabLayout instructionTabs;
    private MagicViewPager instructionViewPager;
    private FancyButton downloadInstructionButton;

    private TextView companyCodeText;
    private TextView paymentCodeText;
    private TextView validityText;
    private FancyButton copyCompanyCodeButton;
    private FancyButton copyPaymentCodeButton;

    private BankTransferMandiriStatusPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_mandiri_status);
        initPresenter();
        initViews();
        initThemes();
        initValues();
        initInstruction();
    }

    private void initPresenter() {
        MandiriBankTransferPaymentResponse response = (MandiriBankTransferPaymentResponse) getIntent().getSerializableExtra(EXTRA_RESPONSE);
        presenter = new BankTransferMandiriStatusPresenter(response);
    }

    private void initViews() {
        instructionTabs = (TabLayout) findViewById(R.id.instruction_tabs);
        instructionViewPager = (MagicViewPager) findViewById(R.id.instruction_view_pager);
        downloadInstructionButton = (FancyButton) findViewById(R.id.btn_download_instruction);
        companyCodeText = (TextView) findViewById(R.id.company_code_text);
        paymentCodeText = (TextView) findViewById(R.id.payment_code_text);
        validityText = (TextView) findViewById(R.id.validity_text);
        copyCompanyCodeButton = (FancyButton) findViewById(R.id.btn_copy_company_code);
        copyPaymentCodeButton = (FancyButton) findViewById(R.id.btn_copy_payment_code);
    }

    private void initThemes() {
        // Set secondary button
        setBorderColor(copyCompanyCodeButton);
        setTextColor(copyCompanyCodeButton);
        setBorderColor(copyPaymentCodeButton);
        setTextColor(copyPaymentCodeButton);
        setBorderColor(downloadInstructionButton);
        setTextColor(downloadInstructionButton);
        instructionTabs.setSelectedTabIndicatorColor(getPrimaryColor());
    }

    private void initValues() {
        paymentCodeText.setText(presenter.getPaymentCode());
        companyCodeText.setText(presenter.getCompanyCode());
        validityText.setText(getString(R.string.text_format_valid_until, presenter.getExpiration()));
        initCopyButton();
    }

    private void initCopyButton() {
        copyCompanyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.copyTextIntoClipboard(
                        BankTransferMandiriStatusActivity.this,
                        companyCodeText.getText().toString()
                );
            }
        });

        copyPaymentCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.copyTextIntoClipboard(
                        BankTransferMandiriStatusActivity.this,
                        paymentCodeText.getText().toString()
                );
            }
        });
    }

    private void initInstruction() {
        initViewPager();
        initTabs();
        initDownloadInstruction();
    }

    private void initViewPager() {
        instructionViewPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.twenty_dp));
        InstructionFragmentPagerAdapter instructionFragmentPagerAdapter = new InstructionFragmentPagerAdapter(this, PaymentType.E_CHANNEL, getSupportFragmentManager(), TABS);
        instructionViewPager.setAdapter(instructionFragmentPagerAdapter);
    }

    private void initTabs() {
        instructionTabs.setupWithViewPager(instructionViewPager);
        instructionTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                instructionViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initDownloadInstruction() {
        downloadInstructionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.startWebIntent(BankTransferMandiriStatusActivity.this, presenter.getDownloadUrl());
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishPayment();
    }

    @Override
    protected void finishPayment() {
        setResult(RESULT_OK);
        finish();
    }
}
