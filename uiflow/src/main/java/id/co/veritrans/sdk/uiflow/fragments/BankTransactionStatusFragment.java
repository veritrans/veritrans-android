package id.co.veritrans.sdk.uiflow.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Pattern;

import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.activities.BankTransferActivity;
import id.co.veritrans.sdk.uiflow.activities.IndosatDompetkuActivity;
import id.co.veritrans.sdk.uiflow.activities.MandiriClickPayActivity;
import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;

/**
 * Created by shivam on 10/27/15.
 */
public class BankTransactionStatusFragment extends Fragment {


    public static final String PENDING = "Pending";
    private static final String VIRTUAL_ACCOUNT = "Virtual Account";
    private static final String MANDIRI_BILL = "Mandiri Bill Payment";
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
    private int mPaymentType = -1;

    /**
     * It creates new BankTransactionStatusFragment object and set TransactionResponse object to it,
     * so later it can be accessible using fragments getArgument().
     *
     * @param transactionResponse   response of the transaction call.
     * @return instance of BankTransactionStatusFragment.
     */
    public static BankTransactionStatusFragment newInstance(TransactionResponse
                                                                    transactionResponse,
                                                            int paymentType) {
        BankTransactionStatusFragment fragment = new BankTransactionStatusFragment();
        Bundle data = new Bundle();
        data.putSerializable(DATA, transactionResponse);
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
     * @param view  view that needed to be initialized
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

        if (mTransactionResponse != null) {

            if (getActivity() != null) {

                if (mPaymentType == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT) {
                    mTextViewBankName.setText(MANDIRI_BILL);

                } else if (mPaymentType == Constants.BANK_TRANSFER_PERMATA
                        || mPaymentType == Constants.BANK_TRANSFER_BCA) {
                    mTextViewBankName.setText(getString(R.string.payment_method_bank_transfer));
                } else if (mPaymentType == Constants.PAYMENT_METHOD_INDOSAT_DOMPETKU) {
                    mTextViewBankName.setText(getActivity().getResources().getString(R.string
                            .indosat_dompetku));
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
            if (mTransactionResponse.getTransactionStatus().contains(PENDING) ||
                    mTransactionResponse.getTransactionStatus().contains("pending")) {

            } else if (mTransactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200))
                    || mTransactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_201))) {

                setUiForSuccess();
            } else {

                setUiForFailure();

                if (getActivity() != null) {

                    // change name of button to 'RETRY'
                    if (mPaymentType == Constants.PAYMENT_METHOD_INDOSAT_DOMPETKU) {
                        ((IndosatDompetkuActivity) getActivity()).activateRetry();
                    } else if (mPaymentType == Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY){
                        ((MandiriClickPayActivity) getActivity()).activateRetry();

                        if ( mTransactionResponse != null &&
                                mTransactionResponse.getTransactionStatus().equalsIgnoreCase("deny")){
                            mTextViewTransactionStatus.setText("Payment Denied.");
                        }
                    }
                    else {
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
}