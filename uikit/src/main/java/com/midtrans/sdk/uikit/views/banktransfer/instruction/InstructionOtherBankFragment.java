package com.midtrans.sdk.uikit.views.banktransfer.instruction;

import android.os.Bundle;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.VaInstructionFragment;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

/**
 * Created by Fajar on 8/18/17.
 */

public class InstructionOtherBankFragment extends VaInstructionFragment {

    @Override
    public int initLayoutId() {
        int instructionCode = getFragmentCode();
        int layoutId = instructionCode;

        switch (instructionCode) {
            case UiKitConstants.ATM_BERSAMA:
                layoutId = R.layout.fragment_instruction_atm_bersama;
                break;
            case UiKitConstants.PRIMA:
                layoutId = R.layout.fragment_instruction_prima;
                break;
            case UiKitConstants.ALTO:
                layoutId = R.layout.fragment_instruction_alto;
                break;
        }
        return layoutId;
    }

    public static InstructionOtherBankFragment newInstance(int position) {
        InstructionOtherBankFragment fragment = new InstructionOtherBankFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INSTRUCTION_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }
}
