package com.midtrans.sdk.uikit.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.MidtransSDK;

/**
 * @author rakawm
 */
public class BoldTextView extends TextView {

    public BoldTextView(Context context) {
        super(context);
        init();
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BoldTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        MidtransSDK paymentSdk = MidtransSDK.getInstance();
        if (paymentSdk != null) {
            if (paymentSdk.getDefaultText() != null) {
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), paymentSdk.getDefaultText());
                if (typeface != null) {
                    setTypeface(typeface);
                }
            }
        }
    }
}
