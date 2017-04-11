package com.midtrans.sdk.ui.views.ewallet.tcash;

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

import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentResponse;
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
 * Created by ziahaqi on 4/7/17.
 */

public class TelkomselCashActivity extends BaseActivity implements TelkomselCashView {

    private TextInputLayout layoutTcashToken;
    private AppCompatEditText editTcashToken;
    private FancyButton buttonPayment;
    private RecyclerView recyclerItemDetails;
    private DefaultTextView textTitle;
    private ProgressDialog progressDialog;


    private TelkomselCashPresenter presenter;
    private ItemDetailsAdapter itemDetailsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_telkomsel_cash);
        initPresenter();
        initProperties();
        initViews();
        initToolbar();
        initThemes();
        initpaymentButton();
        bindValues();
    }

    private void initpaymentButton() {
        buttonPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performPayment();
            }
        });
    }

    private void initToolbar() {
        // Set title
        textTitle.setText(getString(R.string.telkomsel_cash));

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
        presenter = new TelkomselCashPresenter(this);
    }

    private void bindValues() {
        // set item details
        recyclerItemDetails.setLayoutManager(new LinearLayoutManager(this));
        recyclerItemDetails.setAdapter(itemDetailsAdapter);
    }

    private void performPayment() {
        // for sending instruction on email only if email-Id is entered.
        String tcashToken = editTcashToken.getText().toString().trim();
        if (TextUtils.isEmpty(tcashToken)) {
            layoutTcashToken.setError(getString(R.string.error_tcash_token_empty));
            return;
        }

        showProgressDialog(getString(R.string.processing_payment));
        presenter.startPayment(tcashToken);
    }

    private void initThemes() {
        initThemeColor();
        setBackgroundColor(buttonPayment, Theme.PRIMARY_COLOR);
        setBackgroundColor(recyclerItemDetails, Theme.PRIMARY_COLOR);
        setTextInputLayoutColorFilter(layoutTcashToken);
        setEditTextCompatBackgroundTintColor(editTcashToken);
    }


    private void initViews() {
        layoutTcashToken = (TextInputLayout) findViewById(R.id.til_tcash_token);
        editTcashToken = (AppCompatEditText) findViewById(R.id.edit_tcash_token);
        buttonPayment = (FancyButton) findViewById(R.id.btn_pay_now);
        recyclerItemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        textTitle = (DefaultTextView) findViewById(R.id.page_title);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

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
    public void onTelkomselCashPaymentSuccess(TelkomselCashPaymentResponse response) {
        dismissProgressDialog();
        if (presenter.isShowPaymentStatus()) {
            startTelkomselCashStatus(response);
        } else {
            finishPayment(RESULT_OK, presenter.getPaymentResult());
        }
    }

    private void startTelkomselCashStatus(TelkomselCashPaymentResponse response) {
        Intent intent = new Intent(this, PaymentStatusActivity.class);
        intent.putExtra(Constants.PAYMENT_RESULT, new PaymentResult<>(response));
        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }

    @Override
    public void onTelkomselCashPaymentFailure(TelkomselCashPaymentResponse response) {
        dismissProgressDialog();

        if (response != null && response.statusCode.equals(getString(R.string.status_code_400))) {
            UiUtils.showToast(this, getString(R.string.message_payment_cannot_proccessed));
        } else {
            UiUtils.showToast(this, getString(R.string.error_message_invalid_input_telkomsel));
        }
    }

    @Override
    public void onTelkomselCashPaymentError(String message) {
        dismissProgressDialog();
        UiUtils.showToast(this, getString(R.string.message_payment_cannot_proccessed));
    }

    private void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    private void finishPayment(int resultCode, PaymentResult<TelkomselCashPaymentResponse> paymentResult) {
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
