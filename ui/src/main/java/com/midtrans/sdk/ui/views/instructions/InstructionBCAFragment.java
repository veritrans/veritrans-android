package com.midtrans.sdk.ui.views.instructions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midtrans.sdk.ui.R;

/**
 * Displays instructions related to BCA/Prima
 * <p>
 * Created by shivam on 10/28/15.
 */
public class InstructionBCAFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_instruction_bca, container, false);
        return view;
    }
}
