package com.midtrans.sdk.ui.views.banktransfer.payment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.bni.BniBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentResponse;
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

public class BankTransferPaymentStatusActivity extends BaseStatusActivity {
    public static final String EXTRA_RESPONSE = "extra.response";
    public static final String EXTRA_BANK = "extra.bank";

    private static final int BCA_INSTRUCTION_TABS = 3;
    private static final int PERMATA_INSTRUCTION_TABS = 2;
    private static final int OTHER_INSTRUCTION_TABS = 3;
    private static final int BNI_INSTRUCTION_TABS = 3;

    private ProgressDialog progressDialog;

    private TabLayout instructionTabs;
    private MagicViewPager instructionViewPager;
    private FancyButton downloadInstructionButton;
    private TextView virtualAccountNumberText;
    private TextView validityText;
    private FancyButton copyButton;

    private BankTransferPaymentStatusPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_payment_status);
        initPresenter();
        initViews();
        initThemes();
        initToolbar();
        initProgressDialog();
        initItemDetails();
        initValues();
        initInstruction();
    }

    private void initPresenter() {
        BankTransferPaymentResponse response = (BankTransferPaymentResponse) getIntent().getSerializableExtra(EXTRA_RESPONSE);
        String bank = getIntent().getStringExtra(EXTRA_BANK);
        switch (bank) {
            case PaymentType.BCA_VA:
                BcaBankTransferPaymentResponse bcaResponse = (BcaBankTransferPaymentResponse) response;
                presenter = new BankTransferPaymentStatusPresenter(bcaResponse, bank);
                break;
            case PaymentType.PERMATA_VA:
                PermataBankTransferPaymentResponse permataResponse = (PermataBankTransferPaymentResponse) response;
                presenter = new BankTransferPaymentStatusPresenter(permataResponse, bank);
                break;
            case PaymentType.OTHER_VA:
                OtherBankTransferPaymentResponse otherResponse = (OtherBankTransferPaymentResponse) response;
                presenter = new BankTransferPaymentStatusPresenter(otherResponse, bank);
                break;
            case PaymentType.BNI_VA:
                BniBankTransferPaymentResponse bniResponse = (BniBankTransferPaymentResponse) response;
                presenter = new BankTransferPaymentStatusPresenter(bniResponse, bank);
                break;
        }
    }

    private void initViews() {
        instructionTabs = (TabLayout) findViewById(R.id.instruction_tabs);
        instructionViewPager = (MagicViewPager) findViewById(R.id.instruction_view_pager);
        downloadInstructionButton = (FancyButton) findViewById(R.id.btn_download_instruction);
        virtualAccountNumberText = (TextView) findViewById(R.id.virtual_account_number_text);
        validityText = (TextView) findViewById(R.id.validity_text);
        copyButton = (FancyButton) findViewById(R.id.btn_copy_va);
    }

    private void initThemes() {
        // Set secondary button
        setBorderColor(copyButton);
        setTextColor(copyButton);
        setBorderColor(downloadInstructionButton);
        setTextColor(downloadInstructionButton);
        instructionTabs.setSelectedTabIndicatorColor(getPrimaryColor());

    }

    private void initToolbar() {
        // Set title
        switch (presenter.getBank()) {
            case PaymentType.BCA_VA:
                setHeaderTitle(getString(R.string.bank_bca_transfer));
                break;
            case PaymentType.PERMATA_VA:
                setHeaderTitle(getString(R.string.bank_permata_transfer));
                break;
            case PaymentType.OTHER_VA:
                setHeaderTitle(getString(R.string.other_bank_transfer));
                break;
            case PaymentType.BNI_VA:
                setHeaderTitle(getString(R.string.bank_bni_transfer));
                break;
        }
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
    }

    private void initValues() {
        virtualAccountNumberText.setText(presenter.getVirtualAccountNumber());
        validityText.setText(getString(R.string.text_format_valid_until, presenter.getExpirationText()));
        initCopyButton();
    }

    private void initCopyButton() {
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UiUtils.copyTextIntoClipboard(
                        BankTransferPaymentStatusActivity.this,
                        virtualAccountNumberText.getText().toString()
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
        int pages = 0;
        switch (presenter.getBank()) {
            case PaymentType.BCA_VA:
                pages = BCA_INSTRUCTION_TABS;
                break;
            case PaymentType.PERMATA_VA:
                pages = PERMATA_INSTRUCTION_TABS;
                break;
            case PaymentType.OTHER_VA:
                pages = OTHER_INSTRUCTION_TABS;
                break;
            case PaymentType.BNI_VA:
                pages = BNI_INSTRUCTION_TABS;
                break;
        }

        InstructionFragmentPagerAdapter instructionFragmentPagerAdapter = new InstructionFragmentPagerAdapter(this, presenter.getBank(), getSupportFragmentManager(), pages);
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
                UiUtils.startWebIntent(BankTransferPaymentStatusActivity.this, presenter.getDownloadUrl());
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
