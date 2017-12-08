package com.midtrans.sdk.uikit.views.banktransfer.instruction;

import android.os.Bundle;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.VaInstructionFragment;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

/**
 * Created by ziahaqi on 12/6/17.
 */

public class InstructionPermataVaFragment extends VaInstructionFragment {

    public static InstructionPermataVaFragment newInstance(int code, String title) {
        InstructionPermataVaFragment fragment = new InstructionPermataVaFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INSTRUCTION_POSITION, code);
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
                layoutId = R.layout.fragment_instruction_permata;
                break;

            case UiKitConstants.INSTRUCTION_SECOND_POSITION:
                layoutId = R.layout.fragment_instruction_alto;
                break;
        }
        return layoutId;
    }
}
