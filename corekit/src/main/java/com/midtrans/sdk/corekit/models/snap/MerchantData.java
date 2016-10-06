package com.midtrans.sdk.corekit.models.snap;

/**
 * @author rakawm
 */
public class MerchantData {
    private String displayName;
    private String clientKey;
    private String finishUrl;
    private String errorUrl;
    private String unFinishUrl;
    private String logoUrl;
    private String locale;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
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

    public String getUnFinishUrl() {
        return unFinishUrl;
    }

    public void setUnFinishUrl(String unFinishUrl) {
        this.unFinishUrl = unFinishUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
