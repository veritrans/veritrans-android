package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.SdkUtil;

public class AddCardDetailsFragment extends Fragment {

    private EditText etCardHolderName;
    private EditText etCardNo;
    private EditText etCvv;
    private Spinner spExpiryDate;

    public static AddCardDetailsFragment newInstance() {
        AddCardDetailsFragment fragment = new AddCardDetailsFragment();
        return fragment;
    }

    public AddCardDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_card_details, container, false);
    }
    private boolean validate(){
        
        String username = etCardHolderName.getText().toString().trim();
        String cardNumber = etCardNo.getText().toString().trim();
        String expiryMonth = String.valueOf(expMonthSp.getSelectedItem()).trim();
        String expiryYear = String.valueOf(expYearSp.getSelectedItem()).trim();
        String cvv = etCvv.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            SdkUtil.showSnackbar(getActivity(),getString(R.string.validatation_message_card_holder_name));
            etCardHolderName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(cardNumber)) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string.validation_message_card_number));
            etCardNo.requestFocus();
            return false;
        } else {
            cardNumber = cardNumber.replace(" ", "");
            if (cardNumber.length() < 16 || !SdkUtil.isValidCardNumber(cardNumber)) {
                etCardNo.requestFocus();
                showSnackbar(getString(R.string.error_message_invalid_card_no));
                return;
            }
        }

        /*if (TextUtils.isEmpty(expiryMonth)) {
            expMonthSp.requestFocus();
            showSnackbar(getString(R.string.error_message_expiry_month));
            return;
        } else*/
        if (expiryMonth.equalsIgnoreCase(getString(R.string.expiry_month))) {
            expMonthSp.requestFocus();
            showSnackbar(getString(R.string.error_message_expiry_month));
            return;
        }
        /*if (TextUtils.isEmpty(expiryYear)) {
            expYearSp.requestFocus();
            showSnackbar(getString(R.string.error_message_expiry_year));
            return;
        }*/
        if (expiryYear.equalsIgnoreCase(getString(R.string.expiry_year))) {
            expYearSp.requestFocus();
            showSnackbar(getString(R.string.error_message_expiry_year));
            return;
        }
        if (TextUtils.isEmpty(cvv)) {
            etCvv2.requestFocus();
            showSnackbar(getString(R.string.error_message_cvv));
            return;
        } else {
            if (cvv.length() < 3) {
                showSnackbar(getString(R.string.error_message_invalid_cvv));
            }
        }
    }
    
}
