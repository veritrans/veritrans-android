package com.midtrans.sdk.ui.views;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.abtracts.BaseFragment;
import com.midtrans.sdk.ui.constants.PaymentStatus;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.midtrans.sdk.ui.widgets.SemiBoldTextView;

import java.util.regex.Pattern;

/**
 * Created by ziahaqi on 3/1/17.
 */

public class PaymentStatusFragment extends BaseFragment {


    private static final String PARAM_PAYMENT_TYPE = "payment_type";
    private static final String PARAM_PAYMENT_RESULT = "payment_result";

    private PaymentResult paymentResult;
    private String paymentType;

    // views
    private FancyButton buttonFinish;
    private FancyButton buttonInstruction;
    private ImageView ivStatusLogo;
    private DefaultTextView tvStatusMessage;
    private SemiBoldTextView tvStatusErrorMessage;
    private DefaultTextView tvTotalAmount;
    private DefaultTextView tvOrderId;
    private DefaultTextView tvTransactionTime;
    private DefaultTextView tvPaymentType;
    private DefaultTextView tvBank;
    private DefaultTextView tvInstallmentTerm;
    private DefaultTextView tvStatusTitle;
    private DefaultTextView tvTotalDueAmount;
    private LinearLayout layoutTotalAmount, layoutTotalDueAmount, layoutInstallmentTerm, layoutBank,
            layoutOrderId, layoutPaymentType, layoutPaymentTime;

    private LinearLayout layoutDetails;
    private FrameLayout layoutMain;


    public static PaymentStatusFragment newInstance(PaymentResult paymentResult, String paymentType) {
        PaymentStatusFragment fragment = new PaymentStatusFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PARAM_PAYMENT_RESULT, paymentResult);
        bundle.putString(PARAM_PAYMENT_TYPE, paymentType);
        fragment.setArguments(bundle);
        return fragment;
    }


    private void initProperties() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.paymentResult = (PaymentResult) bundle.getSerializable(PARAM_PAYMENT_RESULT);
            this.paymentType = bundle.getString(PARAM_PAYMENT_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        initProperties();
        return inflater.inflate(R.layout.fragment_payment_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindView(view);
        setupView();
        bindDataToView();
    }

    private void setupView() {
        buttonFinish.setBackgroundColor(getPrimaryColor());
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BaseActivity) getActivity()).completePayment(paymentResult);
            }
        });

        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void setPaymentDetails() {
        if (paymentType.equals(PaymentType.CREDIT_CARD)) {
            tvPaymentType.setText(getString(R.string.payment_method_credit_card));
        }

        //set order id
        tvOrderId.setText(paymentResult.getTransactionResponse().orderId);

        try {
            //set total amount
            String amount = paymentResult.getTransactionResponse().grossAmount;
            if (!TextUtils.isEmpty(amount)) {
                String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
                tvTotalAmount.setText(formattedAmount);
            }

            //transaction time
            if (!TextUtils.isEmpty(paymentResult.getTransactionResponse().transactionTime)) {
                tvTransactionTime.setText(paymentResult.getTransactionResponse().transactionTime);
                layoutPaymentTime.setVisibility(View.VISIBLE);
            } else {
                layoutPaymentTime.setVisibility(View.GONE);
            }

            // bank
            if (paymentResult.getTransactionResponse().paymentType.equalsIgnoreCase(PaymentType.CREDIT_CARD)) {
                PaymentResult<CreditCardPaymentResponse> creditCardPaymentResponsePaymentResult = paymentResult;
                if (TextUtils.isEmpty(creditCardPaymentResponsePaymentResult.getTransactionResponse().bank)) {
                    layoutBank.setVisibility(View.GONE);
                } else {
                    tvBank.setText(creditCardPaymentResponsePaymentResult.getTransactionResponse().bank);
                }

                //installment term
                if (TextUtils.isEmpty(creditCardPaymentResponsePaymentResult.getTransactionResponse().installmentTerm)) {
                    layoutInstallmentTerm.setVisibility(View.GONE);
                } else {
                    layoutInstallmentTerm.setVisibility(View.VISIBLE);
                    tvInstallmentTerm.setText(creditCardPaymentResponsePaymentResult.getTransactionResponse().installmentTerm);
                }
            }

        } catch (NullPointerException e) {
            e.printStackTrace();

        }


    }

    private void bindDataToView() {
        initlayoutColor();
        setHeaderValues();
        setPaymentDetails();
    }

    //
    private void initlayoutColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (paymentResult.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.payment_status_success));
            } else if (paymentResult.getPaymentStatus().equals(PaymentStatus.PENDING)) {
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.payment_status_pending));
            } else {
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.payment_status_failed));
            }
        }

        if (paymentResult.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
            layoutMain.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.payment_status_success));
        } else if (paymentResult.getPaymentStatus().equals(PaymentStatus.PENDING)) {
            layoutMain.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.payment_status_pending));
        } else {
            layoutMain.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.payment_status_failed));
        }
    }


    private void setHeaderValues() {
        if (paymentResult.getPaymentStatus().equals(PaymentStatus.SUCCESS)) {
            tvStatusTitle.setText(getString(R.string.payment_successful));
            ivStatusLogo.setImageResource(R.drawable.ic_status_success);
            tvStatusMessage.setText(getString(R.string.thank_you));
        } else if (paymentResult.getPaymentStatus().equals(PaymentStatus.PENDING)) {
            if (paymentResult.getTransactionResponse().fraudStatus.equals(PaymentStatus.CHALLENGE)) {
                tvStatusTitle.setText(getString(R.string.payment_challenged));
            } else {
                tvStatusTitle.setText(getString(R.string.payment_pending));
            }
            tvStatusMessage.setText(getString(R.string.thank_you));
            ivStatusLogo.setImageResource(R.drawable.ic_status_pending);
        } else {
            tvStatusTitle.setText(getString(R.string.payment_unsuccessful));
            tvStatusMessage.setText(getString(R.string.sorry));
            ivStatusLogo.setImageResource(R.drawable.ic_status_failed);
            tvStatusErrorMessage.setVisibility(View.VISIBLE);

            if (paymentResult.getTransactionResponse().transactionStatus != null &&
                    paymentResult.getPaymentStatus().equalsIgnoreCase(getString(R.string.deny))) {
                tvStatusErrorMessage.setText(getString(R.string.payment_deny));
            } else if (paymentResult.getTransactionResponse().statusCode.equals(PaymentStatus.CODE_400)) {
                String message = "";
                if (paymentResult.getTransactionResponse().validationMessages != null
                        && !paymentResult.getTransactionResponse().validationMessages.isEmpty()) {
                    message = paymentResult.getTransactionResponse().validationMessages.get(0);
                }

                if (!TextUtils.isEmpty(message) && message.toLowerCase().contains(getString(R.string.label_expired))) {
                    tvStatusErrorMessage.setText(getString(R.string.message_payment_expired));
                } else {
                    tvStatusErrorMessage.setText(getString(R.string.message_cannot_proccessed));
                }
            } else {
                tvStatusErrorMessage.setText(paymentResult.getTransactionResponse().statusMessage);
            }
        }
    }

    private void bindView(View view) {
        tvStatusTitle = (DefaultTextView) view.findViewById(R.id.text_status_title);
        tvStatusMessage = (DefaultTextView) view.findViewById(R.id.text_status_message);
        tvStatusErrorMessage = (SemiBoldTextView) view.findViewById(R.id.text_status_error_message);


        tvTotalAmount = (DefaultTextView) view.findViewById(R.id.text_status_amount);
        tvTotalDueAmount = (DefaultTextView) view.findViewById(R.id.text_status_due_amount);
        tvInstallmentTerm = (DefaultTextView) view.findViewById(R.id.text_status_due_installment);
        tvBank = (DefaultTextView) view.findViewById(R.id.text_status_bank);
        tvOrderId = (DefaultTextView) view.findViewById(R.id.text_order_id);
        tvPaymentType = (DefaultTextView) view.findViewById(R.id.text_payment_type);
        tvTransactionTime = (DefaultTextView) view.findViewById(R.id.text_status_transaction_time);

        layoutTotalAmount = (LinearLayout) view.findViewById(R.id.layout_status_total_amount);
        layoutTotalDueAmount = (LinearLayout) view.findViewById(R.id.layout_status_due_amount);
        layoutInstallmentTerm = (LinearLayout) view.findViewById(R.id.layout_status_due_installment);
        layoutBank = (LinearLayout) view.findViewById(R.id.layout_status_bank);
        layoutOrderId = (LinearLayout) view.findViewById(R.id.layout_status_order);
        layoutInstallmentTerm = (LinearLayout) view.findViewById(R.id.layout_status_due_installment);
        layoutPaymentType = (LinearLayout) view.findViewById(R.id.layout_status_payment_type);
        layoutPaymentTime = (LinearLayout) view.findViewById(R.id.layout_status_payment_time);
        layoutDetails = (LinearLayout) view.findViewById(R.id.layout_status_details);

        buttonFinish = (FancyButton) view.findViewById(R.id.btn_finish);
        buttonInstruction = (FancyButton) view.findViewById(R.id.btn_see_instruction);


        layoutMain = (FrameLayout) view.findViewById(R.id.layout_main);
        ivStatusLogo = (ImageView) view.findViewById(R.id.image_status_payment);
    }


}
