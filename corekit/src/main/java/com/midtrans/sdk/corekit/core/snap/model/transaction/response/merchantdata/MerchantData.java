package com.midtrans.sdk.corekit.core.snap.model.transaction.response.merchantdata;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MerchantData implements Serializable {
    private MerchantPreferences preference;
    @SerializedName("client_key")
    private String clientKey;
    @SerializedName("enabled_principles")
    private List<String> enabledPrinciples;
    @SerializedName("point_banks")
    private ArrayList<String> pointBanks;
    @SerializedName("merchant_id")
    private String merchantId;
    @SerializedName("acquiring_banks")
    private List<String> acquiringBanks;

    public MerchantPreferences getPreference() {
        return preference;
    }

    public void setPreference(MerchantPreferences preference) {
        this.preference = preference;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public List<String> getEnabledPrinciples() {
        return enabledPrinciples;
    }

    public void setEnabledPrinciples(List<String> enabledPrinciples) {
        this.enabledPrinciples = enabledPrinciples;
    }

    public ArrayList<String> getPointBanks() {
        return pointBanks;
    }

    public void setPointBanks(ArrayList<String> pointBanks) {
        this.pointBanks = pointBanks;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public List<String> getAcquiringBanks() {
        return acquiringBanks;
    }

    public void setAcquiringBanks(List<String> acquiringBanks) {
        this.acquiringBanks = acquiringBanks;
    }
}