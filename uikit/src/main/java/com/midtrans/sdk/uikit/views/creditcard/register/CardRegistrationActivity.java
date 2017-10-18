package com.midtrans.sdk.uikit.views.creditcard.register;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BaseActivity;
import com.midtrans.sdk.uikit.scancard.ExternalScanner;
import com.midtrans.sdk.uikit.scancard.ScannerModel;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ziahaqi on 8/18/17.
 */

public class CardRegistrationActivity extends BaseActivity implements CardRegistrationView {

    public static final String EXTRA_CALLBACK = "extra.callback";
    private static final String TAG = CardRegistrationActivity.class.getSimpleName();
    private TextInputLayout containerCardNumber;
    private TextInputLayout containerCardCvv;
    private TextInputLayout containerCardExpiry;

    private TextInputEditText fieldCardNumber;
    private TextInputEditText fieldCardExpiry;
    private TextInputEditText fieldCardCvv;

    private FancyButton buttonScanCard;
    private FancyButton buttonSaveCard;

    private ImageView imageBankLogo;
    private ImageView imageCardLogo;

    private SemiBoldTextView textTitle;

    private CardRegistrationPresenter presenter;
    private String lastExpDate = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProperties();
        setContentView(R.layout.activity_credit_card_register);
        initActionButton();
        initCardNumber();
        initCardExpiry();
        initCardCvv();
        initData();
    }

    private void initData() {
        textTitle.setText(getString(R.string.card_registration));
    }

    private void initProperties() {
        presenter = new CardRegistrationPresenter(this, this);
    }

    private void initActionButton() {
        buttonSaveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerCardNumber();
            }
        });

        buttonScanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionScanCard();
            }
        });
    }

    private void initCardNumber() {
        fieldCardNumber.addTextChangedListener(new TextWatcher() {
            private static final char SPACE_CHAR = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Logger.i(TAG, "card number:" + s.length());
                containerCardNumber.setError(null);
                try {

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
                    setBankType();

                    // Move to next input
                    if (s.length() >= 18 && cardType.equals(getString(R.string.amex))) {
                        if (s.length() == 19) {
                            s.delete(s.length() - 1, s.length());
                        }
                        if (checkCardNumberValidity()) {
                            fieldCardExpiry.requestFocus();
                        }
                    } else if (s.length() == 19) {
                        if (checkCardNumberValidity()) {
                            fieldCardExpiry.requestFocus();
                        }
                    }

                } catch (RuntimeException e) {
                    Logger.e(TAG, "cardnumber:" + e.getMessage());
                }
            }
        });

        // focus change listener
        fieldCardNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    checkCardNumberValidity();
                }
            }
        });
    }


    private void setBankType() {
        // Don't set card type when card number is empty
        String cardNumberText = fieldCardNumber.getText().toString();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 7) {
            imageBankLogo.setImageDrawable(null);
            return;
        }

        String cleanCardNumber = cardNumberText.replace(" ", "");
        String cardBin = cleanCardNumber.substring(0, 6);
        String bank = presenter.getBankByCardBin(cardBin);

        if (bank != null) {

            switch (bank) {
                case BankType.BCA:
                    imageBankLogo.setImageResource(R.drawable.bca);
                    break;
                case BankType.BNI:
                    imageBankLogo.setImageResource(R.drawable.bni);
                    break;
                case BankType.BRI:
                    imageBankLogo.setImageResource(R.drawable.bri);
                    break;
                case BankType.CIMB:
                    imageBankLogo.setImageResource(R.drawable.cimb);
                    break;
                case BankType.MANDIRI:
                    imageBankLogo.setImageResource(R.drawable.mandiri);
                    break;
                case BankType.MAYBANK:
                    imageBankLogo.setImageResource(R.drawable.maybank);
                    break;
                case BankType.BNI_DEBIT_ONLINE:
                    imageBankLogo.setImageResource(R.drawable.bni);
                    break;
                default:
                    imageBankLogo.setImageDrawable(null);
                    break;
            }
        } else {
            imageBankLogo.setImageDrawable(null);
        }
    }

    private void setCardType() {
        // Don't set card type when card number is empty
        String cardNumberText = fieldCardNumber.getText().toString();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 2) {
            imageCardLogo.setImageResource(0);
            return;
        }

        // Check card type before setting logo
        String cleanCardNumber = cardNumberText.replace(" ", "");
        String cardType = Utils.getCardType(cleanCardNumber);
        switch (cardType) {
            case Utils.CARD_TYPE_VISA:
                imageCardLogo.setImageResource(R.drawable.ic_visa);
                break;
            case Utils.CARD_TYPE_MASTERCARD:
                imageCardLogo.setImageResource(R.drawable.ic_mastercard);
                break;
            case Utils.CARD_TYPE_JCB:
                imageCardLogo.setImageResource(R.drawable.ic_jcb);
                break;
            case Utils.CARD_TYPE_AMEX:
                imageCardLogo.setImageResource(R.drawable.ic_amex);
                break;
        }
    }

    private void initCardExpiry() {
        fieldCardExpiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String input = s.toString();

                try {
                    if (s.length() == 4) {
                        if (lastExpDate.length() > s.length()) {

                            try {
                                int month = Integer.parseInt(input.substring(0, 2));
                                if (month <= 12) {
                                    fieldCardExpiry.setText(fieldCardExpiry.getText().toString().substring(0, 1));
                                    fieldCardExpiry.setSelection(fieldCardExpiry.getText().toString().length());
                                } else {
                                    fieldCardExpiry.setText("");
                                    fieldCardExpiry.setSelection(fieldCardExpiry.getText().toString().length());
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
                                    fieldCardExpiry.setText(getString(R.string.expiry_month_format, fieldCardExpiry.getText().toString()));
                                    fieldCardExpiry.setSelection(fieldCardExpiry.getText().toString().length());
                                } else {
                                    fieldCardExpiry.setText(getString(R.string.expiry_month_int_format, UiKitConstants.MONTH_COUNT));
                                    fieldCardExpiry.setSelection(fieldCardExpiry.getText().toString().length());
                                }

                            } catch (Exception exception) {
                                Logger.e(exception.toString());
                            }
                        }
                    } else if (s.length() == 1) {
                        try {
                            int month = Integer.parseInt(input);
                            if (month > 1) {
                                fieldCardExpiry.setText(getString(R.string.expiry_month_single_digit_format, fieldCardExpiry.getText().toString()));
                                fieldCardExpiry.setSelection(fieldCardExpiry.getText().toString().length());
                            }
                        } catch (Exception exception) {
                            Logger.e(exception.toString());
                        }
                    }

                } catch (RuntimeException e) {
                    Logger.e(TAG, "inputcardnumber:" + e.getMessage());
                }
                lastExpDate = fieldCardExpiry.getText().toString();

                // Move to next input
                if (s.length() == 7) {
                    fieldCardCvv.requestFocus();
                }
            }
        });

        fieldCardExpiry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    checkCardExpiryValidity();
                }
            }
        });

    }

    private void initCardCvv() {
        fieldCardCvv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    checkCardCvvValidity();
                }
            }
        });
    }


    private boolean checkCardNumberValidity() {

        boolean isValid = true;

        String cardNumberText = fieldCardNumber.getText().toString();
        if (TextUtils.isEmpty(cardNumberText)) {
            containerCardNumber.setError(getString(R.string.validation_message_card_number));
            isValid = false;
        } else {
            containerCardNumber.setError("");
        }

        String cleanCardNumber = cardNumberText.replace(" ", "");

        if (cleanCardNumber.length() < 13 || !SdkUIFlowUtil.isValidCardNumber(cleanCardNumber)) {
            containerCardNumber.setError(getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            containerCardNumber.setError("");
        }

        return isValid;
    }

    private boolean checkCardExpiryValidity() {

        boolean isValid = true;
        String expiryDate = fieldCardExpiry.getText().toString().trim();
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
            containerCardExpiry.setError(getString(R.string.validation_message_empty_expiry_date));
            isValid = false;
        } else if (!expiryDate.contains("/")) {
            containerCardExpiry.setError(getString(R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else if (expDateArray.length != 2) {
            containerCardExpiry.setError(getString(R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else {
            try {
                expMonth = Integer.parseInt(expDateArray[0]);
            } catch (NumberFormatException e) {
                fieldCardExpiry.setText(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            }
            try {
                expYear = Integer.parseInt(expDateArray[1]);
            } catch (NumberFormatException e) {
                containerCardExpiry.setError(getString(R.string.validation_message_invalid_expiry_date));
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
                containerCardExpiry.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else if (expYear == currentYear && currentMonth > expMonth) {
                containerCardExpiry.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else {
                containerCardExpiry.setError("");
            }
        }

        return isValid;
    }


    private boolean checkCardCvvValidity() {
        boolean isValid = true;
        String cvv = fieldCardCvv.getText().toString().trim();
        if (TextUtils.isEmpty(cvv)) {
            containerCardCvv.setError(getString(R.string.validation_message_cvv));
            isValid = false;

        } else {
            if (cvv.length() < 3) {
                containerCardCvv.setError(getString(R.string.validation_message_invalid_cvv));
                isValid = false;
            } else {
                containerCardCvv.setError("");
            }
        }

        return isValid;
    }

    private void registerCardNumber() {

        if (isCardInfoValid()) {
            String cardNumber = fieldCardNumber.getText().toString();
            String cvv = fieldCardCvv.getText().toString();
            String date = fieldCardExpiry.getText().toString();
            String expiryMonth = date.split("/")[0].trim();
            String expiryYear = "20" + date.split("/")[1].trim();

            showProgressLayout(getString(R.string.processing_card_registration));
            presenter.register(cardNumber, cvv, expiryMonth, expiryYear);
        }
    }

    private void actionScanCard() {
        presenter.startScan(CardRegistrationActivity.this, UiKitConstants.INTENT_REQUEST_SCAN_CARD);
    }

    @Override
    public void bindViews() {

        fieldCardNumber = (TextInputEditText) findViewById(R.id.edit_card_number);
        fieldCardExpiry = (TextInputEditText) findViewById(R.id.edit_card_expiry);
        fieldCardCvv = (TextInputEditText) findViewById(R.id.edit_card_cvv);

        containerCardNumber = (TextInputLayout) findViewById(R.id.container_edit_card_number);
        containerCardExpiry = (TextInputLayout) findViewById(R.id.container_card_expiry);
        containerCardCvv = (TextInputLayout) findViewById(R.id.container_card_cvv);

        imageBankLogo = (ImageView) findViewById(R.id.image_bank_logo);
        imageCardLogo = (ImageView) findViewById(R.id.image_card_logo);

        buttonSaveCard = (FancyButton) findViewById(R.id.button_save_card);
        buttonScanCard = (FancyButton) findViewById(R.id.button_scan_card);

        textTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
    }

    @Override
    public void initTheme() {

        setBackgroundTintList(fieldCardNumber);
        setBackgroundTintList(fieldCardExpiry);
        setBackgroundTintList(fieldCardCvv);

        setPrimaryBackgroundColor(buttonSaveCard);
        setBorderColor(buttonScanCard);
        setTextColor(buttonScanCard);
        setIconColorFilter(buttonScanCard);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UiKitConstants.INTENT_REQUEST_SCAN_CARD) {
            if (data != null && data.hasExtra(ExternalScanner.EXTRA_SCAN_DATA)) {
                ScannerModel scanData = (ScannerModel) data.getSerializableExtra(ExternalScanner.EXTRA_SCAN_DATA);
                Log.d("scancard", String.format("Card Number: %s, Card Expire: %s/%d", scanData.getCardNumber(), scanData.getExpiredMonth() < 10 ? String.format("0%d", scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()), scanData.getExpiredYear() - 2000));
                updateScanCardData(scanData);
            }
        }
    }

    private void updateScanCardData(ScannerModel scanData) {
        if (scanData != null) {
            String cardNumber = Utils.getFormattedCreditCardNumber(Utils.getFormattedCreditCardNumber(scanData.getCardNumber()));
            String expDate = String.format("%s/%d", scanData.getExpiredMonth() < 10 ? String.format("0%d",
                    scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()),
                    scanData.getExpiredYear() - 2000);
            String cvv = scanData.getCvv();

            fieldCardCvv.setText(cvv);
            fieldCardExpiry.setText(expDate);
            fieldCardNumber.setText(cardNumber);
        }
    }

    private void finishRegistration(int resultCode) {
        setResult(resultCode);
        finish();
    }

    @Override
    public void onRegisterFailure(CardRegistrationResponse response, String reason) {
        Logger.d(TAG, "onRegisterFailure():" + reason);
        hideProgressLayout();
        SdkUIFlowUtil.showToast(this, getString(R.string.message_card_register_error));
        finishRegistration(RESULT_OK);
    }

    @Override
    public void onRegisterCardSuccess(CardRegistrationResponse response) {
        hideProgressLayout();
        SdkUIFlowUtil.showToast(this, getString(R.string.message_card_register_success));
        finishRegistration(RESULT_OK);
    }


    @Override
    public void onRegisterError(Throwable error) {
        Logger.d(TAG, "onRegisterError():" + error.getMessage());
        hideProgressLayout();
        SdkUIFlowUtil.showToast(this, getString(R.string.message_card_register_error));
    }

    @Override
    public void onCallbackUnImplemented() {
        Logger.d(TAG, "onCallbackUnImplemented()");

        hideProgressLayout();
        SdkUIFlowUtil.showToast(this, getString(R.string.callback_unimplemented));
        finishRegistration(RESULT_CANCELED);
    }

    public boolean isCardInfoValid() {
        return checkCardNumberValidity() && checkCardExpiryValidity() && checkCardCvvValidity();
    }
}
