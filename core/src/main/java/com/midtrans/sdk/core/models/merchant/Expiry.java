package com.midtrans.sdk.core.models.merchant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rakawm on 2/8/17.
 */

public class Expiry {
    public static final String UNIT_HOUR = "hours";
    public static final String UNIT_MINUTE = "minutes";
    public static final String UNIT_DAY = "days";

    public final String startTime;
    public final String unit;
    public final int duration;

    public Expiry(String startTime, @TimeUnitDef String unit, int duration) {
        this.startTime = startTime;
        this.unit = unit;
        this.duration = duration;
    }

    @StringDef({UNIT_HOUR, UNIT_DAY, UNIT_MINUTE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TimeUnitDef {
    }
}
