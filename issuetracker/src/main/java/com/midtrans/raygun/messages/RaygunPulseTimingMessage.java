package com.midtrans.raygun.messages;

public class RaygunPulseTimingMessage {
    private String type;
    private long duration;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long milliseconds) {
        this.duration = milliseconds;
    }
}
