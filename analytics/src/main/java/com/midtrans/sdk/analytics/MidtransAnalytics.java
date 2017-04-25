package com.midtrans.sdk.analytics;

import android.os.Build;

import com.midtrans.sdk.analytics.api.AnalyticsApiManager;
import com.midtrans.sdk.analytics.api.RestAdapter;
import com.midtrans.sdk.analytics.environment.BaseAnalyticsEnvironment;
import com.midtrans.sdk.analytics.environment.ProductionAnalyticsEnvironment;
import com.midtrans.sdk.analytics.environment.SandboxAnalyticsEnvironment;
import com.midtrans.sdk.analytics.models.Event;
import com.midtrans.sdk.analytics.models.Properties;
import com.midtrans.sdk.analytics.utils.Logger;

import java.sql.Timestamp;

/**
 * Created by rakawm on 4/21/17.
 */

public class MidtransAnalytics {
    public static final String CARD_MODE_ONE_CLICK = "one click";
    public static final String CARD_MODE_TWO_CLICK = "two click";
    public static final String CARD_MODE_NORMAL = "normal";
    public static final String DEVICE_TYPE_TABLET = "tablet";
    public static final String DEVICE_TYPE_PHONE = "phone";
    private static final String PLATFORM = "Android";
    private static final String FLOW = "UI";
    private static MidtransAnalytics instance;
    private final String sdkVersion;
    private final String deviceId;
    private final String deviceType;
    private final BaseAnalyticsEnvironment environment;
    private final AnalyticsApiManager analyticsApiManager;
    private String merchantName;

    private MidtransAnalytics(Builder builder) {
        this.sdkVersion = builder.sdkVersion;
        this.merchantName = builder.merchantName;
        this.deviceId = builder.deviceId;
        this.deviceType = builder.deviceType;
        this.environment = buildAnalyticsEnvironment(builder.environment);
        this.analyticsApiManager = buildAnalyticsApiManager(builder.environment);

        instance = this;
    }

    public static MidtransAnalytics getInstance() {
        if (instance == null) {
            throw new RuntimeException("Analytics SDK is not initialized. Please initialize it using MidtransAnalytics.Builder().");
        }

        return instance;
    }

    private AnalyticsApiManager buildAnalyticsApiManager(AnalyticsEnvironment environment) {
        return new AnalyticsApiManager(
                buildAnalyticsEnvironment(environment),
                new RestAdapter(environment).getAnalyticsApi()
        );
    }

    private BaseAnalyticsEnvironment buildAnalyticsEnvironment(AnalyticsEnvironment environment) {
        switch (environment) {
            case SANDBOX:
                return new SandboxAnalyticsEnvironment();
            case PRODUCTION:
                return new ProductionAnalyticsEnvironment();
            case STAGING:
                return new SandboxAnalyticsEnvironment();
            default:
                return new SandboxAnalyticsEnvironment();
        }
    }

    /**
     * Set merchant name.
     *
     * @param merchantName merchant name.
     */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /**
     * Send basic analytics event.
     *
     * @param distinctId distinction identifier.
     * @param eventName  event name.
     */
    public void trackEvent(String distinctId, String eventName) {
        Event event = new Event();
        event.setEvent(eventName);

        Properties properties = buildProperties();
        properties.setDistinctId(distinctId);
        event.setProperties(properties);

        analyticsApiManager.trackEvent(event);
    }

    /**
     * Send analytics event with added response time.
     *
     * @param distinctId   distinction identifier.
     * @param eventName    event name.
     * @param responseTime response time.
     */
    public void trackEvent(String distinctId, String eventName, long responseTime) {
        Event event = new Event();
        event.setEvent(eventName);

        Properties properties = buildProperties();
        properties.setDistinctId(distinctId);
        properties.setResponseTime(responseTime);
        event.setProperties(properties);

        analyticsApiManager.trackEvent(event);
    }

    /**
     * Send analytics event for credit card specific event.
     *
     * @param distinctId      distinction identifier.
     * @param eventName       event name.
     * @param cardPaymentMode credit card payment mode.
     */
    public void trackEvent(String distinctId, String eventName, String cardPaymentMode) {
        Event event = new Event();
        event.setEvent(eventName);

        Properties properties = buildProperties();
        properties.setDistinctId(distinctId);
        properties.setCardPaymentMode(cardPaymentMode);
        event.setProperties(properties);

        analyticsApiManager.trackEvent(event);
    }

    private Properties buildProperties() {
        Properties properties = new Properties();
        properties.setMerchant(merchantName);
        properties.setDeviceId(deviceId);
        properties.setVersion(sdkVersion);
        properties.setToken(environment.getMixpanelToken());
        properties.setOsVersion(Build.VERSION.RELEASE);
        properties.setPlatform(PLATFORM);
        properties.setFlow(FLOW);
        properties.setDeviceType(deviceType);
        properties.setTimeStamp(String.valueOf(new Timestamp(System.currentTimeMillis())));

        return properties;
    }

    public static class Builder {
        public boolean logEnabled = true;
        private String sdkVersion;
        private String deviceId;
        private String merchantName;
        private String deviceType;
        private AnalyticsEnvironment environment;

        /**
         * Controls the log of SDK. Log can help you to debug application. Set false to disable log
         * of SDK, by default logs are on.
         *
         * @param logEnabled flag to enable log.
         * @return {@link Builder} instance.
         */
        public Builder setLogEnabled(boolean logEnabled) {
            this.logEnabled = logEnabled;
            return this;
        }

        /**
         * Set Midtrans SDK version.
         *
         * @param sdkVersion Midtrans SDK version.
         * @return {@link Builder} instance.
         */
        public Builder setSdkVersion(String sdkVersion) {
            this.sdkVersion = sdkVersion;
            return this;
        }

        /**
         * Set Merchant name.
         *
         * @param merchantName merchant name.
         * @return {@link Builder} instance.
         */
        public Builder setMerchantName(String merchantName) {
            this.merchantName = merchantName;
            return this;
        }

        /**
         * Set Device specific identifier.
         *
         * @param deviceId device specific identifier.
         * @return {@link Builder} instance.
         */
        public Builder setDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        /**
         * Set device type.
         *
         * @param deviceType device whether it's tablet or phone.
         * @return {@link Builder} instance.
         */
        public Builder setDeviceType(String deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        /**
         * Set analytics environment.
         *
         * @param environment environment.
         * @return {@link Builder} instance.
         */
        public Builder setEnvironment(AnalyticsEnvironment environment) {
            this.environment = environment;
            return this;
        }

        /**
         * Build Midtrans Core SDK.
         */
        public MidtransAnalytics build() {
            if (sdkVersion == null || sdkVersion.equalsIgnoreCase("")) {
                String message = "SDK version cannot be null or empty. Please pass the SDK version using setSdkVersion()";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
                throw runtimeException;
            }

            if (merchantName == null || merchantName.equalsIgnoreCase("")) {
                String message = "Merchant name cannot be null or empty. Please pass the merchant name using setMerchantName()";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
                throw runtimeException;
            }

            if (deviceId == null || deviceId.equalsIgnoreCase("")) {
                String message = "Device id cannot be null or empty. Please pass the device id using setDeviceId()";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
                throw runtimeException;
            }

            if (sdkVersion == null || sdkVersion.equalsIgnoreCase("")) {
                String message = "SDK Version cannot be null or empty. Please pass the client key using setSdkVersion()";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
                throw runtimeException;
            }

            if (deviceType == null || deviceType.equalsIgnoreCase("")) {
                String message = "Device type cannot be null or empty. Please pass the device type using setDeviceType()";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
                throw runtimeException;
            }

            if (environment == null) {
                String message = "You must set an environment. Please use setEnvironment(Environment.$ENV)";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
                throw runtimeException;
            }

            return new MidtransAnalytics(this);
        }
    }
}
