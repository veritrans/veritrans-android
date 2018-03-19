package com.midtrans.demo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.midtrans.demo.models.SavedCard;
import com.midtrans.demo.widgets.DemoButton;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.scancard.ExternalScanner;
import com.midtrans.sdk.uikit.scancard.ScannerModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ziahaqi on 5/4/17.
 */

public class DemoCardRegisterActivity extends AppCompatActivity {

    public static final String CARD_REGISTER_RESPONSE = "register.response";
    private static final int SCAN_REQUEST_CODE = 101;

    private DemoButton buttonCardRegister;
    private DemoButton buttonScanCard;
    private Toolbar toolbar;

    private TextInputEditText editCardNumber;
    private TextInputEditText editCardExpDate;
    private TextInputEditText editCardCvv;

    private TextInputLayout layoutCardNumber;
    private TextInputLayout layoutCardExp;
    private TextInputLayout layoutCardCvv;

    private ProgressDialog progressDialog;
    private ImageView imageCardLogo;
    private ImageView imageCvvHelp;
    private String lastExpDate = "";
    private String cardType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_card_register);
        bindViews();
        initThemes();
        initToolbar();
        initCardNumber();
        initCardExpiry();
        initButtonRegister();
        initButtonScanCard();
    }


    private void initThemes() {
        String theme = DemoPreferenceHelper.getStringPreference(this, DemoConfigActivity.COLOR_THEME);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back);
        if (theme != null && !TextUtils.isEmpty(theme)) {
            switch (theme) {
                case DemoThemeConstants.BLUE_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    imageCvvHelp.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    buttonCardRegister.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    buttonScanCard.setBorderColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    buttonScanCard.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    buttonScanCard.setIconColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    break;
                case DemoThemeConstants.RED_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    imageCvvHelp.setColorFilter(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    buttonCardRegister.setBackgroundColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_HEX));
                    buttonScanCard.setBorderColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX));
                    buttonScanCard.setTextColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX));
                    buttonScanCard.setIconColorFilter(Color.parseColor(DemoThemeConstants.RED_PRIMARY_HEX));
                    break;
                case DemoThemeConstants.GREEN_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    imageCvvHelp.setColorFilter(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    buttonCardRegister.setBackgroundColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_HEX));
                    buttonScanCard.setBorderColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX));
                    buttonScanCard.setTextColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX));
                    buttonScanCard.setIconColorFilter(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_HEX));
                    break;
                case DemoThemeConstants.ORANGE_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    imageCvvHelp.setColorFilter(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    buttonCardRegister.setBackgroundColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_HEX));
                    buttonScanCard.setBorderColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX));
                    buttonScanCard.setTextColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX));
                    buttonScanCard.setIconColorFilter(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_HEX));
                    break;
                case DemoThemeConstants.BLACK_THEME:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    imageCvvHelp.setColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    buttonCardRegister.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_HEX));
                    buttonScanCard.setBorderColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX));
                    buttonScanCard.setTextColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX));
                    buttonScanCard.setIconColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_HEX));
                    break;
                default:
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    imageCvvHelp.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    buttonCardRegister.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    buttonScanCard.setBorderColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    buttonScanCard.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    buttonScanCard.setIconColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    break;
            }
        } else {
            drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
            imageCvvHelp.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
            buttonCardRegister.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
            buttonScanCard.setBorderColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
            buttonScanCard.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
            buttonScanCard.setIconColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
        }

        toolbar.setNavigationIcon(drawable);
    }

    private void initButtonRegister() {
        buttonCardRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCardRegistration();
            }
        });
    }

    private void initButtonScanCard() {
        buttonScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransSDK.getInstance().getExternalScanner().startScan(DemoCardRegisterActivity.this, SCAN_REQUEST_CODE);
            }
        });
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void bindViews() {
        buttonCardRegister = (DemoButton) findViewById(R.id.button_register);
        buttonScanCard = (DemoButton) findViewById(R.id.button_scan_card);

        toolbar = (Toolbar) findViewById(R.id.main_toolbar);

        editCardCvv = (TextInputEditText) findViewById(R.id.edit_card_cvv);
        editCardNumber = (TextInputEditText) findViewById(R.id.edit_card_number);
        editCardExpDate = (TextInputEditText) findViewById(R.id.edit_card_exp);

        layoutCardNumber = (TextInputLayout) findViewById(R.id.til_card_number);
        layoutCardExp = (TextInputLayout) findViewById(R.id.til_card_exp);
        layoutCardCvv = (TextInputLayout) findViewById(R.id.til_card_cvv);

        imageCardLogo = (ImageView) findViewById(R.id.image_card_logo);
        imageCvvHelp = (ImageView) findViewById(R.id.image_cvv_help);
    }

    private void initCardNumber() {
        editCardNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkCardNumberValidity();
                }
            }
        });

        editCardNumber.addTextChangedListener(new TextWatcher() {
            private static final char SPACE_CHAR = ' ';

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                layoutCardNumber.setError(null);
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (SPACE_CHAR == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf
                            (SPACE_CHAR)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(SPACE_CHAR));
                    }
                }
                String cardType = Utils.getCardType(s.toString());

                setCardType();

                // Move to next input
                if (s.length() >= 18 && cardType.equals(getString(com.midtrans.sdk.uikit.R.string.amex))) {
                    if (s.length() == 19) {
                        s.delete(s.length() - 1, s.length());
                    }
                    if (checkCardNumberValidity()) {
                        editCardExpDate.requestFocus();
                    }
                } else if (s.length() == 19) {
                    if (checkCardNumberValidity()) {
                        editCardExpDate.requestFocus();
                    }
                }
            }
        });
    }

    private void setCardType() {
        // Don't set card type when card number is empty
        String cardNumberText = editCardNumber.getText().toString().trim();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 2) {
            imageCardLogo.setImageResource(0);
            return;
        }

        // Check card type before setting logo
        cardType = Utils.getCardType(cardNumberText);
        switch (cardType) {
            case Utils.CARD_TYPE_VISA:
                imageCardLogo.setImageResource(com.midtrans.sdk.uikit.R.drawable.ic_visa);
                break;
            case Utils.CARD_TYPE_MASTERCARD:
                imageCardLogo.setImageResource(com.midtrans.sdk.uikit.R.drawable.ic_mastercard);
                break;
            case Utils.CARD_TYPE_JCB:
                imageCardLogo.setImageResource(com.midtrans.sdk.uikit.R.drawable.ic_jcb);
                break;
            case Utils.CARD_TYPE_AMEX:
                imageCardLogo.setImageResource(com.midtrans.sdk.uikit.R.drawable.ic_amex);
                break;
        }
    }

    private boolean checkCardNumberValidity() {

        boolean isValid = true;

        String cardNumberText = editCardNumber.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(cardNumberText)) {
            layoutCardNumber.setError(getString(com.midtrans.sdk.uikit.R.string.validation_message_card_number));
            isValid = false;
        } else {
            layoutCardNumber.setError(null);
        }
        if (cardNumberText.length() < 13 || !isValidCardNumber(cardNumberText)) {
            layoutCardNumber.setError(getString(com.midtrans.sdk.uikit.R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            layoutCardNumber.setError(null);
        }

        return isValid;
    }

    private boolean isValidCardNumber(String ccNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = ccNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(ccNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        boolean isvalid = (sum % 10 == 0);
        Logger.i("isValid:" + isvalid);
        return isvalid;
    }

    private void initCardExpiry() {
        editCardExpDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkCardExpiryValidity();
                }
            }
        });

        editCardExpDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (s.length() == 4) {
                    if (lastExpDate.length() > s.length()) {

                        try {
                            int month = Integer.parseInt(input.substring(0, 2));
                            if (month <= 12) {
                                editCardExpDate.setText(editCardExpDate.getText().toString().substring(0, 1));
                                editCardExpDate.setSelection(editCardExpDate.getText().toString().length());
                            } else {
                                editCardExpDate.setText("");
                                editCardExpDate.setSelection(editCardExpDate.getText().toString().length());
                            }
                        } catch (Exception exception) {
                            Logger.e(exception.toString());
                        }
                    }
                } else if (s.length() == 2) {

                    if (lastExpDate.length() < s.length()) {

                        try {
                            int month = Integer.parseInt(input);

                            if (month <= 12) {
                                editCardExpDate.setText(getString(com.midtrans.sdk.uikit.R.string.expiry_month_format, editCardExpDate.getText().toString()));
                                editCardExpDate.setSelection(editCardExpDate.getText().toString().length());
                            } else {
                                editCardExpDate.setText(getString(com.midtrans.sdk.uikit.R.string.expiry_month_int_format, 12));
                                editCardExpDate.setSelection(editCardExpDate.getText().toString().length());
                            }

                        } catch (Exception exception) {
                            Logger.e(exception.toString());
                        }
                    }
                } else if (s.length() == 1) {
                    try {
                        int month = Integer.parseInt(input);
                        if (month > 1) {
                            editCardExpDate.setText(getString(com.midtrans.sdk.uikit.R.string.expiry_month_single_digit_format, editCardExpDate.getText().toString()));
                            editCardExpDate.setSelection(editCardExpDate.getText().toString().length());
                        }
                    } catch (Exception exception) {
                        Logger.e(exception.toString());
                    }
                }
                lastExpDate = editCardExpDate.getText().toString();

                // Move to next input
                if (s.length() == 7) {
                    editCardCvv.requestFocus();
                }
            }
        });
    }

    private boolean checkCardExpiryValidity() {

        boolean isValid = true;
        String expiryDate = editCardExpDate.getText().toString().trim();
        String[] expDateArray = new String[2];
        int expMonth = 0;
        int expYear = 0;
        try {
            expDateArray = expiryDate.split("/");
            expDateArray[0] = expDateArray[0].trim();
            expDateArray[1] = expDateArray[1].trim();
            Logger.i("expDate:" + expDateArray[0].trim(), "" + expDateArray[1].trim());
        } catch (NullPointerException e) {
            Logger.i("expiry date empty");
        } catch (IndexOutOfBoundsException e) {
            Logger.i("expiry date issue");
        }

        if (TextUtils.isEmpty(expiryDate)) {
            layoutCardExp.setError(getString(com.midtrans.sdk.uikit.R.string.validation_message_empty_expiry_date));
            isValid = false;
        } else if (!expiryDate.contains("/")) {
            layoutCardExp.setError(getString(com.midtrans.sdk.uikit.R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else if (expDateArray.length != 2) {
            layoutCardExp.setError(getString(com.midtrans.sdk.uikit.R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else {
            try {
                expMonth = Integer.parseInt(expDateArray[0]);
            } catch (NumberFormatException e) {
                layoutCardExp.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            }
            try {
                expYear = Integer.parseInt(expDateArray[1]);
            } catch (NumberFormatException e) {
                layoutCardExp.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            }
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yy");
            String year = format.format(date);

            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int currentYear = Integer.parseInt(year);
            Logger.i("currentMonth:" + currentMonth + ",currentYear:" + currentYear);

            if (expYear < currentYear) {
                layoutCardExp.setError(getString(com.midtrans.sdk.uikit.R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else if (expYear == currentYear && currentMonth > expMonth) {
                layoutCardExp.setError(getString(com.midtrans.sdk.uikit.R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else {
                layoutCardExp.setError(null);
            }
        }
        return isValid;
    }

    private void performCardRegistration() {
        if (!isValidCardInformation()) {
            return;
        }

        showProgressDialog();

        String cardNumberText = editCardNumber.getText().toString();
        String cvvText = editCardCvv.getText().toString();
        String date = editCardExpDate.getText().toString();
        String month = date.split("/")[0].trim();
        String year = "20" + date.split("/")[1].trim();

        MidtransSDK.getInstance().cardRegistration(cardNumberText, cvvText, month, year, new CardRegistrationCallback() {
            @Override
            public void onSuccess(CardRegistrationResponse response) {
                hideProgressDialog();
                Toast.makeText(DemoCardRegisterActivity.this, getString(R.string.save_card_successfully), Toast.LENGTH_SHORT).show();
                saveAndFinish(response);
            }

            @Override
            public void onFailure(CardRegistrationResponse response, String reason) {
                hideProgressDialog();
                Toast.makeText(DemoCardRegisterActivity.this, reason, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable error) {
                hideProgressDialog();
                Toast.makeText(DemoCardRegisterActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkCardCVVValidity() {
        boolean isValid = true;
        String cvv = editCardCvv.getText().toString().trim();
        if (TextUtils.isEmpty(cvv)) {
            layoutCardCvv.setError(getString(R.string.validation_message_cvv));
            isValid = false;
        } else {

            if (cvv.length() < 3) {
                layoutCardCvv.setError(getString(com.midtrans.sdk.uikit.R.string.validation_message_invalid_cvv));
                isValid = false;
            } else {
                layoutCardCvv.setError(null);
            }
        }
        return isValid;
    }

    private boolean isValidCardInformation() {
        return checkCardNumberValidity() && checkCardExpiryValidity() && checkCardCVVValidity();
    }

    private void saveAndFinish(CardRegistrationResponse response) {
        SavedCard savedCard = new SavedCard(cardType, response.getSavedTokenId(), response.getTransactionId(), response.getMaskedCard());

        Intent data = new Intent();
        data.putExtra(CARD_REGISTER_RESPONSE, savedCard);
        setResult(RESULT_OK, data);
        finish();
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_REQUEST_CODE) {
            if (data != null && data.hasExtra(ExternalScanner.EXTRA_SCAN_DATA)) {
                ScannerModel scanData = (ScannerModel) data.getSerializableExtra(ExternalScanner.EXTRA_SCAN_DATA);
                Logger.d("scancard", String.format("Card Number: %s, Card Expire: %s/%d", scanData.getCardNumber(), scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                updateScanCardData(scanData);
            }
        }
    }

    private void updateScanCardData(ScannerModel scanData) {
        String cardNumber = Utils.getFormattedCreditCardNumber(Utils.getFormattedCreditCardNumber(scanData.getCardNumber()));
        String expDate = String.format("%s/%d", scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000);
        String cvv = scanData.getCvv();

        editCardCvv.setText(cvv);
        editCardExpDate.setText(expDate);
        editCardNumber.setText(cardNumber);
    }
}
