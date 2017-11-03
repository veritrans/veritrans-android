package com.midtrans.raygun.messages;

public class RaygunClientMessage {
  private String version;
  private String clientUrl;
  private String name;

  public RaygunClientMessage() {
    setName("Raygun4Android");
    setVersion("3.0.0");
    setClientUrl("https://github.com/MindscapeHQ/raygun4android");
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getClientUrl() {
    return clientUrl;
  }

  public void setClientUrl(String clientUrlString) {
    this.clientUrl = clientUrlString;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
