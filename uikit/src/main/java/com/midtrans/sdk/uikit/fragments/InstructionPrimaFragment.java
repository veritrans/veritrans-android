package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.views.banktransfer.instruction.InstructionOtherBankFragment;

/**
 * Displays Prima bank instruction.
 * Deprecated, use {@link InstructionOtherBankFragment} instead
 */
@Deprecated
public class InstructionPrimaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instruction_prima, container, false);
    }
}
