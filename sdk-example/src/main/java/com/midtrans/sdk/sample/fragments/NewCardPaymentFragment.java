package com.midtrans.sdk.sample.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.sample.CreditCardPaymentActivity;
import com.midtrans.sdk.sample.MainActivity;
import com.midtrans.sdk.sample.R;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 10/18/16.
 */

public class NewCardPaymentFragment extends Fragment {
    private static final String ARGS_SAVE_NORMAL_PAYMENT = "normal_payment";
    public static final String TAG = "NewCardPaymentFragment";
    TextInputLayout cardNumberContainer, cvvContainer, expiredDateContainer;

    private Button payBtn;
    private EditText cardNumber;
    private EditText cvv;
    private EditText expiredDate;
    private CheckBox checkSaveCard;
    ProgressDialog dialog;
    private boolean saveCard;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_card_payment, container, false);
        cardNumberContainer = (TextInputLayout) view.findViewById(R.id.card_number_container);
        expiredDateContainer = (TextInputLayout) view.findViewById(R.id.exp_date_container);
        cvvContainer = (TextInputLayout) view.findViewById(R.id.cvv_number_container);
        payBtn = (Button) view.findViewById(R.id.btn_payment);
        cardNumber = (EditText) view.findViewById(R.id.card_number);
        expiredDate = (EditText) view.findViewById(R.id.exp_date);
        cvv = (EditText) view.findViewById(R.id.cvv_number);
        checkSaveCard = (CheckBox) view.findViewById(R.id.check_paycard_savecard);
        //Initialize progress dialog

        Log.d(TAG, "clicktype:" + MidtransSDK.getInstance().getTransactionRequest()
                .getCardClickType());
        if (!MidtransSDK.getInstance().getTransactionRequest()
                .getCardClickType().equals(getString(R.string.card_click_type_none))) {
            checkSaveCard.setVisibility(View.VISIBLE);
        }

        checkSaveCard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                saveCard = checked;
            }
        });
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh validator
                refreshView();
                if (inputValidator()) {
                    // Create token request before payment
                    String date = expiredDate.getText().toString();
                    String expMonth = date.split("/")[0];
                    String expYear = "20"+ date.split("/")[1];
                    ((CreditCardPaymentActivity) getActivity()).paymentNormal(cardNumber.getText().toString(),
                            expMonth, expYear, cvv.getText().toString(), saveCard);
                }
            }
        });
        return view;
    }


    private void actionOnError(Throwable error) {
        // Handle generic error condition
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("Unknown error: " + error.getMessage())
                .create();
        dialog.show();
    }

    private void saveCreditCard(ArrayList<SaveCardRequest> requests) {

        MidtransSDK.getInstance().saveCards(MainActivity.SAMPLE_USER_ID, requests, new SaveCardCallback() {
            @Override
            public void onSuccess(SaveCardResponse response) {

            }

            @Override
            public void onFailure(String reason) {
                Logger.i("savecard>failed");
                Toast.makeText(getContext().getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable error) {
                actionOnError(error);
            }
        });
    }

    private void refreshView() {
        // Check card number
        if (cardNumber.getText().toString().isEmpty()) {
            cardNumberContainer.setError("Must not be empty.");
        } else {
            if (cardNumber.getText().length() == 16) {
                cardNumberContainer.setError(null);
            } else {
                cardNumberContainer.setError("Must be 16 digits.");
            }
        }

        // Check cvv number
        if (cvv.getText().toString().isEmpty()) {
            cvvContainer.setError("Must not be empty.");
        } else {
            if (cvv.getText().toString().length() == 3) {
                cvvContainer.setError(null);
            } else {
                cvvContainer.setError("Must be 3 digits.");
            }
        }

        // Check exp date
        if (expiredDate.getText().toString().isEmpty()) {
            expiredDateContainer.setError("Must not be empty.");
        } else {
            if (expiredDate.getText().toString().split("/").length == 2) {
                expiredDateContainer.setError(null);
            } else {
                expiredDateContainer.setError("Must be (mm/yy) formatted.");
            }
        }
    }
    /**
     * @return if all input fields is not empty
     */
    private boolean inputValidator() {
        return !cardNumber.getText().toString().isEmpty()
                && cardNumber.getText().toString().length() == 16
                && !cvv.getText().toString().isEmpty()
                && cvv.getText().toString().length() == 3
                && !expiredDate.getText().toString().isEmpty()
                && expiredDate.getText().toString().split("/").length == 2;
    }

    public static NewCardPaymentFragment newInstance(boolean normalPayment) {
        NewCardPaymentFragment fragment = new NewCardPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARGS_SAVE_NORMAL_PAYMENT, normalPayment);
        fragment.setArguments(bundle);
        return fragment;
    }
}
