package com.midtrans.sdk.uikit.abstracts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 12/5/17.
 */

public  class VaInstructionFragment extends Fragment {

    public static final String CODE = "instruction.code";

    protected OnInstructionShownListener listener;
    protected int layoutId = 0;
    protected FancyButton instructionToggle;
    protected LinearLayout instructionLayout;
    protected boolean isInstructionShown = false;

    public interface OnInstructionShownListener {
        void onInstructionShown(boolean isShown, int fragmentCode);
    }

    public static <T extends Fragment> T newInstance(int code, T dat) {
        T fragment = new dat.();
        Bundle bundle = new Bundle();
        bundle.putInt(CODE, code);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            int fragmentCode = getArguments().getInt(CODE);
            int layoutId = initValue(fragmentCode);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public abstract int initValue(int fragmentCode);
}
