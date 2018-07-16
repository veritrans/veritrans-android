package com.midtrans.sdk.uikit.abstracts;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.BaseColorTheme;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.lang.reflect.Field;

/**
 * Created by ziahaqi on 7/20/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    private static final String TAG = BaseActivity.class.getSimpleName();


    private int primaryColor = 0;
    private int primaryDarkColor = 0;
    private int secondaryColor = 0;

    private LinearLayout containerProgress;
    private DefaultTextView textProgressMessage;
    private ImageView imageProgressLogo;


    private boolean activityRunning = false;
    protected boolean backgroundProcess;
    private volatile MidtransSDK midtransSdk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkSdkInstance();
        initThemeProperties();
    }

    private void checkSdkInstance() {
        if (getMidtransSdk() == null) {
            onNullInstanceSdk();
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        initProgressContainer();
        try {
            bindViews();
            initTheme();
            initBadgeLayout();
        } catch (RuntimeException e) {
            Logger.e(TAG, "initTheme():" + e.getMessage());
        }

    }

    private void initProgressContainer() {
        textProgressMessage = (DefaultTextView) findViewById(R.id.progress_bar_message);
        containerProgress = (LinearLayout) findViewById(R.id.progress_container);
        imageProgressLogo = (ImageView) findViewById(R.id.progress_bar_image);

        if (imageProgressLogo != null) {
            Ion.with(imageProgressLogo).load(SdkUIFlowUtil.getImagePath(this) + R.drawable.midtrans_loader);
        }
    }

    private void initBadgeLayout() {
        if (BuildConfig.FLAVOR.equalsIgnoreCase(UiKitConstants.ENVIRONMENT_DEVELOPMENT)) {
            ImageView badgeView = (ImageView) findViewById(R.id.image_sandbox_badge);
            if (badgeView != null) {
                badgeView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initThemeProperties() {
        BaseColorTheme baseColorTheme = getMidtransSdk().getColorTheme();
        if (baseColorTheme != null) {
            this.primaryColor = baseColorTheme.getPrimaryColor();
            this.primaryDarkColor = baseColorTheme.getPrimaryDarkColor();
            this.secondaryColor = baseColorTheme.getSecondaryColor();
        }
    }

    public void setBackgroundTintList(AppCompatEditText editText) throws RuntimeException {
        if (secondaryColor != 0) {
            editText.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{secondaryColor}));
        }
    }

    public void setSecondaryBackgroundColor(View view) throws RuntimeException {
        if (secondaryColor != 0 && view != null) {
            view.setBackgroundColor(secondaryColor);
        }
    }

    protected void setPrimaryBackgroundColor(View view) throws RuntimeException {
        if (primaryColor != 0 && view != null) {
            view.setBackgroundColor(primaryColor);
        }
    }

    public void setCheckboxStateColor(AppCompatCheckBox checkBox) throws RuntimeException {
        if (secondaryColor != 0) {
            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_checked},
                    new int[]{android.R.attr.state_checked},
            };

            int[] trackColors = new int[]{
                    Color.GRAY,
                    secondaryColor
            };
            checkBox.setSupportButtonTintList(new ColorStateList(states, trackColors));
        }
    }

    public void setTextColor(View view) throws RuntimeException {
        if (primaryDarkColor != 0 && view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(primaryDarkColor);
            } else if (view instanceof FancyButton) {
                ((FancyButton) view).setTextColor(primaryDarkColor);
            }
        }
    }

    public void setTextInputlayoutFilter(TextInputLayout textInputLayout) throws RuntimeException {
        if (secondaryColor != 0) {
            try {
                Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                fDefaultTextColor.setAccessible(true);
                fDefaultTextColor.set(textInputLayout, new ColorStateList(new int[][]{{0}}, new int[]{secondaryColor}));

                Field fFocusedTextColor = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                fFocusedTextColor.setAccessible(true);
                fFocusedTextColor.set(textInputLayout, new ColorStateList(new int[][]{{0}}, new int[]{secondaryColor}));

            } catch (RuntimeException | NoSuchFieldException | IllegalAccessException e) {
                Logger.e(TAG, "tilfilter():" + e.getMessage());
            }
        }
    }

    public void setIconColorFilter(FancyButton fancyButton) throws RuntimeException {
        if (primaryDarkColor != 0) {
            fancyButton.setIconColorFilter(primaryDarkColor);
        }
    }

    public void setBorderColor(FancyButton fancyButton) {
        if (primaryDarkColor != 0) {
            fancyButton.setBorderColor(primaryDarkColor);
        }
    }

    public void setColorFilter(View view) throws RuntimeException {
        if (primaryDarkColor != 0 && view != null) {
            if (view instanceof ImageButton) {
                ((ImageButton) view).setColorFilter(primaryDarkColor, PorterDuff.Mode.SRC_ATOP);
            } else {
                view.getBackground().setColorFilter(primaryDarkColor, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public int getPrimaryDarkColor() {
        return primaryDarkColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }


    protected void overrideBackAnimation() {
        UIKitCustomSetting setting = getMidtransSdk().getUIKitCustomSetting();
        if (setting != null && setting.isEnabledAnimation()) {
            overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
        }
    }

    public boolean isActivityRunning() {
        return activityRunning;
    }

    protected void showProgressLayout(String message) {
        setBackgroundProcess(true);

        if (!TextUtils.isEmpty(message)) {
            textProgressMessage.setText(message);
        }

        if (containerProgress != null) {
            containerProgress.setVisibility(View.VISIBLE);
            containerProgress.setClickable(true);
        }
    }

    protected void showProgressLayout() {
        showProgressLayout("");
    }

    protected void hideProgressLayout() {
        setBackgroundProcess(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (containerProgress != null) {
                    containerProgress.setVisibility(View.GONE);
                }

                if (textProgressMessage != null) {
                    textProgressMessage.setText(R.string.loading);
                }
            }
        }, 500);

    }

    private void setBackgroundProcess(boolean backgroundProcess) {
        this.backgroundProcess = backgroundProcess;
    }

    public boolean isBackgroundProcess() {
        return backgroundProcess;
    }


    public abstract void bindViews();

    public abstract void initTheme();

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (getMidtransSdk().getUIKitCustomSetting().isEnabledAnimation()) {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (getMidtransSdk().getUIKitCustomSetting().isEnabledAnimation()) {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    @Override
    public void onBackPressed() {
        if (isBackgroundProcess()) {
            return;
        }
        super.onBackPressed();
        overrideBackAnimation();
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityRunning = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityRunning = true;
    }

    @Override
    protected void onPause() {
        activityRunning = false;
        super.onPause();
    }

    @Override
    protected void onStop() {
        activityRunning = false;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        activityRunning = false;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == UiKitConstants.RESULT_SDK_NOT_AVAILABLE) {
            setResult(resultCode);
            finish();
        }
    }

    @Override
    public void onNullInstanceSdk() {
        setResult(UiKitConstants.RESULT_SDK_NOT_AVAILABLE);
        finish();
    }

    public MidtransSDK getMidtransSdk() {
        if (midtransSdk == null) {
            midtransSdk = MidtransSDK.getInstance();
            if (midtransSdk == null || midtransSdk.isSdkNotAvailable()) {
                onNullInstanceSdk();
            }
        }

        return midtransSdk;
    }
}
