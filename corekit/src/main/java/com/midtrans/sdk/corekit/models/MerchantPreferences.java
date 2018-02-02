package com.midtrans.sdk.corekit.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rakawm on 10/12/16.
 */

public class MerchantPreferences {
    @SerializedName("other_va_processor")
    private String otherVaProcessor;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("finish_url")
    private String finishUrl;
    @SerializedName("error_url")
    private String errorUrl;
    @SerializedName("pending_url")
    private String pendingUrl;
    @SerializedName("logo_url")
    private String logoUrl;
    @SerializedName("color_scheme")
    private String colorScheme;
    @SerializedName("color_scheme_url")
    private String colorSchemeUrl;
    private String locale;

    public String getOtherVaProcessor() {
        return otherVaProcessor;
    }

    public void setOtherVaProcessor(String otherVaProcessor) {
        this.otherVaProcessor = otherVaProcessor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFinishUrl() {
        return finishUrl;
    }

    public void setFinishUrl(String finishUrl) {
        this.finishUrl = finishUrl;
    }

    public String getErrorUrl() {
        return errorUrl;
    }

    public void setErrorUrl(String errorUrl) {
        this.errorUrl = errorUrl;
    }

    public String getPendingUrl() {
        return pendingUrl;
    }

    public void setPendingUrl(String pendingUrl) {
        this.pendingUrl = pendingUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(String colorScheme) {
        this.colorScheme = colorScheme;
    }

    public String getColorSchemeUrl() {
        return colorSchemeUrl;
    }

    public void setColorSchemeUrl(String colorSchemeUrl) {
        this.colorSchemeUrl = colorSchemeUrl;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
