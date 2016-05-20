package id.co.veritrans.sdk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.BankTransferInstructionActivity;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.utilities.Utils;

/**
 * Displays status information about bank transfer's api call .
 *
 * Created by shivam on 10/27/15.
 */
public class BankTransferPaymentFragment extends Fragment {


    public static final String VALID_UNTILL = "Valid Untill : ";
    private static TransactionResponse transactionResponse = null;

    //views
    private TextView mTextViewVirtualAccountNumber = null;
    private TextView mTextViewSeeInstruction = null;
    private TextView mTextViewValidity = null;


    /**
     *
     * it creates new BankTransferPaymentFragment object and set TransactionResponse object to it,
     * so later it can be accessible using fragments getArgument().
     *
     *  @param permataBankTransferResponse  response of transaction call
     * @return instance of BankTransferPaymentFragment
     */
    public static BankTransferPaymentFragment newInstance(TransactionResponse
                                                                  permataBankTransferResponse) {
        transactionResponse = permataBankTransferResponse;
        BankTransferPaymentFragment fragment = new BankTransferPaymentFragment();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bank_transfer_payment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeViews(view);
    }


    /**
     * initializes view and adds click listener for it.
     *
     * @param view  view that needed to be initialized
     */
    private void initializeViews(View view) {

        mTextViewVirtualAccountNumber = (TextView)
                view.findViewById(R.id.text_virtual_account_number);

        mTextViewSeeInstruction = (TextView) view.findViewById(R.id.text_see_instruction);
        mTextViewValidity = (TextView) view.findViewById(R.id.text_validaty);

        if (transactionResponse != null) {
            if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_200))
                    || transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_201))) {
                if (transactionResponse.getAccountNumbers() != null && transactionResponse.getAccountNumbers().size() > 0) {
                    mTextViewVirtualAccountNumber.setText(transactionResponse.getAccountNumbers().get(0).getAccountNumber());
                } else {
                    mTextViewVirtualAccountNumber.setText(transactionResponse.getPermataVANumber());
                }
            } else {
                mTextViewVirtualAccountNumber.setText(transactionResponse.getStatusMessage());
            }

            mTextViewValidity.setText(VALID_UNTILL + Utils.getValidityTime
                    (transactionResponse.getTransactionTime()));
        }
        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null) {
            mTextViewSeeInstruction.setTextColor(veritransSDK.getThemeColor());
        }
        mTextViewSeeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInstruction();
            }
        });
    }

    /**
     * starts {@link BankTransferInstructionActivity} to show payment instruction.
     */
    private void showInstruction() {
        Intent intent = new Intent(getActivity(),
                BankTransferInstructionActivity.class);
        intent.putExtra(BankTransferInstructionActivity.BANK, getArguments().getString(BankTransferInstructionActivity.BANK));
        getActivity().startActivity(intent);
    }

}