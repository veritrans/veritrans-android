package com.midtrans.sdk.uikit.abstracts;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.themes.BaseColorTheme;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 7/20/17.
 */

public class BaseActivity extends AppCompatActivity {

    private int primaryColor = 0;
    private int primaryDarkColor = 0;
    private int secondaryColor = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initThemeProperties();
    }

    private void initThemeProperties() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null) {
            BaseColorTheme baseColorTheme = MidtransSDK.getInstance().getColorTheme();
            if (baseColorTheme != null) {
                this.primaryColor = baseColorTheme.getPrimaryColor();
                this.primaryDarkColor = baseColorTheme.getPrimaryDarkColor();
                this.secondaryColor = baseColorTheme.getSecondaryColor();
            }
        }
    }

    public void setBackgroundTintList(AppCompatEditText editText) {
        if (secondaryColor != 0) {
            editText.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{secondaryColor}));

        }
    }

    public void setSecondaryBackgroundColor(View view) {
        if (secondaryColor != 0 && view != null) {
            view.setBackgroundColor(secondaryColor);
        }
    }

    protected void setPrimaryBackgroundColor(View view) {
        if (primaryColor != 0 && view != null) {
            view.setBackgroundColor(primaryColor);
        }
    }

    public void setCheckboxStateColor(AppCompatCheckBox checkBox) {
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

    public void setTextColor(View view) {
        if (primaryDarkColor != 0 && view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(primaryDarkColor);
            } else if (view instanceof FancyButton) {
                ((FancyButton) view).setTextColor(primaryDarkColor);
            }
        }
    }


    public void setIconColorFilter(FancyButton fancyButton) {
        if (primaryDarkColor != 0) {
            fancyButton.setIconColorFilter(primaryDarkColor);
        }
    }

    public void setBorderColor(FancyButton fancyButton) {
        if (primaryDarkColor != 0) {
            fancyButton.setBorderColor(primaryDarkColor);
        }
    }

    public void setColorFilter(View view) {
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

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overrideBackAnimation();
    }

    protected void overrideBackAnimation() {
        if (MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
            overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
        }
    }

}
