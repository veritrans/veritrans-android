package com.midtrans.sdk.ui.views.banktransfer.payment;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentResponse;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseFragment;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.views.banktransfer.instruction.BankTransferInstructionActivity;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;

/**
 * Created by ziahaqi on 4/4/17.
 */

public class BankTransferStatusFragment extends BaseFragment implements BankTransferPaymentContract.bankTransferStatusView {
    private static final String ARGS_RESPONSE = "paymentType.response";
    private static final String ARGS_TYPE = "paymentType.type";
    private static final String LABEL_VA_NUMBER = "Virtual Account Number";

    private DefaultTextView tvVirtualAccountNumber, tvExpiration;
    private FancyButton buttonCopy, buttonSeeInstruction;
    private PaymentResult paymentResult;
    private String paymentType;

    public static BankTransferStatusFragment newInstance(PaymentResult paymentResult, String paymentType) {
        BankTransferStatusFragment fragment = new BankTransferStatusFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARGS_RESPONSE, paymentResult);
        bundle.putString(ARGS_TYPE
                , paymentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initProperties();
        return inflater.inflate(R.layout.fragment_banktransfer_status, container, false);
    }

    private void initProperties() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.paymentResult = (PaymentResult) bundle.getSerializable(ARGS_RESPONSE);
            this.paymentType = bundle.getString(ARGS_TYPE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvVirtualAccountNumber = (DefaultTextView) view.findViewById(R.id.text_virtual_account_number);
        tvExpiration = (DefaultTextView) view.findViewById(R.id.text_validaty);
        buttonCopy = (FancyButton) view.findViewById(R.id.btn_copy_va);
        buttonSeeInstruction = (FancyButton) view.findViewById(R.id.btn_see_instruction);
        setupView();
        bindDataToView();
    }

    private void bindDataToView() {
        if (paymentResult == null) {
            return;
        }

        String vaNumber = "";
        String expiration = "";

        if (paymentType.equals(PaymentType.BCA_VA)) {
            BcaBankTransferPaymentResponse response = (BcaBankTransferPaymentResponse) paymentResult.getTransactionResponse();
            vaNumber = response.bcaVaNumber;
            expiration = response.bcaExpiration;
        } else if (paymentType.equals(PaymentType.E_CHANNEL)) {

        } else if (paymentType.equals(PaymentType.PERMATA_VA)) {
            PermataBankTransferPaymentResponse response = (PermataBankTransferPaymentResponse) paymentResult.getTransactionResponse();
            vaNumber = response.permataVaNumber;
            expiration = response.permataExpiration;
        } else if (paymentType.equals(PaymentType.OTHER_VA)) {
            OtherBankTransferPaymentResponse response = (OtherBankTransferPaymentResponse) paymentResult.getTransactionResponse();
            vaNumber = response.permataVaNumber;
            expiration = response.permataExpiration;
        }

        tvVirtualAccountNumber.setText(vaNumber);
        tvExpiration.setText(expiration);
    }

    private void setupView() {
        buttonSeeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pdfUrl = ((BankTransferPaymentResponse) paymentResult.getTransactionResponse()).pdfUrl;
                if (TextUtils.isEmpty(pdfUrl)) {
                    showInstruction();
                } else {
                    showInstruction(pdfUrl);
                }
            }
        });

        buttonCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyVANumber();
            }
        });
    }

    /**
     * starts BankTransferInstructionActivityto show payment instruction.
     */
    private void showInstruction() {
        Intent intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
        intent.putExtra(BankTransferInstructionActivity.BANK, paymentType);
        ((BankTransferPaymentActivity) getActivity()).startActivity(intent);
    }

    /**
     * starts {@link BankTransferInstructionActivity} to show payment instruction.
     */
    private void showInstruction(String downloadUrl) {
        Intent intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
        intent.putExtra(BankTransferInstructionActivity.BANK, paymentType);
        intent.putExtra(BankTransferInstructionActivity.DOWNLOAD_URL, downloadUrl);
        ((BankTransferPaymentActivity) getActivity()).startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Copy generated Virtual Account Number to clipboard.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void copyVANumber() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_VA_NUMBER, tvVirtualAccountNumber.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }
}
