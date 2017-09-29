package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

/**
 * Deprecated, please see {@link com.midtrans.sdk.uikit.views.bca_klikpay.BcaKlikPayPaymentActivity}
 * @author rakawm
 */
@Deprecated
public class BCAKlikPayInstructionFragment extends Fragment {

    private DefaultTextView textNotification;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_instruction_bca_klik, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textNotification = (DefaultTextView) view.findViewById(R.id.text_notification);
        showOtpNotification();
    }

    private void showOtpNotification() {
        Animation slideIn = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top);
        textNotification.startAnimation(slideIn);
    }
}
