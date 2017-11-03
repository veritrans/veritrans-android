package com.midtrans.raygun;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import com.google.gson.Gson;
import com.midtrans.raygun.messages.RaygunMessage;
import com.midtrans.raygun.messages.RaygunPulseData;
import com.midtrans.raygun.messages.RaygunPulseDataMessage;
import com.midtrans.raygun.messages.RaygunPulseMessage;
import com.midtrans.raygun.messages.RaygunPulseTimingMessage;
import com.midtrans.raygun.messages.RaygunUserContext;
import com.midtrans.raygun.messages.RaygunUserInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;


/**
 * User: Mindscape
 * The official Raygun provider for Android. This is the main class that provides functionality
 * for automatically sending exceptions to the Raygun service.
 * <p>
 * You should call Init() on the static RaygunClient instance, passing in the current Context,
 * instead of instantiating this class.
 */
public class RaygunClient {
    private static String apiKey;
    private static Context context;
    private static String version;
    private static Intent service;
    private static String appContextIdentifier;
    private static String user;
    private static RaygunUserInfo userInfo;
    private static RaygunUncaughtExceptionHandler handler;
    private static RaygunOnBeforeSend onBeforeSend;
    private static List tags;
    private static Map userCustomData;
    private static String sessionId;

    /**
     * Initializes the Raygun client. This expects that you have placed the API key in your
     * AndroidManifest.xml, in a meta-data element.
     *
     * @param context The context of the calling Android activity.
     */
    public static void init(Context context) {
        String apiKey = readApiKey(context);
        init(context, apiKey);
        RaygunClient.appContextIdentifier = UUID.randomUUID().toString();
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #init(Context)}
     */
    @Deprecated
    public static void Init(Context context) {
        init(context);
    }

    /**
     * Initializes the Raygun client with the version of your application.
     * This expects that you have placed the API key in your AndroidManifest.xml, in a meta-data element.
     *
     * @param version The version of your application, format x.x.x.x, where x is a positive integer.
     * @param context The context of the calling Android activity.
     */
    public static void init(String version, Context context) {
        String apiKey = readApiKey(context);
        init(context, apiKey, version);
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #init(String, Context)}
     */
    @Deprecated
    public static void Init(String version, Context context) {
        init(version, context);
    }

    /**
     * Initializes the Raygun client with your Android application's context and your
     * Raygun API key. The version transmitted will be the value of the versionName attribute in your manifest element.
     *
     * @param context The Android context of your activity
     * @param apiKey  An API key that belongs to a Raygun application created in your dashboard
     */
    public static void init(Context context, String apiKey) {
        RaygunClient.apiKey = apiKey;
        RaygunClient.context = context;

        String version = null;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            RaygunLogger.i("Couldn't read version from calling package");
        }

        if (version != null) {
            RaygunClient.version = version;
        } else {
            RaygunClient.version = "Not provided";
        }
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #init(Context, String)}
     */
    @Deprecated
    public static void Init(Context context, String apiKey) {
        init(context, apiKey);
    }

    /**
     * Initializes the Raygun client with your Android application's context, your
     * Raygun API key, and the version of your application
     *
     * @param context The Android context of your activity
     * @param apiKey  An API key that belongs to a Raygun application created in your dashboard
     * @param version The version of your application, format x.x.x.x, where x is a positive integer.
     */
    public static void init(Context context, String apiKey, String version) {
        init(context, apiKey);
        RaygunClient.version = version;
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #init(Context, String, String)}
     */
    @Deprecated
    public static void Init(Context context, String apiKey, String version) {
        init(context, apiKey, version);
    }

    /**
     * Attaches a pre-built Raygun exception handler to the thread's DefaultUncaughtExceptionHandler.
     * This automatically sends any exceptions that reaches it to the Raygun API.
     */
    public static void attachExceptionHandler() {
        UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(oldHandler instanceof RaygunUncaughtExceptionHandler)) {
            RaygunClient.handler = new RaygunUncaughtExceptionHandler(oldHandler);
            Thread.setDefaultUncaughtExceptionHandler(RaygunClient.handler);
        }
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #attachExceptionHandler()}
     */
    @Deprecated
    public static void AttachExceptionHandler() {
        attachExceptionHandler();
    }

    /**
     * Attaches a pre-built Raygun exception handler to the thread's DefaultUncaughtExceptionHandler.
     * This automatically sends any exceptions that reaches it to the Raygun API.
     *
     * @param tags A list of tags that relate to the calling application's currently build or state.
     *             These will be appended to all exception messages sent to Raygun.
     * @deprecated Call attachExceptionHandler(), then setTags(List) instead
     */
    @Deprecated
    public static void AttachExceptionHandler(List tags) {
        UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(oldHandler instanceof RaygunUncaughtExceptionHandler)) {
            RaygunClient.handler = new RaygunUncaughtExceptionHandler(oldHandler, tags);
            Thread.setDefaultUncaughtExceptionHandler(RaygunClient.handler);
        }
    }

    /**
     * Attaches the Raygun Pulse feature which will automatically report session and view events.
     *
     * @param activity The main/entry activity of the Android app.
     */
    public static void attachPulse(Activity activity) {
        Pulse.attach(activity);
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #attachPulse(Activity)}
     */
    @Deprecated
    public static void AttachPulse(Activity activity) {
        attachPulse(activity);
    }

    /**
     * Attaches the Raygun Pulse feature which will automatically report session and view events.
     *
     * @param activity       The main/entry activity of the Android app.
     * @param networkLogging Automatically report the performance of network requests.
     */
    public static void attachPulse(Activity activity, boolean networkLogging) {
        Pulse.attach(activity, networkLogging);
    }

    /**
     * Attaches a pre-built Raygun exception handler to the thread's DefaultUncaughtExceptionHandler.
     * This automatically sends any exceptions that reaches it to the Raygun API.
     *
     * @param tags           A list of tags that relate to the calling application's currently build or state.
     *                       These will be appended to all exception messages sent to Raygun.
     * @param userCustomData A set of key-value pairs that will be attached to each exception message
     *                       sent to Raygun. This can contain any extra data relating to the calling
     *                       application's state you would like to see.
     * @deprecated Call attachExceptionHandler(), then setUserCustomData(Map) instead
     */
    @Deprecated
    public static void AttachExceptionHandler(List tags, Map userCustomData) {
        UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(oldHandler instanceof RaygunUncaughtExceptionHandler)) {
            RaygunClient.handler = new RaygunUncaughtExceptionHandler(oldHandler, tags, userCustomData);
            Thread.setDefaultUncaughtExceptionHandler(RaygunClient.handler);
        }
    }

    /**
     * Sends an exception-type object to Raygun.
     *
     * @param throwable The Throwable object that occurred in your application that will be sent to Raygun.
     */
    public static void send(Throwable throwable) {
        RaygunMessage msg = buildMessage(throwable);
        postCachedMessages();

        if (RaygunClient.tags != null) {
            msg.getDetails().setTags(RaygunClient.tags);
        }

        if (RaygunClient.userCustomData != null) {
            msg.getDetails().setUserCustomData(RaygunClient.userCustomData);
        }

        if (RaygunClient.onBeforeSend != null) {
            msg = RaygunClient.onBeforeSend.onBeforeSend(msg);
            if (msg == null) {
                return;
            }
        }

        spinUpService(RaygunClient.apiKey, new Gson().toJson(msg), false);
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #send(Throwable)}
     */
    @Deprecated
    public static void Send(Throwable throwable) {
        send(throwable);
    }

    /**
     * Sends an exception-type object to Raygun with a list of tags you specify.
     *
     * @param throwable The Throwable object that occurred in your application that will be sent to Raygun.
     * @param tags      A list of data that will be attached to the Raygun message and visible on the error in the dashboard.
     *                  This could be a build tag, lifecycle state, debug/production version etc.
     */
    public static void send(Throwable throwable, List tags) {
        RaygunMessage msg = buildMessage(throwable);
        msg.getDetails().setTags(mergeTags(tags));

        if (RaygunClient.userCustomData != null) {
            msg.getDetails().setUserCustomData(RaygunClient.userCustomData);
        }

        if (RaygunClient.onBeforeSend != null) {
            msg = RaygunClient.onBeforeSend.onBeforeSend(msg);
            if (msg == null) {
                return;
            }
        }

        postCachedMessages();
        spinUpService(RaygunClient.apiKey, new Gson().toJson(msg), false);
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #send(Throwable, List)}
     */
    @Deprecated
    public static void Send(Throwable throwable, List tags) {
        send(throwable, tags);
    }

    /**
     * Sends an exception-type object to Raygun with a list of tags you specify, and a set of
     * custom data.
     *
     * @param throwable      The Throwable object that occurred in your application that will be sent to Raygun.
     * @param tags           A list of data that will be attached to the Raygun message and visible on the error in the dashboard.
     *                       This could be a build tag, lifecycle state, debug/production version etc.
     * @param userCustomData A set of custom key-value pairs relating to your application and its current state. This is a bucket
     *                       where you can attach any related data you want to see to the error.
     */
    public static void send(Throwable throwable, List tags, Map userCustomData) {
        RaygunMessage msg = buildMessage(throwable);

        msg.getDetails().setTags(mergeTags(tags));
        msg.getDetails().setUserCustomData(mergeUserCustomData(userCustomData));

        if (RaygunClient.onBeforeSend != null) {
            msg = RaygunClient.onBeforeSend.onBeforeSend(msg);
            if (msg == null) {
                return;
            }
        }

        postCachedMessages();
        spinUpService(RaygunClient.apiKey, new Gson().toJson(msg), false);
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #send(Throwable, List, Map)}
     */
    @Deprecated
    public static void Send(Throwable throwable, List tags, Map userCustomData) {
        send(throwable, tags, userCustomData);
    }

    /**
     * Raw post method that delivers a pre-built RaygunMessage to the Raygun API. You do not need to call this method
     * directly unless you want to manually build your own message - for most purposes you should call Send().
     *
     * @param apiKey      The API key of the app to deliver to
     * @param jsonPayload The JSON representation of a RaygunMessage to be delivered over HTTPS.
     * @return HTTP result code - 202 if successful, 403 if API key invalid, 400 if bad message (invalid properties)
     */
    public static int post(String apiKey, String jsonPayload) {
        try {
            if (validateApiKey(apiKey)) {
                URL endpoint = new URL(RaygunSettings.getApiEndpoint());
                HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();

                try {
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("X-ApiKey", apiKey);
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(jsonPayload.toString().getBytes("UTF-8"));
                    outputStream.close();

                    int responseCode = connection.getResponseCode();
                    RaygunLogger.d("Exception message HTTP POST result: " + responseCode);

                    return responseCode;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        } catch (Exception e) {
            RaygunLogger.e("Couldn't post exception - " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #post(String, String)}
     */
    @Deprecated
    public static int Post(String apiKey, String jsonPayload) {
        return post(apiKey, jsonPayload);
    }

    protected static void sendPulseEvent(String name) {
        if ("session_start".equals(name)) {
            RaygunClient.sessionId = UUID.randomUUID().toString();
        }

        RaygunPulseMessage message = new RaygunPulseMessage();
        RaygunPulseDataMessage pulseData = new RaygunPulseDataMessage();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar c = Calendar.getInstance();

        if ("session_end".equals(name)) {
            c.add(Calendar.SECOND, 2);
        }

        String timestamp = df.format(c.getTime());
        pulseData.setTimestamp(timestamp);
        pulseData.setVersion(RaygunClient.version);
        pulseData.setOS("Android");
        pulseData.setOSVersion(Build.VERSION.RELEASE);
        pulseData.setPlatform(String.format("%s %s", Build.MANUFACTURER, Build.MODEL));

        RaygunUserContext userContext = RaygunClient.userInfo == null ? new RaygunUserContext(new RaygunUserInfo(null, null, null, null, null, true), RaygunClient.context) : new RaygunUserContext(RaygunClient.userInfo, RaygunClient.context);
        pulseData.setUser(userContext);

        pulseData.setSessionId(RaygunClient.sessionId);
        pulseData.setType(name);

        message.setEventData(new RaygunPulseDataMessage[]{pulseData});

        spinUpService(RaygunClient.apiKey, new Gson().toJson(message), true);
    }

    /**
     * Sends a pulse timing event to Raygun. The message is sent on a background thread.
     *
     * @param eventType    The type of event that occurred.
     * @param name         The name of the event resource such as the activity name or URL of a network call.
     * @param milliseconds The duration of the event in milliseconds.
     */
    public static void sendPulseTimingEvent(RaygunPulseEventType eventType, String name, long milliseconds) {
        if (RaygunClient.sessionId == null) {
            sendPulseEvent("session_start");
        }

        if (eventType == RaygunPulseEventType.ACTIVITY_LOADED) {
            if (RaygunClient.shouldIgnoreView(name)) {
                return;
            }
        }

        RaygunPulseMessage message = new RaygunPulseMessage();
        RaygunPulseDataMessage dataMessage = new RaygunPulseDataMessage();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MILLISECOND, -(int) milliseconds);
        String timestamp = df.format(c.getTime());

        dataMessage.setTimestamp(timestamp);
        dataMessage.setSessionId(RaygunClient.sessionId);
        dataMessage.setVersion(RaygunClient.version);
        dataMessage.setOS("Android");
        dataMessage.setOSVersion(Build.VERSION.RELEASE);
        dataMessage.setPlatform(String.format("%s %s", Build.MANUFACTURER, Build.MODEL));
        dataMessage.setType("mobile_event_timing");

        RaygunUserContext userContext = RaygunClient.userInfo == null ? new RaygunUserContext(new RaygunUserInfo(null, null, null, null, null, true), RaygunClient.context) : new RaygunUserContext(RaygunClient.userInfo, RaygunClient.context);
        dataMessage.setUser(userContext);

        RaygunPulseData data = new RaygunPulseData();
        RaygunPulseTimingMessage timingMessage = new RaygunPulseTimingMessage();
        timingMessage.setType(eventType == RaygunPulseEventType.ACTIVITY_LOADED ? "p" : "n");
        timingMessage.setDuration(milliseconds);
        data.setName(name);
        data.setTiming(timingMessage);

        RaygunPulseData[] dataArray = new RaygunPulseData[]{data};
        String dataStr = new Gson().toJson(dataArray);
        dataMessage.setData(dataStr);

        message.setEventData(new RaygunPulseDataMessage[]{dataMessage});

        spinUpService(RaygunClient.apiKey, new Gson().toJson(message), true);
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #sendPulseTimingEvent(RaygunPulseEventType, String, long)}
     */
    @Deprecated
    public static void SendPulseTimingEvent(RaygunPulseEventType eventType, String name, long milliseconds) {
        sendPulseTimingEvent(eventType, name, milliseconds);
    }

    protected static int postPulseMessage(String apiKey, String jsonPayload) {
        try {
            if (validateApiKey(apiKey)) {
                URL endpoint = new URL(RaygunSettings.getPulseEndpoint());
                HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();

                try {
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("X-ApiKey", apiKey);
                    connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(jsonPayload.getBytes("UTF-8"));
                    outputStream.close();

                    int responseCode = connection.getResponseCode();
                    RaygunLogger.d("Pulse HTTP POST result: " + responseCode);

                    return responseCode;
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        } catch (Exception e) {
            RaygunLogger.e("Couldn't post exception - " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    private static boolean hasInternetConnection() {
        if (RaygunClient.context != null) {
            ConnectivityManager cm = (ConnectivityManager) RaygunClient.context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }

        return false;
    }

    private static RaygunMessage buildMessage(Throwable throwable) {
        try {
            RaygunMessage msg = RaygunMessageBuilder.instance()
                    .setEnvironmentDetails(RaygunClient.context)
                    .setMachineName(Build.MODEL)
                    .setExceptionDetails(throwable)
                    .setClientDetails()
                    .setAppContext(RaygunClient.appContextIdentifier)
                    .setVersion(RaygunClient.version)
                    .setNetworkInfo(RaygunClient.context)
                    .build();

            if (RaygunClient.version != null) {
                msg.getDetails().setVersion(RaygunClient.version);
            }

            if (RaygunClient.userInfo != null) {
                msg.getDetails().setUserContext(RaygunClient.userInfo, RaygunClient.context);
            } else if (RaygunClient.user != null) {
                msg.getDetails().setUserContext(RaygunClient.user);
            } else {
                msg.getDetails().setUserContext(RaygunClient.context);
            }
            return msg;
        } catch (Exception e) {
            RaygunLogger.e("Failed to build RaygunMessage - " + e);
        }
        return null;
    }

    private static String readApiKey(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString("com.mindscapehq.android.raygun4android.apikey");
        } catch (PackageManager.NameNotFoundException e) {
            RaygunLogger.e("Couldn't read API key from your AndroidManifest.xml <meta-data /> element; cannot send: " + e.getMessage());
        }
        return null;
    }

    private static Boolean validateApiKey(String apiKey) throws Exception {
        if (apiKey.length() == 0) {
            RaygunLogger.e("API key has not been provided, exception will not be logged");
            return false;
        } else {
            return true;
        }
    }

    private static void postCachedMessages() {
        if (hasInternetConnection()) {
            File[] fileList = RaygunClient.context.getCacheDir().listFiles();
            for (File f : fileList) {
                try {
                    String ext = getExtension(f.getName());
                    if (ext.equalsIgnoreCase("raygun")) {
                        ObjectInputStream ois = null;
                        try {
                            ois = new ObjectInputStream(new FileInputStream(f));
                            MessageApiKey messageApiKey = (MessageApiKey) ois.readObject();
                            spinUpService(messageApiKey.apiKey, messageApiKey.message, false);
                            f.delete();
                        } finally {
                            if (ois != null) {
                                ois.close();
                            }
                        }
                    }
                } catch (FileNotFoundException e) {
                    RaygunLogger.e("Error loading cached message from filesystem - " + e.getMessage());
                } catch (IOException e) {
                    RaygunLogger.e("Error reading cached message from filesystem - " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    RaygunLogger.e("Error in cached message from filesystem - " + e.getMessage());
                }
            }
        }
    }

    private static void spinUpService(String apiKey, String jsonPayload, boolean isPulse) {
        System.out.println(jsonPayload);
        Intent intent;
        if (RaygunClient.service == null) {
            intent = new Intent(RaygunClient.context, RaygunPostService.class);
            intent.setAction("main.java.com.mindscapehq.android.raygun4android.RaygunClient.RaygunPostService");
            intent.setPackage("main.java.com.mindscapehq.android.raygun4android.RaygunClient");
            intent.setComponent(new ComponentName(RaygunClient.context, RaygunPostService.class));
        } else {
            intent = RaygunClient.service;
        }

        intent.putExtra("msg", jsonPayload);
        intent.putExtra("apikey", apiKey);
        intent.putExtra("isPulse", isPulse ? "True" : "False");
        RaygunClient.service = intent;
        RaygunClient.context.startService(RaygunClient.service);
    }

    public static void closePostService() {
        if (RaygunClient.service != null) {
            RaygunClient.context.stopService(RaygunClient.service);
            RaygunClient.service = null;
        }
    }

    private static List mergeTags(List paramTags) {
        if (RaygunClient.tags != null) {
            List merged = new ArrayList(RaygunClient.tags);
            merged.addAll(paramTags);
            return merged;
        } else {
            return paramTags;
        }
    }

    private static Map mergeUserCustomData(Map paramUserCustomData) {
        if (RaygunClient.userCustomData != null) {
            Map merged = new HashMap(RaygunClient.userCustomData);
            merged.putAll(paramUserCustomData);
            return merged;
        } else {
            return paramUserCustomData;
        }
    }

    protected static String getExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int separator = Math.max(filename.lastIndexOf('/'), filename.lastIndexOf('\\'));
        int dotPos = filename.lastIndexOf(".");
        int index = separator > dotPos ? -1 : dotPos;
        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

    /**
     * Sets the current user of your application. If user is an email address which is associated with a Gravatar,
     * their picture will be displayed in the error view. If this is not called a random ID will be assigned.
     * If the user context changes in your application (i.e log in/out), be sure to call this again with the
     * updated user name/email address.
     *
     * @param user A user name or email address representing the current user
     */
    public static void setUser(String user) {
        if (user != null && user.length() > 0) {
            RaygunClient.user = user;
        }
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #setUser(RaygunUserInfo)}
     */
    @Deprecated
    public static void SetUser(RaygunUserInfo userInfo) {
        setUser(userInfo);
    }

    public static void setUser(RaygunUserInfo userInfo) {
        RaygunClient.userInfo = userInfo;
    }

    /**
     * Manually stores the version of your application to be transmitted with each message, for version
     * filtering. This is normally read from your AndroidManifest.xml (the versionName attribute on manifest element)
     * or passed in on init(); this is only provided as a convenience.
     *
     * @param version The version of your application, format x.x.x.x, where x is a positive integer.
     */
    public static void setVersion(String version) {
        if (version != null) {
            RaygunClient.version = version;
        }
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #setVersion(String)}
     */
    @Deprecated
    public static void SetVersion(String version) {
        setVersion(version);
    }

    public static RaygunUncaughtExceptionHandler getExceptionHandler() {
        return RaygunClient.handler;
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #getExceptionHandler()}
     */
    @Deprecated
    public static RaygunUncaughtExceptionHandler GetExceptionHandler() {
        return getExceptionHandler();
    }

    public static String getApiKey() {
        return RaygunClient.apiKey;
    }

    public static List getTags() {
        return RaygunClient.tags;
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #getTags()}
     */
    @Deprecated
    public static List GetTags() {
        return getTags();
    }

    public static void setTags(List tags) {
        RaygunClient.tags = tags;
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #setTags(List)}
     */
    @Deprecated
    public static void SetTags(List tags) {
        setTags(tags);
    }

    public static Map getUserCustomData() {
        return RaygunClient.userCustomData;
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #getUserCustomData()}
     */
    @Deprecated
    public static Map GetUserCustomData() {
        return getUserCustomData();
    }

    public static void setUserCustomData(Map userCustomData) {
        RaygunClient.userCustomData = userCustomData;
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #setUserCustomData(Map)}
     */
    @Deprecated
    public static void SetUserCustomData(Map userCustomData) {
        setUserCustomData(userCustomData);
    }

    public static void setOnBeforeSend(RaygunOnBeforeSend onBeforeSend) {
        RaygunClient.onBeforeSend = onBeforeSend;
    }

    /**
     * @deprecated As of release 3.0.0, replaced by {@link #setOnBeforeSend(RaygunOnBeforeSend)}
     */
    @Deprecated
    public static void SetOnBeforeSend(RaygunOnBeforeSend onBeforeSend) {
        setOnBeforeSend(onBeforeSend);
    }

    /**
     * Allows the user to add more URLs to filter out, so network timing events are not sent for them.
     *
     * @param urls An array of urls to filter out by.
     */
    public static void ignoreURLs(String[] urls) {
        RaygunSettings.ignoreURLs(urls);
    }

    /**
     * Allows the user to add more views to filter out, so load timing events are not sent for them.
     *
     * @param views An array of activity names to filter out by.
     */
    public static void ignoreViews(String[] views) {
        RaygunSettings.ignoreViews(views);
    }

    private static boolean shouldIgnoreView(String viewName) {
        if (viewName == null) {
            return true;
        }
        for (String ignoredView : RaygunSettings.getIgnoredViews()) {
            if (viewName.contains(ignoredView) || ignoredView.contains(viewName)) {
                return true;
            }
        }
        return false;
    }

    public static class RaygunUncaughtExceptionHandler implements UncaughtExceptionHandler {
        private UncaughtExceptionHandler defaultHandler;
        private List tags;
        private Map userCustomData;

        public RaygunUncaughtExceptionHandler(UncaughtExceptionHandler defaultHandler) {
            this.defaultHandler = defaultHandler;
        }

        @Deprecated
        public RaygunUncaughtExceptionHandler(UncaughtExceptionHandler defaultHandler, List tags) {
            this.defaultHandler = defaultHandler;
            this.tags = tags;
        }

        @Deprecated
        public RaygunUncaughtExceptionHandler(UncaughtExceptionHandler defaultHandler, List tags, Map userCustomData) {
            this.defaultHandler = defaultHandler;
            this.tags = tags;
            this.userCustomData = userCustomData;
        }

        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            if (userCustomData != null) {
                RaygunClient.send(throwable, tags, userCustomData);
            } else if (RaygunClient.tags != null) {
                RaygunClient.send(throwable, tags);
            } else {
                List tags = new ArrayList();
                tags.add("UnhandledException");
                RaygunClient.send(throwable, tags);
                Pulse.sendRemainingActivity();
            }
            defaultHandler.uncaughtException(thread, throwable);
        }
    }
}
