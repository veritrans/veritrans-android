package com.midtrans.sdk.core.models;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rakawm on 2/8/17.
 */

public class Channel {
    public static final String DRAGON = "dragon";
    public static final String MIGS = "migs";

    @StringDef({DRAGON, MIGS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ChannelDef {
    }
}
