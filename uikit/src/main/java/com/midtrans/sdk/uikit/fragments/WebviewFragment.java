package com.midtrans.sdk.uikit.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

public class WebviewFragment extends Fragment {

    public static final String TYPE_CREDIT_CARD = "credit";
    public static final String TYPE_BCA_KLIKPAY = "bca_klikpay";
    public static final String TYPE_MANDIRI_ECASH = "mandiri_ecash";
    public static final String TYPE_CIMB_CLICK = "cimb_click";
    public static final String TYPE_EPAY_BRI = "epay_bri";

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
        SdkUIFlowUtil.showProgressDialog(getActivity(), true);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new MidtransWebViewClient());
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

    private class MidtransWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Logger.i("Url:finished:" + url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Logger.i("Url:finished:" + url);
            SdkUIFlowUtil.hideProgressDialog();
            if (url.contains(BuildConfig.CALLBACK_STRING)) {
                Intent returnIntent = new Intent();
                getActivity().setResult(getActivity().RESULT_OK, returnIntent);
                getActivity().finish();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Logger.i("Url:" + url);
            super.onPageStarted(view, url, favicon);
            if (type != null && type.equals(TYPE_BCA_KLIKPAY)) {
                if (url.contains("?id=")) {
                    Intent returnIntent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                    return;
                }
            } else if (type != null && type.equals(TYPE_MANDIRI_ECASH)) {
                if (url.contains("notify?id=")) {
                    Intent returnIntent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                    return;
                }
            } else if (type != null && type.equals(TYPE_EPAY_BRI)) {
                if (url.contains("briPayment?tid=")) {
                    Intent returnIntent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                    return;
                }
            } else if (type != null && type.equals(TYPE_CIMB_CLICK)) {
                if (url.contains("cimb-clicks/response")) {
                    Intent returnIntent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                    return;
                }
            }
            SdkUIFlowUtil.showProgressDialog(getActivity(), false);
        }
    }
}
