package com.midtrans.sdk.uikit.views.shopeepay.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

public class ShopeePayStatusActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_shopeepay_status);
        bindViews();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
}
