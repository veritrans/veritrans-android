## GCM

We do not support GCM out of the box, but it can be implemented on top of our SDK.

### Implementation of GCM

 Please visit following link for installtion steps of GCM
 * https://developers.google.com/cloud-messaging/android/client
 *  Create configuration file and add to your root of your project.

 Add the following dependency to your project-level build.gradle:
 ```
 classpath 'com.google.gms:google-services:1.5.0-beta2'
 ```
 Add the plugin to your app-level build.gradle:
 ```
 apply plugin: 'com.google.gms.google-services'
 ```
  Add following permissions to manifest file

 ```
 <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
 <uses-permission android:name="android.permission.WAKE_LOCK" />
 <uses-permission android:name="<your app's package name>.permission.C2D_MESSAGE"/>
 ```

 Add following receivers and services for receiving push notification and registeration purpose

 ```
 <receiver
            android:name="com.google.android.gms.gcm.GcmReceiver"
            android:exported="true"
            android:permission="com.google.android.c2dm.permission.SEND" >
            <intent-filter>
                <action android:name="com.google.android.c2dm.intent.RECEIVE" />
                <category android:name="<your app's package name>" />
            </intent-filter>
        </receiver>
        <service
            android:name="id.co.veritrans.sdk.example.gcmutils.VeritransGcmListenerService"
            android:exported="false" >
            <intent-filter>
                <action android:name="com.google.android.c2dm.intent.RECEIVE" />
            </intent-filter>
        </service>
        <!-- [END gcm_listener] -->
        <!-- [START instanceId_listener] -->
        <service
            android:name="id.co.veritrans.sdk.example.gcmutils.VeritransInstanceIDListenerService"
            android:exported="false">
            <intent-filter>
                <action android:name="com.google.android.gms.iid.InstanceID"/>
            </intent-filter>
        </service>
        <!-- [END instanceId_listener] -->
        <service
            android:name="id.co.veritrans.sdk.example.gcmutils.RegistrationIntentService"
            android:exported="false">
        </service>
 ```

 * Now  start registeration service in first activity or application class. Check availability of GoogleApi in device.
 Here is code for checking availability of code

```
private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
```

For registeration functionality you can refer RegistrationIntentService from sdk example app.
After registeration we get token from google services, we are sending this token to merchant server.

**VeritransGcmListenerService** will catch the push sent by gcm. In push we will receive detaile of payments which are pending.
