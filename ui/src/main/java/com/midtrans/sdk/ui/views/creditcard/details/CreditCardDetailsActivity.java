package com.midtrans.sdk.ui.views.creditcard.details;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.core.models.papi.CardTokenResponse;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.core.utils.CardUtilities;
import com.midtrans.sdk.ui.CustomSetting;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.adapters.ItemDetailsAdapter;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Theme;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.views.PaymentStatusActivity;
import com.midtrans.sdk.ui.views.webpayment.PaymentWebActivity;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by rakawm on 3/27/17.
 */

public class CreditCardDetailsActivity extends BaseActivity implements CreditCardDetailsView {

    public static final String EXTRA_CARD_DETAILS = "card.details";
    public static final int STATUS_REQUEST_CODE = 1015;

    private MidtransUi midtransUi;
    private CreditCardDetailsPresenter presenter;

    private RecyclerView itemDetails;

    private EditText cardNumberField;
    private EditText cardCvvField;
    private EditText cardExpiryField;

    private TextView cardNumberErrorText;
    private TextView cardCvvErrorText;
    private TextView cardExpiryErrorText;

    private ImageView cardLogo;
    private ImageView bankLogo;
    private ImageView promoLogo;

    private ImageButton cvvHelpButton;
    private ImageButton saveCardHelpButton;
    private ImageButton pointHelpButton;

    private FancyButton scanCardButton;
    private FancyButton deleteCardButton;
    private FancyButton payNowButton;

    private RelativeLayout containerSaveCard;
    private LinearLayout containerInstallment;
    private RelativeLayout containerPoint;

    private ProgressDialog progressDialog;

    private AppCompatCheckBox checkboxSaveCard;
    private AppCompatCheckBox checkboxPointEnabled;

    private String lastExpDate = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_payment);
        initPresenter();
        initMidtransUi();
        initViews();
        initThemes();
        initItemDetails();
        initToolbar();
        initCardNumberEditText();
        initExpiryEditText();
        initFocusChanges();
        initSaveCardLayout();
        initSaveCardCheckbox();
        initPayNowButton();
        initProgressDialog();
    }

    private void initPresenter() {
        presenter = new CreditCardDetailsPresenter();
        presenter.init(this);
    }

    private void initMidtransUi() {
        midtransUi = MidtransUi.getInstance();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        itemDetails = (RecyclerView) findViewById(R.id.container_item_details);

        cardNumberField = (EditText) findViewById(R.id.field_card_number);
        cardCvvField = (EditText) findViewById(R.id.field_cvv);
        cardExpiryField = (EditText) findViewById(R.id.field_expiry);

        cardNumberErrorText = (TextView) findViewById(R.id.error_message_card_number);
        cardExpiryErrorText = (TextView) findViewById(R.id.error_message_expiry);
        cardCvvErrorText = (TextView) findViewById(R.id.error_message_cvv);

        cardLogo = (ImageView) findViewById(R.id.payment_card_logo);
        bankLogo = (ImageView) findViewById(R.id.bank_logo);
        promoLogo = (ImageView) findViewById(R.id.promo_logo);

        cvvHelpButton = (ImageButton) findViewById(R.id.help_cvv_button);
        saveCardHelpButton = (ImageButton) findViewById(R.id.help_save_card);
        pointHelpButton = (ImageButton) findViewById(R.id.help_bni_point);

        scanCardButton = (FancyButton) findViewById(R.id.button_scan_card);
        deleteCardButton = (FancyButton) findViewById(R.id.button_delete);
        payNowButton = (FancyButton) findViewById(R.id.btn_pay_now);

        containerSaveCard = (RelativeLayout) findViewById(R.id.container_save_card_details);
        containerInstallment = (LinearLayout) findViewById(R.id.container_installment);
        containerPoint = (RelativeLayout) findViewById(R.id.container_bni_point);

        checkboxSaveCard = (AppCompatCheckBox) findViewById(R.id.checkbox_save_card);
        checkboxPointEnabled = (AppCompatCheckBox) findViewById(R.id.checkbox_point);
    }

    private void initThemes() {
        // Set color theme for item details bar
        setBackgroundColor(itemDetails, Theme.PRIMARY_COLOR);
        setBackgroundColor(payNowButton, Theme.PRIMARY_COLOR);
        // Set color theme for scan card button
        setBorderColor(scanCardButton);
        setTextColor(scanCardButton);
        setIconColorFilter(scanCardButton);
        // Set color filter on help icon buttons
        filterColor(cvvHelpButton);
        filterColor(saveCardHelpButton);
        filterColor(pointHelpButton);
        // Set color filter on checkbox
        setCheckoxStateColor(checkboxSaveCard);
        setCheckoxStateColor(checkboxPointEnabled);
        // Set background color filter on text field
        filterEditTextColor(cardNumberField);
        filterEditTextColor(cardExpiryField);
        filterEditTextColor(cardCvvField);

        initThemeColor();
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

    private void initToolbar() {
        // Set title
        TextView titleText = (TextView) findViewById(R.id.page_title);
        titleText.setText(R.string.card_details);

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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();

        if (midtransUi.getCustomSetting().isAnimationEnabled()) {
            overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
        }
    }

    private void initCardNumberEditText() {
        cardNumberField.addTextChangedListener(new TextWatcher() {
            private static final char SPACE_CHAR = ' ';

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                hideValidationError(cardNumberErrorText);

                // Add space between 4 characters
                if (editable.length() > 0 && (editable.length() % 5) == 0) {
                    final char c = editable.charAt(editable.length() - 1);
                    if (SPACE_CHAR == c) {
                        editable.delete(editable.length() - 1, editable.length());
                    }
                }

                // Insert char where needed.
                if (editable.length() > 0 && (editable.length() % 5) == 0) {
                    char c = editable.charAt(editable.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(editable.toString(), String.valueOf
                            (SPACE_CHAR)).length <= 3) {
                        editable.insert(editable.length() - 1, String.valueOf(SPACE_CHAR));
                    }
                }

                String cardType = CardUtilities.getCardType(cardNumberField.getText().toString());
                setCardType(cardType);

                // Move to next input
                if (editable.length() >= 18 && cardType.equals(getString(R.string.amex))) {
                    if (editable.length() == 19) {
                        editable.delete(editable.length() - 1, editable.length());
                    }
                    if (checkCardNumberValidity()) {
                        cardExpiryField.requestFocus();
                    }
                } else if (editable.length() == 19) {
                    if (checkCardNumberValidity()) {
                        cardExpiryField.requestFocus();
                    }
                }
            }
        });
    }

    private void initExpiryEditText() {
        cardExpiryField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = editable.toString();
                if (editable.length() == 4) {
                    if (lastExpDate.length() > editable.length()) {

                        try {
                            int month = Integer.parseInt(input.substring(0, 2));
                            if (month <= 12) {
                                cardExpiryField.setText(cardExpiryField.getText().toString().substring(0, 1));
                                cardExpiryField.setSelection(cardExpiryField.getText().toString().length());
                            } else {
                                cardExpiryField.setText("");
                                cardExpiryField.setSelection(cardExpiryField.getText().toString().length());
                            }
                        } catch (Exception exception) {
                            Logger.e(TAG, exception.toString());
                        }
                    }
                } else if (editable.length() == 2) {
                    if (lastExpDate.length() < editable.length()) {
                        try {
                            int month = Integer.parseInt(input);
                            if (month <= 12) {
                                cardExpiryField.setText(getString(R.string.expiry_month_format, cardExpiryField.getText().toString()));
                                cardExpiryField.setSelection(cardExpiryField.getText().toString().length());
                            } else {
                                cardExpiryField.setText(getString(R.string.expiry_month_int_format, Constants.MONTH_COUNT));
                                cardExpiryField.setSelection(cardExpiryField.getText().toString().length());
                            }
                        } catch (Exception exception) {
                            Logger.e(TAG, exception.toString());
                        }
                    }
                } else if (editable.length() == 1) {
                    try {
                        int month = Integer.parseInt(input);
                        if (month > 1) {
                            cardExpiryField.setText(getString(R.string.expiry_month_single_digit_format, cardExpiryField.getText().toString()));
                            cardExpiryField.setSelection(cardExpiryField.getText().toString().length());
                        }
                    } catch (Exception exception) {
                        Logger.e(TAG, exception.toString());
                    }
                }
                lastExpDate = cardExpiryField.getText().toString();
                // Move to next input
                if (editable.length() == 7) {
                    if (checkCardExpiryValidity()) {
                        cardCvvField.requestFocus();
                    }
                }
            }
        });
    }

    private void initFocusChanges() {
        cardNumberField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    checkCardNumberValidity();
                }
            }
        });
        cardExpiryField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    checkCardExpiryValidity();
                }
            }
        });
        cardCvvField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    checkCardCVVValidity();
                }
            }
        });
    }

    private void initSaveCardLayout() {
        if (midtransUi.getTransaction().creditCard.saveCard) {
            containerSaveCard.setVisibility(View.VISIBLE);
        } else {
            containerSaveCard.setVisibility(View.GONE);
        }
    }

    private void initSaveCardCheckbox() {
        CustomSetting customSetting = midtransUi.getCustomSetting();
        if (customSetting.isSaveCardChecked()) {
            checkboxSaveCard.setChecked(true);
        }

        checkboxSaveCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkCardValidity();
            }
        });
    }

    private void initPayNowButton() {
        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkCardValidity()) {
                    String cardNumber = cardNumberField.getText().toString();
                    String expiry = cardExpiryField.getText().toString();
                    String cvv = cardCvvField.getText().toString();
                    showProgressDialog();
                    // Start payment
                    presenter.startTokenize(
                            cardNumber.replace(" ", ""),
                            expiry.split(" / ")[0],
                            expiry.split(" / ")[1],
                            cvv
                    );
                }
            }
        });
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.processing_payment));
    }

    private void setCardType(String cardType) {
        switch (cardType) {
            case CardUtilities.CARD_TYPE_VISA:
                cardLogo.setImageResource(R.drawable.ic_visa);
                break;
            case CardUtilities.CARD_TYPE_MASTERCARD:
                cardLogo.setImageResource(R.drawable.ic_mastercard);
                break;
            case CardUtilities.CARD_TYPE_JCB:
                cardLogo.setImageResource(R.drawable.ic_jcb);
                break;
            case CardUtilities.CARD_TYPE_AMEX:
                cardLogo.setImageResource(R.drawable.ic_amex);
                break;
            default:
                cardLogo.setImageResource(0);
        }
    }

    private boolean checkCardValidity() {
        boolean cardNumberValidity = checkCardNumberValidity();
        boolean cardExpiryValidity = checkCardExpiryValidity();
        boolean cardCVVValidity = checkCardCVVValidity();
        return cardNumberValidity && cardExpiryValidity && cardCVVValidity;
    }

    private boolean checkCardNumberValidity() {
        if (isTwoClickMode()) {
            return true;
        }

        boolean isValid = true;

        String cardNumberText = cardNumberField.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(cardNumberText)) {
            showValidationError(cardNumberErrorText, getString(R.string.validation_message_card_number));
            isValid = false;
        } else {
            hideValidationError(cardNumberErrorText);
        }

        if (cardNumberText.length() < 13 || !CardUtilities.isValidCardNumber(cardNumberText)) {
            showValidationError(cardNumberErrorText, getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            hideValidationError(cardNumberErrorText);
        }

        if (!isValid) {
            //TODO: track invalid cc number
            //midtransSDK.trackEvent(AnalyticsEventName.CREDIT_CARD_NUMBER_VALIDATION, MixpanelAnalyticsManager.CARD_MODE_NORMAL);
        }
        return isValid;
    }

    private boolean checkCardExpiryValidity() {
        if (isTwoClickMode()) {
            return true;
        }

        boolean isValid = true;
        String expiryDate = cardExpiryField.getText().toString().trim();
        String[] expDateArray = new String[2];
        int expMonth = 0;
        int expYear = 0;
        try {
            expDateArray = expiryDate.split("/");
            expDateArray[0] = expDateArray[0].trim();
            expDateArray[1] = expDateArray[1].trim();
            Logger.d(TAG, "expDate:" + expDateArray[0].trim() + expDateArray[1].trim());
        } catch (NullPointerException e) {
            Logger.d(TAG, "expiry date empty");
        } catch (IndexOutOfBoundsException e) {
            Logger.d(TAG, "expiry date issue");
        }

        if (TextUtils.isEmpty(expiryDate)) {
            showValidationError(cardExpiryErrorText, getString(R.string.validation_message_empty_expiry_date));
            isValid = false;
        } else if (!expiryDate.contains("/")) {
            showValidationError(cardExpiryErrorText, getString(R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else if (expDateArray.length != 2) {
            showValidationError(cardExpiryErrorText, getString(R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else {
            try {
                expMonth = Integer.parseInt(expDateArray[0]);
            } catch (NumberFormatException e) {
                showValidationError(cardExpiryErrorText, getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            }
            try {
                expYear = Integer.parseInt(expDateArray[1]);
            } catch (NumberFormatException e) {
                showValidationError(cardExpiryErrorText, getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            }
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yy");
            String year = format.format(date);

            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int currentYear = Integer.parseInt(year);
            Logger.d(TAG, "currentMonth:" + currentMonth + ",currentYear:" + currentYear);

            if (expYear < currentYear) {
                showValidationError(cardExpiryErrorText, getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else if (expYear == currentYear && currentMonth > expMonth) {
                showValidationError(cardExpiryErrorText, getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else {
                hideValidationError(cardExpiryErrorText);
            }
        }

        if (!isValid) {
            //TODO: track invalid cc expiry
            //midtransSDK.trackEvent(AnalyticsEventName.CREDIT_CARD_EXPIRY_VALIDATION, MixpanelAnalyticsManager.CARD_MODE_NORMAL);
        }
        return isValid;
    }

    private boolean checkCardCVVValidity() {
        if (isOneClickMode()) {
            return true;
        }

        boolean isValid = true;
        String cvv = cardCvvField.getText().toString().trim();
        if (TextUtils.isEmpty(cvv)) {
            showValidationError(cardCvvErrorText, getString(R.string.validation_message_cvv));
            isValid = false;

        } else {
            if (cvv.length() < 3) {
                showValidationError(cardCvvErrorText, getString(R.string.validation_message_invalid_cvv));
                isValid = false;
            } else {
                hideValidationError(cardCvvErrorText);
            }
        }

        if (!isValid) {
            //TODO: track invalid cc cvv
            //midtransSDK.trackEvent(AnalyticsEventName.CREDIT_CARD_CVV_VALIDATION, MixpanelAnalyticsManager.CARD_MODE_NORMAL);
        }
        return isValid;
    }

    private boolean isTwoClickMode() {
        //TODO: Change to real implementation
        return false;
    }

    private boolean isOneClickMode() {
        //TODO: Change to real implementation
        return false;
    }

    private void showValidationError(TextView textView, String errorText) {
        if (errorText != null && !TextUtils.isEmpty(errorText)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(errorText);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void hideValidationError(TextView textView) {
        textView.setVisibility(View.GONE);
    }

    @Override
    public void onGetCardTokenSuccess(CardTokenResponse response) {
        if (response.redirectUrl != null
                && !TextUtils.isEmpty(response.redirectUrl)) {
            dismissProgressDialog();
            UiUtils.hideKeyboard(cardCvvField, this);
            start3DSecurePage(response.redirectUrl);
        } else {
            presenter.startPayment();
        }
    }

    @Override
    public void onGetCardTokenFailure(CardTokenResponse response) {
        dismissProgressDialog();
        // TODO: Show proper error message
        Toast.makeText(this, "Get card token failed. Cause: " + response.statusMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetCardTokenError(String message) {
        dismissProgressDialog();
        // TODO: Show proper error message
        Toast.makeText(this, "Get card token error. Cause: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreditCardPaymentError(Throwable throwable) {
        dismissProgressDialog();
        Toast.makeText(this, "Payment failed. Cause: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreditCardPaymentFailure(CreditCardPaymentResponse response) {
        dismissProgressDialog();
        Toast.makeText(this, "Payment error. Cause: " + response.statusMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreditCardPaymentSuccess(CreditCardPaymentResponse response) {
        dismissProgressDialog();
        if (midtransUi.getCustomSetting().isShowPaymentStatus()) {
            startPaymentStatus(presenter.getPaymentResult());
        } else {
            finishPayment(RESULT_OK, presenter.getPaymentResult());
        }
    }

    @Override
    public String getMaskedCardNumber() {
        return null;
    }

    @Override
    public boolean isSaveEnabled() {
        return checkboxSaveCard.isChecked();
    }

    private void showProgressDialog() {
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    private void start3DSecurePage(String url) {
        Intent intent = new Intent(this, PaymentWebActivity.class);
        intent.putExtra(Constants.WEB_VIEW_URL, url);
        startActivityForResult(intent, Constants.INTENT_CODE_WEB_PAYMENT);
    }

    private void startPaymentStatus(PaymentResult<CreditCardPaymentResponse> cardPaymentResponse) {
        Intent intent = new Intent(this, PaymentStatusActivity.class);
        intent.putExtra(Constants.PAYMENT_RESULT, cardPaymentResponse);
        startActivityForResult(intent, STATUS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.d(TAG, "reqCode:" + requestCode + " | res:" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.INTENT_CODE_WEB_PAYMENT) {
                showProgressDialog();
                presenter.startPayment();
            }

            if (requestCode == STATUS_REQUEST_CODE) {
                finishPayment(resultCode, presenter.getPaymentResult());
            }

        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == Constants.INTENT_CODE_WEB_PAYMENT) {
                dismissProgressDialog();
                presenter.startPayment();
            }
        }
    }

    private void finishPayment(int resultCode, PaymentResult<CreditCardPaymentResponse> paymentResult) {
        Intent data = new Intent();
        data.putExtra(Constants.PAYMENT_RESULT, paymentResult);
        setResult(resultCode, data);
        finish();
    }
}
