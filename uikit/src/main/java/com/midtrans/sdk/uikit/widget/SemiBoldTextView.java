package com.midtrans.sdk.uikit.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.midtrans.sdk.uikit.MidtransKitConfig;
import com.midtrans.sdk.uikit.MidtransKit;

import androidx.appcompat.widget.AppCompatTextView;

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
        MidtransKitConfig midtransKitConfig = MidtransKit.getInstance().getMidtransKitConfig();
        if (midtransKitConfig != null) {
            if (midtransKitConfig.getSemiBoldText() != null && !midtransKitConfig.getSemiBoldText().isEmpty()) {
                Typeface typeface = null;
                try {
                    typeface = Typeface.createFromAsset(getContext().getAssets(), midtransKitConfig.getSemiBoldText());
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
