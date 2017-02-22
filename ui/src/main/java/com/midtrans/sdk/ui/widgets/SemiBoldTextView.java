package com.midtrans.sdk.ui.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.midtrans.sdk.ui.MidtransUi;

/**
 * Created by ziahaqi on 2/19/17.
 */

public class SemiBoldTextView extends TextView {

    public SemiBoldTextView(Context context) {
        super(context);
        init();
    }

    public SemiBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SemiBoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SemiBoldTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        MidtransUi uiSdk = MidtransUi.getInstance();
        if (uiSdk != null && uiSdk.getFontSemiBold() != null) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), uiSdk.getFontSemiBold());
            if (typeface != null) {
                setTypeface(typeface);
            }
        }
    }
}
