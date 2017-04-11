package com.midtrans.sdk.ui.views.ewallet.xltunai;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentResponse;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.squareup.picasso.Picasso;

/**
 * Created by ziahaqi on 4/7/17.
 */

public class XlTunaiPaymentActivity extends BaseActivity implements XLTunaiPaymentView {

    private ItemDetailsAdapter itemDetailsAdapter;

    private FancyButton buttonPayment;
    private XlTunaiPresenter presenter;
    private DefaultTextView textTitle;
    private RecyclerView recyclerItemDetails;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xltunai);
        initProperties();
        iniViews();
        initThemes();
        setupViews();
    }

    private void initThemes() {
        initThemeColor();
        setBackgroundColor(buttonPayment, Theme.PRIMARY_COLOR);
        setBackgroundColor(recyclerItemDetails, Theme.PRIMARY_COLOR);
    }

    private void setupViews() {
        // Set title
        textTitle.setText(getString(R.string.xl_tunai));

        // set item details
        recyclerItemDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerItemDetails.setAdapter(itemDetailsAdapter);

        // Set merchant logo
        ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        Picasso.with(this)
                .load(presenter.getMerchantLogo())
                .into(merchantLogo);

        initToolbarBackButton();

        // Set back button click listener
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performPayment();
            }
        });
    }

    private void performPayment() {
        showProgressDialog(getString(R.string.processing_payment));
        presenter.startPayment();
    }

    private void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    private void iniViews() {
        buttonPayment = (FancyButton) findViewById(R.id.btn_pay_now);
        recyclerItemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        textTitle = (DefaultTextView) findViewById(R.id.page_title);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
    }

    private void initProperties() {
        presenter = new XlTunaiPresenter(this);
        itemDetailsAdapter = new ItemDetailsAdapter(new ItemDetailsAdapter.ItemDetailListener() {
            @Override
            public void onItemShown() {
                // do nothing
            }
        }, presenter.createItemDetails(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
    }

    @Override
    public void onPaymentError(String message) {
        dismissProgressDialog();
    }

    @Override
    public void onPaymentFailure(XlTunaiPaymentResponse response) {
        dismissProgressDialog();
        showPaymentStatus(response);
    }

    @Override
    public void onPaymentSuccess(XlTunaiPaymentResponse response) {
        dismissProgressDialog();
        showPaymentStatus(response);
    }

    private void showPaymentStatus(XlTunaiPaymentResponse response) {
        Intent intent = new Intent(this, XlTunaiStatusActivity.class);
        intent.putExtra(XlTunaiStatusActivity.EXTRA_ORDER_ID, response.xlTunaiOrderId);
        intent.putExtra(XlTunaiStatusActivity.EXTRA_MERCHANT_CODE, response.xlTunaiMerchantId);
        intent.putExtra(XlTunaiStatusActivity.EXTRA_EXPIRATION, response.xlExpiration);
        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == STATUS_REQUEST_CODE) {
                finishPayment(resultCode, presenter.getPaymentResult());
            }
        }
    }

    private void finishPayment(int resultCode, PaymentResult<XlTunaiPaymentResponse> paymentResult) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        setResult(resultCode, data);
        finish();
    }
}
