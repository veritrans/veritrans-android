package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.callback.TransactionFinishedCallback;
import id.co.veritrans.sdk.coreflow.models.PaymentMethodsModel;

/**
 * Created by ziahaqi on 9/2/16.
 */
public abstract class BaseSdkBuilder<T> {
    private static final String TAG = "BaseSdkBuilder";
    protected String clientKey = null;
    protected Context context = null;
    protected boolean enableLog = true;
    protected String merchantServerUrl = null;
    protected String colorTheme = null;
    protected int colorThemeResourceId = 0;
    protected String merchantName = null;
    protected ISdkFlow sdkFlow;
    protected String defaultText;
    protected String boldText;
    protected String semiBoldText;
    protected ArrayList<PaymentMethodsModel> selectedPaymentMethods;
    protected IScanner externalScanner;
    protected TransactionFinishedCallback transactionFinishedCallback;


    public abstract T setDefaultText(String defaultText);

    public abstract T setBoldText(String boldText);

    public abstract T setSemiBoldText(String semiBoldText);

    public abstract T setSelectedPaymentMethods(ArrayList<PaymentMethodsModel> selectedPaymentMethods);

    /**
     * controls the log of sdk. Log can help you to debug application.
     * set false to disable log of sdk, by default logs are on.
     *
     * @param enableLog     is log enabled
     * @return object of SdkCoreFlowBuilder
     */
    public abstract T enableLog(boolean enableLog);

    /**
     * This method will start payment flow if you have set useUi field to true.
     *
     * @return it returns fully initialized object of veritrans sdk.
     */
    public VeritransSDK buildSDK() {
        if(VeritransSDK.getVeritransSDK() != null){
            return VeritransSDK.getVeritransSDK();
        }

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
            Logger.e(TAG, "invalid data supplied to sdk");
            return false;
        }
        return true;
    }
}
