package com.midtrans.sdk.uikit.fragments;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.KiosonInstructionActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 8/26/16.
 * Deprecated, please see {@link com.midtrans.sdk.uikit.views.kioson.payment.KiosonPaymentActivity}
 */
@Deprecated
public class KiosonPaymentFragment extends Fragment {
    public static final String KEY_ARG = "arg";
    private static final String LABEL_PAYMENT_CODE = "Payment Code";
    private TransactionResponse transactionResponse;

    //views

    private TextView mTextViewValidity = null;
    private TextView mTextViewPaymentCode = null;
    private FancyButton btnCopyToClipboard = null;
    private FancyButton btnSeeInstruction;


    public static KiosonPaymentFragment newInstance(TransactionResponse mTransactionResponse) {
        KiosonPaymentFragment fragment = new KiosonPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ARG, mTransactionResponse);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_kioson_payment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get data from argument
        if (getArguments() != null && getArguments().getSerializable(KEY_ARG) != null) {
            transactionResponse = (TransactionResponse) getArguments().getSerializable(KEY_ARG);
        }

        initializeViews(view);
    }


    /**
     * initializes view and adds click listener for it.
     *
     * @param view view that needed to be initialized
     */
    private void initializeViews(View view) {
        mTextViewValidity = (TextView) view.findViewById(R.id.text_validity);
        mTextViewPaymentCode = (TextView) view.findViewById(R.id.text_payment_code);
        btnCopyToClipboard = (FancyButton) view.findViewById(R.id.btn_copy_va);
        btnSeeInstruction = (FancyButton) view.findViewById(R.id.btn_see_instruction);

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                btnSeeInstruction.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnSeeInstruction.setIconColorFilter(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnCopyToClipboard.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnCopyToClipboard.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            }
        }

        if (transactionResponse != null) {
            if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_200))
                    || transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_201))) {
                mTextViewValidity.setText(transactionResponse.getKiosonExpireTime());
            }
            if (transactionResponse.getPaymentCodeResponse() != null)
                mTextViewPaymentCode.setText(transactionResponse.getPaymentCodeResponse());
        }
        btnCopyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyPaymentCode();
            }
        });
        btnSeeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), KiosonInstructionActivity.class));
                if (MidtransSDK.getInstance().getUIKitCustomSetting()!=null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            }
        });
    }

    /**
     * Copy payment code into clipboard.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyPaymentCode() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_PAYMENT_CODE, mTextViewPaymentCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }
}
