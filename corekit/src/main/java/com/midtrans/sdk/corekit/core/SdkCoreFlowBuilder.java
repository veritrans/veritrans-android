package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Created by shivam on 10/20/15.
 * <p/>
 * helper class to create object of midtrans sdk. </p> Call its constructor with activity ,
 * client_key and sever_key. </p> You can also enable or disable using {@link #enableLog(boolean)}
 */
public class SdkCoreFlowBuilder extends BaseSdkBuilder<SdkCoreFlowBuilder> {

    protected SdkCoreFlowBuilder() {
        this.flow = CORE_FLOW;
    }

    /**
     * It  will initialize an data required to sdk.
     *
     * @param context application context
     */

    private SdkCoreFlowBuilder(@NonNull Context context, @NonNull String clientKey, @NonNull String merchantServerUrl) {
        this.context = context.getApplicationContext();
        this.clientKey = clientKey;
        this.merchantServerUrl = merchantServerUrl;
        this.flow = CORE_FLOW;
    }

    /**
     * this Sdk builder is deprecated, please use init() method instead
     *
     * @param context
     * @param clientKey
     * @param merchantServerUrl
     * @return
     */
    @Deprecated
    public static SdkCoreFlowBuilder init(@NonNull Context context, @NonNull String clientKey, @NonNull String merchantServerUrl) {
        return new SdkCoreFlowBuilder(context, clientKey, merchantServerUrl);
    }

    /**
     * this new Sdk builder for corekit sdk
     */
    public static SdkCoreFlowBuilder init() {
        return new SdkCoreFlowBuilder();
    }


    /**
     * controls the log of sdk. Log can help you to debug application. set false to disable log of
     * sdk, by default logs are on.
     *
     * @param enableLog is log enabled
     * @return object of SdkCoreFlowBuilder
     */
    public SdkCoreFlowBuilder enableLog(boolean enableLog) {
        this.enableLog = enableLog;
        return this;
    }

    public SdkCoreFlowBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public SdkCoreFlowBuilder setClientKey(String clientKey) {
        this.clientKey = clientKey;
        return this;
    }

    public SdkCoreFlowBuilder setMerchantBaseUrl(String merchantBaseUrl) {
        this.merchantServerUrl = merchantBaseUrl;
        return this;
    }
}