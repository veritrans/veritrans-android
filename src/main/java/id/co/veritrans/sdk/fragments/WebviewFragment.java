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
    /*private Button sucBt;
    private Button unsucBt;*/

    public WebviewFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        webView = (WebView) view.findViewById(R.id.webview);
       /* sucBt = (Button)view.findViewById(R.id.btn_success);
        unsucBt = (Button)view.findViewById(R.id.btn_unsuccess);
        sucBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trnsSuc(v);
            }
        });
        unsucBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trnsUnsuc(v);
            }
        });*/
        initwebview();
        webView.loadUrl(webUrl);
        return view;
    }

    private void initwebview() {
        SdkUtil.showProgressDialog(getActivity(), true);

        webView.getSettings().setJavaScriptEnabled(true);
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
            /*if (Uri.parse(url).getHost().equals("URL_NAME")) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);*/

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
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Logger.i("Url:" + url);
            super.onPageStarted(view, url, favicon);
            SdkUtil.showProgressDialog(getActivity(), false);
        }
    }

   /* public void trnsSuc(View view){
        PaymentTransactionStatusFragment paymentTransactionStatusFragment =
        PaymentTransactionStatusFragment.newInstance(true);
        ((CreditDebitCardFlowActivity) getActivity()).replaceFragment
        (paymentTransactionStatusFragment,true,false);
    }
    public void trnsUnsuc(View view){
        PaymentTransactionStatusFragment paymentTransactionStatusFragment =
        PaymentTransactionStatusFragment.newInstance(false);
        ((CreditDebitCardFlowActivity) getActivity()).replaceFragment
        (paymentTransactionStatusFragment,true,false);
    }*/
}
