package com.midtrans.sdk.ui.views.webpayment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseTitleActivity;
import com.midtrans.sdk.ui.constants.Constants;
import com.midtrans.sdk.ui.utils.Logger;
import com.midtrans.sdk.ui.utils.UiUtils;


public class PaymentWebActivity extends BaseTitleActivity implements MidtransWebViewClient.WebViewClientCallback {
    private WebView webView;
    private String webUrl;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web);
        initDefaultProperties();
        initViews();
        initWebView();
        initValues();
    }

    private void initViews() {
        webView = (WebView) findViewById(R.id.webview);
    }

    private void initDefaultProperties() {
        webUrl = getIntent().getStringExtra(Constants.WEB_VIEW_URL);
        type = getIntent().getStringExtra(Constants.WEB_VIEW_PARAM_TYPE);
        Logger.d(TAG, "weburl:" + webUrl);
        Logger.d(TAG, "type:" + type);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        UiUtils.showProgressDialog(this, true);
        webView.getSettings().setAllowFileAccess(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new MidtransWebViewClient(this, type));
        webView.setWebChromeClient(new WebChromeClient());
    }

    private void initValues() {
        webView.loadUrl(webUrl);
    }

    @Override
    public void onBackPressed() {
        showCancelConfirmationDialog();
    }

    private void showCancelConfirmationDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this, R.style.AlertDialogCustom)
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent returnIntent = new Intent();
                        setResult(RESULT_CANCELED, returnIntent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle(R.string.cancel_transaction)
                .setMessage(R.string.cancel_transaction_message)
                .create();
        dialog.show();

        changeDialogButtonColor(dialog);
    }

    private void changeDialogButtonColor(AlertDialog alertDialog) {
        if (alertDialog.isShowing()
                && MidtransUi.getInstance() != null
                && MidtransUi.getInstance().getColorTheme() != null
                && MidtransUi.getInstance().getColorTheme().getPrimaryDarkColor() != 0) {
            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            positiveButton.setTextColor(MidtransUi.getInstance().getColorTheme().getPrimaryDarkColor());
            negativeButton.setTextColor(MidtransUi.getInstance().getColorTheme().getPrimaryDarkColor());
        }
    }


    @Override
    public void onCompleted() {
        setResult(RESULT_OK);
        finish();
    }
}
