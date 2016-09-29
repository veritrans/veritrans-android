package com.midtrans.sdk.corekit.analytics;

/**
 * @author rakawm
 */
public class MixpanelEvent {
    private String event;
    private MixpanelProperties properties;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public MixpanelProperties getProperties() {
        return properties;
    }

    public void setProperties(MixpanelProperties properties) {
        this.properties = properties;
    }
}
