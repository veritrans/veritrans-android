package id.co.veritrans.sdk.uiflow.fragments;

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
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import id.co.veritrans.sdk.coreflow.BuildConfig;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.utilities.SdkUIFlowUtil;

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
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new VeritransWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JsInterface(), getString(R.string.veritrans_response));
       // webView.addJavascriptInterface(new WebAppInterface(getActivity()), "Android");
    }

    public void webviewBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    private class VeritransWebViewClient extends WebViewClient {

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



    public class JsInterface {
        /**
         * code is written on merchant server (redirect url)
         * doctype html
         html
         head
         title= title
         script(type='text/javascript').
         function paymentStatus(data) {
         Android.paymentResponse(data);
         }

         body(onload="paymentStatus('" + paymentStatus + "')")
         h1 Success.
         * @param data  JS data
         */
        @JavascriptInterface
        public void paymentResponse(String data) {
            Logger.i("paymentStatus:"+data);
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.payment_response), data);
            getActivity().setResult(getActivity().RESULT_OK, intent);
            getActivity().finish();
        }

    }
}
