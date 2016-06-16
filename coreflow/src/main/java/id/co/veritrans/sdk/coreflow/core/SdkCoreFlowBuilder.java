package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.models.PaymentMethodsModel;

/**
 * Created by shivam on 10/20/15.
 * <p/>
 * helper class to create object of veritrans sdk.
 * </p>
 * Call its constructor with activity , client_key and sever_key.
 *</p>
 * You can also enable or disable using {@link #enableLog(boolean)}
 */
public class SdkCoreFlowBuilder {

    /*protected String serverKey = null;*/
    protected String clientKey = null;
    protected Context context = null;
    protected boolean enableLog = true;
    protected String merchantServerUrl = null;
    protected String colorTheme = null;
    protected String merchantName = null;
    protected ISdkFlow sdkFlow;
    protected String defaultText;
    protected String boldText;
    protected String semiBoldText;
    protected ArrayList<PaymentMethodsModel> selectedPaymentMethods;
    protected IScanner externalScanner;

    /**
     * It  will initialize an data required to sdk.
     *
     * @param context   application context

     */
    public SdkCoreFlowBuilder(Context context, String clientKey, String merchantServerUrl) {
            this.context = context.getApplicationContext();
            this.clientKey = clientKey;
            this.merchantServerUrl = merchantServerUrl;
    }

    /**
     * controls the log of sdk. Log can help you to debug application.
     * set false to disable log of sdk, by default logs are on.
     *
     * @param enableLog     is log enabled
     * @return object of SdkCoreFlowBuilder
     */
    public SdkCoreFlowBuilder enableLog(boolean enableLog) {
        this.enableLog = enableLog;
        return this;
    }

    /**
     * Set color theme using formatted hex
     *
     * @param colorTheme formatted hex color code
     * @return SdkCoreFlowBuilder instance.
     */
    public SdkCoreFlowBuilder setColorTheme(String colorTheme) {
        this.colorTheme = colorTheme;
        return this;
    }


    /**
     * Set merchant name.
     *
     * @param merchantName merchant name
     * @return SdkCoreFlowBuilder instance
     */
    public SdkCoreFlowBuilder setMerchantName(String merchantName) {
        this.merchantName = merchantName;
        return this;
    }

    /**
     * This method will start payment flow if you have set useUi field to true.
     *
     * @return it returns fully initialized object of veritrans sdk.
     */
    public VeritransSDK buildSDK() {
        if (VeritransSDK.getVeritransSDK() == null && isValidData()) {
            VeritransSDK veritransSDK = VeritransSDK.getInstance(this);
            return veritransSDK;

        } else {
            Logger.e("already performing an transaction");
        }
        return null;
    }

    public boolean isValidData() {
        if(merchantServerUrl == null || clientKey == null || context == null){
            Logger.e("invalid data supplied to sdk");
            return false;
        }
        return true;
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