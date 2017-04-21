package com.midtrans.sdk.analytics.environment;

/**
 * Created by rakawm on 4/21/17.
 */

public class SandboxAnalyticsEnvironment extends BaseAnalyticsEnvironment {
    private static final String MIXPANEL_TOKEN = "cc005b296ca4ce612fe3939177c668bb";

    @Override
    public String getMixpanelToken() {
        return MIXPANEL_TOKEN;
    }
}
