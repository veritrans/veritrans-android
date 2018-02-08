package com.midtrans.sdk.uikit.views.banktransfer.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.widget.Toast;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.activities.UserDetailsActivity;
import com.midtrans.sdk.uikit.models.BankTransfer;
import com.midtrans.sdk.uikit.models.EnabledPayments;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.banktransfer.payment.BankTransferPaymentActivity;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

import java.util.List;

/**
 * Created by ziahaqi on 8/10/17.
 */

public class BankTransferListActivity extends BasePaymentActivity implements BankTransferListAdapter.BankTransferAdapterListener {

    public static final String EXTRA_BANK_LIST = "extra.bank.list";
    private final String PAGE_NAME = "Select Bank Transfer";

    private RecyclerView listBankTransfers;
    private SemiBoldTextView textTitle;

    private BankTransferListAdapter adapter;

    private BankTransferListPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_list);
        initProperties();
        bindData();
    }

    private void bindData() {
        textTitle = findViewById(R.id.text_page_title);
        if (textTitle != null) {
            textTitle.setText(getString(R.string.activity_select_bank));
        }

        //track page view after page properly loaded
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);

        List<BankTransfer> bankTransfers = presenter.getBankList();
        if (bankTransfers != null && !bankTransfers.isEmpty()) {
            if (bankTransfers.size() == 1) {
                startBankTransferPayment(presenter.getBankList().get(0).getBankType());
            } else {
                if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_PERMATA, false)) {
                    startBankTransferPayment(PaymentType.PERMATA_VA);
                } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_MANDIRI, false)) {
                    startBankTransferPayment(PaymentType.E_CHANNEL);
                } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_BCA, false)) {
                    startBankTransferPayment(PaymentType.BCA_VA);
                } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_BNI, false)) {
                    startBankTransferPayment(PaymentType.BNI_VA);
                } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_OTHER, false)) {
                    startBankTransferPayment(PaymentType.ALL_VA);
                }
            }
        } else {
            finish();
        }
    }

    @Override
    public void bindViews() {
    }

    @Override
    public void initTheme() {

    }

    private void initProperties() {
        EnabledPayments bankList = (EnabledPayments) getIntent().getSerializableExtra(EXTRA_BANK_LIST);
        presenter = new BankTransferListPresenter(this, bankList);

        adapter = new BankTransferListAdapter(this);
        adapter.setData(presenter.getBankList());

        listBankTransfers = findViewById(R.id.rv_bank_list);
        if (listBankTransfers != null) {
            listBankTransfers.setLayoutManager(new LinearLayoutManager(this));
            listBankTransfers.setHasFixedSize(true);
            listBankTransfers.setAdapter(adapter);
        } else {
            Toast.makeText(this, getString(R.string.message_error_internal_server), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClick(int position) {
        BankTransfer item = adapter.getItem(position);
        startBankTransferPayment(item.getBankType());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT) {

            if (resultCode == RESULT_OK && data != null) {

                finishPayment(RESULT_OK, data);
            } else if (resultCode == RESULT_CANCELED) {

                if (data == null) {
                    if (presenter.getBankList() == null
                            || presenter.getBankList().size() == 1
                            || getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_PERMATA, false)
                            || getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_MANDIRI, false)
                            || getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_BCA, false)
                            || getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_OTHER, false)) {
                        finish();
                    }
                } else {
                    finishPayment(RESULT_OK, data);
                }
            }
        }
    }

    private void startBankTransferPayment(String bankType) {
        Intent intent = new Intent(this, BankTransferPaymentActivity.class);
        intent.putExtra(BankTransferPaymentActivity.EXTRA_BANK_TYPE, bankType);
        startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT);
    }

    private void finishPayment(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (presenter != null) {
            presenter.trackBackButtonClick(PAGE_NAME);
        }
        super.onBackPressed();
    }
}
