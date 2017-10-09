package com.midtrans.raygun.messages;

public class RaygunPulseMessage {
  private RaygunPulseDataMessage[] eventData;

  public RaygunPulseDataMessage[] getEventData() {
    return eventData;
  }

  public void setEventData(RaygunPulseDataMessage[] eventData) {
    this.eventData = eventData;
  }
}
