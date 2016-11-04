package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 11/3/16.
 */

public class Expiry {
    /**
     * sample "2017-01-11 11:52:00 +0700"
     */
    @SerializedName("start_time")
    private String startTime;

    @SerializedName("unit")
    private String unit;
    @SerializedName("duration")
    private int duration;

    public Expiry(String startTime, String unit, int duration) {
        this.startTime = startTime;
        this.unit = unit;
        this.duration = duration;
    }
}
