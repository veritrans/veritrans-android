package com.midtrans.sdk.uikit;

import android.app.Activity;
import android.os.Build;
import com.midtrans.raygun.RaygunLogger;
import com.midtrans.raygun.RaygunOnBeforeSend;
import com.midtrans.raygun.messages.RaygunErrorStackTraceLineMessage;
import com.midtrans.raygun.messages.RaygunMessage;
import com.midtrans.raygun.messages.RaygunMessageDetails;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.SdkUtil;
import com.midtrans.sdk.corekit.models.MerchantPreferences;
import com.midtrans.sdk.uikit.utilities.DeviceUtils;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Fajar on 02/04/18.
 */

public class UiKitOnBeforeSend implements RaygunOnBeforeSend {

    private Activity activity;
    private final String TAG = UiKitOnBeforeSend.class.getName();
    private final String IDENTIFIER = "com.midtrans";

    public UiKitOnBeforeSend(Activity activity) {
        this.activity = activity;
    }

    /**
     * Iterate all lines of stack trace to check whether the error has something
     * to do with Midtrans SDK
     * @param raygunMessage raygun message that will be modified before being sent
     * @return raygun message that will be sent
     */

    @Override
    public RaygunMessage onBeforeSend(RaygunMessage raygunMessage) {
        Logger.d(TAG, "Error report is intercepted.");
        String rootMessage = raygunMessage.getDetails().getError().getMessage();
        if (rootMessage != null) {
            RaygunLogger.d(rootMessage);
            if (rootMessage.contains(IDENTIFIER)) {
                addCustomProperties(raygunMessage);
                return raygunMessage;
            }
        }
        RaygunErrorStackTraceLineMessage[] stackTrace = raygunMessage.getDetails()
            .getError().getStackTrace();
        if (stackTrace != null) {
            for (RaygunErrorStackTraceLineMessage message : stackTrace) {
                String className = message.getClassName();
                RaygunLogger.d(className);
                if (className.contains(IDENTIFIER)) {
                    addCustomProperties(raygunMessage);
                    return raygunMessage;
                }
            }
        }
        //check for inner error message
        RaygunErrorStackTraceLineMessage[] innerStackTrace = null;
        try {
            innerStackTrace = raygunMessage.getDetails().getError().getInnerError().getStackTrace();
            if (innerStackTrace != null) {
                for (RaygunErrorStackTraceLineMessage message : stackTrace) {
                    String className = message.getClassName();
                    RaygunLogger.w(className);
                    if (className.contains(IDENTIFIER)) {
                        addCustomProperties(raygunMessage);
                        return raygunMessage;
                    }
                }
            }
        } catch (NullPointerException ex) {
        }
        return null;
    }

    private void addCustomProperties(RaygunMessage raygunMessage) {
        try {
            RaygunMessageDetails details = raygunMessage.getDetails();

            Map<String, String> map = new HashMap<>();

            MerchantPreferences preferences = MidtransSDK.getInstance()
                .getMerchantData().getPreference();

            if (preferences != null) {
                map.put(UiKitConstants.KEY_TRACKING_MERCHANT_NAME,
                    preferences.getDisplayName());
            }
            String[] appInfo = DeviceUtils
                .getApplicationName(activity);
            map.put(UiKitConstants.KEY_TRACKING_HOST_APP, appInfo[0]);
            map.put(UiKitConstants.KEY_TRACKING_HOST_APP_VERSION, appInfo[1]);
            map.put(UiKitConstants.KEY_TRACKING_DEVICE_ID,
                SdkUtil.getDeviceId(activity));
            map.put(UiKitConstants.KEY_TRACKING_LANGUAGE,
                Locale.getDefault().getLanguage());
            map.put(UiKitConstants.KEY_TRACKING_DEVICE_MODEL, Build.MODEL);
            map.put(UiKitConstants.KEY_TRACKING_DEVICE_TYPE, Build.BRAND);
            map.put(UiKitConstants.KEY_TRACKING_TIME_STAMP,
                String.valueOf(System.currentTimeMillis()));
            map.put(UiKitConstants.KEY_TRACKING_NETWORK,
                DeviceUtils.getConnectivityType(activity));
            map.put(UiKitConstants.KEY_TRACKING_OS_VERSION,
                String.valueOf(Build.VERSION.SDK_INT));
            map.put(UiKitConstants.KEY_TRACKING_PLATFORM,
                UiKitConstants.VALUE_TRACKING_PLATFORM);
            map.put(UiKitConstants.KEY_TRACKING_SCREEN_SIZE,
                DeviceUtils.getDisplaySize(activity));
            map.put(UiKitConstants.KEY_TRACKING_SDK_VERSION,
                BuildConfig.VERSION_NAME);
            map.put(UiKitConstants.KEY_TRACKING_CPU_USAGE,
                DeviceUtils.getTotalCpuUsage());
            map.put(UiKitConstants.KEY_TRACKING_MEMORY_USAGE,
                DeviceUtils.getMemoryUsage());
            map.put(UiKitConstants.KEY_TRACKING_ENVIRONMENT,
                BuildConfig.FLAVOR);

            details.setUserCustomData(map);

        } catch (Exception e) {
            Logger.d(TAG, "raygun:" + e.getMessage());
        }
    }
}
