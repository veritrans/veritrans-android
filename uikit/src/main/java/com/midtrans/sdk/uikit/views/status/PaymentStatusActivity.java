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

import com.midtrans.sdk.corekit.core.Logger;
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
    private final String PAGE_NAME_SUCCESS = "Page Success";
    private final String PAGE_NAME_FAILED = "Page Failed";

    private FancyButton buttonFinish;
    private FancyButton buttonInstruction;
    private ImageView imageStatusLogo;
    private DefaultTextView textStatusMessage;
    private SemiBoldTextView textStatusErrorMessage;
    private DefaultTextView textTotalAmount;
    private DefaultTextView textOrderId;
    private DefaultTextView textPaymentType;
    private DefaultTextView textDueInstallment;
    private DefaultTextView textStatusTitle;
    private DefaultTextView textTotalDueAmount;
    private DefaultTextView textPointAmount;

    private LinearLayout layoutTotalAmount;
    private LinearLayout layoutTotalDueAmount;
    private LinearLayout layoutInstallmentTerm;
    private LinearLayout layoutOrderId;
    private LinearLayout layoutPaymentType;
    private LinearLayout layoutPointAmount;

    private LinearLayout layoutDetails;
    private FrameLayout layoutMain;

    private TransactionResponse transactionResponse;
    private String paymentStatus;
    private PaymentStatusPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProperties();
        initPaymentResponse();
        setContentView(R.layout.activity_payment_status);
        bindViews();
        initTheme();
        initActionButton();
        bindData();
    }

    private void initPaymentResponse() {

        this.transactionResponse = (TransactionResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_RESULT);

        if (transactionResponse != null) {
            if (transactionResponse.getStatusCode().equals(UiKitConstants.STATUS_CODE_200) ||
                    (!TextUtils.isEmpty(transactionResponse.getTransactionStatus()) &&
                            (transactionResponse.getTransactionStatus().equalsIgnoreCase(UiKitConstants.STATUS_SUCCESS) ||
                                    transactionResponse.getTransactionStatus().equalsIgnoreCase(UiKitConstants.STATUS_SETTLEMENT)))) {
                paymentStatus = UiKitConstants.STATUS_SUCCESS;
                presenter.trackPageView(PAGE_NAME_SUCCESS, false);
            } else if (transactionResponse.getStatusCode().equals(UiKitConstants.STATUS_CODE_201)
                    || (!TextUtils.isEmpty(transactionResponse.getTransactionStatus())
                    && (transactionResponse.getTransactionStatus().equalsIgnoreCase(UiKitConstants.STATUS_PENDING)))) {
                if (!TextUtils.isEmpty(transactionResponse.getFraudStatus())
                        && transactionResponse.getFraudStatus().equalsIgnoreCase(UiKitConstants.STATUS_CHALLENGE)) {
                    paymentStatus = UiKitConstants.STATUS_CHALLENGE;
                } else {
                    paymentStatus = UiKitConstants.STATUS_PENDING;
                }
            } else {
                this.paymentStatus = UiKitConstants.STATUS_FAILED;
                presenter.trackPageView(PAGE_NAME_FAILED, false);
            }
        } else {
            this.paymentStatus = UiKitConstants.STATUS_FAILED;
            finishPayment();
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

                if (transactionResponse != null) {
                    String buttonName;
                    String paymentType = transactionResponse.getPaymentType();
                    switch (paymentType) {
                        case PaymentType.CREDIT_CARD:
                            buttonName = "Done Credit Card";
                            break;
                        case PaymentType.MANDIRI_CLICKPAY:
                            buttonName = "Done Mandiri Clickpay";
                            break;
                        default:
                            buttonName = "Next";
                            break;
                    }

                    if (paymentStatus != null) {
                        if (paymentStatus.equalsIgnoreCase(UiKitConstants.STATUS_SUCCESS)) {
                            presenter.trackButtonClick(buttonName, PAGE_NAME_SUCCESS);
                        } else if (paymentStatus.equalsIgnoreCase(UiKitConstants.STATUS_FAILED)) {
                            presenter.trackButtonClick(buttonName, PAGE_NAME_FAILED);
                        }
                    }
                }

                finishPayment();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (presenter != null && paymentStatus != null) {
            if (paymentStatus.equalsIgnoreCase(UiKitConstants.STATUS_SUCCESS)) {
                presenter.trackBackButtonClick(PAGE_NAME_SUCCESS);
            } else if (paymentStatus.equalsIgnoreCase(UiKitConstants.STATUS_FAILED)) {
                presenter.trackBackButtonClick(PAGE_NAME_FAILED);
            }
        }
        super.onBackPressed();
    }

    private void finishPayment() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void bindViews() {
        textStatusTitle = (DefaultTextView) findViewById(R.id.text_status_title);
        textStatusMessage = (DefaultTextView) findViewById(R.id.text_status_message);
        textStatusErrorMessage = (SemiBoldTextView) findViewById(R.id.text_status_error_message);
        textOrderId = (DefaultTextView) findViewById(R.id.text_order_id);
        textTotalAmount = (DefaultTextView) findViewById(R.id.text_status_amount);
        textTotalDueAmount = (DefaultTextView) findViewById(R.id.text_status_due_amount);
        textDueInstallment = (DefaultTextView) findViewById(R.id.text_status_due_installment);
        textPaymentType = (DefaultTextView) findViewById(R.id.text_payment_type);
        textPointAmount = (DefaultTextView) findViewById(R.id.text_point_amount);

        layoutOrderId = (LinearLayout) findViewById(R.id.layout_status_order);
        layoutTotalAmount = (LinearLayout) findViewById(R.id.layout_status_total_amount);
        layoutTotalDueAmount = (LinearLayout) findViewById(R.id.layout_status_due_amount);
        layoutInstallmentTerm = (LinearLayout) findViewById(R.id.layout_status_due_installment);
        layoutPaymentType = (LinearLayout) findViewById(R.id.layout_status_payment_type);
        layoutMain = (FrameLayout) findViewById(R.id.layout_main);
        layoutDetails = (LinearLayout) findViewById(R.id.layout_status_details);
        layoutPointAmount = (LinearLayout) findViewById(R.id.layout_status_point_amount);


        imageStatusLogo = (ImageView) findViewById(R.id.image_status_payment);

        buttonInstruction = (FancyButton) findViewById(R.id.button_status_see_instruction);
        buttonFinish = (FancyButton) findViewById(R.id.button_primary);
    }

    @Override
    public void initTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch (this.paymentStatus) {
                case UiKitConstants.STATUS_SUCCESS:
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.payment_status_success));
                    break;
                case UiKitConstants.STATUS_PENDING:
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.payment_status_pending));
                    break;
                default:
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.payment_status_failed));
                    break;
            }
        }

        switch (this.paymentStatus) {
            case UiKitConstants.STATUS_SUCCESS:
                layoutMain.setBackgroundColor(ContextCompat.getColor(this, R.color.payment_status_success));
                break;
            case UiKitConstants.STATUS_PENDING:
                layoutMain.setBackgroundColor(ContextCompat.getColor(this, R.color.payment_status_pending));
                break;
            default:
                layoutMain.setBackgroundColor(ContextCompat.getColor(this, R.color.payment_status_failed));
                break;
        }

        buttonInstruction.setTextColor(ContextCompat.getColor(this, R.color.white));
        buttonInstruction.setIconColorFilter(ContextCompat.getColor(this, R.color.white));

        setPrimaryBackgroundColor(buttonFinish);
        buttonFinish.setText(getString(R.string.done));
        buttonFinish.setTextBold();
        findViewById(R.id.button_chevron).setVisibility(View.GONE);
    }

    private void bindData() {
        setHeaderValues();
        setContentValues();
    }

    private void initProperties() {
        this.presenter = new PaymentStatusPresenter();
    }

    private void setHeaderValues() {
        if (!TextUtils.isEmpty(paymentStatus)) {
            switch (paymentStatus) {
                case UiKitConstants.STATUS_SUCCESS:
                    textStatusTitle.setText(getString(R.string.payment_successful));
                    imageStatusLogo.setImageResource(R.drawable.ic_status_success);
                    textStatusMessage.setText(getString(R.string.thank_you));
                    break;
                case UiKitConstants.STATUS_PENDING:
                    if (transactionResponse.getFraudStatus().equals(UiKitConstants.STATUS_CHALLENGE)) {
                        textStatusTitle.setText(getString(R.string.payment_challenged));
                    } else {
                        textStatusTitle.setText(getString(R.string.payment_pending));
                    }
                    textStatusMessage.setText(getString(R.string.thank_you));
                    imageStatusLogo.setImageResource(R.drawable.ic_status_pending);
                    break;
                default:
                    textStatusTitle.setText(getString(R.string.payment_unsuccessful));
                    textStatusMessage.setText(getString(R.string.sorry));
                    imageStatusLogo.setImageResource(R.drawable.ic_status_failed);
                    textStatusErrorMessage.setVisibility(View.VISIBLE);

                    if (transactionResponse.getTransactionStatus() != null &&
                            transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.deny))) {
                        textStatusErrorMessage.setText(getString(R.string.message_payment_denied));
                    } else if (transactionResponse.getStatusCode().equals(UiKitConstants.STATUS_CODE_400)) {
                        String message = "";
                        if (transactionResponse.getValidationMessages() != null
                                && !transactionResponse.getValidationMessages().isEmpty()) {
                            message = transactionResponse.getValidationMessages().get(0);
                        }

                        if (!TextUtils.isEmpty(message) && message.toLowerCase().contains(getString(R.string.label_expired))) {
                            textStatusErrorMessage.setText(getString(R.string.message_payment_expired));
                        } else {
                            textStatusErrorMessage.setText(getString(R.string.message_payment_cannot_proccessed));
                        }
                    } else {
                        textStatusErrorMessage.setText(transactionResponse.getStatusMessage());
                    }

                    setLayoutVisibilityWhenFailed();
                    break;
            }
        }
    }

    private void setLayoutVisibilityWhenFailed() {
        //order id
        String orderId = transactionResponse.getOrderId();
        if (TextUtils.isEmpty(orderId)) {
            layoutOrderId.setVisibility(View.GONE);
        } else {
            textOrderId.setText(orderId);
        }

        //total amount
        String amount = transactionResponse.getGrossAmount();
        if (TextUtils.isEmpty(amount)) {
            layoutTotalAmount.setVisibility(View.GONE);
        } else {
            try {
                String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
                textTotalAmount.setText(formattedAmount);
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
                    textPaymentType.setText(R.string.payment_method_credit_card);
                    setCreditCardPaymentStatus();
                    break;
                case PaymentType.MANDIRI_CLICKPAY:
                    textPaymentType.setText(R.string.payment_method_mandiri_clickpay);
                    break;
                case PaymentType.GCI:
                    textPaymentType.setText(R.string.payment_method_gci);
                    break;

                case PaymentType.TELKOMSEL_CASH:
                    textPaymentType.setText(getString(R.string.payment_method_telkomsel_cash));
                    break;
                case PaymentType.INDOSAT_DOMPETKU:
                    textPaymentType.setText(getString(R.string.payment_method_indosat_dompetku));
                    break;
                case PaymentType.GOPAY:
                    textPaymentType.setText(getString(R.string.payment_method_gopay));
                    break;
            }

            // Set order id
            textOrderId.setText(String.valueOf(transactionResponse.getOrderId()));

            // Set total amount
            try {
                String amount = transactionResponse.getGrossAmount();
                if (!TextUtils.isEmpty(amount)) {
                    String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
                    textTotalAmount.setText(formattedAmount);
                }
            } catch (RuntimeException e) {
                Logger.e(TAG, "amount:" + e.getMessage());
            }

            // Set credit card properties
            if (transactionResponse.getPaymentType().equalsIgnoreCase(PaymentType.CREDIT_CARD)) {

                //installment term
                if (TextUtils.isEmpty(transactionResponse.getInstallmentTerm())) {
                    layoutInstallmentTerm.setVisibility(View.GONE);
                } else {
                    layoutInstallmentTerm.setVisibility(View.VISIBLE);
                    textDueInstallment.setText(transactionResponse.getInstallmentTerm());
                }
            }
        }

        buttonInstruction.setVisibility(View.GONE);
    }

    private void setCreditCardPaymentStatus() {
        int pointRedeemed = (int) transactionResponse.getPointRedeemAmount();
        if (pointRedeemed != 0.f) {
            String formattedBalance = String.format("%s", String.valueOf(pointRedeemed));
            textPointAmount.setText(formattedBalance);
            layoutPointAmount.setVisibility(View.VISIBLE);
        }

        String transactionStatus = transactionResponse.getTransactionStatus();
        if (!TextUtils.isEmpty(transactionStatus) && transactionStatus.equals(UiKitConstants.STATUS_PENDING)) {
            String transactionMessage = transactionResponse.getStatusMessage();
            if (!TextUtils.isEmpty(transactionMessage) && transactionMessage.contains(MessageUtil.STATUS_UNSUCCESSFUL)) {
                textStatusMessage.setText(R.string.status_rba_unsuccessful);
            }
        }
    }
}
