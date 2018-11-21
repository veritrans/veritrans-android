package com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExpiryModel implements Serializable {
    public static final String UNIT_HOUR = "hours";
    public static final String UNIT_MINUTE = "minutes";
    public static final String UNIT_DAY = "days";

    @SerializedName("start_time")
    private String startTime;
    @SerializedName("unit")
    private String unit;
    @SerializedName("duration")
    private int duration;

    public ExpiryModel() {
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}