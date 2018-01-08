package com.midtrans.sdk.uikit.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.callback.ObtainPromoCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CreditCardFromScanner;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.promo.ObtainPromosResponse;
import com.midtrans.sdk.corekit.models.snap.PromoResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.CreditDebitCardFlowActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.AspectRatioImageView;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Deprecated, please see {@link com.midtrans.sdk.uikit.views.creditcard.register.CardRegistrationActivity}
 */
@Deprecated
public class AddCardDetailsFragment extends Fragment {

    private static final String KEY_PAY_BUTTON_EVENT = "Pay";
    private static final String KEY_SCAN_BUTTON_EVENT = "Scan Card";
    private static final String KEY_CHECKBOX_SAVE_CARD_EVENT = "Save Card";
    private static final String ARGS_SAVED_CARD = "args_saved_card";
    private static final String ARGS_PROMO = "args_promo";
    private static final String TAG = AddCardDetailsFragment.class.getSimpleName();

    TextInputLayout tilCardNo, tilCvv, tilExpiry;
    TextView textInstallmentTerm;
    int installmentCurrentPosition, installmentTotalPositions;
    FancyButton payNowBtn;
    private String lastExpDate = "";
    private EditText etCardNo;
    private EditText etCvv;
    private EditText etExpiryDate;
    private AppCompatCheckBox cbSaveCard, cbBankPoint;
    private FancyButton buttonIncrease, buttonDecrease;
    private ImageView logo;
    private ImageView bankLogo;
    private ImageButton imageCvvHelp;
    private ImageView imageBanksPointHelp;
    private AspectRatioImageView promoLogoBtn;
    private ImageButton imageSaveCardHelp;
    private Button scanCardBtn;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String[] expDateArray;
    private int expMonth;
    private int expYear;
    private MidtransSDK midtransSDK;
    private String cardType = "";
    private RelativeLayout formLayout;
    private LinearLayout layoutInstallment;
    private RelativeLayout layoutSaveCard;
    private RelativeLayout layoutBanksPoint;
    private DefaultTextView textInvalidPromoStatus;
    private SaveCardRequest savedCard;
    private PromoResponse promo;
    private String discountToken;
    private TextView installmentText;

    public static AddCardDetailsFragment newInstance(SaveCardRequest card, PromoResponse promo) {
        AddCardDetailsFragment fragment = new AddCardDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_SAVED_CARD, card);
        bundle.putSerializable(ARGS_PROMO, promo);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static AddCardDetailsFragment newInstance(SaveCardRequest card) {
        AddCardDetailsFragment fragment = new AddCardDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_SAVED_CARD, card);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        midtransSDK = ((CreditDebitCardFlowActivity) getActivity()).getMidtransSDK();
    }

    private void setupSavedCard() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            SaveCardRequest savedCard = (SaveCardRequest) bundle.getSerializable(ARGS_SAVED_CARD);
            promo = (PromoResponse) bundle.getSerializable(ARGS_PROMO);
            if (savedCard != null) {
                Log.i(TAG, "savedcard");
                this.savedCard = savedCard;

                ((CreditDebitCardFlowActivity) getActivity()).showDeleteCardIcon(true);

                String cardType = Utils.getCardType(savedCard.getMaskedCard());
                if (!TextUtils.isEmpty(cardType)) {
                    String cardBin = savedCard.getMaskedCard().substring(0, 4);
                    String title = cardType + "-" + cardBin;
                    ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(title);
                }

                layoutSaveCard.setVisibility(View.GONE);
                scanCardBtn.setVisibility(View.GONE);

                etExpiryDate.setInputType(InputType.TYPE_CLASS_TEXT);
                InputFilter[] filterArray = new InputFilter[1];
                filterArray[0] = new InputFilter.LengthFilter(20);
                etExpiryDate.setFilters(filterArray);
                etCardNo.setEnabled(false);
                etExpiryDate.setEnabled(false);
                etCvv.requestFocus();
                etCardNo.setText(SdkUIFlowUtil.getMaskedCardNumber(savedCard.getMaskedCard()));
                etExpiryDate.setText(SdkUIFlowUtil.getMaskedExpDate());
                if (promo != null && promo.getDiscountAmount() > 0) {
                    obtainPromo(promo);
                }
                if (isOneClickMode()) {
                    etCvv.setInputType(InputType.TYPE_CLASS_TEXT);
                    etCvv.setFilters(filterArray);
                    etCvv.setText(SdkUIFlowUtil.getMaskedCardCvv());
                    etCvv.setEnabled(false);

                    ((CreditDebitCardFlowActivity) getActivity()).setInstallmentAvailableStatus(false);
                } else {
                    initCardInstallment();
                }

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(getString(R.string.card_details));
        return inflater.inflate(R.layout.fragment_add_card_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Logger.i("onViewCreated called addcarddetail called");
            CreditDebitCardFlowActivity creditDebitCardFlowActivity = (CreditDebitCardFlowActivity) getActivity();
            if (creditDebitCardFlowActivity != null && creditDebitCardFlowActivity.getSupportActionBar() != null) {
                creditDebitCardFlowActivity.getSupportActionBar().setTitle(getString(R.string.card_details));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        bindViews(view);
        setupSavedCard();
        fadeIn();
    }


    @Override
    public void onDestroyView() {
        ((CreditDebitCardFlowActivity) getActivity()).showDeleteCardIcon(false);
        super.onDestroyView();
    }

    private void bindViews(View view) {
        formLayout = (RelativeLayout) view.findViewById(R.id.form_layout);
        tilCardNo = (TextInputLayout) view.findViewById(R.id.til_card_no);
        tilCvv = (TextInputLayout) view.findViewById(R.id.cvv_til);
        tilExpiry = (TextInputLayout) view.findViewById(R.id.exp_til);
        etCardNo = (EditText) view.findViewById(R.id.et_card_no);
        etCvv = (EditText) view.findViewById(R.id.et_cvv);
        etExpiryDate = (EditText) view.findViewById(R.id.et_exp_date);
        cbSaveCard = (AppCompatCheckBox) view.findViewById(R.id.cb_store_card);
        cbBankPoint = (AppCompatCheckBox) view.findViewById(R.id.cb_bni_point);
        initCheckbox();
        imageCvvHelp = (ImageButton) view.findViewById(R.id.image_cvv_help);
        payNowBtn = (FancyButton) view.findViewById(R.id.btn_pay_now);
        imageBanksPointHelp = (ImageView) view.findViewById(R.id.image_bni_help);
        payNowBtn = (FancyButton) view.findViewById(R.id.btn_pay_now);
        scanCardBtn = (Button) view.findViewById(R.id.scan_card);
        logo = (ImageView) view.findViewById(R.id.payment_card_logo);
        bankLogo = (ImageView) view.findViewById(R.id.bank_logo);
        layoutInstallment = (LinearLayout) view.findViewById(R.id.layout_installment);
        layoutSaveCard = (RelativeLayout) view.findViewById(R.id.layout_save_card_detail);
        buttonIncrease = (FancyButton) view.findViewById(R.id.button_installment_increase);
        buttonDecrease = (FancyButton) view.findViewById(R.id.button_installment_decrease);
        layoutBanksPoint = (RelativeLayout) view.findViewById(R.id.layout_bni_point);
        textInstallmentTerm = (TextView) view.findViewById(R.id.text_installment_term);
        textInvalidPromoStatus = (DefaultTextView) view.findViewById(R.id.text_offer_status_not_applied);
        promoLogoBtn = (AspectRatioImageView) view.findViewById(R.id.promo_logo);
        installmentText = (TextView) view.findViewById(R.id.installment_title);
        imageSaveCardHelp = (ImageButton) view.findViewById(R.id.help_save_card);

        // Set color theme for field
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getSecondaryColor() != 0) {
                etCardNo.getBackground().setColorFilter(midtransSDK.getColorTheme().getSecondaryColor(), PorterDuff.Mode.SRC_ATOP);
                etCvv.getBackground().setColorFilter(midtransSDK.getColorTheme().getSecondaryColor(), PorterDuff.Mode.SRC_ATOP);
                etExpiryDate.getBackground().setColorFilter(midtransSDK.getColorTheme().getSecondaryColor(), PorterDuff.Mode.SRC_ATOP);
                etCardNo.setHintTextColor(midtransSDK.getColorTheme().getSecondaryColor());
                etCvv.setHintTextColor(midtransSDK.getColorTheme().getSecondaryColor());
                etExpiryDate.setHintTextColor(midtransSDK.getColorTheme().getSecondaryColor());
            }

            if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                installmentText.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                buttonIncrease.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                buttonIncrease.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                buttonDecrease.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                buttonDecrease.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                imageSaveCardHelp.setColorFilter(midtransSDK.getColorTheme().getPrimaryDarkColor(), PorterDuff.Mode.SRC_ATOP);
                imageCvvHelp.setColorFilter(midtransSDK.getColorTheme().getPrimaryDarkColor(), PorterDuff.Mode.SRC_ATOP);
            }

            if (midtransSDK.getColorTheme().getSecondaryColor() != 0) {
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
                cbSaveCard.setSupportButtonTintList(new ColorStateList(states, trackColors));
            }
        }


        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onIncraseTerm();
            }
        });

        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDecreaseTerm();
            }
        });

        cbSaveCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkCardValidity();
            }
        });

        imageSaveCardHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show help dialog
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
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

        etCardNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    boolean validCardNumber = checkCardNumberValidity();
                    checkBinLockingValidity();
                    initCardInstallment();
                    initBNIPoints(validCardNumber);
                    initPromoUsingPromoEngine();

                }
            }
        });
        etExpiryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    checkCardExpiryValidity();
                }
            }
        });
        etCvv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    checkCardCVVValidity();
                }
            }
        });

        if (midtransSDK != null && midtransSDK.getSemiBoldText() != null) {
            payNowBtn.setCustomTextFont(midtransSDK.getSemiBoldText());
            // Set background for pay now button
            if (midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryColor() != 0) {
                payNowBtn.setBackgroundColor(midtransSDK.getColorTheme().getPrimaryColor());
            }
            scanCardBtn.setTypeface(Typeface.createFromAsset(getContext().getAssets(), midtransSDK.getDefaultText()));
            if (midtransSDK.getExternalScanner() != null) {
                // Set background color for scan button
                if (midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                    scanCardBtn.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                }
                scanCardBtn.setVisibility(View.VISIBLE);
                scanCardBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start scanning
                        midtransSDK.getExternalScanner().startScan(getActivity(), CreditDebitCardFlowActivity.SCAN_REQUEST_CODE);
                    }
                });
            } else {
                scanCardBtn.setVisibility(View.GONE);
            }

            if (midtransSDK.getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_none))) {
                showSwitchSaveCardLayout(false);
            } else {
                showSwitchSaveCardLayout(true);
            }
        }

        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCardValidity()) {
                    ((CreditDebitCardFlowActivity) getActivity()).setBankPointStatus(isBanksPointActivated());

                    if (!isValidPayment()) {
                        return;
                    }

                    if (isOneClickMode()) {
                        ((CreditDebitCardFlowActivity) getActivity()).oneClickPayment(savedCard.getMaskedCard());
                    } else if (isTwoClickMode()) {

                        CardTokenRequest request = new CardTokenRequest();
                        request.setSavedTokenId(savedCard.getSavedTokenId());
                        request.setCardCVV(cvv);
                        if (promo != null && promo.getDiscountAmount() > 0) {
                            // Calculate discount amount
                            double preDiscountAmount = midtransSDK.getTransactionRequest().getAmount();
                            double discountedAmount = preDiscountAmount - SdkUIFlowUtil.calculateDiscountAmount(promo);
                            request.setGrossAmount(discountedAmount);
                        } else {
                            request.setGrossAmount(midtransSDK.getTransactionRequest().getAmount());
                        }
                        ((CreditDebitCardFlowActivity) getActivity()).twoClickPayment(request);

                    } else {
                        String date = etExpiryDate.getText().toString();
                        String month = date.split("/")[0].trim();
                        String year = "20" + date.split("/")[1].trim();
                        CardTokenRequest cardTokenRequest = new CardTokenRequest(cardNumber, cvv,
                                month, year,
                                midtransSDK.getClientKey());
                        cardTokenRequest.setIsSaved(cbSaveCard.isChecked());
                        cardTokenRequest.setSecure(midtransSDK.getTransactionRequest().isSecureCard());
                        cardTokenRequest.setGrossAmount(midtransSDK.getTransactionRequest().getAmount());
                        cardTokenRequest.setCardType(cardType);


                        //make payment
                        SdkUIFlowUtil.showProgressDialog((AppCompatActivity) getActivity(), false);
                        setPaymentInstallment();
                        ((CreditDebitCardFlowActivity) getActivity()).setSavedCardInfo(cbSaveCard.isChecked(), cardType);
                        if (promo != null && promo.getDiscountAmount() > 0) {
                            // Calculate discount amount
                            double preDiscountAmount = midtransSDK.getTransactionRequest().getAmount();
                            double discountedAmount = preDiscountAmount - SdkUIFlowUtil.calculateDiscountAmount(promo);
                            cardTokenRequest.setGrossAmount(discountedAmount);
                            ((CreditDebitCardFlowActivity) getActivity()).normalPayment(cardTokenRequest);
                        } else {
                            ((CreditDebitCardFlowActivity) getActivity()).normalPayment(cardTokenRequest);
                        }
                    }
                }
            }
        });

        imageCvvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
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

        imageBanksPointHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.redeem_bni_title)
                        .setMessage(R.string.redeem_bni_details)
                        .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                dialog.show();
            }
        });

        etCardNo.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Logger.i("textchanged:" + s.length());
                tilCardNo.setError(null);
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    final char c = s.charAt(s.length() - 1);
                    if (space == c) {
                        s.delete(s.length() - 1, s.length());
                    }
                }
                // Insert char where needed.
                if (s.length() > 0 && (s.length() % 5) == 0) {
                    char c = s.charAt(s.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf
                            (space)).length <= 3) {
                        s.insert(s.length() - 1, String.valueOf(space));
                    }
                }
                setCardType();
                setBankType();

                // Move to next input
                if (s.length() >= 18 && cardType.equals(getString(R.string.amex))) {
                    if (s.length() == 19) {
                        s.delete(s.length() - 1, s.length());
                    }
                    etExpiryDate.requestFocus();
                } else if (s.length() == 19) {
                    etExpiryDate.requestFocus();
                }
            }
        });

        etExpiryDate.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String input = s.toString();
                        if (s.length() == 4) {
                            if (lastExpDate.length() > s.length()) {
                                try {
                                    int month = Integer.parseInt(input.substring(0, 2));
                                    if (month <= 12) {
                                        etExpiryDate.setText(etExpiryDate.getText().toString().substring(0, 1));
                                        etExpiryDate.setSelection(etExpiryDate.getText().toString().length());
                                    } else {
                                        etExpiryDate.setText("");
                                        etExpiryDate.setSelection(etExpiryDate.getText().toString().length());
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
                                        etExpiryDate.setText(getString(R.string.expiry_month_format, etExpiryDate.getText().toString()));
                                        etExpiryDate.setSelection(etExpiryDate.getText().toString().length());
                                    } else {
                                        etExpiryDate.setText(getString(R.string.expiry_month_int_format, UiKitConstants.MONTH_COUNT));
                                        etExpiryDate.setSelection(etExpiryDate.getText().toString().length());
                                    }

                                } catch (Exception exception) {
                                    Logger.e(exception.toString());
                                }
                            }
                        } else if (s.length() == 1) {
                            try {
                                int month = Integer.parseInt(input);
                                if (month > 1) {
                                    etExpiryDate.setText(getString(R.string.expiry_month_single_digit_format, etExpiryDate.getText().toString()));
                                    etExpiryDate.setSelection(etExpiryDate.getText().toString().length());
                                }
                            } catch (Exception exception) {
                                Logger.e(exception.toString());
                            }
                        }
                        lastExpDate = etExpiryDate.getText().toString();

                        // Move to next input
                        if (s.length() == 7) {
                            etCvv.requestFocus();
                        }
                    }
                }
        );
    }

    private void initPromoUsingPromoEngine() {
        if (midtransSDK.getTransactionRequest().isPromoEnabled()
                && midtransSDK.getPromoResponses() != null
                && !midtransSDK.getPromoResponses().isEmpty()) {
            String cardNumber = etCardNo.getText().toString();
            if (TextUtils.isEmpty(cardNumber)) {
                promoLogoBtn.setVisibility(View.GONE);
                promoLogoBtn.setOnClickListener(null);
            } else if (cardNumber.length() < 7) {
                promoLogoBtn.setVisibility(View.GONE);
                promoLogoBtn.setOnClickListener(null);
            } else {
                String cardBin = cardNumber.trim().replace(" ", "").substring(0, 6);
                final PromoResponse promoResponse = SdkUIFlowUtil.getPromoFromCardBins(midtransSDK.getPromoResponses(), cardBin);
                if (promoResponse != null) {
                    obtainPromo(promoResponse);
                } else {
                    setPromo(null);
                    promoLogoBtn.setVisibility(View.GONE);
                    promoLogoBtn.setOnClickListener(null);
                }
            }
        }
    }

    private boolean isValidPayment() {

        //card bin validation for bin locking and installment
        if (!isCardBinValid()) {
            SdkUIFlowUtil.showToast(getActivity(), getString(R.string.card_bin_invalid));
            return false;
        }

        //installment validation
        if (!((CreditDebitCardFlowActivity) getActivity()).isValidInstallment()) {
            SdkUIFlowUtil.showToast(getActivity(), getString(R.string.installment_required));
            return false;
        }
        return true;
    }

    private void initCheckbox() {
        UIKitCustomSetting uiKitCustomSetting = MidtransSDK.getInstance().getUIKitCustomSetting();
        if (uiKitCustomSetting.isSaveCardChecked()) {
            cbSaveCard.setChecked(true);
        }
    }

    private boolean isCardBinLockingValid() {
        boolean valid = false;
        String cardNumber = etCardNo.getText().toString().trim();
        if (TextUtils.isEmpty(cardNumber)) {
            showInvalidBinLockingStatus(true);

        } else if (cardNumber.length() < 7) {
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


    private boolean isCardBinValid() {
        if (((CreditDebitCardFlowActivity) getActivity()).isWhiteListBinsAvailable()) {
            if (cardNumber != null) {
                String cardBin = cardNumber.replace(" ", "").substring(0, 6);
                if (!((CreditDebitCardFlowActivity) getActivity()).isCardBinValid(cardBin)) {
                    return false;
                }
            }
        }

        return true;
    }


    private void showSwitchSaveCardLayout(boolean show) {
        if (show) {
            layoutSaveCard.setVisibility(View.VISIBLE);
        } else {
            layoutSaveCard.setVisibility(View.GONE);
        }
    }

    private boolean checkBinLockingValidity() {
        if (((CreditDebitCardFlowActivity) getActivity()).isWhiteListBinsAvailable()) {
            if (!isCardBinLockingValid()) {
                return false;
            }
        }
        return true;
    }

    private void showInvalidBinLockingStatus(boolean show) {
        if (show) {
            textInvalidPromoStatus.setVisibility(View.VISIBLE);
        } else {
            textInvalidPromoStatus.setVisibility(View.GONE);
        }
    }

    private void setPaymentInstallment() {
        if (layoutInstallment.getVisibility() == View.VISIBLE) {
            ((CreditDebitCardFlowActivity) getActivity()).setInstallment(installmentCurrentPosition);
        }
    }

    private void initCardInstallment() {

        if (((CreditDebitCardFlowActivity) getActivity()).isInstallmentAvailable()) {
            String cardNumber = etCardNo.getText().toString();
            if (TextUtils.isEmpty(cardNumber)) {
                showInstallmentLayout(false);
            } else if (cardNumber.length() < 7) {
                showInstallmentLayout(false);
            } else if (midtransSDK.getCreditCard() != null
                    && midtransSDK.getCreditCard().getBank() != null
                    && (midtransSDK.getCreditCard().getBank().equalsIgnoreCase(BankType.MAYBANK)
                    || midtransSDK.getCreditCard().getBank().equalsIgnoreCase(BankType.BRI))) {
                showInstallmentLayout(false);
            } else {
                String cleanCardNumber = etCardNo.getText().toString().trim().replace(" ", "").substring(0, 6);
                ArrayList<Integer> installmentTerms = ((CreditDebitCardFlowActivity) getActivity()).getInstallmentTerms(cleanCardNumber);

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

    private void setInstallmentTerm() {
        String installmentTerm;
        int term = ((CreditDebitCardFlowActivity) getActivity()).getInstallmentTerm(installmentCurrentPosition);
        if (term < 1) {
            installmentTerm = getString(R.string.no_installment);
        } else {
            installmentTerm = getString(R.string.formatted_installment_month, term + "");
        }
        textInstallmentTerm.setText(installmentTerm);
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
            ((CreditDebitCardFlowActivity) getActivity()).setInstallment(0);
        }
    }

    private boolean checkCardNumberValidity() {
        if (isTwoClickMode()) {
            return true;
        }

        boolean isValid = true;

        cardNumber = etCardNo.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(cardNumber)) {
            tilCardNo.setError(getString(R.string.validation_message_card_number));
            isValid = false;
        } else {
            tilCardNo.setError(null);
        }

        if (cardNumber.length() < 13 || !SdkUIFlowUtil.isValidCardNumber(cardNumber)) {
            tilCardNo.setError(getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            tilCardNo.setError(null);
        }
        return isValid;
    }

    private boolean checkCardExpiryValidity() {
        if (isTwoClickMode()) {
            return true;
        }
        boolean isValid = true;
        expiryDate = etExpiryDate.getText().toString().trim();
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
            tilExpiry.setError(getString(R.string.validation_message_empty_expiry_date));
            isValid = false;
        } else if (!expiryDate.contains("/")) {
            tilExpiry.setError(getString(R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else if (expDateArray == null || expDateArray.length != 2) {
            tilExpiry.setError(getString(R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else if (expDateArray != null) {
            try {
                expMonth = Integer.parseInt(expDateArray[0]);
            } catch (NumberFormatException e) {
                tilExpiry.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            }
            try {
                expYear = Integer.parseInt(expDateArray[1]);
            } catch (NumberFormatException e) {
                tilExpiry.setError(getString(R.string.validation_message_invalid_expiry_date));
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
                tilExpiry.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else if (expYear == currentYear && currentMonth > expMonth) {
                tilExpiry.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else {
                tilExpiry.setError(null);
            }
        } else {
            tilExpiry.setError(null);
        }
        return isValid;
    }

    private boolean checkCardCVVValidity() {
        if (isOneClickMode()) {
            return true;
        }

        boolean isValid = true;
        cvv = etCvv.getText().toString().trim();
        if (TextUtils.isEmpty(cvv)) {
            tilCvv.setError(getString(R.string.validation_message_cvv));
            isValid = false;

        } else {

            if (cvv.length() < 3) {
                tilCvv.setError(getString(R.string.validation_message_invalid_cvv));
                isValid = false;
            } else {
                tilCvv.setError(null);
            }
        }
        return isValid;
    }

    private boolean checkCardValidity() {
        boolean cardNumberValidity = checkCardNumberValidity();
        boolean cardExpiryValidity = checkCardExpiryValidity();
        boolean cardCVVValidity = checkCardCVVValidity();
        return cardNumberValidity && cardExpiryValidity && cardCVVValidity;
    }

    private void setCardType() {
        // Don't set card type when card number is empty
        String cardNumberText = etCardNo.getText().toString().trim();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 2) {
            logo.setImageResource(0);
            return;
        }

        // Check card type before setting logo
        String cardType = Utils.getCardType(cardNumberText);
        switch (cardType) {
            case Utils.CARD_TYPE_VISA:
                logo.setImageResource(R.drawable.ic_visa);
                break;
            case Utils.CARD_TYPE_MASTERCARD:
                logo.setImageResource(R.drawable.ic_mastercard);
                break;
            case Utils.CARD_TYPE_JCB:
                logo.setImageResource(R.drawable.ic_jcb);
                break;
            case Utils.CARD_TYPE_AMEX:
                logo.setImageResource(R.drawable.ic_amex);
                break;
        }
    }

    private void setBankType() {
        // Don't set card type when card number is empty
        String cardNumberText = etCardNo.getText().toString().trim();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 7) {
            bankLogo.setImageDrawable(null);
            return;
        }

        String cleanCardNumber = cardNumberText.replace(" ", "").substring(0, 6);
        String bank = ((CreditDebitCardFlowActivity) getActivity()).getBankByBin(cleanCardNumber);

        if (bank != null) {
            switch (bank) {
                case BankType.BCA:
                    bankLogo.setImageResource(R.drawable.bca);
                    ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    break;
                case BankType.BNI:
                    bankLogo.setImageResource(R.drawable.bni);
                    ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    break;
                case BankType.BRI:
                    bankLogo.setImageResource(R.drawable.bri);
                    ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    break;
                case BankType.CIMB:
                    bankLogo.setImageResource(R.drawable.cimb);
                    ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    break;
                case BankType.MANDIRI:
                    bankLogo.setImageResource(R.drawable.mandiri);
                    ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    if (((CreditDebitCardFlowActivity) getActivity()).isMandiriDebitCard(cleanCardNumber)) {
                        ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.mandiri_debit_card);
                    }
                    break;
                case BankType.MAYBANK:
                    bankLogo.setImageResource(R.drawable.maybank);
                    ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    break;
                case BankType.BNI_DEBIT_ONLINE:
                    bankLogo.setImageResource(R.drawable.bni);
                    ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.bni_debit_online_card);
                    break;
                default:
                    bankLogo.setImageDrawable(null);
                    ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    break;
            }
        } else {
            bankLogo.setImageDrawable(null);
            ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void fadeIn() {
        formLayout.setAlpha(0);
        ObjectAnimator fadeInAnimation = ObjectAnimator.ofFloat(formLayout, "alpha", 0, 1f);
        fadeInAnimation.setDuration(Constants.FADE_IN_FORM_TIME);
        fadeInAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                payNowBtn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        fadeInAnimation.start();
    }


    private void disableEnableInstallmentButton() {
        if (installmentCurrentPosition == 0 && installmentTotalPositions == 0) {
            buttonDecrease.setEnabled(false);
            buttonIncrease.setEnabled(false);
        } else if (installmentCurrentPosition > 0 && installmentCurrentPosition < installmentTotalPositions) {
            buttonDecrease.setEnabled(true);
            buttonIncrease.setEnabled(true);
        } else if (installmentCurrentPosition > 0 && installmentCurrentPosition == installmentTotalPositions) {
            buttonDecrease.setEnabled(true);
            buttonIncrease.setEnabled(false);
        } else if (installmentCurrentPosition == 0 && installmentCurrentPosition < installmentTotalPositions) {
            buttonDecrease.setEnabled(false);
            buttonIncrease.setEnabled(true);
        }
    }

    public void updateFromScanCardEvent(CreditCardFromScanner creditCardFromScanner) {
        etCardNo.setText(creditCardFromScanner.getCardNumber());
        etCvv.setText(creditCardFromScanner.getCvv());
        etExpiryDate.setText(creditCardFromScanner.getExpired());
    }

    private void onDecreaseTerm() {
        if (installmentCurrentPosition > 0 && installmentCurrentPosition <= installmentTotalPositions) {
            installmentCurrentPosition -= 1;
            setInstallmentTerm();
        }
        disableEnableInstallmentButton();
    }

    private void onIncraseTerm() {
        if (installmentCurrentPosition >= 0 && installmentCurrentPosition < installmentTotalPositions) {
            installmentCurrentPosition += 1;
            setInstallmentTerm();
        }
        disableEnableInstallmentButton();
    }

    public SaveCardRequest getSavedCard() {
        return savedCard;
    }

    public boolean isOneClickMode() {
        return savedCard != null && savedCard.getType() != null && savedCard.getType().equals(getString(R.string.saved_card_one_click));
    }

    public boolean isTwoClickMode() {
        return savedCard != null;
    }

    public PromoResponse getPromo() {
        return promo;
    }

    public void setPromo(PromoResponse promo) {
        this.promo = promo;
    }

    private void obtainPromo(final PromoResponse promoResponse) {
        midtransSDK.obtainPromo(String.valueOf(promoResponse.getId()), midtransSDK.getTransactionRequest().getAmount(), new ObtainPromoCallback() {
            @Override
            public void onSuccess(ObtainPromosResponse response) {
                // Set promo
                setPromo(promoResponse);
                // Set discount token
                CreditDebitCardFlowActivity activity = (CreditDebitCardFlowActivity) getActivity();
                if (activity != null) {
                    double finalAmount = midtransSDK.getTransactionRequest().getAmount()
                            - SdkUIFlowUtil.calculateDiscountAmount(promoResponse);
                    activity.setTextTotalAmount(finalAmount);

                    promoLogoBtn.setVisibility(View.VISIBLE);
                    promoLogoBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                                    .setTitle(R.string.promo_dialog_title)
                                    .setMessage(getString(R.string.promo_dialog_message, Utils.getFormattedAmount(SdkUIFlowUtil.calculateDiscountAmount(promoResponse)), promoResponse.getSponsorName()))
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
            }

            @Override
            public void onFailure(String statusCode, String message) {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.error_obtain_promo), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        obtainPromo(promoResponse);
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.error_obtain_promo), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        obtainPromo(promoResponse);
                    }
                });
            }
        });
    }

    private void initBNIPoints(boolean validCardNumber) {

        ArrayList<String> pointBanks = MidtransSDK.getInstance().getBanksPointEnabled();
        if (validCardNumber && pointBanks != null && !pointBanks.isEmpty()) {
            String cardBin = cardNumber.replace(" ", "").substring(0, 6);
            String bankBin = ((CreditDebitCardFlowActivity) getActivity()).getBankByBin(cardBin);
            if (!TextUtils.isEmpty(bankBin) && bankBin.equals(BankType.BNI)) {
                showBanksPoint(true);
            } else {
                showBanksPoint(false);
            }
        } else {
            showBanksPoint(false);
        }
    }

    private boolean isBanksPointActivated() {
        return layoutBanksPoint.getVisibility() == View.VISIBLE && cbBankPoint.isChecked();

    }

    private void showBanksPoint(boolean show) {
        if (show) {
            layoutBanksPoint.setVisibility(View.VISIBLE);
        } else {
            layoutBanksPoint.setVisibility(View.GONE);
        }
    }

    private void changeDialogButtonColor(AlertDialog alertDialog) {
        if (alertDialog.isShowing()
                && midtransSDK != null
                && midtransSDK.getColorTheme() != null
                && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
        }
    }
}