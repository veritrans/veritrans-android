package com.midtrans.sdk.uikit.views.webview;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.status.PaymentStatusPresenter;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;

/**
 * Created by ziahaqi on 8/23/17.
 */

public class WebViewPaymentActivity extends BasePaymentActivity {

    public static final String EXTRA_PAYMENT_TYPE = "extra.paymentType";
    public static final String EXTRA_PAYMENT_URL = "extra.url";
    private static final String TAG = WebViewPaymentActivity.class.getSimpleName();
    private WebView webviewContainer;
    private Toolbar toolbar;

    private DefaultTextView textMerchantName;
    private SemiBoldTextView textTitle;
    private ImageView imageMerchantLogo;

    private String webUrl;
    private String paymentType;

    private PaymentStatusPresenter presenter;

    private static void showCancelConfirmationDialog(final WebViewPaymentActivity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                try {
                    AlertDialog dialog = new AlertDialog.Builder(activity, R.style.AlertDialogCustom)
                            .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (activity != null && !activity.isFinishing()) {
                                        dialog.dismiss();
                                        finishWebViewPayment(activity, RESULT_CANCELED);
                                    }
                                }
                            })
                            .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!activity.isFinishing()) {
                                        dialog.dismiss();
                                    }
                                }
                            })
                            .setTitle(R.string.cancel_transaction)
                            .setMessage(R.string.cancel_transaction_message)
                            .create();
                    dialog.show();
                } catch (Exception e) {
                    Logger.e(TAG, "showDialog:" + e.getMessage());
                }
            } else {
                activity.finish();
            }
        }
    }

    private static void finishWebViewPayment(WebViewPaymentActivity activity, int resultCode) {
        Intent returnIntent = new Intent();
        activity.setResult(resultCode, returnIntent);
        activity.finish();
        activity.overrideBackAnimation();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initProperties();
        setContentView(R.layout.activity_webview_payment);
        initWebViewContainer();
        initPageTitle();

        if (paymentType != null && paymentType.equalsIgnoreCase(PaymentType.CREDIT_CARD)) {
            presenter.trackPageView("CC 3DS", false);
        }
    }

    private void initPageTitle() {
        if (!TextUtils.isEmpty(paymentType)) {
            switch (paymentType) {
                case PaymentType.CREDIT_CARD:
                    textTitle.setText(getString(R.string.payment_method_credit_card));
                    break;
                case PaymentType.DANAMON_ONLINE:
                    textTitle.setText(getString(R.string.payment_method_danamon_online));
                    break;
                case PaymentType.BCA_KLIKPAY:
                    textTitle.setText(getString(R.string.payment_method_bca_klikpay));
                    break;
                case PaymentType.MANDIRI_ECASH:
                    textTitle.setText(getString(R.string.payment_method_mandiri_ecash));
                    break;
                case PaymentType.BRI_EPAY:
                    textTitle.setText(getString(R.string.payment_method_bri_epay));
                    break;
                case PaymentType.CIMB_CLICKS:
                    textTitle.setText(getString(R.string.payment_method_cimb_clicks));
                    break;
            }
        }
    }

    private void initProperties() {
        Intent intent = getIntent();
        if (intent != null) {
            webUrl = intent.getStringExtra(EXTRA_PAYMENT_URL);
            paymentType = intent.getStringExtra(EXTRA_PAYMENT_TYPE);
        }
        presenter = new PaymentStatusPresenter();
    }

    @SuppressLint("AddJavascriptInterface")
    private void initWebViewContainer() {
        webviewContainer.getSettings().setAllowFileAccess(false);
        webviewContainer.getSettings().setJavaScriptEnabled(true);
        webviewContainer.setInitialScale(1);
        webviewContainer.getSettings().setLoadWithOverviewMode(true);
        webviewContainer.getSettings().setUseWideViewPort(true);
        webviewContainer.getSettings().setBuiltInZoomControls(true);
        webviewContainer.getSettings().setDisplayZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webviewContainer.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webviewContainer.setWebViewClient(new MidtransWebViewClient(this, paymentType));
        webviewContainer.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Logger.d(TAG, consoleMessage.message());
                return true;
            }
        });
        webviewContainer.loadUrl(webUrl);
    }

    @Override
    public void bindViews() {
        webviewContainer = findViewById(R.id.webview_container);
        imageMerchantLogo = findViewById(R.id.merchant_logo);

        textTitle = findViewById(R.id.text_page_title);
        textMerchantName = findViewById(R.id.text_page_merchant_name);
        toolbar = findViewById(R.id.main_toolbar);
    }

    @Override
    public void initTheme() {
        Drawable backIcon = ContextCompat.getDrawable(this, R.drawable.ic_back);
        if (backIcon != null) {
            backIcon.setColorFilter(getPrimaryColor(), PorterDuff.Mode.SRC_ATOP);
        }
        toolbar.setNavigationIcon(backIcon);
    }

    @Override
    public void onBackPressed() {
        if (presenter != null && paymentType != null && paymentType.equalsIgnoreCase(PaymentType.CREDIT_CARD)) {
            presenter.trackBackButtonClick("CC 3DS");
        }
        showCancelConfirmationDialog(this);
    }

    private static class MidtransWebViewClient extends WebViewClient {

        private final String paymentType;
        private WebViewPaymentActivity activity;

        private MidtransWebViewClient(WebViewPaymentActivity activity, String type) {
            this.paymentType = type;
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logger.d(TAG, "shouldOverrideUrlLoading()>url:" + url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Logger.d(TAG, "onPageFinished()>url:" + url);
            if (activity != null && activity.isActivityRunning()) {
                if (url.contains(UiKitConstants.CALLBACK_PATTERN_3DS) || url.contains(UiKitConstants.CALLBACK_PATTERN_RBA)) {
                    finishWebViewPayment(activity, RESULT_OK);
                }
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Logger.d(TAG, "onPageStarted()>url:" + url);
            super.onPageStarted(view, url, favicon);

            if (activity != null && activity.isActivityRunning()) {
                if (!TextUtils.isEmpty(paymentType) && paymentType.equals(PaymentType.BCA_KLIKPAY)) {
                    if (url.contains(UiKitConstants.CALLBACK_BCA_KLIKPAY)) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                } else if (!TextUtils.isEmpty(paymentType) && paymentType.equals(PaymentType.MANDIRI_ECASH)) {
                    if (url.contains(UiKitConstants.CALLBACK_MANDIRI_ECAH)) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                } else if (!TextUtils.isEmpty(paymentType) && paymentType.equals(PaymentType.BRI_EPAY)) {
                    if (url.contains(UiKitConstants.CALLBACK_BRI_EPAY)) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                } else if (!TextUtils.isEmpty(paymentType) && paymentType.equals(PaymentType.CIMB_CLICKS)) {
                    if (url.contains(UiKitConstants.CALLBACK_CIMB_CLICKS)) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                } else if (!TextUtils.isEmpty(paymentType) && paymentType.equals(PaymentType.DANAMON_ONLINE)) {
                    if (url.contains(UiKitConstants.CALLBACK_DANAMON_ONLINE)) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                }
            }
        }

    }
}
