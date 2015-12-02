package id.co.veritrans.sdk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.BankTransferInstructionActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.utilities.Utils;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Displays status information about bank transfer's api call .
 *
 * Created by shivam on 10/27/15.
 */
public class IndomaretPaymentFragment extends Fragment {


    public static final String VALID_UNTILL = "Valid Untill : ";
    private static TransactionResponse transactionResponse = null;

    //views

    private TextViewFont mTextViewValidity = null;
    private TextViewFont mTextViewPaymentCode = null;



    public static IndomaretPaymentFragment newInstance(TransactionResponse
                                                                  mTransactionResponse) {
        transactionResponse = mTransactionResponse;
        IndomaretPaymentFragment fragment = new IndomaretPaymentFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_indomaret_transfer_payment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeViews(view);
    }


    /**
     * initializes view and adds click listener for it.
     *
     * @param view
     */
    private void initializeViews(View view) {
        mTextViewValidity = (TextViewFont) view.findViewById(R.id.text_validaty);
        mTextViewPaymentCode = (TextViewFont) view.findViewById(R.id.text_payment_code);

        if (transactionResponse != null) {
            if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(Constants
                    .SUCCESS_CODE_200) || transactionResponse.getStatusCode().trim().equalsIgnoreCase(Constants.SUCCESS_CODE_201))
            mTextViewValidity.setText(VALID_UNTILL + Utils.getValidityTime
                    (transactionResponse.getTransactionTime()));

            if (transactionResponse.getPaymentCodeIndomaret() != null)
                mTextViewPaymentCode.setText(transactionResponse.getPaymentCodeIndomaret());

        } else {
            //TODO..what to do here...
        }
    }
}