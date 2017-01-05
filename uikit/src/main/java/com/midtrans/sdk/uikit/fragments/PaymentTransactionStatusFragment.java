package com.midtrans.sdk.uikit.fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BCAKlikPayActivity;
import com.midtrans.sdk.uikit.activities.CIMBClickPayActivity;
import com.midtrans.sdk.uikit.activities.CreditDebitCardFlowActivity;
import com.midtrans.sdk.uikit.activities.EpayBriActivity;
import com.midtrans.sdk.uikit.activities.MandiriECashActivity;
import com.midtrans.sdk.uikit.activities.OffersActivity;

import java.util.regex.Pattern;

/**
 * Created by shivam on 10/27/15.
 */
public class PaymentTransactionStatusFragment extends Fragment {

    private static final String TRANSACTION_RESPONSE_PARAM = "transaction_response_param";
    private static final int STATUS_SUCCESS = 2;
    private static final int STATUS_PENDING = 1;
    private static final int STATUS_FAILED = 0;
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
    private TextView textBank;
    private TextView textInstallmentTerm;
    private TextView textStatusTitle;
    private int count = 1;
    private LinearLayout detailsTable;
    private FrameLayout layoutMain;
    private RelativeLayout layoutDueTotalAmount, layoutInstallment, layoutTransactionTime, layoutBank;

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
        bindDataToView();
    }

    private void initializeViews(View view) {
        amountTextView = (TextView) view.findViewById(R.id.text_status_amount);
        orderIdTextView = (TextView) view.findViewById(R.id.text_order_id);
        transactionTimeTextView = (TextView) view.findViewById(R.id.text_status_transaction_time);
        paymentTypeTextView = (TextView) view.findViewById(R.id.text_payment_type);
        actionBt = (Button) view.findViewById(R.id.btn_action);
        paymentIv = (ImageView) view.findViewById(R.id.image_payment);
        paymentStatusTv = (TextView) view.findViewById(R.id.text_payment_status);
        paymentMessageTv = (TextView) view.findViewById(R.id.text_payment_message);
        detailsTable = (LinearLayout) view.findViewById(R.id.transaction_info_layout);
        layoutInstallment = (RelativeLayout) view.findViewById(R.id.layout_status_due_installment);
        layoutDueTotalAmount = (RelativeLayout) view.findViewById(R.id.layout_status_due_amount);
        layoutInstallment = (RelativeLayout) view.findViewById(R.id.layout_status_due_installment);
        layoutTransactionTime = (RelativeLayout) view.findViewById(R.id.layout_status_payment_time);
        layoutBank = (RelativeLayout) view.findViewById(R.id.layout_status_bank);
        textBank = (TextView) view.findViewById(R.id.text_status_bank);
        textInstallmentTerm = (TextView) view.findViewById(R.id.text_status_due_installment);
        textStatusTitle = (TextView) view.findViewById(R.id.text_title_payment_status);
        layoutMain = (FrameLayout) view.findViewById(R.id.layout_transaction_status);
    }

    private void setPaymentStatusValues() {
        if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200)) ||
                transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.capital_success)) ||
                transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.settlement))) {

            paymentIv.setImageResource(R.drawable.ic_status_success);
            textStatusTitle.setText(getString(R.string.payment_successful));
            paymentStatusTv.setText(getString(R.string.thank_you));
            paymentMessageTv.setVisibility(View.GONE);
            setInstallmentStatus();
            setupStatusBarColor(STATUS_SUCCESS);
            setupStatusInfo();
        } else if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_201)) ||
                transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.pending))) {
            setupStatusBarColor(STATUS_PENDING);
            setupStatusInfo();

            paymentIv.setImageResource(R.drawable.ic_status_pending);
            paymentMessageTv.setVisibility(View.GONE);
            paymentStatusTv.setText(getString(R.string.thank_you));
            if (transactionResponse.getFraudStatus().equalsIgnoreCase(getString(R.string.challenge))) {
                textStatusTitle.setText(getString(R.string.payment_challenged));
            } else {
                textStatusTitle.setText(getString(R.string.payment_pending));

            }
        } else {
            setUiForFailure();
        }

    }

    private void setupStatusInfo() {

        //set payment type
        setPaymentType();

        TransactionRequest request = MidtransSDK.getInstance().getTransactionRequest();

        //set order id
        String orderId = TextUtils.isEmpty(transactionResponse.getOrderId()) ? request.getOrderId() : transactionResponse.getOrderId();
        orderIdTextView.setText(transactionResponse.getOrderId());

        //set total amount
        String amount = TextUtils.isEmpty(transactionResponse.getGrossAmount()) ? String.valueOf(request.getAmount()) : transactionResponse.getGrossAmount();
        try {
            if (!TextUtils.isEmpty(amount)) {
                String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
                amountTextView.setText(formattedAmount);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();

        }

        // set transaction time
        if (!TextUtils.isEmpty(transactionResponse.getTransactionTime())) {
            transactionTimeTextView.setText(transactionResponse.getTransactionTime());
            layoutTransactionTime.setVisibility(View.VISIBLE);
        } else {
            layoutTransactionTime.setVisibility(View.GONE);
        }

        // setbank
        if (!TextUtils.isEmpty(transactionResponse.getBank())) {
            textBank.setText(transactionResponse.getBank());
            layoutBank.setVisibility(View.VISIBLE);
        } else {
            layoutBank.setVisibility(View.GONE);
        }

        //installment
        if (!TextUtils.isEmpty(transactionResponse.getInstallmentTerm())) {
            textInstallmentTerm.setText(transactionResponse.getInstallmentTerm());
            layoutInstallment.setVisibility(View.VISIBLE);
        } else {
            layoutInstallment.setVisibility(View.GONE);
        }

    }

    private void setupStatusBarColor(int status) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (status == STATUS_FAILED) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.payment_status_failed));
                layoutMain.setBackgroundColor(getContext().getResources().getColor(R.color.payment_status_failed));
            } else if (status == STATUS_PENDING) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.payment_status_pending));
                layoutMain.setBackgroundColor(getContext().getResources().getColor(R.color.payment_status_pending));

            } else if (status == STATUS_SUCCESS) {
                getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.payment_status_success));

            }
        }
    }

    private void setInstallmentStatus() {
        if (!TextUtils.isEmpty(transactionResponse.getInstallmentTerm())) {
            textBank.setText(transactionResponse.getBank());
            textInstallmentTerm.setText(getString(R.string.installment_months, transactionResponse.getInstallmentTerm()));
            layoutInstallment.setVisibility(View.VISIBLE);
        }
    }

    private void bindDataToView() {

        if (transactionResponse != null) {
            try {
                Logger.i("transactionstatus:" + transactionResponse.getString());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
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
        paymentIv.setImageResource(R.drawable.ic_status_failed);
        paymentStatusTv.setText(getString(R.string.sorry));
        textStatusTitle.setText(getString(R.string.payment_unsuccessful));
        setupStatusBarColor(STATUS_FAILED);
        setupStatusInfo();
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
        } else if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.failed_code_400))) {
            paymentMessageTv.setVisibility(View.VISIBLE);
            String message = "";
            if (transactionResponse.getValidationMessages() != null && !transactionResponse.getValidationMessages().isEmpty()) {
                message = transactionResponse.getValidationMessages().get(0);
            }

            if (!TextUtils.isEmpty(message) && message.toLowerCase().contains(getString(R.string.label_expired))) {
                paymentMessageTv.setText(getString(R.string.message_payment_expired));
            } else {
                paymentMessageTv.setText(getString(R.string.message_cannot_proccessed));
            }

        } else {
            if (!TextUtils.isEmpty(transactionResponse.getStatusMessage())) {
                paymentMessageTv.setVisibility(View.VISIBLE);
                paymentMessageTv.setText(getString(R.string.message_payment_failed));
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