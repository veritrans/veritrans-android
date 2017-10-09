package com.midtrans.raygun.messages;

public class RaygunUserInfo {
  private Boolean isAnonymous;
  private String email;
  private String fullName;
  private String firstName;
  private String uuid;
  private String identifier;

  /**
   * Set the current user's info to be transmitted - any parameters can be null if the data is not available or
   * you do not wish to send it.
   * @param firstName The user's first name
   * @param fullName The user's full name - if setting the first name you should set this too
   * @param emailAddress User's email address
   * @param uuid Device identifier - if this is null we will attempt to generate it automatically (legacy behavior).
   * @param isAnonymous Whether this user data represents an anonymous user
   * @param identifier Unique identifier for this user. Set this to the internal identifier you use to look up users,
   *                   or a correlation ID for anonymous users if you have one. It doesn't have to be unique, but we will treat
   *                   any duplicated values as the same user. If you use their email address here, pass it in as the 'emailAddress' parameter too.
   */
  public RaygunUserInfo(String identifier, String firstName, String fullName, String emailAddress, String uuid, Boolean isAnonymous) {
    this.identifier = identifier;
    this.firstName = firstName;
    this.fullName = fullName;
    this.email = emailAddress;
    this.uuid = uuid;
    this.isAnonymous = isAnonymous;
  }

  public RaygunUserInfo() {
  }

  public Boolean getIsAnonymous() {
    return this.isAnonymous;
  }

  public void setAnonymous(Boolean anonymous) {
    isAnonymous = anonymous;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFullName() {
    return this.fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getUuid() {
    return this.uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }
}
