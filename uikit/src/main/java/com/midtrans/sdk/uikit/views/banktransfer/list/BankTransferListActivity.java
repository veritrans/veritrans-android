package com.midtrans.sdk.uikit.views.banktransfer.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BaseActivity;
import com.midtrans.sdk.uikit.adapters.BankTransferListAdapter;

import java.util.List;

/**
 * Created by ziahaqi on 8/10/17.
 */

public class BankTransferListActivity extends BaseActivity{

    public static final String ARGS_BANK_LIST = "args.banks";

    private RecyclerView bankListContainer;

    private BankTransferListAdapter bankTransferListAdapter;

    private BankTransferListPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_list);
        initProperties();
        initValues();
    }

    private void initProperties() {
        List<String> bankList = getIntent().getStringArrayListExtra(ARGS_BANK_LIST);
        presenter = new BankTransferListPresenter(this, bankList);

    }

    private void initViews() {
        bankListContainer = (RecyclerView) findViewById(R.id.rv_bank_list);
    }

    private void initValues() {
        bankTransferListAdapter = new BankTransferListAdapter(this);
        bankTransferListAdapter.setData(presenter.getBankList());

        bankListContainer.setLayoutManager(new LinearLayoutManager(this));
        bankListContainer.setHasFixedSize(true);
        bankListContainer.setAdapter(bankTransferListAdapter);
    }

    @Override
    public void onItemClick(int position) {
        PaymentMethodModel bankPaymentMethod = bankTransferListAdapter.getItem(position);
        startBankTransferPayment(bankPaymentMethod);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                finishPayment(RESULT_OK, data);
            }
        }
    }

    private void startBankTransferPayment(PaymentMethodModel bankPaymentMethod) {
        Intent intent = new Intent(this, BankTransferPaymentActivity.class);
        intent.putExtra(BankTransferPaymentActivity.ARGS_PAYMENT_TYPE, bankPaymentMethod.getPaymentType());
        startActivityForResult(intent, Constants.INTENT_CODE_PAYMENT);
    }

    private void finishPayment(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }
}
