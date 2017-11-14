package com.midtrans.sdk.uikit.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;
import java.lang.reflect.Field;

/**
 * @author rakawm
 * Deprecated, please refer to {@link com.midtrans.sdk.uikit.views.telkomsel_cash.TelkomselCashPaymentActivity}
 */
@Deprecated
public class InstructionTelkomselCashFragment extends Fragment {

    private TextInputLayout telkomselTokenTextInputLayout = null;
    private AppCompatEditText telkomselTokenEditText = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instruction_telkomsel, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        telkomselTokenEditText = (AppCompatEditText) view.findViewById(R.id.telkomsel_token_et);
        telkomselTokenTextInputLayout = (TextInputLayout) view.findViewById(R.id.telkomsel_token_til);

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getSecondaryColor() != 0) {
                // Set color filter in edit text
                try {
                    Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                    fDefaultTextColor.setAccessible(true);
                    fDefaultTextColor.set(telkomselTokenTextInputLayout, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    Field fFocusedTextColor = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                    fFocusedTextColor.setAccessible(true);
                    fFocusedTextColor.set(telkomselTokenTextInputLayout, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    telkomselTokenEditText.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getTelkomselToken() {
        if (telkomselTokenEditText != null) {
            return telkomselTokenEditText.getText().toString();
        } else {
            return null;
        }
    }

}
