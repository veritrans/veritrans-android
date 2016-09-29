package com.midtrans.sdk.corekit.core;

import com.google.gson.Gson;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Base64;

import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.analytics.MixpanelApi;
import com.midtrans.sdk.corekit.analytics.MixpanelEvent;
import com.midtrans.sdk.corekit.analytics.MixpanelProperties;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * @author rakawm
 */
public class MixpanelAnalyticsManager {
    // Platform properties
    private static final String PLATFORM = "Android";
    /**
     * Track event for mixpanel.
     *
     * @param event Mixpanel parameter.
     */


    private MixpanelApi mixpanelApi;

    public MixpanelAnalyticsManager(@NonNull MixpanelApi mixpanelApi) {
        this.mixpanelApi = mixpanelApi;
    }

    public void setMixpanelApi(MixpanelApi mixpanelApi) {
        this.mixpanelApi = mixpanelApi;
    }

    void trackEvent(MixpanelEvent event) {

        if (mixpanelApi != null) {
            Gson gson = new Gson();
            String eventObject = gson.toJson(event);
            String data = Base64.encodeToString(eventObject.getBytes(), Base64.DEFAULT);
            mixpanelApi.trackEvent(data, new Callback<Integer>() {
                @Override
                public void success(Integer integer, Response response) {
                    Logger.i("Response: " + Integer.toString(integer));

                }

                @Override
                public void failure(RetrofitError error) {
                    Logger.e("Response>error: " + error.getMessage());

                }
            });
        } else {
            Logger.e("No network connection");
        }
    }

    // Mixpanel event tracker
    public void trackMixpanel(String eventName, String paymentType, String bankType, long responseTime) {
        MixpanelEvent event = new MixpanelEvent();
        event.setEvent(eventName);

        MixpanelProperties properties = new MixpanelProperties();
        properties.setOsVersion(Build.VERSION.RELEASE);
        properties.setVersion(BuildConfig.VERSION_NAME);
        properties.setPlatform(PLATFORM);
        properties.setDeviceId(SdkUtil.getDeviceId(MidtransSDK.getInstance().getContext()));
        properties.setToken(BuildConfig.MIXPANEL_TOKEN);
        properties.setMerchant(MidtransSDK.getInstance() != null &&
                MidtransSDK.getInstance().getMerchantName() != null ?
                MidtransSDK.getInstance().getMerchantName() :
                MidtransSDK.getInstance().getClientKey());
        properties.setPaymentType(paymentType);
        if (bankType != null && !bankType.equals("")) {
            properties.setBank(bankType);
        }
        properties.setResponseTime(responseTime);

        event.setProperties(properties);

        trackEvent(event);
    }

    public void trackMixpanel(String eventName, String paymentType, long responseTime) {
        MixpanelEvent event = new MixpanelEvent();
        event.setEvent(eventName);

        MixpanelProperties properties = new MixpanelProperties();
        properties.setOsVersion(Build.VERSION.RELEASE);
        properties.setVersion(BuildConfig.VERSION_NAME);
        properties.setPlatform(PLATFORM);
        properties.setDeviceId(SdkUtil.getDeviceId(MidtransSDK.getInstance().getContext()));
        properties.setToken(BuildConfig.MIXPANEL_TOKEN);
        properties.setMerchant(MidtransSDK.getInstance() != null &&
                MidtransSDK.getInstance().getMerchantName() != null ?
                MidtransSDK.getInstance().getMerchantName() :
                MidtransSDK.getInstance().getClientKey());
        properties.setPaymentType(paymentType);
        properties.setResponseTime(responseTime);

        event.setProperties(properties);

        trackEvent(event);
    }

    public void trackMixpanel(String eventName, String paymentType, long responseTime, String errorMessage) {
        MixpanelEvent event = new MixpanelEvent();
        event.setEvent(eventName);

        MixpanelProperties properties = new MixpanelProperties();
        properties.setOsVersion(Build.VERSION.RELEASE);
        properties.setVersion(BuildConfig.VERSION_NAME);
        properties.setPlatform(PLATFORM);
        properties.setDeviceId(SdkUtil.getDeviceId(MidtransSDK.getInstance().getContext()));
        properties.setToken(BuildConfig.MIXPANEL_TOKEN);
        properties.setPaymentType(paymentType);
        properties.setMerchant(MidtransSDK.getInstance() != null &&
                MidtransSDK.getInstance().getMerchantName() != null ?
                MidtransSDK.getInstance().getMerchantName() :
                MidtransSDK.getInstance().getClientKey());
        properties.setResponseTime(responseTime);
        properties.setMessage(errorMessage);

        event.setProperties(properties);

        trackEvent(event);
    }

    public void trackMixpanel(String eventName, String paymentType, String bank, long responseTime, String errorMessage) {
        MixpanelEvent event = new MixpanelEvent();
        event.setEvent(eventName);

        MixpanelProperties properties = new MixpanelProperties();
        properties.setOsVersion(Build.VERSION.RELEASE);
        properties.setVersion(BuildConfig.VERSION_NAME);
        properties.setPlatform(PLATFORM);
        properties.setDeviceId(SdkUtil.getDeviceId(MidtransSDK.getInstance().getContext()));
        properties.setToken(BuildConfig.MIXPANEL_TOKEN);
        properties.setPaymentType(paymentType);
        properties.setBank(bank);
        properties.setMerchant(MidtransSDK.getInstance() != null &&
                MidtransSDK.getInstance().getMerchantName() != null ?
                MidtransSDK.getInstance().getMerchantName() :
                MidtransSDK.getInstance().getClientKey());
        properties.setResponseTime(responseTime);
        properties.setMessage(errorMessage);

        event.setProperties(properties);

        trackEvent(event);
    }
}
