package com.midtrans.sdk.corekit.core;

import android.content.Context;

import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 9/2/16.
 */
public abstract class BaseSdkBuilder<T> {
    private static final String TAG = "BaseSdkBuilder";
    protected String clientKey = null;
    protected Context context = null;
    protected boolean enableLog = false;
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
     * controls the log of sdk. Log can help you to debug application. set false to disable log of
     * sdk, by default logs are on.
     *
     * @param enableLog is log enabled
     * @return object of SdkCoreFlowBuilder
     */
    public abstract T enableLog(boolean enableLog);

    /**
     * This method will start payment flow if you have set useUi field to true.
     *
     * @return it returns fully initialized object of midtrans sdk.
     */
    public MidtransSDK buildSDK() {
        if (MidtransSDK.getInstance() != null) {
            return MidtransSDK.getInstance();
        }

        if (MidtransSDK.getInstance() == null && isValidData()) {
            MidtransSDK midtransSDK = MidtransSDK.delegateInstance(this);
            return midtransSDK;

        } else {
            Logger.e("already performing an transaction");
        }
        return null;
    }

    public boolean isValidData() {
        if (merchantServerUrl == null || clientKey == null || context == null) {
            Logger.e(TAG, "invalid data supplied to sdk");
            return false;
        }
        return true;
    }
}
