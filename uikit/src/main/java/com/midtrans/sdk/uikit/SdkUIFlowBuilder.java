package com.midtrans.sdk.uikit;

import android.content.Context;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.BaseSdkBuilder;
import com.midtrans.sdk.corekit.core.IScanner;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public class SdkUIFlowBuilder extends BaseSdkBuilder<SdkUIFlowBuilder> {
    /**
     * It  will initialize an data required to sdk.
     *
     * @param context application context
     */


    private SdkUIFlowBuilder(Context context, String clientKey, String merchantServerUrl, TransactionFinishedCallback callback) {
        this.context = context;
        this.clientKey = clientKey;
        this.merchantServerUrl = merchantServerUrl;
        this.transactionFinishedCallback = callback;
        this.flow = UI_FLOW;
        this.sdkFlow = new UIFlow();
    }

    public static SdkUIFlowBuilder init(Context context, String clientKey, String merchantServerUrl, TransactionFinishedCallback callback) {
        return new SdkUIFlowBuilder(context, clientKey, merchantServerUrl, callback);

    }

    public SdkUIFlowBuilder setExternalScanner(IScanner externalScanner) {
        this.externalScanner = externalScanner;
        return this;
    }

    public SdkUIFlowBuilder setBoldText(String boldText) {
        this.boldText = boldText;
        return this;
    }

    public SdkUIFlowBuilder setSemiBoldText(String semiBoldText) {
        this.semiBoldText = semiBoldText;
        return this;
    }

    public SdkUIFlowBuilder setDefaultText(String defaultText) {
        this.defaultText = defaultText;
        return this;
    }

    @Override
    public SdkUIFlowBuilder enableLog(boolean enableLog) {
        this.enableLog = enableLog;
        return this;
    }

    public SdkUIFlowBuilder setSelectedPaymentMethods(ArrayList<PaymentMethodsModel> selectedPaymentMethods) {
        this.selectedPaymentMethods = selectedPaymentMethods;
        return this;
    }

    public SdkUIFlowBuilder useBuiltInTokenStorage(boolean enableBuiltInTokenStorage) {
        this.enableBuiltInTokenStorage = enableBuiltInTokenStorage;
        return this;
    }

    public SdkUIFlowBuilder setUIkitCustomSetting(UIKitCustomSetting setting) {
        this.UIKitCustomSetting = setting;
        return this;
    }

    public SdkUIFlowBuilder setColorTheme(CustomColorTheme customColorTheme) {
        this.colorTheme = customColorTheme;
        return this;
    }
}
