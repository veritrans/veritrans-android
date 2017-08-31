package com.midtrans.sdk.uikit.views.banktransfer.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.models.BankTransfer;
import com.midtrans.sdk.uikit.models.EnabledPayments;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.banktransfer.payment.BankTransferPaymentActivity;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

/**
 * Created by ziahaqi on 8/10/17.
 */

public class BankTransferListActivity extends BasePaymentActivity implements BankTransferListAdapter.BankTransferAdapterListener {

    public static final String EXTRA_BANK_LIST = "extra.bank.list";

    private RecyclerView listBankTransfers;
    private DefaultTextView textTitle;

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
        textTitle.setText(getString(R.string.activity_select_bank));
    }

    @Override
    public void bindViews() {
        listBankTransfers = (RecyclerView) findViewById(R.id.rv_bank_list);
        textTitle = (DefaultTextView) findViewById(R.id.text_page_title);
    }

    @Override
    public void initTheme() {

    }

    private void initProperties() {
        EnabledPayments bankList = (EnabledPayments) getIntent().getSerializableExtra(EXTRA_BANK_LIST);
        presenter = new BankTransferListPresenter(this, bankList);

        adapter = new BankTransferListAdapter(this);
        adapter.setData(presenter.getBankList());

        listBankTransfers.setLayoutManager(new LinearLayoutManager(this));
        listBankTransfers.setHasFixedSize(true);
        listBankTransfers.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position) {
        BankTransfer item = adapter.getItem(position);
        startBankTransferPayment(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT) {
            if (resultCode == RESULT_OK && data != null) {
                finishPayment(RESULT_OK, data);
            }
        }
    }

    private void startBankTransferPayment(BankTransfer bankPaymentMethod) {
        Intent intent = new Intent(this, BankTransferPaymentActivity.class);
        intent.putExtra(BankTransferPaymentActivity.EXTRA_BANK_TYPE, bankPaymentMethod.getBankType());
        startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT);
    }

    private void finishPayment(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }
}
