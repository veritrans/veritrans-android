package com.midtrans.sdk.ui.views.instructions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midtrans.sdk.ui.R;

/**
 * Displays ATM bersama payment instruction.
 *
 * @author rakawm
 */
public class InstructionAtmBersamaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instruction_atm_bersama, container, false);
    }
}
