package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.midtrans.sdk.uikit.R;

/**
 * Klik BCA payment fragment. Shows user ID text field and payment instructions.
 *
 * @author rakawm
 */
public class KlikBCAFragment extends Fragment {

    private EditText userIdEditText;
    private TextInputLayout userIdContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_klik_bca_payment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize view
        userIdEditText = (EditText) view.findViewById(R.id.user_id_et);
        userIdContainer = (TextInputLayout) view.findViewById(R.id.user_id_container);
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
