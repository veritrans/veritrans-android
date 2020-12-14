package com.midtrans.sdk.uikit;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.BaseSdkBuilder;
import com.midtrans.sdk.corekit.core.IScanner;
import com.midtrans.sdk.corekit.core.UIKitCustomSetting;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;

import java.util.ArrayList;
import java.util.Locale;

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

    private SdkUIFlowBuilder() {
        this.flow = UI_FLOW;
        this.sdkFlow = new UIFlow();
    }

    /**
     * create sdk builder
     *
     * @return SdkUIFlowBuilder
     */
    public static SdkUIFlowBuilder init() {
        return new SdkUIFlowBuilder();
    }

    /**
     * This Sdk builder has been deprecated since version 1.10, please use init() method instead
     *
     * @param context
     * @param clientKey
     * @param merchantServerUrl
     * @param callback
     * @return
     */
    @Deprecated
    public static SdkUIFlowBuilder init(Context context, String clientKey, String merchantServerUrl, TransactionFinishedCallback callback) {
        return new SdkUIFlowBuilder(context, clientKey, merchantServerUrl, callback);

    }

    public SdkUIFlowBuilder setClientKey(String clientKey) {
        this.clientKey = clientKey;
        return this;
    }

    public SdkUIFlowBuilder setContext(Context context) {
        this.context = context;
        return this;
    }

    public SdkUIFlowBuilder setTransactionFinishedCallback(TransactionFinishedCallback callback) {
        this.transactionFinishedCallback = callback;
        return this;
    }

    public SdkUIFlowBuilder setMerchantBaseUrl(String merchantBaseUrl) {
        this.merchantServerUrl = merchantBaseUrl;
        return this;
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public SdkUIFlowBuilder setLanguage(String languageCode) {
        Resources resources = context.getResources();
        Locale currentLocale = resources.getConfiguration().locale;
        Locale englishLocale = new Locale("en");
        Locale locale;
        if(!languageCode.equals("en") && !languageCode.equals("id")) {
            locale = englishLocale;
        } else {
            locale = new Locale(languageCode);
        }
        Locale.setDefault(currentLocale);
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        return this;
    }
}
