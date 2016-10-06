package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.support.annotation.NonNull;

import com.midtrans.sdk.corekit.models.PaymentMethodsModel;

import java.util.ArrayList;

/**
 * Created by shivam on 10/20/15.
 * <p/>
 * helper class to create object of midtrans sdk. </p> Call its constructor with activity ,
 * client_key and sever_key. </p> You can also enable or disable using {@link #enableLog(boolean)}
 */
public class SdkCoreFlowBuilder extends BaseSdkBuilder<SdkCoreFlowBuilder> {

    /**
     * It  will initialize an data required to sdk.
     *
     * @param context application context
     */
    private SdkCoreFlowBuilder(@NonNull Context context, @NonNull String clientKey, @NonNull String merchantServerUrl) {
        this.context = context.getApplicationContext();
        this.clientKey = clientKey;
        this.merchantServerUrl = merchantServerUrl;
    }

    public static SdkCoreFlowBuilder init(@NonNull Context context, @NonNull String clientKey, @NonNull String merchantServerUrl) {
        return new SdkCoreFlowBuilder(context, clientKey, merchantServerUrl);
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


    public SdkCoreFlowBuilder setDefaultText(String defaultText) {
        this.defaultText = defaultText;
        return this;
    }

    public SdkCoreFlowBuilder setBoldText(String boldText) {
        this.boldText = boldText;
        return this;
    }

    public SdkCoreFlowBuilder setSemiBoldText(String semiBoldText) {
        this.semiBoldText = semiBoldText;
        return this;
    }

    public SdkCoreFlowBuilder setSelectedPaymentMethods(ArrayList<PaymentMethodsModel> selectedPaymentMethods) {
        this.selectedPaymentMethods = selectedPaymentMethods;
        return this;
    }
}