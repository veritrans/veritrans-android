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
 * @author rakawm
 */
public class XLTunaiPaymentFragment extends Fragment {

    private static final String DATA = "data";
    private static final String LABEL_ORDER_ID = "Order ID";
    private static final String LABEL_MERCHANT_CODE = "Merchant Code";

    private TransactionResponse mTransactionResponse = null;

    private TextView mTextViewOrderId = null;
    private TextView mTextViewMerchantCode = null;
    private TextView mTextViewValidity = null;
    private FancyButton btnCopyOrderId = null;
    private FancyButton btnCopyMerchantCode = null;

    public static XLTunaiPaymentFragment newInstance(TransactionResponse transactionResponse) {
        XLTunaiPaymentFragment fragment = new XLTunaiPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA, transactionResponse);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_xl_tunai, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTransactionResponse = (TransactionResponse) getArguments().getSerializable(DATA);
        }
        initializeViews(view);
    }

    /**
     * initializes view and adds click listener for it.
     *
     * @param view view that needed to be initialized
     */
    private void initializeViews(View view) {
        mTextViewOrderId = (TextView) view.findViewById(R.id.text_order_id);
        mTextViewMerchantCode = (TextView) view.findViewById(R.id.text_merchant_code);
        mTextViewValidity = (TextView) view.findViewById(R.id.text_validity);
        btnCopyOrderId = (FancyButton) view.findViewById(R.id.btn_copy_order_id);
        btnCopyMerchantCode = (FancyButton) view.findViewById(R.id.btn_copy_merchant_code);

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                btnCopyOrderId.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnCopyOrderId.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnCopyMerchantCode.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnCopyMerchantCode.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            }
        }

        if (mTransactionResponse != null) {
            mTextViewOrderId.setText(mTransactionResponse.getXlTunaiOrderId());

            mTextViewMerchantCode.setText(mTransactionResponse.getXlTunaiMerchantId());

            mTextViewValidity.setText(mTransactionResponse.getXlTunaiExpiration());
        }

        btnCopyOrderId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyOrderId();
            }
        });
        btnCopyMerchantCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyMerchantCode();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyOrderId() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_ORDER_ID, mTextViewOrderId.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_order_id, Toast.LENGTH_SHORT).show();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyMerchantCode() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_MERCHANT_CODE, mTextViewMerchantCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_merchant_code, Toast.LENGTH_SHORT).show();
    }


}
