package com.midtrans.sdk.uikit.views.status;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BaseActivity;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

import java.util.regex.Pattern;

/**
 * Created by ziahaqi on 7/21/17.
 */

public class PaymentStatusActivity extends BaseActivity {
    public static final String EXTRA_PAYMENT_RESULT = "payment.result";
    private static final String TAG = PaymentStatusActivity.class.getSimpleName();

    private FancyButton buttonFinish;
    private FancyButton buttonInstruction;
    private ImageView statusLogo;
    private DefaultTextView statusMessage;
    private SemiBoldTextView statusErrorMessage;
    private DefaultTextView totalAmount;
    private DefaultTextView labelOrderId;
    private DefaultTextView paymentType;
    private DefaultTextView bank;
    private DefaultTextView dueInstallment;
    private DefaultTextView statusTitle;
    private DefaultTextView totalDueAmount;
    private DefaultTextView pointAmount;


    private LinearLayout layoutTotalAmount;
    private LinearLayout layoutTotalDueAmount;
    private LinearLayout layoutInstallmentTerm;
    private LinearLayout layoutBank;
    private LinearLayout layoutOrderId;
    private LinearLayout layoutPaymentType;
    private LinearLayout layoutPointAmount;

    private LinearLayout layoutDetails;
    private FrameLayout layoutMain;

    private TransactionResponse transactionResponse;
    private String paymentStatus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        initPaymentResponse();
        bindViews();
        initTheme();
        initActionButton();
        bindData();
    }

    private void initPaymentResponse() {

        this.transactionResponse = (TransactionResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_RESULT);

        if (transactionResponse != null) {
            if (transactionResponse.getStatusCode().equals(Constants.STATUS_CODE_200) ||
                    (!TextUtils.isEmpty(transactionResponse.getTransactionStatus()) &&
                            (transactionResponse.getTransactionStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS) ||
                                    transactionResponse.getTransactionStatus().equalsIgnoreCase(Constants.STATUS_SETTLEMENT)))) {
                paymentStatus = Constants.STATUS_SUCCESS;
            } else if (transactionResponse.getStatusCode().equals(Constants.STATUS_CODE_201)
                    || (!TextUtils.isEmpty(transactionResponse.getTransactionStatus())
                    && (transactionResponse.getTransactionStatus().equalsIgnoreCase(Constants.STATUS_PENDING)))) {
                if (!TextUtils.isEmpty(transactionResponse.getFraudStatus())
                        && transactionResponse.getFraudStatus().equalsIgnoreCase(Constants.STATUS_CHALLENGE)) {
                    paymentStatus = Constants.STATUS_CHALLENGE;
                } else {
                    paymentStatus = Constants.STATUS_PENDING;
                }
            } else {
                this.paymentStatus = Constants.STATUS_FAILED;
            }
        } else {
            this.paymentStatus = Constants.STATUS_FAILED;
        }
    }

    private void initActionButton() {
        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishPayment();
            }
        });
    }

    private void finishPayment() {
        setResult(RESULT_OK);
        finish();
    }

    private void bindViews() {
        statusTitle = (DefaultTextView) findViewById(R.id.text_status_title);
        statusMessage = (DefaultTextView) findViewById(R.id.text_status_message);
        statusErrorMessage = (SemiBoldTextView) findViewById(R.id.text_status_error_message);
        labelOrderId = (DefaultTextView) findViewById(R.id.text_order_id);
        totalAmount = (DefaultTextView) findViewById(R.id.text_status_amount);
        totalDueAmount = (DefaultTextView) findViewById(R.id.text_status_due_amount);
        dueInstallment = (DefaultTextView) findViewById(R.id.text_status_due_installment);
        bank = (DefaultTextView) findViewById(R.id.text_status_bank);
        paymentType = (DefaultTextView) findViewById(R.id.text_payment_type);
        pointAmount = (DefaultTextView) findViewById(R.id.text_point_amount);

        layoutOrderId = (LinearLayout) findViewById(R.id.layout_status_order);
        layoutTotalAmount = (LinearLayout) findViewById(R.id.layout_status_total_amount);
        layoutTotalDueAmount = (LinearLayout) findViewById(R.id.layout_status_due_amount);
        layoutInstallmentTerm = (LinearLayout) findViewById(R.id.layout_status_due_installment);
        layoutBank = (LinearLayout) findViewById(R.id.layout_status_bank);
        layoutPaymentType = (LinearLayout) findViewById(R.id.layout_status_payment_type);
        layoutMain = (FrameLayout) findViewById(R.id.layout_main);
        layoutDetails = (LinearLayout) findViewById(R.id.layout_status_details);
        layoutPointAmount = (LinearLayout) findViewById(R.id.layout_status_point_amount);


        statusLogo = (ImageView) findViewById(R.id.image_status_payment);

        buttonInstruction = (FancyButton) findViewById(R.id.button_status_see_instruction);
        buttonFinish = (FancyButton) findViewById(R.id.button_status_finish);
    }

    private void initTheme() {
        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                switch (this.paymentStatus) {
                    case Constants.STATUS_SUCCESS:
                        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.payment_status_success));
                        break;
                    case Constants.STATUS_PENDING:
                        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.payment_status_pending));
                        break;
                    default:
                        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.payment_status_failed));
                        break;
                }
            }

            switch (this.paymentStatus) {
                case Constants.STATUS_SUCCESS:
                    layoutMain.setBackgroundColor(ContextCompat.getColor(this, R.color.payment_status_success));
                    break;
                case Constants.STATUS_PENDING:
                    layoutMain.setBackgroundColor(ContextCompat.getColor(this, R.color.payment_status_pending));
                    break;
                default:
                    layoutMain.setBackgroundColor(ContextCompat.getColor(this, R.color.payment_status_failed));
                    break;
            }

            setPrimaryBackgroundColor(buttonFinish);

        } catch (Exception e) {

        }

    }

    private void bindData() {
        setHeaderValues();
        setContentValues();
    }

    private void setHeaderValues() {
        switch (paymentStatus) {
            case Constants.STATUS_SUCCESS:
                statusTitle.setText(getString(R.string.payment_successful));
                statusLogo.setImageResource(R.drawable.ic_status_success);
                statusMessage.setText(getString(R.string.thank_you));
                break;
            case Constants.STATUS_PENDING:
                if (transactionResponse.getFraudStatus().equals(Constants.STATUS_CHALLENGE)) {
                    statusTitle.setText(getString(R.string.payment_challenged));
                } else {
                    statusTitle.setText(getString(R.string.payment_pending));
                }
                statusMessage.setText(getString(R.string.thank_you));
                statusLogo.setImageResource(R.drawable.ic_status_pending);
                break;
            default:
                statusTitle.setText(getString(R.string.payment_unsuccessful));
                statusMessage.setText(getString(R.string.sorry));
                statusLogo.setImageResource(R.drawable.ic_status_failed);
                statusErrorMessage.setVisibility(View.VISIBLE);

                if (transactionResponse.getTransactionStatus() != null &&
                        transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.deny))) {
                    statusErrorMessage.setText(getString(R.string.payment_deny));
                } else if (transactionResponse.getStatusCode().equals(Constants.STATUS_CODE_400)) {
                    String message = "";
                    if (transactionResponse.getValidationMessages() != null
                            && !transactionResponse.getValidationMessages().isEmpty()) {
                        message = transactionResponse.getValidationMessages().get(0);
                    }

                    if (!TextUtils.isEmpty(message) && message.toLowerCase().contains(getString(R.string.label_expired))) {
                        statusErrorMessage.setText(getString(R.string.message_payment_expired));
                    } else {
                        statusErrorMessage.setText(getString(R.string.message_payment_cannot_proccessed));
                    }
                } else {
                    statusErrorMessage.setText(transactionResponse.getStatusMessage());
                }

                setLayoutVisibilityWhenFailed();
                break;
        }
    }

    private void setLayoutVisibilityWhenFailed() {
        //order id
        String orderId = transactionResponse.getOrderId();
        if (TextUtils.isEmpty(orderId)) {
            layoutOrderId.setVisibility(View.GONE);
        } else {
            labelOrderId.setText(orderId);
        }

        //total amount
        String amount = transactionResponse.getGrossAmount();
        if (TextUtils.isEmpty(amount)) {
            layoutTotalAmount.setVisibility(View.GONE);
        } else {
            try {
                String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
                totalAmount.setText(formattedAmount);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }

        String paymentType = transactionResponse.getPaymentType();
        if (TextUtils.isEmpty(paymentType)) {
            layoutPaymentType.setVisibility(View.GONE);
        }
    }

    private void setContentValues() {
        if (transactionResponse != null) {
            // Set payment type
            switch (transactionResponse.getPaymentType()) {
                case PaymentType.CREDIT_CARD:
                    paymentType.setText(R.string.payment_method_credit_card);
                    setCreditCardPaymentStatus();
                    break;
                case PaymentType.MANDIRI_CLICKPAY:
                    paymentType.setText(R.string.payment_method_mandiri_clickpay);
                    break;
                case PaymentType.GCI:
                    paymentType.setText(R.string.payment_method_gci);
                    break;

                case PaymentType.TELKOMSEL_CASH:
                    paymentType.setText(getString(R.string.payment_method_telkomsel_cash));
                    break;
                case PaymentType.INDOSAT_DOMPETKU:
                    paymentType.setText(getString(R.string.payment_method_indosat_dompetku));
                    break;
            }

            // Set order id
            labelOrderId.setText(String.valueOf(transactionResponse.getOrderId()));

            // Set total amount
            String amount = transactionResponse.getGrossAmount();
            if (!TextUtils.isEmpty(amount)) {
                String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
                totalAmount.setText(formattedAmount);
            }

            // Set credit card properties
            if (transactionResponse.getPaymentType().equalsIgnoreCase(PaymentType.CREDIT_CARD)) {
                if (TextUtils.isEmpty(transactionResponse.getBank())) {
                    layoutBank.setVisibility(View.GONE);
                } else {
                    bank.setText(transactionResponse.getBank());
                }

                //installment term
                if (TextUtils.isEmpty(transactionResponse.getInstallmentTerm())) {
                    layoutInstallmentTerm.setVisibility(View.GONE);
                } else {
                    layoutInstallmentTerm.setVisibility(View.VISIBLE);
                    dueInstallment.setText(transactionResponse.getInstallmentTerm());
                }
            } else {
                layoutBank.setVisibility(View.GONE);
            }
        }

        buttonInstruction.setVisibility(View.GONE);
    }

    private void setCreditCardPaymentStatus() {
        int pointRedeemed = (int) transactionResponse.getPointRedeemAmount();
        if (pointRedeemed != 0.f) {
            String formattedBalance = String.format("%s", String.valueOf(pointRedeemed));
            pointAmount.setText(formattedBalance);
            layoutPointAmount.setVisibility(View.VISIBLE);
        }

        String transactionStatus = transactionResponse.getTransactionStatus();
        if (!TextUtils.isEmpty(transactionStatus) && transactionStatus.equals(UiKitConstants.STATUS_PENDING)) {
            String transactionMessage = transactionResponse.getStatusMessage();
            if (!TextUtils.isEmpty(transactionMessage) && transactionMessage.contains(MessageUtil.STATUS_UNSUCCESSFUL)) {
                statusMessage.setText(R.string.status_rba_unsuccessful);
            }
        }
    }
}
