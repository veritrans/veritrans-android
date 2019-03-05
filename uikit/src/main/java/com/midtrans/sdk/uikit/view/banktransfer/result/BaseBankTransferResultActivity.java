package com.midtrans.sdk.uikit.view.banktransfer.result;

import com.google.android.material.tabs.TabLayout;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaBankTransferReponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BniBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriBillResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.OtherBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PermataBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.merchantdata.MerchantPreferences;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BaseActivity;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.view.PaymentListActivity;
import com.midtrans.sdk.uikit.view.banktransfer.instruction.instruction.adapter.InstructionPagerAdapter;
import com.midtrans.sdk.uikit.widget.FancyButton;
import com.midtrans.sdk.uikit.widget.MagicViewPager;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public abstract class BaseBankTransferResultActivity extends BaseActivity {

    public static final String EXTRA_PAYMENT_RESULT_BCA = "bank.payment.result.bca";
    public static final String EXTRA_PAYMENT_RESULT_BNI = "bank.payment.result.bni";
    public static final String EXTRA_PAYMENT_RESULT_MANDIRI = "bank.payment.result.mandiri";
    public static final String EXTRA_PAYMENT_RESULT_PERMATA = "bank.payment.result.permata";
    public static final String EXTRA_PAYMENT_RESULT_OTHER = "bank.payment.result.other";
    public static final String EXTRA_BANK_TYPE = "bank.type";

    private static final int PAGE_MARGIN = 20;
    private static final int CURRENT_POSITION = -1;

    protected TabLayout tabInstruction;
    protected MagicViewPager pagerInstruction;
    protected FancyButton buttonCompletePayment;
    protected BankTransferResultPresenter presenter;
    private PaymentInfoResponse paymentInfoResponse;
    private String paymentType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProperties();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initToolbarAndView();
        initCompletePaymentButton();
        initMerchantPreferences();
        initPagerInstruction();
        initTabInstruction();
        setTitle();
        initTheme();
        initItemDetails(paymentInfoResponse);
    }

    /**
     * This method used for binding view and setup other view stuff like toolbar and progress image
     */
    protected void initToolbarAndView() {
        toolbar = findViewById(R.id.toolbar_base);
        buttonCompletePayment = findViewById(R.id.button_primary);
        tabInstruction = findViewById(R.id.tab_instructions);
        pagerInstruction = findViewById(R.id.pager_instruction);

        tabInstruction = findViewById(R.id.tab_instructions);

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> finishPaymentStatus());
        }
    }

    /**
     * This method use for setup view stuff based on response and merchant preferences
     */
    private void initMerchantPreferences() {
        String paymentType = getIntent().getStringExtra(EXTRA_BANK_TYPE);
        MerchantPreferences preferences = paymentInfoResponse.getMerchantData().getPreference();
        if (!TextUtils.isEmpty(preferences.getLogoUrl())) {
            merchantNameInToolbar.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(paymentType)) {
            paymentMethodTitleInToolbar.setText(PaymentListHelper.mappingPaymentTitle(this, paymentType));
            paymentMethodTitleInToolbar.setVisibility(View.VISIBLE);
        }
    }

    public void initTheme() {
        tabInstruction.setSelectedTabIndicatorColor(getPrimaryColor());
    }

    protected void initProperties() {
        paymentType = getIntent().getStringExtra(EXTRA_BANK_TYPE);
        paymentInfoResponse = (PaymentInfoResponse) getIntent().getSerializableExtra(PaymentListActivity.EXTRA_PAYMENT_INFO);
        BcaBankTransferReponse responseBca = (BcaBankTransferReponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_RESULT_BCA);
        BniBankTransferResponse responseBni = (BniBankTransferResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_RESULT_BNI);
        MandiriBillResponse responseMandiri = (MandiriBillResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_RESULT_MANDIRI);
        PermataBankTransferResponse responsePermata = (PermataBankTransferResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_RESULT_PERMATA);
        OtherBankTransferResponse responseOther = (OtherBankTransferResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_RESULT_OTHER);
        presenter = BankTransferResultPresenter
                .builder(paymentType, paymentInfoResponse)
                .setBcaResponse(responseBca)
                .setBniResponse(responseBni)
                .setMandiriResponse(responseMandiri)
                .setPermataResponse(responsePermata)
                .setOtherResponse(responseOther)
                .build();
    }

    protected abstract void initCompletePaymentButton();

    protected void finishPaymentStatus() {
        Intent intent = new Intent();
        Logger.debug("Payment you should finish is >>> " + paymentType);
        switch (paymentType) {
            case PaymentType.BCA_VA:
                intent.putExtra(Constants.INTENT_DATA_CALLBACK, presenter.getBcaResponse());
                intent.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.BCA_VA);
                break;
            case PaymentType.BNI_VA:
                intent.putExtra(Constants.INTENT_DATA_CALLBACK, presenter.getBniResponse());
                intent.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.BNI_VA);
                break;
            case PaymentType.PERMATA_VA:
                intent.putExtra(Constants.INTENT_DATA_CALLBACK, presenter.getPermataResponse());
                intent.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.PERMATA_VA);
                break;
            case PaymentType.OTHER_VA:
                intent.putExtra(Constants.INTENT_DATA_CALLBACK, presenter.getOtherResponse());
                intent.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.OTHER_VA);
                break;
            case PaymentType.ECHANNEL:
                intent.putExtra(Constants.INTENT_DATA_CALLBACK, presenter.getMandiriResponse());
                intent.putExtra(Constants.INTENT_DATA_TYPE, PaymentType.ECHANNEL);
                break;
        }
        setResult(RESULT_OK, intent);
        onBackPressed();
    }

    private void initPagerInstruction() {
        pagerInstruction.setPageMargin(PAGE_MARGIN);
        int pageNumber;
        switch (presenter.getBankType()) {
            case PaymentType.BCA_VA:
                pageNumber = 3;
                break;
            case PaymentType.PERMATA_VA:
                pageNumber = 2;
                break;
            case PaymentType.BNI_VA:
                pageNumber = 3;
                break;
            case PaymentType.ECHANNEL:
                pageNumber = 2;
                break;
            case PaymentType.OTHER_VA:
                pageNumber = 3;
                break;
            default:
                pageNumber = 0;
                break;
        }
        InstructionPagerAdapter adapter = new InstructionPagerAdapter(this, presenter.getBankType(), getSupportFragmentManager(), pageNumber, paymentInfoResponse);
        pagerInstruction.setAdapter(adapter);

        if (CURRENT_POSITION > -1) {
            pagerInstruction.setCurrentItem(CURRENT_POSITION);
        }

        pagerInstruction.clearOnPageChangeListeners();
        final ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                changeConfirmationButtonLable();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        pagerInstruction.addOnPageChangeListener(onPageChangeListener);
        pagerInstruction.post(() -> onPageChangeListener.onPageSelected(pagerInstruction.getCurrentItem()));
    }

    private void initTabInstruction() {
        tabInstruction.setupWithViewPager(pagerInstruction);
        tabInstruction.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                pagerInstruction.setCurrentItem(position);
                changeConfirmationButtonLable();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }


    private void changeConfirmationButtonLable() {
        buttonCompletePayment.setText(getString(R.string.complete_payment_at_atm));
        buttonCompletePayment.setTextBold();
    }

    protected void setTitle() {
        switch (presenter.getBankType()) {
            case PaymentType.BCA_VA:
                setPageTitle(getString(R.string.bank_bca_transfer));
                break;
            case PaymentType.PERMATA_VA:
                setPageTitle(getString(R.string.bank_permata_transfer));
                break;
            case PaymentType.OTHER_VA:
                setPageTitle(getString(R.string.other_bank_transfer));
                break;
            case PaymentType.BNI_VA:
                setPageTitle(getString(R.string.bank_bni_transfer));
                break;
            case PaymentType.ECHANNEL:
                setPageTitle(getString(R.string.mandiri_bill_transfer));
                break;
        }
    }

    protected boolean copyToClipboard(String label, String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(label, text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            return true;
        } else {
            return false;
        }
    }

    protected void startWebIntent(Activity activity, String instructionUrl) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW);
        webIntent.setData(Uri.parse(instructionUrl));
        activity.startActivity(webIntent);
    }

    public void setPageTitle(String pageTitle) {
        this.paymentMethodTitleInToolbar.setText(pageTitle);
    }
}