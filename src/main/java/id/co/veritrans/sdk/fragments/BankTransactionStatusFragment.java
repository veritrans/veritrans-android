package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.BankTransferActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by shivam on 10/27/15.
 */
public class BankTransactionStatusFragment extends Fragment {


    private static final String VIRTUAL_ACCOUNT = "Virtual Account";
    private static final String MANDIRI_BILL = "Mandiri Bill Payment";
    private static final String DATA = "data";

    private TransactionResponse mPermataBankTransferResponse = null;

    private TextViewFont mTextViewAmount = null;
    private TextViewFont mTextViewOrderId = null;
    private TextViewFont mTextViewTransactionTime = null;
    private TextViewFont mTextViewBankName = null;

    private ImageView mImageViewTransactionStatus = null;
    private TextViewFont mTextViewFontTransactionStatus = null;
    private TextViewFont mTextViewFontPaymentErrorMessage = null;

    public static BankTransactionStatusFragment newInstance(TransactionResponse
                                                                    transactionResponse) {
        BankTransactionStatusFragment fragment = new BankTransactionStatusFragment();
        Bundle data = new Bundle();
        data.putSerializable(DATA, transactionResponse);
        fragment.setArguments(data);
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

        Bundle data = getArguments();
        mPermataBankTransferResponse = (TransactionResponse) data.getSerializable(DATA);
        initializeDataToView();

    }

    private void initializeViews(View view) {

        mTextViewAmount = (TextViewFont) view.findViewById(R.id.text_amount);
        mTextViewOrderId = (TextViewFont) view.findViewById(R.id.text_order_id);
        mTextViewBankName = (TextViewFont) view.findViewById(R.id.text_payment_type);
        mTextViewTransactionTime = (TextViewFont) view.findViewById(R.id.text_transaction_time);

        mImageViewTransactionStatus = (ImageView) view.findViewById(R.id.img_transaction_status);
        mTextViewFontTransactionStatus = (TextViewFont) view.findViewById(R.id
                .text_transaction_status);
        mTextViewFontPaymentErrorMessage = (TextViewFont) view.findViewById(R.id
                .text_payment_error_message);

    }


    private void initializeDataToView() {

        if (mPermataBankTransferResponse != null) {

            if (getActivity() != null) {

                if (((BankTransferActivity) getActivity()).getPosition()
                        == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT) {
                    mTextViewBankName.setText(MANDIRI_BILL);
                } else {
                    mTextViewBankName.setText(VIRTUAL_ACCOUNT);
                }

            }

            mTextViewTransactionTime.setText(mPermataBankTransferResponse.getTransactionTime());
            mTextViewOrderId.setText(mPermataBankTransferResponse.getOrderId());
            mTextViewAmount.setText(mPermataBankTransferResponse.getGrossAmount());

            if (mPermataBankTransferResponse.getTransactionStatus().contains("Pending") ||
                    mPermataBankTransferResponse.getTransactionStatus().contains("pending")) {

            } else if (mPermataBankTransferResponse.getStatusCode().equalsIgnoreCase(Constants
                    .SUCCESS_CODE_200) || mPermataBankTransferResponse.getStatusCode().
                    equalsIgnoreCase(Constants.SUCCESS_CODE_201)) {

                setUiForSuccess();
            } else {

                setUiForFailure();
                if (getActivity() != null) {
                    ((BankTransferActivity) getActivity()).activateRetry();
                }
            }
        }
    }


    private void setUiForFailure() {
        mImageViewTransactionStatus.setImageResource(R.drawable.ic_failure);
        mTextViewFontTransactionStatus.setText(getString(R.string.payment_unsuccessful));
        mTextViewFontPaymentErrorMessage.setVisibility(View.VISIBLE);
    }

    private void setUiForSuccess() {

        mImageViewTransactionStatus.setImageResource(R.drawable.ic_successful);
        mTextViewFontTransactionStatus.setText(getString(R.string.payment_successful));
        mTextViewFontPaymentErrorMessage.setVisibility(View.GONE);
    }

}