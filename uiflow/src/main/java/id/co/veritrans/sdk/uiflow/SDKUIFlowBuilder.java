package id.co.veritrans.sdk.uiflow;

import android.content.Context;

import id.co.veritrans.sdk.coreflow.core.*;
import id.co.veritrans.sdk.coreflow.core.SdkFlow;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public class SdkUIFlowBuilder extends SdkCoreFlowBuilder {

    /**
     * It  will initialize an data required to sdk.
     *
     * @param context           application context
     * @param clientKey
     * @param merchantServerUrl
     */
    public SdkUIFlowBuilder(Context context, String clientKey, String merchantServerUrl) {
        super(context, clientKey, merchantServerUrl);

    }



    public SdkUIFlowBuilder setUIFlow(SdkFlow sdkFlow){
        this.sdkFlow = sdkFlow;
        return this;
    }

    public SdkUIFlowBuilder setExternalScanner(IScanner externalScanner){
        this.externalScanner = externalScanner;
        return this;
    }

    @Override
    public SdkUIFlowBuilder setBoldText(String boldText) {
        this.boldText = boldText;
        return this;
    }

    @Override
    public SdkUIFlowBuilder setColorTheme(String colorTheme) {
        this.colorTheme = colorTheme;
        return this;
    }

    @Override
    public SdkUIFlowBuilder setSemiBoldText(String semiBoldText) {
        this.semiBoldText = semiBoldText;
        return this;
    }

    @Override
    public SdkUIFlowBuilder setDefaultText(String defaultText) {
        this.defaultText = defaultText;
        return this;
    }

    @Override
    public SdkUIFlowBuilder enableLog(boolean enableLog) {
       this.enableLog = enableLog;
        return this;
    }
}
