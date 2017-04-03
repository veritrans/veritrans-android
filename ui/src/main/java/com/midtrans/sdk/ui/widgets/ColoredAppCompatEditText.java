package com.midtrans.sdk.ui.widgets;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.themes.BaseColorTheme;
import com.midtrans.sdk.ui.utils.Logger;

/**
 * Created by ziahaqi on 3/5/17.
 */

public class ColoredAppCompatEditText extends AppCompatEditText {
    public ColoredAppCompatEditText(Context context) {
        super(context);
        init();
    }

    public ColoredAppCompatEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColoredAppCompatEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        MidtransUi uiSdk = MidtransUi.getInstance();
        if (uiSdk != null && uiSdk.getColorTheme()!= null) {
            BaseColorTheme colorTheme = uiSdk.getColorTheme();
            try {
                getBackground().setColorFilter(colorTheme.getSecondaryColor(), PorterDuff.Mode.SRC_ATOP);
                setHintTextColor(colorTheme.getSecondaryColor());
            } catch (RuntimeException exception) {
                Logger.e(this.getClass().getSimpleName(), exception.getMessage());
            }
        }

        if (uiSdk != null && uiSdk.getDefaultFontPath() != null) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), uiSdk.getDefaultFontPath());
            if (typeface != null) {
                setTypeface(typeface);
            }
        }
    }
}
