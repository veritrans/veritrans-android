package com.midtrans.sdk.ui.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.midtrans.sdk.ui.MidtransUi;

/**
 * Created by ziahaqi on 2/19/17.
 */

public class SemiBoldTextView extends AppCompatTextView {

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

    private void init() {
        MidtransUi uiSdk = MidtransUi.getInstance();
        if (uiSdk != null && uiSdk.getSemiBoldFontPath() != null) {
            Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), uiSdk.getSemiBoldFontPath());
            if (typeface != null) {
                setTypeface(typeface);
            }
        }
    }
}
