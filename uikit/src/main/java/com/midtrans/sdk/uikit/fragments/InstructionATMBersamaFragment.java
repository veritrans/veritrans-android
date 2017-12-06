package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.views.banktransfer.instruction.InstructionOtherBankFragment;

/**
 * Displays ATM bersama payment instruction.
 * @author rakawm
 * Deprecated, use {@link InstructionOtherBankFragment} instead
 */
@Deprecated
public class InstructionATMBersamaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instruction_atm_bersama, container, false);
    }
}
