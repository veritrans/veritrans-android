package com.midtrans.sdk.uikit.views.creditcard.details;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.promo.Promo;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.models.CreditCardType;
import com.midtrans.sdk.uikit.models.MessageInfo;
import com.midtrans.sdk.uikit.scancard.ExternalScanner;
import com.midtrans.sdk.uikit.scancard.ScannerModel;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.creditcard.bankpoints.BankPointsActivity;
import com.midtrans.sdk.uikit.views.creditcard.tnc.TermsAndConditionsActivity;
import com.midtrans.sdk.uikit.views.status.PaymentStatusActivity;
import com.midtrans.sdk.uikit.views.webview.WebViewPaymentActivity;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.midtrans.sdk.uikit.utilities.UiKitConstants.ENVIRONMENT_DEVELOPMENT;

/**
 * Created by ziahaqi on 7/11/17.
 */

public class CreditCardDetailsActivity extends BasePaymentActivity implements CreditCardDetailsView {

    public static final String EXTRA_DELETED_CARD_DETAILS = "card.deleted.details";
    public static final String EXTRA_SAVED_CARD = "extra.card.saved";
    private static final String TAG = CreditCardDetailsActivity.class.getSimpleName();
    private final String PAGE_NAME = "CC Card Details";
    private final String BUTTON_CONFIRM_NAME = "Confirm Payment Credit Card";
    private final String BUTTON_RETRY_NAME = "Retry Credit Card";
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
    private SemiBoldTextView textTitle;

    private ImageView imageCardLogo;
    private ImageView imageBankLogo;
    private ImageView ccBadge;

    private ImageButton buttonCvvHelp;
    private ImageButton buttonSaveCardHelp;
    private ImageButton buttonPointHelp;

    private FancyButton buttonScanCard;
    private FancyButton buttonDeleteCard;
    private FancyButton buttonPayNow;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init screenshot prevention in production
        if (!BuildConfig.FLAVOR.equalsIgnoreCase(ENVIRONMENT_DEVELOPMENT)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);
        }
        initProperties();
        setContentView(R.layout.activity_credit_card);
        initCardNumber();
        initCardExpiry();
        initCardCvv();
        intPaymentButton();
        initHelpButtons();
        initInstallmentButton();
        initScanCardButton();
        initDeleteButton();
        initCheckBox();
        initPromoList();
        initLayoutState();
        bindData();
    }

    private void initScanCardButton() {
        if (presenter.isCardScannerAvailable()) {
            showScanCardButton(true);

            buttonScanCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start scanning
                    presenter.startScanCard(CreditCardDetailsActivity.this, UiKitConstants.INTENT_REQUEST_SCAN_CARD);
                }
            });
        } else {
            showScanCardButton(false);
        }
    }

    private void initCheckBox() {
        checkboxPointEnabled.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!checkboxPointEnabled.isChecked() && presenter.isBniPointAvailable(getCardNumberBin())) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Intent intent = new Intent(CreditCardDetailsActivity.this, TermsAndConditionsActivity.class);
                        startActivityForResult(intent, TermsAndConditionsActivity.INTENT_TNC);
                    }

                    return true;
                }

                return false;
            }
        });

        checkboxPointEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showInstallmentLayout(false);
                } else {
                    checkInstallment();
                }
            }
        });
    }

    private void setCheckboxPoint(boolean check) {
        if (check) {
            checkboxPointEnabled.setChecked(true);
        } else {
            checkboxPointEnabled.setChecked(false);
        }

    }

    private void initPromoList() {
        promosAdapter = new PromosAdapter(getPrimaryColor(),
                new PromosAdapter.OnPromoCheckedChangeListener() {
                    @Override
                    public void onPromoCheckedChanged(Promo promo) {
                        updateItemDetails(promo);
                    }

                    @Override
                    public void onPromoUnavailable() {
                        updateItemDetails(null);
                    }
                });

        recyclerViewPromo.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPromo.setHasFixedSize(true);
        recyclerViewPromo.setAdapter(promosAdapter);

        initCreditCardPromos(true);
    }

    private void updateItemDetails(Promo promo) {
        if (transactionDetailAdapter != null) {
            if (promo != null && promo.isSelected()) {
                addNewItemDetails(presenter.createTransactionItem(promo));
            } else {
                removeItemDetails(UiKitConstants.PROMO_ID);
            }
        }
    }

    private void initDeleteButton() {
        buttonDeleteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCardDeletionConfirmation();
            }
        });
    }

    private void showCardDeletionConfirmation() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.card_delete_message)
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        showProgressLayout(getString(R.string.processing_delete));
                        presenter.deleteSavedCard(savedCard);
                    }
                })
                .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        alertDialog.show();
    }

    private void bindData() {
        textTitle.setText(R.string.card_details);

        if (savedCard != null) {
            showDeleteIcon();

            String cardType = Utils.getCardType(savedCard.getMaskedCard());
            if (!TextUtils.isEmpty(cardType)) {
                try {
                    String cardBin = savedCard.getMaskedCard().substring(0, 4);
                    String title = cardType + "-" + cardBin;
                    textTitle.setText(title);

                } catch (RuntimeException e) {
                    Logger.e(TAG, "cardType:" + e.getMessage());
                }
            }

            showSavedCardLayout(false);

            fieldCardExpiry.setInputType(InputType.TYPE_CLASS_TEXT);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(20);
            fieldCardExpiry.setFilters(filterArray);
            fieldCardExpiry.setEnabled(false);
            fieldCardExpiry.setText(SdkUIFlowUtil.getMaskedExpDate());

            fieldCardNumber.setEnabled(false);
            fieldCardNumber.setText(SdkUIFlowUtil.getMaskedCardNumber(savedCard.getMaskedCard()));

            fieldCardCvv.requestFocus();


            if (isOneClickMode()) {
                fieldCardCvv.setInputType(InputType.TYPE_CLASS_TEXT);
                fieldCardCvv.setFilters(filterArray);
                fieldCardCvv.setText(SdkUIFlowUtil.getMaskedCardCvv());
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

        //track page view after page properly loaded
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);

        initUserDetail();

    }

    private void showDeleteIcon() {
        buttonDeleteCard.setVisibility(View.VISIBLE);
        findViewById(R.id.button_separator).setVisibility(View.VISIBLE);
    }

    private void initExtras() {
    }

    private void initLayoutState() {
        if (presenter.isSavedCardEnabled()) {
            showSavedCardLayout(true);
            if (presenter.isSaveCardOptionChecked()) {
                checkboxSaveCard.setChecked(true);
            }
        }

    }


    private void initProperties() {
        this.savedCard = (SaveCardRequest) getIntent().getSerializableExtra(EXTRA_SAVED_CARD);
        this.presenter = new CreditCardDetailsPresenter(this, this);
    }

    @Override
    public void initTheme() {
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
            setPrimaryBackgroundColor(buttonPayNow);

        } catch (Exception e) {
            Logger.e(TAG, "rendering theme:" + e.getMessage());
        }
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

                    String cardType = Utils.getCardType(s.toString());

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
                    Logger.e(TAG, "inputCcNumber:" + e.getMessage());
                }
            }
        });

        // focus change listener
        fieldCardNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    checkCardNumberValidity();
                    checkBinLockingValidity();
                    checkInstallment();
                    checkBankPoint();
                }
            }
        });
    }

    public static String formatCard(String cardNumber) {
        if (cardNumber == null) return null;
        char delimiter = ' ';
        return cardNumber.replaceAll(".{4}(?!$)", "$0" + delimiter);
    }

    private void initCreditCardPromos(final boolean firstTime) {
        final String cardNumber = getCleanedCardNumber();
        if (cardNumber.length() < 7) {
            recyclerViewPromo.postDelayed(new Runnable() {
                @Override
                public void run() {
                    promosAdapter.setData(presenter.getCreditCardPromos(cardNumber, firstTime));
                }
            }, 100);
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
                    Logger.e(TAG, "ccexpiry:" + e.getMessage());
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

    private void intPaymentButton() {
        buttonPayNow.setTextBold();
        buttonPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SdkUIFlowUtil.hideKeyboard(CreditCardDetailsActivity.this);
                if (checkCardValidity() && checkUserDataValidity()) {
                    if (isEmailShown) {
                        presenter.saveUserDetail(fieldEmail.getEditableText().toString(), fieldPhone.getEditableText().toString());
                    }
                    if (checkPaymentValidity()) {
                        setPromoSelected();
                        presenter.trackButtonClick(attempt == 0 ? BUTTON_CONFIRM_NAME : BUTTON_RETRY_NAME, PAGE_NAME);
                        TokenizeCreditCard();
                    }
                }
            }
        });
    }

    private void setPromoSelected() {
        if (promosAdapter != null) {
            presenter.setSelectedPromo(promosAdapter.getSeletedPromo());
        }
    }

    private void initHelpButtons() {
        buttonCvvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(CreditCardDetailsActivity.this)
                        .setTitle(R.string.what_is_cvv)
                        .setView(R.layout.dialog_cvv)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
                changeDialogButtonColor(alertDialog);
            }
        });

        buttonSaveCardHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show help dialog
                AlertDialog alertDialog = new AlertDialog.Builder(CreditCardDetailsActivity.this)
                        .setTitle(R.string.save_card_message)
                        .setMessage(R.string.save_card_dialog)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
                changeDialogButtonColor(alertDialog);
            }
        });
    }

    private void initBankPointHelp(final String bankName) {
        buttonPointHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                alertDialog.show();
                changeDialogButtonColor(alertDialog);
            }
        });
    }

    private void changeDialogButtonColor(AlertDialog alertDialog) {
        try {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            setTextColor(positiveButton);
        } catch (Exception e) {
            Logger.d(TAG, "RenderThemeError:" + e.getMessage());
        }
    }

    private void TokenizeCreditCard() {
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

    private boolean checkPaymentValidity() {
        // Card bin validation for bin locking and installment
        String cardNumber = getCleanedCardNumber();
        if (presenter.isCardBinBlocked(cardNumber)) {
            Toast.makeText(this, getString(R.string.offer_not_applied), Toast.LENGTH_SHORT).show();
            return false;
        }

        // Installment validation
        if (!presenter.isInstallmentValid()) {
            Toast.makeText(this, getString(R.string.installment_required), Toast.LENGTH_SHORT).show();
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

    private void checkBinLockingValidity() {
        String cardNumber = getCleanedCardNumber();

        if (presenter.isCardBinBlocked(cardNumber)) {
            showInApplicablePromo(true);
        } else {
            showInApplicablePromo(false);
        }
    }

    private void showInApplicablePromo(boolean show) {
        if (show) {
            textInApplicablePromoStatus.setVisibility(View.VISIBLE);
        } else {
            textInApplicablePromoStatus.setVisibility(View.GONE);
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

    @Override
    public void bindViews() {
        fieldCardNumber = findViewById(R.id.field_card_number);
        fieldCardCvv = findViewById(R.id.field_cvv);
        fieldCardExpiry = findViewById(R.id.field_expiry);
        fieldEmail = findViewById(R.id.field_email);
        fieldPhone = findViewById(R.id.field_phone);

        textInstallmentTerm = findViewById(R.id.text_installment_term);
        textTitleInstallment = findViewById(R.id.title_installment);
        textTitle = findViewById(R.id.text_page_title);
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
        buttonPayNow = findViewById(R.id.button_primary);
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

    private void setBankType() {
        // Don't set card type when card number is empty
        String cardNumberText = getCardNumberValue();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 7) {
            imageBankLogo.setImageDrawable(null);
            textTitle.setText(R.string.card_details);
            return;
        }

        String cleanCardNumber = getCardNumberBin();
        String bank = presenter.getBankByCardBin(cleanCardNumber);
        textTitle.setText(R.string.card_details);

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
                    if (presenter.isMandiriDebitCard(cleanCardNumber)) {
                        textTitle.setText(R.string.mandiri_debit_card);
                    }
                    break;
                case BankType.MAYBANK:
                    imageBankLogo.setImageResource(R.drawable.maybank);
                    break;
                case BankType.BNI_DEBIT_ONLINE:
                    imageBankLogo.setImageResource(R.drawable.bni);
                    textTitle.setText(R.string.bni_debit_online_card);
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
        String cardType = Utils.getCardType(cardNumberText);
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
            fieldPhone.setOnFocusChangeListener(new OnFocusChangeListener() {
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

        if (cardNumberText.length() < 13 || !SdkUIFlowUtil.isValidCardNumber(cardNumberText)) {
            showValidationError(textCardNumberError, getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            hideValidationError(textCardNumberError);
        }
        return isValid;
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
            Logger.d(TAG, "expDate:" + expDateArray[0].trim() + expDateArray[1].trim());
        } catch (NullPointerException e) {
            Logger.d(TAG, "expiry date empty");
        } catch (IndexOutOfBoundsException e) {
            Logger.d(TAG, "expiry date issue");
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
            Logger.d(TAG, "currentMonth:" + currentMonth + ",currentYear:" + currentYear);

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


    public String getCardNumberValue() {
        return fieldCardNumber.getText().toString().trim();
    }

    public String getCardNumberBin() {
        try {
            String cardNumber = getCardNumberValue();
            if (!TextUtils.isEmpty(cardNumber) && cardNumber.length() > 6) {
                String cardBin = cardNumber.replace(" ", "").substring(0, 6);
                return cardBin;
            }

        } catch (RuntimeException e) {
            Logger.e(TAG, "getCardNumberBin:" + e.getMessage());
        }
        return null;
    }

    public String getCleanedCardNumber() {
        String cleanCardNumber = "";
        try {
            String cardNumber = getCardNumberValue();
            cleanCardNumber = cardNumber.replace(" ", "");
        } catch (RuntimeException e) {
            Logger.e(TAG, "getCleanedCardNumber():" + e.getMessage());
        }
        return cleanCardNumber;
    }


    private void getPaymentStatus() {
        showProgressLayout(null);
        presenter.getPaymentStatus();
    }

    private void initBanksPointPayment(float redeemedPoint) {
        showProgressLayout(getString(R.string.processing_payment));
        presenter.startBankPointsPayment(redeemedPoint, checkboxSaveCard.isChecked());
    }

    private void updateScanCardData(ScannerModel scanCardData) {
        if (scanCardData != null) {
            String formattedCardNumber = Utils.getFormattedCreditCardNumber(scanCardData.getCardNumber());
            String cvv = scanCardData.getCvv();
            String cardExpiry = String.format("%s/%d", scanCardData.getExpiredMonth() < 10 ? String.format("0%d",
                    scanCardData.getExpiredMonth()) : String.format("%d", scanCardData.getExpiredMonth()),
                    scanCardData.getExpiredYear() - 2000);
            fieldCardNumber.setText(formattedCardNumber);
            fieldCardCvv.setText(cvv);
            fieldCardExpiry.setText(cardExpiry);
        }
    }

    private void showSavedCardLayout(boolean show) {
        if (show) {
            containerSaveCard.setVisibility(View.VISIBLE);
        } else {
            checkboxSaveCard.setChecked(false);
            containerSaveCard.setVisibility(View.GONE);
        }
    }

    private void showScanCardButton(boolean show) {
        if (show) {
            buttonScanCard.setVisibility(View.VISIBLE);
            findViewById(R.id.button_separator).setVisibility(View.VISIBLE);
        } else {
            buttonScanCard.setVisibility(View.GONE);
            findViewById(R.id.button_separator).setVisibility(View.GONE);
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

    public boolean isOneClickMode() {
        return savedCard != null && savedCard.getType() != null && savedCard.getType().equals(CreditCardType.ONE_CLICK);
    }

    public boolean isTwoClicksMode() {
        return savedCard != null;
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

    private void showInstallmentLayout(boolean show) {
        if (show) {
            containerInstallment.setVisibility(View.VISIBLE);
            resetInstallmentButtons();
        } else {
            containerInstallment.setVisibility(View.GONE);
            presenter.setCurrentInstallment(0);
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


    private void initInstallmentButton() {
        buttonIncreaseInstallment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int installmentTermCurrentPosition = presenter.getInstallmentCurrentPosition();
                int installmentTermCount = presenter.getInstallmentTotalPositions();
                if (installmentTermCurrentPosition >= 0 && installmentTermCurrentPosition < installmentTermCount) {
                    presenter.setCurrentInstallment(installmentTermCurrentPosition + 1);
                    setCurrentInstallmentTerm();
                }
                changeBankPointVisibility();
                disableEnableInstallmentButton();
            }
        });

        buttonDecreaseInstallment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int installmentTermCurrentPosition = presenter.getInstallmentCurrentPosition();
                int installmentTermCount = presenter.getInstallmentTotalPositions();
                if (installmentTermCurrentPosition > 0 && installmentTermCurrentPosition <= installmentTermCount) {
                    presenter.setCurrentInstallment(installmentTermCurrentPosition - 1);
                    setCurrentInstallmentTerm();
                }
                changeBankPointVisibility();
                disableEnableInstallmentButton();
            }
        });
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

    private void initPaymentStatus(TransactionResponse response) {
        if (presenter.isShowPaymentStatus()) {
            showPaymentStatus(response);
        } else {
            finishPayment(RESULT_OK);
        }
    }

    private void showErrorMessage(Throwable error) {
        MessageInfo messageInfo = MessageUtil.createMessageOnError(this, error, null);
        SdkUIFlowUtil.showToast(this, messageInfo.detailsMessage);
    }

    private void startBankPointPage(BanksPointResponse response) {
        Intent intent = new Intent(this, BankPointsActivity.class);
        float point = Float.parseFloat(response.getPointBalanceAmount());
        intent.putExtra(BankPointsActivity.EXTRA_POINT, point);
        String cardBin = getCardNumberBin();
        intent.putExtra(BankPointsActivity.EXTRA_BANK, presenter.getBankByCardBin(cardBin));
        startActivityForResult(intent, UiKitConstants.INTENT_BANK_POINT);
    }

    private void showPaymentStatus(TransactionResponse response) {
        Intent intent = new Intent(this, PaymentStatusActivity.class);
        intent.putExtra(PaymentStatusActivity.EXTRA_PAYMENT_RESULT, response);
        startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT_STATUS);
    }

    private void finishPayment(int resultCode) {
        Intent data = new Intent();
        data.putExtra(UiKitConstants.KEY_TRANSACTION_RESPONSE, presenter.getTransactionResponse());
        setResult(resultCode, data);
        finish();
    }

    private void startPreCreditCardPayment() {
        String bankName = presenter.getBankByCardBin(getCardNumberBin());
        if (isBankPointEnabled() && bankName != null) {
            presenter.getBankPoint(bankName);
        } else {
            if (TextUtils.isEmpty(redirectUrl)) {
                startCreditCardPayment();
            } else {
                start3DSecurePage(redirectUrl, UiKitConstants.INTENT_CODE_3DS_PAYMENT);
            }
        }
    }

    private void startCreditCardPayment() {
        showProgressLayout(getString(R.string.processing_payment));
        presenter.startNormalPayment(checkboxSaveCard.isChecked(), false);
    }

    private void start3DSecurePage(String redirectUrl, int requestCode) {
        Intent intent = new Intent(this, WebViewPaymentActivity.class);
        intent.putExtra(WebViewPaymentActivity.EXTRA_PAYMENT_URL, redirectUrl);
        intent.putExtra(WebViewPaymentActivity.EXTRA_PAYMENT_TYPE, PaymentType.CREDIT_CARD);
        startActivityForResult(intent, requestCode);
    }

    private void initPaymentError(Throwable error) {
        if (isActivityRunning()) {
            hideProgressLayout();
            showErrorMessage(error);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == UiKitConstants.INTENT_CODE_3DS_PAYMENT) {
                //change flow
                startCreditCardPayment();
            } else if (requestCode == UiKitConstants.INTENT_CODE_3DS_BANK_POINT) {
                initBanksPointPayment(redeemedPoint);
            } else if (requestCode == UiKitConstants.INTENT_CODE_RBA_AUTHENTICATION) {
                getPaymentStatus();
            } else if (requestCode == UiKitConstants.INTENT_REQUEST_SCAN_CARD) {
                if (data != null && data.hasExtra(ExternalScanner.EXTRA_SCAN_DATA)) {
                    ScannerModel scanData = (ScannerModel) data.getSerializableExtra(ExternalScanner.EXTRA_SCAN_DATA);
                    Logger.i(String.format("Card Number: %s, Card Expire: %s/%d",
                            scanData.getCardNumber(), scanData.getExpiredMonth() < 10 ? String.format("0%d",
                                    scanData.getExpiredMonth()) : String.format("%d", scanData.getExpiredMonth()),
                            scanData.getExpiredYear() - 2000));
                    updateScanCardData(scanData);
                }
            } else if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
                finishPayment(resultCode);
            } else if (requestCode == UiKitConstants.INTENT_BANK_POINT) {
                //change flow
                if (data != null) {
                    redeemedPoint = data.getFloatExtra(BankPointsActivity.EXTRA_DATA_POINT, 0f);
                }
                if (!TextUtils.isEmpty(redirectUrl)) {
                    start3DSecurePage(redirectUrl, UiKitConstants.INTENT_CODE_3DS_BANK_POINT);
                } else {
                    initBanksPointPayment(redeemedPoint);
                }
            }

            if (requestCode == TermsAndConditionsActivity.INTENT_TNC) {
                setCheckboxPoint(true);
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == UiKitConstants.INTENT_CODE_3DS_PAYMENT) {
                hideProgressLayout();
            } else if (requestCode == UiKitConstants.INTENT_CODE_RBA_AUTHENTICATION) {
                getPaymentStatus();
            } else if (requestCode == UiKitConstants.INTENT_CODE_PAYMENT_STATUS) {
                finishPayment(RESULT_OK);
            }

        }
    }


    @Override
    public boolean isBankPointEnabled() {
        return checkboxPointEnabled.isChecked();
    }

    @Override
    public void onGetCardTokenSuccess(TokenDetailsResponse response) {
        if (isActivityRunning()) {
            SdkUIFlowUtil.hideKeyboard(this);

            //change flow
            if (!TextUtils.isEmpty(response.getRedirectUrl())) {
                redirectUrl = response.getRedirectUrl();
            }
            startPreCreditCardPayment();
        }
    }

    @Override
    public void onGetCardTokenFailure() {
        hideProgressLayout();
        if (isActivityRunning()) {
            String detailsMessage = getString(R.string.message_getcard_token_failed);
            SdkUIFlowUtil.showToast(this, detailsMessage);
        }
    }

    @Override
    public void onGetBankPointSuccess(BanksPointResponse response) {
        hideProgressLayout();
        if (isActivityRunning()) {
            startBankPointPage(response);
        }
    }


    @Override
    public void onGetBankPointFailure() {
        hideProgressLayout();
        if (isActivityRunning()) {
            SdkUIFlowUtil.showToast(this, getString(R.string.failed_to_get_bank_point));
        }
    }

    @Override
    public void onPaymentSuccess(TransactionResponse response) {
        if (isActivityRunning()) {
            if (presenter.isRbaAuthentication(response)) {
                start3DSecurePage(response.getRedirectUrl(), UiKitConstants.INTENT_CODE_RBA_AUTHENTICATION);
            } else {
                hideProgressLayout();
                initPaymentStatus(response);
            }
        } else {
            finishPayment(RESULT_OK);
        }
    }

    @Override
    public void onPaymentFailure(TransactionResponse response) {
        hideProgressLayout();
        if (isActivityRunning()) {
            if (attempt < UiKitConstants.MAX_ATTEMPT) {
                attempt += 1;
                showPaymentFailureMessage(response);
            } else {
                initPaymentStatus(response);
            }

            if (response != null && response.getStatusCode().equals(getString(R.string.failed_code_400))) {
                Logger.d("3dserror", "400:" + response.getValidationMessages().get(0));
            }
        }
    }

    private void showPaymentFailureMessage(TransactionResponse response) {
        String statusMessage = getString(R.string.message_payment_failed);

        if (response != null
                && response.getStatusCode().equals(UiKitConstants.STATUS_CODE_411)
                && !TextUtils.isEmpty(response.getStatusMessage())
                && response.getStatusMessage().toLowerCase().contains(MessageUtil.PROMO_UNAVAILABLE)) {

            statusMessage = getString(R.string.promo_unavailable);
        }

        SdkUIFlowUtil.showToast(this, statusMessage);
    }

    @Override
    public void onPaymentError(Throwable error) {
        hideProgressLayout();
        if (isActivityRunning()) {
            initPaymentError(error);
        }
    }

    @Override
    public void onDeleteCardSuccess(String maskedCardNumber) {
        hideProgressLayout();
        if (isActivityRunning()) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DELETED_CARD_DETAILS, maskedCardNumber);
            setResult(UiKitConstants.INTENT_RESULT_DELETE_CARD, intent);
            onBackPressed();
        }
    }


    @Override
    public void onDeleteCardFailure() {
        hideProgressLayout();
        if (isActivityRunning()) {
            SdkUIFlowUtil.showToast(this, getString(R.string.error_delete_message));
        }
    }

    @Override
    public void onGetTransactionStatusError(Throwable error) {
        hideProgressLayout();
        if (isActivityRunning()) {
            initPaymentError(error);
        }
    }

    @Override
    public void onGetTransactionStatusFailure(TransactionResponse response) {
        Logger.d(TAG, "rba>onGetTransactionStatusFailure()");
        hideProgressLayout();
        if (isActivityRunning()) {
            initPaymentStatus(response);
        }
    }

    @Override
    public void onGetTransactionStatusSuccess(TransactionResponse transactionResponse) {
        Logger.d(TAG, "rba>onGetTransactionStatusSuccess()");
        hideProgressLayout();

        if (isActivityRunning()) {
            initPaymentStatus(transactionResponse);
        }
    }


    @Override
    public void onBackPressed() {
        if (presenter != null) {
            presenter.trackBackButtonClick(PAGE_NAME);
        }
        super.onBackPressed();
    }
}
