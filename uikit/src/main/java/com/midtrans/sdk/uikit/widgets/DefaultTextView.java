package com.midtrans.sdk.uikit.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.midtrans.sdk.corekit.core.MidtransSDK;

/**
 * @author rakawm
 */
public class DefaultTextView extends TextViewWithImages {

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

    private void init() {
        MidtransSDK paymentSdk = MidtransSDK.getInstance();
        if (paymentSdk != null) {
            if (paymentSdk.getDefaultText() != null && !paymentSdk.getDefaultText().isEmpty()) {
                Typeface typeface = null;
                try {
                    typeface = Typeface.createFromAsset(getContext().getAssets(), paymentSdk.getDefaultText());
                } catch (RuntimeException exception) {
                    exception.printStackTrace();
                }
                if (typeface != null) {
                    setTypeface(typeface);
                }
            }
        }
    }
}
