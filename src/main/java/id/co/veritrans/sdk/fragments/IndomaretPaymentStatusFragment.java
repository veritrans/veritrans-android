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
import id.co.veritrans.sdk.activities.IndomaretActivity;
import id.co.veritrans.sdk.activities.IndosatDompetkuActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by shivam on 10/27/15.
 */
public class IndomaretPaymentStatusFragment extends Fragment {

    private static final String INDOMARET = "Indomaret";
    private static final String DATA = "data";

    private TransactionResponse transactionResponse = null;

    // Views
    private TextViewFont mTextViewAmount = null;
    private TextViewFont mTextViewOrderId = null;
    private TextViewFont mTextViewTransactionTime = null;
    private TextViewFont mTextViewBankName = null;

    private TextViewFont mTextViewFontTransactionStatus = null;
    private TextViewFont mTextViewFontPaymentErrorMessage = null;
    private ImageView mImageViewTransactionStatus = null;
    private static  final  String IS_FROM_INDOMARET = "indomaret";
    private boolean isFromIndomaret = false;

    public static IndomaretPaymentStatusFragment newInstance(TransactionResponse
                                                                    transactionResponse, boolean
            isFromIndomaret) {
        IndomaretPaymentStatusFragment fragment = new IndomaretPaymentStatusFragment();
        Bundle data = new Bundle();
        data.putSerializable(DATA, transactionResponse);
        data.putBoolean(IS_FROM_INDOMARET, isFromIndomaret);
        fragment.setArguments(data);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_indomaret_payment_status, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initializeViews(view);

        //retrieve data from bundle.
        Bundle data = getArguments();
        transactionResponse = (TransactionResponse) data.getSerializable(DATA);
        isFromIndomaret = data.getBoolean(IS_FROM_INDOMARET);
        initializeDataToView();
    }

    /**
     * initializes view and adds click listener for it.
     *
     * @param view
     */
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


    /**
     * apply data to view
     */
    private void initializeDataToView() {

        if (transactionResponse != null) {

            if ( getActivity() != null ) {

                if( !isFromIndomaret ) {
                    if (((IndomaretActivity) getActivity()).getPosition()
                            == Constants.PAYMENT_METHOD_INDOMARET) {
                        mTextViewBankName.setText(INDOMARET);
                    }
                }else {
                    mTextViewBankName.setText(getActivity().getResources().getString(R.string.indomaret));
                }
            }

            mTextViewTransactionTime.setText(transactionResponse.getTransactionTime());
            mTextViewOrderId.setText(transactionResponse.getOrderId());
            mTextViewAmount.setText(transactionResponse.getGrossAmount());

            if (transactionResponse.getTransactionStatus().contains("Pending") ||
                    transactionResponse.getTransactionStatus().contains("pending")) {

            } else if (transactionResponse.getStatusCode().equalsIgnoreCase(Constants
                    .SUCCESS_CODE_200) || transactionResponse.getStatusCode().
                    equalsIgnoreCase(Constants.SUCCESS_CODE_201)) {

                setUiForSuccess();
            } else {

                setUiForFailure();

                if (getActivity() != null ) {

                    if (isFromIndomaret) {
                        // change name of button to 'RETRY'
                        ((IndomaretActivity) getActivity()).activateRetry();
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
        mTextViewFontTransactionStatus.setText(getString(R.string.payment_unsuccessful));
        mTextViewFontPaymentErrorMessage.setVisibility(View.VISIBLE);
    }


    /**
     * enables ui related to success of payment transaction.
     */
    private void setUiForSuccess() {
        mImageViewTransactionStatus.setImageResource(R.drawable.ic_successful);
        mTextViewFontTransactionStatus.setText(getString(R.string.payment_successful));
        mTextViewFontPaymentErrorMessage.setVisibility(View.GONE);
    }
}