package com.midtrans.sdk.corekit.analytics;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class MixpanelProperties {
    @SerializedName("distinct_id")
    private String distinctId;
    private String token;
    @SerializedName("Response Time")
    private long responseTime;
    @SerializedName("Error Message")
    private String message;
    @SerializedName("Platform")
    private String platform;
    @SerializedName("OS Version")
    private String osVersion;
    @SerializedName("Merchant")
    private String merchant;
    @SerializedName("SDK Version")
    private String version;
    @SerializedName("Device ID")
    private String deviceId;
    @SerializedName("Payment Type")
    private String paymentType;
    @SerializedName("Bank")
    private String bank;

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

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
