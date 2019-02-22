package com.midtrans.sdk.uikit.view.banktransfer.list;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.merchantdata.MerchantPreferences;
import com.midtrans.sdk.uikit.MidtransKitFlow;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BaseActivity;
import com.midtrans.sdk.uikit.base.model.BankTransfer;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.view.banktransfer.list.adapter.BankTransferListAdapter;
import com.midtrans.sdk.uikit.view.banktransfer.list.model.EnabledBankTransfer;
import com.midtrans.sdk.uikit.view.banktransfer.instruction.BankTransferInstructionActivity;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BankTransferListActivity extends BaseActivity {

    public static final String EXTRA_BANK_LIST = "extra.bank.list";
    public static final String EXTRA_PAYMENT_INFO = "extra.payment.info";

    private RecyclerView containerBankTransfers;
    private TextView merchantNameInToolbar;
    private TextView paymentMethodTitleInToolbar;
    private ImageView merchantLogoInToolbar;
    private Toolbar toolbar;
    private BankTransferListPresenter presenter;

    private PaymentInfoResponse paymentInfo;
    private EnabledBankTransfer enabledBankTransfer;
    private boolean isDeepLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_bank_transfer_list);
        getIntentData();
        initToolbarAndView();
        initializeTheme();
        initMerchantPreferences();
        initPaymentMethodList();
        initItemDetails(paymentInfo);
    }

    /**
     * This method used for binding view and setup other view stuff like toolbar and progress image
     */
    private void initToolbarAndView() {
        toolbar = findViewById(R.id.toolbar_base);
        merchantLogoInToolbar = findViewById(R.id.image_view_merchant_logo);
        merchantNameInToolbar = findViewById(R.id.text_view_merchant_name);
        paymentMethodTitleInToolbar = findViewById(R.id.text_view_page_title);
        containerBankTransfers = findViewById(R.id.recycler_View_bank_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    /**
     * This method used for getting intent extra from MidtransKitFlow
     * later it can be used for making direct payment, callback, and deciding sdk process
     */
    @SuppressWarnings("unchecked")
    private void getIntentData() {
        paymentInfo = (PaymentInfoResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_INFO);
        enabledBankTransfer = (EnabledBankTransfer) getIntent().getSerializableExtra(EXTRA_BANK_LIST);
        isDeepLink = getIntent().getBooleanExtra(USE_DEEP_LINK, false);
        presenter = new BankTransferListPresenter(this, enabledBankTransfer);

        List<BankTransfer> bankTransfers = presenter.getBankList();
        if (bankTransfers != null && !bankTransfers.isEmpty()) {
            if (bankTransfers.size() == 1) {
                startBankTransferPayment(presenter.getBankList().get(0).getBankType());
            } else {
                if (getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_PERMATA, false)) {
                    startBankTransferPayment(PaymentType.PERMATA_VA);
                } else if (getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_MANDIRI, false)) {
                    startBankTransferPayment(PaymentType.ECHANNEL);
                } else if (getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_BCA, false)) {
                    startBankTransferPayment(PaymentType.BCA_VA);
                } else if (getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_BNI, false)) {
                    startBankTransferPayment(PaymentType.BNI_VA);
                } else if (getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_OTHER, false)) {
                    startBankTransferPayment(PaymentType.OTHER_VA);
                }
            }
        } else {
            onBackPressed();
        }
    }

    /**
     * This method use for setup view stuff based on response and merchant preferences
     */
    private void initMerchantPreferences() {
        MerchantPreferences preferences = paymentInfo.getMerchantData().getPreference();
        if (!TextUtils.isEmpty(preferences.getDisplayName())) {
            merchantNameInToolbar.setText(preferences.getDisplayName());
            merchantNameInToolbar.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(preferences.getLogoUrl())) {
            Ion.with(merchantLogoInToolbar)
                    .load(preferences.getLogoUrl());
            merchantLogoInToolbar.setVisibility(View.VISIBLE);
        }
        paymentMethodTitleInToolbar.setText(getString(R.string.activity_select_bank));
    }

    /**
     * This method used for making list of Payment Method
     */
    private void initPaymentMethodList() {
        BankTransferListAdapter adapter = new BankTransferListAdapter(position -> startBankTransferPayment(presenter.getBankList().get(position).getBankType()));

        containerBankTransfers.setLayoutManager(new LinearLayoutManager(this));
        containerBankTransfers.setAdapter(adapter);
        containerBankTransfers.setHasFixedSize(true);
        adapter.setData(presenter.getBankList());
    }

    private void startBankTransferPayment(String bankType) {
        Intent intent = new Intent(this, BankTransferInstructionActivity.class);
        intent.putExtra(BankTransferInstructionActivity.EXTRA_BANK_TYPE, bankType);
        intent.putExtra(BankTransferInstructionActivity.EXTRA_PAYMENT_INFO, paymentInfo);
        startActivityForResult(intent, Constants.INTENT_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_CODE_PAYMENT) {
            if (resultCode == RESULT_OK && data != null) {
                finishPayment(RESULT_OK, data);
            } else if (resultCode == RESULT_CANCELED) {
                if (data == null) {
                    if (presenter.getBankList() == null
                            || presenter.getBankList().size() == 1
                            || getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_PERMATA, false)
                            || getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_MANDIRI, false)
                            || getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_BCA, false)
                            || getIntent().getBooleanExtra(MidtransKitFlow.INTENT_EXTRA_BANK_TRANSFER_OTHER, false)) {
                        onBackPressed();
                    }
                } else {
                    finishPayment(RESULT_OK, data);
                }
            }
        }
    }

    private void finishPayment(int resultCode, Intent data) {
        setResult(resultCode, data);
        onBackPressed();
    }
}