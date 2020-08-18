package com.midtrans.sdk.uikit.views.banktransfer.instruction;

import android.os.Bundle;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.VaInstructionFragment;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

public class InstructionBriVaFragment extends VaInstructionFragment {

    public static InstructionBriVaFragment newInstance(int position, String title) {
        InstructionBriVaFragment fragment = new InstructionBriVaFragment();
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
                layoutId = R.layout.fragment_instruction_bri_atm;
                break;

            case UiKitConstants.INSTRUCTION_SECOND_POSITION:
                layoutId = R.layout.fragment_instruction_bri_mobile;
                break;

            case UiKitConstants.INSTRUCTION_THIRD_POSITION:
                layoutId = R.layout.fragment_instruction_bri_internet;
                break;
        }

        return layoutId;
    }

}