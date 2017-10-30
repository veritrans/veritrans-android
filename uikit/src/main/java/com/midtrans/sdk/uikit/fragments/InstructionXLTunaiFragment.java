package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.midtrans.sdk.uikit.R;

/**
 * @author rakawm
 * Deprecated, please refer to {@link com.midtrans.sdk.uikit.views.xl_tunai.payment.XlTunaiPaymentActivity}
 */
@Deprecated
public class InstructionXLTunaiFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instruction_xl_tunai, container, false);
    }
}
