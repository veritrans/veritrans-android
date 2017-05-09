package com.midtrans.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.midtrans.demo.widgets.DemoRadioButton;

/**
 * Created by ziahaqi on 5/8/17.
 */

public class InstallmentDialogFragment extends DialogFragment {

    private static final String ARG_LISTENER = "arg.listener";
    private static final String ARG_COLOR = "arg.color";
    private static final String ARG_REQUIRED = "arg.required";

    private boolean required;
    private int color;
    private CustomInstallmentDialogListener listener;

    private DemoRadioButton requiredSelection;
    private DemoRadioButton optionalSelection;
    private Button okButton;
    private Button cancelButton;

    public static InstallmentDialogFragment newInstance(boolean required, int color, CustomInstallmentDialogListener listener) {
        Log.d("xinstall", "required:" + required);
        InstallmentDialogFragment fragment = new InstallmentDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_LISTENER, listener);
        bundle.putInt(ARG_COLOR, color);
        bundle.putBoolean(ARG_REQUIRED, required);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_demo_installment_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initExtras();
        initValues();
        initThemes();
        initButtons();
    }

    private void initViews(View view) {
        requiredSelection = (DemoRadioButton) view.findViewById(R.id.radio_installment_required);
        optionalSelection = (DemoRadioButton) view.findViewById(R.id.radio_installment_optional);
        okButton = (Button) view.findViewById(R.id.ok_button);
        cancelButton = (Button) view.findViewById(R.id.cancel_button);
    }

    private void initThemes() {
        if (color != 0) {
            okButton.setTextColor(color);
            cancelButton.setTextColor(color);
        }
    }

    private void initExtras() {
        this.listener = (CustomInstallmentDialogListener) getArguments().getSerializable(ARG_LISTENER);
        this.color = getArguments().getInt(ARG_COLOR);
        this.required = getArguments().getBoolean(ARG_REQUIRED, false);
    }

    private void initValues() {
        if (required) {
            requiredSelection.setChecked(true);
        } else {
            optionalSelection.setChecked(true);
        }
    }

    private void initButtons() {
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (listener != null) {
                    dismiss();
                    listener.onOkClicked(requiredSelection.isChecked());
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (listener != null) {
                    listener.onCancelClicked();
                }
            }
        });
    }
}
