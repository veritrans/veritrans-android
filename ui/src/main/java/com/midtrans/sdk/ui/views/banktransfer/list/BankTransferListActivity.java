package com.midtrans.sdk.ui.views.banktransfer.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.BankTransferListAdapter;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.models.PaymentMethodModel;
import com.midtrans.sdk.ui.utils.PaymentMethodUtils;
import com.midtrans.sdk.ui.views.banktransfer.payment.BankTransferPaymentActivity;
import com.midtrans.sdk.ui.widgets.DefaultTextView;

import java.util.List;

/**
 * Created by ziahaqi on 3/30/17.
 */

public class BankTransferListActivity extends BaseActivity implements BankTransferListAdapter.BankTransferListener {
    public static final String ARGS_BANK_LIST = "args.banks";

    private RecyclerView rvBankList, rvItemDetails;
    private DefaultTextView tvTitle;

    private BankTransferListAdapter bankTransferListAdapter;
    private ItemDetailsAdapter itemDetailsAdapter;
    private BankTransferListPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banktransfer_list);
        initProperties();
        bindView();
        setupView();
        initThemeColor();
    }

    private void initProperties() {
        presenter = new BankTransferListPresenter();
        bankTransferListAdapter = new BankTransferListAdapter(this);
        itemDetailsAdapter = new ItemDetailsAdapter(new ItemDetailsAdapter.ItemDetailListener() {
            @Override
            public void onItemShown() {

            }
        }, presenter.createItemDetails(this));

        List<String> bankList = getIntent().getStringArrayListExtra(ARGS_BANK_LIST);
        List<PaymentMethodModel> bankPaymentMethods = PaymentMethodUtils.createBankPaymentMethods(this, bankList);
        bankTransferListAdapter.setData(bankPaymentMethods);
    }

    private void setupView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                onBackPressed();
            }
        });
        rvItemDetails.setLayoutManager(new LinearLayoutManager(this));
        rvItemDetails.setAdapter(itemDetailsAdapter);
        rvBankList.setHasFixedSize(true);
        rvBankList.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvBankList.setAdapter(bankTransferListAdapter);
        tvTitle.setText(getString(R.string.activity_select_bank));
        setBackgroundColor(rvItemDetails, Theme.PRIMARY_COLOR);
    }

    private void bindView() {
        rvBankList = (RecyclerView) findViewById(R.id.rv_bank_list);
        rvItemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        tvTitle = (DefaultTextView) findViewById(R.id.page_title);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
    }

    @Override
    public void onItemClick(int position) {
        PaymentMethodModel bankPaymentMethod = bankTransferListAdapter.getItem(position);
        Intent intent = new Intent(this, BankTransferPaymentActivity.class);
        intent.putExtra(BankTransferPaymentActivity.ARGS_PAYMENT_TYPE, bankPaymentMethod.getPaymentType());
        startActivityForResult(intent, Constants.INTENT_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.INTENT_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                finish();
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null) {
                    setResult(RESULT_CANCELED, data);
                    finish();
                }
            }
        }
    }
}
