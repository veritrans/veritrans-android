package com.midtrans.sdk.uikit.view.status;

import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.Promo;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BaseActivity;
import com.midtrans.sdk.uikit.base.model.PaymentResponse;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.CurrencyHelper;
import com.midtrans.sdk.uikit.utilities.MessageHelper;
import com.midtrans.sdk.uikit.utilities.PaymentListHelper;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;
import com.midtrans.sdk.uikit.widget.SemiBoldTextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

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
    private DefaultTextView textPromoAmount;

    private LinearLayout layoutTotalAmount;
    private LinearLayout layoutTotalDueAmount;
    private LinearLayout layoutInstallmentTerm;
    private LinearLayout layoutOrderId;
    private LinearLayout layoutPaymentType;
    private LinearLayout layoutPointAmount;
    private LinearLayout layoutPromoAmount;

    private LinearLayout layoutDetails;
    private FrameLayout layoutMain;

    private PaymentResponse paymentResponse;
    private String paymentStatus;
    private PaymentStatusPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        initPaymentResponse();
        initPresenter();
        initViews();
        initData();
        initActionButton();
        initTheme();
    }

    private void initPresenter(){
        presenter = new PaymentStatusPresenter(getIntent().getSerializableExtra(Constants.INTENT_DATA_CALLBACK));
    }

    private void initPaymentResponse() {
        paymentResponse = (PaymentResponse) getIntent().getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
        if (paymentResponse != null) {
            if (paymentResponse.getStatusCode().equals(Constants.STATUS_CODE_200) ||
                    (!TextUtils.isEmpty(paymentResponse.getTransactionStatus()) &&
                            (paymentResponse.getTransactionStatus().equalsIgnoreCase(Constants.STATUS_SUCCESS) ||
                                    paymentResponse.getTransactionStatus().equalsIgnoreCase(Constants.STATUS_SETTLEMENT)))) {
                paymentStatus = Constants.STATUS_SUCCESS;
            } else if (paymentResponse.getStatusCode().equals(Constants.STATUS_CODE_201)
                    || (!TextUtils.isEmpty(paymentResponse.getTransactionStatus())
                    && (paymentResponse.getTransactionStatus().equalsIgnoreCase(Constants.STATUS_PENDING)))) {
                if (!TextUtils.isEmpty(paymentResponse.getFraudStatus())
                        && paymentResponse.getFraudStatus().equalsIgnoreCase(Constants.STATUS_CHALLENGE)) {
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

    private void initViews() {
        textStatusTitle = findViewById(R.id.text_status_title);
        textStatusMessage = findViewById(R.id.text_status_message);
        textStatusErrorMessage = findViewById(R.id.text_status_error_message);
        textOrderId = findViewById(R.id.text_order_id);
        textTotalAmount = findViewById(R.id.text_status_amount);
        textTotalDueAmount = findViewById(R.id.text_status_due_amount);
        textDueInstallment = findViewById(R.id.text_status_due_installment);
        textPaymentType = findViewById(R.id.text_payment_type);
        textPointAmount = findViewById(R.id.text_point_amount);
        textPromoAmount = findViewById(R.id.text_status_promo_amount);

        layoutOrderId = findViewById(R.id.layout_status_order);
        layoutTotalAmount = findViewById(R.id.layout_status_total_amount);
        layoutTotalDueAmount = findViewById(R.id.layout_status_due_amount);
        layoutInstallmentTerm = findViewById(R.id.layout_status_due_installment);
        layoutPaymentType = findViewById(R.id.layout_status_payment_type);
        layoutMain = findViewById(R.id.layout_main);
        layoutDetails = findViewById(R.id.layout_status_details);
        layoutPointAmount = findViewById(R.id.layout_status_point_amount);
        layoutPromoAmount = findViewById(R.id.layout_status_promo);


        imageStatusLogo = findViewById(R.id.image_status_payment);

        buttonInstruction = findViewById(R.id.button_status_see_instruction);
        buttonFinish = findViewById(R.id.button_primary);
    }

    private void initActionButton() {
        buttonInstruction.setOnClickListener(v -> {

        });

        buttonFinish.setOnClickListener(v -> {
            if (PaymentListHelper.convertTransactionStatus(getIntent().getSerializableExtra(Constants.INTENT_DATA_CALLBACK)) != null) {
                String buttonName;
                String paymentType = paymentResponse.getPaymentType();
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
            }
            finishPayment();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void finishPayment() {
        setResult(RESULT_OK);
        finish();
    }

    private void initTheme() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int primaryColor = getPrimaryDarkColor();
            if (primaryColor != 0) {
                getWindow().setStatusBarColor(primaryColor);
            }
        }

        int colorPaymentStatus;
        switch (this.paymentStatus) {
            case Constants.STATUS_SUCCESS:
                colorPaymentStatus = ContextCompat.getColor(this, R.color.payment_status_success);
                break;
            case Constants.STATUS_PENDING:
                colorPaymentStatus = ContextCompat.getColor(this, R.color.payment_status_pending);
                break;
            default:
                colorPaymentStatus = ContextCompat.getColor(this, R.color.payment_status_failed);
                break;
        }

        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{getPrimaryColor(), colorPaymentStatus});
        setBackgroundDrawable(drawable);


        buttonInstruction.setTextColor(ContextCompat.getColor(this, R.color.white));
        buttonInstruction.setIconColorFilter(ContextCompat.getColor(this, R.color.white));

        setPrimaryBackgroundColor(buttonFinish);
        buttonFinish.setText(getString(R.string.done));
        buttonFinish.setTextBold();
        findViewById(R.id.button_chevron).setVisibility(View.GONE);
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundDrawable(GradientDrawable drawable) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutMain.setBackground(drawable);
        } else {
            layoutMain.setBackgroundDrawable(drawable);
        }
    }

    private void initData() {
        setHeaderValues();
        setContentValues();
    }

    private void initProperties() {
        this.presenter = new PaymentStatusPresenter();
    }

    private void setHeaderValues() {
        if (!TextUtils.isEmpty(paymentStatus)) {
            switch (paymentStatus) {
                case Constants.STATUS_SUCCESS:
                    textStatusTitle.setText(getString(R.string.payment_successful));
                    imageStatusLogo.setImageResource(R.drawable.ic_status_success);
                    textStatusMessage.setText(getString(R.string.thank_you));
                    break;
                case Constants.STATUS_PENDING:
                    if (paymentResponse.getFraudStatus().equals(Constants.STATUS_CHALLENGE)) {
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

                    if (paymentResponse.getTransactionStatus() != null &&
                            paymentResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.deny))) {
                        textStatusErrorMessage.setText(getString(R.string.message_payment_denied));
                    } else if (paymentResponse.getStatusCode().equals(Constants.STATUS_CODE_400)) {
                        String message = "";
                        if (paymentResponse.getValidationMessages() != null
                                && !paymentResponse.getValidationMessages().isEmpty()) {
                            message = paymentResponse.getValidationMessages().get(0);
                        }

                        if (!TextUtils.isEmpty(message) && message.toLowerCase().contains(getString(R.string.label_expired))) {
                            textStatusErrorMessage.setText(getString(R.string.message_payment_expired));
                        } else {
                            textStatusErrorMessage.setText(getString(R.string.message_payment_cannot_proccessed));
                        }
                    } else if (paymentResponse.getStatusCode().equals(Constants.STATUS_CODE_411)
                            && !TextUtils.isEmpty(paymentResponse.getStatusMessage())
                            && paymentResponse.getStatusMessage().toLowerCase().contains(MessageHelper.PROMO_UNAVAILABLE)) {
                        textStatusErrorMessage.setText(getString(R.string.promo_unavailable));
                    } else if (paymentResponse.getStatusCode().equals(Constants.STATUS_CODE_406)) {
                        textStatusErrorMessage.setText(getString(R.string.message_payment_paid));
                    } else {
                        textStatusErrorMessage.setText(paymentResponse.getStatusMessage());
                    }

                    setLayoutVisibilityWhenFailed();
                    break;
            }
        }
    }

    private void setLayoutVisibilityWhenFailed() {
        //order id
        String orderId = paymentResponse.getOrderId();
        if (TextUtils.isEmpty(orderId)) {
            layoutOrderId.setVisibility(View.GONE);
        } else {
            textOrderId.setText(orderId);
        }

        //total amount
        String amount = paymentResponse.getGrossAmount();
        if (TextUtils.isEmpty(amount)) {
            layoutTotalAmount.setVisibility(View.GONE);
        } else {
            textTotalAmount.setText(getFormattedAmount(paymentResponse));
        }

        String paymentType = paymentResponse.getPaymentType();
        if (TextUtils.isEmpty(paymentType)) {
            layoutPaymentType.setVisibility(View.GONE);
        }
    }

    private void setContentValues() {
        if (paymentResponse != null) {
            // Set payment type
            switch (paymentResponse.getPaymentType()) {
                case PaymentType.CREDIT_CARD:
                    textPaymentType.setText(R.string.payment_method_credit_card);
                    setCreditCardPaymentStatus();
                    break;
                case PaymentType.MANDIRI_CLICKPAY:
                    textPaymentType.setText(R.string.payment_method_mandiri_clickpay);
                    break;
                case PaymentType.TELKOMSEL_CASH:
                    textPaymentType.setText(getString(R.string.payment_method_telkomsel_cash));
                    break;
                case PaymentType.GOPAY:
                    textPaymentType.setText(getString(R.string.payment_method_gopay));
                    break;
            }

            // Set order id
            textOrderId.setText(String.valueOf(paymentResponse.getOrderId()));

            // Set total amount
            textTotalAmount.setText(getFormattedAmount(paymentResponse));

            // Set credit card properties
            if (paymentResponse.getPaymentType().equalsIgnoreCase(PaymentType.CREDIT_CARD)) {

                /*//installment term
                if (TextUtils.isEmpty(transactionResponse.getInstallmentTerm())) {
                    layoutInstallmentTerm.setVisibility(View.GONE);
                } else {
                    layoutInstallmentTerm.setVisibility(View.VISIBLE);
                    textDueInstallment.setText(transactionResponse.getInstallmentTerm());
                }*/
            }
        }

        buttonInstruction.setVisibility(View.GONE);
    }

    private String getFormattedAmount(PaymentResponse transactionResponse) {
        String amountStr = transactionResponse.getGrossAmount();
        String currency = transactionResponse.getCurrency();
        double amount = 0;

        try {
            amount = Double.parseDouble(amountStr);
        } catch (RuntimeException e) {
            Logger.error(e.getMessage());
        }

        return CurrencyHelper.formatAmount(this, amount, currency);
    }

    private void setCreditCardPaymentStatus() {
        /*int pointRedeemed = (int) transactionResponse.getPointRedeemAmount();
        if (pointRedeemed != 0.f) {
            String formattedBalance = CurrencyHelper.formatAmount(this, pointRedeemed, paymentResponse.getCurrency());
            textPointAmount.setText(formattedBalance);
            layoutPointAmount.setVisibility(View.VISIBLE);
        }*/

        String transactionStatus = paymentResponse.getTransactionStatus();
        if (!TextUtils.isEmpty(transactionStatus) && transactionStatus.equals(Constants.STATUS_PENDING)) {
            String transactionMessage = paymentResponse.getStatusMessage();
            if (!TextUtils.isEmpty(transactionMessage) && transactionMessage.contains(MessageHelper.STATUS_UNSUCCESSFUL)) {
                textStatusMessage.setText(R.string.status_rba_unsuccessful);
            }
        }

        /*PaymentDetails paymentDetails = getMidtransSdk().getPaymentDetails();
        if (paymentDetails != null) {
            Promo promo = paymentDetails.getPromoSelected();
            if (promo != null && promo.getId() != 0) {
                layoutPromoAmount.setVisibility(View.VISIBLE);
                textPromoAmount.setText(CurrencyHelper.formatAmount(
                        this,
                        promo.getCalculatedDiscountAmount(),
                        transactionResponse.getCurrency()));
            }
        }*/
    }

}