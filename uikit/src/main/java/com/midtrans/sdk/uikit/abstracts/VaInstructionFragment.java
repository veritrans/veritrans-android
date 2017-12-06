package com.midtrans.sdk.uikit.abstracts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

/**
 * Created by ziahaqi on 12/5/17.
 */

public abstract class VaInstructionFragment extends Fragment implements View.OnClickListener {

    public static final String CODE = "instruction.code";

    protected OnInstructionShownListener listener;
    protected int layoutId = 0;
    protected Button instructionToggle;
    protected LinearLayout instructionLayout;
    protected boolean isInstructionShown = false;


    public interface OnInstructionShownListener {
        void onInstructionShown(boolean isShown, int fragmentCode);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutId = initLayoutId();
        View view = inflater.inflate(layoutId, container, false);
        instructionLayout = (LinearLayout) view.findViewById(R.id.instruction_layout);
        instructionToggle = (Button) view.findViewById(R.id.instruction_toggle);
        instructionToggle.setOnClickListener(this);

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null) {
            instructionToggle.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
//            instructionToggle.setIconColorFilter(midtransSDK.getColorTheme().getPrimaryDarkColor());
        }

        return view;
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

            if(isInstructionShown){
                instructionToggle.setSelected(true);
                instructionToggle.setText(getText(R.string.hide_instruction).toString());
                instructionLayout.setVisibility(View.VISIBLE);

            }else{
                instructionToggle.setSelected(false);
                instructionToggle.setText(getText(R.string.show_instruction).toString());
                instructionLayout.setVisibility(View.GONE);
            }
        }
    }

    protected int getFragmentCode() {
        return getArguments() == null ? 0 : getArguments().getInt(CODE);
    }

    public abstract int initLayoutId();
}
