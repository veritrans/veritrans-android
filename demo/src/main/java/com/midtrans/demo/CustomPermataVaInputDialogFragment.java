package com.midtrans.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by rakawm on 4/27/17.
 */

public class CustomPermataVaInputDialogFragment extends DialogFragment {

    private static final String ARG_NUMBER = "arg.number";
    private static final String ARG_LISTENER = "arg.listener";
    private static final String ARG_COLOR = "arg.color";

    private String number;
    private int color;
    private CustomVaDialogListener listener;
    private EditText customVAField;
    private TextInputLayout customVaContainer;
    private Button okButton;
    private Button cancelButton;

    public static CustomPermataVaInputDialogFragment newInstance(int color, CustomVaDialogListener listener) {
        CustomPermataVaInputDialogFragment fragment = new CustomPermataVaInputDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_LISTENER, listener);
        bundle.putInt(ARG_COLOR, color);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CustomPermataVaInputDialogFragment newInstance(String number, int color, CustomVaDialogListener listener) {
        CustomPermataVaInputDialogFragment fragment = new CustomPermataVaInputDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_LISTENER, listener);
        bundle.putInt(ARG_COLOR, color);
        bundle.putString(ARG_NUMBER, number);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_custom_permata_va, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initExtras();
        initThemes();
        initButtons();
    }

    private void initViews(View view) {
        customVAField = (EditText) view.findViewById(R.id.permata_va_field);
        customVaContainer = (TextInputLayout) view.findViewById(R.id.permata_va_field_container);
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
        this.listener = (CustomVaDialogListener) getArguments().getSerializable(ARG_LISTENER);
        this.color = getArguments().getInt(ARG_COLOR);
        if (!TextUtils.isEmpty(getArguments().getString(ARG_NUMBER))) {
            this.number = getArguments().getString(ARG_NUMBER);
            initNumber(number);
        }
    }

    private void initButtons() {
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    dismiss();
                    customVaContainer.setError(null);
                    if (listener != null) {
                        listener.onOkClicked(customVAField.getText().toString());
                    }
                } else {
                    customVaContainer.setError(getString(R.string.custom_permata_va_error));
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

    private boolean isInputValid() {
        String customVaNumber = customVAField.getText().toString();
        return customVaNumber.length() == 10;
    }

    private void initNumber(String number) {
        customVAField.setText(number);
    }
}
