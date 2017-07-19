package com.midtrans.sdk.uikit.views.creditcard.details;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
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

import com.midtrans.sdk.analytics.MixpanelAnalyticsManager;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BaseActivity;
import com.midtrans.sdk.uikit.activities.CreditCardFlowActivity;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 7/11/17.
 */

public class CreditCardDetailsActivity extends BaseActivity implements CreditCardDetailsView {

    private static final String TAG = CreditCardDetailsActivity.class.getSimpleName();

    public static final String EXTRA_CARD_DETAILS = "card.details";
    public static final String EXTRA_DELETED_CARD_DETAILS = "card.deleted.details";
    private static final int BANK_POINT_REQUEST_CODE = 1025;

    private Toolbar toolbar;

    private RecyclerView itemDetails;

    private AppCompatEditText cardNumberField;
    private AppCompatEditText cardCvvField;
    private AppCompatEditText cardExpiryField;

    private TextView cardNumberErrorText;
    private TextView cardCvvErrorText;
    private TextView cardExpiryErrorText;
    private TextView textInvalidPromoStatus;
    private TextView textInstallmentTerm;
    private TextView textTitleInstallment;

    private ImageView cardLogo;
    private ImageView bankLogo;
    private ImageView promoLogo;

    private ImageButton cvvHelpButton;
    private ImageButton saveCardHelpButton;
    private ImageButton pointHelpButton;

    private FancyButton scanCardButton;
    private FancyButton deleteCardButton;
    private FancyButton payNowButton;
    private FancyButton decreaseInstallmentButton;
    private FancyButton increaseInstallmentButton;

    private RelativeLayout containerSaveCard;
    private LinearLayout containerInstallment;
    private RelativeLayout containerPoint;

    private ProgressDialog progressDialog;

    private AppCompatCheckBox checkboxSaveCard;
    private AppCompatCheckBox checkboxPointEnabled;

    private CreditCardDetailsPresenter presenter;
    private String lastExpDate = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        initProperties();
        bindviews();
        initTheme();
        setupToolbarEvent();
        initCardNumber();
        setupInputCardExpiryEvent();
        setupInputCardCvvEvent();
        setupInstallmentEvent();
        setupBankPointEvent();
    }

    private void initProperties() {
        this.presenter = new CreditCardDetailsPresenter(this);

    }

    private void initTheme() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
//        try {
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getSecondaryColor() != 0) {

                cardNumberField.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));
                cardExpiryField.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));
                cardCvvField.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                textInstallmentTerm.setBackgroundColor(midtransSDK.getColorTheme().getSecondaryColor());
                textInstallmentTerm.getBackground().setAlpha(50);

                int[][] states = new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked},
                };

                int[] trackColors = new int[]{
                        Color.GRAY,
                        midtransSDK.getColorTheme().getSecondaryColor(),
                };
                checkboxSaveCard.setSupportButtonTintList(new ColorStateList(states, trackColors));
                checkboxPointEnabled.setSupportButtonTintList(new ColorStateList(states, trackColors));
            }

            if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                textInstallmentTerm.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                increaseInstallmentButton.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                decreaseInstallmentButton.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                saveCardHelpButton.setColorFilter(midtransSDK.getColorTheme().getPrimaryDarkColor(), PorterDuff.Mode.SRC_ATOP);
                cvvHelpButton.setColorFilter(midtransSDK.getColorTheme().getPrimaryDarkColor(), PorterDuff.Mode.SRC_ATOP);
                pointHelpButton.setColorFilter(midtransSDK.getColorTheme().getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);
                scanCardButton.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                scanCardButton.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                scanCardButton.setIconColorFilter(midtransSDK.getColorTheme().getPrimaryColor());
            }

            if (midtransSDK.getColorTheme().getPrimaryColor() != 0) {
                payNowButton.setBackgroundColor(midtransSDK.getColorTheme().getPrimaryColor());
            }
        }

        deleteCardButton.setBorderColor(ContextCompat.getColor(this, R.color.delete_color));
        deleteCardButton.setTextColor(ContextCompat.getColor(this, R.color.delete_color));
        deleteCardButton.setIconColorFilter(ContextCompat.getColor(this, R.color.delete_color));

//        } catch (Exception e) {
//            Log.e(TAG, "rendering theme:" + e.getMessage());
//        }
    }


    private void setupToolbarEvent() {
        setSupportActionBar(toolbar);
    }


    private void initCardNumber() {
        cardNumberField.addTextChangedListener(new TextWatcher() {
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
                cardNumberErrorText.setError(null);
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
                        cardExpiryField.requestFocus();
                    }
                } else if (s.length() == 19) {
                    if (checkCardNumberValidity()) {
                        cardExpiryField.requestFocus();
                    }
                }
            }
        });
    }


    private void setupInputCardExpiryEvent() {
        // text change event
        cardExpiryField.addTextChangedListener(new TextWatcher() {
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
                                cardExpiryField.setText(cardExpiryField.getText().toString().substring(0, 1));
                                cardExpiryField.setSelection(cardExpiryField.getText().toString().length());
                            } else {
                                cardExpiryField.setText("");
                                cardExpiryField.setSelection(cardExpiryField.getText().toString().length());
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
                                cardExpiryField.setText(getString(R.string.expiry_month_format, cardExpiry.getText().toString()));
                                cardExpiryField.setSelection(cardExpiryField.getText().toString().length());
                            } else {
                                cardExpiryField.setText(getString(R.string.expiry_month_int_format, Constants.MONTH_COUNT));
                                cardExpiryField.setSelection(cardExpiryField.getText().toString().length());
                            }

                        } catch (Exception exception) {
                            Logger.e(exception.toString());
                        }
                    }
                } else if (s.length() == 1) {
                    try {
                        int month = Integer.parseInt(input);
                        if (month > 1) {
                            cardExpiryField.setText(getString(R.string.expiry_month_single_digit_format, cardExpiry.getText().toString()));
                            cardExpiryField.setSelection(cardExpiryField.getText().toString().length());
                        }
                    } catch (Exception exception) {
                        Logger.e(exception.toString());
                    }
                }
                lastExpDate = cardExpiryField.getText().toString();

                // Move to next input
                if (s.length() == 7) {
                    cardCvvField.requestFocus();
                }
            }
        });

        // focus change listener
        cardNumberField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    checkCardNumberValidity();
                    checkBinLockingValidity();
                    initCardInstallment();
                    initBNIPoints(checkCardNumberValidity());
                }
            }
        });
    }

    private void setupInputCardCvvEvent() {

    }

    private void setupBankPointEvent() {

    }

    private void setupInstallmentEvent() {

    }


    private void bindviews() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        itemDetails = (RecyclerView) findViewById(R.id.container_item_details);

        cardNumberField = (AppCompatEditText) findViewById(R.id.field_card_number);
        cardCvvField = (AppCompatEditText) findViewById(R.id.field_cvv);
        cardExpiryField = (AppCompatEditText) findViewById(R.id.field_expiry);
        textInvalidPromoStatus = (TextView) findViewById(R.id.text_offer_status_not_applied);
        textInstallmentTerm = (TextView) findViewById(R.id.text_installment_term);
        textTitleInstallment = (TextView) findViewById(R.id.title_installment);

        cardNumberErrorText = (TextView) findViewById(R.id.error_message_card_number);
        cardExpiryErrorText = (TextView) findViewById(R.id.error_message_expiry);
        cardCvvErrorText = (TextView) findViewById(R.id.error_message_cvv);

        cardLogo = (ImageView) findViewById(R.id.payment_card_logo);
        bankLogo = (ImageView) findViewById(R.id.bank_logo);
        promoLogo = (ImageView) findViewById(R.id.promo_logo);

        cvvHelpButton = (ImageButton) findViewById(R.id.help_cvv_button);
        saveCardHelpButton = (ImageButton) findViewById(R.id.help_save_card);
        pointHelpButton = (ImageButton) findViewById(R.id.help_bank_point);

        scanCardButton = (FancyButton) findViewById(R.id.button_scan_card);
        deleteCardButton = (FancyButton) findViewById(R.id.button_delete);
        payNowButton = (FancyButton) findViewById(R.id.btn_pay_now);
        decreaseInstallmentButton = (FancyButton) findViewById(R.id.button_installment_decrease);
        increaseInstallmentButton = (FancyButton) findViewById(R.id.button_installment_increase);

        containerSaveCard = (RelativeLayout) findViewById(R.id.container_save_card_details);
        containerInstallment = (LinearLayout) findViewById(R.id.container_installment);
        containerPoint = (RelativeLayout) findViewById(R.id.container_bni_point);

        checkboxSaveCard = (AppCompatCheckBox) findViewById(R.id.checkbox_save_card);
        checkboxPointEnabled = (AppCompatCheckBox) findViewById(R.id.checkbox_point);
    }

    private void setBankType() {
        // Don't set card type when card number is empty
        String cardNumberText = cardNumber.getText().toString().trim();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 7) {
            bankLogo.setImageDrawable(null);
            return;
        }

        String cleanCardNumber = cardNumberText.replace(" ", "").substring(0, 6);
        String bank = getBankByBin(cleanCardNumber);

        if (bank != null) {
            switch (bank) {
                case BankType.BCA:
                    bankLogo.setImageResource(R.drawable.bca);
                    titleHeaderTextView.setText(R.string.card_details);
                    break;
                case BankType.BNI:
                    bankLogo.setImageResource(R.drawable.bni);
                    titleHeaderTextView.setText(R.string.card_details);
                    break;
                case BankType.BRI:
                    bankLogo.setImageResource(R.drawable.bri);
                    titleHeaderTextView.setText(R.string.card_details);
                    break;
                case BankType.CIMB:
                    bankLogo.setImageResource(R.drawable.cimb);
                    titleHeaderTextView.setText(R.string.card_details);
                    break;
                case BankType.MANDIRI:
                    bankLogo.setImageResource(R.drawable.mandiri);
                    titleHeaderTextView.setText(R.string.card_details);
                    if (isMandiriDebitCard(cleanCardNumber)) {
                        titleHeaderTextView.setText(R.string.mandiri_debit_card);
                    }
                    break;
                case BankType.MAYBANK:
                    bankLogo.setImageResource(R.drawable.maybank);
                    titleHeaderTextView.setText(R.string.card_details);
                    break;
                case BankType.BNI_DEBIT_ONLINE:
                    bankLogo.setImageResource(R.drawable.bni);
//                    titleHeaderTextView.setText(R.string.bni_debit_online_card);
                    break;
                default:
                    bankLogo.setImageDrawable(null);
//                    titleHeaderTextView.setText(R.string.card_details);
                    break;
            }
        } else {
            bankLogo.setImageDrawable(null);
//            titleHeaderTextView.setText(R.string.card_details);
        }
    }
//
//    public boolean isMandiriDebitCard(String cleanCardNumber) {
//        return creditCardTransaction.isMandiriCardDebit(cleanCardNumber);
//    }
//
//    public String getBankByBin(String cardBin) {
//        return creditCardTransaction.getBankByBin(cardBin);
//    }

    private void setCardType() {
        // Don't set card type when card number is empty
        String cardNumberText = cardNumber.getText().toString().trim();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 2) {
            cardLogo.setImageResource(0);
            return;
        }

        // Check card type before setting logo
        String cardType = Utils.getCardType(cardNumberText);
        switch (cardType) {
            case Utils.CARD_TYPE_VISA:
                cardLogo.setImageResource(R.drawable.ic_visa);
                break;
            case Utils.CARD_TYPE_MASTERCARD:
                cardLogo.setImageResource(R.drawable.ic_mastercard);
                break;
            case Utils.CARD_TYPE_JCB:
                cardLogo.setImageResource(R.drawable.ic_jcb);
                break;
            case Utils.CARD_TYPE_AMEX:
                cardLogo.setImageResource(R.drawable.ic_amex);
                break;
        }
    }

    private boolean checkCardNumberValidity() {
        if (isTwoClickMode()) {
            return true;
        }

        boolean isValid = true;

        String cardNumberText = cardNumberField.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(cardNumberText)) {
            cardNumberErrorText.setError(getString(R.string.validation_message_card_number));
            isValid = false;
        } else {
            cardNumberErrorText.setError(null);
        }

        if (cardNumberText.length() < 13 || !SdkUIFlowUtil.isValidCardNumber(cardNumberText)) {
            cardNumberErrorText.setError(getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            cardNumberErrorText.setError(null);
        }
        if (!isValid) {
            //track invalid cc number
            MidtransSDK.getInstance().trackEvent(AnalyticsEventName.CREDIT_CARD_NUMBER_VALIDATION, MixpanelAnalyticsManager.CARD_MODE_NORMAL);
        }
        return isValid;
    }
}
