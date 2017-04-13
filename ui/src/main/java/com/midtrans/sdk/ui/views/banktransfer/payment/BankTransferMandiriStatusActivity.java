package com.midtrans.sdk.ui.views.banktransfer.payment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.InstructionFragmentPagerAdapter;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.midtrans.sdk.ui.widgets.MagicViewPager;
import com.squareup.picasso.Picasso;

/**
 * Created by rakawm on 4/13/17.
 */

public class BankTransferMandiriStatusActivity extends BaseActivity {
    public static final String EXTRA_RESPONSE = "extra.response";
    private static final int TABS = 2;

    private MidtransUi midtransUi;

    private RecyclerView itemDetails;
    private TextView titleText;
    private FancyButton finishButton;
    private ProgressDialog progressDialog;

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
        initMidtransUi();
        initPresenter();
        initViews();
        initThemes();
        initToolbar();
        initProgressDialog();
        initItemDetails();
        initFinishButton();
        initValues();
        initInstruction();
    }

    private void initMidtransUi() {
        midtransUi = MidtransUi.getInstance();
    }

    private void initPresenter() {
        MandiriBankTransferPaymentResponse response = (MandiriBankTransferPaymentResponse) getIntent().getSerializableExtra(EXTRA_RESPONSE);
        presenter = new BankTransferMandiriStatusPresenter(response);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        itemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        finishButton = (FancyButton) findViewById(R.id.btn_finish);
        instructionTabs = (TabLayout) findViewById(R.id.instruction_tabs);
        instructionViewPager = (MagicViewPager) findViewById(R.id.instruction_view_pager);
        downloadInstructionButton = (FancyButton) findViewById(R.id.btn_download_instruction);
        companyCodeText = (TextView) findViewById(R.id.company_code_text);
        paymentCodeText = (TextView) findViewById(R.id.payment_code_text);
        validityText = (TextView) findViewById(R.id.validity_text);
        titleText = (TextView) findViewById(R.id.page_title);
        copyCompanyCodeButton = (FancyButton) findViewById(R.id.btn_copy_company_code);
        copyPaymentCodeButton = (FancyButton) findViewById(R.id.btn_copy_payment_code);
    }

    private void initThemes() {
        // Set color theme for item details bar
        setBackgroundColor(itemDetails, Theme.PRIMARY_COLOR);
        setBackgroundColor(finishButton, Theme.PRIMARY_COLOR);
        // Set secondary button
        setBorderColor(copyCompanyCodeButton);
        setTextColor(copyCompanyCodeButton);
        setBorderColor(copyPaymentCodeButton);
        setTextColor(copyPaymentCodeButton);
        setBorderColor(downloadInstructionButton);
        setTextColor(downloadInstructionButton);
        instructionTabs.setSelectedTabIndicatorColor(getPrimaryColor());
        // Set font into pay now button
        if (!TextUtils.isEmpty(midtransUi.getSemiBoldFontPath())) {
            finishButton.setCustomTextFont(midtransUi.getSemiBoldFontPath());
        }
        initThemeColor();
    }

    private void initToolbar() {
        // Set title
        setHeaderTitle(getString(R.string.mandiri_bill_transfer));


        // Set merchant logo
        ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        Picasso.with(this)
                .load(midtransUi.getTransaction().merchant.preference.logoUrl)
                .into(merchantLogo);

        initToolbarBackButton();
        // Set back button click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
    }

    private void initItemDetails() {
        ItemDetailsAdapter itemDetailsAdapter = new ItemDetailsAdapter(new ItemDetailsAdapter.ItemDetailListener() {
            @Override
            public void onItemShown() {
                // Do nothing
            }
        }, presenter.createItemDetails(this));
        itemDetails.setLayoutManager(new LinearLayoutManager(this));
        itemDetails.setAdapter(itemDetailsAdapter);
    }

    private void initFinishButton() {
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishPayment();
            }
        });
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

    private void setHeaderTitle(String title) {
        titleText.setText(title);
    }

    @Override
    public void onBackPressed() {
        finishPayment();
    }

    private void finishPayment() {
        setResult(RESULT_OK);
        finish();
    }
}
