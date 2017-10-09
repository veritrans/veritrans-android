package com.midtrans.raygun.messages;

public class RaygunPulseDataMessage {
  private String sessionId;
  private String timestamp;
  private String type;
  private RaygunUserContext user;
  private String version;
  private String os;
  private String osVersion;
  private String platform;
  private String data;

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public RaygunUserContext getUser() {
    return user;
  }

  public void setUser(RaygunUserContext user) {
    this.user = user;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getOS() {
    return os;
  }

  public void setOS(String os) {
    this.os = os;
  }

  public String getOSVersion() {
    return osVersion;
  }

  public void setOSVersion(String osVersion) {
    this.osVersion = osVersion;
  }

  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }
}
