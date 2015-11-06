package id.co.veritrans.sdk.fragments;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.CreditDebitCardFlowActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.models.UserDetail;
import id.co.veritrans.sdk.widgets.VeritransDialog;

public class AddCardDetailsFragment extends Fragment {
    String lastExpDate = "";
    private EditText etCardHolderName;
    private EditText etCardNo;
    private EditText etCvv;
    private EditText etExpiryDate;
    private CheckBox cbStoreCard;
    private ImageView questionImg;
    private Button payNowBtn;
    private String username;
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String[] expDateArray;
    private int expMonth;
    private int expYear;
    private VeritransSDK veritransSDK;
    private UserDetail userDetail;

    public AddCardDetailsFragment() {
        // Required empty public constructor
    }

    public static AddCardDetailsFragment newInstance() {
        AddCardDetailsFragment fragment = new AddCardDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        veritransSDK = ((CreditDebitCardFlowActivity) getActivity()).getVeritransSDK();
        userDetail = ((CreditDebitCardFlowActivity) getActivity()).getUserDetail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_card_details, container, false);
        bindViews(view);
        return view;
    }


    private void bindViews(View view) {
        etCardHolderName = (EditText) view.findViewById(R.id.et_holder_name);
        etCardNo = (EditText) view.findViewById(R.id.et_card_no);
        etCvv = (EditText) view.findViewById(R.id.et_cvv);
        etExpiryDate = (EditText) view.findViewById(R.id.et_exp_date);
        cbStoreCard = (CheckBox) view.findViewById(R.id.cb_store_card);
        questionImg = (ImageView) view.findViewById(R.id.question_img);
        payNowBtn = (Button) view.findViewById(R.id.btn_pay_now);

        if (veritransSDK.isLogEnabled()) {
            etExpiryDate.setText("12/20");
            etCardNo.setText("4811 1111 1111 1114");
            etCardHolderName.setText("Chetan");
            etCvv.setText("123");
        }
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

                    //tokenRequestModel.setTwoClick(true);
                    //make payment
                    SdkUtil.showProgressDialog(getActivity(), false);
                    ((CreditDebitCardFlowActivity) getActivity()).getToken(cardTokenRequest);
                }
            }
        });
        questionImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VeritransDialog veritransDialog = new VeritransDialog(getActivity(), "",
                        getString(R.string.message_cvv), getString(android.R.string.ok), "");
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
                        //Toast.makeText(getApplicationContext(), "Enter a valid month", Toast
                        // .LENGTH_LONG).show();
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
        });
    }

    private boolean isValid() {

        username = etCardHolderName.getText().toString().trim();
        cardNumber = etCardNo.getText().toString().trim().replace(" ", "");
        expiryDate = etExpiryDate.getText().toString().trim();
        cvv = etCvv.getText().toString().trim();
        try {
            expDateArray = expiryDate.split("/");
            Logger.i("expDate:" + expDateArray[0], "" + expDateArray[1]);
        } catch (NullPointerException e) {
            Logger.i("expiry date empty");
        } catch (IndexOutOfBoundsException e) {
            Logger.i("expiry date issue");
        }
        if (TextUtils.isEmpty(cardNumber)) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string.validation_message_card_number));
            etCardNo.requestFocus();
            return false;
        } else if (cardNumber.length() < 16 || !SdkUtil.isValidCardNumber(cardNumber)) {
            etCardNo.requestFocus();
            SdkUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_message_invalid_card_no));
            return false;
        } else if (TextUtils.isEmpty(username)) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string
                    .validatation_message_card_holder_name));
            etCardHolderName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(expiryDate)) {
            etExpiryDate.requestFocus();
            SdkUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_message_empty_expiry_date));
            return false;
        } else if (!expiryDate.contains("/")) {
            etExpiryDate.requestFocus();
            SdkUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_message_invalid_expiry_date));
            return false;
        } else if (expDateArray == null || expDateArray.length != 2) {
            etExpiryDate.requestFocus();
            SdkUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_message_invalid_expiry_date));
            return false;
        } else if (expDateArray != null && expDateArray.length == 2) {
            try {
                expMonth = Integer.parseInt(expDateArray[0]);
            } catch (NumberFormatException e) {
                etExpiryDate.requestFocus();
                SdkUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_message_invalid_expiry_date));
                return false;
            }
            try {

                expYear = Integer.parseInt(expDateArray[1]);
            } catch (NumberFormatException e) {
                etExpiryDate.requestFocus();
                SdkUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_message_invalid_expiry_date));
                return false;
            }
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            SimpleDateFormat format = new SimpleDateFormat("yy");
            String year = format.format(date);

            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            //int currentYear = calendar.get(Calendar.YEAR);
            int currentYear = Integer.parseInt(year);
            Logger.i("currentMonth:" + currentMonth + ",currentYear:" + currentYear);
            if (expYear < currentYear) {
                etExpiryDate.requestFocus();
                SdkUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_message_invalid_expiry_date));
                return false;
            } else if (expYear == currentYear && currentMonth > expMonth) {
                etExpiryDate.requestFocus();
                SdkUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_message_invalid_expiry_date));
                return false;
            } else if (TextUtils.isEmpty(cvv)) {
                etCvv.requestFocus();
                SdkUtil.showSnackbar(getActivity(), getString(R.string.validation_message_cvv));
                return false;
            } else {
                if (cvv.length() < 3) {
                    etCvv.requestFocus();
                    SdkUtil.showSnackbar(getActivity(), getString(R.string
                            .validation_message_invalid_cvv));
                    return false;
                }
            }
        }
        return true;
    }

}