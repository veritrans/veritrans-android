package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.CreditDebitCardFlowActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by shivam on 10/27/15.
 */
public class PaymentTransactionStatusFragment extends Fragment {

    private static final String TRANSACTION_RESPONSE_PARAM = "transaction_response_param";
    private VeritransSDK veritrans;
    private TransactionResponse transactionResponse;
    private boolean isSuccessful;

    // views
    private Button actionBt = null;
    private ImageView paymentIv = null;
    private TextViewFont paymentStatusTv = null;
    private TextViewFont paymentMessageTv = null;
    private TextViewFont amountTextViewFont = null;
    private TextViewFont orderIdTextViewFont = null;
    private TextViewFont transactionTimeTextViewFont = null;
    private TextViewFont paymentTypeTextViewFont = null;
    private int count = 1;

    public PaymentTransactionStatusFragment() {
        // Required empty public constructor
    }

    public static PaymentTransactionStatusFragment newInstance(TransactionResponse
                                                                       transactionResponse) {
        Logger.i("payment status get instance called");
        PaymentTransactionStatusFragment fragment = new PaymentTransactionStatusFragment();
        Bundle args = new Bundle();
        args.putSerializable(TRANSACTION_RESPONSE_PARAM, transactionResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transactionResponse = (TransactionResponse) getArguments().getSerializable
                    (TRANSACTION_RESPONSE_PARAM);
        }
        veritrans = VeritransSDK.getVeritransSDK();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_transaction_status, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeViews(view);
        bindDataToView();
    }

    private void initializeViews(View view) {
        amountTextViewFont = (TextViewFont) view.findViewById(R.id.text_amount);
        orderIdTextViewFont = (TextViewFont) view.findViewById(R.id.text_order_id);
        transactionTimeTextViewFont = (TextViewFont) view.findViewById(R.id.text_transaction_time);
        paymentTypeTextViewFont = (TextViewFont) view.findViewById(R.id.text_payment_type);
        actionBt = (Button) view.findViewById(R.id.btn_action);
        paymentIv = (ImageView) view.findViewById(R.id.image_payment);
        paymentStatusTv = (TextViewFont) view.findViewById(R.id.text_payment_status);
        paymentMessageTv = (TextViewFont) view.findViewById(R.id.text_payment_message);
    }

    private void bindDataToView() {

        if (transactionResponse != null) {

            try {
                Logger.i("transactionstatus:" + transactionResponse.getString());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (transactionResponse != null) {
                if (transactionResponse.getStatusCode().equalsIgnoreCase(Constants
                        .SUCCESS_CODE_200) ||

                        transactionResponse.getStatusCode().
                                equalsIgnoreCase(Constants.SUCCESS_CODE_201)) {

                    setUiForSuccess();

                } else {
                    setUiForFailure();
                }

                transactionTimeTextViewFont.setText(transactionResponse.getTransactionTime());
                setPaymentType();

            } else {
                setUiForFailure();
            }

            amountTextViewFont.setText("" + veritrans.getTransactionRequest().getAmount());
            orderIdTextViewFont.setText("" + veritrans.getTransactionRequest().getOrderId());
            actionBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((CreditDebitCardFlowActivity) getActivity())
                            .setResultCode(getActivity().RESULT_OK);
                    ((CreditDebitCardFlowActivity) getActivity()).setResultAndFinish();
                }
            });
        }
    }

    private void setUiForFailure() {
        isSuccessful = false;
        actionBt.setText(getString(R.string.retry));
        paymentIv.setImageResource(R.drawable.ic_failure);
        paymentStatusTv.setText(getString(R.string.payment_unsuccessful));
        paymentMessageTv.setVisibility(View.VISIBLE);
        paymentMessageTv.setText(transactionResponse.getStatusMessage());
    }

    private void setUiForSuccess() {
        isSuccessful = true;
        actionBt.setText(getString(R.string.done));
        paymentIv.setImageResource(R.drawable.ic_successful);
        paymentStatusTv.setText(getString(R.string.payment_successful));
        paymentMessageTv.setVisibility(View.GONE);
    }

    private void setPaymentType() {

        if (veritrans != null) {

            switch (veritrans.getTransactionRequest().getPaymentMethod()) {

                case Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT:
                    paymentTypeTextViewFont.setText(getString(R.string.credit_card));
                    break;

                case Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT:
                    paymentTypeTextViewFont.setText(getString(R.string.mandiri_bill_payment));
                    break;

                case Constants.PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER:
                    paymentTypeTextViewFont.setText(getString(R.string.virtual_account));
                    break;

                case Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY:
                    paymentTypeTextViewFont.setText(getString(R.string.mandiri_click_pay));
                    break;

                //todo add payment type as per requirement.
            }

        }

    }
}