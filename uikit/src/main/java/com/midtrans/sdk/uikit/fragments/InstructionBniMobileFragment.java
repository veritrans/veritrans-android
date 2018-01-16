package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midtrans.sdk.uikit.R;

/**
 * Created by ziahaqi on 4/4/17.
 */

/**
 * Displays ATM bersama payment instruction.
 * @author rakawm
 * Deprecated, use {@link com.midtrans.sdk.uikit.views.banktransfer.instruction.InstructionBniVaFragment} instead
 */
@Deprecated
public class InstructionBniMobileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instruction_bni_mobile, container, false);
    }
}
