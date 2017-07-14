package com.midtrans.sdk.uikit.views.creditcard.details;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
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
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BaseActivity;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.models.CreditCardTransaction;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.AspectRatioImageView;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 7/11/17.
 */

public class CreditCardDetailsActivity extends BaseActivity implements CreditCardDetailsView {

    public static final int SCAN_REQUEST_CODE = 101;
    private static final String TAG = CreditCardDetailsActivity.class.getSimpleName();
    private static final int PAYMENT_WEB_INTENT = 100;
    private static final int MAX_ATTEMPT = 2;
    private TransactionResponse transactionResponse;
    private String errorMessage;

    private Toolbar toolbar;
    private TextView titleHeaderTextView;
    private TextView textTotalAmount;
    private String discountToken;
    private TokenDetailsResponse tokenDetailsResponse;
    private boolean saveCard = false;
    private boolean isNewCard = true;
    private String maskedCardNumber;
    private String savedCardClickType;
    private int attempt = 0;
    private CardTokenRequest cardTokenRequest;
    private ArrayList<SaveCardRequest> creditCards = new ArrayList<>();
    private CreditCardTransaction creditCardTransaction = new CreditCardTransaction();
    private int installmentCurrentPosition, installmentTotalPositions;


    private TextInputLayout cardNumberContainer;
    private TextInputLayout cardExpiryContainer;
    private TextInputLayout cardCvvNumberContainer;
    private TextInputEditText cardNumber;
    private TextInputEditText cardExpiry;
    private TextInputEditText cardCvv;
    private ImageView cardLogo;
    private ImageView bankLogo;
    private ImageButton imageCvvHelp;
    private ImageButton imageSaveCardHelp;
    private ImageButton imagePointHelp;
    private FancyButton scanCardBtn;
    private FancyButton payNowBtn;
    private RelativeLayout layoutSaveCard;
    private AppCompatCheckBox saveCardCheckBox;
    private FancyButton buttonIncrease;
    private FancyButton buttonDecrease;
    private AspectRatioImageView promoLogoBtn;
    private FancyButton deleteCardBtn;
    private AppCompatCheckBox cbBankPoint;
    private RelativeLayout layoutBanksPoint;
    private TextView installmentText;
    private TextView textInstallmentTerm;
    private TextView textInvalidPromoStatus;
    private LinearLayout layoutInstallment;
    private CardTokenRequest savedCard;
    private String lastExpDate;
    private CreditCardDetailsPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        initProperties();
        bindviews();
        initTheme();
        setupToolbarEvent();
        setupInputCardNumberEvent();
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

                cardNumber.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));
                cardCvv.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));
                cardExpiry.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

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
                saveCardCheckBox.setSupportButtonTintList(new ColorStateList(states, trackColors));
                cbBankPoint.setSupportButtonTintList(new ColorStateList(states, trackColors));
            }

            if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                installmentText.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                buttonIncrease.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                buttonDecrease.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                imageSaveCardHelp.setColorFilter(midtransSDK.getColorTheme().getPrimaryDarkColor(), PorterDuff.Mode.SRC_ATOP);
                imageCvvHelp.setColorFilter(midtransSDK.getColorTheme().getPrimaryDarkColor(), PorterDuff.Mode.SRC_ATOP);
                imagePointHelp.setColorFilter(midtransSDK.getColorTheme().getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);
                scanCardBtn.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                scanCardBtn.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                scanCardBtn.setIconColorFilter(midtransSDK.getColorTheme().getPrimaryColor());
            }

            if (midtransSDK.getColorTheme().getPrimaryColor() != 0) {
                payNowBtn.setBackgroundColor(midtransSDK.getColorTheme().getPrimaryColor());
            }
        }

        deleteCardBtn.setBorderColor(ContextCompat.getColor(this, R.color.delete_color));
        deleteCardBtn.setTextColor(ContextCompat.getColor(this, R.color.delete_color));
        deleteCardBtn.setIconColorFilter(ContextCompat.getColor(this, R.color.delete_color));

//        } catch (Exception e) {
//            Log.e(TAG, "rendering theme:" + e.getMessage());
//        }
    }


    private void setupToolbarEvent() {
        setSupportActionBar(toolbar);
    }


    private void setupInputCardNumberEvent() {
        cardNumber.addTextChangedListener(new TextWatcher() {
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
                cardNumberContainer.setError(null);
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
                        cardExpiry.requestFocus();
                    }
                } else if (s.length() == 19) {
                    if (checkCardNumberValidity()) {
                        cardExpiry.requestFocus();
                    }
                }
            }
        });
    }


    private void setupInputCardExpiryEvent() {
        // text change event
        cardExpiry.addTextChangedListener(new TextWatcher() {
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
                                cardExpiry.setText(cardExpiry.getText().toString().substring(0, 1));
                                cardExpiry.setSelection(cardExpiry.getText().toString().length());
                            } else {
                                cardExpiry.setText("");
                                cardExpiry.setSelection(cardExpiry.getText().toString().length());
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
                                cardExpiry.setText(getString(R.string.expiry_month_format, cardExpiry.getText().toString()));
                                cardExpiry.setSelection(cardExpiry.getText().toString().length());
                            } else {
                                cardExpiry.setText(getString(R.string.expiry_month_int_format, Constants.MONTH_COUNT));
                                cardExpiry.setSelection(cardExpiry.getText().toString().length());
                            }

                        } catch (Exception exception) {
                            Logger.e(exception.toString());
                        }
                    }
                } else if (s.length() == 1) {
                    try {
                        int month = Integer.parseInt(input);
                        if (month > 1) {
                            cardExpiry.setText(getString(R.string.expiry_month_single_digit_format, cardExpiry.getText().toString()));
                            cardExpiry.setSelection(cardExpiry.getText().toString().length());
                        }
                    } catch (Exception exception) {
                        Logger.e(exception.toString());
                    }
                }
                lastExpDate = cardExpiry.getText().toString();

                // Move to next input
                if (s.length() == 7) {
                    cardCvv.requestFocus();
                }
            }
        });

        // focus change listener
        cardNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

        titleHeaderTextView = (DefaultTextView) findViewById(R.id.text_title);
        textInstallmentTerm = (TextView) findViewById(R.id.text_installment_term);
        installmentText = (TextView) findViewById(R.id.installment_title);

        cardNumber = (TextInputEditText) findViewById(R.id.et_card_no);
        cardCvv = (TextInputEditText) findViewById(R.id.et_cvv);
        cardExpiry = (TextInputEditText) findViewById(R.id.et_exp_date);

        cardNumberContainer = (TextInputLayout) findViewById(R.id.til_card_no);
        cardExpiryContainer = (TextInputLayout) findViewById(R.id.exp_til);
        cardCvvNumberContainer = (TextInputLayout) findViewById(R.id.cvv_til);

        bankLogo = (ImageView) findViewById(R.id.bank_logo);
        cardLogo = (ImageView) findViewById(R.id.payment_card_logo);
        promoLogoBtn = (AspectRatioImageView) findViewById(R.id.promo_logo);
        imageCvvHelp = (ImageButton) findViewById(R.id.image_cvv_help);
        imagePointHelp = (ImageButton) findViewById(R.id.image_bni_help);
        imageSaveCardHelp = (ImageButton) findViewById(R.id.help_save_card);

        saveCardCheckBox = (AppCompatCheckBox) findViewById(R.id.cb_store_card);
        cbBankPoint = (AppCompatCheckBox) findViewById(R.id.cb_bni_point);
        saveCardCheckBox = (AppCompatCheckBox) findViewById(R.id.cb_store_card);

        buttonIncrease = (FancyButton) findViewById(R.id.button_installment_increase);
        buttonDecrease = (FancyButton) findViewById(R.id.button_installment_decrease);
        scanCardBtn = (FancyButton) findViewById(R.id.scan_card);
        payNowBtn = (FancyButton) findViewById(R.id.btn_pay_now);
        deleteCardBtn = (FancyButton) findViewById(R.id.image_saved_card_delete);

        layoutInstallment = (LinearLayout) findViewById(R.id.layout_installment);
        layoutSaveCard = (RelativeLayout) findViewById(R.id.layout_save_card_detail);
        layoutBanksPoint = (RelativeLayout) findViewById(R.id.layout_bni_point);
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
                    titleHeaderTextView.setText(R.string.bni_debit_online_card);
                    break;
                default:
                    bankLogo.setImageDrawable(null);
                    titleHeaderTextView.setText(R.string.card_details);
                    break;
            }
        } else {
            bankLogo.setImageDrawable(null);
            titleHeaderTextView.setText(R.string.card_details);
        }
    }

    public boolean isMandiriDebitCard(String cleanCardNumber) {
        return creditCardTransaction.isMandiriCardDebit(cleanCardNumber);
    }

    public String getBankByBin(String cardBin) {
        return creditCardTransaction.getBankByBin(cardBin);
    }

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

        String cardNumberText = cardNumber.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(cardNumberText)) {
            cardNumberContainer.setError(getString(R.string.validation_message_card_number));
            isValid = false;
        } else {
            cardNumberContainer.setError(null);
        }

        if (cardNumberText.length() < 13 || !SdkUIFlowUtil.isValidCardNumber(cardNumberText)) {
            cardNumberContainer.setError(getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            cardNumberContainer.setError(null);
        }
        if (!isValid) {
            //track invalid cc number
            MidtransSDK.getInstance().trackEvent(AnalyticsEventName.CREDIT_CARD_NUMBER_VALIDATION, MixpanelAnalyticsManager.CARD_MODE_NORMAL);
        }
        return isValid;
    }

    public boolean isTwoClickMode() {
        return savedCard != null;
    }

    private boolean checkBinLockingValidity() {
        if (isWhiteListBinsAvailable()) {
            if (!isCardBinLockingValid()) {
                return false;
            }
        }
        return true;
    }

    public boolean isWhiteListBinsAvailable() {
        return creditCardTransaction.isWhiteListBinsAvailable();
    }

    private boolean isCardBinLockingValid() {
        boolean valid = false;
        String cardNumberText = cardNumber.getText().toString().trim();
        if (TextUtils.isEmpty(cardNumberText)) {
            showInvalidBinLockingStatus(true);

        } else if (cardNumberText.length() < 7) {
            showInvalidBinLockingStatus(true);
        } else {
            if (isCardBinValid()) {
                showInvalidBinLockingStatus(false);
                valid = true;
            } else {
                showInvalidBinLockingStatus(true);
            }
        }

        return valid;
    }

    private void showInvalidBinLockingStatus(boolean show) {
        if (show) {
            textInvalidPromoStatus.setVisibility(View.VISIBLE);
        } else {
            textInvalidPromoStatus.setVisibility(View.GONE);
        }
    }

//    private void initCreditCard() {
//        MidtransSDK midtransSDK = MidtransSDK.getInstance();
//
//        creditCardTransaction.setProperties(midtransSDK.getCreditCard(), SdkUIFlowUtil.getBankBins(this));
//
//        initBankBins();
//
//        if (isClickPayment()) {
//            getCreditCards();
//
//        } else {
//            showAddCardDetailFragment();
//
//        }
//        setTextTotalAmount(MidtransSDK.getInstance().getTransactionRequest().getAmount());
//    }

    private boolean isCardBinValid() {
        if (isWhiteListBinsAvailable()) {
            String cardNumberText = cardNumber.getText().toString();
            String cardBin = cardNumberText.replace(" ", "").substring(0, 6);
            if (!creditCardTransaction.isInWhiteList(cardBin)) {
                return false;
            }
        }
        return true;
    }

    private void initCardInstallment() {
        if (isInstallmentAvailable()) {
            String cardNumberText = cardNumber.getText().toString();
            if (TextUtils.isEmpty(cardNumberText)) {
                showInstallmentLayout(false);
            } else if (cardNumber.length() < 7) {
                showInstallmentLayout(false);
            } else if (MidtransSDK.getInstance().getCreditCard() == null) {
                showInstallmentLayout(false);
            } else {
                String cleanCardNumber = cardNumber.getText().toString().trim().replace(" ", "").substring(0, 6);
                ArrayList<Integer> installmentTerms = getInstallmentTerms(cleanCardNumber);

                if (installmentTerms != null && installmentTerms.size() > 1) {
                    installmentCurrentPosition = 0;
                    installmentTotalPositions = installmentTerms.size() - 1;
                    setInstallmentTerm();
                    showInstallmentLayout(true);
                } else {
                    showInstallmentLayout(false);
                }
            }
        }

    }

    public boolean isInstallmentAvailable() {
        return creditCardTransaction.isInstallmentAvailable();
    }

    private void showInstallmentLayout(boolean show) {
        if (show) {
            if (layoutInstallment.getVisibility() == View.GONE) {
                layoutInstallment.setVisibility(View.VISIBLE);
            }
            buttonDecrease.setEnabled(false);
            buttonIncrease.setEnabled(true);
        } else {
            if (layoutInstallment.getVisibility() == View.VISIBLE) {
                layoutInstallment.setVisibility(View.GONE);
            }
            setInstallment(0);
        }
    }

    public void setInstallment(int position) {
        this.creditCardTransaction.setInstallment(position);
    }

    public ArrayList<Integer> getInstallmentTerms(String cleanCardNumber) {
        return creditCardTransaction.getInstallmentTerms(cleanCardNumber);
    }

    private void initBNIPoints(boolean validCardNumber) {

        ArrayList<String> pointBanks = MidtransSDK.getInstance().getBanksPointEnabled();
        if (validCardNumber && pointBanks != null && !pointBanks.isEmpty()) {
            String cardNumberText = cardNumber.getText().toString();
            String cardBin = cardNumberText.replace(" ", "").substring(0, 6);
            String bankBin = getBankByBin(cardBin);
            if (!TextUtils.isEmpty(bankBin) && bankBin.equals(BankType.BNI)) {
                showBanksPoint(true);
            } else {
                showBanksPoint(false);
            }
        } else {
            showBanksPoint(false);
        }
    }

    private void showBanksPoint(boolean show) {
        if (show) {
            layoutBanksPoint.setVisibility(View.VISIBLE);
        } else {
            layoutBanksPoint.setVisibility(View.GONE);
        }
    }

    private void setInstallmentTerm() {
        String installmentTerm;
        int term = getInstallmentTerm(installmentCurrentPosition);
        if (term < 1) {
            installmentTerm = getString(R.string.no_installment);
        } else {
            installmentTerm = getString(R.string.formatted_installment_month, term + "");
        }
        textInstallmentTerm.setText(installmentTerm);
    }

    public int getInstallmentTerm(int installmentCurrentPosition) {
        return creditCardTransaction.getInstallmentTerm(installmentCurrentPosition);
    }
}
