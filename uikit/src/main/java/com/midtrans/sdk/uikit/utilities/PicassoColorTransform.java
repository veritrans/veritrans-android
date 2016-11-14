package com.midtrans.sdk.uikit.utilities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;

import com.squareup.picasso.Transformation;

/**
 * Created by ziahaqi on 11/11/16.
 */

public class PicassoColorTransform implements Transformation{
    private int mColor;

    public PicassoColorTransform(int color) {
        mColor = color;
    }

    @Override public Bitmap transform(Bitmap source) {

        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColorFilter(new PorterDuffColorFilter(mColor, PorterDuff.Mode.SRC_ATOP));
        canvas.drawBitmap(source, 0, 0, paint);
        source.recycle();

        return bitmap;
    }

    @Override public String key() {
        return "ColorFilterTransformation(color=" + mColor + ")";
    }
}
