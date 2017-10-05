package com.midtrans.raygun.network;

import com.midtrans.raygun.RaygunClient;
import com.midtrans.raygun.RaygunPulseEventType;
import com.midtrans.raygun.RaygunSettings;
import com.midtrans.raygun.network.http.RaygunUrlStreamHandlerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RaygunNetworkLogger {
    private static final long CONNECTION_TIMEOUT = 60000L; // 1 min
    private static volatile HashMap<String, RaygunNetworkRequestInfo> connections = new HashMap<String, RaygunNetworkRequestInfo>();
    private static boolean loggingEnabled = true;
    private static boolean loggingInitialized = false;

    public static void init() {
        if (loggingEnabled && !loggingInitialized) {
            try {
                RaygunUrlStreamHandlerFactory factory = new RaygunUrlStreamHandlerFactory();
                URL.setURLStreamHandlerFactory(factory);
                loggingInitialized = true;
            } catch (SecurityException e) {
                loggingInitialized = false;
            }
        }
    }

    public static void setEnabled(boolean enabled) {
        loggingEnabled = enabled;
    }

    public static synchronized void startNetworkCall(String url, long startTime) {
        if (!shouldIgnoreURL(url) && loggingEnabled) {
            removeOldEntries();
            String id = sanitiseURL(url);
            connections.put(id, new RaygunNetworkRequestInfo(url, startTime));
        }
    }

    public static synchronized void endNetworkCall(String url, String requestMethod, long endTime, int statusCode) {
        if (url != null) {
            String id = sanitiseURL(url);
            if ((connections.containsKey(id))) {
                RaygunNetworkRequestInfo request = connections.get(id);
                if (request != null) {
                    sendNetworkTimingEvent(request.url, requestMethod, request.startTime, endTime, statusCode, null);
                    connections.remove(id);
                }
            }
        }
    }

    /**
     * When a network request is cancelled we stop tracking it and do not send the information through.
     * Future updates may include sending the cancelled request timing through with information showing it was cancelled.
     */
    public static synchronized void cancelNetworkCall(String url, String requestMethod, long endTime, String exception) {
        if (url != null) {
            String id = sanitiseURL(url);
            if ((connections != null) && (connections.containsKey(id))) {
                connections.remove(id);
            }
        }
    }

    public static synchronized void sendNetworkTimingEvent(String url, String requestMethod, long startTime, long endTime, int statusCode, String exception) {
        if (!shouldIgnoreURL(url) && loggingEnabled) {
            url = sanitiseURL(url);
            RaygunClient.sendPulseTimingEvent(RaygunPulseEventType.NETWORK_CALL, requestMethod + " " + url, endTime - startTime);
        }
    }

    private static synchronized void removeOldEntries() {
        Iterator<Map.Entry<String, RaygunNetworkRequestInfo>> it = connections.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, RaygunNetworkRequestInfo> pairs = it.next();
            long startTime = pairs.getValue().startTime;
            if (System.currentTimeMillis() - startTime > CONNECTION_TIMEOUT) {
                it.remove();
            }
        }
    }

    private static String sanitiseURL(String url) {
        if (url != null) {
            int queryIndex = url.indexOf("?");
            if (queryIndex > 0) {
                url = url.substring(0, queryIndex);
            }
        }
        return url;
    }

    private static boolean shouldIgnoreURL(String url) {
        if (url == null) {
            return true;
        }
        for (String ignoredUrl : RaygunSettings.getIgnoredURLs()) {
            if (url.contains(ignoredUrl) || ignoredUrl.contains(url)) {
                return true;
            }
        }
        return false;
    }
}