package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ziahaqi on 7/19/16.
 */
public class ExpiryModel {
    @SerializedName("start_time")
    private String startTime;
    @SerializedName("unit")
    private String unit;
    @SerializedName("duration")
    private int duration;

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
