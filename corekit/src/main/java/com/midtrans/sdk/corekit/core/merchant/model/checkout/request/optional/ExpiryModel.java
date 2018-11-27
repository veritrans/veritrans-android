package com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional;

import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.base.enums.ExpiryModelUnit;

import java.io.Serializable;

public class ExpiryModel implements Serializable {
    private final String UNIT_HOUR = "hours";
    private final String UNIT_MINUTE = "minutes";
    private final String UNIT_DAY = "days";

    @SerializedName("start_time")
    private String startTime;
    @SerializedName("unit")
    private String unit;
    @SerializedName("duration")
    private int duration;

    public ExpiryModel(String startTime,
                       ExpiryModelUnit unit,
                       int duration) {
        this.startTime = startTime;
        this.duration = duration;
        setUnit(unit);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public ExpiryModelUnit getUnit() {
        switch (this.unit) {
            case UNIT_DAY:
                return ExpiryModelUnit.EXPIRY_UNIT_DAY;
            case UNIT_HOUR:
                return ExpiryModelUnit.EXPIRY_UNIT_HOUR;
            case UNIT_MINUTE:
                return ExpiryModelUnit.EXPIRY_UNIT_MINUTE;
        }
        return null;
    }

    public void setUnit(ExpiryModelUnit unit) {
        switch (unit) {
            case EXPIRY_UNIT_HOUR:
                this.unit = UNIT_HOUR;
                break;
            case EXPIRY_UNIT_MINUTE:
                this.unit = UNIT_MINUTE;
                break;
            case EXPIRY_UNIT_DAY:
                this.unit = UNIT_DAY;
                break;
        }

    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}