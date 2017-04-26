package com.midtrans.sdk.uikit.widgets;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.midtrans.sdk.uikit.R;

/**
 * Created by rakawm on 4/25/17.
 */

public class MidtransProgressDialogFragment extends AppCompatDialogFragment {
    private static final String ARG_MESSAGE = "dialog.message";

    public static MidtransProgressDialogFragment newInstance(String message) {
        MidtransProgressDialogFragment fragment = new MidtransProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_MESSAGE, message);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static MidtransProgressDialogFragment newInstance() {
        return new MidtransProgressDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_midtrans_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMessageTitle(view);
        initProgressAnimation(view);
    }

    private void initMessageTitle(View view) {
        if (getArguments() != null) {
            String message = getArguments().getString(ARG_MESSAGE);
            if (message != null && !TextUtils.isEmpty(message)) {
                TextView messageView = (TextView) view.findViewById(R.id.progress_bar_message);
                messageView.setText(message);
            }
        }
    }

    private void initProgressAnimation(View view) {
        ImageView progressView = (ImageView) view.findViewById(R.id.progress_bar_image);
        Glide.with(getContext())
                .load(R.drawable.midtrans_loader)
                .asGif()
                .into(progressView);
    }
}
