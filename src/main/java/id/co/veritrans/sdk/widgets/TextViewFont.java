package id.co.veritrans.sdk.widgets;

/**
 * Created by shivam on 10/19/15.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.R.styleable;
import id.co.veritrans.sdk.core.VeritransSDK;

/**
 * Created by shivam on 8/2/15.
 */
public class TextViewFont extends TextView {

    private static VeritransSDK baseApplication = null;

    public TextViewFont(final Context context) {
        this(context, null);
    }

    public TextViewFont(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewFont(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        if (baseApplication == null) {
            baseApplication = VeritransSDK.getInstance(context);
        }

        // prevent exception in Android Studio / ADT interface builder
        if (this.isInEditMode()) {
            return;
        }

        final TypedArray array = context.obtainStyledAttributes(attrs, styleable.TextViewFont);
        if (array != null) {
            final String typefaceAssetPath = array.getString(styleable.TextViewFont_font);

            if (typefaceAssetPath != null) {

                if (typefaceAssetPath.equalsIgnoreCase(getResources().getString(R.string
                        .font_roboto_light))) {
                    setTypeface(baseApplication.getTypefaceRobotoLight());
                } else if (typefaceAssetPath.equalsIgnoreCase(getResources().getString(R.string
                        .font_roboto_bold))) {
                    setTypeface(baseApplication.getTypefaceRobotoBold());
                } else {
                    setTypeface(baseApplication.getTypefaceRobotoRegular());
                }

            }
            array.recycle();
        }
    }
}
