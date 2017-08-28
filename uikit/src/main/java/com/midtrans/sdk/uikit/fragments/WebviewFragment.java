package com.midtrans.sdk.uikit.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.PaymentWebActivity;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

public class WebviewFragment extends Fragment {

    private static final String TAG = WebviewFragment.class.getSimpleName();

    public static final String TYPE_CREDIT_CARD = "credit";
    public static final String TYPE_BCA_KLIKPAY = "bca_klikpay";
    public static final String TYPE_MANDIRI_ECASH = "mandiri_ecash";
    public static final String TYPE_CIMB_CLICK = "cimb_click";
    public static final String TYPE_EPAY_BRI = "epay_bri";
    public static final String RBA = "rba";

    private static final String URL_PARAM = "url_param";
    private static final String TYPE_PARAM = "type_param";

    public WebView webView;
    private String webUrl;
    private String type = "";

    public WebviewFragment() {
    }

    public static WebviewFragment newInstance(String url) {
        WebviewFragment fragment = new WebviewFragment();
        Bundle args = new Bundle();
        args.putString(URL_PARAM, url);

        fragment.setArguments(args);
        return fragment;
    }

    public static WebviewFragment newInstance(String url, String type) {
        WebviewFragment fragment = new WebviewFragment();
        Bundle args = new Bundle();
        args.putString(URL_PARAM, url);
        args.putString(TYPE_PARAM, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            webUrl = getArguments().getString(URL_PARAM);
            type = getArguments().getString(TYPE_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        webView = (WebView) view.findViewById(R.id.webview);
        initwebview();
        webView.loadUrl(webUrl);
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressLint("AddJavascriptInterface")
    private void initwebview() {
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new MidtransWebViewClient(((PaymentWebActivity) getActivity()), type));
        webView.setWebChromeClient(new WebChromeClient());
    }


    @Override
    public void onDetach() {
        super.onDetach();
        if (webView != null) {
            webView.removeAllViews();
            webView.destroyDrawingCache();
            webView.clearHistory();
            webView.destroy();
        }
    }

    /**
     * Function to fill 3DS otp field by loading Javascript code to WebView.
     *
     * @param otp otp code.
     */
    public void setOtp(final String otp) {
        if (BuildConfig.FLAVOR.equals("production")) {
            webView.loadUrl("javascript: {" +
                    "var input = document.getElementById('PaRes');" +
                    "if(input) input.value = '" + otp + "';" +
                    "input = document.getElementById('otp');" +
                    "if(input) input.value = '" + otp + "';" +
                    "setTimeout(function(){" +
                    "var form = document.getElementsByName('vbv_auth_form')[0];" +
                    "if(form) form.submit();" +
                    "}, 1000); " +
                    "};");
        } else {
            webView.loadUrl("javascript: {" +
                    "var input = document.getElementById('PaRes');" +
                    "if(input) input.value = '" + otp + "';" +
                    "input = document.getElementById('otp');" +
                    "if(input) input.value = '" + otp + "';" +
                    "setTimeout(function(){" +
                    "var form = document.getElementsByTagName('FORM')[0];" +
                    "if(form) form.submit();" +
                    "}, 1000); " +
                    "};");
        }
    }

    private static class MidtransWebViewClient extends WebViewClient {

        private final String type;
        private PaymentWebActivity activity;

        private MidtransWebViewClient(PaymentWebActivity activity, String type) {
            this.type = type;
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading()>url:" + url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.d(TAG, "onPageFinished()>url:" + url);
            if (activity != null && !activity.isFinishing()) {
                if (url.contains(UiKitConstants.CALLBACK_PATTERN_3DS) || url.contains(UiKitConstants.CALLBACK_PATTERN_RBA)) {
                    Intent returnIntent = new Intent();
                    activity.setResult(Activity.RESULT_OK, returnIntent);
                    activity.finish();
                    overridePendingTransition();
                }
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "onPageStarted()>url:" + url);
            super.onPageStarted(view, url, favicon);

            if (activity != null && !activity.isFinishing()) {
                if (type != null && type.equals(TYPE_BCA_KLIKPAY)) {
                    if (url.contains("?id=")) {
                        Intent returnIntent = new Intent();
                        activity.setResult(Activity.RESULT_OK, returnIntent);
                        activity.finish();
                        overridePendingTransition();
                        return;
                    }
                } else if (type != null && type.equals(TYPE_MANDIRI_ECASH)) {
                    if (url.contains("notify?id=")) {
                        Intent returnIntent = new Intent();
                        activity.setResult(Activity.RESULT_OK, returnIntent);
                        activity.finish();
                        overridePendingTransition();
                        return;
                    }
                } else if (type != null && type.equals(TYPE_EPAY_BRI)) {
                    if (url.contains("briPayment?tid=")) {
                        Intent returnIntent = new Intent();
                        activity.setResult(Activity.RESULT_OK, returnIntent);
                        activity.finish();
                        overridePendingTransition();
                        return;
                    }
                } else if (type != null && type.equals(TYPE_CIMB_CLICK)) {
                    if (url.contains("cimb-clicks/response")) {
                        Intent returnIntent = new Intent();
                        activity.setResult(Activity.RESULT_OK, returnIntent);
                        activity.finish();
                        overridePendingTransition();
                        return;
                    }
                }
            }
        }

        private void overridePendingTransition() {
            if (activity != null && !activity.isFinishing()) {
                MidtransSDK midtransSDK = MidtransSDK.getInstance();
                if (midtransSDK != null && midtransSDK.getUIKitCustomSetting() != null
                        && midtransSDK.getUIKitCustomSetting().isEnabledAnimation()) {
                    activity.overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);

                }
            }
        }
    }

}
