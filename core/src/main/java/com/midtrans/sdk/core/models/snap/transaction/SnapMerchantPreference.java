package com.midtrans.sdk.core.models.snap.transaction;

/**
 * Created by rakawm on 10/21/16.
 */

public class SnapMerchantPreference {

    public final String displayName;
    public final String finishUrl;
    public final String errorUrl;
    public final String pendingUrl;
    public final String logoUrl;
    public final String colorScheme;
    public final String colorSchemeUrl;
    public final String locale;

    public SnapMerchantPreference(String displayName,
                                  String finishUrl,
                                  String errorUrl,
                                  String pendingUrl,
                                  String logoUrl,
                                  String colorScheme,
                                  String colorSchemeUrl,
                                  String locale) {
        this.displayName = displayName;
        this.finishUrl = finishUrl;
        this.errorUrl = errorUrl;
        this.pendingUrl = pendingUrl;
        this.logoUrl = logoUrl;
        this.colorScheme = colorScheme;
        this.colorSchemeUrl = colorSchemeUrl;
        this.locale = locale;
    }
}
