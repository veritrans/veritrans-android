package com.midtrans.raygun.messages;

import android.content.Context;

import java.util.List;
import java.util.Map;

public class RaygunMessageDetails {
  private String groupingKey;
  private String machineName;
  private String version = "Not supplied";
  private RaygunErrorMessage error;
  private RaygunEnvironmentMessage environment;
  private RaygunClientMessage client;
  private List tags;
  private Map userCustomData;
  private RaygunAppContext context;
  private RaygunUserContext user;
  private NetworkInfo request;

  public void setMachineName(String machineName) {
    this.machineName = machineName;
  }

  public String getMachineName() {
    return machineName;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public RaygunErrorMessage getError() {
    return error;
  }

  public void setError(RaygunErrorMessage error) {
    this.error = error;
  }

  public RaygunEnvironmentMessage getEnvironment() {
    return environment;
  }

  public void setEnvironment(RaygunEnvironmentMessage environment) {
    this.environment = environment;
  }

  public RaygunClientMessage getClient() {
    return client;
  }

  public void setClient(RaygunClientMessage client) {
    this.client = client;
  }

  public List getTags() {
    return tags;
  }

  public void setTags(List tags) {
    this.tags = tags;
  }

  public Map getUserCustomData() {
    return userCustomData;
  }

  public void setUserCustomData(Map userCustomData) {
    this.userCustomData = userCustomData;
  }

  public RaygunAppContext getAppContext() {
    return context;
  }

  public void setAppContext(String identifier) {
    this.context = new RaygunAppContext(identifier);
  }

  public RaygunUserContext getUserContext() {
    return user;
  }

  public void setUserContext(Context context) {
    this.user = new RaygunUserContext(context);
  }

  public void setUserContext(String user) {
    this.user = new RaygunUserContext(user);
  }

  public void setUserContext(RaygunUserInfo userInfo, Context context) {
    this.user = new RaygunUserContext(userInfo, context);
  }

  public void setNetworkInfo(Context context) {
    this.request = new NetworkInfo(context);
  }

  public void setGroupingKey(String groupingKey) {
    this.groupingKey = groupingKey;
  }
}
