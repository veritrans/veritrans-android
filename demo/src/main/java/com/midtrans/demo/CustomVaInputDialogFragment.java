package com.midtrans.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
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

public class CustomVaInputDialogFragment extends DialogFragment {

    private static final String ARG_LISTENER = "arg.listener";
    private static final String ARG_COLOR = "arg.color";
    private static final String ARG_NUMBER = "arg.number";
    private static final String ARG_TITLE = "arg.title";

    private String number;
    private int color;
    private CustomVaDialogListener listener;
    private EditText customVAField;
    private TextInputLayout customVaContainer;
    private Button okButton;
    private Button cancelButton;
    private TextView title;

    private String vaTitle;

    public static CustomVaInputDialogFragment newInstance(String title, int color,
        CustomVaDialogListener listener) {
        CustomVaInputDialogFragment fragment = new CustomVaInputDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_LISTENER, listener);
        bundle.putInt(ARG_COLOR, color);
        bundle.putString(ARG_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static CustomVaInputDialogFragment newInstance(String number, String title, int color,
        CustomVaDialogListener listener) {
        CustomVaInputDialogFragment fragment = new CustomVaInputDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_LISTENER, listener);
        bundle.putInt(ARG_COLOR, color);
        bundle.putString(ARG_NUMBER, number);
        bundle.putString(ARG_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_custom_bca_va, container, false);
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
        title = (TextView) view.findViewById(R.id.custom_va_title);
        customVAField = (EditText) view.findViewById(R.id.bca_va_field);
        customVaContainer = (TextInputLayout) view.findViewById(R.id.bca_va_field_container);
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
        this.vaTitle = getArguments().getString(ARG_TITLE);
        setTitle(vaTitle);
        initInputFilter();
    }

    private void initButtons() {
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (listener != null) {
                    listener.onOkClicked(customVAField.getText().toString());
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

    private void initNumber(String number) {
        customVAField.setText(number);
    }

    private void setTitle(String vaTitle) {
        title.setText(getString(R.string.format_va_enable_title, vaTitle));
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = AppBarLayout.LayoutParams.MATCH_PARENT;
        params.height = AppBarLayout.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    private void initInputFilter() {
        InputFilter[] filterArray = new InputFilter[1];

        if (vaTitle.equalsIgnoreCase(getString(R.string.bni_va_title))) {
            filterArray[0] = new InputFilter.LengthFilter(8);
        } else if (vaTitle.equalsIgnoreCase(getString(R.string.bca_va_title))) {
            filterArray[0] = new InputFilter.LengthFilter(11);
        } else if (vaTitle.equalsIgnoreCase(getString(R.string.subcompany_bca_va_title))) {
            filterArray[0] = new LengthFilter(5);
        }

        customVAField.setFilters(filterArray);
    }
}
