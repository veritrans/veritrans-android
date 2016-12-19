package com.midtrans.sdk.uikit.fragments;

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
import android.widget.EditText;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

/**
 * Created by ziahaqi on 12/7/16.
 */

public class GCIPaymentFragment extends Fragment {
    private EditText editGiftCardNumber, editGiftCardPassword;
    private TextInputLayout cardNumberContainer, passwordContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gci_payment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize view
        editGiftCardNumber = (EditText) view.findViewById(R.id.edit_gci_card_number);
        editGiftCardPassword = (EditText) view.findViewById(R.id.edit_gci_password);
        cardNumberContainer = (TextInputLayout) view.findViewById(R.id.card_number_container);
        passwordContainer = (TextInputLayout) view.findViewById(R.id.password_container);

        editGiftCardNumber.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                cardNumberContainer.setError(null);
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

                // Move to next input
                if (s.length() >= 19) {
                    editGiftCardPassword.requestFocus();
                }
            }
        });

    }


    /**
     * Check password
     *
     * @return if text was empty return false else return true
     */
    public boolean checkPasswordValidity() {
        boolean valid = true;
        String password = editGiftCardPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordContainer.setError(getString(R.string.error_password_empty));
            valid = false;
        } else if (password.length() < 3) {
            passwordContainer.setError(getString(R.string.error_password_invalid));
            valid = false;
        } else {
            passwordContainer.setError(null);
        }
        return valid;
    }

    public boolean checkFormValidity() {
        return checkCardNumberValidity() && checkPasswordValidity();
    }

    public String getPassword() {
        return editGiftCardPassword.getText().toString();
    }

    public String getCardNumber() {
        String cardNumber = editGiftCardNumber.getText().toString().trim().replace(" ", "");
        return cardNumber;
    }

    private boolean checkCardNumberValidity() {
        boolean isValid = true;

        String cardNumber = editGiftCardNumber.getText().toString().trim().replace(" ", "");
        if (TextUtils.isEmpty(cardNumber)) {
            cardNumberContainer.setError(getString(R.string.validation_message_card_number));
            isValid = false;
        } else {
            cardNumberContainer.setError(null);
        }

        if (cardNumber.length() < 13 ) {
            cardNumberContainer.setError(getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            cardNumberContainer.setError(null);
        }
        return isValid;
    }
}
