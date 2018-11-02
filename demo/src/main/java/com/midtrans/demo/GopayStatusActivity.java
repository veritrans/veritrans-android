package com.midtrans.demo;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Created by ziahaqi on 7/21/17.
 */

public class GopayStatusActivity extends AppCompatActivity {
    private static final String TAG = GopayStatusActivity.class.getSimpleName();

    public static final String INTENT_ORDERID = "intent.order_id";
    public static final String INTENT_AMOUNT = "intent.amount";
    public static final String INTENT_TYPE = "intent.type";
    public static final String INTENT_STATUS = "intent.status";

    private FancyButton buttonFinish;
    private ImageView imageStatusLogo;
    private DefaultTextView textStatusMessage;
    private SemiBoldTextView textStatusErrorMessage;
    private DefaultTextView textTotalAmount;
    private DefaultTextView textOrderId;
    private DefaultTextView textStatusTitle;

    private FrameLayout layoutMain;
    private LinearLayout layoutTotalAmount;
    private LinearLayout layoutOrderId;
    private LinearLayout layoutPaymentType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gopay_status);
        bindViews();
        initTheme();
        initActionButton();
        bindData();
    }

    private void initActionButton() {
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishPayment();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void finishPayment() {
        finish();
    }

    public void bindViews() {
        textStatusTitle = findViewById(R.id.text_status_title);
        textStatusMessage = findViewById(R.id.text_status_message);
        textStatusErrorMessage = findViewById(R.id.text_status_error_message);
        textOrderId = findViewById(R.id.text_order_id);
        textTotalAmount = findViewById(R.id.text_status_amount);

        layoutOrderId = findViewById(R.id.layout_status_order);
        layoutTotalAmount = findViewById(R.id.layout_status_total_amount);
        layoutPaymentType = findViewById(R.id.layout_status_payment_type);


        imageStatusLogo = findViewById(R.id.image_status_payment);
        layoutMain = findViewById(com.midtrans.sdk.uikit.R.id.layout_main);

        buttonFinish = findViewById(R.id.button_primary);
    }

    public void initTheme() {
        buttonFinish.setText(getString(R.string.done));
        buttonFinish.setTextBold();
        findViewById(R.id.button_chevron).setVisibility(View.GONE);
    }

    private void bindData() {
        setLayoutVisibilityWhenFailed();
    }

    private void setHeaderValues(String paymentStatus) {
        int colorPaymentStatus;
        if (!TextUtils.isEmpty(paymentStatus)) {
            switch (paymentStatus) {
                case "success":
                    textStatusTitle.setText(getString(R.string.payment_successful));
                    imageStatusLogo.setImageResource(R.drawable.ic_status_success);
                    textStatusMessage.setText(getString(R.string.thank_you));
                    colorPaymentStatus = ContextCompat.getColor(this, com.midtrans.sdk.uikit.R.color.payment_status_success);
                    break;
                default:
                    textStatusTitle.setText(getString(R.string.payment_unsuccessful));
                    textStatusMessage.setText(getString(R.string.sorry));
                    imageStatusLogo.setImageResource(R.drawable.ic_status_failed);
                    textStatusErrorMessage.setVisibility(View.VISIBLE);
                    colorPaymentStatus = ContextCompat.getColor(this, com.midtrans.sdk.uikit.R.color.payment_status_failed);
                    break;
            }
            setBackgroundDrawable(colorPaymentStatus);
        }
    }

    @SuppressWarnings("deprecation")
    private void setBackgroundDrawable(int drawable) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutMain.setBackgroundColor(drawable);
        } else {
            layoutMain.setBackgroundColor(drawable);
        }
    }

    private void setLayoutVisibilityWhenFailed() {
        //order id
        String orderId = getIntent().getStringExtra(INTENT_ORDERID);
        if (TextUtils.isEmpty(orderId)) {
            layoutOrderId.setVisibility(View.GONE);
        } else {
            textOrderId.setText(orderId);
        }

        //total amount
        String amount = getIntent().getStringExtra(INTENT_AMOUNT);
        if (TextUtils.isEmpty(amount)) {
            layoutTotalAmount.setVisibility(View.GONE);
        } else {
            textTotalAmount.setText(getFormattedAmount(amount));
        }

        //payment status
        String paymentType = getIntent().getStringExtra(INTENT_TYPE);
        if (TextUtils.isEmpty(paymentType)) {
            layoutPaymentType.setVisibility(View.GONE);
        }

        //payment status
        String paymentStatus = getIntent().getStringExtra(INTENT_STATUS);
        setHeaderValues(paymentStatus);
    }

    private String getFormattedAmount(String transactionResponse) {
        double amount = 0;
        try {
            amount = Double.parseDouble(transactionResponse);
        } catch (RuntimeException e) {
            Logger.e(e.getMessage());
        }

        String formattedAmount = "Rp " + getFormattedAmount(amount);
        return formattedAmount;
    }

    public static String getFormattedAmount(double amount) {
        try {
            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
            otherSymbols.setDecimalSeparator('.');
            otherSymbols.setGroupingSeparator(',');
            String amountString = new DecimalFormat("#,###.##", otherSymbols).format(amount);
            return amountString;
        } catch (NullPointerException | IllegalArgumentException e) {
            return "" + amount;
        }
    }
}
