package com.midtrans.sdk.ui.views.banktransfer.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.InstructionFragmentPagerAdapter;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.constants.AnalyticsEventName;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.squareup.picasso.Picasso;

/**
 * Created by ziahaqi on 4/3/17.
 */

public class BankTransferPaymentActivity extends BaseActivity implements BankTransferPaymentView {
    public static final String ARGS_PAYMENT_TYPE = "payment.type";

    private FancyButton seeAccountNumberButton;
    private TextView titleText;
    private RecyclerView itemDetails;
    private BankTransferPresenter presenter;
    private ImageView merchantLogo;

    private ViewPager pagerInstruction;
    private TabLayout tabLayout;
    private TextInputLayout textEmail;
    private AppCompatEditText editEmail;

    private String paymentType;
    private ItemDetailsAdapter itemDetailsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_payment);

        initPresenter();
        initProperties();
        initViews();
        initThemes();
        initToolbar();
        initItemDetails();
        initValues();
        initSeeAccountNumberButton();
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

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        seeAccountNumberButton = (FancyButton) findViewById(R.id.btn_confirm_payment);
        titleText = (DefaultTextView) findViewById(R.id.page_title);
        itemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        pagerInstruction = (ViewPager) findViewById(R.id.tab_view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_instructions);
        editEmail = (AppCompatEditText) findViewById(R.id.et_email);
        textEmail = (TextInputLayout) findViewById(R.id.email_til);
    }

    private void initThemes() {
        setBackgroundColor(seeAccountNumberButton, Theme.PRIMARY_COLOR);
        setBackgroundColor(itemDetails, Theme.PRIMARY_COLOR);
        if (!TextUtils.isEmpty(MidtransUi.getInstance().getSemiBoldFontPath())) {
            seeAccountNumberButton.setCustomTextFont(MidtransUi.getInstance().getSemiBoldFontPath());
        }
        initThemeColor();
    }

    private void initToolbar() {
        setHeaderTitle(getString(R.string.bank_transfer));

        Picasso.with(this)
                .load(MidtransUi.getInstance().getTransaction().merchant.preference.logoUrl)
                .into(merchantLogo);
        initToolbarBackButton();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initItemDetails() {
        itemDetailsAdapter = new ItemDetailsAdapter(new ItemDetailsAdapter.ItemDetailListener() {
            @Override
            public void onItemShown() {

            }
        }, presenter.createItemDetails(this));
        itemDetails.setLayoutManager(new LinearLayoutManager(this));
        itemDetails.setAdapter(itemDetailsAdapter);

        if (!TextUtils.isEmpty(MidtransUi.getInstance().getSemiBoldFontPath())) {
            seeAccountNumberButton.setCustomTextFont(MidtransUi.getInstance().getSemiBoldFontPath());
        }
    }

    private void initSeeAccountNumberButton() {
        seeAccountNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performTransaction();
            }
        });
    }

    public void setHeaderTitle(String title) {
        titleText.setText(title);
    }

    /**
     * Performs the validation and if satisfies the required condition then it will either start
     * mandiri bill pay flow or bank transfer flow depending on selected payment method.
     * <p>
     * {@see }
     */
    private void performTransaction() {
        // for sending instruction on email only if email-Id is entered.
        String userEmail = editEmail.getText().toString();
        if (!TextUtils.isEmpty(userEmail) && !UiUtils.isEmailValid(userEmail)) {
            UiUtils.showToast(this, getString(R.string.error_invalid_email_id));
        } else {
            UiUtils.showProgressDialog(this, getString(R.string.processing_payment), false);
            presenter.performPayment(userEmail, paymentType);
        }
    }

    public void showPaymentStatus(PaymentResult response) {
        if(paymentType.equals(PaymentType.E_CHANNEL)){
            Intent intent = new Intent(this, BankTransferMandiriStatusActivity.class);
            intent.putExtra(BankTransferPaymentStatusActivity.EXTRA_RESPONSE, response.getTransactionResponse());
            startActivityForResult(intent, STATUS_REQUEST_CODE);
        } else {
            Intent intent = new Intent(this, BankTransferPaymentStatusActivity.class);
            intent.putExtra(BankTransferPaymentStatusActivity.EXTRA_RESPONSE, response.getTransactionResponse());
            intent.putExtra(BankTransferPaymentStatusActivity.EXTRA_BANK, paymentType);
            startActivityForResult(intent, STATUS_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        setResultCode(RESULT_CANCELED);
        finish();
        overrideBackAnimation();
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
    public void onPaymentError(String error) {
        UiUtils.hideProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_failed));
    }

    @Override
    public void onPaymentFailure(PaymentResult paymentResult) {
        UiUtils.hideProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_failed));
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
