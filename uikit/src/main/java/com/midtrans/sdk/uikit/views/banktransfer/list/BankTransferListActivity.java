package com.midtrans.sdk.uikit.views.banktransfer.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.midtrans.sdk.corekit.models.BankTransferModel;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BaseActivity;
import com.midtrans.sdk.uikit.adapters.BankTransferListAdapter;
import com.midtrans.sdk.uikit.models.EnabledPayments;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.banktransfer.payment.BankTransferPaymentActivity;

/**
 * Created by ziahaqi on 8/10/17.
 */

public class BankTransferListActivity extends BaseActivity implements BankTransferListAdapter.BankTransferAdapterListener {

    public static final String ARGS_BANK_LIST = "args.banks";

    private RecyclerView bankListContainer;

    private BankTransferListAdapter adapter;

    private BankTransferListPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_list);
        initProperties();
    }

    @Override
    public void bindViews() {

    }

    @Override
    public void initTheme() {

    }

    private void initProperties() {
        EnabledPayments bankList = (EnabledPayments) getIntent().getSerializableExtra(ARGS_BANK_LIST);
        presenter = new BankTransferListPresenter(this, bankList);

        adapter = new BankTransferListAdapter(this);
        adapter.setData(presenter.getBankList());

        bankListContainer.setLayoutManager(new LinearLayoutManager(this));
        bankListContainer.setHasFixedSize(true);
        bankListContainer.setAdapter(adapter);

    }


    @Override
    public void onItemClick(int position) {
        BankTransferModel item = adapter.getItem(position);
        startBankTransferPayment(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                finishPayment(RESULT_OK, data);
            }
        }
    }

    private void startBankTransferPayment(BankTransferModel bankPaymentMethod) {
        Intent intent = new Intent(this, BankTransferPaymentActivity.class);
        intent.putExtra(BankTransferPaymentActivity.EXTRA_PAYMENT_TYPE, bankPaymentMethod.get());
        startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT);
    }

    private void finishPayment(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }
}
