package com.midtrans.sdk.uikit.fragments;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.BankDetail;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.CreditCardFromScanner;
import com.midtrans.sdk.corekit.models.OffersListModel;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.OffersActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.MidtransDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ankit on 12/8/15.
 */
public class OffersAddCardDetailsFragment extends Fragment {
    private final String MONTH = "Month";
    int currentPosition, totalPositions;
    private TextView textViewTitleOffers = null;
    private TextView textViewTitleCardDetails = null;
    private TextView textViewOfferName = null;
    private TextView textViewOfferApplied = null;
    private TextView textViewOfferNotApplied = null;
    private LinearLayout layoutOfferStatus = null;
    private Button scanCardBtn = null;
    private int offerPosition = 0;
    private String offerName = null;
    private String offerType = null;
    private ImageView imageViewPlus = null;
    private ImageView imageViewMinus = null;
    private TextView textViewInstalment = null;
    private LinearLayout layoutPayWithInstalment = null;
    private String lastExpDate = "";
    private TextInputLayout tilCardNo;
    private EditText etCardNo;
    private EditText etCvv;
    private EditText etExpiryDate;
    private CheckBox cbStoreCard;
    private ImageView questionImg;
    private Button payNowBtn;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String[] expDateArray;
    private int expMonth;
    private int expYear;
    private MidtransSDK midtransSDK;
    private UserDetail userDetail;
    private ArrayList<BankDetail> bankDetails;
    private String cardType = "";
    private boolean isInstalment = false;
    private RelativeLayout formLayout;
    private ArrayList<String> bins = new ArrayList<>();

    public OffersAddCardDetailsFragment() {
    }

    public static OffersAddCardDetailsFragment newInstance(int position, String offerName, String
            offerType) {
        OffersAddCardDetailsFragment fragment = new OffersAddCardDetailsFragment();
        Bundle data = new Bundle();
        data.putInt(OffersActivity.OFFER_POSITION, position);
        data.putString(OffersActivity.OFFER_NAME, offerName);
        data.putString(OffersActivity.OFFER_TYPE, offerType);
        fragment.setArguments(data);
        return fragment;
    }

    public ArrayList<String> getBins() {
        return bins;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        midtransSDK = ((OffersActivity) getActivity()).getMidtransSDK();
        userDetail = ((OffersActivity) getActivity()).getUserDetail();
        bankDetails = ((OffersActivity) getActivity()).getBankDetails();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers_add_card, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //retrieve data from bundle.
        Bundle data = getArguments();
        offerPosition = data.getInt(OffersActivity.OFFER_POSITION);
        offerName = data.getString(OffersActivity.OFFER_NAME);
        offerType = data.getString(OffersActivity.OFFER_TYPE);
        initialiseView(view);
        ((OffersActivity) getActivity()).getBtnMorph().setVisibility(View.GONE);
        fadeIn();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initialiseView(View view) {
        if (view != null && getActivity() != null) {
            textViewTitleOffers = (TextView) getActivity().findViewById(R.id.text_title);
            textViewTitleCardDetails = (TextView) getActivity().findViewById(R.id.text_title_card_details);
            textViewOfferName = (TextView) getActivity().findViewById(R.id.text_title_offer_name);

            setToolbar();

            textViewOfferApplied = (TextView) view.findViewById(R.id.text_offer_status_applied);
            textViewOfferNotApplied = (TextView) view.findViewById(R.id.text_offer_status_not_applied);
            layoutOfferStatus = (LinearLayout) view.findViewById(R.id.layout_offer_status);

            imageViewPlus = (ImageView) view.findViewById(R.id.img_plus);
            imageViewMinus = (ImageView) view.findViewById(R.id.img_minus);
            textViewInstalment = (TextView) view.findViewById(R.id.text_instalment);
            layoutPayWithInstalment = (LinearLayout) view.findViewById(R.id.layout_pay_with_instalments);
            scanCardBtn = (Button) view.findViewById(R.id.scan_card);

            if (offerType.equalsIgnoreCase(OffersActivity.OFFER_TYPE_INSTALMENTS)) {
                hideOrShowPayWithInstalment(true);
                isInstalment = true;
            } else {
                hideOrShowPayWithInstalment(false);
                isInstalment = false;
            }

            hideOrShowOfferStatus(false, false);

            bindCreditCardView(view);

            imageViewMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onMinusClicked();
                }
            });

            imageViewPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPlusClicked();
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
                            midtransSDK.getExternalScanner().startScan(getActivity(), OffersActivity.SCAN_REQUEST_CODE);
                        }
                    });
                } else {
                    scanCardBtn.setVisibility(View.GONE);
                }
                if (midtransSDK.getTransactionRequest().getCardClickType().equals(getString(R.string.card_click_type_none))) {
                    cbStoreCard.setVisibility(View.GONE);
                    questionImg.setVisibility(View.GONE);
                } else {
                    cbStoreCard.setVisibility(View.VISIBLE);
                    questionImg.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setToolbar() {
        textViewTitleOffers.setVisibility(View.GONE);
        textViewTitleCardDetails.setVisibility(View.VISIBLE);
        textViewOfferName.setVisibility(View.VISIBLE);
        textViewOfferName.setText(offerName);

        textViewTitleCardDetails.setText(getResources().getString(R.string.card_details));
    }

    private void hideOrShowPayWithInstalment(boolean isShowLayout) {
        if (isShowLayout) {
            layoutPayWithInstalment.setVisibility(View.VISIBLE);
            showDuration();
        } else {
            layoutPayWithInstalment.setVisibility(View.GONE);
        }
    }

    private void showDuration() {

        ArrayList<OffersListModel> offersList = new ArrayList<>();

        if (((OffersActivity) getActivity()).offersListModels != null || !((OffersActivity) getActivity()).offersListModels.isEmpty()) {
            offersList.addAll(((OffersActivity) getActivity()).offersListModels);
            if (offersList.get(offerPosition).getDuration() != null || !offersList.get(offerPosition).getDuration().isEmpty()) {
                currentPosition = 0;
                totalPositions = offersList.get(offerPosition).getDuration().size() - 1;
                textViewInstalment.setText(getString(R.string.formatted_month, offersList.get(offerPosition).getDuration().get(0)));
                disableEnableMinusPlus();
            }
        }
    }

    private void disableEnableMinusPlus() {

        Logger.i("Positions: " + currentPosition + "----" + totalPositions);

        if (currentPosition == 0 && totalPositions == 0) {
            imageViewMinus.setEnabled(false);
            imageViewPlus.setEnabled(false);
        } else if (currentPosition > 0 && currentPosition < totalPositions) {
            imageViewMinus.setEnabled(true);
            imageViewPlus.setEnabled(true);
        } else if (currentPosition > 0 && currentPosition == totalPositions) {
            imageViewMinus.setEnabled(true);
            imageViewPlus.setEnabled(false);
        } else if (currentPosition == 0 && currentPosition < totalPositions) {
            imageViewMinus.setEnabled(false);
            imageViewPlus.setEnabled(true);
        }
    }

    private void onMinusClicked() {
        if (currentPosition > 0 && currentPosition <= totalPositions) {
            currentPosition = currentPosition - 1;
            textViewInstalment.setText(getString(R.string.formatted_month, ((OffersActivity) getActivity()).offersListModels.get(offerPosition).getDuration().get(currentPosition)));
        }
        disableEnableMinusPlus();
    }

    private void onPlusClicked() {
        if (currentPosition >= 0 && currentPosition < totalPositions) {
            currentPosition = currentPosition + 1;
            textViewInstalment.setText(getString(R.string.formatted_month, ((OffersActivity) getActivity()).offersListModels.get(offerPosition).getDuration().get(currentPosition)));
        }
        disableEnableMinusPlus();
    }

    private void hideOrShowOfferStatus(boolean isShowLayout, boolean isOfferApplied) {
        if (isShowLayout) {
            layoutOfferStatus.setVisibility(View.VISIBLE);
            if (isOfferApplied) {
                textViewOfferApplied.setVisibility(View.VISIBLE);
                textViewOfferNotApplied.setVisibility(View.GONE);
            } else {
                textViewOfferApplied.setVisibility(View.GONE);
                textViewOfferNotApplied.setVisibility(View.VISIBLE);
            }
        } else {
            layoutOfferStatus.setVisibility(View.GONE);
        }
    }

    private void bindCreditCardView(View view) {
        formLayout = (RelativeLayout) view.findViewById(R.id.form_layout);
        etCardNo = (EditText) view.findViewById(R.id.et_card_no);
        etCvv = (EditText) view.findViewById(R.id.et_cvv);
        etExpiryDate = (EditText) view.findViewById(R.id.et_exp_date);
        cbStoreCard = (CheckBox) view.findViewById(R.id.cb_store_card);
        questionImg = (ImageView) view.findViewById(R.id.image_question);
        payNowBtn = (Button) view.findViewById(R.id.btn_pay_now);
        tilCardNo = (TextInputLayout) view.findViewById(R.id.til_card_no);

        cbStoreCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkCardValidity();
            }
        });

        etCardNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) { //here show status
                Logger.i("onFocus change etCardNo");
                if (!hasFocus) {
                    Logger.i("onFocus change not etCardNo");
                    offerStatusValidation();
                } else {
                    Logger.i("onFocus change has focus etCardNo");
                }
            }
        });

        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCardValidity()) {
                    String date = etExpiryDate.getText().toString();
                    String month = date.split("/")[0];
                    String year = "20" + date.split("/")[1];
                    CardTokenRequest cardTokenRequest = new CardTokenRequest(cardNumber, cvv,
                            month, year,
                            midtransSDK.getClientKey());

                    int instalmentTerm = 0;
                    if (isInstalment) {
                        String duration = textViewInstalment.getText().toString().trim();
                        String durationSplit[] = duration.split(" ");
                        duration = durationSplit[0];
                        try {
                            instalmentTerm = Integer.parseInt(duration);
                        } catch (NumberFormatException ne) {
                            ne.printStackTrace();
                        }
                        cardTokenRequest.setInstalment(isInstalment);
                        cardTokenRequest.setInstalmentTerm(instalmentTerm);
                    }

                    cardTokenRequest.setIsSaved(cbStoreCard.isChecked());
                    cardTokenRequest.setSecure(midtransSDK.getTransactionRequest().isSecureCard());
                    cardTokenRequest.setGrossAmount(midtransSDK.getTransactionRequest()
                            .getAmount());
                    cardTokenRequest.setCardType(cardType);
                    cardTokenRequest.setBins(bins);
                    if (bankDetails != null && !bankDetails.isEmpty()) {
                        String firstSix = cardNumber.substring(0, 6);
                        for (BankDetail bankDetail : bankDetails) {
                            if (bankDetail.getBin().equalsIgnoreCase(firstSix)) {
                                cardTokenRequest.setBank(bankDetail.getIssuing_bank());
                                cardTokenRequest.setCardType(bankDetail.getCard_association());
                                break;
                            }
                        }
                    }
                    //make payment
                    SdkUIFlowUtil.showProgressDialog(getActivity(), false);
                    ((OffersActivity) getActivity()).getToken(cardTokenRequest);
                }
            }
        });
        questionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransDialog midtransDialog = new MidtransDialog(getActivity(), getResources().getDrawable(R.drawable.cvv_dialog_image),
                        getString(R.string.message_cvv), getString(R.string.got_it), "");
                midtransDialog.show();
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
                if (s.length() == 19) {
                    etExpiryDate.requestFocus();
                }
            }
        });

        etExpiryDate.addTextChangedListener(new TextWatcher() {
                                                @Override
                                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                }

                                                @Override
                                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                }

                                                @Override
                                                public void afterTextChanged(Editable s) {
                                                    String input = s.toString();
                                                    if (s.length() == 2 && !lastExpDate.endsWith("/")) {
                                                        int month = Integer.parseInt(input);
                                                        if (month <= 12) {
                                                            etExpiryDate.setText(etExpiryDate.getText().toString() + "/");
                                                            etExpiryDate.setSelection(etExpiryDate.getText().toString().length());
                                                        } else {
                                                            etExpiryDate.setText(Constants.MONTH_COUNT + "/");
                                                            etExpiryDate.setSelection(etExpiryDate.getText().toString().length());
                                                        }
                                                    } else if (s.length() == 2 && lastExpDate.endsWith("/")) {
                                                        int month = Integer.parseInt(input);
                                                        if (month <= 12) {
                                                            etExpiryDate.setText(etExpiryDate.getText().toString().substring(0, 1));
                                                            etExpiryDate.setSelection(etExpiryDate.getText().toString().length());
                                                        } else {
                                                            etExpiryDate.setText("");
                                                            etExpiryDate.setSelection(etExpiryDate.getText().toString().length());
                                                        }
                                                    } else if (s.length() == 1) {
                                                        int month = Integer.parseInt(input);
                                                        if (month > 1) {
                                                            etExpiryDate.setText("0" + etExpiryDate.getText().toString() + "/");
                                                            etExpiryDate.setSelection(etExpiryDate.getText().toString().length());
                                                        }
                                                    }
                                                    lastExpDate = etExpiryDate.getText().toString();

                                                    // Move to next input
                                                    if (s.length() == 5) {
                                                        etCvv.requestFocus();
                                                    }
                                                }
                                            }
        );
    }

    private boolean checkCardValidity() {
        cardNumber = etCardNo.getText().toString().trim().replace(" ", "");
        expiryDate = etExpiryDate.getText().toString().trim();
        cvv = etCvv.getText().toString().trim();
        questionImg.setVisibility(View.VISIBLE);
        boolean isValid = true;
        try {
            expDateArray = expiryDate.split("/");
            Logger.i("expDate:" + expDateArray[0], "" + expDateArray[1]);
        } catch (NullPointerException e) {
            Logger.i("expiry date empty");
        } catch (IndexOutOfBoundsException e) {
            Logger.i("expiry date issue");
        }
        if (TextUtils.isEmpty(cardNumber)) {
            tilCardNo.setError(getString(R.string.validation_message_card_number));
            isValid = false;
        } else {
            tilCardNo.setError(null);
        }
        if (cardNumber.length() < 15 || !SdkUIFlowUtil.isValidCardNumber(cardNumber)) {
            tilCardNo.setError(getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            tilCardNo.setError(null);
        }

        if (TextUtils.isEmpty(expiryDate)) {
            etExpiryDate.setError(getString(R.string.validation_message_empty_expiry_date));
            isValid = false;
        } else if (!expiryDate.contains("/")) {
            etExpiryDate.setError(getString(R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else if (expDateArray == null || expDateArray.length != 2) {
            etExpiryDate.setError(getString(R.string.validation_message_invalid_expiry_date));
            isValid = false;
        } else if (expDateArray != null) {
            try {
                expMonth = Integer.parseInt(expDateArray[0]);
            } catch (NumberFormatException e) {
                etExpiryDate.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            }
            try {
                expYear = Integer.parseInt(expDateArray[1]);
            } catch (NumberFormatException e) {
                etExpiryDate.setError(getString(R.string.validation_message_invalid_expiry_date));
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
                etExpiryDate.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else if (expYear == currentYear && currentMonth > expMonth) {
                etExpiryDate.setError(getString(R.string.validation_message_invalid_expiry_date));
                isValid = false;
            } else {
                etExpiryDate.setError(null);
            }
        } else {
            etExpiryDate.setError(null);
        }

        if (TextUtils.isEmpty(cvv)) {
            questionImg.setVisibility(View.GONE);
            etCvv.setError(getString(R.string.validation_message_cvv));
            isValid = false;
        } else {
            if (cvv.length() < 3) {
                questionImg.setVisibility(View.GONE);
                etCvv.setError(getString(R.string.validation_message_invalid_cvv));
                isValid = false;
            } else {
                etCvv.setError(null);
            }
            questionImg.setVisibility(View.VISIBLE);
        }
        return isValid;
    }

    private void setCardType() {
        String cardNo = etCardNo.getText().toString().trim();
        if (cardNo.isEmpty() || cardNo.length() < 2) {
            etCardNo.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return;
        }
        if (cardNo.charAt(0) == '4') {
            Drawable visa = getResources().getDrawable(R.drawable.ic_visa_dark);
            etCardNo.setCompoundDrawablesWithIntrinsicBounds(null, null, visa, null);
            cardType = getString(R.string.visa);
        } else if ((cardNo.charAt(0) == '5') && ((cardNo.charAt(1) == '1') || (cardNo.charAt(1) == '2')
                || (cardNo.charAt(1) == '3') || (cardNo.charAt(1) == '4') || (cardNo.charAt(1) == '5'))) {
            Drawable masterCard = getResources().getDrawable(R.drawable.ic_mastercard);
            etCardNo.setCompoundDrawablesWithIntrinsicBounds(null, null, masterCard, null);
            cardType = getString(R.string.mastercard);

        } else if ((cardNo.charAt(0) == '3') && ((cardNo.charAt(1) == '4') || (cardNo.charAt(1) == '7'))) {
            Drawable amex = getResources().getDrawable(R.drawable.ic_amex_dark);
            etCardNo.setCompoundDrawablesWithIntrinsicBounds(null, null, amex, null);
            cardType = "AMEX";

        } else if (cardNo.startsWith("35") || cardNo.startsWith("2131") || cardNo.startsWith("1800")) {
            Drawable jcb = getResources().getDrawable(R.drawable.ic_jcb);
            etCardNo.setCompoundDrawablesWithIntrinsicBounds(null, null, jcb, null);
            cardType = getString(R.string.jcb);
        } else {
            cardType = "";

        }
    }

    private void offerStatusValidation() {

        String cardNumber = etCardNo.getText().toString().trim();

        if (((OffersActivity) getActivity()).offersListModels != null && !((OffersActivity) getActivity()).offersListModels.isEmpty()) {
            if (!((OffersActivity) getActivity()).offersListModels.get(offerPosition).getBins().isEmpty()) {

                bins.clear();
                bins.addAll(((OffersActivity) getActivity()).offersListModels.get(offerPosition).getBins());

                boolean isMatch = false, isNumberFormatException = false;

                for (int loop = 0; loop < ((OffersActivity) getActivity()).offersListModels.get
                        (offerPosition).getBins().size(); loop++) {
                    String binValue = ((OffersActivity) getActivity()).offersListModels.get
                            (offerPosition).getBins().get(loop);

                    int binInt = 0;
                    try {

                        binInt = Integer.parseInt(binValue);
                        int binValueLength = binValue.length();
                        String matchCard = null;
                        if (!cardNumber.isEmpty()) {
                            matchCard = cardNumber.replace(" ", "");
                            matchCard = matchCard.substring(0, binValueLength);
                        }

                        if (!TextUtils.isEmpty(matchCard) && matchCard.equalsIgnoreCase(binValue)) {
                            Logger.i("matches");
                            isMatch = true;
                            break;
                        }

                    } catch (NumberFormatException ne) {
                        ne.printStackTrace();
                        isNumberFormatException = true;
                    }
                }
                if (isMatch) {
                    hideOrShowOfferStatus(true, true);
                } else {
                    hideOrShowOfferStatus(true, false);
                }
            }
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

    public void updateFromScanCardEvent(CreditCardFromScanner card) {
        etCardNo.setText(card.getCardNumber());
        etCvv.setText(card.getCvv());
        etExpiryDate.setText(card.getExpired());
    }
}
