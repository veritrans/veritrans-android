package com.midtrans.sdk.sample;

import android.view.View;

import org.hamcrest.Matcher;

/**
 * Created by rakawm on 1/20/17.
 */

public class EspressoTestsMatchers {

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(-1);
    }
}
