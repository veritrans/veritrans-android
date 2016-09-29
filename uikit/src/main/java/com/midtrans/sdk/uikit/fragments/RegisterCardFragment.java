package com.midtrans.sdk.uikit.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.RelativeLayout;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.BankDetail;
import com.midtrans.sdk.corekit.models.CreditCardFromScanner;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.SaveCreditCardActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.MidtransDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * @author rakawm
 */
public class RegisterCardFragment extends Fragment {

    private String lastExpDate = "";
    private TextInputLayout tilCardNo;
    private EditText etCardNo;
    private EditText etCvv;
    private EditText etExpiryDate;
    private CheckBox cbStoreCard;
    private ImageView questionImg;
    private ImageView questionSaveCardImg;
    private Button saveBtn;
    private Button scanCardBtn;
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
    private RelativeLayout formLayout;

    public static RegisterCardFragment newInstance() {
        return new RegisterCardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SaveCreditCardActivity) getActivity()).getTitleHeaderTextView().setText(getString(R.string.card_details));
        midtransSDK = ((SaveCreditCardActivity) getActivity()).getMidtransSDK();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_card_details, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            Logger.i("onViewCreated called addcarddetail called");
            SaveCreditCardActivity creditCardActivity = (SaveCreditCardActivity) getActivity();
            if (creditCardActivity != null) {
                if (creditCardActivity.getSupportActionBar() != null) {
                    creditCardActivity.getSupportActionBar().setTitle(getString(R.string.card_details));
                    creditCardActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        bindViews(view);
        ((SaveCreditCardActivity) getActivity()).getBtnMorph().setVisibility(View.GONE);
        fadeIn();
        super.onViewCreated(view, savedInstanceState);
    }

    private void bindViews(View view) {
        formLayout = (RelativeLayout) view.findViewById(R.id.form_layout);
        tilCardNo = (TextInputLayout) view.findViewById(R.id.til_card_no);
        etCardNo = (EditText) view.findViewById(R.id.et_card_no);
        etCvv = (EditText) view.findViewById(R.id.et_cvv);
        etExpiryDate = (EditText) view.findViewById(R.id.et_exp_date);
        cbStoreCard = (CheckBox) view.findViewById(R.id.cb_store_card);
        cbStoreCard.setVisibility(View.GONE);
        questionImg = (ImageView) view.findViewById(R.id.image_question);
        questionSaveCardImg = (ImageView) view.findViewById(R.id.image_question_save_card);
        questionSaveCardImg.setVisibility(View.GONE);
        saveBtn = (Button) view.findViewById(R.id.btn_pay_now);
        saveBtn.setText(R.string.btn_save_card);
        scanCardBtn = (Button) view.findViewById(R.id.scan_card);

        cbStoreCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                checkCardValidity();
            }
        });
        if (midtransSDK != null && midtransSDK.getSemiBoldText() != null) {
            saveBtn.setTypeface(Typeface.createFromAsset(getContext().getAssets(), midtransSDK.getSemiBoldText()));
            scanCardBtn.setTypeface(Typeface.createFromAsset(getContext().getAssets(), midtransSDK.getDefaultText()));

            if (midtransSDK.getExternalScanner() != null) {
                scanCardBtn.setVisibility(View.VISIBLE);
                scanCardBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        midtransSDK.getExternalScanner().startScan(getActivity(), SaveCreditCardActivity.SCAN_REQUEST_CODE);
                    }
                });
            } else {
                scanCardBtn.setVisibility(View.GONE);
            }
        }
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCardValidity()) {
                    // Save card
                    SdkUIFlowUtil.showProgressDialog(getActivity(), false);
                    SaveCreditCardActivity activity = (SaveCreditCardActivity) getActivity();
                    String date = etExpiryDate.getText().toString();
                    String month = date.split("/")[0];
                    String year = "20" + date.split("/")[1];
                    if (activity != null) {
                        activity.registerCard(cardNumber, cvv, month, year);
                    }
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
        questionSaveCardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MidtransDialog midtransDialog = new MidtransDialog(getActivity(), getResources().getDrawable(R.drawable.cart_dialog),
                        getString(R.string.message_save_card), getString(R.string.got_it), "");
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
                            try {
                                int month = Integer.parseInt(input);
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
                        } else if (s.length() == 1) {
                            try {
                                int month = Integer.parseInt(input);
                                if (month > 1) {
                                    etExpiryDate.setText("0" + etExpiryDate.getText().toString() + "/");
                                    etExpiryDate.setSelection(etExpiryDate.getText().toString().length());
                                }
                            } catch (Exception exception) {
                                Logger.e(exception.toString());
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

        } else {
            cardType = "";

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
                saveBtn.setVisibility(View.VISIBLE);
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
