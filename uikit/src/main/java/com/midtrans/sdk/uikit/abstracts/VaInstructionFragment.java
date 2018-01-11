package com.midtrans.sdk.uikit.abstracts;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.views.banktransfer.instruction.InstructionPermataVaFragment;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

/**
 * Created by ziahaqi on 12/5/17.
 */

public abstract class VaInstructionFragment extends Fragment implements View.OnClickListener {

    public static final String INSTRUCTION_POSITION = "instruction.position";
    public static final String INSTRUCTION_TITLE = "instruction.title";

    private final String OTHER_VA_PROCESSOR_BNI = "bni_va";

    protected OnInstructionShownListener listener;
    protected int layoutId = 0;
    protected AppCompatButton instructionToggle;
    protected LinearLayout instructionLayout;
    protected boolean isInstructionShown = false;
    private int colorPrimary = 0;

    public interface OnInstructionShownListener {
        void onInstructionShown(boolean isShown, int fragmentCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //dynamic instruction for supporting other VA switching
        boolean isUsingPermata = true;
        try {
            String otherVaProcessor = MidtransSDK.getInstance().getMerchantData().getPreference().getOtherVaProcessor();
            isUsingPermata = !otherVaProcessor.equalsIgnoreCase(OTHER_VA_PROCESSOR_BNI);
        } catch (RuntimeException exception) {
            Logger.e(exception.getMessage());
        }

        layoutId = initLayoutId();
        //specific for Alto, since the diff is significant, we use another layout for VA switching
        if (layoutId == R.layout.fragment_instruction_alto && !isUsingPermata && !(this instanceof InstructionPermataVaFragment)) {
            layoutId = R.layout.fragment_instruction_alto_bni;
            isUsingPermata = !isUsingPermata;
        }
        View view = inflater.inflate(layoutId, container, false);

        if (!isUsingPermata) {
            ImageView bankCode = (ImageView) view.findViewById(R.id.instruction_bank_code_image);
            DefaultTextView bankCodeInstruction = (DefaultTextView) view.findViewById(R.id.instruction_bank_code_text);
            if (layoutId == R.layout.fragment_instruction_atm_bersama) {
                bankCode.setImageResource(R.drawable.instruction_atm_bersama_4_bni);
                bankCodeInstruction.setText(R.string.instruction_atm_bersama4_bni);
            } else if (layoutId == R.layout.fragment_instruction_prima) {
                bankCode.setImageResource(R.drawable.instruction_prima_4_bni);
                bankCodeInstruction.setText(R.string.instruction_prima4_bni);
            }
        }

        instructionLayout = (LinearLayout) view.findViewById(R.id.instruction_layout);
        instructionToggle = (AppCompatButton) view.findViewById(R.id.instruction_toggle);

        instructionToggle.setOnClickListener(this);
        instructionToggle.setText(getString(R.string.payment_instruction, getFragmentTitle()));
        colorPrimary = ((BaseActivity) getActivity()).getPrimaryColor();

        int colorPrimaryDark = ((BaseActivity) getActivity()).getPrimaryDarkColor();
        if (colorPrimaryDark != 0) {
            instructionToggle.setTextColor(colorPrimaryDark);
        }
        filterInstructionToggle();

        return view;
    }

    private void filterInstructionToggle() {
        if (colorPrimary != 0) {
            Drawable drawable;

            if (isInstructionShown) {
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_expand_less);
            } else {
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_expand_more);
            }

            try {
                drawable.setColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN);
                instructionToggle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            } catch (RuntimeException e) {

            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnInstructionShownListener) context;
        } catch (ClassCastException e) {
            Logger.e("The activity needs to implement interface first.");
        }
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.instruction_toggle) {
            isInstructionShown = !isInstructionShown;
            if (listener != null) {
                listener.onInstructionShown(isInstructionShown, getFragmentCode());
            }

            if (isInstructionShown) {
                instructionToggle.setSelected(true);
                instructionLayout.setVisibility(View.VISIBLE);
            } else {
                instructionToggle.setSelected(false);
                instructionLayout.setVisibility(View.GONE);
            }
            filterInstructionToggle();
        }
    }

    public int getFragmentCode() {
        return getArguments() == null ? 0 : getArguments().getInt(INSTRUCTION_POSITION);
    }

    public String getFragmentTitle() {
        return getArguments() == null ? "" : getArguments().getString(INSTRUCTION_TITLE, "");
    }

    public abstract int initLayoutId();

    @Override
    public void onDestroyView() {
        listener = null;
        super.onDestroyView();
    }
}
