package com.midtrans.sdk.uikit.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * @author rakawm
 */
public class CreditCardTextView extends AppCompatTextView {

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

    private void init() {
        setTypeface(Typeface.createFromAsset(getContext().getAssets(), "credit_card_font.ttf"));
    }
}
