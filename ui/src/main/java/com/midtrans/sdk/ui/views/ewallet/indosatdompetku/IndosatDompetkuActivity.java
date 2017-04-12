package com.midtrans.sdk.ui.views.ewallet.indosatdompetku;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentResponse;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.views.status.PaymentStatusActivity;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.squareup.picasso.Picasso;

/**
 * Created by ziahaqi on 4/10/17.
 */

public class IndosatDompetkuActivity extends BaseActivity implements IndosatDompetkuView {

    private TextInputLayout layoutIndosatNumber;
    private AppCompatEditText editIndosatNumber;
    private FancyButton buttonPayment;
    private RecyclerView recyclerItemDetails;
    private DefaultTextView textTitle;
    private ProgressDialog progressDialog;

    private IndosatDompetkuPresenter presenter;
    private ItemDetailsAdapter itemDetailsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indosat_dompetku);
        initPresenter();
        initProperties();
        initViews();
        initToolbar();
        initPaymentButton();
        initThemes();
        bindValues();
    }

    private void initPaymentButton() {
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performPayment();
            }
        });
    }

    private void initToolbar() {
        // Set title
        textTitle.setText(getString(R.string.indosat_dompetku));

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
    }

    private void initPresenter() {
        presenter = new IndosatDompetkuPresenter(this);
    }


    private void bindValues() {
        // set item details
        recyclerItemDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerItemDetails.setAdapter(itemDetailsAdapter);
    }

    private void performPayment() {
        String phoneNumber = editIndosatNumber.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNumber)) {
            layoutIndosatNumber.setError(getString(R.string.validation_phone_no_empty));
        } else {
            layoutIndosatNumber.setError(null);
            showProgressDialog(getString(R.string.processing_payment));
            presenter.startPayment(phoneNumber);
        }
    }

    private void initThemes() {
        initThemeColor();
        setBackgroundColor(buttonPayment, Theme.PRIMARY_COLOR);
        setBackgroundColor(recyclerItemDetails, Theme.PRIMARY_COLOR);
        setTextInputLayoutColorFilter(layoutIndosatNumber);
        setEditTextCompatBackgroundTintColor(editIndosatNumber);
    }


    private void initViews() {
        layoutIndosatNumber = (TextInputLayout) findViewById(R.id.til_indosat_phone_number);
        editIndosatNumber = (AppCompatEditText) findViewById(R.id.edit_indosat_number);
        buttonPayment = (FancyButton) findViewById(R.id.btn_pay_now);
        recyclerItemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        textTitle = (DefaultTextView) findViewById(R.id.page_title);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

    }

    private void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    private void initProperties() {
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
        UiUtils.showToast(this, getString(R.string.message_payment_failed));
    }

    @Override
    public void onPaymentFailure(IndosatDompetkuPaymentResponse response) {
        dismissProgressDialog();
        if (presenter.isShowPaymentStatus()) {
            showPaymentStatus(response);
        } else {
            finishPayment(RESULT_OK, presenter.getPaymentResult());
        }
    }

    @Override
    public void onPaymentSuccess(IndosatDompetkuPaymentResponse response) {
        dismissProgressDialog();
        if (presenter.isShowPaymentStatus()) {
            showPaymentStatus(response);
        } else {
            finishPayment(RESULT_OK, presenter.getPaymentResult());
        }
    }

    private void showPaymentStatus(IndosatDompetkuPaymentResponse response) {
        Intent intent = new Intent(this, PaymentStatusActivity.class);
        intent.putExtra(Constants.PAYMENT_RESULT, new PaymentResult<>(response));
        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }

    private void finishPayment(int resultCode, PaymentResult<IndosatDompetkuPaymentResponse> paymentResult) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        setResult(resultCode, data);
        finish();
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();

        overrideBackAnimation();
    }
}
