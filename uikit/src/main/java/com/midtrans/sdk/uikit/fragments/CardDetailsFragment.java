package com.midtrans.sdk.uikit.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CreditCardFromScanner;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.snap.PromoResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.CreditCardFlowActivity;
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
 * Deprecated, please use {@link com.midtrans.sdk.uikit.views.creditcard.details.CreditCardDetailsActivity}
 * Created by rakawm on 3/7/17.
 */
@Deprecated
public class CardDetailsFragment extends Fragment {

    public static final int SCAN_REQUEST_CODE = 101;
    private static final String PARAM_CARD = "card";
    private static final String PARAM_PROMO = "promo";
    private static final String TAG = "CardDetailsFragment";
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

    private SaveCardRequest savedCard;

    private String lastExpDate = "";

    private PromoResponse promo;
    private String discountToken;
    private TextView installmentText;
    private TextView textInstallmentTerm;
    private TextView textInvalidPromoStatus;
    private LinearLayout layoutInstallment;

    private int installmentCurrentPosition, installmentTotalPositions;

    public static CardDetailsFragment newInstance() {
        return new CardDetailsFragment();
    }

    public static CardDetailsFragment newInstance(SaveCardRequest saveCardRequest) {
        CardDetailsFragment cardDetailsFragment = new CardDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAM_CARD, saveCardRequest);
        cardDetailsFragment.setArguments(bundle);
        return cardDetailsFragment;
    }

    public static CardDetailsFragment newInstance(SaveCardRequest saveCardRequest, PromoResponse promo) {
        CardDetailsFragment cardDetailsFragment = new CardDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAM_CARD, saveCardRequest);
        bundle.putSerializable(PARAM_PROMO, promo);
        cardDetailsFragment.setArguments(bundle);
        return cardDetailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_card_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        initScanCard();
        initSaveCardLayout();
        initSaveCardCheckbox();
        initBniPointCheckbox();
        initDelete();
        fetchSavedCardIfAvailable();
        initTheme();
        initCvvHelp();
        initPointHelp();
        initSaveCardHelp();
        initCardNumber();
        initCardExpiry();
        initFocusChanges();
        initInstallmentTermButton();
        initPayNow();
    }

    private void initBniPointCheckbox() {
        cbBankPoint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    showInstallmentLayout(false);
                } else {
                    initCardInstallment();
                }
            }
        });
    }

    private void initPointHelp() {
        imagePointHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.redeem_bni_title)
                        .setMessage(R.string.redeem_bni_details)
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

    private void initDelete() {
        deleteCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                        .setMessage(R.string.card_delete_message)
                        .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                SdkUIFlowUtil.showProgressDialog((AppCompatActivity) getActivity(), getString(R.string.processing_delete), false);
                                SaveCardRequest savedCard = getSavedCard();
                                ((CreditCardFlowActivity) getActivity()).deleteSavedCard(savedCard);
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
        });
    }

    private void initInstallmentTermButton() {
        buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onIncreaseTerm();
            }
        });

        buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDecreaseTerm();
            }
        });
    }

    private void initFocusChanges() {
        cardNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    checkCardNumberValidity();
                    checkBinLockingValidity();
                    initCardInstallment();
                    initBNIPoints(checkCardNumberValidity());
                    initPromoUsingPromoEngine();
                }
            }
        });
        cardExpiry.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    checkCardExpiryValidity();
                }
            }
        });
        cardCvv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    checkCardCVVValidity();
                }
            }
        });
    }

    private void initScanCard() {
        final MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getExternalScanner() != null) {
            // Set background color for scan button
            if (midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                scanCardBtn.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            }
            scanCardBtn.setVisibility(View.VISIBLE);
            scanCardBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start scanning
                    midtransSDK.getExternalScanner().startScan(getActivity(), SCAN_REQUEST_CODE);
                }
            });
        } else {
            scanCardBtn.setVisibility(View.GONE);
        }
    }

    private void initSaveCardLayout() {
        if (((CreditCardFlowActivity) getActivity()).isClickPayment()) {
            showSavedCardlayout(true);
        } else {
            showSavedCardlayout(false);
        }
    }

    private void showSavedCardlayout(boolean show) {
        if (show) {
            layoutSaveCard.setVisibility(View.VISIBLE);
            saveCardCheckBox.setChecked(true);
        } else {
            layoutSaveCard.setVisibility(View.GONE);
            saveCardCheckBox.setChecked(false);
        }
    }

    private void initSaveCardCheckbox() {
        UIKitCustomSetting uiKitCustomSetting = MidtransSDK.getInstance().getUIKitCustomSetting();
        if (uiKitCustomSetting.isSaveCardChecked()) {
            saveCardCheckBox.setChecked(true);
        }

        saveCardCheckBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkCardValidity();
            }
        });
    }

    private void initPayNow() {
        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK midtransSDK = MidtransSDK.getInstance();
                setPaymentInstallment();

                if (checkCardValidity()) {
                    ((CreditCardFlowActivity) getActivity()).setBankPointStatus(isBanksPointActivated());
                    if (isValidPayment()) {
                        if (isOneClickMode()) {
                            ((CreditCardFlowActivity) getActivity()).oneClickPayment(savedCard);
                        } else if (isTwoClickMode()) {
                            CardTokenRequest request = new CardTokenRequest();
                            request.setSavedTokenId(savedCard.getSavedTokenId());
                            request.setCardCVV(cardCvv.getText().toString());
                            if (promo != null && promo.getDiscountAmount() > 0) {
                                // Calculate discount amount
                                double preDiscountAmount = midtransSDK.getTransactionRequest().getAmount();
                                double discountedAmount = preDiscountAmount - SdkUIFlowUtil.calculateDiscountAmount(promo);
                                request.setGrossAmount((long) discountedAmount);
                            } else {
                                request.setGrossAmount((long) midtransSDK.getTransactionRequest().getAmount());
                            }
                            ((CreditCardFlowActivity) getActivity()).twoClickPayment(request);
                        } else {
                            String cardNumberText = cardNumber.getText().toString();
                            String cvvText = cardCvv.getText().toString();
                            String date = cardExpiry.getText().toString();
                            String month = date.split("/")[0].trim();
                            String year = "20" + date.split("/")[1].trim();
                            CardTokenRequest cardTokenRequest = new CardTokenRequest(
                                    cardNumberText,
                                    cvvText,
                                    month,
                                    year,
                                    midtransSDK.getClientKey());
                            cardTokenRequest.setIsSaved(saveCardCheckBox.isChecked());
                            cardTokenRequest.setSecure(((CreditCardFlowActivity) getActivity()).isSecurePayment());
                            cardTokenRequest.setGrossAmount((long) midtransSDK.getTransactionRequest().getAmount());

                            SdkUIFlowUtil.showProgressDialog((AppCompatActivity) getActivity(), false);
                            ((CreditCardFlowActivity) getActivity()).setSavedCardInfo(saveCardCheckBox.isChecked(), "");
                            if (promo != null && promo.getDiscountAmount() > 0) {
                                // Calculate discount amount
                                double preDiscountAmount = midtransSDK.getTransactionRequest().getAmount();
                                double discountedAmount = preDiscountAmount - SdkUIFlowUtil.calculateDiscountAmount(promo);
                                cardTokenRequest.setGrossAmount((long) discountedAmount);
                                ((CreditCardFlowActivity) getActivity()).normalPayment(cardTokenRequest);
                            } else {
                                ((CreditCardFlowActivity) getActivity()).normalPayment(cardTokenRequest);
                            }
                        }
                    }
                }
            }
        });
    }


    private boolean isBanksPointActivated() {
        return layoutBanksPoint.getVisibility() == View.VISIBLE && cbBankPoint.isChecked();
    }

    private void fetchSavedCardIfAvailable() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();

        if (getArguments() != null) {
            savedCard = (SaveCardRequest) getArguments().getSerializable(PARAM_CARD);
            promo = (PromoResponse) getArguments().getSerializable(PARAM_PROMO);
            if (savedCard != null) {
                showDeleteIcon();

                String cardType = Utils.getCardType(savedCard.getMaskedCard());
                if (!TextUtils.isEmpty(cardType)) {
                    String cardBin = savedCard.getMaskedCard().substring(0, 4);
                    String title = cardType + "-" + cardBin;
                    ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(title);
                }

                layoutSaveCard.setVisibility(View.GONE);
                scanCardBtn.setVisibility(View.GONE);

                cardExpiry.setInputType(InputType.TYPE_CLASS_TEXT);
                InputFilter[] filterArray = new InputFilter[1];
                filterArray[0] = new InputFilter.LengthFilter(20);
                cardExpiry.setFilters(filterArray);
                cardNumber.setEnabled(false);
                cardExpiry.setEnabled(false);
                cardCvv.requestFocus();
                cardNumber.setText(SdkUIFlowUtil.getMaskedCardNumber(savedCard.getMaskedCard()));
                cardExpiry.setText(SdkUIFlowUtil.getMaskedExpDate());

                if (promo != null && promo.getDiscountAmount() > 0) {
                    obtainPromo(promo);
                }

                if (isOneClickMode()) {
                    cardCvv.setInputType(InputType.TYPE_CLASS_TEXT);
                    cardCvv.setFilters(filterArray);
                    cardCvv.setText(SdkUIFlowUtil.getMaskedCardCvv());
                    cardCvv.setEnabled(false);

                    ((CreditCardFlowActivity) getActivity()).setInstallmentAvailableStatus(false);
                } else {
                    initCardInstallment();
                    initBNIPoints(checkCardNumberValidity());
                }

                setBankType();
                setCardType();
            }
        }
    }

    private void bindViews(View view) {
        deleteCardBtn = (FancyButton) view.findViewById(R.id.image_saved_card_delete);
        cardNumberContainer = (TextInputLayout) view.findViewById(R.id.til_card_no);
        cardExpiryContainer = (TextInputLayout) view.findViewById(R.id.exp_til);
        cardCvvNumberContainer = (TextInputLayout) view.findViewById(R.id.cvv_til);
        cardNumber = (TextInputEditText) view.findViewById(R.id.et_card_no);
        cardExpiry = (TextInputEditText) view.findViewById(R.id.et_exp_date);
        cardCvv = (TextInputEditText) view.findViewById(R.id.et_cvv);
        cardLogo = (ImageView) view.findViewById(R.id.payment_card_logo);
        bankLogo = (ImageView) view.findViewById(R.id.bank_logo);
        imageCvvHelp = (ImageButton) view.findViewById(R.id.image_cvv_help);
        imageSaveCardHelp = (ImageButton) view.findViewById(R.id.help_save_card);
        saveCardCheckBox = (AppCompatCheckBox) view.findViewById(R.id.cb_store_card);
        scanCardBtn = (FancyButton) view.findViewById(R.id.scan_card);
        layoutSaveCard = (RelativeLayout) view.findViewById(R.id.layout_save_card_detail);
        payNowBtn = (FancyButton) view.findViewById(R.id.btn_pay_now);
        layoutInstallment = (LinearLayout) view.findViewById(R.id.layout_installment);
        buttonIncrease = (FancyButton) view.findViewById(R.id.button_installment_increase);
        buttonDecrease = (FancyButton) view.findViewById(R.id.button_installment_decrease);
        textInstallmentTerm = (TextView) view.findViewById(R.id.text_installment_term);
        textInvalidPromoStatus = (DefaultTextView) view.findViewById(R.id.text_offer_status_not_applied);
        buttonIncrease = (FancyButton) view.findViewById(R.id.button_installment_increase);
        buttonDecrease = (FancyButton) view.findViewById(R.id.button_installment_decrease);
        textInstallmentTerm = (TextView) view.findViewById(R.id.text_installment_term);
        textInvalidPromoStatus = (DefaultTextView) view.findViewById(R.id.text_offer_status_not_applied);
        promoLogoBtn = (AspectRatioImageView) view.findViewById(R.id.promo_logo);
        installmentText = (TextView) view.findViewById(R.id.installment_title);
        cbBankPoint = (AppCompatCheckBox) view.findViewById(R.id.cb_bni_point);
        layoutBanksPoint = (RelativeLayout) view.findViewById(R.id.layout_bni_point);
        imagePointHelp = (ImageButton) view.findViewById(R.id.image_bni_help);
    }

    private void initTheme() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        try {
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
                    buttonIncrease.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                    buttonDecrease.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
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

            deleteCardBtn.setBorderColor(ContextCompat.getColor(getContext(), R.color.delete_color));
            deleteCardBtn.setTextColor(ContextCompat.getColor(getContext(), R.color.delete_color));
            deleteCardBtn.setIconColorFilter(ContextCompat.getColor(getContext(), R.color.delete_color));

        } catch (Exception e) {
            Logger.e(TAG, "rendering theme:" + e.getMessage());
        }
    }

    private void initCvvHelp() {
        imageCvvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }

    private void initSaveCardHelp() {
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
    }

    private void changeDialogButtonColor(AlertDialog alertDialog) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (alertDialog.isShowing()
                && midtransSDK != null
                && midtransSDK.getColorTheme() != null
                && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
        }
    }

    private void initCardNumber() {
        cardNumber.addTextChangedListener(new TextWatcher() {
            private static final char SPACE_CHAR = ' ';

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Logger.i("textchanged:" + s.length());
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

    private boolean checkCardCVVValidity() {
        if (isOneClickMode()) {
            return true;
        }

        boolean isValid = true;
        String cvv = cardCvv.getText().toString().trim();
        if (TextUtils.isEmpty(cvv)) {
            cardCvvNumberContainer.setError(getString(R.string.validation_message_cvv));
            isValid = false;

        } else {

            if (cvv.length() < 3) {
                cardCvvNumberContainer.setError(getString(R.string.validation_message_invalid_cvv));
                isValid = false;
            } else {
                cardCvvNumberContainer.setError(null);
            }
        }
        return isValid;
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
        return isValid;
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

    private void setBankType() {
        // Don't set card type when card number is empty
        String cardNumberText = cardNumber.getText().toString().trim();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 7) {
            bankLogo.setImageDrawable(null);
            return;
        }

        String cleanCardNumber = cardNumberText.replace(" ", "").substring(0, 6);
        String bank = ((CreditCardFlowActivity) getActivity()).getBankByBin(cleanCardNumber);

        if (bank != null) {
            switch (bank) {
                case BankType.BCA:
                    bankLogo.setImageResource(R.drawable.bca);
                    ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    break;
                case BankType.BNI:
                    bankLogo.setImageResource(R.drawable.bni);
                    ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    break;
                case BankType.BRI:
                    bankLogo.setImageResource(R.drawable.bri);
                    ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    break;
                case BankType.CIMB:
                    bankLogo.setImageResource(R.drawable.cimb);
                    ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    break;
                case BankType.MANDIRI:
                    bankLogo.setImageResource(R.drawable.mandiri);
                    ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    if (((CreditCardFlowActivity) getActivity()).isMandiriDebitCard(cleanCardNumber)) {
                        ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.mandiri_debit_card);
                    }
                    break;
                case BankType.MAYBANK:
                    bankLogo.setImageResource(R.drawable.maybank);
                    ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    break;
                case BankType.BNI_DEBIT_ONLINE:
                    bankLogo.setImageResource(R.drawable.bni);
                    ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.bni_debit_online_card);
                    break;
                default:
                    bankLogo.setImageDrawable(null);
                    ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
                    break;
            }
        } else {
            bankLogo.setImageDrawable(null);
            ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(R.string.card_details);
        }
    }

    private void initCardExpiry() {
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
                                cardExpiry.setText(getString(R.string.expiry_month_int_format, UiKitConstants.MONTH_COUNT));
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
    }

    private boolean checkCardExpiryValidity() {
        if (isTwoClickMode()) {
            return true;
        }

        boolean isValid = true;
        String expiryDate = cardExpiry.getText().toString().trim();
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
            cardExpiryContainer.setError(getString(R.string.validation_message_empty_expiry_date));
            isValid = false;
        } else if (!expiryDate.contains("/")) {
            cardExpiryContainer.setError(getString(R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else if (expDateArray.length != 2) {
            cardExpiryContainer.setError(getString(R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else {
            try {
                expMonth = Integer.parseInt(expDateArray[0]);
            } catch (NumberFormatException e) {
                cardExpiryContainer.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            }
            try {
                expYear = Integer.parseInt(expDateArray[1]);
            } catch (NumberFormatException e) {
                cardExpiryContainer.setError(getString(R.string.validation_message_invalid_expiry_date));
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
                cardExpiryContainer.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else if (expYear == currentYear && currentMonth > expMonth) {
                cardExpiryContainer.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else {
                cardExpiryContainer.setError(null);
            }
        }
        return isValid;
    }

    public boolean isOneClickMode() {
        return savedCard != null && savedCard.getType() != null && savedCard.getType().equals(getString(R.string.saved_card_one_click));
    }

    public boolean isTwoClickMode() {
        return savedCard != null;
    }

    private boolean checkCardValidity() {
        boolean cardNumberValidity = checkCardNumberValidity();
        boolean cardExpiryValidity = checkCardExpiryValidity();
        boolean cardCVVValidity = checkCardCVVValidity();
        return cardNumberValidity && cardExpiryValidity && cardCVVValidity;
    }

    private boolean isValidPayment() {
        //card bin validation for bin locking and installment
        if (!isCardBinValid()) {
            SdkUIFlowUtil.showToast(getActivity(), getString(R.string.card_bin_invalid));
            return false;
        }

        //installment validation
        if (!((CreditCardFlowActivity) getActivity()).isValidInstallment()) {
            SdkUIFlowUtil.showToast(getActivity(), getString(R.string.installment_required));
            return false;
        }
        return true;
    }

    private boolean isCardBinValid() {
        if (((CreditCardFlowActivity) getActivity()).isWhiteListBinsAvailable()) {
            String cardNumberText = cardNumber.getText().toString();
            String cardBin = cardNumberText.replace(" ", "").substring(0, 6);
            if (!((CreditCardFlowActivity) getActivity()).isCardBinValid(cardBin)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkBinLockingValidity() {
        if (((CreditCardFlowActivity) getActivity()).isWhiteListBinsAvailable()) {
            if (!isCardBinLockingValid()) {
                return false;
            }
        }
        return true;
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

    private void initCardInstallment() {
        if (((CreditCardFlowActivity) getActivity()).isInstallmentAvailable()) {
            String cardNumberText = cardNumber.getText().toString();
            if (TextUtils.isEmpty(cardNumberText)) {
                showInstallmentLayout(false);
            } else if (cardNumber.length() < 7) {
                showInstallmentLayout(false);
            } else if (MidtransSDK.getInstance().getCreditCard() == null) {
                showInstallmentLayout(false);
            } else {
                String cleanCardNumber = cardNumber.getText().toString().trim().replace(" ", "").substring(0, 6);
                ArrayList<Integer> installmentTerms = ((CreditCardFlowActivity) getActivity()).getInstallmentTerms(cleanCardNumber);

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
        int term = ((CreditCardFlowActivity) getActivity()).getInstallmentTerm(installmentCurrentPosition);
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
            ((CreditCardFlowActivity) getActivity()).setInstallment(0);
        }
    }

    private void initPromoUsingPromoEngine() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK.getTransactionRequest().isPromoEnabled()
                && midtransSDK.getPromoResponses() != null
                && !midtransSDK.getPromoResponses().isEmpty()) {
            String cardNumberText = cardNumber.getText().toString();
            if (TextUtils.isEmpty(cardNumberText)) {
                promoLogoBtn.setVisibility(View.GONE);
                promoLogoBtn.setOnClickListener(null);
            } else if (cardNumber.length() < 7) {
                promoLogoBtn.setVisibility(View.GONE);
                promoLogoBtn.setOnClickListener(null);
            } else {
                String cardBin = cardNumberText.trim().replace(" ", "").substring(0, 6);
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

    private void obtainPromo(final PromoResponse promoResponse) {
        final MidtransSDK midtransSDK = MidtransSDK.getInstance();
    }

    public void setPromo(PromoResponse promo) {
        this.promo = promo;
    }

    private void onDecreaseTerm() {
        if (installmentCurrentPosition > 0 && installmentCurrentPosition <= installmentTotalPositions) {
            installmentCurrentPosition -= 1;
            setInstallmentTerm();
            changeBniPointVisibility();
        }
        disableEnableInstallmentButton();
    }

    private void onIncreaseTerm() {
        if (installmentCurrentPosition >= 0 && installmentCurrentPosition < installmentTotalPositions) {
            installmentCurrentPosition += 1;
            setInstallmentTerm();
            changeBniPointVisibility();
        }
        disableEnableInstallmentButton();
    }

    private void changeBniPointVisibility() {
        if (installmentCurrentPosition == 0) {
            initBNIPoints(true);
        } else {
            showBanksPoint(false);
        }
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
        cardNumber.setText(creditCardFromScanner.getCardNumber());
        cardCvv.setText(creditCardFromScanner.getCvv());
        cardExpiry.setText(creditCardFromScanner.getExpired());
    }

    private void showDeleteIcon() {
        deleteCardBtn.setVisibility(View.VISIBLE);
    }

    private void setPaymentInstallment() {
        if (layoutInstallment.getVisibility() == View.VISIBLE && installmentCurrentPosition > 0) {
            ((CreditCardFlowActivity) getActivity()).setInstallment(installmentCurrentPosition);
        }
    }

    public SaveCardRequest getSavedCard() {
        return savedCard;
    }

    private void initBNIPoints(boolean validCardNumber) {

        ArrayList<String> pointBanks = MidtransSDK.getInstance().getBanksPointEnabled();
        if (validCardNumber && pointBanks != null && !pointBanks.isEmpty()) {
            String cardNumberText = cardNumber.getText().toString();
            String cardBin = cardNumberText.replace(" ", "").substring(0, 6);
            String bankBin = ((CreditCardFlowActivity) getActivity()).getBankByBin(cardBin);
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
}
