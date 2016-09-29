package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.midtrans.sdk.uikit.R;

/**
 * @author rakawm
 */
public class InstructionTelkomselCashFragment extends Fragment {

    private EditText telkomselTokenEditText = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instruction_telkomsel, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        telkomselTokenEditText = (EditText) view.findViewById(R.id.telkomsel_token_et);
    }

    public String getTelkomselToken() {
        if (telkomselTokenEditText != null) {
            return telkomselTokenEditText.getText().toString();
        } else {
            return null;
        }
    }

}
