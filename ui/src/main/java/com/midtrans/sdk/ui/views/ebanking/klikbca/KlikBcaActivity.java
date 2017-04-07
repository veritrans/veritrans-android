package com.midtrans.sdk.ui.views.ebanking.klikbca;

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
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.squareup.picasso.Picasso;

/**
 * Created by rakawm on 4/7/17.
 */

public class KlikBcaActivity extends BaseActivity implements KlikBcaView {

    private MidtransUi midtransUi;

    private RecyclerView itemDetails;
    private TextView titleText;
    private FancyButton payNowButton;
    private ProgressDialog progressDialog;
    private AppCompatEditText userIdField;
    private TextInputLayout userIdTextInput;

    private KlikBcaPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klikbca);
        initMidtransUi();
        initPresenter();
        initViews();
        initThemes();
        initToolbar();
        initProgressDialog();
        initItemDetails();
        initPayNowButton();
    }

    private void initMidtransUi() {
        midtransUi = MidtransUi.getInstance();
    }

    private void initPresenter() {
        presenter = new KlikBcaPresenter(this);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        itemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        payNowButton = (FancyButton) findViewById(R.id.btn_pay_now);
        titleText = (TextView) findViewById(R.id.page_title);
        userIdField = (AppCompatEditText) findViewById(R.id.user_id_field);
        userIdTextInput = (TextInputLayout) findViewById(R.id.user_id_text_input);
    }

    private void initThemes() {
        // Set color theme for item details bar
        setBackgroundColor(itemDetails, Theme.PRIMARY_COLOR);
        setBackgroundColor(payNowButton, Theme.PRIMARY_COLOR);
        // Set text field themes
        setTextInputLayoutColorFilter(userIdTextInput);
        setEditTextCompatBackgroundTintColor(userIdField);
        // Set font into pay now button
        if (!TextUtils.isEmpty(midtransUi.getSemiBoldFontPath())) {
            payNowButton.setCustomTextFont(midtransUi.getSemiBoldFontPath());
        }
        initThemeColor();
    }

    private void initToolbar() {
        // Set title
        setHeaderTitle(getString(R.string.klik_bca));

        // Set merchant logo
        ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        Picasso.with(this)
                .load(midtransUi.getTransaction().merchant.preference.logoUrl)
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

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
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

    private void initPayNowButton() {
        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isUserIdValid()) {
                    userIdTextInput.setError(null);
                    showProgressDialog(getString(R.string.processing_payment));
                    presenter.startPayment(userIdField.getText().toString());
                } else {
                    userIdTextInput.setError(getString(R.string.error_user_id));
                }
            }
        });
    }

    private void setHeaderTitle(String title) {
        titleText.setText(title);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();

        overrideBackAnimation();
    }

    private void startKlikBcaPaymentStatus(KlikBcaPaymentResponse paymentResponse) {
        Intent intent = new Intent(this, KlikBcaStatusActivity.class);
        intent.putExtra(KlikBcaStatusActivity.EXTRA_VALIDITY, paymentResponse.bcaKlikbcaExpireTime);
        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }

    private void showProgressDialog(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
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

    private void finishPayment(int resultCode, PaymentResult<KlikBcaPaymentResponse> paymentResult) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void onKlikBcaSuccess(KlikBcaPaymentResponse paymentResponse) {
        dismissProgressDialog();
        if (midtransUi.getCustomSetting().isShowPaymentStatus()) {
            startKlikBcaPaymentStatus(paymentResponse);
        } else {
            finishPayment(RESULT_OK, presenter.getPaymentResult());
        }
    }

    @Override
    public void onKlikBcaFailure(String message) {
        dismissProgressDialog();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isUserIdValid() {
        String userId = userIdField.getText().toString();
        return !TextUtils.isEmpty(userId);
    }
}
