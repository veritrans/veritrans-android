package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.midtrans.sdk.uikit.R;

/**
 * Created by ziahaqi on 8/26/16.
 * Deprecated, please refer to {@link com.midtrans.sdk.uikit.views.kioson.payment.KiosonPaymentActivity}
 */
@Deprecated
public class InstructionKiosonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instruction_kioson, container, false);
        return view;
    }
}
