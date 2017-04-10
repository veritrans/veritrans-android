package com.midtrans.sdk.ui.views.ebanking.mandiri_clickpay;

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

import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentResponse;
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
 * Created by rakawm on 4/10/17.
 */

public class MandiriClickpayActivity extends BaseActivity implements MandiriClickpayView {

    private MidtransUi midtransUi;

    private RecyclerView itemDetails;
    private TextView titleText;
    private FancyButton payNowButton;
    private ProgressDialog progressDialog;

    private TextInputLayout cardNumberTextInput;
    private TextInputLayout tokenResponseTextInput;
    private AppCompatEditText cardNumberField;
    private AppCompatEditText tokenResponseField;
    private TextView input1Text;
    private TextView input2Text;
    private TextView input3Text;

    private MandiriClickpayPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mandiri_clickpay);
        initMidtransUi();
        initPresenter();
        initViews();
        initThemes();
        initToolbar();
        initProgressDialog();
        initItemDetails();
        initPayNowButton();
        initCardNumberField();
        initValues();
    }

    private void initMidtransUi() {
        midtransUi = MidtransUi.getInstance();
    }

    private void initPresenter() {
        presenter = new MandiriClickpayPresenter(this);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        itemDetails = (RecyclerView) findViewById(R.id.container_item_details);
        payNowButton = (FancyButton) findViewById(R.id.btn_pay_now);
        titleText = (TextView) findViewById(R.id.page_title);
        cardNumberField = (AppCompatEditText) findViewById(R.id.debit_card_no_field);
        tokenResponseField = (AppCompatEditText) findViewById(R.id.challenge_token_field);
        cardNumberTextInput = (TextInputLayout) findViewById(R.id.debit_card_no_text_input);
        tokenResponseTextInput = (TextInputLayout) findViewById(R.id.challenge_token_text_input);
        input1Text = (TextView) findViewById(R.id.text_input_1);
        input2Text = (TextView) findViewById(R.id.text_input_2);
        input3Text = (TextView) findViewById(R.id.text_input_3);
    }

    private void initThemes() {
        // Set color theme for item details bar
        setBackgroundColor(itemDetails, Theme.PRIMARY_COLOR);
        setBackgroundColor(payNowButton, Theme.PRIMARY_COLOR);
        // Set text field themes
        setTextInputLayoutColorFilter(cardNumberTextInput);
        setTextInputLayoutColorFilter(tokenResponseTextInput);
        setEditTextCompatBackgroundTintColor(cardNumberField);
        setEditTextCompatBackgroundTintColor(tokenResponseField);
        // Set font into pay now button
        if (!TextUtils.isEmpty(midtransUi.getSemiBoldFontPath())) {
            payNowButton.setCustomTextFont(midtransUi.getSemiBoldFontPath());
        }
        initThemeColor();
    }

    private void initToolbar() {
        // Set title
        setHeaderTitle(getString(R.string.mandiri_click_pay));

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
                if (checkInputValidity()) {
                    showProgressDialog(getString(R.string.processing_payment));
                    String cardNumber = cardNumberField.getText().toString().trim().replace(" ", "");
                    String tokenResponse = tokenResponseField.getText().toString().trim();
                    presenter.startPayment(cardNumber, tokenResponse);
                }
            }
        });
    }

    private void initCardNumberField() {
        cardNumberField.addTextChangedListener(new TextWatcher() {
            private static final char SPACE = ' ';

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                onChangeOfDebitCardNumer(charSequence);
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
                        tokenResponseField.requestFocus();
                    }
                }
            }
        });
    }

    private void initValues() {
        // Set input 1 to empty string
        input1Text.setText("");
        // Set input 3 to random number
        input3Text.setText(presenter.getInput3());
        // Set input 2 to transaction amount
        input2Text.setText(presenter.getInput2());
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

    private void startPaymentResult() {
        Intent intent = new Intent(this, PaymentStatusActivity.class);
        intent.putExtra(Constants.PAYMENT_RESULT, presenter.getPaymentResult());
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

    private void finishPayment(int resultCode, PaymentResult<MandiriClickpayPaymentResponse> paymentResult) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        setResult(resultCode, data);
        finish();
    }

    @Override
    public void onMandiriClickpaySuccess(MandiriClickpayPaymentResponse response) {
        dismissProgressDialog();
        if (MidtransUi.getInstance().getCustomSetting().isShowPaymentStatus()) {
            startPaymentResult();
        } else {
            finishPayment(RESULT_OK, presenter.getPaymentResult());
        }
    }

    @Override
    public void onMandiriClickpayFailure(String message) {
        dismissProgressDialog();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean checkInputValidity() {
        return !TextUtils.isEmpty(presenter.getInput3())
                && checkCardNumberValidity()
                && checkTokenResponseValidity();
    }

    private boolean checkCardNumberValidity() {
        String cardNumber = cardNumberField.getText().toString().trim().replace(" ", "");
        if (!TextUtils.isEmpty(cardNumber) && cardNumber.length() == 16) {
            return true;
        }
        cardNumberTextInput.setError(getString(R.string.validation_message_card_number));
        return false;
    }

    private boolean checkTokenResponseValidity() {
        String tokenResponse = tokenResponseField.getText().toString().trim();
        if (!TextUtils.isEmpty(tokenResponse) && tokenResponse.length() == 6) {
            return true;
        }
        tokenResponseField.setError(getString(R.string.validation_message_invalid_token_no));
        return false;
    }

    private void onChangeOfDebitCardNumer(CharSequence text) {
        if (text != null && text.length() > 0) {
            String cardNumber = text.toString().trim().replace(" ", "");

            if (cardNumber.length() > 10) {
                input1Text.setText(cardNumber.substring(cardNumber.length() - 10,
                        cardNumber.length()));
            } else {
                input1Text.setText(cardNumber);
            }
        } else {
            input1Text.setText("");
        }
    }
}
