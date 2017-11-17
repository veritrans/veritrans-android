package com.midtrans.sdk.uikit.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.R;
import java.lang.reflect.Field;

/**
 * Deprecated, please refer to {@link com.midtrans.sdk.uikit.views.indosat_dompetku.IndosatDompetkuPaymentActivity}
 */
@Deprecated
public class InstructionIndosatFragment extends Fragment {
    private TextInputLayout mTextInputLayoutPhoneNumber = null;
    private AppCompatEditText mEditTextPhoneNumber = null;
    private UserDetail userDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_instruction_indosat, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        try {
            userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mTextInputLayoutPhoneNumber = (TextInputLayout) view.findViewById(R.id.til_indosat_phone_number);
        mEditTextPhoneNumber = (AppCompatEditText) view.findViewById(R.id.et_indosat_phone_number);
        if (!TextUtils.isEmpty(userDetail.getPhoneNumber())) {
            mEditTextPhoneNumber.setText(userDetail.getPhoneNumber());
        }

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getSecondaryColor() != 0) {
                // Set color filter in edit text
                try {
                    Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                    fDefaultTextColor.setAccessible(true);
                    fDefaultTextColor.set(mTextInputLayoutPhoneNumber, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    Field fFocusedTextColor = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                    fFocusedTextColor.setAccessible(true);
                    fFocusedTextColor.set(mTextInputLayoutPhoneNumber, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    mEditTextPhoneNumber.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPhoneNumber() {
        if (mEditTextPhoneNumber != null) {
            return mEditTextPhoneNumber.getText().toString();
        } else {
            return null;
        }
    }

}
