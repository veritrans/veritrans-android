package com.midtrans.sdk.uikit.views.qris.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.models.EnabledPayments;

public class QrisListActivity extends BasePaymentActivity implements QrisListAdapter.QrisListAdapterListener {
    public static final String EXTRA_QRIS_LIST = "qris.list";

    private QrisListPresenter presenter;
    private QrisListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qris_list);
        initProperties();
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
    }
}
