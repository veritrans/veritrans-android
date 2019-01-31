package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional;

import com.google.gson.annotations.SerializedName;

import com.midtrans.sdk.corekit.base.enums.ExpiryTimeUnit;

import java.io.Serializable;

import static com.midtrans.sdk.corekit.utilities.Helper.mappingToExpiryTimeUnit;

public class CheckoutExpiry implements Serializable {

    @SerializedName("start_time")
    private String startTime;
    @SerializedName("unit")
    private String unit;
    @SerializedName("duration")
    private int duration;

    public CheckoutExpiry(String startTime,
                          @ExpiryTimeUnit String unit,
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

    public @ExpiryTimeUnit
    String getUnit() {
        return mappingToExpiryTimeUnit(unit);
    }

    public void setUnit(@ExpiryTimeUnit String unit) {
        this.unit = mappingToExpiryTimeUnit(unit);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}