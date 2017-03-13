package com.midtrans.sdk.uikit.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BankTransferInstructionActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Displays status information about mandiri bill pay's api call . Created by shivam on 10/28/15.
 */
public class MandiriBillPayFragment extends Fragment {

    private static final String DATA = "data";
    private static final String LABEL_BILL_CODE = "Bill Code Number";
    private static final String LABEL_COMPANY_CODE = "Company Code Number";

    private TransactionResponse mTransactionResponse = null;

    //views
    private TextView mTextViewCompanyCode = null;
    private TextView mTextViewBillpayCode = null;
    private FancyButton btnSeeInstruction = null;
    private TextView mTextViewValidity = null;
    private FancyButton btnCopyBillCode = null;
    private FancyButton btnCopyCompany = null;


    /**
     * it creates new MandiriBillPayment object and set Transaction object to it, so later it can be
     * accessible using fragments getArgument().
     *
     * @param transactionResponse response of transaction call
     * @return instance of MandiriBillPayFragment.
     */
    public static MandiriBillPayFragment newInstance(TransactionResponse transactionResponse) {
        MandiriBillPayFragment fragment = new MandiriBillPayFragment();
        Bundle data = new Bundle();
        data.putSerializable(DATA, transactionResponse);
        fragment.setArguments(data);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mandiri_bill_pay, container, false);
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
        mTextViewCompanyCode = (TextView) view.findViewById(R.id.text_company_code);
        mTextViewBillpayCode = (TextView) view.findViewById(R.id.text_bill_pay_code);
        btnSeeInstruction = (FancyButton) view.findViewById(R.id.btn_see_instruction);
        mTextViewValidity = (TextView) view.findViewById(R.id.text_validaty);
        btnCopyBillCode = (FancyButton) view.findViewById(R.id.btn_copy_va);
        btnCopyCompany = (FancyButton) view.findViewById(R.id.btn_copy_company_code);
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                btnCopyBillCode.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnCopyBillCode.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnCopyCompany.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnCopyCompany.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnSeeInstruction.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnSeeInstruction.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            }
        }

        if (mTransactionResponse != null) {
            if (mTransactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_200)) ||
                    mTransactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_201))) {
                mTextViewCompanyCode.setText(mTransactionResponse.getCompanyCode());
            } else {
                mTextViewCompanyCode.setText(mTransactionResponse.getCompanyCode());
            }

            mTextViewBillpayCode.setText(mTransactionResponse.getPaymentCode());

            mTextViewValidity.setText(mTransactionResponse.getMandiriBillExpiration());
        }

        btnSeeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mTransactionResponse.getPdfUrl())) {
                    showInstruction(mTransactionResponse.getPdfUrl());
                } else {
                    showInstruction();
                }
            }
        });
        btnCopyBillCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyBillCode();
            }
        });
        btnCopyCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyCompanyCode();
            }
        });
    }

    /**
     * starts {@link BankTransferInstructionActivity} to show payment instruction.
     */
    private void showInstruction() {
        Intent intent = new Intent(getActivity(),
                BankTransferInstructionActivity.class);
        intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_MANDIRI_BILL);
        getActivity().startActivity(intent);
    }

    /**
     * starts {@link BankTransferInstructionActivity} to show payment instruction.
     */
    private void showInstruction(String downloadUrl) {
        Intent intent = new Intent(getActivity(),
                BankTransferInstructionActivity.class);
        intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_MANDIRI_BILL);
        intent.putExtra(BankTransferInstructionActivity.DOWNLOAD_URL, downloadUrl);
        getActivity().startActivity(intent);
    }

    /**
     * Copy generated Bill Code Number to clipboard.
     */
    private void copyBillCode() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_BILL_CODE, mTextViewBillpayCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_bill_code, Toast.LENGTH_SHORT).show();
    }

    /**
     * Copy generated Company Code Number to clipboard.
     */
    private void copyCompanyCode() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_COMPANY_CODE, mTextViewCompanyCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_company_code, Toast.LENGTH_SHORT).show();
    }
}