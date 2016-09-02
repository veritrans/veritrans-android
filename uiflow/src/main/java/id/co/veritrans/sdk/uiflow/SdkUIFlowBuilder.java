package id.co.veritrans.sdk.uiflow;

import android.content.Context;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.callback.TransactionFinishedCallback;
import id.co.veritrans.sdk.coreflow.core.BaseSdkBuilder;
import id.co.veritrans.sdk.coreflow.core.IScanner;
import id.co.veritrans.sdk.coreflow.core.ISdkFlow;
import id.co.veritrans.sdk.coreflow.core.SdkCoreFlowBuilder;
import id.co.veritrans.sdk.coreflow.models.PaymentMethodsModel;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public class SdkUIFlowBuilder extends BaseSdkBuilder<SdkUIFlowBuilder> {

    /**
     * It  will initialize an data required to sdk.
     *
     * @param context application context
     * @param clientKey
     * @param merchantServerUrl
     */


    private SdkUIFlowBuilder(Context context, String clientKey, String merchantServerUrl, TransactionFinishedCallback callback) {
        this.context = context;
        this.clientKey = clientKey;
        this.merchantServerUrl = merchantServerUrl;
        this.transactionFinishedCallback = callback;
        this.sdkFlow = new UIFlow();
    }

    public static SdkUIFlowBuilder init(Context context, String clientKey, String merchantServerUrl, TransactionFinishedCallback callback){
        return new SdkUIFlowBuilder(context, clientKey, merchantServerUrl, callback);

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

    public SdkUIFlowBuilder setSelectedPaymentMethods(ArrayList<PaymentMethodsModel> selectedPaymentMethods) {
        this.selectedPaymentMethods = selectedPaymentMethods;
        return this;
    }

}
