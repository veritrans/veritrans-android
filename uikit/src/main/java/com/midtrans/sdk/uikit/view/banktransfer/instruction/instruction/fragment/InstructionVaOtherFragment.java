package com.midtrans.sdk.uikit.view.banktransfer.instruction.instruction.fragment;

import android.os.Bundle;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.Constants;

public class InstructionVaOtherFragment extends VaInstructionFragment {

    public static InstructionVaOtherFragment newInstance(int code, String title, PaymentInfoResponse paymentInfoResponse) {
        InstructionVaOtherFragment fragment = new InstructionVaOtherFragment();
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
            case Constants.ATM_BERSAMA:
                layoutId = R.layout.fragment_payment_instruction_bank_transfer_atm_bersama;
                break;
            case Constants.PRIMA:
                layoutId = R.layout.fragment_payment_instruction_bank_transfer_prima;
                break;
            case Constants.ALTO:
                layoutId = R.layout.fragment_payment_instruction_bank_transfer_alto;
                break;
        }
        return layoutId;
    }
}