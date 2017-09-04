package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.banktransfer.payment.BankTransferPaymentActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by Fajar on 8/18/17.
 */

public class InstructionOtherBankFragment extends Fragment implements OnClickListener {

    private static final String CODE = "ATM_CODE";

    private int layoutId = 0;

    private FancyButton instructionToggle;

    private LinearLayout instructionLayout;
    private boolean isInstructionShown = false;

    public static InstructionOtherBankFragment newInstance(int code) {
        InstructionOtherBankFragment fragment = new InstructionOtherBankFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CODE, code);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        if (getArguments() != null) {
            int fragmentCode = getArguments().getInt(CODE);
            initValue(fragmentCode);
        }

        View view = inflater.inflate(layoutId, container, false);
        instructionLayout = (LinearLayout) view.findViewById(R.id.instruction_layout);
        instructionToggle = (FancyButton) view.findViewById(R.id.instruction_toggle);
        instructionToggle.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.instruction_toggle) {
            isInstructionShown = !isInstructionShown;
            instructionToggle.setText(isInstructionShown ? getText(R.string.hide_instruction).toString() : getText(R.string.show_instruction).toString());
            instructionLayout.setVisibility(isInstructionShown ? View.VISIBLE : View.GONE);
            adjustEmailForm();
        }
    }

    private void initValue(final int code) {
        switch (code) {
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
    }

    public int getFragmentCode() {
        return getArguments() == null ? 0 : getArguments().getInt(CODE);
    }

    public boolean getInstructionFlag() {
        return isInstructionShown;
    }

    public void adjustEmailForm() {
        if (getActivity() instanceof BankTransferPaymentActivity) {
            if (isInstructionShown) {
                ((BankTransferPaymentActivity) getActivity()).showEmailForm();
            } else {
                ((BankTransferPaymentActivity) getActivity()).hideEmailForm();
            }
        }
    }
}
