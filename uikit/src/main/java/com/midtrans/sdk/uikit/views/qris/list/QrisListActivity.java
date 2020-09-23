package com.midtrans.sdk.uikit.views.qris.list;

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
import com.midtrans.sdk.uikit.models.EnabledPayments;
import com.midtrans.sdk.uikit.models.Qris;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.gopay.payment.GoPayPaymentActivity;
import com.midtrans.sdk.uikit.views.shopeepay.payment.ShopeePayPaymentActivity;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;
import java.util.List;

public class QrisListActivity extends BasePaymentActivity implements QrisListAdapter.QrisListAdapterListener {
    public static final String EXTRA_QRIS_LIST = "qris.list";
    private static final String PAGE_NAME = "Select Qris Payment";

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
        //do nothing
    }

    @Override
    public void initTheme() {
        //do nothing
    }

    @Override
    public void onItemClick(int position) {
        Qris item = adapter.getItem(position);
        startQrisPaymentDirectly(item.getType());
    }

    @Override
    public void onBackPressed() {
        if (presenter != null) {
            presenter.trackBackButtonClick(PAGE_NAME);
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT) {
            if (resultCode == RESULT_OK && data != null) {
                finishPayment(RESULT_OK, data);
            } else if (resultCode == RESULT_CANCELED) {
                if (data == null) {
                    if (presenter.getQrisList() == null
                        || presenter.getQrisList().size() == 1
                        || getIntent().getBooleanExtra(UserDetailsActivity.GO_PAY, false)
                        || getIntent().getBooleanExtra(UserDetailsActivity.SHOPEE_PAY, false)) {
                        finish();
                    }
                } else {
                    finishPayment(RESULT_OK, data);
                }
            }
        }
    }

    private void initProperties() {
        EnabledPayments qrisList = (EnabledPayments) getIntent().getSerializableExtra(EXTRA_QRIS_LIST);
        presenter = new QrisListPresenter(this, qrisList);
        adapter = new QrisListAdapter(this);
        adapter.setData(presenter.getQrisList());

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
            textTitle.setText(getString(R.string.activity_select_qris));
        }

        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);

        List<Qris> qrisList = presenter.getQrisList();
        if (qrisList != null && !qrisList.isEmpty()) {
            if (qrisList.size() == 1) {
                startQrisPaymentDirectly(presenter.getQrisList().get(0).getType());
            } else {
                startQrisPayment();
            }
        } else {
            finish();
        }
    }

    private void startQrisPaymentDirectly(String qrisType) {
        if (qrisType != null) {
            if (qrisType.equals(PaymentType.GOPAY)) {
                startGopayActivity();
            } else if (qrisType.equals(PaymentType.SHOPEEPAY)){
                startShopeePayActivity();
            }
        }
    }

    private void startQrisPayment() {
        if (getIntent().getBooleanExtra(UserDetailsActivity.GO_PAY, false)) {
            startGopayActivity();
        } else if (getIntent().getBooleanExtra(UserDetailsActivity.SHOPEE_PAY, false)) {
            startShopeePayActivity();
        }
    }

    private void startGopayActivity() {
        Intent intent = new Intent(this, GoPayPaymentActivity.class);
        startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT);
    }

    private void startShopeePayActivity() {
        Intent intent = new Intent(this, ShopeePayPaymentActivity.class);
        startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT);
    }

    private void finishPayment(int resultCode, Intent data) {
        setResult(resultCode, data);
        finish();
    }
}
