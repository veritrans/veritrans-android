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
    private Button actionBt;
    private ImageView paymentIv;
    private TextViewFont paymentStatusTv;
    private TextViewFont paymentMessageTv;
    private TextViewFont amountTextViewFont;
    private TextViewFont orderIdTextViewFont;
    private TextViewFont transactionTimeTextViewFont;
    private TextViewFont paymentTypeTextViewFont;
    private VeritransSDK veritrans;
    private TransactionResponse transactionResponse;
    private boolean isSuccessful;

    public static PaymentTransactionStatusFragment newInstance(TransactionResponse transactionResponse) {
        Logger.i("payment status get instance called");
        PaymentTransactionStatusFragment fragment = new PaymentTransactionStatusFragment();
        Bundle args = new Bundle();
        args.putSerializable(TRANSACTION_RESPONSE_PARAM,transactionResponse);
        fragment.setArguments(args);
        return fragment;
    }

    public PaymentTransactionStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transactionResponse = (TransactionResponse) getArguments().getSerializable(TRANSACTION_RESPONSE_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_transaction_status, container, false);
        veritrans = VeritransSDK.getVeritransSDK();
        amountTextViewFont = (TextViewFont) view.findViewById(R.id.text_amount);
        orderIdTextViewFont = (TextViewFont) view.findViewById(R.id.text_order_id);
        transactionTimeTextViewFont = (TextViewFont) view.findViewById(R.id.text_transaction_time);
        paymentTypeTextViewFont = (TextViewFont) view.findViewById(R.id.text_payment_type);
        actionBt = (Button) view.findViewById(R.id.btn_action);
        paymentIv = (ImageView) view.findViewById(R.id.image_payment);
        paymentStatusTv = (TextViewFont) view.findViewById(R.id.text_payment_status);
        paymentMessageTv = (TextViewFont) view.findViewById(R.id.text_payment_message);
        if(transactionResponse !=null) {
            if (transactionResponse.getStatusCode().equalsIgnoreCase(Constants.SUCCESS_CODE_200) ||
                    transactionResponse.getStatusCode().equalsIgnoreCase(Constants.SUCCESS_CODE_201)) {
                isSuccessful = true;
                actionBt.setText(getString(R.string.done));
                paymentIv.setImageResource(R.drawable.ic_successful);
                paymentStatusTv.setText(getString(R.string.payment_successful));
                paymentMessageTv.setVisibility(View.GONE);
            } else {
                isSuccessful = false;
                actionBt.setText(getString(R.string.retry));
                paymentIv.setImageResource(R.drawable.ic_failure);
                paymentStatusTv.setText(getString(R.string.payment_unsuccessful));
                paymentMessageTv.setVisibility(View.VISIBLE);
            }
            transactionTimeTextViewFont.setText(transactionResponse.getTransactionTime());
            //set card type
            if(transactionResponse.getPaymentType().equalsIgnoreCase(Constants.CREDIT_CARD)){
                paymentTypeTextViewFont.setText(getString(R.string.credit_card));
            }
        } else {
            isSuccessful = false;
            actionBt.setText(getString(R.string.retry));
            paymentIv.setImageResource(R.drawable.ic_failure);
            paymentStatusTv.setText(getString(R.string.payment_unsuccessful));
            paymentMessageTv.setVisibility(View.VISIBLE);
        }
        amountTextViewFont.setText("" + veritrans.getAmount());
        orderIdTextViewFont.setText(""+veritrans.getOrderId());
        actionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSuccessful) {
                    getActivity().finish();
                } else {
                    //SdkUtil.showSnackbar(getActivity(), getString(R.string.coming_soon));
                    retryTransaction();
                }
            }
        });

        return view;
    }

    private void retryTransaction() {
        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        switch (veritransSDK.getCurrentPaymentMethod()){
            case Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT :
                ((CreditDebitCardFlowActivity)getActivity()).getToken();
                break;
        }

    }

}
