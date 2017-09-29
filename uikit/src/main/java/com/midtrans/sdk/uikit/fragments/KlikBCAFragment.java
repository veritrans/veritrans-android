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
 * Klik BCA payment fragment. Shows user ID text field and payment instructions.
 * Deprecated, please use {@link com.midtrans.sdk.uikit.views.bca_klikbca.payment.KlikBcaPaymentActivity}
 * @author rakawm
 */
@Deprecated
public class KlikBCAFragment extends Fragment {

    private AppCompatEditText userIdEditText;
    private TextInputLayout userIdContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_klik_bca_payment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize view
        userIdEditText = (AppCompatEditText) view.findViewById(R.id.user_id_et);
        userIdContainer = (TextInputLayout) view.findViewById(R.id.user_id_container);

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getSecondaryColor() != 0) {
                // Set color filter in edit text
                try {
                    Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                    fDefaultTextColor.setAccessible(true);
                    fDefaultTextColor.set(userIdContainer, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    Field fFocusedTextColor = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                    fFocusedTextColor.setAccessible(true);
                    fFocusedTextColor.set(userIdContainer, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    userIdEditText.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Check user ID edit text.
     *
     * @return if text was empty return false else return true
     */
    public boolean checkUserId() {
        if (userIdEditText.getText().toString().isEmpty()) {
            userIdContainer.setError(getString(R.string.error_user_id));
        } else {
            userIdContainer.setError(null);
        }
        return !userIdEditText.getText().toString().isEmpty();
    }

    /**
     * Return user ID text
     *
     * @return user ID text.
     */
    public String getUserId() {
        return userIdEditText.getText().toString();
    }
}
