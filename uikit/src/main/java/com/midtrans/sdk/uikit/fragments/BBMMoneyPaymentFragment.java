package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;

/**
 * Created by Ankit on 12/03/15.
 */
public class BBMMoneyPaymentFragment extends Fragment {

    public static final String KEY_ARG = "arg";
    public static final String VALID_UNTILL = "Complete payment via BBM Money App before: ";
    public String PERMATA_VA = null;
    private TransactionResponse transactionResponse;

    //views
    private TextView mTextViewValidity = null;
    private TextView mTextViewPaymentCode = null;

    public static BBMMoneyPaymentFragment newInstance(TransactionResponse mTransactionResponse) {
        BBMMoneyPaymentFragment fragment = new BBMMoneyPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ARG, mTransactionResponse);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbm_money_transfer_payment, container, false);
        return view;
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
        mTextViewValidity = (TextView) view.findViewById(R.id.text_validaty);
        mTextViewPaymentCode = (TextView) view.findViewById(R.id.text_payment_code);

        if (transactionResponse != null) {
            if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_200))
                    || transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_201))) {
                mTextViewValidity.setText(VALID_UNTILL + "\n" + Utils.getValidityTime(transactionResponse.getTransactionTime()));
            }

            if (transactionResponse.getPermataVANumber() != null) {
                mTextViewPaymentCode.setText(transactionResponse.getPermataVANumber());
                PERMATA_VA = transactionResponse.getPermataVANumber();
            }

        }
    }
}