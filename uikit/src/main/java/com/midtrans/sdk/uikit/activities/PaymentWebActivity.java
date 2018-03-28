package com.midtrans.sdk.uikit.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.fragments.WebviewFragment;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Deprecated, please use {@link com.midtrans.sdk.uikit.views.webview.WebViewPaymentActivity} instead
 */
@Deprecated
public class PaymentWebActivity extends BaseActivity {
    private static final String TAG = "PaymentWebActivity";
    private Toolbar toolbar;
    private String webUrl;
    private String type = "";
    private SmsVerifyCatcher smsVerifyCatcher;
    private WebviewFragment webviewFragment;

    private static void showCancelConfirmationDialog(final Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            try {
                AlertDialog dialog = new AlertDialog.Builder(activity, R.style.AlertDialogCustom)
                        .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!activity.isFinishing()) {
                                    dialog.dismiss();
                                    Intent returnIntent = new Intent();
                                    activity.setResult(RESULT_CANCELED, returnIntent);
                                    activity.finish();
                                    MidtransSDK midtransSDK = MidtransSDK.getInstance();
                                    if (midtransSDK != null && midtransSDK.getUIKitCustomSetting() != null
                                            && midtransSDK.getUIKitCustomSetting().isEnabledAnimation()) {
                                        activity.overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
                                    }
                                }
                            }
                        })
                        .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!activity.isFinishing()) {
                                    dialog.dismiss();
                                }
                            }
                        })
                        .setTitle(R.string.cancel_transaction)
                        .setMessage(R.string.cancel_transaction_message)
                        .create();
                dialog.show();
            } catch (Exception e) {
                Logger.e(TAG, "showDialog:" + e.getMessage());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_web);

        saveCurrentFragment = true;
        webUrl = getIntent().getStringExtra(Constants.WEBURL);
        if (getIntent().getStringExtra(Constants.TYPE) != null && !getIntent().getStringExtra(Constants.TYPE).equals("")) {
            type = getIntent().getStringExtra(Constants.TYPE);
        }
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        initializeTheme();

//        toolbar.setTitle(R.string.processing_payment);

        setSupportActionBar(toolbar);
        prepareToolbar();
        if (!TextUtils.isEmpty(type)) {
            webviewFragment = WebviewFragment.newInstance(webUrl, type);
        } else {
            webviewFragment = WebviewFragment.newInstance(webUrl);
        }
        replaceFragment(webviewFragment, R.id.webview_container, true, false);

        if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase(WebviewFragment.TYPE_CREDIT_CARD)) {
            initSmsCatcher();
        }
    }

    private void prepareToolbar() {

        try {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_back);

            MidtransSDK midtransSDK = MidtransSDK.getInstance();

            if (midtransSDK != null && midtransSDK.getColorTheme() != null && midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                drawable.setColorFilter(
                        midtransSDK.getColorTheme().getPrimaryDarkColor(),
                        PorterDuff.Mode.SRC_ATOP);
            }
            toolbar.setNavigationIcon(drawable);
        } catch (Exception e) {
            Log.d(TAG, "render toolbar:" + e.getMessage());
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelConfirmationDialog(PaymentWebActivity.this);
            }
        });

        //set title
        ((SemiBoldTextView) findViewById(R.id.text_page_title)).setText(R.string.processing_payment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_close) {
            showCancelConfirmationDialog(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        showCancelConfirmationDialog(this);
    }

    private void initSmsCatcher() {
        try {
            MidtransSDK midtransSDK = MidtransSDK.getInstance();
            if (midtransSDK != null && midtransSDK.getUIKitCustomSetting() != null
                    && midtransSDK.getUIKitCustomSetting().isEnableAutoReadSms()) {
                smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
                    @Override
                    public void onSmsCatch(String message) {
                        Pattern otpPattern = Pattern.compile("[0-9]{6}");
                        if (otpPattern != null) {
                            Matcher matcher = otpPattern.matcher(message);
                            matcher.find();
                            String otp = matcher.group(0);
                            if (!TextUtils.isEmpty(otp) && webviewFragment != null) {
                                webviewFragment.setOtp(matcher.group(0));
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            Logger.e(TAG, "initSmsCatcher:" + e.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isFinishing() && smsVerifyCatcher != null) {
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
        if (!isFinishing()) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (smsVerifyCatcher != null) {
                smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
