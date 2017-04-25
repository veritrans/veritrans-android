package com.midtrans.sdk.analytics.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rakawm on 4/21/17.
 */

public class Properties {
    @SerializedName("distinct_id")
    private String distinctId;

    private String token;
    @SerializedName("response time")
    private long responseTime;
    @SerializedName("error message")
    private String message;
    @SerializedName("platform")
    private String platform;
    @SerializedName("os version")
    private String osVersion;
    @SerializedName("merchant")
    private String merchant;
    @SerializedName("sdk version")
    private String version;
    @SerializedName("device id")
    private String deviceId;
    @SerializedName("flow")
    private String flow;
    @SerializedName("device type")
    private String deviceType;
    @SerializedName("time stamp")
    private String timeStamp;

    @SerializedName("card mode")
    private String cardPaymentMode;

    public String getDistinctId() {
        return distinctId;
    }

    public void setDistinctId(String distinctId) {
        this.distinctId = distinctId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCardPaymentMode() {
        return cardPaymentMode;
    }

    public void setCardPaymentMode(String cardPaymentMode) {
        this.cardPaymentMode = cardPaymentMode;
    }
}
