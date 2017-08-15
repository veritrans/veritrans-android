package com.midtrans.sdk.uikit.views.banktransfer.status;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.adapters.InstructionPagerAdapter;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.MagicViewPager;

/**
 * Created by ziahaqi on 8/14/17.
 */

public class BankTransferStatusActivity extends BasePaymentActivity {

    public static final String EXTRA_PAYMENT_RESULT = "bank.payment.result";
    public static final String EXTRA_BANK_TYPE = "bank.type";

    private static final int PAGE_MARGIN = 20;
    private static final int CURRENT_POSITION = -1;

    private DefaultTextView textVaNumber;
    private DefaultTextView textValidity;
    private DefaultTextView textTitle;

    private TabLayout tabInstruction;
    private MagicViewPager pagerInstruction;

    private FancyButton buttonCopyVa;
    private FancyButton buttonInstruction;
    private FancyButton buttonCompletePayment;

    private BankTransferStatusPresenter presenter;
    private String bankType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_status);
        initProperties();
        initActionButton();
        initPagerInstruction();
        initTabInstruction();
        initData();
    }

    private void initTabInstruction() {
        tabInstruction.setupWithViewPager(pagerInstruction);
        tabInstruction.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                pagerInstruction.setCurrentItem(position);
                changeConfirmationButtonLable(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initPagerInstruction() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        pagerInstruction.setPageMargin(PAGE_MARGIN);
        int pageNumber;
        switch (bankType) {
            case PaymentType.BCA_VA:
                pageNumber = 3;
                //track page bca va overview
                midtransSDK.trackEvent(AnalyticsEventName.PAGE_BCA_VA_OVERVIEW);

                break;
            case PaymentType.PERMATA_VA:
                pageNumber = 2;

                //track page permata va overview
                midtransSDK.trackEvent(AnalyticsEventName.PAGE_PERMATA_VA_OVERVIEW);
                break;
            case PaymentType.BNI_VA:
                pageNumber = 3;

                //track page permata va overview
                midtransSDK.trackEvent(AnalyticsEventName.PAGE_BNI_VA_OVERVIEW);
                break;
            case PaymentType.E_CHANNEL:
                pageNumber = 2;

                //track page mandiri bill overview
                midtransSDK.trackEvent(AnalyticsEventName.PAGE_MANDIRI_BILL_OVERVIEW);
                break;
            case PaymentType.ALL_VA:
                pageNumber = 3;

                //track page other bank va overview
                midtransSDK.trackEvent(AnalyticsEventName.PAGE_OTHER_BANK_VA_OVERVIEW);
                break;
            default:
                pageNumber = 0;
                break;
        }
        InstructionPagerAdapter adapter = new InstructionPagerAdapter(this, bankType, getSupportFragmentManager(), pageNumber);
        pagerInstruction.setAdapter(adapter);

        if (CURRENT_POSITION > -1) {
            pagerInstruction.setCurrentItem(CURRENT_POSITION);
        }

        pagerInstruction.clearOnPageChangeListeners();
        pagerInstruction.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeConfirmationButtonLable(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void changeConfirmationButtonLable(int position) {
        switch (bankType) {

            case PaymentType.BCA_VA:
                if (position == 0) {
                    buttonCompletePayment.setText(getString(R.string.complete_payment_at_atm));
                } else if (position == 1) {
                    buttonCompletePayment.setText(getString(R.string.complete_payment_at_klik_bca_va));
                } else {
                    buttonCompletePayment.setText(getString(R.string.complete_payment_at_mobile_bca));
                }
                break;

            case PaymentType.PERMATA_VA:
                buttonCompletePayment.setText(getString(R.string.complete_payment_at_atm));
                break;

            case PaymentType.E_CHANNEL:
                if (position == 0) {
                    buttonCompletePayment.setText(getString(R.string.complete_payment_at_atm));
                } else {
                    buttonCompletePayment.setText(getString(R.string.complete_payment_via_internet_banking));
                }
                break;

            case PaymentType.BNI_VA:
                if (position == 0) {
                    buttonCompletePayment.setText(getString(R.string.complete_payment_at_atm));
                } else if (position == 1) {
                    buttonCompletePayment.setText(getString(R.string.complete_payment_va_bni_mobile));
                } else {
                    buttonCompletePayment.setText(getString(R.string.complete_payment_via_internet_banking));
                }

                break;

            default:
                buttonCompletePayment.setText(getString(R.string.complete_payment_at_atm));
                break;
        }
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
                SdkUIFlowUtil.startWebIntent(BankTransferStatusActivity.this, instructionUrl);
            }
        });

        buttonCompletePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void copyVaNumber() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(getString(R.string.message_virtual_account), textVaNumber.getText().toString());
        clipboard.setPrimaryClip(clip);
        SdkUIFlowUtil.showToast(this, getString(R.string.copied_to_clipboard));
    }

    private void initProperties() {

        this.bankType = getIntent().getStringExtra(EXTRA_BANK_TYPE);
        TransactionResponse response = (TransactionResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_RESULT);

        presenter = new BankTransferStatusPresenter(response);
    }

    private void initData() {
        setTitle();

        String vaNumber = presenter.getVaNumber(bankType);
        textVaNumber.setText(vaNumber);

        textValidity.setText(getString(R.string.text_format_valid_until, presenter.getVaExpiration(bankType)));
    }

    private void setTitle() {
        switch (bankType) {
            case PaymentType.BCA_VA:
                setPageTitle(getString(R.string.bank_bca_transfer));
                break;
            case PaymentType.PERMATA_VA:
                setPageTitle(getString(R.string.bank_permata_transfer));
                break;
            case PaymentType.ALL_VA:
                setPageTitle(getString(R.string.other_bank_transfer));
                break;
            case PaymentType.BNI_VA:
                setPageTitle(getString(R.string.bank_bni_transfer));
                break;
        }
    }

    @Override
    public void bindViews() {
        tabInstruction = (TabLayout) findViewById(R.id.tab_instructions);
        pagerInstruction = (MagicViewPager) findViewById(R.id.pager_instruction);

        buttonInstruction = (FancyButton) findViewById(R.id.button_download_instruction);
        buttonCopyVa = (FancyButton) findViewById(R.id.button_copy_va);
        buttonCompletePayment = (FancyButton) findViewById(R.id.button_complete_payment);

        textValidity = (DefaultTextView) findViewById(R.id.text_validity);
        textVaNumber = (DefaultTextView) findViewById(R.id.text_virtual_account_number);
        textTitle = (DefaultTextView) findViewById(R.id.text_page_title);

    }

    @Override
    public void initTheme() {
        setBorderColor(buttonCopyVa);
        setTextColor(buttonCopyVa);
        setBorderColor(buttonInstruction);
        setTextColor(buttonInstruction);
        setPrimaryBackgroundColor(buttonCompletePayment);

        tabInstruction.setSelectedTabIndicatorColor(getPrimaryColor());
    }

    public void setPageTitle(String pageTitle) {
        this.textTitle.setText(pageTitle);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
