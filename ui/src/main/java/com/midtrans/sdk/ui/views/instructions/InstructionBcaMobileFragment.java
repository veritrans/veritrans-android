package com.midtrans.sdk.ui.views.instructions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midtrans.sdk.ui.R;

/**
 * Displays instructions related to m-bca.
 *
 * @author rakawm
 */
public class InstructionBcaMobileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_instruction_bca_transfer_mbca, container, false);
        return view;
    }
}
