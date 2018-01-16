package com.midtrans.sdk.uikit.views.bca_klikbca.status;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.activities.KlikBCAInstructionActivity;
import com.midtrans.sdk.uikit.models.MessageInfo;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.status.PaymentStatusPresenter;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

/**
 * Created by ziahaqi on 9/18/17.
 */

public class KlikBcaStatusActivity extends BasePaymentActivity {

    public static final String EXTRA_PAYMENT_STATUS = "extra.status";
    private final String PAGE_NAME = "KlikBCA";
    private final String BUTTON_CONFIRM_NAME = "Done KlikBCA";

    private SemiBoldTextView textExpiry;
    private SemiBoldTextView textTitle;
    private DefaultTextView textStatusFailed;

    private FancyButton buttonInstruction;
    private FancyButton buttonFinish;

    private PaymentStatusPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bca_klikbca_status);
        presenter = new PaymentStatusPresenter();
        initActionButton();
        bindData();
    }

    private void initActionButton() {
        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.trackButtonClick(BUTTON_CONFIRM_NAME, PAGE_NAME);
                finish();
            }
        });

        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isActivityRunning()) {
                    Intent intent = new Intent(KlikBcaStatusActivity.this, KlikBCAInstructionActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void bindData() {
        TransactionResponse response = (TransactionResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_STATUS);
        if (response != null) {
            if (!TextUtils.isEmpty(response.getStatusCode()) && (response.getStatusCode().equals(UiKitConstants.STATUS_CODE_200)
                    || response.getStatusCode().equals(UiKitConstants.STATUS_CODE_201))) {
                textExpiry.setText(response.getBcaKlikBcaExpiration());
            } else {
                findViewById(R.id.container_expiry).setVisibility(View.GONE);
                MessageInfo messageInfo = MessageUtil.createpaymentFailedMessage(this, response, null);
                textStatusFailed.setText(messageInfo.detailsMessage);
            }
        }
        buttonFinish.setText(getString(R.string.complete_payment_at_klik_bca));
        buttonFinish.setTextBold();
        textTitle.setText(getString(R.string.klik_bca));

        //track page view after page properly loaded
        boolean isFirstPage = getIntent().getBooleanExtra(USE_DEEP_LINK, true);
        presenter.trackPageView(PAGE_NAME, isFirstPage);
    }

    @Override
    public void bindViews() {
        buttonFinish = (FancyButton) findViewById(R.id.button_primary);
        buttonInstruction = (FancyButton) findViewById(R.id.button_instruction);
        textExpiry = (SemiBoldTextView) findViewById(R.id.text_expiry);
        textTitle = (SemiBoldTextView) findViewById(R.id.text_page_title);
        textStatusFailed = (DefaultTextView) findViewById(R.id.text_status_failed);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonFinish);
        setTextColor(buttonInstruction);
        setIconColorFilter(buttonInstruction);
    }

    @Override
    public void onBackPressed() {
        if (presenter != null) {
            presenter.trackBackButtonClick(PAGE_NAME);
        }
        super.onBackPressed();
    }
}
