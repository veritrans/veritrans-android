package com.midtrans.sdk.uikit.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author rakawm
 */
public class CreditCardTextView extends TextView {

    public CreditCardTextView(Context context) {
        super(context);
        init();
    }

    public CreditCardTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CreditCardTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CreditCardTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "credit_card_font.ttf"));
    }
}
