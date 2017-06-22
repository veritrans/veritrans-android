package com.midtrans.sdk.uikit.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;

public class WebviewFragment extends Fragment {

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
        webView.addJavascriptInterface(new CustomJavaScriptInterface(getContext()), "Android");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new MidtransWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
    }


    private void initjS() {

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
            if (url.contains(BuildConfig.CALLBACK_STRING)) {
                Intent returnIntent = new Intent();
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    getActivity().overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
                }
            }

//            if (url.contains(BuildConfig.CALLBACK_STRING)) {
//                Log.d("xids", "callback:");
//                webView.addJavascriptInterface(new CustomJavaScriptInterface(getContext()), "Android");
//                webView.loadUrl("javascript:{" +
//                        "var token_id = '', status_code = '', status_message = '', eci = '';" +
//                        "var input_status_code = document.getElementsByName('status_code')[0];" +
//                        "if(input_status_code) var status_code = input_status_code.value;" +
//                        "var input_token_id = document.getElementsByName('token_id')[0];" +
//                        "if(input_token_id) var token_id = input_token_id.value;" +
//                        "var input_status_message = document.getElementsByClassName('center desc')[0];" +
//                        "if(input_status_message)" +
//                        "{ var content = input_status_message.innerHTML;  " +
//                        "if(content) var status_message = content.getElementsByTagName('p')[0];" +
//                        "}" +
//                        "var input_eci = document.getElementsByName('eci')[0];" +
//                        "if(input_eci) var eci = input_eci.value;" +
//                        "Android.getRbaStatus(token_id, status_code, status_message, eci);" +
//                        "}");

//                webView.loadUrl("javascript:{" +
//                        "var str_json = JSON.stringify(f.response);" +
//                        "Android.getJsonResponse(str_json);" +
//                        "};");
//                webView.loadUrl("javascript:Android.getIds(response);");
//            }
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
                    if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                            && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                        getActivity().overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
                    }
                    return;
                }
            } else if (type != null && type.equals(TYPE_MANDIRI_ECASH)) {
                if (url.contains("notify?id=")) {
                    Intent returnIntent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                    if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                            && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                        getActivity().overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
                    }
                    return;
                }
            } else if (type != null && type.equals(TYPE_EPAY_BRI)) {
                if (url.contains("briPayment?tid=")) {
                    Intent returnIntent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                    if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                            && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                        getActivity().overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
                    }
                    return;
                }
            } else if (type != null && type.equals(TYPE_CIMB_CLICK)) {
                if (url.contains("cimb-clicks/response")) {
                    Intent returnIntent = new Intent();
                    getActivity().setResult(Activity.RESULT_OK, returnIntent);
                    getActivity().finish();
                    if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                            && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                        getActivity().overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
                    }
                    return;
                }
            }
        }
    }

    class CustomJavaScriptInterface {
        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        CustomJavaScriptInterface(Context c) {
            mContext = c;
        }


        /**
         * retrieve the ids
         */
        @JavascriptInterface
        public void getRbaStatus(String tokenId, String statusCode, String statusMessage, String eci) {
            //Do somethings with the Ids
            Log.d("xids", "data:" + tokenId + " |" + statusCode + " | " + statusMessage + " | " + eci);
        }

        @JavascriptInterface
        public void getRbaStatus(String tokenId) {
            //Do somethings with the Ids
            Log.d("xids", "data:" + tokenId);
        }

        @JavascriptInterface
        public void getJsonResponse(String response) {
            Log.d("xids", "response:" + response);

        }
    }
}
