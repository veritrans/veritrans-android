package id.co.veritrans.sdk.fragments;

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

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.Constants;
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

    private void initwebview() {
        SdkUtil.showProgressDialog(getActivity(), true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new VeritransWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
    }

    public void webviewBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        }
    }

    private class VeritransWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            SdkUtil.hideProgressDialog();
            if (url.contains(Constants.CALLBACK_STRING)) {
                Intent returnIntent = new Intent();
                getActivity().setResult(getActivity().RESULT_OK, returnIntent);
                getActivity().finish();
            } else if (url.contains(Constants.CALLBACK_URL)) {
                Intent returnIntent = new Intent();
                getActivity().setResult(getActivity().RESULT_OK, returnIntent);
                getActivity().finish();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Logger.i("Url:" + url);
            super.onPageStarted(view, url, favicon);
            SdkUtil.showProgressDialog(getActivity(), false);
        }
    }
}
