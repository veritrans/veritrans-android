package com.midtrans.sdk.uikit.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.widgets.AspectRatioImageView;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 7/11/17.
 */

public class CreditCardActivity extends BaseActivity {

    public static final int SCAN_REQUEST_CODE = 101;
    private static final String TAG = CreditCardFlowActivity.class.getSimpleName();
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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card);
        bindviews();
        setupTolbarEvent();
        setupInputCardNumberEvent();
        setupInputCardExpiryEvent();
        setupInputCardCvvEvent();
        setupInstallmentEvent();
        setupBankPointEvent();
    }


    private void setupTolbarEvent() {

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

    }

    private void setupInputCardCvvEvent() {

    }

    private void setupBankPointEvent() {

    }

    private void setupInstallmentEvent() {

    }


    private void bindviews() {
        titleHeaderTextView = (DefaultTextView) findViewById(R.id.text_title);

        cardNumber = (TextInputEditText) findViewById(R.id.et_card_no);
        cardCvv = (TextInputEditText) findViewById(R.id.et_cvv);
        cardExpiry = (TextInputEditText) findViewById(R.id.et_exp_date);

        cardNumberContainer = (TextInputLayout) findViewById(R.id.card_number_container);
        cardExpiryContainer = (TextInputLayout) findViewById(R.id.exp_til);
        cardCvvNumberContainer = (TextInputLayout) findViewById(R.id.cvv_til);

        bankLogo = (ImageView) findViewById(R.id.bank_logo);
        cardLogo = (ImageView) findViewById(R.id.payment_card_logo);
        promoLogoBtn = (AspectRatioImageView) findViewById(R.id.promo_logo);

        saveCardCheckBox = (AppCompatCheckBox) findViewById(R.id.cb_store_card);
        cbBankPoint = (AppCompatCheckBox) findViewById(R.id.cb_bni_point);
        saveCardCheckBox = (AppCompatCheckBox) findViewById(R.id.cb_store_card);

        buttonIncrease = (FancyButton) findViewById(R.id.button_installment_increase);
        buttonDecrease = (FancyButton) findViewById(R.id.button_installment_decrease);
        scanCardBtn = (FancyButton) findViewById(R.id.scan_card);
        payNowBtn = (FancyButton) findViewById(R.id.btn_pay_now);

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

}
