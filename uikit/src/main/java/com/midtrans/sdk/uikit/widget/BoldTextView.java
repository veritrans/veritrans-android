package com.midtrans.sdk.uikit.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.midtrans.sdk.uikit.CustomKitConfig;
import com.midtrans.sdk.uikit.MidtransKit;

import androidx.appcompat.widget.AppCompatTextView;

public class BoldTextView extends AppCompatTextView {

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

    private void init() {
        CustomKitConfig customKitConfig = MidtransKit.getInstance().getCustomKitConfig();
        if (customKitConfig != null) {
            if (customKitConfig.getBoldText() != null && !customKitConfig.getBoldText().isEmpty()) {
                Typeface typeface = null;
                try {
                    typeface = Typeface.createFromAsset(getContext().getAssets(), customKitConfig.getBoldText());
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