package com.midtrans.sdk.uikit.abstracts;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.MerchantPreferences;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.MerchantData;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.models.MessageInfo;
import com.midtrans.sdk.uikit.utilities.MessageUtil;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.status.PaymentStatusActivity;
import com.midtrans.sdk.uikit.views.webview.WebViewPaymentActivity;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

/**
 * Created by ziahaqi on 7/20/17.
 */

public abstract class BasePaymentActivity extends BaseActivity {

    private static final String TAG = BasePaymentActivity.class.getSimpleName();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        try {
            initMerchantLogo();
            initToolbarBackButton();
            initItemDetails();
        } catch (Exception e) {
            Logger.e(TAG, "appbar:" + e.getMessage());
        }
    }


    private void initItemDetails() {
        View itemDetailContainer = findViewById(R.id.container_item_details);
        if (itemDetailContainer != null) {
            setPrimaryBackgroundColor(itemDetailContainer);
            setTotalAmount();
        }
    }

    private void setTotalAmount() {
        Transaction transaction = MidtransSDK.getInstance().getTransaction();
        if (transaction.getTransactionDetails() != null) {
            DefaultTextView textTotalAmount = (DefaultTextView) findViewById(R.id.text_amount);
            if (textTotalAmount != null) {
                String totalAmount = getString(R.string.prefix_money, Utils.getFormattedAmount(transaction.getTransactionDetails().getAmount()));
                textTotalAmount.setText(totalAmount);
            }
        }
    }

    protected void initMerchantLogo() {
        ImageView merchantLogo = (ImageView) findViewById(R.id.merchant_logo);
        DefaultTextView merchantNameText = (DefaultTextView) findViewById(R.id.text_page_merchant_name);

        MerchantData merchantData = MidtransSDK.getInstance().getMerchantData();

        if (merchantData != null) {
            MerchantPreferences preferences = merchantData.getPreference();
            if (preferences != null) {
                String merchantName = preferences.getDisplayName();
                String merchantLogoUrl = preferences.getLogoUrl();
                if (!TextUtils.isEmpty(merchantLogoUrl)) {
                    if (merchantLogo != null) {
                        Glide.with(this)
                                .load(merchantLogoUrl)
                                .into(merchantLogo);
                        merchantLogo.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (merchantName != null) {
                        if (merchantNameText != null && !TextUtils.isEmpty(merchantName)) {
                            merchantNameText.setVisibility(View.VISIBLE);
                            merchantNameText.setText(merchantName);
                            if (merchantLogo != null) {
                                merchantLogo.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }
        }

    }

    protected void initToolbarBackButton() {
        // Set toolbar back icon
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        if (toolbar != null) {
            Drawable backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back);
            if (backIcon != null) {
                backIcon.setColorFilter(getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);
            }
            toolbar.setNavigationIcon(backIcon);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    protected void setPageTitle(@NonNull String title) {
        DefaultTextView textTitle = (DefaultTextView) findViewById(R.id.text_page_title);
        if (textTitle != null) {
            textTitle.setText(title);
        }
    }

    protected void showPaymentStatusPage(TransactionResponse response, boolean showPaymentStatus) {
        if (isActivityRunning() && showPaymentStatus) {
            Intent intent = new Intent(this, PaymentStatusActivity.class);
            intent.putExtra(PaymentStatusActivity.EXTRA_PAYMENT_RESULT, response);
            startActivityForResult(intent, UiKitConstants.INTENT_CODE_PAYMENT_STATUS);
        } else {
            finishPayment(RESULT_OK, response);
        }
    }

    protected void showWebViewPaymentPage(TransactionResponse response, String paymentType) {
        if (isActivityRunning()) {
            Intent intent = new Intent(this, WebViewPaymentActivity.class);
            intent.putExtra(WebViewPaymentActivity.EXTRA_PAYMENT_TYPE, paymentType);
            intent.putExtra(WebViewPaymentActivity.EXTRA_PAYMENT_URL, response.getRedirectUrl());
            startActivityForResult(intent, UiKitConstants.INTENT_WEBVIEW_PAYMENT);
        } else {
            finishPayment(RESULT_OK, response);
        }
    }

    protected void finishPayment(int resultCode, TransactionResponse response) {
        Intent data = new Intent();
        data.putExtra(getString(R.string.transaction_response), response);
        setResult(resultCode, data);
        finish();
    }

    protected void showOnErrorPaymentStatusmessage(Throwable error) {
        showOnErrorPaymentStatusmessage(error, null);
    }

    protected void showOnErrorPaymentStatusmessage(Throwable error, String defaultmessage) {
        if (isActivityRunning()) {
            MessageInfo messageInfo = MessageUtil.createMessageOnError(this, error, defaultmessage);
            SdkUIFlowUtil.showToast(this, messageInfo.detailsMessage);
        }
    }
}
