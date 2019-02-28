package com.midtrans.sdk.uikit.view.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.Constants;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class WebViewPaymentActivity extends BasePaymentActivity {

    public static final String EXTRA_PAYMENT_URL = "intent.extra.payment.url";

    private static final String TAG = WebViewPaymentActivity.class.getSimpleName();
    private WebView webviewContainer;

    private String webUrl;
    private String paymentType;

    @Override
    protected void initTheme() {

    }

    @Override
    protected void initToolbarAndView() {
        super.initToolbarAndView();
        webviewContainer = findViewById(R.id.webview_container);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_webview);
        initToolbarAndView();
        initProperties();
        initWebViewContainer();
        initPageTitle();
    }

    private void initPageTitle() {
        if (!TextUtils.isEmpty(paymentType)) {
            switch (paymentType) {
                case PaymentType.CREDIT_CARD:
                    setTitle(getString(R.string.payment_method_credit_card));
                    break;
                case PaymentType.DANAMON_ONLINE:
                    setTitle(getString(R.string.payment_method_danamon_online));
                    break;
                case PaymentType.BCA_KLIKPAY:
                    setTitle(getString(R.string.payment_method_bca_klikpay));
                    break;
                case PaymentType.MANDIRI_ECASH:
                    setTitle(getString(R.string.payment_method_mandiri_ecash));
                    break;
                case PaymentType.BRI_EPAY:
                    setTitle(getString(R.string.payment_method_bri_epay));
                    break;
                case PaymentType.CIMB_CLICKS:
                    setTitle(getString(R.string.payment_method_cimb_clicks));
                    break;
                case PaymentType.AKULAKU:
                    setTitle(getString(R.string.payment_method_akulaku));
                    break;
            }
        }
    }

    @Override
    protected void initProperties() {
        super.initProperties();
        Intent intent = getIntent();
        if (intent != null) {
            webUrl = intent.getStringExtra(EXTRA_PAYMENT_URL);
            paymentType = intent.getStringExtra(EXTRA_PAYMENT_TYPE);
        }
    }

    @SuppressLint("AddJavascriptInterface")
    private void initWebViewContainer() {
        webviewContainer.getSettings().setAllowFileAccess(false);
        webviewContainer.getSettings().setJavaScriptEnabled(true);
        webviewContainer.getSettings().setDomStorageEnabled(true);
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
                Logger.debug(TAG, consoleMessage.message());
                return true;
            }
        });
        webviewContainer.loadUrl(webUrl);
    }

    private void showCancelConfirmationDialog(final WebViewPaymentActivity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                try {
                    AlertDialog dialog = new AlertDialog.Builder(activity, R.style.AlertDialogCustom)
                            .setPositiveButton(R.string.text_yes, (dialog1, which) -> {
                                if (activity != null && !activity.isFinishing()) {
                                    dialog1.dismiss();
                                    finishWebViewPayment(activity, RESULT_OK);
                                }
                            })
                            .setNegativeButton(R.string.text_no, (dialog12, which) -> {
                                if (!activity.isFinishing()) {
                                    dialog12.dismiss();
                                }
                            })
                            .setTitle(R.string.cancel_transaction)
                            .setMessage(R.string.cancel_transaction_message)
                            .create();
                    dialog.show();
                } catch (Exception e) {
                    Logger.error(TAG, "showDialog:" + e.getMessage());
                }
            } else {
                activity.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        showCancelConfirmationDialog(this);
    }

    private class MidtransWebViewClient extends WebViewClient {

        private final String paymentType;
        private WebViewPaymentActivity activity;

        private MidtransWebViewClient(WebViewPaymentActivity activity, String type) {
            this.paymentType = type;
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logger.debug(TAG, "shouldOverrideUrlLoading()>url:" + url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Logger.debug(TAG, "onPageFinished()>url:" + url);
            if (activity != null && activity.isActivityRunning()) {
                if (url.contains(Constants.CALLBACK_PATTERN_3DS) || url.contains(Constants.CALLBACK_PATTERN_RBA)) {
                    finishWebViewPayment(activity, RESULT_OK);
                }
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Logger.debug(TAG, "onPageStarted()>url:" + url);
            super.onPageStarted(view, url, favicon);
            if (activity != null && activity.isActivityRunning()) {
                if (!TextUtils.isEmpty(paymentType) && paymentType.equals(PaymentType.BCA_KLIKPAY)) {
                    if (url.contains(Constants.CALLBACK_BCA_KLIKPAY)) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                } else if (!TextUtils.isEmpty(paymentType) && paymentType.equals(PaymentType.MANDIRI_ECASH)) {
                    if (url.contains(Constants.CALLBACK_MANDIRI_ECAH)) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                } else if (!TextUtils.isEmpty(paymentType) && paymentType.equals(PaymentType.BRI_EPAY)) {
                    if (url.contains(Constants.CALLBACK_BRI_EPAY)) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                } else if (!TextUtils.isEmpty(paymentType) && paymentType.equals(PaymentType.CIMB_CLICKS)) {
                    if (url.contains(Constants.CALLBACK_CIMB_CLICKS)) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                } else if (!TextUtils.isEmpty(paymentType) && paymentType.equals(PaymentType.DANAMON_ONLINE)) {
                    if (url.contains(Constants.CALLBACK_DANAMON_ONLINE)) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                } else if (!TextUtils.isEmpty(paymentType) && paymentType.equals(PaymentType.AKULAKU)) {
                    if (url.contains(Constants.CALLBACK_AKULAKU)) {
                        finishWebViewPayment(activity, RESULT_OK);
                    }
                }
            }
        }

    }
}