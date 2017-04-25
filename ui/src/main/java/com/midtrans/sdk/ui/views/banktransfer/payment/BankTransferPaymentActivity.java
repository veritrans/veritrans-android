package com.midtrans.sdk.ui.views.banktransfer.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BasePaymentActivity;
import com.midtrans.sdk.ui.adapters.InstructionFragmentPagerAdapter;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;

/**
 * Created by ziahaqi on 4/3/17.
 */

public class BankTransferPaymentActivity extends BasePaymentActivity implements BankTransferPaymentView {
    public static final String ARGS_PAYMENT_TYPE = "payment.type";
    private BankTransferPresenter presenter;

    private ViewPager pagerInstruction;
    private TabLayout tabLayout;
    private TextInputLayout textEmail;
    private AppCompatEditText editEmail;

    private String paymentType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_payment);
        initPresenter();
        initProperties();
        trackPage();
        initViews();
        initItemDetails();
        initValues();
    }

    private void initPresenter() {
        presenter = new BankTransferPresenter(this);
    }

    private void initProperties() {
        Intent data = getIntent();
        if (data != null) {
            paymentType = getIntent().getStringExtra(ARGS_PAYMENT_TYPE);
        } else {
            UiUtils.showToast(BankTransferPaymentActivity.this, getString(R.string.error_something_wrong));
            finish();
        }
    }

    private void trackPage() {
        switch (paymentType) {
            case PaymentType.BCA_VA:
                presenter.trackEvent(AnalyticsEventName.PAGE_BCA_VA);
                break;
            case PaymentType.PERMATA_VA:
                presenter.trackEvent(AnalyticsEventName.PAGE_PERMATA_VA);
                break;
            case PaymentType.OTHER_VA:
                presenter.trackEvent(AnalyticsEventName.PAGE_PERMATA_VA);
                break;
            case PaymentType.E_CHANNEL:
                presenter.trackEvent(AnalyticsEventName.PAGE_MANDIRI_BILL);
                break;
            case PaymentType.BNI_VA:
                break;
        }
    }

    private void initViews() {
        pagerInstruction = (ViewPager) findViewById(R.id.tab_view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_instructions);
        editEmail = (AppCompatEditText) findViewById(R.id.et_email);
        textEmail = (TextInputLayout) findViewById(R.id.email_til);
    }

    public void showPaymentStatus(PaymentResult result) {
        if (paymentType.equals(PaymentType.E_CHANNEL)) {
            showMandiriBillPaymentStatus(result);
        } else {
            showBankTransferPaymentStatus(result);
        }
    }

    private void showBankTransferPaymentStatus(PaymentResult result) {
        Intent intent = new Intent(this, BankTransferMandiriStatusActivity.class);
        intent.putExtra(BankTransferPaymentStatusActivity.EXTRA_RESPONSE, result.getTransactionResponse());
        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }

    private void showMandiriBillPaymentStatus(PaymentResult result) {
        Intent intent = new Intent(this, BankTransferPaymentStatusActivity.class);
        intent.putExtra(BankTransferPaymentStatusActivity.EXTRA_RESPONSE, result.getTransactionResponse());
        intent.putExtra(BankTransferPaymentStatusActivity.EXTRA_BANK, paymentType);
        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == STATUS_REQUEST_CODE) {
                completePayment(presenter.getPaymentResult());
            }
        }
    }

    private void initValues() {
        setTextInputLayoutColorFilter(textEmail);
        setEditTextCompatBackgroundTintColor(editEmail);
        tabLayout.setSelectedTabIndicatorColor(getPrimaryColor());
        pagerInstruction.setPageMargin(getResources().getDimensionPixelSize(R.dimen.twenty_dp));
        int pageNumber;
        String title;
        String bankType = getIntent().getStringExtra(ARGS_PAYMENT_TYPE);
        switch (bankType) {
            case PaymentType.BCA_VA:
                title = getString(R.string.bank_bca_transfer);
                pageNumber = 3;

                //track page bca va overview
                presenter.trackEvent(AnalyticsEventName.PAGE_BCA_VA_OVERVIEW);
                break;
            case PaymentType.E_CHANNEL:
                title = getString(R.string.mandiri_bill_transfer);
                pageNumber = 2;

                //track page mandiri bill overview
                presenter.trackEvent(AnalyticsEventName.PAGE_MANDIRI_BILL_OVERVIEW);
                break;
            case PaymentType.PERMATA_VA:
                title = getString(R.string.bank_permata_transfer);
                pageNumber = 2;

                //track page permata va overview
                presenter.trackEvent(AnalyticsEventName.PAGE_PERMATA_VA_OVERVIEW);
                break;
            case PaymentType.OTHER_VA:
                title = getString(R.string.other_bank_transfer);
                pageNumber = 3;

                //track page other bank va overview
                presenter.trackEvent(AnalyticsEventName.PAGE_OTHER_BANK_VA_OVERVIEW);
                break;
            case PaymentType.BNI_VA:
                title = getString(R.string.bank_transfer);
                pageNumber = 4;
                break;
            default:
                title = getString(R.string.bank_transfer);
                pageNumber = 0;
                break;
        }
        setHeaderTitle(title);
        InstructionFragmentPagerAdapter adapter = new InstructionFragmentPagerAdapter(this, bankType, getSupportFragmentManager(), pageNumber);
        pagerInstruction.setAdapter(adapter);
        setUpTabLayout();
    }

    private void setUpTabLayout() {
        if (MidtransUi.getInstance().getColorTheme().getPrimaryColor() != 0) {
            tabLayout.setSelectedTabIndicatorColor(MidtransUi.getInstance().getColorTheme().getPrimaryColor());
        }
        tabLayout.setupWithViewPager(pagerInstruction);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pagerInstruction.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    protected void startPayment() {
        UiUtils.showProgressDialog(this, getString(R.string.processing_payment), false);
        presenter.performPayment(editEmail.getText().toString(), paymentType);
    }

    @Override
    protected boolean validatePayment() {
        String userEmail = editEmail.getText().toString();
        boolean valid = !TextUtils.isEmpty(userEmail) && !UiUtils.isEmailValid(userEmail);
        if (!valid) {
            UiUtils.showToast(this, getString(R.string.error_invalid_email_id));
        }
        return valid;
    }

    @Override
    public void onPaymentError(String error) {
        UiUtils.hideProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_failed_general));
    }

    @Override
    public void onPaymentFailure(PaymentResult paymentResult) {
        UiUtils.hideProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_failed_general));
    }

    @Override
    public void onPaymentSuccess(PaymentResult paymentResult) {
        UiUtils.hideProgressDialog();
        if (MidtransUi.getInstance().getCustomSetting().isShowPaymentStatus()) {
            showPaymentStatus(paymentResult);
        } else {
            completePayment(presenter.getPaymentResult());
        }
    }
}
