package com.midtrans.sdk.uikit.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.WebviewFragment;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentWebActivity extends BaseActivity {
    private static final String TAG = "PaymentWebActivity";
    private Toolbar toolbar;
    private String webUrl;
    private String type = "";
    private SmsVerifyCatcher smsVerifyCatcher;
    private WebviewFragment webviewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web);

        saveCurrentFragment = true;
        webUrl = getIntent().getStringExtra(Constants.WEBURL);
        if (getIntent().getStringExtra(Constants.TYPE) != null && !getIntent().getStringExtra(Constants.TYPE).equals("")) {
            type = getIntent().getStringExtra(Constants.TYPE);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initializeTheme();

        toolbar.setTitle(R.string.processing_payment);

        setSupportActionBar(toolbar);
        prepareToolbar();
        if (!type.equals("")) {
            webviewFragment = WebviewFragment.newInstance(webUrl, type);
        } else {
            webviewFragment = WebviewFragment.newInstance(webUrl);
        }
        replaceFragment(webviewFragment, R.id.webview_container, true, false);

        if (type != null && type.equalsIgnoreCase(WebviewFragment.TYPE_CREDIT_CARD)) {
            initSmsCatcher();
        }
    }

    private void prepareToolbar() {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_back);

        try {
            MidtransSDK midtransSDK = MidtransSDK.getInstance();

            if (midtransSDK != null && midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                drawable.setColorFilter(
                        midtransSDK.getColorTheme().getPrimaryDarkColor(),
                        PorterDuff.Mode.SRC_ATOP);
            }
        } catch (Exception e) {
            Log.d(TAG, "render toolbar:" + e.getMessage());
        }

        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showCancelConfirmationDialog();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_close) {
            showCancelConfirmationDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                        if (MidtransSDK.getInstance().getUIKitCustomSetting()!=null
                                && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                            overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
                        }
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
    }

    private void initSmsCatcher() {
        if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                && MidtransSDK.getInstance().getUIKitCustomSetting().isEnableAutoReadSms()) {
            smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
                @Override
                public void onSmsCatch(String message) {
                    Pattern otpPattern = Pattern.compile("[0-9]{6}");
                    Matcher matcher = otpPattern.matcher(message);
                    if (matcher.find()) {
                        webviewFragment.setOtp(matcher.group(0));
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (smsVerifyCatcher != null) {
            smsVerifyCatcher.onStart();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (smsVerifyCatcher != null) {
            smsVerifyCatcher.onStop();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (smsVerifyCatcher != null) {
            smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
