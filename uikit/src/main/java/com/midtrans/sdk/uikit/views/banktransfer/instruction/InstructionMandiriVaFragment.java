package com.midtrans.sdk.uikit.views.banktransfer.instruction;

import android.os.Bundle;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.VaInstructionFragment;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

/**
 * Created by ziahaqi on 12/6/17.
 */

public class InstructionMandiriVaFragment extends VaInstructionFragment {


    public static InstructionMandiriVaFragment newInstance(int position, String title) {
        InstructionMandiriVaFragment fragment = new InstructionMandiriVaFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INSTRUCTION_POSITION, position);
        bundle.putString(INSTRUCTION_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId() {
        int instructionCode = getFragmentCode();
        int layoutId = instructionCode;

        switch (instructionCode) {
            case UiKitConstants.INSTRUCTION_FIRST_POSITION:
                layoutId = R.layout.fragment_instruction_mandiri;
                break;

            case UiKitConstants.INSTRUCTION_SECOND_POSITION:
                layoutId = R.layout.fragment_instruction_mandiri_internet;
                break;
        }
        return layoutId;
    }
}
