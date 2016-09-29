package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BBMMoneyActivity;

import java.util.regex.Pattern;

/**
 * Created by shivam on 10/27/15.
 */
public class BBMMoneyPaymentStatusFragment extends Fragment {

    private static final String BBM_MONEY = "BBM Money";
    private static final String DATA = "data";
    private static final String IS_FROM_BBM = "bbm_money";
    private TransactionResponse transactionResponse = null;
    // Views
    private TextView mTextViewAmount = null;
    private TextView mTextViewOrderId = null;
    private TextView mTextViewTransactionTime = null;
    private TextView mTextViewBankName = null;
    private TextView mTextViewTransactionStatus = null;
    private TextView mTextViewPaymentErrorMessage = null;
    private ImageView mImageViewTransactionStatus = null;
    private boolean isFromBBM = false;

    public static BBMMoneyPaymentStatusFragment newInstance(TransactionResponse
                                                                    transactionResponse, boolean
                                                                    isFromBBM) {
        BBMMoneyPaymentStatusFragment fragment = new BBMMoneyPaymentStatusFragment();
        Bundle data = new Bundle();
        data.putSerializable(DATA, transactionResponse);
        data.putBoolean(IS_FROM_BBM, isFromBBM);
        fragment.setArguments(data);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bbm_money_payment_status, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initializeViews(view);

        //retrieve data from bundle.
        Bundle data = getArguments();
        transactionResponse = (TransactionResponse) data.getSerializable(DATA);
        isFromBBM = data.getBoolean(IS_FROM_BBM);
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

        if (transactionResponse != null) {

            if (getActivity() != null) {

                if (!isFromBBM) {
                    if (((BBMMoneyActivity) getActivity()).getPosition()
                            == Constants.PAYMENT_METHOD_BBM_MONEY) {
                        mTextViewBankName.setText(BBM_MONEY);
                    }
                } else {
                    mTextViewBankName.setText(getActivity().getResources().getString(R.string.bbm_money));
                }
            }

            mTextViewTransactionTime.setText(transactionResponse.getTransactionTime());
            mTextViewOrderId.setText(transactionResponse.getOrderId());
            String amount = transactionResponse.getGrossAmount();
            String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
            mTextViewAmount.setText(formattedAmount);

            //noinspection StatementWithEmptyBody
            if (transactionResponse.getTransactionStatus().contains("Pending") ||
                    transactionResponse.getTransactionStatus().contains("pending")) {

            } else if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200))
                    || transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_201))) {
                setUiForSuccess();
            } else {

                setUiForFailure();

                if (getActivity() != null) {

                    if (isFromBBM) {
                        // change name of button to 'RETRY'
                        ((BBMMoneyActivity) getActivity()).activateRetry();
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
}