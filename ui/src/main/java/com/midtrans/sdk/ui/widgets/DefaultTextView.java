package com.midtrans.sdk.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.midtrans.sdk.ui.MidtransUiKit;

/**
 * Created by ziahaqi on 2/19/17.
 */

public class DefaultTextView extends TextView {

    public DefaultTextView(Context context) {
        super(context);
        init();
    }

    public DefaultTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DefaultTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DefaultTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        MidtransUiKit uiSdk = MidtransUiKit.getInstance();
        if (uiSdk != null && uiSdk.getFontDefault() != null) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), uiSdk.getFontDefault());
            if (typeface != null) {
                setTypeface(typeface);
            }
        }
    }
}
