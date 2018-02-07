package com.midtrans.sdk.uikit.views.bca_klikbca.status;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
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
    private final String TAG = KlikBcaStatusActivity.class.getSimpleName();
    private final String PAGE_NAME = "KlikBCA";
    private final String BUTTON_CONFIRM_NAME = "Done KlikBCA";

    private DefaultTextView textExpiry;
    private SemiBoldTextView textTitle;
    private DefaultTextView textStatusFailed;
    private LinearLayout instructionLayout;

    private AppCompatButton buttonInstruction;
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
                changeToggleInstructionVisibility();
            }
        });
    }

    private void bindData() {
        TransactionResponse response = (TransactionResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_STATUS);
        if (response != null) {
            if (!TextUtils.isEmpty(response.getStatusCode()) && (response.getStatusCode().equals(UiKitConstants.STATUS_CODE_200)
                    || response.getStatusCode().equals(UiKitConstants.STATUS_CODE_201))) {
                textExpiry.setText(getString(R.string.text_format_valid_until, response.getBcaKlikBcaExpiration()));
            } else {
                textExpiry.setVisibility(View.GONE);
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
        buttonFinish = findViewById(R.id.button_primary);
        buttonInstruction = findViewById(R.id.instruction_toggle);
        instructionLayout = findViewById(R.id.instruction_layout);
        textExpiry = findViewById(R.id.text_expiry);
        textTitle = findViewById(R.id.text_page_title);
        textStatusFailed = findViewById(R.id.text_status_failed);
    }

    private void changeToggleInstructionVisibility() {
        int colorPrimary = getPrimaryColor();

        if (colorPrimary != 0) {
            Drawable drawable;
            if (instructionLayout.getVisibility() == View.VISIBLE) {
                drawable = ContextCompat.getDrawable(this, R.drawable.ic_expand_more);
                instructionLayout.setVisibility(View.GONE);
            } else {
                drawable = ContextCompat.getDrawable(this, R.drawable.ic_expand_less);
                instructionLayout.setVisibility(View.VISIBLE);
            }

            try {
                drawable.setColorFilter(colorPrimary, PorterDuff.Mode.SRC_IN);
                buttonInstruction.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
            } catch (RuntimeException e) {
                Logger.e(TAG, "changeToggleInstructionVisibility" + e.getMessage());
            }
        }
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonFinish);
        setTextColor(buttonInstruction);
    }

    @Override
    public void onBackPressed() {
        if (presenter != null) {
            presenter.trackBackButtonClick(PAGE_NAME);
        }
        super.onBackPressed();
    }
}
