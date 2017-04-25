package com.midtrans.sdk.analytics.models;

/**
 * Created by rakawm on 4/21/17.
 */

public class Event {
    private String event;
    private Properties properties;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
