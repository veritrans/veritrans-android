package com.midtrans.sdk.analytics.environment;

/**
 * Created by rakawm on 4/21/17.
 */

public class ProductionAnalyticsEnvironment extends BaseAnalyticsEnvironment {
    private static final String MIXPANEL_TOKEN = "0269722c477a0e085fde32e0248c6003";

    @Override
    public String getMixpanelToken() {
        return MIXPANEL_TOKEN;
    }
}
