package com.midtrans.sdk.ui.views.status;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseActivity;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.PaymentStatus;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.widgets.DefaultTextView;
import com.midtrans.sdk.ui.widgets.FancyButton;
import com.midtrans.sdk.ui.widgets.SemiBoldTextView;

import java.util.regex.Pattern;

/**
 * Created by rakawm on 3/29/17.
 */

public class PaymentStatusActivity extends BaseActivity {
    private FancyButton buttonFinish;
    private FancyButton buttonInstruction;
    private ImageView statusLogo;
    private DefaultTextView statusMessage;
    private SemiBoldTextView statusErrorMessage;
    private DefaultTextView totalAmount;
    private DefaultTextView orderId;
    private DefaultTextView paymentType;
    private DefaultTextView bank;
    private DefaultTextView installmentTerm;
    private DefaultTextView statusTitle;
    private DefaultTextView totalDueAmount;

    private LinearLayout layoutTotalAmount;
    private LinearLayout layoutTotalDueAmount;
    private LinearLayout layoutInstallmentTerm;
    private LinearLayout layoutBank;
    private LinearLayout layoutOrderId;
    private LinearLayout layoutPaymentType;

    private LinearLayout layoutDetails;
    private FrameLayout layoutMain;

    private PaymentResult paymentResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_payment_status);
        initViews();
        initResponse();
        initLayoutColor();
        bindResponse();
        setHeaderValues();
        initFinishButton();
    }

    private void initViews() {
        statusTitle = (DefaultTextView) findViewById(R.id.text_status_title);
        statusMessage = (DefaultTextView) findViewById(R.id.text_status_message);
        statusErrorMessage = (SemiBoldTextView) findViewById(R.id.text_status_error_message);
        totalAmount = (DefaultTextView) findViewById(R.id.text_status_amount);
        totalDueAmount = (DefaultTextView) findViewById(R.id.text_status_due_amount);
        installmentTerm = (DefaultTextView) findViewById(R.id.text_status_due_installment);
        bank = (DefaultTextView) findViewById(R.id.text_status_bank);
        orderId = (DefaultTextView) findViewById(R.id.text_order_id);
        paymentType = (DefaultTextView) findViewById(R.id.text_payment_type);
        layoutTotalAmount = (LinearLayout) findViewById(R.id.layout_status_total_amount);
        layoutTotalDueAmount = (LinearLayout) findViewById(R.id.layout_status_due_amount);
        layoutInstallmentTerm = (LinearLayout) findViewById(R.id.layout_status_due_installment);
        layoutBank = (LinearLayout) findViewById(R.id.layout_status_bank);
        layoutOrderId = (LinearLayout) findViewById(R.id.layout_status_order);
        layoutInstallmentTerm = (LinearLayout) findViewById(R.id.layout_status_due_installment);
        layoutPaymentType = (LinearLayout) findViewById(R.id.layout_status_payment_type);
        layoutDetails = (LinearLayout) findViewById(R.id.layout_status_details);
        buttonFinish = (FancyButton) findViewById(R.id.btn_finish);
        buttonInstruction = (FancyButton) findViewById(R.id.btn_see_instruction);
        layoutMain = (FrameLayout) findViewById(R.id.layout_main);
        statusLogo = (ImageView) findViewById(R.id.image_status_payment);
    }

    private void initResponse() {
        paymentResult = (PaymentResult) getIntent().getSerializableExtra(Constants.PAYMENT_RESULT);
    }

    private void bindResponse() {
        if (paymentResult != null) {
            // Set payment type
            switch (paymentResult.getTransactionResponse().paymentType) {
                case PaymentType.CREDIT_CARD:
                    paymentType.setText(R.string.payment_method_credit_card);
                    break;
                case PaymentType.MANDIRI_CLICKPAY:
                    paymentType.setText(R.string.payment_method_mandiri_clickpay);
                    break;
                case PaymentType.GCI:
                    paymentType.setText(R.string.payment_method_gci);
                    break;
            }

            // Set order id
            orderId.setText(paymentResult.getTransactionResponse().orderId);

            // Set total amount
            String amount = paymentResult.getTransactionResponse().grossAmount;
            if (!TextUtils.isEmpty(amount)) {
                String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
                totalAmount.setText(formattedAmount);
            }

            // Set credit card properties
            if (paymentResult.getTransactionResponse().paymentType.equalsIgnoreCase(PaymentType.CREDIT_CARD)) {
                PaymentResult<CreditCardPaymentResponse> creditCardPaymentResponse = (PaymentResult<CreditCardPaymentResponse>) paymentResult;
                if (TextUtils.isEmpty(creditCardPaymentResponse.getTransactionResponse().bank)) {
                    layoutBank.setVisibility(View.GONE);
                } else {
                    bank.setText(creditCardPaymentResponse.getTransactionResponse().bank);
                }

                //installment term
                if (TextUtils.isEmpty(creditCardPaymentResponse.getTransactionResponse().installmentTerm)) {
                    layoutInstallmentTerm.setVisibility(View.GONE);
                } else {
                    layoutInstallmentTerm.setVisibility(View.VISIBLE);
                    installmentTerm.setText(creditCardPaymentResponse.getTransactionResponse().installmentTerm);
                }
            } else {
                layoutBank.setVisibility(View.GONE);
            }
        }

        buttonInstruction.setVisibility(View.GONE);
    }

    private void initLayoutColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            switch (paymentResult.getPaymentStatus()) {
                case PaymentStatus.SUCCESS:
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.payment_status_success));
                    break;
                case PaymentStatus.PENDING:
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.payment_status_pending));
                    break;
                default:
                    getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.payment_status_failed));
                    break;
            }
        }

        switch (paymentResult.getPaymentStatus()) {
            case PaymentStatus.SUCCESS:
                layoutMain.setBackgroundColor(ContextCompat.getColor(this, R.color.payment_status_success));
                break;
            case PaymentStatus.PENDING:
                layoutMain.setBackgroundColor(ContextCompat.getColor(this, R.color.payment_status_pending));
                break;
            default:
                layoutMain.setBackgroundColor(ContextCompat.getColor(this, R.color.payment_status_failed));
                break;
        }

        if (MidtransUi.getInstance().getColorTheme() != null
                && MidtransUi.getInstance().getColorTheme().getPrimaryColor() != 0) {
            buttonFinish.setBackgroundColor(MidtransUi.getInstance().getColorTheme().getPrimaryColor());
            // Set font into pay now button
            if (!TextUtils.isEmpty(MidtransUi.getInstance().getSemiBoldFontPath())) {
                buttonFinish.setCustomTextFont(MidtransUi.getInstance().getSemiBoldFontPath());
            }
        }
    }


    private void setHeaderValues() {
        switch (paymentResult.getPaymentStatus()) {
            case PaymentStatus.SUCCESS:
                statusTitle.setText(getString(R.string.payment_successful));
                statusLogo.setImageResource(R.drawable.ic_status_success);
                statusMessage.setText(getString(R.string.thank_you));
                break;
            case PaymentStatus.PENDING:
                if (paymentResult.getTransactionResponse().fraudStatus.equals(PaymentStatus.CHALLENGE)) {
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

                if (paymentResult.getTransactionResponse().transactionStatus != null &&
                        paymentResult.getPaymentStatus().equalsIgnoreCase(getString(R.string.deny))) {
                    statusErrorMessage.setText(getString(R.string.payment_deny));
                } else if (paymentResult.getTransactionResponse().statusCode.equals(PaymentStatus.CODE_400)) {
                    String message = "";
                    if (paymentResult.getTransactionResponse().validationMessages != null
                            && !paymentResult.getTransactionResponse().validationMessages.isEmpty()) {
                        message = paymentResult.getTransactionResponse().validationMessages.get(0);
                    }

                    if (!TextUtils.isEmpty(message) && message.toLowerCase().contains(getString(R.string.label_expired))) {
                        statusErrorMessage.setText(getString(R.string.message_payment_expired));
                    } else {
                        statusErrorMessage.setText(getString(R.string.message_cannot_proccessed));
                    }
                } else {
                    statusErrorMessage.setText(paymentResult.getTransactionResponse().statusMessage);
                }
                break;
        }
    }

    private void initFinishButton() {
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishPayment();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishPayment();
    }

    private void finishPayment() {
        setResult(RESULT_OK);
        finish();
    }
}
