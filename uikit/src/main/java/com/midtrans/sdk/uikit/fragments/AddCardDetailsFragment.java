package com.midtrans.sdk.uikit.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CreditCardFromScanner;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.CreditDebitCardFlowActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.MidtransDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddCardDetailsFragment extends Fragment {

    private static final String KEY_PAY_BUTTON_EVENT = "Pay";
    private static final String KEY_SCAN_BUTTON_EVENT = "Scan Card";
    private static final String KEY_CHECKBOX_SAVE_CARD_EVENT = "Save Card";
    private static final String ARGS_SAVED_CARD = "args_saved_card";
    private static final String TAG = AddCardDetailsFragment.class.getSimpleName();

    TextInputLayout tilCardNo, tilCvv, tilExpiry;
    TextView textInstallmentTerm;
    int installmentCurrentPosition, installmentTotalPositions;
    private String lastExpDate = "";
    private EditText etCardNo;
    private EditText etCvv;
    private EditText etExpiryDate;
    private SwitchCompat switchSaveCard, switchBNIPoints;
    private Button buttonIncrease, buttonDecrease;
    private ImageView logo;
    private ImageView imageCvvHelp;
    private ImageView imageBniHelp;
    private ImageView imageSaveHelp;
    private Button payNowBtn;
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
    private LinearLayout layoutInstallment, layoutSaveCard, layoutBNIPoints;
    private DefaultTextView textInvalidPromoStatus;
    private SaveCardRequest savedCard;

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
            if (savedCard != null) {
                Log.i(TAG, "savedcard");
                this.savedCard = savedCard;

                if (!MidtransSDK.getInstance().isEnableBuiltInTokenStorage()) {
                    ((CreditDebitCardFlowActivity) getActivity()).showDeleteCardIcon(true);
                }

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
                if (savedCard.getType() != null && savedCard.getType().equals(getString(R.string.saved_card_one_click))) {
                    etCvv.setInputType(InputType.TYPE_CLASS_TEXT);
                    etCvv.setFilters(filterArray);
                    etCvv.setText(SdkUIFlowUtil.getMaskedCardCvv());
                    etCvv.setEnabled(false);
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
        switchSaveCard = (SwitchCompat) view.findViewById(R.id.cb_store_card);
        switchBNIPoints = (SwitchCompat) view.findViewById(R.id.cb_bni_point);
        initCheckbox();
        imageCvvHelp = (ImageView) view.findViewById(R.id.image_cvv_help);
        imageBniHelp = (ImageView) view.findViewById(R.id.image_bni_help);
        imageSaveHelp = (ImageView) view.findViewById(R.id.image_save_card_help);
        payNowBtn = (Button) view.findViewById(R.id.btn_pay_now);
        scanCardBtn = (Button) view.findViewById(R.id.scan_card);
        logo = (ImageView) view.findViewById(R.id.payment_card_logo);
        layoutInstallment = (LinearLayout) view.findViewById(R.id.layout_installment);
        layoutSaveCard = (LinearLayout) view.findViewById(R.id.layout_save_card_detail);
        layoutBNIPoints = (LinearLayout) view.findViewById(R.id.layout_bni_point);
        buttonIncrease = (Button) view.findViewById(R.id.button_installment_increase);
        buttonDecrease = (Button) view.findViewById(R.id.button_installment_decrease);
        textInstallmentTerm = (TextView) view.findViewById(R.id.text_installment_term);
        textInvalidPromoStatus = (DefaultTextView) view.findViewById(R.id.text_offer_status_not_applied);

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

        switchSaveCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkCardValidity();
            }
        });
        etCardNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    boolean validCardNumber = checkCardNumberValidity();
                    checkPromoValidity();
                    initCardInstallment();
                    initBNIPoints(validCardNumber);
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
            payNowBtn.setTypeface(Typeface.createFromAsset(getContext().getAssets(), midtransSDK.getSemiBoldText()));
            scanCardBtn.setTypeface(Typeface.createFromAsset(getContext().getAssets(), midtransSDK.getDefaultText()));
            if (midtransSDK.getExternalScanner() != null) {
                scanCardBtn.setVisibility(View.VISIBLE);
                scanCardBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Track event scan
                        midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(MidtransSDK.getInstance().readAuthenticationToken(), KEY_SCAN_BUTTON_EVENT, CreditDebitCardFlowActivity.PAYMENT_CREDIT_CARD, null);
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

                String authenticationToken = MidtransSDK.getInstance().readAuthenticationToken();
                // Track event pay now
                midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(authenticationToken, KEY_PAY_BUTTON_EVENT, CreditDebitCardFlowActivity.PAYMENT_CREDIT_CARD, null);
                // Track event checkbox save card
                if (switchSaveCard.isChecked()) {
                    midtransSDK.getmMixpanelAnalyticsManager().trackMixpanel(authenticationToken, KEY_CHECKBOX_SAVE_CARD_EVENT, CreditDebitCardFlowActivity.PAYMENT_CREDIT_CARD, null);
                }

                if (!checkPromoValidity()) {
                    SdkUIFlowUtil.showToast(getActivity(), getString(R.string.message_payment_cannot_proccessed));
                    return;
                }

                if (checkCardValidity()) {
                    ((CreditDebitCardFlowActivity) getActivity()).setBNIPointStatus(isBNIPointActivated());
                    if (isOneClickMode()) {
                        ((CreditDebitCardFlowActivity) getActivity()).oneClickPayment(savedCard.getMaskedCard());
                    } else if (isTwoClickMode()) {

                        CardTokenRequest request = new CardTokenRequest();
                        request.setSavedTokenId(savedCard.getSavedTokenId());
                        request.setCardCVV(cvv);
                        ((CreditDebitCardFlowActivity) getActivity()).twoClickPayment(request);

                    } else {
                        String date = etExpiryDate.getText().toString();
                        String month = date.split("/")[0].trim();
                        String year = "20" + date.split("/")[1].trim();
                        CardTokenRequest cardTokenRequest = new CardTokenRequest(cardNumber, cvv,
                                month, year,
                                midtransSDK.getClientKey());
                        cardTokenRequest.setIsSaved(switchSaveCard.isChecked());
                        cardTokenRequest.setSecure(midtransSDK.getTransactionRequest().isSecureCard());
                        cardTokenRequest.setGrossAmount(midtransSDK.getTransactionRequest().getAmount());
                        cardTokenRequest.setCardType(cardType);


                        //make payment
                        SdkUIFlowUtil.showProgressDialog(getActivity(), false);
                        setPaymentInstallment();
                        ((CreditDebitCardFlowActivity) getActivity()).setSavedCardInfo(switchSaveCard.isChecked(), cardType);
                        ((CreditDebitCardFlowActivity) getActivity()).normalPayment(cardTokenRequest);
                    }
                }
            }
        });

        imageCvvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransDialog midtransDialog = new MidtransDialog(getActivity(), getResources().getDrawable(R.drawable.cvv_dialog_image),
                        getString(R.string.message_cvv), getString(R.string.got_it), "");
                midtransDialog.show();
            }
        });

        imageBniHelp.setOnClickListener(new View.OnClickListener() {
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

        imageSaveHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.save_card_dialog_title)
                        .setMessage(R.string.save_card_dialog_message)
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
                                        etExpiryDate.setText(getString(R.string.expiry_month_int_format, Constants.MONTH_COUNT));
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

    private void initBNIPoints(boolean validCardNumber) {

        ArrayList<String> pointBanks = MidtransSDK.getInstance().getPointBanks();
        if (validCardNumber && pointBanks != null && !pointBanks.isEmpty()) {
            String cardBin = cardNumber.replace(" ", "").substring(0, 6);
            String bankBin = ((CreditDebitCardFlowActivity) getActivity()).getBankByBin(cardBin);
            if (!TextUtils.isEmpty(bankBin) && bankBin.equals(BankType.BNI)) {
                showBNIPointsLayout(true);
            } else {
                showBNIPointsLayout(false);
            }
        } else {
            showBNIPointsLayout(false);
        }
    }

    private boolean isBNIPointActivated() {
        if (layoutBNIPoints.getVisibility() == View.VISIBLE && switchBNIPoints.isChecked()) {
            return true;
        }

        return false;
    }

    private void showBNIPointsLayout(boolean show) {
        if (show) {
            layoutBNIPoints.setVisibility(View.VISIBLE);
        } else {
            layoutBNIPoints.setVisibility(View.GONE);
        }
    }

    private void initCheckbox() {
        UIKitCustomSetting uiKitCustomSetting = midtransSDK.getUIKitCustomSetting();
        if (uiKitCustomSetting.isSaveCardChecked()) {
            switchSaveCard.setChecked(true);
        }
    }

    private boolean isCarBinValid() {
        boolean valid = false;
        String cardNumber = etCardNo.getText().toString().trim();
        if (TextUtils.isEmpty(cardNumber)) {
            showInvalidPromoStatus(true);

        } else if (cardNumber.length() < 7) {
            showInvalidPromoStatus(true);
        } else {
            String cardBin = cardNumber.replace(" ", "").substring(0, 6);
            if (((CreditDebitCardFlowActivity) getActivity()).isCardBinValid(cardBin)) {
                showInvalidPromoStatus(false);
                valid = true;
            } else {
                showInvalidPromoStatus(true);
            }
        }


        return valid;
    }


    private void showSwitchSaveCardLayout(boolean show) {
        if (show) {
            layoutSaveCard.setVisibility(View.VISIBLE);
        } else {
            layoutSaveCard.setVisibility(View.GONE);
        }
    }

    private boolean checkPromoValidity() {
        if (((CreditDebitCardFlowActivity) getActivity()).isPaymentWithPromo()) {
            if (!isCarBinValid()) {
                return false;
            }
        }
        return true;
    }

    private void showInvalidPromoStatus(boolean show) {
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


    public void updateFromScanCardEvent(CreditCardFromScanner creditCardFromScanner) {
        etCardNo.setText(creditCardFromScanner.getCardNumber());
        etCvv.setText(creditCardFromScanner.getCvv());
        etExpiryDate.setText(creditCardFromScanner.getExpired());
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
}