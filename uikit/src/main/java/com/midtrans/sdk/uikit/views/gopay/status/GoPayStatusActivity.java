package com.midtrans.sdk.uikit.views.gopay.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

/**
 * Created by Fajar on 16/11/17.
 */

public class GoPayStatusActivity extends BasePaymentActivity {
    private final String EXTRA_PAYMENT_STATUS = "extra.status";

    private FancyButton buttonPrimary;
    private SemiBoldTextView textTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gopay_status);
        initActionButton();
        bindData();
    }

    private void initActionButton() {

        buttonPrimary.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/11/17 wait for back-end
                Toast.makeText(GoPayStatusActivity.this, "Will be implemented soon!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void bindData() {
        TransactionResponse response = (TransactionResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_STATUS);
        if (response != null) {
            // TODO: 16/11/17 process the deeplink here
        }
        buttonPrimary.setText(getString(R.string.payment_method_description_gopay));
        buttonPrimary.setTextBold();
        textTitle.setText(getString(R.string.payment_method_description_gopay));
    }

    @Override
    public void bindViews() {
        textTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
        buttonPrimary = (FancyButton) findViewById(R.id.button_primary);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonPrimary);
    }
}
