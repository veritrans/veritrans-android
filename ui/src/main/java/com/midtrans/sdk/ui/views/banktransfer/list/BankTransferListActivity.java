package com.midtrans.sdk.ui.views.banktransfer.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.BankTransferListAdapter;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.models.PaymentMethodModel;
import com.midtrans.sdk.ui.views.banktransfer.payment.BankTransferPaymentActivity;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ziahaqi on 3/30/17.
 */

public class BankTransferListActivity extends BaseActivity implements BankTransferListAdapter.BankTransferListener {
    public static final String ARGS_BANK_LIST = "args.banks";

    private RecyclerView bankListContainer;
    private RecyclerView itemDetails;
    private DefaultTextView pageTitle;
    private ImageView merchantLogo;

    private BankTransferListAdapter bankTransferListAdapter;

    private BankTransferListPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banktransfer_list);
        initPresenter();
        initViews();
        initItemDetails();
        initValues();
        setupView();
        initThemeColor();
    }

    private void initPresenter() {
        List<String> bankList = getIntent().getStringArrayListExtra(ARGS_BANK_LIST);
        presenter = new BankTransferListPresenter(this, bankList);
    }

    private void initViews() {
        bankListContainer = (RecyclerView) findViewById(R.id.rv_bank_list);
        itemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        pageTitle = (DefaultTextView) findViewById(R.id.page_title);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
    }

    private void initItemDetails() {
        ItemDetailsAdapter itemDetailsAdapter = new ItemDetailsAdapter(new ItemDetailsAdapter.ItemDetailListener() {
            @Override
            public void onItemShown() {
                // Do nothing
            }
        }, presenter.createItemDetails(this));
        itemDetails.setLayoutManager(new LinearLayoutManager(this));
        itemDetails.setAdapter(itemDetailsAdapter);
    }

    private void initValues() {
        bankTransferListAdapter = new BankTransferListAdapter(this);
        bankTransferListAdapter.setData(presenter.getBankList());

        bankListContainer.setLayoutManager(new LinearLayoutManager(this));
        bankListContainer.setHasFixedSize(true);
        bankListContainer.setAdapter(bankTransferListAdapter);
    }

    private void setupView() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                onBackPressed();
            }
        });

        pageTitle.setText(getString(R.string.activity_select_bank));
        Picasso.with(this)
                .load(MidtransUi.getInstance().getTransaction().merchant.preference.logoUrl)
                .into(merchantLogo);
        setBackgroundColor(itemDetails, Theme.PRIMARY_COLOR);
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
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null) {
                    finishPayment(RESULT_CANCELED, data);
                } else {
                    finishPayment(RESULT_CANCELED);
                }
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

    private void finishPayment(int resultCode) {
        setResult(resultCode);
        finish();
    }
}
