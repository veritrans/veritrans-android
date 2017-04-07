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
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentResponse;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseFragment;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.views.instructions.BankTransferInstructionActivity;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;

/**
 * Created by ziahaqi on 4/4/17.
 */

public class BankTransferMandiriStatusFragment extends BaseFragment {
    private static final String ARGS_RESPONSE = "paymentType.response";
    private static final String ARGS_TYPE = "paymentType.type";
    private static final String LABEL_VA_NUMBER = "Virtual Account Number";

    private DefaultTextView tvCompanyCode, tvBillpayCode, tvExpiration;
    private FancyButton buttonCopyCompanyCode, buttonBillPayCode, buttonSeeInstruction;
    private PaymentResult paymentResult;
    private String paymentType;

    public static BankTransferMandiriStatusFragment newInstance(PaymentResult paymentResult, String paymentType) {
        BankTransferMandiriStatusFragment fragment = new BankTransferMandiriStatusFragment();
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
        return inflater.inflate(R.layout.fragment_banktransfer_mandiri_status, container, false);
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
        tvCompanyCode = (DefaultTextView) view.findViewById(R.id.text_company_code);
        tvBillpayCode = (DefaultTextView) view.findViewById(R.id.text_bill_pay_code);
        tvExpiration = (DefaultTextView) view.findViewById(R.id.text_validaty);
        buttonCopyCompanyCode = (FancyButton) view.findViewById(R.id.btn_copy_company_code);
        buttonBillPayCode = (FancyButton) view.findViewById(R.id.btn_copy_billpay_code);
        buttonSeeInstruction = (FancyButton) view.findViewById(R.id.btn_see_instruction);
        setupView();
        bindDataToView();
    }

    private void bindDataToView() {

        MandiriBankTransferPaymentResponse response = (MandiriBankTransferPaymentResponse) paymentResult.getTransactionResponse();
        tvCompanyCode.setText(response.billKey);
        tvBillpayCode.setText(response.billerCode);
        tvExpiration.setText(response.billpaymentExpiration);

    }

    private void setupView() {
        // Set color themes
        setBorderColor(buttonSeeInstruction);
        setTextColor(buttonSeeInstruction);
        setBorderColor(buttonCopyCompanyCode);
        setTextColor(buttonCopyCompanyCode);
        setBorderColor(buttonBillPayCode);
        setTextColor(buttonBillPayCode);

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

        buttonCopyCompanyCode.setOnClickListener(new View.OnClickListener() {
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
        startActivity(intent);
    }

    /**
     * starts {@link BankTransferInstructionActivity} to show payment instruction.
     */
    private void showInstruction(String downloadUrl) {
        Intent intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
        intent.putExtra(BankTransferInstructionActivity.BANK, paymentType);
        intent.putExtra(BankTransferInstructionActivity.DOWNLOAD_URL, downloadUrl);
        startActivity(intent);
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
        ClipData clip = ClipData.newPlainText(LABEL_VA_NUMBER, tvCompanyCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }
}
