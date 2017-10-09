package com.midtrans.raygun.messages;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

public class RaygunUserContext {
  protected static final String PREFS_FILE = "device_id.xml";
  protected static final String PREFS_DEVICE_ID = "device_id";

  private String identifier;
  private String firstName;
  private String lastName;
  private String fullName;
  private String email;
  private String uuid;
  private Boolean isAnonymous;

  public RaygunUserContext(Context context) {
    identifier = getDeviceUuid(context);
  }

  public RaygunUserContext(String user) {
    identifier = user;
  }

  public RaygunUserContext(RaygunUserInfo userInfo, Context context) {
    if (userInfo.getIdentifier() == null) {
      identifier = getDeviceUuid(context);
    }
    else {
      identifier = userInfo.getIdentifier();
    }

    firstName = userInfo.getFirstName();
    fullName = userInfo.getFullName();
    email = userInfo.getEmail();
    uuid = userInfo.getUuid();
    isAnonymous = userInfo.getIsAnonymous();
  }

  private String getDeviceUuid(Context context) {
    synchronized (RaygunAppContext.class) {
      if (identifier == null) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
        String id = prefs.getString(PREFS_DEVICE_ID, null );

        if (id != null) {
          return UUID.fromString(id).toString();
        }
        else {
          final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

          try {
            if (!"9774d56d682e549c".equals(androidId)) {
              id = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
            }
            else {
              id = UUID.randomUUID().toString();
            }
          }
          catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
          }

          prefs.edit().putString(PREFS_DEVICE_ID, id.toString() ).commit();
          return id;
        }
      }
      return identifier;
    }
  }

  public String getIdentifier() {
    return identifier;
  }
}
