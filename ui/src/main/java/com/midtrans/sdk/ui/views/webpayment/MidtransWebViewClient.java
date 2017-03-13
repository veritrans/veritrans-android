package com.midtrans.sdk.ui.views.webpayment;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.utils.UiUtils;

/**
 * Created by ziahaqi on 2/28/17.
 */

public class MidtransWebViewClient extends WebViewClient {
    private final String type;
    private String TAG = getClass().getSimpleName();

    private WebViewClientCallback callback;

    public interface WebViewClientCallback {
        void onCompleted();
    }

    public MidtransWebViewClient(@NonNull WebViewClientCallback callback, String type) {
        this.type = type;
        this.callback = callback;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Logger.d(TAG, "Url:loading:" + url);
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Logger.d(TAG, "Url:finished:" + url);
        UiUtils.hideProgressDialog();
        if (url.contains(Constants.WEB_VIEW_CALLBACK_STRING)) {
            if (callback != null) {
                callback.onCompleted();
            }
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Logger.d(TAG, "Url:started:" + url);
        super.onPageStarted(view, url, favicon);
        if (type != null && type.equals(PaymentType.BCA_KLIKPAY)) {
            if (url.contains("?id=")) {
                if (callback != null) {
                    callback.onCompleted();
                }
            }
        } else if (type != null && type.equals(PaymentType.MANDIRI_ECASH)) {
            if (url.contains("notify?id=")) {
                if (callback != null) {
                    callback.onCompleted();
                }
            }
        } else if (type != null && type.equals(PaymentType.BRI_EPAY)) {
            if (url.contains("briPayment?tid=")) {
                if (callback != null) {
                    callback.onCompleted();
                }
            }
        } else if (type != null && type.equals(PaymentType.CIMB_CLICKS)) {
            if (url.contains("cimb-clicks/response")) {
                callback.onCompleted();
            }
        }
    }
}
