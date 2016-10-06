package com.midtrans.sdk.uikit.widgets;

import android.graphics.drawable.GradientDrawable;

/**
 * Created by chetan on 24/11/15.
 */
public class StrokeGradientDrawable {

    private int mStrokeWidth;
    private int mStrokeColor;

    private GradientDrawable mGradientDrawable;
    private float mRadius;
    private int mColor;

    public StrokeGradientDrawable(GradientDrawable drawable) {
        mGradientDrawable = drawable;
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        mGradientDrawable.setStroke(strokeWidth, getStrokeColor());
    }

    public int getStrokeColor() {
        return mStrokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        mStrokeColor = strokeColor;
        mGradientDrawable.setStroke(getStrokeWidth(), strokeColor);
    }

    public void setCornerRadius(float radius) {
        mRadius = radius;
        mGradientDrawable.setCornerRadius(radius);
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
        mGradientDrawable.setColor(color);
    }

    public float getRadius() {
        return mRadius;
    }

    public GradientDrawable getGradientDrawable() {
        return mGradientDrawable;
    }
}
