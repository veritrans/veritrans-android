package com.midtrans.sdk.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.midtrans.sdk.core.Environment;
import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenRequest;
import com.midtrans.sdk.core.models.merchant.CustomerDetails;
import com.midtrans.sdk.core.utils.Logger;
import com.midtrans.sdk.ui.models.Transaction;
import com.midtrans.sdk.ui.themes.BaseColorTheme;
import com.midtrans.sdk.ui.themes.ColorTheme;
import com.midtrans.sdk.ui.themes.CustomColorTheme;
import com.midtrans.sdk.ui.utils.LocalDataHandler;
import com.midtrans.sdk.ui.views.transaction.TransactionActivity;

import java.util.UUID;

/**
 * Created by ziahaqi on 2/19/17.
 */

public class MidtransUi {
    private static MidtransUi instance;
    private Context context;
    private boolean enableLog = true;
    private String clientKey;
    private Environment environment;
    private CustomSetting customSetting;
    private String checkoutToken;
    private String checkoutUrl;
    private CheckoutTokenRequest checkoutTokenRequest;
    private Transaction transaction;
    private String merchantLogoUrl;
    private String merchantName;
    private boolean builtInTokenStorage = true;
    private BaseColorTheme colorTheme;

    private MidtransUi(Builder builder) {
        this.context = builder.context;
        this.clientKey = builder.clientKey;
        this.environment = builder.environment;
        this.customSetting = builder.customSetting == null ? new CustomSetting() : builder.customSetting;
        this.enableLog = builder.enableLog;
        instance = this;
        initDefaultValues();
    }

    private void initDefaultValues() {
        if(customSetting.colorTheme != null){
            this.colorTheme = customSetting.colorTheme;
        }

    }

    public static Builder builder() {
        return new Builder();
    }

    public static MidtransUi getInstance() {
        if (instance == null) {
            throw new RuntimeException("SDK is not initialized. Please initialize it using MidtransUi.Builder().");
        }
        return instance;
    }


    /**
     * @param context              Context
     * @param checkoutUrl          Merchant checkout URL
     * @param callback             PaymentCallback
     * @param checkoutTokenRequest CheckoutTokenRequest
     */
    public void runUiSdk(@NonNull Context context, @NonNull String checkoutUrl, @NonNull CheckoutTokenRequest checkoutTokenRequest, @NonNull MidtransUiCallback callback) {
        if (checkoutTokenRequest.customerDetails == null) {
            String message = "You must set an customerDetails. Please set customerDetails into CheckoutTokenRequest instance";
            RuntimeException runtimeException = new RuntimeException(message);
            Logger.error(message, runtimeException);
            throw runtimeException;
        }
        this.checkoutUrl = checkoutUrl;
        this.checkoutTokenRequest = checkoutTokenRequest;
        Intent intent = new Intent(context, TransactionActivity.class);
        context.startActivity(intent);
    }

    public CustomSetting getCustomSetting() {
        return customSetting;
    }

    public void setColorTheme(BaseColorTheme baseColorTheme) {
        if (this.colorTheme == null) {
            this.colorTheme = baseColorTheme;
        }
    }

    public BaseColorTheme getColorTheme() {
        return this.colorTheme;
    }


    public String getFontBold() {
        if (customSetting != null) {
            return customSetting.fontBold;
        }
        return null;
    }

    public String getFontDefault() {
        if (customSetting != null) {
            return customSetting.fontDefault;
        }
        return null;
    }

    public String getFontSemiBold() {
        if (customSetting != null) {
            return customSetting.fontSemiBold;
        }
        return null;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    public CheckoutTokenRequest getCheckoutTokenRequest() {
        return checkoutTokenRequest;
    }

    public void setCheckoutToken(String checkoutToken) {
        this.checkoutToken = checkoutToken;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public boolean isBuiltInTokenStorage() {
        return builtInTokenStorage;
    }

    public boolean isCreditCardNormalMode() {
        if (transaction != null && transaction.isCreditCardNormalMode()) {
            return true;
        }
        return false;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public String readCheckoutToken() {
        return this.transaction.getCheckoutToken();
    }


    public static class Builder {
        private Context context;
        private boolean enableLog = true;
        private String clientKey;
        private Environment environment;
        private CustomSetting customSetting;
        private CustomerDetails customerDetails;

        public Builder with(Context context) {
            this.context = context.getApplicationContext();
            return this;
        }

        public Builder setClientKey(String clientKey) {
            this.clientKey = clientKey;
            return this;
        }

        public Builder setEnvironment(Environment environment) {
            this.environment = environment;
            return this;
        }

        public Builder setCustomerDetails(CustomerDetails customerDetails) {
            this.customerDetails = customerDetails;
            return this;
        }

        public Builder setCustomSetting(CustomSetting customSetting) {
            this.customSetting = customSetting;
            return this;
        }


        public Builder enableLog(boolean enableLog) {
            this.enableLog = enableLog;
            return this;
        }

        public MidtransUi build() {

            new MidtransCore.Builder()
                    .enableLog(true)
                    .setEnvironment(this.environment)
                    .setClientKey(clientKey)
                    .build();


            return new MidtransUi(this);
        }
    }
}
