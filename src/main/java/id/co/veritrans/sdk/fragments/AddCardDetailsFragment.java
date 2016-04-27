package id.co.veritrans.sdk.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.CreditDebitCardFlowActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.BankDetail;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.models.UserDetail;
import id.co.veritrans.sdk.widgets.VeritransDialog;

public class AddCardDetailsFragment extends Fragment {
    private String lastExpDate = "";
    private EditText etCardNo;
    private EditText etCvv;
    private EditText etExpiryDate;
    private CheckBox cbStoreCard;
    private ImageView questionImg;
    private ImageView questionSaveCardImg;
    private Button payNowBtn;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String[] expDateArray;
    private int expMonth;
    private int expYear;
    private VeritransSDK veritransSDK;
    private UserDetail userDetail;
    private ArrayList<BankDetail> bankDetails;
    private String cardType = "";
    private RelativeLayout formLayout;

    public AddCardDetailsFragment() {
    }

    public static AddCardDetailsFragment newInstance() {
        AddCardDetailsFragment fragment = new AddCardDetailsFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextViewFont().setText(getString(R.string.card_details));
        veritransSDK = ((CreditDebitCardFlowActivity) getActivity()).getVeritransSDK();
        userDetail = ((CreditDebitCardFlowActivity) getActivity()).getUserDetail();
        bankDetails = ((CreditDebitCardFlowActivity) getActivity()).getBankDetails();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_card_details, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            Logger.i("onViewCreated called addcarddetail called");
            CreditDebitCardFlowActivity creditDebitCardFlowActivity = (CreditDebitCardFlowActivity) getActivity();
            if (creditDebitCardFlowActivity != null && creditDebitCardFlowActivity.getSupportActionBar() != null) {
                creditDebitCardFlowActivity.getSupportActionBar().setTitle(getString(R.string.card_details));
                creditDebitCardFlowActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        bindViews(view);
        ((CreditDebitCardFlowActivity) getActivity()).getBtnMorph().setVisibility(View.GONE);
        fadeIn();
        super.onViewCreated(view, savedInstanceState);
    }

    private void bindViews(View view) {
        formLayout = (RelativeLayout) view.findViewById(R.id.form_layout);
        etCardNo = (EditText) view.findViewById(R.id.et_card_no);
        etCvv = (EditText) view.findViewById(R.id.et_cvv);
        etExpiryDate = (EditText) view.findViewById(R.id.et_exp_date);
        cbStoreCard = (CheckBox) view.findViewById(R.id.cb_store_card);
        questionImg = (ImageView) view.findViewById(R.id.image_question);
        questionSaveCardImg = (ImageView) view.findViewById(R.id.image_question_save_card);
        payNowBtn = (Button) view.findViewById(R.id.btn_pay_now);
        //formLayout.setAlpha(0);
        cbStoreCard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                isValid();
            }
        });
        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {

                    CardTokenRequest cardTokenRequest = new CardTokenRequest(cardNumber, Integer
                            .parseInt(cvv),
                            expMonth, expYear,
                            veritransSDK.getClientKey());
                    cardTokenRequest.setIsSaved(cbStoreCard.isChecked());
                    cardTokenRequest.setSecure(veritransSDK.getTransactionRequest().isSecureCard());
                    cardTokenRequest.setGrossAmount(veritransSDK.getTransactionRequest()
                            .getAmount());
                    cardTokenRequest.setCardType(cardType);
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
                    SdkUtil.showProgressDialog(getActivity(), false);
                    ((CreditDebitCardFlowActivity) getActivity()).getToken(cardTokenRequest);
                }
            }
        });
        questionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeritransDialog veritransDialog = new VeritransDialog(getActivity(), getResources().getDrawable(R.drawable.cvv_dialog_image),
                        getString(R.string.message_cvv), getString(R.string.got_it), "");
                veritransDialog.show();
            }
        });
        questionSaveCardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeritransDialog veritransDialog = new VeritransDialog(getActivity(), getResources().getDrawable(R.drawable.cart_dialog),
                        getString(R.string.message_save_card), getString(R.string.got_it), "");
                veritransDialog.show();
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
            }
        });

        etExpiryDate.addTextChangedListener(new

                                                    TextWatcher() {
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
                                                        }
                                                    }

        );
    }

    private boolean isValid() {
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
            if (!etCardNo.hasFocus()) {
                etCardNo.setError(getString(R.string.validation_message_card_number));
            }
            isValid = false;
        }
        if (cardNumber.length() < 16 || !SdkUtil.isValidCardNumber(cardNumber)) {
            if (!etCardNo.hasFocus()) {
                etCardNo.setError(getString(R.string.validation_message_invalid_card_no));
            }
            isValid = false;
        } else if (TextUtils.isEmpty(expiryDate)) {
            if (!etExpiryDate.hasFocus()) {
                etExpiryDate.setError(getString(R.string.validation_message_empty_expiry_date));
            }
            isValid = false;
        } else if (!expiryDate.contains("/")) {
            if (!etExpiryDate.hasFocus()) {
                etExpiryDate.setError(getString(R.string.validation_message_invalid_expiry_date));
            }
            isValid = false;
        } else if (expDateArray == null || expDateArray.length != 2) {
            if (!etExpiryDate.hasFocus()) {
                etExpiryDate.setError(getString(R.string.validation_message_invalid_expiry_date));
            }
            isValid = false;
        } else if (expDateArray != null) {
            try {
                expMonth = Integer.parseInt(expDateArray[0]);
            } catch (NumberFormatException e) {
                if (!etExpiryDate.hasFocus()) {
                    etExpiryDate.setError(getString(R.string.validation_message_invalid_expiry_date));
                }
                isValid = false;
            }
            try {
                expYear = Integer.parseInt(expDateArray[1]);
            } catch (NumberFormatException e) {
                if (!etExpiryDate.hasFocus()) {
                    etExpiryDate.setError(getString(R.string.validation_message_invalid_expiry_date));
                }
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
                if (!etExpiryDate.hasFocus()) {
                    etExpiryDate.setError(getString(R.string.validation_message_invalid_expiry_date));
                }
                isValid = false;
            } else if (expYear == currentYear && currentMonth > expMonth) {
                if (!etExpiryDate.hasFocus()) {
                    etExpiryDate.setError(getString(R.string.validation_message_invalid_expiry_date));
                }
                isValid = false;
            }

            if (TextUtils.isEmpty(cvv)) {
                if (!etCvv.hasFocus()) {
                    etCvv.setError(getString(R.string.validation_message_cvv));
                }
                questionImg.setVisibility(View.GONE);
                isValid = false;
            } else {
                if (cvv.length() < 3) {
                    if (!etCvv.hasFocus()) {
                        etCvv.setError(getString(R.string.validation_message_invalid_cvv));
                    }
                    questionImg.setVisibility(View.GONE);
                    isValid = false;
                }
                questionImg.setVisibility(View.VISIBLE);
            }
        }
        return isValid;
    }

    public void focusChange() {
        Logger.i("onFocus change has not focus");
        isValid();
    }

    private void setCardType() {
        String cardNo = etCardNo.getText().toString().trim();
        if (cardNo.isEmpty() || cardNo.length() < 2) {
            etCardNo.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            return;
        }
        if (cardNo.charAt(0) == '4') {
            Drawable visa = getResources().getDrawable(R.drawable.visa_non_transperent);
            etCardNo.setCompoundDrawablesWithIntrinsicBounds(null, null, visa, null);
            cardType = getString(R.string.visa);
        } else if ((cardNo.charAt(0) == '5') && ((cardNo.charAt(1) == '1') || (cardNo.charAt(1) == '2')
                || (cardNo.charAt(1) == '3') || (cardNo.charAt(1) == '4') || (cardNo.charAt(1) == '5'))) {
            Drawable masterCard = getResources().getDrawable(R.drawable.mastercard_non_transperent);
            etCardNo.setCompoundDrawablesWithIntrinsicBounds(null, null, masterCard, null);
            cardType = getString(R.string.mastercard);

        } else if ((cardNo.charAt(0) == '3') && ((cardNo.charAt(1) == '4') || (cardNo.charAt(1) == '7'))) {
            Drawable amex = getResources().getDrawable(R.drawable.amex_non_transperent);
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

}