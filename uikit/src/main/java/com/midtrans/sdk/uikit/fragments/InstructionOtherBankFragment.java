package com.midtrans.sdk.uikit.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BaseActivity;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by Fajar on 8/18/17.
 */

public class InstructionOtherBankFragment extends Fragment implements OnClickListener {

    private static final String CODE = "ATM_CODE";
    private static final String TAG = InstructionOtherBankFragment.class.getSimpleName();

    private int layoutId = 0;

    private FancyButton instructionToggle;

    private LinearLayout instructionLayout;
    private boolean isInstructionShown = false;
    private OnInstructionShownListener listener;
    private int colorPrimary = 0;
    private int colorPrimaryDark = 0;

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

        try {
            colorPrimary = ((BaseActivity) getActivity()).getPrimaryColor();
            colorPrimaryDark = ((BaseActivity) getActivity()).getPrimaryDarkColor();

            if (colorPrimary != 0) {
                instructionToggle.setTextColor(colorPrimary);
            }

            if (colorPrimaryDark != 0) {
                instructionToggle.setIconColorFilter(colorPrimaryDark);
            }

        } catch (RuntimeException e) {
            Log.e(TAG, "sdkColor:" + e.getMessage());
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
            instructionToggle.setText(isInstructionShown ? getText(R.string.hide_instruction).toString() : getText(R.string.show_instruction).toString());
            instructionToggle.setIconResource(isInstructionShown ? R.drawable.ic_hide : R.drawable.ic_view);

            if (colorPrimaryDark != 0) {
                instructionToggle.setIconColorFilter(colorPrimaryDark);
            }
            instructionLayout.setVisibility(isInstructionShown ? View.VISIBLE : View.GONE);
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

    @Override
    public void onDestroyView() {
        if (listener != null) {
            listener.onInstructionShown(false, getFragmentCode());
            listener = null;
        }
        super.onDestroyView();
    }

    public interface OnInstructionShownListener {
        void onInstructionShown(boolean isShown, int fragmentCode);
    }
}
