package com.midtrans.sdk.uikit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BankTransferActivity;
import com.midtrans.sdk.uikit.activities.BankTransferInstructionActivity;
import com.midtrans.sdk.uikit.activities.IndosatDompetkuActivity;
import com.midtrans.sdk.uikit.activities.MandiriClickPayActivity;

import java.util.regex.Pattern;

/**
 * Created by shivam on 10/27/15.
 */
public class BankTransactionStatusFragment extends Fragment {


    public static final String PENDING = "Pending";
    public static final String EXTRA_INSTRUCTION_URL = "url";
    private static final String DATA = "data";
    private static final String PAYMENT_TYPE = "payment_type";
    private TransactionResponse mTransactionResponse = null;
    // Views
    private TextView mTextViewAmount = null;
    private TextView mTextViewOrderId = null;
    private TextView mTextViewTransactionTime = null;
    private TextView mTextViewBankName = null;
    private TextView mTextViewTransactionStatus = null;
    private TextView mTextViewPaymentErrorMessage = null;
    private ImageView mImageViewTransactionStatus = null;
    private Button mSeeInstructions = null;
    private int mPaymentType = -1;

    /**
     * It creates new BankTransactionStatusFragment object and set Transaction object to it, so
     * later it can be accessible using fragments getArgument().
     *
     * @param transactionResponse response of the transaction call.
     * @return instance of BankTransactionStatusFragment.
     */
    public static BankTransactionStatusFragment newInstance(TransactionResponse transactionResponse,
                                                            int paymentType) {
        BankTransactionStatusFragment fragment = new BankTransactionStatusFragment();
        Bundle data = new Bundle();
        data.putSerializable(DATA, transactionResponse);
        data.putInt(PAYMENT_TYPE, paymentType);
        fragment.setArguments(data);
        return fragment;
    }

    public static BankTransactionStatusFragment newInstance(TransactionResponse transactionResponse,
                                                            int paymentType,
                                                            String url) {
        BankTransactionStatusFragment fragment = new BankTransactionStatusFragment();
        Bundle data = new Bundle();
        data.putSerializable(DATA, transactionResponse);
        data.putString(EXTRA_INSTRUCTION_URL, url);
        data.putInt(PAYMENT_TYPE, paymentType);
        fragment.setArguments(data);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bank_transaction_status, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initializeViews(view);

        //retrieve data from bundle.
        Bundle data = getArguments();
        mTransactionResponse = (TransactionResponse) data.getSerializable(DATA);
        mPaymentType = data.getInt(PAYMENT_TYPE);

        initializeDataToView();
    }

    /**
     * initializes view and adds click listener for it.
     *
     * @param view view that needed to be initialized
     */
    private void initializeViews(View view) {

        mTextViewAmount = (TextView) view.findViewById(R.id.text_amount);
        mTextViewOrderId = (TextView) view.findViewById(R.id.text_order_id);
        mTextViewBankName = (TextView) view.findViewById(R.id.text_payment_type);
        mTextViewTransactionTime = (TextView) view.findViewById(R.id.text_transaction_time);
        mSeeInstructions = (Button) view.findViewById(R.id.btn_see_instruction);

        mImageViewTransactionStatus = (ImageView) view.findViewById(R.id.img_transaction_status);
        mTextViewTransactionStatus = (TextView) view.findViewById(R.id
                .text_transaction_status);
        mTextViewPaymentErrorMessage = (TextView) view.findViewById(R.id
                .text_payment_error_message);

    }


    /**
     * apply data to view
     */
    private void initializeDataToView() {

        if (mTransactionResponse != null) {

            if (getActivity() != null) {

                if (mPaymentType == Constants.BANK_TRANSFER_PERMATA
                        || mPaymentType == Constants.BANK_TRANSFER_BCA
                        || mPaymentType == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT
                        || mPaymentType == Constants.PAYMENT_METHOD_BANK_TRANSFER_ALL_BANK) {
                    mTextViewBankName.setText(getString(R.string.payment_method_bank_transfer));
                } else if (mPaymentType == Constants.PAYMENT_METHOD_INDOSAT_DOMPETKU) {
                    mTextViewBankName.setText(getActivity().getResources().getString(R.string
                            .indosat_dompetku));
                } else if (mPaymentType == Constants.PAYMENT_METHOD_TELKOMSEL_CASH) {
                    mTextViewBankName.setText(getString(R.string.payment_method_telkomsel_cash));
                } else if (mPaymentType == Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY) {
                    mTextViewBankName.setText(getActivity().getResources().getString(R.string
                            .mandiri_click_pay));
                } else if (mPaymentType == Constants.PAYMENT_METHOD_KLIKBCA) {
                    mTextViewBankName.setText(getString(R.string.payment_method_klik_bca));
                }
            }

            mTextViewTransactionTime.setText(mTransactionResponse.getTransactionTime());
            mTextViewOrderId.setText(mTransactionResponse.getOrderId());
            String amount = mTransactionResponse.getGrossAmount();
            String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
            mTextViewAmount.setText(formattedAmount);

            //noinspection StatementWithEmptyBody
            if (mTransactionResponse.getTransactionStatus().contains(PENDING)
                    || mTransactionResponse.getTransactionStatus().contains("pending")) {
                mSeeInstructions.setVisibility(View.VISIBLE);
                // Show instruction
                if (!TextUtils.isEmpty(getArguments().getString(BankTransferInstructionActivity.DOWNLOAD_URL))) {
                    mSeeInstructions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showInstruction(getArguments().getString(BankTransferInstructionActivity.DOWNLOAD_URL));
                        }
                    });
                } else {
                    mSeeInstructions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showInstruction();
                        }
                    });
                }
            } else if (mTransactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200))
                    || mTransactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_201))) {
                mSeeInstructions.setVisibility(View.GONE);
                setUiForSuccess();
            } else {
                mSeeInstructions.setVisibility(View.GONE);
                setUiForFailure();

                if (getActivity() != null) {

                    // change name of button to 'RETRY'
                    if (mPaymentType == Constants.PAYMENT_METHOD_INDOSAT_DOMPETKU) {
                        ((IndosatDompetkuActivity) getActivity()).activateRetry();
                    } else if (mPaymentType == Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY) {
                        ((MandiriClickPayActivity) getActivity()).activateRetry();

                        if (mTransactionResponse != null &&
                                mTransactionResponse.getTransactionStatus().equalsIgnoreCase("deny")) {
                            mTextViewTransactionStatus.setText("Payment Denied.");
                        }
                    } else {
                        ((BankTransferActivity) getActivity()).activateRetry();
                    }
                }
            }
        }
    }


    /**
     * enables ui related to failure of payment transaction.
     */
    private void setUiForFailure() {
        mImageViewTransactionStatus.setImageResource(R.drawable.ic_failure);
        mTextViewTransactionStatus.setText(getString(R.string.payment_unsuccessful));
        mTextViewPaymentErrorMessage.setVisibility(View.VISIBLE);
    }


    /**
     * enables ui related to success of payment transaction.
     */
    private void setUiForSuccess() {
        mImageViewTransactionStatus.setImageResource(R.drawable.ic_successful);
        mTextViewTransactionStatus.setText(getString(R.string.payment_successful));
        mTextViewPaymentErrorMessage.setVisibility(View.GONE);
    }

    private void showInstruction() {
        Intent intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
        switch (mPaymentType) {
            case Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT:
                intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_MANDIRI_BILL);
                break;
            case Constants.BANK_TRANSFER_PERMATA:
                intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_PERMATA);
                break;
            case Constants.BANK_TRANSFER_BCA:
                intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_BCA);
                break;
            case Constants.PAYMENT_METHOD_BANK_TRANSFER_ALL_BANK:
                intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_ALL_BANK);
                break;
            case Constants.PAYMENT_METHOD_KLIKBCA:
                intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_BCA);
                intent.putExtra(BankTransferInstructionActivity.PAGE, BankTransferInstructionActivity.KLIKBCA_PAGE);
                break;
        }
        getActivity().startActivity(intent);
    }

    private void showInstruction(String downloadUrl) {
        Intent intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
        switch (mPaymentType) {
            case Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT:
                intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_MANDIRI_BILL);
                break;
            case Constants.PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER:
                intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_PERMATA);
                break;
            case Constants.BANK_TRANSFER_BCA:
                intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_BCA);
                break;
            case Constants.PAYMENT_METHOD_BANK_TRANSFER_ALL_BANK:
                intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_ALL_BANK);
                break;
        }
        intent.putExtra(BankTransferInstructionActivity.DOWNLOAD_URL, downloadUrl);
        getActivity().startActivity(intent);
    }
}