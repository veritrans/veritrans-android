package com.midtrans.sdk.uikit.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;
import java.lang.reflect.Field;

/**
 * Created by ziahaqi on 12/7/16.
 * Deprecated, please refer to {@link com.midtrans.sdk.uikit.views.gci.GciPaymentActivity}
 */
@Deprecated
public class GCIPaymentFragment extends Fragment {
    private AppCompatEditText editGiftCardNumber, editGiftCardPassword;
    private TextInputLayout cardNumberContainer, passwordContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gci_payment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize view
        editGiftCardNumber = (AppCompatEditText) view.findViewById(R.id.edit_gci_card_number);
        editGiftCardPassword = (AppCompatEditText) view.findViewById(R.id.edit_gci_password);
        cardNumberContainer = (TextInputLayout) view.findViewById(R.id.card_number_container);
        passwordContainer = (TextInputLayout) view.findViewById(R.id.password_container);

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getSecondaryColor() != 0) {
                // Set color filter in edit text
                try {
                    // Set on card number
                    Field fDefaultTextColorCardNumber = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                    fDefaultTextColorCardNumber.setAccessible(true);
                    fDefaultTextColorCardNumber.set(cardNumberContainer, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    Field fFocusedTextColorCardNumber = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                    fFocusedTextColorCardNumber.setAccessible(true);
                    fFocusedTextColorCardNumber.set(cardNumberContainer, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    editGiftCardNumber.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    // Set on debit PIN
                    Field fDefaultTextColorPin = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                    fDefaultTextColorPin.setAccessible(true);
                    fDefaultTextColorPin.set(passwordContainer, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    Field fFocusedTextColorPin = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                    fFocusedTextColorPin.setAccessible(true);
                    fFocusedTextColorPin.set(passwordContainer, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    editGiftCardPassword.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

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

        if (cardNumber.length() != 16 ) {
            cardNumberContainer.setError(getString(R.string.validation_message_invalid_card_no));
            isValid = false;
        } else {
            cardNumberContainer.setError(null);
        }
        return isValid;
    }
}
