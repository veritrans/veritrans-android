package com.midtrans.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by rakawm on 4/27/17.
 */

public class CustomPermataVaInputDialogFragment extends DialogFragment {

    private static final String ARG_INPUT = "arg.input";
    private static final String ARG_LISTENER = "arg.listener";
    private static final String ARG_COLOR = "arg.color";
    private static final String ARG_IS_RECIPIENT_DIALOG = "arg.recipient";

    private String input;
    private int color;
    private CustomVaDialogListener listener;
    private EditText customVAField, customRecipientField;
    private TextView customDialogTitle;
    private TextInputLayout customVaContainer;
    private TextInputLayout customRecipientContainer;
    private Button okButton;
    private Button cancelButton;
    private boolean isRecipientDialog;

    public static CustomPermataVaInputDialogFragment newInstance(int color, boolean isRecipientDialog, CustomVaDialogListener listener) {
        CustomPermataVaInputDialogFragment fragment = new CustomPermataVaInputDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_LISTENER, listener);
        bundle.putInt(ARG_COLOR, color);
        bundle.putBoolean(ARG_IS_RECIPIENT_DIALOG, isRecipientDialog);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CustomPermataVaInputDialogFragment newInstance(String number, int color, boolean isRecipientDialog, CustomVaDialogListener listener) {
        CustomPermataVaInputDialogFragment fragment = new CustomPermataVaInputDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_LISTENER, listener);
        bundle.putInt(ARG_COLOR, color);
        bundle.putString(ARG_INPUT, number);
        bundle.putBoolean(ARG_IS_RECIPIENT_DIALOG, isRecipientDialog);
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
        initButtonsAndTitle();
    }

    private void initViews(View view) {
        customVAField = (EditText) view.findViewById(R.id.permata_va_field);
        customRecipientField = (EditText) view.findViewById(R.id.permata_recipient_field);
        customDialogTitle = (TextView) view.findViewById(R.id.permata_custom_dialog_title);
        customVaContainer = (TextInputLayout) view.findViewById(R.id.permata_va_field_container);
        customRecipientContainer = (TextInputLayout) view.findViewById(R.id.permata_recipient_field_container);
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
        this.isRecipientDialog = getArguments().getBoolean(ARG_IS_RECIPIENT_DIALOG);
        if (!TextUtils.isEmpty(getArguments().getString(ARG_INPUT))) {
            this.input = getArguments().getString(ARG_INPUT);
            initInput(input);
        }
    }

    private void initButtonsAndTitle() {
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInputValid()) {
                    dismiss();
                    customVaContainer.setError(null);
                    if (listener != null) {
                        listener.onOkClicked(isRecipientDialog ? customRecipientField.getText().toString().toUpperCase() : customVAField.getText().toString());
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

        if (isRecipientDialog) {
            customDialogTitle.setText(R.string.custom_permata_recipient_enable_title);
            customRecipientContainer.setVisibility(View.VISIBLE);
            customVaContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = AppBarLayout.LayoutParams.MATCH_PARENT;
        params.height = AppBarLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    private boolean isInputValid() {
        if (isRecipientDialog) {
            return true;
        }
        String customVaNumber = customVAField.getText().toString();
        return customVaNumber.length() == 10;
    }

    private void initInput(String input) {
        if (isRecipientDialog) {
            customRecipientField.setText(input);
        } else {
            customVAField.setText(input);
        }
    }
}
