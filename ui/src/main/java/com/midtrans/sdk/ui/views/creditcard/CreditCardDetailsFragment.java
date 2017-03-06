package com.midtrans.sdk.ui.views.creditcard;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
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

import com.midtrans.sdk.core.models.BankType;
import com.midtrans.sdk.core.models.papi.CardTokenRequest;
import com.midtrans.sdk.core.models.papi.CardTokenResponse;
import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.core.utils.CardUtilities;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.abtracts.BaseFragment;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.Payment;
import com.midtrans.sdk.ui.models.CreditCardDetails;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.utils.CreditCardUtils;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.views.webpayment.PaymentWebActivity;
import com.midtrans.sdk.ui.widgets.AspectRatioImageView;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by ziahaqi on 2/22/17.
 */

public class CreditCardDetailsFragment extends BaseFragment implements CreditCardContract.CreditCardDetailView {

    private static final String ARGS_SAVED_CARD = "args_saved_card";
    private static final String ARGS_PROMO = "args_promo";
    private static final String ARGS_CARD_DETAIL = "args_card_detail";


    private CreditCardContract.Presenter presenter;
    private CreditCardDetails cardDetailModel;


    private SavedToken savedCardToken;
    private MidtransUi midtransUi;

    TextInputLayout tilCardNo, tilCvv, tilExpiry;
    TextView tvInstallmentTerm;
    int installmentCurrentPosition, installmentTotalPositions;
    FancyButton buttonPay;
    private String lastExpDate = "";
    private EditText etCardNumber;
    private EditText etCvv;
    private EditText etExpiryDate;
    private AppCompatCheckBox cbSaveCard;
    private FancyButton buttonIncrease, buttonDecrease;
    private ImageView ivLogo;
    private ImageView ivBankLogo;
    private ImageButton ibCvvHelp;
    private AspectRatioImageView ivPromoLogo;
    private ImageButton ibSaveCardHelp;
    private Button buttonScanCard;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String[] expDateArray;
    private int expMonth;
    private int expYear;
    private String cardType = "";
    private RelativeLayout layoutCardForm;
    private LinearLayout layoutInstallment;
    private RelativeLayout layoutSaveCard;
    private DefaultTextView tvInvalidPromoStatus;
    //        private PromoResponse promo;
    private String discountToken;
    private TextView tvInstallment;
    private int attempt = 1;

//    public static CreditCardDetailsFragment newInstance(SavedCardToken card, PromoResponse promo) {
//        CreditCardDetailsFragment fragment = new CreditCardDetailsFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(ARGS_SAVED_CARD, card);
//        bundle.putSerializable(ARGS_PROMO, promo);
//        fragment.setArguments(bundle);
//        return fragment;
//    }

    public static CreditCardDetailsFragment newInstance(CreditCardDetails cardDetails) {
        CreditCardDetailsFragment fragment = new CreditCardDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_SAVED_CARD, cardDetails);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        presenter = ((CreditCardActivity) getActivity()).getPresenter();
        initProperties();


        return inflater.inflate(R.layout.fragment_card_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        initViewsColor();
        setupView();
        initSavedCardState();
        fadeIn();
    }

    private void initViewsColor() {
        Logger.d(TAG, "prim:");


        setTextColor(tvInstallment);
        setBorderColor(buttonIncrease);
        setBorderColor(buttonDecrease);
        setTextColor(buttonIncrease);
        setTextColor(buttonDecrease);
        filterColor(ibSaveCardHelp);
        filterColor(ibCvvHelp);
        setBackgroundColor(tvInstallmentTerm, Constants.Theme.SECONDARY_COLOR);
        tvInstallmentTerm.getBackground().setAlpha(50);
        setCheckoxStateColor(cbSaveCard);
    }

    private void initProperties() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.cardDetailModel = (CreditCardDetails) bundle.getSerializable(ARGS_CARD_DETAIL);
        }
    }

    private void setupView() {
        initCheckbox();

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

        ibSaveCardHelp.setOnClickListener(new View.OnClickListener() {
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

        etCardNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                if (!hasfocus) {
                    checkCardNumberValidity();
                    checkBinLockingValidity();
                    initCardInstallment();
//                    presenter.initPromoUsingPromoEngine();
//                    initPromoUsingPromoEngine();

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

        if (midtransUi != null && midtransUi.getFontSemiBold() != null) {
            buttonPay.setCustomTextFont(midtransUi.getFontSemiBold());
            // Set background for pay now button
            if (midtransUi.getColorTheme() != null && midtransUi.getColorTheme().getPrimaryColor() != 0) {
                buttonPay.setBackgroundColor(midtransUi.getColorTheme().getPrimaryColor());
            }
            buttonScanCard.setTypeface(Typeface.createFromAsset(getContext().getAssets(), midtransUi.getFontDefault()));
//            if (midtransUi.getExternalScanner() != null) {
//                // Set background color for scan button
//                if (midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
//                    buttonScanCard.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
//                }
//                buttonScanCard.setVisibility(View.VISIBLE);
//                buttonScanCard.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Start scanning
//                        midtransSDK.getExternalScanner().startScan(getActivity(), CreditDebitCardFlowActivity.SCAN_REQUEST_CODE);
//                    }
//                });
//            } else {
//                buttonScanCard.setVisibility(View.GONE);
//            }

            if (savedCardToken == null) {
                showSwitchSaveCardLayout(false);
            } else {
                showSwitchSaveCardLayout(true);
            }
        }

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Track event pay now
//                midtransSDK.trackEvent(AnalyticsEventName.BTN_CONFIRM_PAYMENT);
                if (checkCardValidity()) {

                    if (!isValidPayment()) {
                        return;
                    }
                    UiUtils.showProgressDialog(getActivity(), getString(R.string.processing_payment), false);
                    if (isOneClickMode()) {
                        presenter.oneClickPayment(cardDetailModel.getMaskedCardNumber());
                    } else if (isTwoClicksMode()) {

                        CardTokenRequest request = new CardTokenRequest();
//                        request.setSavedTokenId(savedCard.getSavedTokenId());
//                        request.setCardCVV(cvv);
//                        if (promo != null && promo.getDiscountAmount() > 0) {
//                            // Calculate discount amount
//                            double preDiscountAmount = midtransSDK.getTransactionRequest().getAmount();
//                            double discountedAmount = preDiscountAmount - UiUtils.calculateDiscountAmount(promo);
//                            request.setGrossAmount(discountedAmount);
//                        } else {
//                            request.setGrossAmount(midtransSDK.getTransactionRequest().getAmount());
//                        }
                        setPaymentInstallment();
                        presenter.twoClicksPayment(cardDetailModel, cvv);

                    } else {
                        String date = etExpiryDate.getText().toString();
                        String month = date.split("/")[0].trim();
                        String year = "20" + date.split("/")[1].trim();

//                        CardTokenRequest cardTokenRequest = new CardTokenRequest(cardNumber, cvv,
//                                month, year,
//                                midtransSDK.getClientKey());
//                        cardTokenRequest.setIsSaved(cbSaveCard.isChecked());
//                        cardTokenRequest.setSecure(midtransSDK.getTransactionRequest().isSecureCard());
//                        cardTokenRequest.setGrossAmount(midtransSDK.getTransactionRequest().getAmount());
//                        cardTokenRequest.setCardType(cardType);


                        //make payment
//                        UiUtils.showProgressDialog(getActivity(), false);
//                        setPaymentInstallment();
//                        ((CreditDebitCardFlowActivity) getActivity()).setSavedCardInfo(cbSaveCard.isChecked(), cardType);
//                        if (promo != null && promo.getDiscountAmount() > 0) {
//                            // Calculate discount amount
//                            double preDiscountAmount = midtransSDK.getTransactionRequest().getAmount();
//                            double discountedAmount = preDiscountAmount - UiUtils.calculateDiscountAmount(promo);
//                            cardTokenRequest.setGrossAmount(discountedAmount);
//                            ((CreditDebitCardFlowActivity) getActivity()).normalPayment(cardTokenRequest);
//                        } else {
//                            ((CreditDebitCardFlowActivity) getActivity()).normalPayment(cardTokenRequest);
//                        }
                        setPaymentInstallment();
                        presenter.normalPayment(cardNumber, cvv, month, year, cbSaveCard.isChecked());
                    }
                }
            }
        });

        ibCvvHelp.setOnClickListener(new View.OnClickListener() {
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

        etCardNumber.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Logger.d(TAG, "textchanged:" + s.length());
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
                                    Logger.e(TAG, exception.toString());
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
                                        etExpiryDate.setText(getString(R.string.expiry_month_int_format, Constants.DateTime.MONTH_COUNT));
                                        etExpiryDate.setSelection(etExpiryDate.getText().toString().length());
                                    }

                                } catch (Exception exception) {
                                    Logger.e(TAG, exception.getMessage());
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
                                Logger.e(TAG, exception.toString());
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

    private boolean hasSavedToken() {
        return cardDetailModel != null && cardDetailModel.hasSavedToken();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        midtransUi = MidtransUi.getInstance();
    }

    private void initSavedCardState() {
        Bundle bundle = getArguments();
        if (cardDetailModel != null && cardDetailModel.hasSavedToken()) {
            SavedToken savedCard = cardDetailModel.getSavedToken();
//            promo = (PromoResponse) bundle.getSerializable(ARGS_PROMO);
            if (savedCard != null) {
                Log.i(TAG, "savedcard");
                this.savedCardToken = savedCard;

                if (!MidtransUi.getInstance().isBuiltInTokenStorage()) {
                    ((CreditCardActivity) getActivity()).showDeleteCardIcon(true);
                }

                String cardType = CardUtilities.getCardType(savedCardToken.maskedCard);
                if (!TextUtils.isEmpty(cardType)) {
                    String cardBin = savedCardToken.maskedCard.substring(0, 4);
                    String title = cardType + "-" + cardBin;
                    ((CreditCardActivity) getActivity()).setHeaderTitle(title);
                }

                layoutSaveCard.setVisibility(View.GONE);
                buttonScanCard.setVisibility(View.GONE);

                etExpiryDate.setInputType(InputType.TYPE_CLASS_TEXT);
                InputFilter[] filterArray = new InputFilter[1];
                filterArray[0] = new InputFilter.LengthFilter(20);
                etExpiryDate.setFilters(filterArray);
                etCardNumber.setEnabled(false);
                etExpiryDate.setEnabled(false);
                etCvv.requestFocus();
                etCardNumber.setText(CreditCardUtils.getMaskedCardNumber(savedCard.maskedCard));
                etExpiryDate.setText(CreditCardUtils.getMaskedExpDate());
//                if (promo != null && promo.getDiscountAmount() > 0) {
//                    obtainPromo(promo);
//                }
                if (isOneClickMode()) {
                    etCvv.setInputType(InputType.TYPE_CLASS_TEXT);
                    etCvv.setFilters(filterArray);
                    etCvv.setText(CreditCardUtils.getMaskedCardCvv());
                    etCvv.setEnabled(false);

                    presenter.setInstallmentAvailableStatus(false);

                    //track page cc oneclick
//                    midtransSDK.trackEvent(AnalyticsEventName.PAGE_CREDIT_CARD_DETAILS, MixpanelAnalyticsManager.CARD_MODE_ONE_CLICK);
                } else {
                    initCardInstallment();
                    //track page cc twoclick
//                    midtransSDK.trackEvent(AnalyticsEventName.PAGE_CREDIT_CARD_DETAILS, MixpanelAnalyticsManager.CARD_MODE_TWO_CLICK);
                }

            } else {
                //track page cc detail
//                midtransSDK.trackEvent(AnalyticsEventName.PAGE_CREDIT_CARD_DETAILS, MixpanelAnalyticsManager.CARD_MODE_NORMAL);
            }
        } else {
            //track page cc detail
//            midtransSDK.trackEvent(AnalyticsEventName.PAGE_CREDIT_CARD_DETAILS, MixpanelAnalyticsManager.CARD_MODE_NORMAL);
        }
    }


    @Override
    public void onDestroyView() {
        ((CreditCardActivity) getActivity()).showDeleteCardIcon(false);
        super.onDestroyView();
    }

    private void bindViews(View view) {
        layoutCardForm = (RelativeLayout) view.findViewById(R.id.form_layout);
        layoutInstallment = (LinearLayout) view.findViewById(R.id.layout_installment);
        layoutSaveCard = (RelativeLayout) view.findViewById(R.id.layout_save_card_detail);

        tilCardNo = (TextInputLayout) view.findViewById(R.id.til_card_no);
        tilCvv = (TextInputLayout) view.findViewById(R.id.cvv_til);
        tilExpiry = (TextInputLayout) view.findViewById(R.id.exp_til);

        etCardNumber = (EditText) view.findViewById(R.id.et_card_no);
        etCvv = (EditText) view.findViewById(R.id.et_cvv);
        etExpiryDate = (EditText) view.findViewById(R.id.et_exp_date);
        cbSaveCard = (AppCompatCheckBox) view.findViewById(R.id.cb_store_card);

        ibCvvHelp = (ImageButton) view.findViewById(R.id.image_cvv_help);
        ibSaveCardHelp = (ImageButton) view.findViewById(R.id.help_save_card);

        buttonPay = (FancyButton) view.findViewById(R.id.btn_pay_now);
        buttonScanCard = (Button) view.findViewById(R.id.scan_card);
        buttonIncrease = (FancyButton) view.findViewById(R.id.button_installment_increase);
        buttonDecrease = (FancyButton) view.findViewById(R.id.button_installment_decrease);

        ivLogo = (ImageView) view.findViewById(R.id.payment_card_logo);
        ivBankLogo = (ImageView) view.findViewById(R.id.bank_logo);
        ivPromoLogo = (AspectRatioImageView) view.findViewById(R.id.promo_logo);

        tvInstallmentTerm = (TextView) view.findViewById(R.id.text_installment_term);
        tvInvalidPromoStatus = (DefaultTextView) view.findViewById(R.id.text_offer_status_not_applied);
        tvInstallment = (TextView) view.findViewById(R.id.installment_title);
    }

//    private void initPromoUsingPromoEngine() {
//        if (midtransSDK.getTransactionRequest().isPromoEnabled()
//                && midtransSDK.getPromoResponses() != null
//                && !midtransSDK.getPromoResponses().isEmpty()) {
//            String cardNumber = etCardNumber.getText().toString();
//            if (TextUtils.isEmpty(cardNumber)) {
//                ivPromoLogo.setVisibility(View.GONE);
//                ivPromoLogo.setOnClickListener(null);
//            } else if (cardNumber.length() < 7) {
//                ivPromoLogo.setVisibility(View.GONE);
//                ivPromoLogo.setOnClickListener(null);
//            } else {
//                String cardBin = cardNumber.trim().replace(" ", "").substring(0, 6);
//                final PromoResponse promoResponse = UiUtils.getPromoFromCardBins(midtransSDK.getPromoResponses(), cardBin);
//                if (promoResponse != null) {
//                    obtainPromo(promoResponse);
//                } else {
//                    setPromo(null);
//                    ivPromoLogo.setVisibility(View.GONE);
//                    ivPromoLogo.setOnClickListener(null);
//                }
//            }
//        }
//    }

    private boolean isValidPayment() {

        //card bin validation for bin locking and installment
        if (!isCardBinValid()) {
            UiUtils.showToast(getActivity(), getString(R.string.card_bin_invalid));
            return false;
        }

        //installment validation
        if (!presenter.isValidInstallment()) {
            UiUtils.showToast(getActivity(), getString(R.string.installment_required));
            return false;
        }
        return true;
    }

    private void initCheckbox() {
        if (presenter.isSaveCardChecked()) {
            cbSaveCard.setChecked(true);
        }
    }

    private boolean isCardBinLockingValid() {
        boolean valid = false;
        String cardNumber = etCardNumber.getText().toString().trim();
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
        if (presenter.isWhiteListBinsAvailable()) {
            if (cardNumber != null) {
                String cardBin = cardNumber.replace(" ", "").substring(0, 6);
                if (!presenter.isCardBinValid(cardBin)) {
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
        if (presenter.isWhiteListBinsAvailable()) {
            if (!isCardBinLockingValid()) {
                return false;
            }
        }
        return true;
    }

    private void showInvalidBinLockingStatus(boolean show) {
        if (show) {
            tvInvalidPromoStatus.setVisibility(View.VISIBLE);
        } else {
            tvInvalidPromoStatus.setVisibility(View.GONE);
        }
    }

    private void setPaymentInstallment() {
        if (layoutInstallment.getVisibility() == View.VISIBLE) {
            presenter.setInstallment(installmentCurrentPosition);
        }
    }

    private void initCardInstallment() {
        if (presenter.isInstallmentAvailable()) {
            String cardNumber = etCardNumber.getText().toString();
            if (TextUtils.isEmpty(cardNumber)) {
                showInstallmentLayout(false);
            } else if (cardNumber.length() < 7) {
                showInstallmentLayout(false);
            } else if (!presenter.isBankSupportInstallment()) {
                showInstallmentLayout(false);
            } else {
                String cleanCardNumber = etCardNumber.getText().toString().trim().replace(" ", "").substring(0, 6);

                List<Integer> installmentTerms = presenter.getInstallmentTerms(cleanCardNumber);

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
        int term = presenter.getInstallmentTerm(installmentCurrentPosition);
        if (term < 1) {
            installmentTerm = getString(R.string.no_installment);
        } else {
            installmentTerm = getString(R.string.formatted_installment_month, term + "");
        }
        tvInstallmentTerm.setText(installmentTerm);
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
            presenter.setInstallment(0);
        }
    }

    private boolean checkCardNumberValidity() {
        if (isTwoClicksMode()) {
            return true;
        }

        boolean isValid = true;

        cardNumber = etCardNumber.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(cardNumber)) {
            tilCardNo.setError(getString(R.string.validation_message_card_number));
            isValid = false;
        } else {
            tilCardNo.setError(null);
        }

        if (cardNumber.length() < 13 || !UiUtils.isValidCardNumber(cardNumber)) {
            tilCardNo.setError(getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            tilCardNo.setError(null);
        }
        if (!isValid) {
            //track invalid cc number
//            midtransSDK.trackEvent(AnalyticsEventName.CREDIT_CARD_NUMBER_VALIDATION, MixpanelAnalyticsManager.CARD_MODE_NORMAL);
        }
        return isValid;
    }

    private boolean checkCardExpiryValidity() {
        if (isTwoClicksMode()) {
            return true;
        }
        boolean isValid = true;
        expiryDate = etExpiryDate.getText().toString().trim();
        try {
            expDateArray = expiryDate.split("/");
            expDateArray[0] = expDateArray[0].trim();
            expDateArray[1] = expDateArray[1].trim();
            Logger.d(TAG, "expDate:" + expDateArray[0].trim() + "" + expDateArray[1].trim());
        } catch (NullPointerException e) {
            Logger.d(TAG, "expiry date empty");
        } catch (IndexOutOfBoundsException e) {
            Logger.d(TAG, "expiry date issue");
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
            Logger.d(TAG, "currentMonth:" + currentMonth + ",currentYear:" + currentYear);

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

        if (!isValid) {
            //track invalid cc expiry
//            midtransSDK.trackEvent(AnalyticsEventName.CREDIT_CARD_EXPIRY_VALIDATION, MixpanelAnalyticsManager.CARD_MODE_NORMAL);
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

        if (!isValid) {
            //track invalid cc cvv
//            midtransSDK.trackEvent(AnalyticsEventName.CREDIT_CARD_CVV_VALIDATION, MixpanelAnalyticsManager.CARD_MODE_NORMAL);
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
        String cardNumberText = etCardNumber.getText().toString().trim();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 2) {
            ivLogo.setImageResource(0);
            return;
        }

        // Check card type before setting ivLogo
        String cardType = CardUtilities.getCardType(cardNumberText);
        switch (cardType) {
            case CardUtilities.CARD_TYPE_VISA:
                ivLogo.setImageResource(R.mipmap.ic_visa);
                break;
            case CardUtilities.CARD_TYPE_MASTERCARD:
                ivLogo.setImageResource(R.mipmap.ic_mastercard);
                break;
            case CardUtilities.CARD_TYPE_JCB:
                ivLogo.setImageResource(R.mipmap.ic_jcb);
                break;
            case CardUtilities.CARD_TYPE_AMEX:
                ivLogo.setImageResource(R.mipmap.ic_amex);
                break;
        }
    }

    private void setBankType() {
        // Don't set card type when card number is empty
        String cardNumberText = etCardNumber.getText().toString().trim();
        if (TextUtils.isEmpty(cardNumberText) || cardNumberText.length() < 7) {
            ivBankLogo.setImageDrawable(null);
            return;
        }

        String cleanCardNumber = cardNumberText.replace(" ", "").substring(0, 6);
        String bank = presenter.getBankByBin(cleanCardNumber);

        if (bank != null) {
            switch (bank) {
                case BankType.BCA:
                    ivBankLogo.setImageResource(R.mipmap.ic_bank_bca);
                    break;
                case BankType.BNI:
                    ivBankLogo.setImageResource(R.mipmap.ic_bank_bni);
                    break;
                case BankType.BRI:
                    ivBankLogo.setImageResource(R.mipmap.ic_bank_bri);
                    break;
                case BankType.CIMB:
                    ivBankLogo.setImageResource(R.mipmap.ic_bank_cimb);
                    break;
                case BankType.MANDIRI:
                    ivBankLogo.setImageResource(R.mipmap.ic_bank_mandiri);
                    break;
                case BankType.MAYBANK:
                    ivBankLogo.setImageResource(R.mipmap.ic_bank_mybank);
                    break;
                default:
                    ivBankLogo.setImageDrawable(null);
                    break;
            }
        } else {
            ivBankLogo.setImageDrawable(null);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void fadeIn() {
        layoutCardForm.setAlpha(0);
        ObjectAnimator fadeInAnimation = ObjectAnimator.ofFloat(layoutCardForm, "alpha", 0, 1f);
        fadeInAnimation.setDuration(Constants.Animation.FADE_IN_FORM_TIME);
        fadeInAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                buttonPay.setVisibility(View.VISIBLE);
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


    public boolean isOneClickMode() {
        return (hasSavedToken() && cardDetailModel.isOneclickMode());
    }

    public boolean isTwoClicksMode() {
        return (hasSavedToken() && cardDetailModel.isTwoClicksMode());
    }

//    public PromoResponse getPromo() {
//        return promo;
//    }

//    public void setPromo(PromoResponse promo) {
//        this.promo = promo;
//    }

//    private void obtainPromo(final PromoResponse promoResponse) {
//        midtransSDK.obtainPromo(String.valueOf(promoResponse.getId()), midtransSDK.getTransactionRequest().getAmount(), new ObtainPromoCallback() {
//            @Override
//            public void onSuccess(ObtainPromoResponse response) {
//                // Set promo
//                setPromo(promoResponse);
//                // Set discount token
//                CreditDebitCardFlowActivity activity = (CreditDebitCardFlowActivity) getActivity();
//                if (activity != null) {
//                    activity.setDiscountToken(response.getDiscountToken());
//                    double finalAmount = midtransSDK.getTransactionRequest().getAmount()
//                            - UiUtils.calculateDiscountAmount(promoResponse);
//                    activity.setTextTotalAmount(finalAmount);
//
//                    ivPromoLogo.setVisibility(View.VISIBLE);
//                    ivPromoLogo.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
//                                    .setTitle(R.string.promo_dialog_title)
//                                    .setMessage(getString(R.string.promo_dialog_message, Utils.getFormattedAmount(UiUtils.calculateDiscountAmount(promoResponse)), promoResponse.getSponsorName()))
//                                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialogInterface, int i) {
//                                            dialogInterface.dismiss();
//                                        }
//                                    })
//                                    .create();
//                            alertDialog.show();
//                            changeDialogButtonColor(alertDialog);
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(String message) {
//                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.error_obtain_promo), Snackbar.LENGTH_INDEFINITE);
//                snackbar.setAction(R.string.retry, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        obtainPromo(promoResponse);
//                    }
//                });
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.error_obtain_promo), Snackbar.LENGTH_INDEFINITE);
//                snackbar.setAction(R.string.retry, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        obtainPromo(promoResponse);
//                    }
//                });
//            }
//        });
//    }

    private void changeDialogButtonColor(AlertDialog alertDialog) {
        if (alertDialog.isShowing() && presenter.isPrimaryDarkColorAvailable()) {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            positiveButton.setTextColor(presenter.getPrimaryDarkColor());
        }
    }

    @Override
    public void setPresenter(@NonNull CreditCardContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void onGetCardTokenSuccess(CardTokenResponse response) {
        if (presenter.isSecureCardpayment()) {
            UiUtils.hideProgressDialog();
            Intent intentPaymentWeb = new Intent(getContext(), PaymentWebActivity.class);
            intentPaymentWeb.putExtra(Constants.WebView.WEB_URL, response.redirectUrl);
            intentPaymentWeb.putExtra(Constants.WebView.TYPE, Constants.WebView.TYPE_CREDIT_CARD);
            getActivity().startActivityForResult(intentPaymentWeb, Constants.IntentCode.PAYMENT_WEB_INTENT);
        } else {
            presenter.payUsingCard();
        }
    }


    @Override
    public void onGetCardTokenFailure(CardTokenResponse response) {
        UiUtils.hideProgressDialog();
        UiUtils.showApiFailedMessage(getActivity(), getString(R.string.message_getcard_token_failed));
    }

    @Override
    public void onGetCardTokenError(String message) {
        UiUtils.hideProgressDialog();
        UiUtils.showApiFailedMessage(getActivity(), getString(R.string.message_getcard_token_failed));
    }

    @Override
    public void onCreditCardPaymentError(Throwable throwable) {
        Logger.d(TAG, "cardPayment:error");
        UiUtils.hideProgressDialog();
        UiUtils.showApiFailedMessage(getActivity(), throwable.getMessage());
    }

    @Override
    public void onCreditCardPaymentFailure(CreditCardPaymentResponse response) {
        Logger.d(TAG, "cardPayment:failure");

        UiUtils.hideProgressDialog();
        if (attempt < Constants.MAX_ATTEMPT) {
            attempt += 1;
            UiUtils.showApiFailedMessage(getActivity(), getString(R.string.message_payment_failed));
        } else {
            ((BaseActivity) getActivity()).initPaymentResult(new PaymentResult(response), Payment.Type.CREDIT_CARD);
        }
    }


    @Override
    public void onCreditCardPaymentSuccess(CreditCardPaymentResponse response) {
        Logger.d(TAG, "cardPayment:success");
        UiUtils.hideProgressDialog();
        ((BaseActivity) getActivity()).initPaymentResult(new PaymentResult(response), Payment.Type.CREDIT_CARD);
    }

    @Override
    public String getMaskedCardNumber() {
        if (hasSavedToken()) {
            return savedCardToken.maskedCard;
        }
        return null;
    }

    @Override
    public boolean isSaveEnabled() {
        return cbSaveCard.isChecked();
    }


}
