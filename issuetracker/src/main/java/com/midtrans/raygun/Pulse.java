package com.midtrans.raygun;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import com.midtrans.raygun.network.RaygunNetworkLogger;

import java.util.concurrent.TimeUnit;

public class Pulse implements ActivityLifecycleCallbacks {
    private static Pulse pulse;
    private static Activity mainActivity;
    private static Activity currentActivity;
    private static Activity loadingActivity;
    private static long startTime;

    protected static void attach(Activity mainActivity) {
        if (Pulse.pulse == null && mainActivity != null) {
            Application application = mainActivity.getApplication();

            if (application != null) {
                Pulse.mainActivity = mainActivity;
                Pulse.currentActivity = mainActivity;
                Pulse.startTime = System.nanoTime();

                Pulse.pulse = new Pulse();
                application.registerActivityLifecycleCallbacks(Pulse.pulse);

                RaygunClient.sendPulseEvent("session_start");
                RaygunNetworkLogger.init();
            }
        }
    }

    protected static void attach(Activity mainActivity, boolean networkLogging) {
        RaygunNetworkLogger.setEnabled(networkLogging);
        attach(mainActivity);
    }

    protected static void detach() {
        if (Pulse.pulse != null && Pulse.mainActivity != null && Pulse.mainActivity.getApplication() != null) {
            Pulse.mainActivity.getApplication().unregisterActivityLifecycleCallbacks(Pulse.pulse);
            Pulse.mainActivity = null;
            Pulse.currentActivity = null;
            Pulse.pulse = null;
        }
    }

    protected static void sendRemainingActivity() {
        if (Pulse.pulse != null) {
            if (Pulse.loadingActivity != null) {
                String activityName = getActivityName(Pulse.loadingActivity);

                long diff = System.nanoTime() - Pulse.startTime;
                long duration = TimeUnit.NANOSECONDS.toMillis(diff);
                RaygunClient.sendPulseTimingEvent(RaygunPulseEventType.ACTIVITY_LOADED, activityName, duration);
            }
            RaygunClient.sendPulseEvent("session_end");
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (Pulse.currentActivity == null) {
            RaygunClient.sendPulseEvent("session_start");
        }

        if (activity != Pulse.currentActivity) {
            Pulse.currentActivity = activity;
            Pulse.loadingActivity = activity;
            Pulse.startTime = System.nanoTime();
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (Pulse.currentActivity == null) {
            RaygunClient.sendPulseEvent("session_start");
        }

        if (activity != Pulse.currentActivity) {
            Pulse.currentActivity = activity;
            Pulse.loadingActivity = activity;
            Pulse.startTime = System.nanoTime();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (Pulse.currentActivity == null) {
            RaygunClient.sendPulseEvent("session_start");
        }

        String activityName = getActivityName(activity);
        long duration = 0;

        if (activity == Pulse.currentActivity) {
            long diff = System.nanoTime() - Pulse.startTime;
            duration = TimeUnit.NANOSECONDS.toMillis(diff);
        }

        Pulse.currentActivity = activity;
        Pulse.loadingActivity = null;

        RaygunClient.sendPulseTimingEvent(RaygunPulseEventType.ACTIVITY_LOADED, activityName, duration);
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity == Pulse.currentActivity) {
            Pulse.currentActivity = null;
            Pulse.loadingActivity = null;
            RaygunClient.sendPulseEvent("session_end");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    private static String getActivityName(Activity activity) {
        return activity.getClass().getSimpleName();
    }
}
