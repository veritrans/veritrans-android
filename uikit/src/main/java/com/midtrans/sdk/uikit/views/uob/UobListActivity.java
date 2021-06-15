package com.midtrans.sdk.uikit.views.uob;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.activities.UserDetailsActivity;
import com.midtrans.sdk.uikit.models.EnabledPayments;
import com.midtrans.sdk.uikit.models.UobPayment;
import com.midtrans.sdk.uikit.views.uob.app.UobAppPaymentActivity;
import com.midtrans.sdk.uikit.views.uob.web.UobWebPaymentActivity;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

import java.util.List;

public class UobListActivity extends BasePaymentActivity implements UobListAdapter.UobAdapterListener {

    public static final String EXTRA_UOB_LIST = "extra.uob.list";
    private final String PAGE_NAME = "UOB EZ Pay";

    private RecyclerView listUobPayments;
    private SemiBoldTextView textTitle;

    private UobListAdapter adapter;

    private UobPaymentListPresenter presenter;

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
            textTitle.setText(getString(R.string.activity_uob_list));
        }

        //track page view after page properly loaded
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);

        List<UobPayment> uobPayments = presenter.getUobPaymentList();
        if (uobPayments != null && !uobPayments.isEmpty()) {
            if (getIntent().getBooleanExtra(UserDetailsActivity.UOB_WEB, false)) {
                startUobPayment(PaymentType.UOB_WEB);
            } else if (getIntent().getBooleanExtra(UserDetailsActivity.UOB_APP, false)) {
                startUobPayment(PaymentType.UOB_APP);
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
        EnabledPayments bankList = (EnabledPayments) getIntent().getSerializableExtra(EXTRA_UOB_LIST);
        presenter = new UobPaymentListPresenter(this, bankList);

        adapter = new UobListAdapter(this);
        adapter.setData(presenter.getUobPaymentList());

        listUobPayments = findViewById(R.id.rv_bank_list);
        if (listUobPayments != null) {
            listUobPayments.setLayoutManager(new LinearLayoutManager(this));
            listUobPayments.setHasFixedSize(true);
            listUobPayments.setAdapter(adapter);
        } else {
            Toast.makeText(this, getString(R.string.message_error_internal_server), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemClick(int position) {
        UobPayment item = adapter.getItem(position);
        startUobPayment(item.getType());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.RESULT_CODE_PAYMENT_TRANSFER) {

            if (resultCode == RESULT_OK && data != null) {
                finishPayment(RESULT_OK, data);
            } else if (resultCode == RESULT_CANCELED) {
                finishPayment(RESULT_CANCELED, data);
            }
        }
    }

    private void startUobPayment(String paymentType) {
        if (paymentType.equals(PaymentType.UOB_WEB)) {
            Intent uobWebActivity = new Intent(this, UobWebPaymentActivity.class);
            startActivityForResult(uobWebActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        } else {
            Intent uobAppActivity = new Intent(this, UobAppPaymentActivity.class);
            startActivityForResult(uobAppActivity, Constants.RESULT_CODE_PAYMENT_TRANSFER);
        }

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
