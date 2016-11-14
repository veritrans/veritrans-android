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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
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
    private TransactionResponse transactionResponse;
    private boolean isSuccessful;

    // views
    private Button buttonAction = null;
    private ImageView imagePayment = null;
    private TextView textPaymentStatus = null;
    private TextView textPaymentMessage = null;
    private TextView textAmount = null;
    private TextView textOrderId = null;
    private TextView textTransactionTime = null;
    private TextView textPaymentType = null;
    private TextView textTitlePaymentStatus;
    private int count = 1;
    private LinearLayout layoutDetailsTable;
    private RelativeLayout layoutPaymentType;
    private RelativeLayout layoutPaymentTime;
    private FrameLayout layoutMain;

    public PaymentTransactionStatusFragment() {
        // Required empty public constructor
    }

    public static PaymentTransactionStatusFragment newInstance(TransactionResponse transactionResponse) {
        Logger.i("payment status get instance called");
        Logger.d("xstatus>response:" + transactionResponse);
        Logger.d("xstatus>response:" + transactionResponse.getStatusCode());
        Logger.d("xstatus>response:" + transactionResponse.getTransactionTime());
        Logger.d("xstatus>response:" + transactionResponse.getStatusMessage());
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
        textAmount = (TextView) view.findViewById(R.id.text_amount);
        textOrderId = (TextView) view.findViewById(R.id.text_order_id);
        textTransactionTime = (TextView) view.findViewById(R.id.text_transaction_time);
        textPaymentType = (TextView) view.findViewById(R.id.text_payment_type);
        buttonAction = (Button) view.findViewById(R.id.btn_action);
        imagePayment = (ImageView) view.findViewById(R.id.image_payment);
        textPaymentStatus = (TextView) view.findViewById(R.id.text_payment_status);
        textPaymentMessage = (TextView) view.findViewById(R.id.text_payment_message);
        layoutDetailsTable = (LinearLayout) view.findViewById(R.id.transaction_info_layout);
        textTitlePaymentStatus = (TextView) view.findViewById(R.id.text_title_payment_status);
        layoutPaymentType = (RelativeLayout) view.findViewById(R.id.layout_trans_payment_type);
        layoutPaymentTime = (RelativeLayout) view.findViewById(R.id.layout_trans_payment_time);
        layoutMain = (FrameLayout) view.findViewById(R.id.layout_transaction_status);
    }

    private void setPaymentStatusValues() {
        if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200)) ||
                transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.capital_success)) ||
                transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.settlement))) {

            imagePayment.setImageResource(R.drawable.ic_status_success);
            textTitlePaymentStatus.setText(R.string.payment_successful);
            textPaymentStatus.setText(getString(R.string.thank_you));
            textPaymentMessage.setVisibility(View.GONE);
        } else if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_201)) ||
                transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.pending))) {
            if (transactionResponse.getFraudStatus().equalsIgnoreCase(getString(R.string.challenge))) {
                imagePayment.setImageResource(R.drawable.ic_status_pending);
                textTitlePaymentStatus.setText(R.string.payment_challenged);
            } else {
                imagePayment.setImageResource(R.drawable.ic_status_pending);
                textTitlePaymentStatus.setText(R.string.payment_pending);
            }
            textPaymentStatus.setText(getString(R.string.thank_you));
            textPaymentMessage.setVisibility(View.GONE);
            layoutMain.setBackgroundColor(getResources().getColor(R.color.payment_status_pending));
        } else {
            setUiForFailure();
        }

        String orderId = transactionResponse.getOrderId() == null ?
                MidtransSDK.getInstance().getTransactionRequest().getOrderId() : transactionResponse.getOrderId();
        textOrderId.setText(orderId);

        try {
            String amount = TextUtils.isEmpty(transactionResponse.getGrossAmount()) ?
                    MidtransSDK.getInstance().getTransactionRequest().getAmount() + "" : transactionResponse.getGrossAmount();
            if (!TextUtils.isEmpty(amount)) {
                String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
                textAmount.setText(formattedAmount);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(transactionResponse.getTransactionTime())) {
            layoutPaymentTime.setVisibility(View.GONE);
        } else {
            textTransactionTime.setText(transactionResponse.getTransactionTime());
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

        buttonAction.setOnClickListener(new View.OnClickListener() {
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
        imagePayment.setImageResource(R.drawable.ic_status_failed);
        textPaymentStatus.setText(getString(R.string.sorry));
        textTitlePaymentStatus.setText(getString(R.string.payment_unsuccessful));
        layoutMain.setBackgroundColor(getResources().getColor(R.color.payment_status_failed));

        if (transactionResponse == null) {
            textPaymentMessage.setVisibility(View.VISIBLE);
            textPaymentMessage.setText(getString(R.string.api_fail_message));
            layoutDetailsTable.setVisibility(View.GONE);
            return;
        }

        if (transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.deny))) {
            textPaymentMessage.setVisibility(View.VISIBLE);
            textPaymentMessage.setText(getString(R.string.payment_deny));
        } else if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.failed_code_400))) {
            textPaymentMessage.setVisibility(View.VISIBLE);
            String message = "";
            if (transactionResponse.getValidationMessages() != null && !transactionResponse.getValidationMessages().isEmpty()) {
                message = transactionResponse.getValidationMessages().get(0);
            }

            if (!TextUtils.isEmpty(message) && message.toLowerCase().contains(getString(R.string.label_expired))) {
                textPaymentMessage.setText(getString(R.string.message_payment_expired));
            } else {
                textPaymentMessage.setText(getString(R.string.message_cannot_proccessed));
            }
        } else {
            if (!TextUtils.isEmpty(transactionResponse.getStatusMessage())) {
                textPaymentMessage.setVisibility(View.VISIBLE);
                textPaymentMessage.setText(getString(R.string.message_payment_failed));
            } else {
                textPaymentMessage.setVisibility(View.GONE);
            }
        }
    }

    private void setPaymentType() {

        if (transactionResponse == null) {
            return;
        }

        if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_epay_bri))) {
            textPaymentType.setText(R.string.epay_bri);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_mandiri_bill_payment))) {
            textPaymentType.setText(R.string.mandiri_bill_payment);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_cimb_clicks))) {
            textPaymentType.setText(R.string.cimb_clicks);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_mandiri_ecash))) {
            textPaymentType.setText(R.string.mandiri_e_cash);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_mandiri_clickpay))) {
            textPaymentType.setText(R.string.mandiri_click_pay);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_indomaret))) {
            textPaymentType.setText(R.string.indomaret);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_credit_debit))) {
            textPaymentType.setText(R.string.credit_card);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_indosat_dompetku))) {
            textPaymentType.setText(R.string.indosat_dompetku);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_bank_transfer))) {
            textPaymentType.setText(R.string.bank_transfer);
        } else if (transactionResponse.getPaymentType().equalsIgnoreCase(getString(R.string.payment_bca_click))) {
            textPaymentType.setText(getString(R.string.payment_method_bca_klikpay));
        } else {
            layoutPaymentType.setVisibility(View.GONE);
        }
    }

}