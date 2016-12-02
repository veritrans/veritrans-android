package id.co.veritrans.sdk.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import id.co.veritrans.sdk.BuildConfig;
import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;

public class WebviewFragment extends Fragment {

    private static final String URL_PARAM = "url_param";
    public WebView webView;
    private String webUrl;

    public WebviewFragment() {
    }

    public static WebviewFragment newInstance(String url) {
        WebviewFragment fragment = new WebviewFragment();
        Bundle args = new Bundle();
        args.putString(URL_PARAM, url);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            webUrl = getArguments().getString(URL_PARAM);
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
        SdkUtil.showProgressDialog(getActivity(), true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new VeritransWebViewClient(getActivity()));
        webView.setWebChromeClient(new WebChromeClient());
        webView.addJavascriptInterface(new JsInterface(getActivity()), getString(R.string.veritrans_response));
        // webView.addJavascriptInterface(new WebAppInterface(getActivity()), "Android");
    }

    public void webviewBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    private static class VeritransWebViewClient extends WebViewClient {
        private FragmentActivity activity;

        public VeritransWebViewClient(FragmentActivity activity) {
            this.activity = activity;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            SdkUtil.hideProgressDialog();
            if (this.activity != null) {
                if (url != null && url.contains(BuildConfig.CALLBACK_STRING)) {
                    Intent returnIntent = new Intent();
                    activity.setResult(activity.RESULT_OK, returnIntent);
                    activity.finish();
                }
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Logger.i("Url:" + url);
            super.onPageStarted(view, url, favicon);
            if (activity != null) {
                SdkUtil.showProgressDialog(activity, false);
            }
        }
    }


    public static class JsInterface {
        private FragmentActivity activity;

        public JsInterface(FragmentActivity activity) {
            this.activity = activity;
        }

        /**
         * code is written on merchant server (redirect url)
         * doctype html
         * html
         * head
         * title= title
         * script(type='text/javascript').
         * function paymentStatus(data) {
         * Android.paymentResponse(data);
         * }
         * <p>
         * body(onload="paymentStatus('" + paymentStatus + "')")
         * h1 Success.
         *
         * @param data JS data
         */
        @JavascriptInterface
        public void paymentResponse(String data) {
            Logger.i("paymentStatus:" + data);
            if(activity != null){
                Intent intent = new Intent();
                intent.putExtra(activity.getString(R.string.payment_response), data);
                activity.setResult(activity.RESULT_OK, intent);
                activity.finish();
            }
        }

    }

    /*public class WebAppInterface {
        Context mContext;

        *//** Instantiate the interface and set the context *//*
        WebAppInterface(Context c) {
            mContext = c;
        }

        *//** Show a toast from the web page *//*
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
    }*/

}
