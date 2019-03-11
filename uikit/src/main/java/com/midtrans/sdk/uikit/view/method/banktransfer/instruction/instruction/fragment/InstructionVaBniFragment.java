package com.midtrans.sdk.uikit.view.method.banktransfer.instruction.instruction.fragment;

import android.os.Bundle;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.Constants;

public class InstructionVaBniFragment extends VaInstructionFragment {

    public static InstructionVaBniFragment newInstance(int code, String title, PaymentInfoResponse paymentInfoResponse) {
        InstructionVaBniFragment fragment = new InstructionVaBniFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(INSTRUCTION_POSITION, code);
        bundle.putString(INSTRUCTION_TITLE, title);
        bundle.putSerializable(MERCHANT_INFORMATION, paymentInfoResponse);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int initLayoutId() {
        int instructionCode = getFragmentCode();
        int layoutId = instructionCode;

        switch (instructionCode) {
            case Constants.INSTRUCTION_FIRST_POSITION:
                layoutId = R.layout.fragment_payment_instruction_bank_transfer_bni_atm;
                break;

            case Constants.INSTRUCTION_SECOND_POSITION:
                layoutId = R.layout.fragment_payment_instruction_bank_transfer_bni_mobile;
                break;

            case Constants.INSTRUCTION_THIRD_POSITION:
                layoutId = R.layout.fragment_payment_instruction_bank_transfer_bni_internet;
                break;
        }

        return layoutId;
    }
}