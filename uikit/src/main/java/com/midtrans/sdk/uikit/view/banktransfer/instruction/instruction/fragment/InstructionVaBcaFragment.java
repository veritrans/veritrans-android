package com.midtrans.sdk.uikit.view.banktransfer.instruction.instruction.fragment;

import android.os.Bundle;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.Constants;

public class InstructionVaBcaFragment extends VaInstructionFragment {

    public static InstructionVaBcaFragment newInstance(int code, String title, PaymentInfoResponse paymentInfoResponse) {
        InstructionVaBcaFragment fragment = new InstructionVaBcaFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INSTRUCTION_POSITION, code);
        bundle.putString(INSTRUCTION_TITLE, title);
        bundle.putSerializable(MERCHANT_INFORMATION, paymentInfoResponse);
        Logger.debug("xtitle", "title:" + title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId() {
        int instructionCode = getFragmentCode();
        int layoutId = instructionCode;

        switch (instructionCode) {
            case Constants.INSTRUCTION_FIRST_POSITION:
                layoutId = R.layout.fragment_payment_instruction_bank_transfer_bca;
                break;

            case Constants.INSTRUCTION_SECOND_POSITION:
                layoutId = R.layout.fragment_payment_instruction_bank_transfer_bca_click;
                break;

            case Constants.INSTRUCTION_THIRD_POSITION:
                layoutId = R.layout.fragment_payment_instruction_bank_transfer_bca_mobile;
                break;
        }

        return layoutId;
    }

}