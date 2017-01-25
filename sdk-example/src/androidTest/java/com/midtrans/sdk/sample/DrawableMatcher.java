package com.midtrans.sdk.sample;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created by rakawm on 1/20/17.
 */

public class DrawableMatcher extends TypeSafeMatcher<View> {

    private final int expectedId;

    public DrawableMatcher(int expectedId) {
        super(View.class);
        this.expectedId = expectedId;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    @Override
    protected boolean matchesSafely(View target) {
        if (!(target instanceof ImageView)) {
            return false;
        }
        ImageView imageView = (ImageView) target;
        if (expectedId < 0) {
            return imageView.getDrawable() == null;
        }
        Resources resources = target.getContext().getResources();
        Drawable expectedDrawable = resources.getDrawable(expectedId);
        if (expectedDrawable == null) {
            return false;
        }
        BitmapDrawable bmd = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bmd.getBitmap();
        BitmapDrawable expected = (BitmapDrawable) expectedDrawable;
        Bitmap otherBitmap = expected.getBitmap();
        return bitmap.sameAs(otherBitmap);

    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with drawable from resource id: ");
        description.appendValue(expectedId);
        if (expectedId != 0) {
            description.appendText("[");
            description.appendText(String.valueOf(expectedId));
            description.appendText("]");
        }
    }
}
