package com.midtrans.sdk.uikit.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.midtrans.sdk.uikit.CustomKitConfig;
import com.midtrans.sdk.uikit.MidtransKit;

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
        CustomKitConfig customKitConfig = MidtransKit.getInstance().getCustomKitConfig();
        if (customKitConfig != null) {
            if (customKitConfig.getDefaultText() != null && !customKitConfig.getDefaultText().isEmpty()) {
                Typeface typeface = null;
                try {
                    typeface = Typeface.createFromAsset(getContext().getAssets(), customKitConfig.getDefaultText());
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