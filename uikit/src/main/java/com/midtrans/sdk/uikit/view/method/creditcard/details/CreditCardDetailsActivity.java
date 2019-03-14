package com.midtrans.sdk.uikit.view.method.creditcard.details;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.base.enums.BankType;
import com.midtrans.sdk.corekit.base.enums.Currency;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.mandatory.TransactionDetails;
import com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails.TokenDetailsResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CreditCardResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.Promo;
import com.midtrans.sdk.corekit.core.api.snap.model.point.PointResponse;
import com.midtrans.sdk.corekit.utilities.Helper;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.MidtransKit;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.base.enums.CreditCardFlow;
import com.midtrans.sdk.uikit.base.model.MessageInfo;
import com.midtrans.sdk.uikit.base.model.PaymentResponse;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.utilities.NetworkHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.view.method.creditcard.details.adapter.PromosAdapter;
import com.midtrans.sdk.uikit.view.method.creditcard.point.BankPointsActivity;
import com.midtrans.sdk.uikit.view.method.creditcard.tnc.TermsAndConditionsActivity;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CreditCardDetailsActivity extends BasePaymentActivity implements CreditCardDetailsContract {

    public static final String EXTRA_DELETED_CARD_DETAILS = "card.deleted.details";
    public static final String EXTRA_SAVED_CARD = "extra.card.saved";

    private static final String TAG = CreditCardDetailsActivity.class.getSimpleName();
    private AppCompatEditText fieldCardNumber;
    private AppCompatEditText fieldCardCvv;
    private AppCompatEditText fieldCardExpiry;
    private AppCompatEditText fieldEmail;
    private AppCompatEditText fieldPhone;

    private DefaultTextView textCardNumberHint;
    private DefaultTextView textExpriyHint;
    private DefaultTextView textCvvHint;
    private DefaultTextView textInApplicablePromoStatus;
    private DefaultTextView textEmailHint;
    private DefaultTextView textPhoneHint;

    private TextView textCardNumberError;
    private TextView textCardCvvError;
    private TextView textCardExpiryError;
    private TextView textInstallmentTerm;
    private TextView textTitleInstallment;
    private TextView textEmailError;
    private TextView textPhoneError;

    private ImageView imageCardLogo;
    private ImageView imageBankLogo;
    private ImageView ccBadge;

    private ImageButton buttonCvvHelp;
    private ImageButton buttonSaveCardHelp;
    private ImageButton buttonPointHelp;

    private FancyButton buttonScanCard;
    private FancyButton buttonDeleteCard;
    private FancyButton buttonDecreaseInstallment;
    private FancyButton buttonIncreaseInstallment;

    private RelativeLayout containerSaveCard;
    private LinearLayout containerInstallment;
    private RelativeLayout containerPoint;
    private LinearLayout containerUserDetail;

    private AppCompatCheckBox checkboxSaveCard;
    private AppCompatCheckBox checkboxPointEnabled;
    private RecyclerView recyclerViewPromo;

    private CreditCardDetailsPresenter presenter;
    private PromosAdapter promosAdapter;
    private SaveCardRequest savedCard;
    private String lastExpDate = "";
    private String redirectUrl = null;
    private float redeemedPoint = 0f;
    private int attempt = 0;
    private boolean isEmailShown = false;

    private CreditCardResponse creditCardResponse;

    private boolean isAlreadyGotResponse = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_credit_card_details);
        initProperties();
        initCardNumber();
        initCardExpiry();
        initCardCvv();
        initHelpButtons();
        initInstallmentButton();
        initDeleteButton();
        initPayOrCompleteButton();
        initCheckBox();
        initPromoList();
        initLayoutState();
        initUserDetail();
        bindData();
    }

    private void initCardNumber() {
        fieldCardNumber.addTextChangedListener(new TextWatcher() {
            private static final char SPACE_CHAR = ' ';

            public boolean deleteAction;
            private int lastPosition;
            private int currentPosition = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastPosition = start;
                deleteAction = count == 0;
            }

            @Override
            public void afterTextChanged(Editable s) {
                textCardNumberError.setError(null);
                try {
                    String cleanCardNumber = s.toString().replaceAll("[\\s-]+", "");
                    String cardNumber = formatCard(cleanCardNumber);


                    if (s.length() > 0) {
                        if (deleteAction) {
                            if (s.charAt(lastPosition - 1) == SPACE_CHAR) {
                                currentPosition = lastPosition - 1;
                            } else {
                                currentPosition = lastPosition;
                            }
                        } else {
                            if (s.charAt(lastPosition) == SPACE_CHAR) {
                                s.delete(lastPosition - 1, lastPosition);
                            }

                            if (cardNumber.charAt(lastPosition) == SPACE_CHAR) {
                                currentPosition = lastPosition + 2;
                            } else {
                                currentPosition = lastPosition + 1;
                            }
                        }
                    } else {
                        if (textInApplicablePromoStatus != null) {
                            showInApplicablePromo(false);
                        }
                    }

                    String cardType = Helper.getCardType(s.toString());

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

                    fieldCardNumber.removeTextChangedListener(this);

                    fieldCardNumber.setText(cardNumber);
                    if (!TextUtils.isEmpty(cardNumber)) {
                        fieldCardNumber.setSelection(currentPosition);
                    }

                    fieldCardNumber.addTextChangedListener(this);

                    setCardType();
                    setBankType();

                    initCreditCardPromos(false);

                } catch (RuntimeException e) {
                    Logger.error(TAG, "inputCcNumber:" + e.getMessage());
                }
            }
        });

        // focus change listener
        fieldCardNumber.setOnFocusChangeListener((view, hasfocus) -> {
            if (!hasfocus) {
                checkCardNumberValidity();
                checkBinLockingValidity();
                checkInstallment();
                checkBankPoint();
            }
        });
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
                                Logger.error(exception.toString());
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
                                    fieldCardExpiry.setText(getString(R.string.expiry_month_int_format, Constants.MONTH_COUNT));
                                    fieldCardExpiry.setSelection(fieldCardExpiry.getText().toString().length());
                                }

                            } catch (Exception exception) {
                                Logger.error(exception.toString());
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
                            Logger.error(exception.toString());
                        }
                    }
                } catch (RuntimeException e) {
                    Logger.error(TAG, "ccexpiry:" + e.getMessage());
                }

                lastExpDate = fieldCardExpiry.getText().toString();

                // Move to next input
                if (s.length() == 7) {
                    fieldCardCvv.requestFocus();
                }
            }
        });

        fieldCardExpiry.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                checkCardExpiryValidity();
            }
        });

    }

    private void initCardCvv() {
        fieldCardCvv.setOnFocusChangeListener((view, focused) -> {
            if (!focused) {
                checkCardCvvValidity();
            }
        });
    }

    private void initHelpButtons() {
        buttonCvvHelp.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(CreditCardDetailsActivity.this)
                    .setTitle(R.string.what_is_cvv)
                    .setView(R.layout.layout_dialog_cvv)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();
            alertDialog.show();
            changeDialogButtonColor(alertDialog);
        });

        buttonSaveCardHelp.setOnClickListener(v -> {
            // Show help dialog
            AlertDialog alertDialog = new AlertDialog.Builder(CreditCardDetailsActivity.this)
                    .setTitle(R.string.save_card_message)
                    .setMessage(R.string.save_card_dialog)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();
            alertDialog.show();
            changeDialogButtonColor(alertDialog);
        });
    }

    private void initInstallmentButton() {
        buttonIncreaseInstallment.setOnClickListener(view -> {
            int installmentTermCurrentPosition = presenter.getInstallmentCurrentPosition();
            int installmentTermCount = presenter.getInstallmentTotalPositions();
            if (installmentTermCurrentPosition >= 0 && installmentTermCurrentPosition < installmentTermCount) {
                presenter.setCurrentInstallment(installmentTermCurrentPosition + 1);
                setCurrentInstallmentTerm();
            }
            changeBankPointVisibility();
            disableEnableInstallmentButton();
        });

        buttonDecreaseInstallment.setOnClickListener(view -> {
            int installmentTermCurrentPosition = presenter.getInstallmentCurrentPosition();
            int installmentTermCount = presenter.getInstallmentTotalPositions();
            if (installmentTermCurrentPosition > 0 && installmentTermCurrentPosition <= installmentTermCount) {
                presenter.setCurrentInstallment(installmentTermCurrentPosition - 1);
                setCurrentInstallmentTerm();
            }
            changeBankPointVisibility();
            disableEnableInstallmentButton();
        });
    }

    private void initDeleteButton() {
        buttonDeleteCard.setOnClickListener(v -> showCardDeletionConfirmation());
    }

    private void initPayOrCompleteButton() {
        buttonCompletePayment.setOnClickListener(v -> {
            com.midtrans.sdk.uikit.utilities.Helper.hideKeyboard(this);
            if (checkCardValidity() && checkUserDataValidity()) {
                if (checkPaymentValidity()) {
                    setPromoSelected();
                    startTokenizeCreditCard();
                }
            }
        });
        buttonCompletePayment.setText(getString(R.string.pay_now));
        buttonCompletePayment.setTextBold();
        setTitle(getString(R.string.card_details));
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCheckBox() {
        checkboxPointEnabled.setOnTouchListener((v, event) -> {
            if (!checkboxPointEnabled.isChecked() && presenter.isBniPointAvailable(getCardNumberBin())) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Intent intent = new Intent(CreditCardDetailsActivity.this, TermsAndConditionsActivity.class);
                    startActivityForResult(intent, TermsAndConditionsActivity.INTENT_TNC);
                }
                return true;
            }
            return false;
        });

        checkboxPointEnabled.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                showInstallmentLayout(false);
            } else {
                checkInstallment();
            }
        });
    }

    private void initPromoList() {
        TransactionDetails transactionDetails = paymentInfoResponse.getTransactionDetails();
        String currency = Currency.IDR;

        if (transactionDetails != null) {
            currency = transactionDetails.getCurrency();
        }

        promosAdapter = new PromosAdapter(
                getPrimaryColor(),
                currency,
                new PromosAdapter.PromoListener() {
                    @Override
                    public void onPromoCheckedChanged(Promo promo) {
                        updateItemDetails(promo);
                    }

                    @Override
                    public void onPromoUnavailable() {
                        updateItemDetails(null);
                    }
                }
        );

        recyclerViewPromo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPromo.setHasFixedSize(true);
        recyclerViewPromo.setAdapter(promosAdapter);

        initCreditCardPromos(true);
    }

    private void initLayoutState() {
        if (presenter.isSavedCardEnabled()) {
            showSavedCardLayout(true);
            if (presenter.isSaveCardOptionChecked()) {
                checkboxSaveCard.setChecked(true);
            }
        }

    }

    private void initUserDetail() {
        if (presenter.isShowEmailForm()) {
            isEmailShown = true;
            fieldEmail.setText(presenter.getUserEmail());
            fieldEmail.clearFocus();
            fieldEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String email = s.toString();
                    if (TextUtils.isEmpty(email) || presenter.isEmailValid(email)) {
                        textEmailError.setVisibility(View.GONE);
                    } else {
                        textEmailError.setVisibility(View.VISIBLE);
                    }
                }
            });
            fieldPhone.setText(presenter.getUserPhone());
            fieldPhone.clearFocus();
            fieldPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String phone = s.toString();
                    if (TextUtils.isEmpty(phone) || phone.length() > 5) {
                        textPhoneError.setVisibility(View.GONE);
                    } else {
                        textPhoneError.setVisibility(View.VISIBLE);
                    }
                }
            });
            fieldPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        fieldPhone.setSelection(fieldPhone.getEditableText().toString().length());
                    }
                }
            });
            containerUserDetail.setVisibility(View.VISIBLE);
        }
    }

    private void bindData() {
        if (savedCard != null) {
            showDeleteIcon();

            String cardType = Helper.getCardType(savedCard.getMaskedCard());
            if (!TextUtils.isEmpty(cardType)) {
                try {
                    String cardBin = savedCard.getMaskedCard().substring(0, 4);
                    String title = cardType + "-" + cardBin;
                    setTitle(title);
                } catch (RuntimeException e) {
                    Logger.error(TAG, "cardType:" + e.getMessage());
                }
            }

            showSavedCardLayout(false);

            fieldCardExpiry.setInputType(InputType.TYPE_CLASS_TEXT);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(20);
            fieldCardExpiry.setFilters(filterArray);
            fieldCardExpiry.setEnabled(false);
            fieldCardExpiry.setText(com.midtrans.sdk.uikit.utilities.Helper.getMaskedExpDate());

            fieldCardNumber.setEnabled(false);
            fieldCardNumber.setText(com.midtrans.sdk.uikit.utilities.Helper.getMaskedCardNumber(savedCard.getMaskedCard()));

            fieldCardCvv.requestFocus();

            if (isOneClickMode()) {
                fieldCardCvv.setInputType(InputType.TYPE_CLASS_TEXT);
                fieldCardCvv.setFilters(filterArray);
                fieldCardCvv.setText(com.midtrans.sdk.uikit.utilities.Helper.getMaskedCardCvv());
                fieldCardCvv.setEnabled(false);
            } else {
                checkInstallment();
                checkBankPoint();
                checkBinLockingValidity();
            }

            setBankType();
            setCardType();
        }

        int badgeCode = presenter.getCcBadge();
        switch (badgeCode) {
            case 1:
                ccBadge.setImageResource(R.drawable.badge_full);
                break;
            case 3:
                ccBadge.setImageResource(R.drawable.badge_jcb);
                break;
            case 4:
                ccBadge.setImageResource(R.drawable.badge_amex);
                break;
            default:
                ccBadge.setImageResource(R.drawable.badge_default);

        }
    }

    private void initCreditCardPromos(final boolean firstTime) {
        final String cardNumber = getCleanedCardNumber();
        if (cardNumber.length() < 7) {
            recyclerViewPromo.postDelayed(() -> promosAdapter.setData(presenter.getCreditCardPromos(cardNumber, firstTime)), 100);
        }
    }

    private void initBankPointHelp(final String bankName) {
        buttonPointHelp.setOnClickListener(v -> {
            int titleId = 0, detailId = 0;

            if (bankName.equalsIgnoreCase(BankType.BNI)) {
                titleId = R.string.redeem_bni_title;
                detailId = R.string.redeem_bni_details;
            } else if (bankName.equalsIgnoreCase(BankType.MANDIRI)) {
                titleId = R.string.redeem_mandiri_title;
                detailId = R.string.redeem_mandiri_details;
            }

            AlertDialog alertDialog = new AlertDialog.Builder(CreditCardDetailsActivity.this)
                    .setTitle(titleId)
                    .setMessage(detailId)
                    .setPositiveButton(R.string.ok, (dialogInterface, i) -> dialogInterface.dismiss())
                    .create();
            alertDialog.show();
            changeDialogButtonColor(alertDialog);
        });
    }

    /**
     * Layout Manipulation
     */

    private void changeDialogButtonColor(AlertDialog alertDialog) {
        try {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setTextColor(positiveButton);
        } catch (Exception e) {
            Logger.debug(TAG, "RenderThemeError:" + e.getMessage());
        }
    }

    private void showCardDeletionConfirmation() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.card_delete_message)
                .setPositiveButton(R.string.text_yes, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    showProgressLayout(getString(R.string.processing_delete));
                    presenter.deleteSavedCard(savedCard);
                })
                .setNegativeButton(R.string.text_no, (dialogInterface, i) -> dialogInterface.dismiss())
                .create();
        alertDialog.show();
    }

    private void showDeleteIcon() {
        buttonDeleteCard.setVisibility(View.VISIBLE);
        findViewById(R.id.button_separator).setVisibility(View.VISIBLE);
    }

    private void showSavedCardLayout(boolean show) {
        if (show) {
            containerSaveCard.setVisibility(View.VISIBLE);
        } else {
            checkboxSaveCard.setChecked(false);
            containerSaveCard.setVisibility(View.GONE);
        }
    }

    private void showInApplicablePromo(boolean show) {
        if (show) {
            textInApplicablePromoStatus.setVisibility(View.VISIBLE);
        } else {
            textInApplicablePromoStatus.setVisibility(View.GONE);
        }
    }

    private void showInstallmentLayout(boolean show) {
        if (show) {
            containerInstallment.setVisibility(View.VISIBLE);
            resetInstallmentButtons();
        } else {
            containerInstallment.setVisibility(View.GONE);
            presenter.setCurrentInstallment(0);
        }
    }

    private void changeBankPointVisibility() {
        if (presenter.getInstallmentCurrentPosition() == 0) {
            checkBankPoint();
        } else {
            showBankPointLayout("", false);
        }
    }

    private void disableEnableInstallmentButton() {
        int installmentTermCurrentPosition = presenter.getInstallmentCurrentPosition();
        int installmentTermCount = presenter.getInstallmentTotalPositions();
        if (installmentTermCurrentPosition == 0
                && installmentTermCount == 0) {
            buttonDecreaseInstallment.setEnabled(false);
            buttonIncreaseInstallment.setEnabled(false);
        } else if (installmentTermCurrentPosition > 0
                && installmentTermCurrentPosition < installmentTermCount) {
            buttonDecreaseInstallment.setEnabled(true);
            buttonIncreaseInstallment.setEnabled(true);
        } else if (installmentTermCurrentPosition > 0
                && installmentTermCurrentPosition == installmentTermCount) {
            buttonDecreaseInstallment.setEnabled(true);
            buttonIncreaseInstallment.setEnabled(false);
        } else if (installmentTermCurrentPosition == 0
                && installmentTermCurrentPosition < installmentTermCount) {
            buttonDecreaseInstallment.setEnabled(false);
            buttonIncreaseInstallment.setEnabled(true);
        }
    }

    private void setCheckboxPoint(boolean check) {
        if (check) {
            checkboxPointEnabled.setChecked(true);
        } else {
            checkboxPointEnabled.setChecked(false);
        }

    }

    /**
     * Validation and checker
     */
    private boolean checkPaymentValidity() {
        // Card bin validation for bin locking and installment
        String cardNumber = getCleanedCardNumber();
        if (presenter.isCardBinBlocked(cardNumber)) {
            MessageHelper.showToast(this, getString(R.string.offer_not_applied));
            return false;
        }

        // Installment validation
        if (!presenter.isInstallmentValid()) {
            MessageHelper.showToast(this, getString(R.string.installment_required));
            return false;
        }

        return true;
    }

    private boolean checkCardValidity() {
        boolean cardNumberValidity = checkCardNumberValidity();
        boolean cardExpiryValidity = checkCardExpiryValidity();
        boolean cardCVVValidity = checkCardCvvValidity();
        return cardNumberValidity && cardExpiryValidity && cardCVVValidity;
    }

    private boolean checkUserDataValidity() {
        if (isEmailShown) {
            boolean emailValidity = presenter.isEmailValid(fieldEmail.getEditableText().toString());
            String phoneNumber = fieldPhone.getEditableText().toString();
            boolean phoneValidity = TextUtils.isEmpty(phoneNumber) || phoneNumber.length() > 5;
            return emailValidity && phoneValidity;
        }
        return true;
    }

    private boolean checkCardExpiryValidity() {
        if (isTwoClicksMode()) {
            return true;
        }

        boolean isValid = true;
        String expiryDate = fieldCardExpiry.getText().toString().trim();
        String[] expDateArray = new String[2];
        int expMonth = 0;
        int expYear = 0;
        try {
            expDateArray = expiryDate.split("/");
            expDateArray[0] = expDateArray[0].trim();
            expDateArray[1] = expDateArray[1].trim();
            Logger.debug(TAG, "expDate:" + expDateArray[0].trim() + expDateArray[1].trim());
        } catch (NullPointerException e) {
            Logger.debug(TAG, "expiry date empty");
        } catch (IndexOutOfBoundsException e) {
            Logger.debug(TAG, "expiry date issue");
        }

        if (TextUtils.isEmpty(expiryDate)) {
            showValidationError(textCardExpiryError, getString(R.string.validation_message_empty_expiry_date));
            isValid = false;
        } else if (!expiryDate.contains("/")) {
            showValidationError(textCardExpiryError, getString(R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else if (expDateArray.length != 2) {
            showValidationError(textCardExpiryError, getString(R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else {
            try {
                expMonth = Integer.parseInt(expDateArray[0]);
            } catch (NumberFormatException e) {
                showValidationError(textCardExpiryError, getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            }
            try {
                expYear = Integer.parseInt(expDateArray[1]);
            } catch (NumberFormatException e) {
                showValidationError(textCardExpiryError, getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            }
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yy");
            String year = format.format(date);

            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int currentYear = Integer.parseInt(year);
            Logger.debug(TAG, "currentMonth:" + currentMonth + ",currentYear:" + currentYear);

            if (expYear < currentYear) {
                showValidationError(textCardExpiryError, getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else if (expYear == currentYear && currentMonth > expMonth) {
                showValidationError(textCardExpiryError, getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else if (expYear > (currentYear + 10)) {
                showValidationError(textCardExpiryError, getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else {
                hideValidationError(textCardExpiryError);
            }
        }
        return isValid;
    }

    private boolean checkCardCvvValidity() {
        if (isOneClickMode()) {
            return true;
        }
        boolean isValid = true;
        String cvv = fieldCardCvv.getText().toString().trim();
        if (TextUtils.isEmpty(cvv)) {
            showValidationError(textCardCvvError, getString(R.string.validation_message_cvv));
            isValid = false;

        } else {
            if (cvv.length() < 3) {
                showValidationError(textCardCvvError, getString(R.string.validation_message_invalid_cvv));
                isValid = false;
            } else {
                hideValidationError(textCardCvvError);
            }
        }
        return isValid;
    }

    private boolean isOneClickMode() {
        return savedCard != null && savedCard.getType() != null && savedCard.getType().equals(CreditCardFlow.ONE_CLICK);
    }

    private boolean isTwoClicksMode() {
        return savedCard != null;
    }

    private boolean checkCardNumberValidity() {
        if (isTwoClicksMode()) {
            return true;
        }

        boolean isValid = true;

        String cardNumberText = getCleanedCardNumber();
        if (TextUtils.isEmpty(cardNumberText)) {
            showValidationError(textCardNumberError, getString(R.string.validation_message_card_number));
            isValid = false;
        } else {
            hideValidationError(textCardNumberError);
        }

        if (cardNumberText.length() < 13 || !com.midtrans.sdk.uikit.utilities.Helper.isValidCardNumber(cardNumberText)) {
            showValidationError(textCardNumberError, getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            hideValidationError(textCardNumberError);
        }
        return isValid;
    }

    private void checkBinLockingValidity() {
        String cardNumber = getCleanedCardNumber();

        if (presenter.isCardBinBlocked(cardNumber)) {
            showInApplicablePromo(true);
        } else {
            showInApplicablePromo(false);
        }
    }

    private void checkInstallment() {
        String cardNumber = getCleanedCardNumber();
        if (presenter.isInstallmentAvailable()
                && presenter.isCardBinValidForBankChecking(cardNumber)) {
            String cardBin = getCardNumberBin();
            ArrayList<Integer> installmentTerms = presenter.getInstallmentTermsByCardBin(cardBin);

            if (installmentTerms != null && installmentTerms.size() > 1) {
                setInstallmentTerms(installmentTerms);
                setCurrentInstallmentTerm();
                showInstallmentLayout(true);
            } else {
                showInstallmentLayout(false);
            }
        } else {
            showInstallmentLayout(false);
        }
    }

    private void checkBankPoint() {
        String cardBin = getCardNumberBin();
        if (!TextUtils.isEmpty(cardBin)) {
            if (presenter.isBniPointAvailable(cardBin)) {
                showBankPointLayout(BankType.BNI, true);
            } else if (presenter.isMandiriPointAvailable(cardBin)) {
                showBankPointLayout(BankType.MANDIRI, true);
            } else {
                showBankPointLayout("", false);
            }
        } else {
            showBankPointLayout("", false);
        }
    }

    private void showBankPointLayout(String bankName, boolean show) {
        if (show) {
            initBankPointHelp(bankName);
            switch (bankName) {
                case BankType.BNI:
                    checkboxPointEnabled.setText(getString(R.string.redeem_bni_reward));
                    break;
                case BankType.MANDIRI:
                    checkboxPointEnabled.setText(getString(R.string.redeem_mandiri_point));
                    break;
            }
            containerPoint.setVisibility(View.VISIBLE);
        } else {
            checkboxPointEnabled.setChecked(false);
            containerPoint.setVisibility(View.GONE);
        }
    }

    /**
     * Begin set and get value function
     */

    private void setCallbackOrSendToStatusPage() {
        if (presenter.isShowPaymentStatusPage()) {
            startResultActivity(Constants.INTENT_CODE_PAYMENT_RESULT, PaymentListHelper.convertTransactionStatus(creditCardResponse));
        } else {
            finishPayment(RESULT_OK, creditCardResponse);
        }
    }

    private void setCallbackOrSendFailedToStatusPage() {
        if (presenter.isShowPaymentStatusPage()) {
            startResultActivity(
                    Constants.INTENT_CODE_PAYMENT_RESULT,
                    PaymentListHelper.newErrorPaymentResponse(
                            PaymentType.CREDIT_CARD,
                            creditCardResponse.getStatusCode(),
                            paymentInfoResponse.getTransactionDetails().getOrderId(),
                            Double.toString(paymentInfoResponse.getTransactionDetails().getGrossAmount())
                    )
            );
        } else {
            finishPayment(RESULT_OK, creditCardResponse);
        }
    }

    private void updateItemDetails(Promo promo) {
        if (detailAdapter != null) {
            if (promo != null && promo.isSelected()) {
                addNewItemDetails(presenter.createTransactionItem(promo));
            } else {
                removeItemDetails(Constants.PROMO_ID);
            }
        }
    }

    private void resetInstallmentButtons() {
        buttonDecreaseInstallment.setEnabled(false);
        buttonIncreaseInstallment.setEnabled(true);
    }

    private void setCurrentInstallmentTerm() {
        String installmentTerm;
        int term = presenter.getCurrentInstallmentTerm();
        if (term < 1) {
            installmentTerm = getString(R.string.no_installment);
        } else {
            installmentTerm = getString(R.string.formatted_installment_month, String.valueOf(term));
        }
        textInstallmentTerm.setText(installmentTerm);
    }

    private void setInstallmentTerms(ArrayList<Integer> installmentTerms) {
        presenter.initInstallmentTerms(installmentTerms);
    }

    private void setPromoSelected() {
        if (promosAdapter != null) {
            presenter.setSelectedPromo(promosAdapter.getSeletedPromo());
        }
    }

    private void setBankType() {
        // Don't set card type when card number is empty
        String cardNumberText = getCardNumberValue();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 7) {
            imageBankLogo.setImageDrawable(null);
            setTitle(R.string.card_details);
            return;
        }

        String cleanCardNumber = getCardNumberBin();
        String bank = presenter.getBankByCardBin(cleanCardNumber);
        setTitle(R.string.card_details);

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
                case BankType.MANDIRI_DEBIT:
                    imageBankLogo.setImageResource(R.drawable.mandiri);
                    setTitle(getString(R.string.mandiri_debit_card));
                    break;
                case BankType.MAYBANK:
                    imageBankLogo.setImageResource(R.drawable.maybank);
                    break;
                case BankType.BNI_DEBIT_ONLINE:
                    imageBankLogo.setImageResource(R.drawable.bni);
                    setTitle(getString(R.string.bni_debit_online_card));
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
        String cardNumberText = getCardNumberValue();
        if (TextUtils.isEmpty(cardNumberText)) {
            imageCardLogo.setImageResource(0);
            return;
        }

        // Check card type before setting logo
        String cardType = Helper.getCardType(cardNumberText);
        switch (cardType) {
            case Helper.CARD_TYPE_VISA:
                imageCardLogo.setImageResource(R.drawable.ic_visa);
                break;
            case Helper.CARD_TYPE_MASTERCARD:
                imageCardLogo.setImageResource(R.drawable.ic_mastercard);
                break;
            case Helper.CARD_TYPE_JCB:
                imageCardLogo.setImageResource(R.drawable.ic_jcb);
                break;
            case Helper.CARD_TYPE_AMEX:
                imageCardLogo.setImageResource(R.drawable.ic_amex);
                break;
        }
    }

    private static String formatCard(String cardNumber) {
        if (cardNumber == null) return null;
        char delimiter = ' ';
        return cardNumber.replaceAll(".{4}(?!$)", "$0" + delimiter);
    }

    private String getCleanedCardNumber() {
        String cleanCardNumber = "";
        try {
            String cardNumber = getCardNumberValue();
            cleanCardNumber = cardNumber.replace(" ", "");
        } catch (RuntimeException e) {
            Logger.error(TAG, "getCleanedCardNumber():" + e.getMessage());
        }
        return cleanCardNumber;
    }

    private String getCardNumberBin() {
        try {
            String cardNumber = getCardNumberValue();
            if (!TextUtils.isEmpty(cardNumber) && cardNumber.length() > 6) {
                String cardBin = cardNumber.replace(" ", "").substring(0, 6);
                return cardBin;
            }

        } catch (RuntimeException e) {
            Logger.error(TAG, "getCardNumberBin:" + e.getMessage());
        }
        return null;
    }

    private String getCardNumberValue() {
        return fieldCardNumber.getText().toString().trim();
    }

    /**
     * Begin negative dialog
     */

    private void showValidationError(TextView textView, String errorText) {
        if (errorText != null && !TextUtils.isEmpty(errorText)) {
            textView.setVisibility(View.VISIBLE);
            textView.setText(errorText);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void initPaymentError(Throwable error) {
        if (isActivityRunning()) {
            hideProgressLayout();
            showErrorMessage(error);
        }
    }

    private void showErrorMessage(Throwable error) {
        MessageInfo messageInfo = MessageHelper.createMessageOnError(error, this);
        MessageHelper.showToast(this, messageInfo.getDetailsMessage());
    }

    private void hideValidationError(TextView textView) {
        textView.setVisibility(View.GONE);
    }

    /**
     * Start network request
     */

    private void startTokenizeCreditCard() {
        showProgressLayout(getString(R.string.processing_payment));

        if (isOneClickMode()) {
            presenter.startOneClickPayment(savedCard.getMaskedCard());
        } else if (isTwoClicksMode()) {
            String cvv = fieldCardCvv.getText().toString().trim();
            presenter.startGettingCardToken(savedCard.getSavedTokenId(), cvv);
        } else {
            String cardNumber = getCleanedCardNumber();
            String cvv = fieldCardCvv.getText().toString().trim();
            String date = fieldCardExpiry.getText().toString();
            String month = date.split("/")[0].trim();
            String year = "20" + date.split("/")[1].trim();
            presenter.startGettingCardToken(cardNumber, month, year, cvv, checkboxSaveCard.isChecked());
        }
    }

    private void startPreCreditCardPayment() {
        String bankName = presenter.getBankByCardBin(getCardNumberBin());
        if (isBankPointEnabled() && bankName != null) {
            presenter.getBankPoint(bankName);
        } else {
            if (TextUtils.isEmpty(redirectUrl)) {
                startCreditCardPayment();
            } else {
                showWebViewPaymentPage(PaymentType.CREDIT_CARD, redirectUrl, Constants.INTENT_CODE_3DS_PAYMENT);
            }
        }
    }

    private void startCreditCardPayment() {
        showProgressLayout(getString(R.string.processing_payment));
        presenter.startNormalPayment(checkboxSaveCard.isChecked(), false);
    }

    private void getPaymentStatus() {
        showProgressLayout(null);
        presenter.getPaymentStatus();
    }

    private void startBankPointPage(PointResponse response) {
        Intent intent = new Intent(this, BankPointsActivity.class);
        float point = Float.parseFloat(response.getBalanceAmount());
        intent.putExtra(BankPointsActivity.EXTRA_POINT, point);
        String cardBin = getCardNumberBin();
        intent.putExtra(BankPointsActivity.EXTRA_BANK, presenter.getBankByCardBin(cardBin));
        startActivityForResult(intent, Constants.INTENT_BANK_POINT);
    }

    private void initBanksPointPayment(float redeemedPoint) {
        showProgressLayout(getString(R.string.processing_payment));
        presenter.startBankPointsPayment(redeemedPoint, checkboxSaveCard.isChecked());
    }

    /**
     * Override abstract and interface
     */

    @Override
    protected void initTheme() {
        try {
            setBackgroundTintList(fieldCardNumber);
            setBackgroundTintList(fieldCardExpiry);
            setBackgroundTintList(fieldCardCvv);

            setSecondaryBackgroundColor(textInstallmentTerm);
            textInstallmentTerm.getBackground().setAlpha(50);


            setCheckboxStateColor(checkboxSaveCard);
            setCheckboxStateColor(checkboxPointEnabled);
            setTextColor(textInstallmentTerm);
            setTextColor(textCardNumberHint);
            setTextColor(textExpriyHint);
            setTextColor(textCvvHint);
            setTextColor(textEmailHint);
            setTextColor(textPhoneHint);

            setTextColor(buttonIncreaseInstallment);
            setTextColor(buttonDecreaseInstallment);
            setBorderColor(buttonIncreaseInstallment);
            setBorderColor(buttonDecreaseInstallment);
            setColorFilter(buttonSaveCardHelp);
            setColorFilter(buttonCvvHelp);
            setColorFilter(buttonPointHelp);

            setTextColor(buttonScanCard);
            setIconColorFilter(buttonScanCard);
            setPrimaryBackgroundColor(buttonCompletePayment);
        } catch (Exception e) {
            Logger.error(TAG, "rendering theme:" + e.getMessage());
        }
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        if (MidtransKit.getInstance().getEnvironment() != Environment.SANDBOX) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
        }
        this.savedCard = (SaveCardRequest) getIntent().getSerializableExtra(EXTRA_SAVED_CARD);
        this.presenter = new CreditCardDetailsPresenter(CreditCardDetailsActivity.this, this, paymentInfoResponse);
    }

    @Override
    protected void initToolbarAndView() {
        super.initToolbarAndView();
        fieldCardNumber = findViewById(R.id.field_card_number);
        fieldCardCvv = findViewById(R.id.field_cvv);
        fieldCardExpiry = findViewById(R.id.field_expiry);
        fieldEmail = findViewById(R.id.field_email);
        fieldPhone = findViewById(R.id.field_phone);

        textInstallmentTerm = findViewById(R.id.text_installment_term);
        textTitleInstallment = findViewById(R.id.title_installment);
        textCardNumberHint = findViewById(R.id.hint_card_number);
        textExpriyHint = findViewById(R.id.hint_card_expiry);
        textCvvHint = findViewById(R.id.hint_card_cvv);
        textInApplicablePromoStatus = findViewById(R.id.text_offer_status_not_applied);
        textEmailHint = findViewById(R.id.hint_user_email);
        textPhoneHint = findViewById(R.id.hint_user_phone);

        textCardNumberError = findViewById(R.id.error_message_card_number);
        textCardExpiryError = findViewById(R.id.error_message_expiry);
        textCardCvvError = findViewById(R.id.error_message_cvv);
        textEmailError = findViewById(R.id.error_message_email);
        textPhoneError = findViewById(R.id.error_message_phone);

        imageCardLogo = findViewById(R.id.payment_card_logo);
        imageBankLogo = findViewById(R.id.bank_logo);
        ccBadge = findViewById(R.id.cc_badge);

        buttonCvvHelp = findViewById(R.id.help_cvv_button);
        buttonSaveCardHelp = findViewById(R.id.help_save_card);
        buttonPointHelp = findViewById(R.id.help_bank_point);

        buttonScanCard = findViewById(R.id.button_scan_card);
        buttonDeleteCard = findViewById(R.id.button_delete);
        buttonDecreaseInstallment = findViewById(R.id.button_installment_decrease);
        buttonIncreaseInstallment = findViewById(R.id.button_installment_increase);

        containerSaveCard = findViewById(R.id.container_save_card_details);
        containerInstallment = findViewById(R.id.container_installment);
        containerPoint = findViewById(R.id.container_bni_point);
        containerUserDetail = findViewById(R.id.container_user_detail);

        checkboxSaveCard = findViewById(R.id.checkbox_save_card);
        checkboxPointEnabled = findViewById(R.id.checkbox_point);

        recyclerViewPromo = findViewById(R.id.recycler_promo);
    }

    @Override
    public <T> void onPaymentSuccess(T response) {
        hideProgressLayout();
        creditCardResponse = (CreditCardResponse) response;
        if (creditCardResponse != null) {
            if (NetworkHelper.isPaymentSuccess(creditCardResponse)) {
                if (presenter.isRbaAuthentication(creditCardResponse)) {
                    showWebViewPaymentPage(PaymentType.CREDIT_CARD, creditCardResponse.getRedirectUrl(), Constants.INTENT_CODE_RBA_AUTHENTICATION);
                } else {
                    hideProgressLayout();
                    setCallbackOrSendToStatusPage();
                }
            } else {
                if (attempt < Constants.MAX_ATTEMPT) {
                    attempt += 1;
                    MessageHelper.showToast(this, getString(R.string.error_gopay_transaction));
                } else {
                    setCallbackOrSendFailedToStatusPage();
                }
            }
        } else {
            finishPayment(RESULT_OK, creditCardResponse);
        }
    }

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        showOnErrorPaymentStatusMessage(error);
    }

    @Override
    public void onNullInstanceSdk() {
        setResult(Constants.RESULT_SDK_NOT_AVAILABLE);
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.INTENT_CODE_3DS_PAYMENT) {
                startCreditCardPayment();
            } else if (requestCode == Constants.INTENT_CODE_3DS_BANK_POINT) {
                initBanksPointPayment(redeemedPoint);
            } else if (requestCode == Constants.INTENT_CODE_RBA_AUTHENTICATION) {
                getPaymentStatus();
            } else if (requestCode == Constants.INTENT_BANK_POINT) {
                //change flow
                if (data != null) {
                    redeemedPoint = data.getFloatExtra(BankPointsActivity.EXTRA_DATA_POINT, 0f);
                }
                if (!TextUtils.isEmpty(redirectUrl)) {
                    showWebViewPaymentPage(PaymentType.CREDIT_CARD, redirectUrl, Constants.INTENT_CODE_3DS_BANK_POINT);
                } else {
                    initBanksPointPayment(redeemedPoint);
                }
            } else if (requestCode == Constants.INTENT_CODE_PAYMENT_RESULT) {
                creditCardResponse.setPaymentType(PaymentType.CREDIT_CARD);
                creditCardResponse.setOrderId(paymentInfoResponse.getTransactionDetails().getOrderId());
                creditCardResponse.setGrossAmount(Double.toString(paymentInfoResponse.getTransactionDetails().getGrossAmount()));
                finishPayment(RESULT_OK, creditCardResponse);
            } else if (requestCode == TermsAndConditionsActivity.INTENT_TNC) {
                setCheckboxPoint(true);
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == Constants.INTENT_CODE_3DS_PAYMENT) {
                hideProgressLayout();
            } else if (requestCode == Constants.INTENT_CODE_RBA_AUTHENTICATION) {
                getPaymentStatus();
            } else if (requestCode == Constants.INTENT_CODE_PAYMENT_RESULT) {

            }
        }
    }

    @Override
    public boolean isBankPointEnabled() {
        return checkboxPointEnabled.isChecked();
    }

    @Override
    public void onGetCardTokenSuccess(TokenDetailsResponse response) {
        if (!TextUtils.isEmpty(response.getRedirectUrl())) {
            redirectUrl = response.getRedirectUrl();
        }
        startPreCreditCardPayment();
    }

    @Override
    public void onGetCardTokenFailure() {
        MessageHelper.showToast(this, getString(R.string.message_getcard_token_failed));
    }

    @Override
    public void onGetBankPointSuccess(PointResponse response) {
        startBankPointPage(response);
    }

    @Override
    public void onGetBankPointFailure() {
        hideProgressLayout();
        MessageHelper.showToast(this, getString(R.string.failed_to_get_bank_point));
    }

    @Override
    public void onGetTransactionStatusError(Throwable error) {
        initPaymentError(error);
    }

    @Override
    public void onGetTransactionStatusSuccess(PaymentResponse transactionResponse) {
        hideProgressLayout();
        setCallbackOrSendToStatusPage();
    }

    @Override
    public void onDeleteCardSuccess(String maskedCard) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DELETED_CARD_DETAILS, maskedCard);
        setResult(Constants.INTENT_RESULT_DELETE_CARD, intent);
        super.onBackPressed();
    }

    @Override
    public void onDeleteCardFailure() {
        hideProgressLayout();
    }
}