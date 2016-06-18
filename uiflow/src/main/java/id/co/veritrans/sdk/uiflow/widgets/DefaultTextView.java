package id.co.veritrans.sdk.uiflow.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;

import id.co.veritrans.sdk.coreflow.core.VeritransSDK;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DefaultTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        VeritransSDK paymentSdk = VeritransSDK.getVeritransSDK();
        if (paymentSdk != null) {
            if (VeritransSDK.getDefaultText() != null) {
                Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), VeritransSDK.getDefaultText());
                if (typeface != null) {
                    setTypeface(typeface);
                }
            }
        }
    }
}
