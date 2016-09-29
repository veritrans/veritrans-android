package com.midtrans.sdk.uikit.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BCAKlikPayActivity;
import com.midtrans.sdk.uikit.activities.CIMBClickPayActivity;
import com.midtrans.sdk.uikit.activities.CreditDebitCardFlowActivity;
import com.midtrans.sdk.uikit.activities.EpayBriActivity;
import com.midtrans.sdk.uikit.activities.MandiriECashActivity;
import com.midtrans.sdk.uikit.activities.NotificationActivity;
import com.midtrans.sdk.uikit.activities.OffersActivity;

import java.util.regex.Pattern;

/**
 * Created by shivam on 10/27/15.
 */
public class PaymentTransactionStatusFragment extends Fragment {

    private static final String TRANSACTION_RESPONSE_PARAM = "transaction_response_param";
    private TransactionResponse transactionResponse;
    private boolean isSuccessful;

    // views
    private Button actionBt = null;
    private ImageView paymentIv = null;
    private TextView paymentStatusTv = null;
    private TextView paymentMessageTv = null;
    private TextView amountTextView = null;
    private TextView orderIdTextView = null;
    private TextView transactionTimeTextView = null;
    private TextView paymentTypeTextView = null;
    private int count = 1;
    private LinearLayout detailsTable;

    public PaymentTransactionStatusFragment() {
        // Required empty public constructor
    }

    public static PaymentTransactionStatusFragment newInstance(TransactionResponse transactionResponse) {
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
            transactionResponse = (TransactionResponse) getArguments().getSerializable(TRANSACTION_RESPONSE_PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_transaction_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeViews(view);
        if (getActivity().getClass().getName().equalsIgnoreCase(NotificationActivity.class.getName())) {
            bindDataForNotificationData();
        } else {
            bindDataToView();
        }
    }

    private void initializeViews(View view) {
        amountTextView = (TextView) view.findViewById(R.id.text_amount);
        orderIdTextView = (TextView) view.findViewById(R.id.text_order_id);
        transactionTimeTextView = (TextView) view.findViewById(R.id.text_transaction_time);
        paymentTypeTextView = (TextView) view.findViewById(R.id.text_payment_type);
        actionBt = (Button) view.findViewById(R.id.btn_action);
        paymentIv = (ImageView) view.findViewById(R.id.image_payment);
        paymentStatusTv = (TextView) view.findViewById(R.id.text_payment_status);
        paymentMessageTv = (TextView) view.findViewById(R.id.text_payment_message);
        detailsTable = (LinearLayout) view.findViewById(R.id.transaction_info_layout);
    }

    private void bindDataForNotificationData() {
        actionBt.setVisibility(View.GONE);
        setPaymentType();
        setPaymentStatusValues();

    }

    private void setPaymentStatusValues() {
        if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200)) ||
                transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.capital_success)) ||
                transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.settlement))) {
            paymentIv.setImageResource(R.drawable.ic_successful);
            paymentStatusTv.setText(getString(R.string.payment_successful));
            paymentMessageTv.setVisibility(View.GONE);
        } else if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_201)) ||
                transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.pending))) {
            if (transactionResponse.getFraudStatus().equalsIgnoreCase(getString(R.string.challenge))) {
                paymentIv.setImageResource(R.drawable.ic_successful);
                paymentStatusTv.setText(getString(R.string.payment_successful));
                paymentMessageTv.setVisibility(View.GONE);
            } else {
                paymentIv.setImageResource(R.drawable.ic_pending);
                paymentStatusTv.setText(getString(R.string.payment_pending));
            }
            //}
        } else {
            setUiForFailure();
        }
        try {
            transactionTimeTextView.setText(transactionResponse.getTransactionTime());
            String amount = transactionResponse.getGrossAmount();
            String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
            amountTextView.setText(formattedAmount);
            orderIdTextView.setText(transactionResponse.getOrderId());
        } catch (NullPointerException e) {
            e.printStackTrace();

        }
        if (transactionResponse != null && TextUtils.isEmpty(transactionResponse.getTransactionTime()) &&
                TextUtils.isEmpty(transactionResponse.getGrossAmount()) && TextUtils.isEmpty(transactionResponse.getOrderId())) {
            detailsTable.setVisibility(View.GONE);
        }
    }

    private void bindDataToView() {

        if (transactionResponse != null) {
            try {
                Logger.i("transactionstatus:" + transactionResponse.getString());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            setPaymentType();
            setPaymentStatusValues();

        } else {
            setUiForFailure();
        }

        actionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getClass().getName().equalsIgnoreCase(CreditDebitCardFlowActivity.class.getName())) {
                    ((CreditDebitCardFlowActivity) getActivity())
                            .setResultCode(getActivity().RESULT_OK);
                    ((CreditDebitCardFlowActivity) getActivity()).setResultAndFinish();
                } else if (getActivity().getClass().getName().equalsIgnoreCase(EpayBriActivity.class.getName())) {
                    ((EpayBriActivity) getActivity())
                            .setResultCode(getActivity().RESULT_OK);
                    ((EpayBriActivity) getActivity()).setResultAndFinish();
                } else if (getActivity().getClass().getName().equalsIgnoreCase(CIMBClickPayActivity
                        .class.getName())) {
                    ((CIMBClickPayActivity) getActivity())
                            .setResultCode(getActivity().RESULT_OK);
                    ((CIMBClickPayActivity) getActivity()).setResultAndFinish();
                } else if (getActivity().getClass().getName().equalsIgnoreCase
                        (MandiriECashActivity
                                .class.getName())) {
                    ((MandiriECashActivity) getActivity())
                            .setResultCode(getActivity().RESULT_OK);
                    ((MandiriECashActivity) getActivity()).setResultAndFinish();
                } else if (getActivity().getClass().getName().equalsIgnoreCase
                        (OffersActivity.class.getName())) {
                    ((OffersActivity) getActivity())
                            .setResultCode(getActivity().RESULT_OK);
                    ((OffersActivity) getActivity()).setResultAndFinish();
                } else if (getActivity().getClass().getName().equals(BCAKlikPayActivity.class.getName())) {
                    ((BCAKlikPayActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    ((BCAKlikPayActivity) getActivity()).setResultAndFinish();
                }

            }
        });
    }

    private void setUiForFailure() {
        isSuccessful = false;
        paymentIv.setImageResource(R.drawable.ic_failure);
        paymentStatusTv.setText(getString(R.string.payment_unsuccessful));

        if (transactionResponse == null) {
            paymentMessageTv.setVisibility(View.VISIBLE);
            paymentMessageTv.setText(getString(R.string.api_fail_message));
            detailsTable.setVisibility(View.GONE);
            return;
        }
        try {
            Logger.i("fail_message" + transactionResponse.getStatusMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.deny))) {
            paymentMessageTv.setVisibility(View.VISIBLE);
            paymentMessageTv.setText(getString(R.string.payment_deny));
        } else {
            if (!TextUtils.isEmpty(transactionResponse.getStatusMessage())) {
                paymentMessageTv.setVisibility(View.VISIBLE);
                paymentMessageTv.setText(transactionResponse.getStatusMessage());
            } else {
                paymentMessageTv.setVisibility(View.GONE);
            }
        }
    }

    private void setPaymentType() {
        try {
            Logger.i("PaymentType:" + transactionResponse.getPaymentType());
        } catch (NullPointerException e) {

        }
        if (transactionResponse == null) {
            return;
        }
        if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_epay_bri))) {
            paymentTypeTextView.setText(R.string.epay_bri);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_mandiri_bill_payment))) {
            paymentTypeTextView.setText(R.string.mandiri_bill_payment);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_cimb_clicks))) {
            paymentTypeTextView.setText(R.string.cimb_clicks);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_mandiri_ecash))) {
            paymentTypeTextView.setText(R.string.mandiri_e_cash);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_mandiri_clickpay))) {
            paymentTypeTextView.setText(R.string.mandiri_click_pay);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_indomaret))) {
            paymentTypeTextView.setText(R.string.indomaret);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_credit_debit))) {
            paymentTypeTextView.setText(R.string.credit_card);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_indosat_dompetku))) {
            paymentTypeTextView.setText(R.string.indosat_dompetku);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_bank_transfer))) {
            paymentTypeTextView.setText(R.string.bank_transfer);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_bca_click))) {
            paymentTypeTextView.setText(getString(R.string.payment_method_bca_klikpay));
        }

    }

}