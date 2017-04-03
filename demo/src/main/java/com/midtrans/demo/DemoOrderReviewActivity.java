package com.midtrans.demo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by rakawm on 3/16/17.
 */

public class DemoOrderReviewActivity extends AppCompatActivity implements TransactionFinishedCallback {

    private static final long DELAY = 300;
    private RelativeLayout amountContainer;
    private TextView amountText;
    private FancyButton payBtn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_review);
        bindViews();
        bindThemes();
        initPayButton();
        initMidtransSDK();
    }

    private void bindViews() {
        payBtn = (FancyButton) findViewById(R.id.btn_buy);
        amountContainer = (RelativeLayout) findViewById(R.id.amount_container);
        amountText = (TextView) findViewById(R.id.product_price_amount);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void bindThemes() {
        String themes = DemoPreferenceHelper.getStringPreference(this, DemoConfigActivity.COLOR_THEME);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back);
        if (themes != null && !TextUtils.isEmpty(themes)) {
            switch (themes) {
                case DemoThemeConstants.BLUE_THEME:
                    amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    amountText.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.RED_THEME:
                    amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_HEX));
                    amountText.setTextColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX));
                    payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.RED_PRIMARY_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.GREEN_THEME:
                    amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_HEX));
                    amountText.setTextColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX));
                    payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.ORANGE_THEME:
                    amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_HEX));
                    amountText.setTextColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX));
                    payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.BLACK_THEME:
                    amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_HEX));
                    amountText.setTextColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX));
                    payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                default:
                    amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    amountText.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
            }
        } else {
            amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
            amountText.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
            payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
            drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
        }

        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
    }

    private void initPayButton() {
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MidtransSDK.getInstance().startPaymentUiFlow(DemoOrderReviewActivity.this);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    private void initMidtransSDK() {
        MidtransSDK.getInstance().setTransactionFinishedCallback(this);
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show();
        } else {
            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                Toast.makeText(this, "Transaction Invalid", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
            }
        }

        Intent intent = new Intent(DemoOrderReviewActivity.this, DemoConfigActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(DemoOrderReviewActivity.this);
        taskStackBuilder.addNextIntent(intent);
        taskStackBuilder.startActivities();
        startActivity(intent);
    }
}
