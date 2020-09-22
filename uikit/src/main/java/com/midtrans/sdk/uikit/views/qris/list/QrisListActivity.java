package com.midtrans.sdk.uikit.views.qris.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.models.EnabledPayments;
import com.midtrans.sdk.uikit.models.Qris;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;
import java.util.List;

public class QrisListActivity extends BasePaymentActivity implements QrisListAdapter.QrisListAdapterListener {
    public static final String EXTRA_QRIS_LIST = "qris.list";

    private QrisListPresenter presenter;
    private QrisListAdapter adapter;
    private RecyclerView qrisRecyclerView;
    private SemiBoldTextView textTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qris_list);
        initProperties();
        bindData();
    }

    @Override
    public void bindViews() {

    }

    @Override
    public void initTheme() {

    }

    @Override
    public void onItemClick(int positiion) {

    }

    private void initProperties() {
        EnabledPayments qrisList = (EnabledPayments) getIntent().getSerializableExtra(EXTRA_QRIS_LIST);
        presenter = new QrisListPresenter(this, qrisList);
        adapter = new QrisListAdapter(this);
//        adapter.setData(presenter.getBankList()); TODO

        qrisRecyclerView = findViewById(R.id.rv_qris_list);
        if (qrisRecyclerView != null) {
            qrisRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            qrisRecyclerView.setHasFixedSize(true);
            qrisRecyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, getString(R.string.message_error_internal_server), Toast.LENGTH_SHORT).show();
        }
    }

    private void bindData() {
        textTitle = findViewById(R.id.text_page_title);
        if (textTitle != null) {
            textTitle.setText(getString(R.string.activity_select_bank));
        }

        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        //presenter.trackPageView(PAGE_NAME, isFirstPage);

        List<Qris> bankTransfers = presenter.getQrisList();
        if (bankTransfers != null && !bankTransfers.isEmpty()) {
            if (bankTransfers.size() == 1) {
                startQrisPayment(presenter.getQrisList().get(0).getType());
            } else {
                //TODO check if gopay/shopee
//                if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_PERMATA, false)) {
//                    startBankTransferPayment(PaymentType.PERMATA_VA);
//                } else if (getIntent().getBooleanExtra(UserDetailsActivity.BANK_TRANSFER_MANDIRI, false)) {
//                    startBankTransferPayment(PaymentType.E_CHANNEL);
            }
        } else {
            finish();
        }
    }

    private void startQrisPayment(String qrisType) {
        //TODO start Gopay/Shopee
//        Intent intent = new Intent(this, BankTransferPaymentActivity.class);
//        intent.putExtra(BankTransferPaymentActivity.EXTRA_BANK_TYPE, bankType);
//        startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT);
    }
}
