package com.midtrans.sdk.ui.views.gci;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.views.status.PaymentStatusActivity;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.squareup.picasso.Picasso;

/**
 * Created by rakawm on 4/6/17.
 */

public class GiftCardIndonesiaActivity extends BaseActivity implements GiftCardIndonesiaView {
    private MidtransUi midtransUi;

    private RecyclerView itemDetails;
    private TextView titleText;
    private FancyButton payNowButton;
    private ProgressDialog progressDialog;
    private AppCompatEditText cardNumberField;
    private AppCompatEditText passwordField;
    private TextInputLayout cardNumberTextInput;
    private TextInputLayout passwordTextInput;

    private GiftCardIndonesiaPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_card);
        initMidtransUi();
        initPresenter();
        initViews();
        initThemes();
        initToolbar();
        initProgressDialog();
        initItemDetails();
        initPayNowButton();
        initCardNumberField();
    }

    private void initMidtransUi() {
        midtransUi = MidtransUi.getInstance();
    }

    private void initPresenter() {
        presenter = new GiftCardIndonesiaPresenter(this);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        itemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        payNowButton = (FancyButton) findViewById(R.id.btn_pay_now);
        titleText = (TextView) findViewById(R.id.page_title);
        cardNumberField = (AppCompatEditText) findViewById(R.id.card_number_field);
        passwordField = (AppCompatEditText) findViewById(R.id.password_field);
        cardNumberTextInput = (TextInputLayout) findViewById(R.id.card_number_text_input);
        passwordTextInput = (TextInputLayout) findViewById(R.id.password_text_input);
    }

    private void initThemes() {
        // Set color theme for item details bar
        setBackgroundColor(itemDetails, Theme.PRIMARY_COLOR);
        setBackgroundColor(payNowButton, Theme.PRIMARY_COLOR);
        // Set color theme for edit text field
        setTextInputLayoutColorFilter(cardNumberTextInput);
        setTextInputLayoutColorFilter(passwordTextInput);
        setEditTextCompatBackgroundTintColor(cardNumberField);
        setEditTextCompatBackgroundTintColor(passwordField);
        // Set font into pay now button
        if (!TextUtils.isEmpty(midtransUi.getSemiBoldFontPath())) {
            payNowButton.setCustomTextFont(midtransUi.getSemiBoldFontPath());
        }
        initThemeColor();
    }

    private void initToolbar() {
        // Set title
        setHeaderTitle(getString(R.string.title_gci));

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
                if (checkFormValidity()) {
                    String cardNumber = cardNumberField.getText().toString().trim().replace(" ", "");
                    String password = passwordField.getText().toString().trim();
                    showProgressDialog(getString(R.string.processing_payment));
                    presenter.startPayment(cardNumber, password);
                }
            }
        });
    }

    private void initCardNumberField() {
        cardNumberField.addTextChangedListener(new TextWatcher() {
            private static final char SPACE = ' ';

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                cardNumberTextInput.setError(null);

                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (SPACE == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {

                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a SPACE we insert a SPACE
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf
                            (SPACE)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(SPACE));
                    }

                }

                // Move to next input
                if (s.length() >= 19) {
                    if (checkCardNumberValidity()) {
                        passwordField.requestFocus();
                    }
                }
            }
        });
    }

    private void setHeaderTitle(String title) {
        titleText.setText(title);
    }

    private void startPaymentStatus(GiftCardPaymentResponse paymentResponse) {
        Intent intent = new Intent(this, PaymentStatusActivity.class);
        intent.putExtra(Constants.PAYMENT_RESULT, paymentResponse);
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

    private void finishPayment(int resultCode, PaymentResult<GiftCardPaymentResponse> paymentResult) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void onGiftCardIndonesiaSuccess(GiftCardPaymentResponse response) {
        dismissProgressDialog();
        if (midtransUi.getCustomSetting().isShowPaymentStatus()) {
            startPaymentStatus(response);
        } else {
            finishPayment(RESULT_OK, presenter.getPaymentResult());
        }
    }

    @Override
    public void onGiftCardIndonesiaFailure(String message) {
        dismissProgressDialog();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean checkFormValidity() {
        return checkCardNumberValidity() && checkPasswordValidity();
    }

    private boolean checkCardNumberValidity() {
        boolean isValid = true;

        String cardNumber = cardNumberField.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(cardNumber)) {
            cardNumberTextInput.setError(getString(R.string.validation_message_card_number));
            isValid = false;
        } else {
            cardNumberTextInput.setError(null);
        }

        if (cardNumber.length() != 16) {
            cardNumberTextInput.setError(getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            cardNumberTextInput.setError(null);
        }
        return isValid;
    }

    public boolean checkPasswordValidity() {
        boolean valid = true;
        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordTextInput.setError(getString(R.string.error_password_empty));
            valid = false;
        } else if (password.length() < 3) {
            passwordTextInput.setError(getString(R.string.error_password_invalid));
            valid = false;
        } else {
            passwordTextInput.setError(null);
        }
        return valid;
    }
}
