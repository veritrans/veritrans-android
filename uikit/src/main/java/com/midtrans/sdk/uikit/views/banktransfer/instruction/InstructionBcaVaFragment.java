package com.midtrans.sdk.uikit.views.banktransfer.instruction;

import android.os.Bundle;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.VaInstructionFragment;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

/**
 * Created by ziahaqi on 12/6/17.
 */

public class InstructionBcaVaFragment extends VaInstructionFragment {


    public static InstructionBcaVaFragment newInstance(int code, String title) {
        InstructionBcaVaFragment fragment = new InstructionBcaVaFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INSTRUCTION_POSITION, code);
        bundle.putString(INSTRUCTION_TITLE, title);
        Logger.d("xtitle", "title:" + title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId() {
        int instructionCode = getFragmentCode();
        int layoutId = instructionCode;

        switch (instructionCode) {
            case UiKitConstants.INSTRUCTION_FIRST_POSITION:
                layoutId = R.layout.fragment_instruction_bca;
                break;

            case UiKitConstants.INSTRUCTION_SECOND_POSITION:
                layoutId = R.layout.fragment_bca_tranfer_click_instruction;
                break;

            case UiKitConstants.INSTRUCTION_THIRD_POSITION:
                layoutId = R.layout.fragment_instruction_bca_transfer_mbca;
                break;
        }

        return layoutId;
    }

}
