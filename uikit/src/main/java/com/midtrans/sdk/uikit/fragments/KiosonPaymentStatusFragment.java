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
import com.midtrans.sdk.uikit.activities.KiosonActivity;

import java.util.regex.Pattern;

/**
 * Created by ziahaqi on 8/26/16.
 */
public class KiosonPaymentStatusFragment extends Fragment {
    private static final String KIOSON = "Kioson";
    private static final String DATA = "data";
    private static final String IS_FROM_KIOSON = "kioson";
    private TransactionResponse transactionResponse = null;
    // Views
    private TextView mTextViewAmount = null;
    private TextView mTextViewOrderId = null;
    private TextView mTextViewTransactionTime = null;
    private TextView mTextViewBankName = null;
    private TextView mTextViewTransactionStatus = null;
    private TextView mTextViewPaymentErrorMessage = null;
    private ImageView mImageViewTransactionStatus = null;
    private boolean isFromKioson = false;

    public static KiosonPaymentStatusFragment newInstance(TransactionResponse transactionResponse, boolean isFromKioson) {
        KiosonPaymentStatusFragment fragment = new KiosonPaymentStatusFragment();
        Bundle data = new Bundle();
        data.putSerializable(DATA, transactionResponse);
        data.putBoolean(IS_FROM_KIOSON, isFromKioson);
        fragment.setArguments(data);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kioson_payment_status, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initializeViews(view);

        //retrieve data from bundle.
        Bundle data = getArguments();
        transactionResponse = (TransactionResponse) data.getSerializable(DATA);
        isFromKioson = data.getBoolean(IS_FROM_KIOSON);
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

                if (!isFromKioson) {
                    if (((KiosonActivity) getActivity()).getPosition()
                            == Constants.PAYMENT_METHOD_KIOSON) {
                        mTextViewBankName.setText(KIOSON);
                    }
                } else {
                    mTextViewBankName.setText(getActivity().getResources().getString(R.string.kioson));
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

                    if (isFromKioson) {
                        // change name of button to 'RETRY'
                        ((KiosonActivity) getActivity()).activateRetry();
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
