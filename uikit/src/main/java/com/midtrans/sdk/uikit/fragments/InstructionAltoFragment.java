package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.midtrans.sdk.uikit.R;

/**
 * @author rakawm
 * Deprecated, use {@link com.midtrans.sdk.uikit.fragments.InstructionOtherBankFragment} instead
 */
@Deprecated
public class InstructionAltoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instruction_alto, container, false);
    }
}
