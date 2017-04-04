package com.midtrans.sdk.ui.views.banktransfer.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;

/**
 * Created by ziahaqi on 4/3/17.
 */

public class BankTransferPaymentActivity extends BaseActivity {
    public static final String PAYMENT_FRAGMENT = "payment";
    public static final String STATUS_FRAGMENT = "status";
    public static final String SOMETHING_WENT_WRONG = "Something went wrong";
    public static final String ARGS_PAYMENT_TYPE = "payment.type";
    public String currentFragment = PAYMENT_FRAGMENT;

    private TextView tvTotalAmount;
    private FancyButton buttonSeeAccountNumber;
    private TextView tvTitle;

    private BankTransferPresenter presenter;
    private String paymentType;
    private BankTransferPaymentFragment paymentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banktransfer_payment);

        // get position of selected payment method
        initProperties();
        bindView();
        initThemeColor();
        setupView();
        setupFragment();
    }


    private void setupFragment() {
        paymentFragment = BankTransferPaymentFragment.newInstance(paymentType);
        presenter.setPaymentView(paymentFragment);
        replaceFragment(paymentFragment, R.id.fragment_container, false, false);
    }

    private void initProperties() {
        presenter = new BankTransferPresenter();
        Intent data = getIntent();
        if (data != null) {
            paymentType = getIntent().getStringExtra(ARGS_PAYMENT_TYPE);
        } else {
            UiUtils.showToast(BankTransferPaymentActivity.this, getString(R.string.error_something_wrong));
            finish();
        }
    }

    private void bindView() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        buttonSeeAccountNumber = (FancyButton) findViewById(R.id.btn_confirm_payment);
        tvTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);
        tvTitle = (DefaultTextView) findViewById(R.id.page_title);

    }

    private void setupView() {
        setBackgroundColor(buttonSeeAccountNumber, Theme.PRIMARY_COLOR);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTotalAmount.setText(presenter.getFormattedTotalAmount());
        tvTitle.setText(getString(R.string.bank_transfer));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        buttonSeeAccountNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentFragment.equals(PAYMENT_FRAGMENT)) {
                    performTransaction();
                } else {
                    onBackPressed();
                }
            }
        });
    }

    public void setHeaderTitle(String title) {
        tvTitle.setText(title);
    }

    /**
     * Performs the validation and if satisfies the required condition then it will either start
     * mandiri bill pay flow or bank transfer flow depending on selected payment method.
     * <p>
     * {@see }
     */
    private void performTransaction() {
        // for sending instruction on email only if email-Id is entered.
        if (paymentFragment != null && !paymentFragment.isDetached()) {

            String userEmail = paymentFragment.getUserEmail();
            if (!TextUtils.isEmpty(userEmail) && !UiUtils.isEmailValid(userEmail)) {
                UiUtils.showToast(this, getString(R.string.error_invalid_email_id));
                return;
            }

            UiUtils.showProgressDialog(this, getString(R.string.processing_payment), false);
            presenter.performPayment(userEmail, paymentType);
        } else {
            UiUtils.showToast(this, getString(R.string.error_invalid_payment_form));
        }
    }

    public void showPaymentStatus(PaymentResult response) {
        BankTransferStatusFragment statusFragment = BankTransferStatusFragment.newInstance(response, paymentType);
        presenter.setStatusView(statusFragment);
        replaceFragment(statusFragment, R.id.fragment_container, false, false);
        currentFragment = STATUS_FRAGMENT;
    }

    @Override
    public void onBackPressed() {
        setResultCode(RESULT_CANCELED);
        if (currentFragment.equals(STATUS_FRAGMENT)) {
            completePayment(presenter.getPaymentResult());
        } else {
            super.onBackPressed();
        }
    }

}
