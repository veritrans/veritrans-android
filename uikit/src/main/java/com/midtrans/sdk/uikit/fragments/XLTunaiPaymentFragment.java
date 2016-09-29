package com.midtrans.sdk.uikit.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;

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
    private Button btnCopyOrderId = null;
    private Button btnCopyMerchantCode = null;

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
        mTextViewValidity = (TextView) view.findViewById(R.id.text_validaty);
        btnCopyOrderId = (Button) view.findViewById(R.id.btn_copy_order_id);
        btnCopyMerchantCode = (Button) view.findViewById(R.id.btn_copy_merchant_code);

        if (mTransactionResponse != null) {
            mTextViewOrderId.setText(mTransactionResponse.getXlTunaiOrderId());

            mTextViewMerchantCode.setText(mTransactionResponse.getXlTunaiMerchantId());

            mTextViewValidity.setText(getString(R.string.text_format_valid_until, mTransactionResponse.getXlTunaiExpiration()));
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

    private void copyOrderId() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_ORDER_ID, mTextViewOrderId.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_order_id, Toast.LENGTH_SHORT).show();
    }

    private void copyMerchantCode() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_MERCHANT_CODE, mTextViewMerchantCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_merchant_code, Toast.LENGTH_SHORT).show();
    }


}
