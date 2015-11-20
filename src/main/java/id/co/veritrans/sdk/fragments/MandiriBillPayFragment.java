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
 * Created by shivam on 10/28/15.
 */
public class MandiriBillPayFragment extends Fragment {

    public static final String VALID_UNTILL = "Valid Untill : ";
    private static final String DATA = "data";
    private TransactionResponse mTransactionResponse = null;
    private TextViewFont mTextViewCompanyCode = null;
    private TextViewFont mTextViewBillpayCode = null;
    private TextViewFont mTextViewSeeInstruction = null;
    private TextViewFont mTextViewValidity = null;


    public static MandiriBillPayFragment newInstance(TransactionResponse
                                                             permataBankTransferResponse) {

        MandiriBillPayFragment fragment = new MandiriBillPayFragment();
        Bundle data = new Bundle();
        data.putSerializable(DATA, permataBankTransferResponse);
        fragment.setArguments(data);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mandiri_bill_pay, container, false);

        if (getArguments() != null) {
            mTransactionResponse = (TransactionResponse) getArguments().getSerializable(DATA);
            initializeViews(view);
        }
        return view;
    }

    private void initializeViews(View view) {

        mTextViewCompanyCode = (TextViewFont) view.findViewById(R.id.text_company_code);
        mTextViewBillpayCode = (TextViewFont) view.findViewById(R.id.text_bill_pay_code);

        mTextViewSeeInstruction = (TextViewFont) view.findViewById(R.id.text_see_instruction);
        mTextViewValidity = (TextViewFont) view.findViewById(R.id.text_validaty);


        if (mTransactionResponse != null) {
            if (mTransactionResponse.getStatusCode().trim().equalsIgnoreCase(Constants
                    .SUCCESS_CODE_200) ||
                    mTransactionResponse.getStatusCode().trim().equalsIgnoreCase(Constants
                            .SUCCESS_CODE_201)
                    )
                mTextViewCompanyCode.setText(mTransactionResponse
                        .getCompanyCode());

            mTextViewBillpayCode.setText(mTransactionResponse
                    .getPaymentCode());

            mTextViewValidity.setText(VALID_UNTILL + Utils.getValidityTime
                    (mTransactionResponse.getTransactionTime()));

        } else {
            mTextViewCompanyCode.setText(mTransactionResponse.getCompanyCode());
        }

        mTextViewSeeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstruction();
            }
        });
    }

    private void showInstruction() {
        Intent intent = new Intent(getActivity(),
                BankTransferInstructionActivity.class);
        intent.putExtra(Constants.POSITION, 0);
        getActivity().startActivity(intent);
    }

}