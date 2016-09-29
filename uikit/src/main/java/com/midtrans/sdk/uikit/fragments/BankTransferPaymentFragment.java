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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BankTransferInstructionActivity;

/**
 * Displays status information about bank transfer's api call .
 *
 * Created by shivam on 10/27/15.
 */
public class BankTransferPaymentFragment extends Fragment {

    public static final String KEY_ARG = "args";
    public static final String VALID_UNTIL = "Valid Until : ";
    private static final String LABEL_VA_NUMBER = "Virtual Account Number";
    private TransactionResponse transactionResponse;

    //views
    private TextView mTextViewVirtualAccountNumber = null;
    private Button btnSeeInstruction = null;
    private TextView mTextViewValidity = null;
    private Button btnCopyToClipboard = null;


    /**
     * it creates new BankTransferPaymentFragment object and set Transaction object to it, so later
     * it can be accessible using fragments getArgument().
     *
     * @param transactionResponse response of transaction call
     * @return instance of BankTransferPaymentFragment
     */
    public static BankTransferPaymentFragment newInstance(TransactionResponse transactionResponse, String bank) {
        BankTransferPaymentFragment fragment = new BankTransferPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ARG, transactionResponse);
        bundle.putString(BankTransferInstructionActivity.BANK, bank);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bank_transfer_payment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        transactionResponse = (TransactionResponse) getArguments().getSerializable(KEY_ARG);
        initializeViews(view);
    }


    /**
     * initializes view and adds click listener for it.
     *
     * @param view view that needed to be initialized
     */
    private void initializeViews(View view) {

        mTextViewVirtualAccountNumber = (TextView) view.findViewById(R.id.text_virtual_account_number);
        btnSeeInstruction = (Button) view.findViewById(R.id.btn_see_instruction);
        mTextViewValidity = (TextView) view.findViewById(R.id.text_validaty);
        btnCopyToClipboard = (Button) view.findViewById(R.id.btn_copy_va);

        if (transactionResponse != null) {
            if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_200))
                    || transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_201))) {
                if (getArguments() != null && getArguments().getString(BankTransferInstructionActivity.BANK) != null && getArguments().getString(BankTransferInstructionActivity.BANK).equals(BankTransferInstructionActivity.TYPE_BCA)) {
                    if (transactionResponse.getAccountNumbers() != null && transactionResponse.getAccountNumbers().size() > 0) {
                        mTextViewVirtualAccountNumber.setText(transactionResponse.getAccountNumbers().get(0).getAccountNumber());
                    }
                } else {
                    mTextViewVirtualAccountNumber.setText(transactionResponse.getPermataVANumber());
                }
            } else {
                mTextViewVirtualAccountNumber.setText(transactionResponse.getStatusMessage());
            }

            mTextViewValidity.setText(VALID_UNTIL + Utils.getValidityTime(transactionResponse.getTransactionTime()));
        }
        btnSeeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(transactionResponse.getPdfUrl())) {
                    showInstruction(transactionResponse.getPdfUrl());
                } else {
                    showInstruction();
                }
            }
        });
        btnCopyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyVANumber();
            }
        });
    }

    /**
     * starts {@link BankTransferInstructionActivity} to show payment instruction.
     */
    private void showInstruction() {
        Intent intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
        intent.putExtra(BankTransferInstructionActivity.BANK, getArguments().getString(BankTransferInstructionActivity.BANK));
        getActivity().startActivity(intent);
    }

    private void showInstruction(String downloadUrl) {
        Intent intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
        intent.putExtra(BankTransferInstructionActivity.BANK, getArguments().getString(BankTransferInstructionActivity.BANK));
        intent.putExtra(BankTransferInstructionActivity.DOWNLOAD_URL, downloadUrl);
        getActivity().startActivity(intent);
    }

    /**
     * Copy generated Virtual Account Number to clipboard.
     */
    private void copyVANumber() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_VA_NUMBER, mTextViewVirtualAccountNumber.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }
}