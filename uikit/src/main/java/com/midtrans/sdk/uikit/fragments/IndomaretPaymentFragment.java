package com.midtrans.sdk.uikit.fragments;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
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
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Displays status information about bank transfer's api call .
 *
 * Created by shivam on 10/27/15.
 * Deprecated, please refer to {@link com.midtrans.sdk.uikit.views.indomaret.payment.IndomaretPaymentActivity}
 */
@Deprecated
public class IndomaretPaymentFragment extends Fragment {

    public static final String KEY_ARG = "arg";
    private static final String LABEL_PAYMENT_CODE = "Payment Code";
    private TransactionResponse transactionResponse;

    //views

    private TextView mTextViewValidity = null;
    private TextView mTextViewPaymentCode = null;
    private FancyButton btnCopyToClipboard = null;


    public static IndomaretPaymentFragment newInstance(TransactionResponse mTransactionResponse) {
        IndomaretPaymentFragment fragment = new IndomaretPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ARG, mTransactionResponse);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_indomaret_transfer_payment, container, false);
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

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                btnCopyToClipboard.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnCopyToClipboard.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            }
        }

        if (transactionResponse != null) {
            if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_200))
                    || transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_201))) {
                mTextViewValidity.setText(transactionResponse.getIndomaretExpireTime());
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