package com.midtrans.sdk.uikit.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.midtrans.sdk.corekit.core.MidtransSDK;

/**
 * @author rakawm
 */
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
        MidtransSDK paymentSdk = MidtransSDK.getInstance();
        if (paymentSdk != null) {
            if (paymentSdk.getBoldText() != null && !paymentSdk.getBoldText().isEmpty()) {
                Typeface typeface = null;
                try {
                    typeface = Typeface.createFromAsset(getContext().getAssets(), paymentSdk.getBoldText());
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
