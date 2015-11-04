package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.BankTransferActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by shivam on 10/27/15.
 */
public class BankTransactionStatusFragment extends Fragment {


    public static final String VIRTUAL_ACCOUNT = "Virtual Account";
    public static final String MANDIRI_BILL = "Mandiri Bill Payment";

    private static TransactionResponse sPermataBankTransferResponse = null;

    private TextViewFont mTextViewAmount = null;
    private TextViewFont mTextViewOrderId = null;
    private TextViewFont mTextViewTransactionTime = null;
    private TextViewFont mTextViewBankName = null;


    public static BankTransactionStatusFragment newInstance(TransactionResponse
                                                                    permataBankTransferResponse) {
        sPermataBankTransferResponse = permataBankTransferResponse;
        BankTransactionStatusFragment fragment = new BankTransactionStatusFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_transaction_status, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeViews(view);
        initializeDataToView();
    }

    private void initializeViews(View view) {
        mTextViewAmount = (TextViewFont) view.findViewById(R.id.text_amount);
        mTextViewOrderId = (TextViewFont) view.findViewById(R.id.text_order_id);
        mTextViewBankName = (TextViewFont) view.findViewById(R.id.text_payment_type);
        mTextViewTransactionTime = (TextViewFont) view.findViewById(R.id.text_transaction_time);
    }


    private void initializeDataToView() {


        if (sPermataBankTransferResponse != null) {

            if (getActivity() != null) {

                if (((BankTransferActivity) getActivity()).getPosition()
                        == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT) {
                    mTextViewBankName.setText(MANDIRI_BILL);
                } else {
                    mTextViewBankName.setText(VIRTUAL_ACCOUNT);
                }

            }


            mTextViewTransactionTime.setText(sPermataBankTransferResponse.getTransactionTime());
            mTextViewOrderId.setText(sPermataBankTransferResponse.getOrderId());
            mTextViewAmount.setText(sPermataBankTransferResponse.getGrossAmount());

        }
    }

}