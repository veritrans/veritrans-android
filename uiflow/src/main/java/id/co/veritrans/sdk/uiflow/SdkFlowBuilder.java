package id.co.veritrans.sdk.uiflow;

import android.content.Context;

import id.co.veritrans.sdk.coreflow.core.*;
import id.co.veritrans.sdk.coreflow.core.ISdkFlow;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public class SdkFlowBuilder extends SdkCoreFlowBuilder {

    /**
     * It  will initialize an data required to sdk.
     *
     * @param context           application context
     * @param clientKey
     * @param merchantServerUrl
     */
    public SdkFlowBuilder(Context context, String clientKey, String merchantServerUrl) {
        super(context, clientKey, merchantServerUrl);

    }



    public SdkFlowBuilder setUIFlow(ISdkFlow sdkFlow){
        this.sdkFlow = sdkFlow;
        return this;
    }

    public SdkFlowBuilder setExternalScanner(IScanner externalScanner){
        this.externalScanner = externalScanner;
        return this;
    }

    @Override
    public SdkFlowBuilder setBoldText(String boldText) {
        this.boldText = boldText;
        return this;
    }

    @Override
    public SdkFlowBuilder setColorTheme(String colorTheme) {
        this.colorTheme = colorTheme;
        return this;
    }

    @Override
    public SdkFlowBuilder setSemiBoldText(String semiBoldText) {
        this.semiBoldText = semiBoldText;
        return this;
    }

    @Override
    public SdkFlowBuilder setDefaultText(String defaultText) {
        this.defaultText = defaultText;
        return this;
    }

    @Override
    public SdkFlowBuilder enableLog(boolean enableLog) {
       this.enableLog = enableLog;
        return this;
    }
}
