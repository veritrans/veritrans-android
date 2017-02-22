package com.midtrans.sdk.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.midtrans.sdk.core.Environment;
import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenRequest;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;
import com.midtrans.sdk.ui.models.TransactionOption;
import com.midtrans.sdk.ui.views.activities.TransactionActivity;

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
    private TransactionOption transactionOption;
    private String merchantLogoUrl;
    private String merchantName;

    private MidtransUi(Builder builder) {
        this.context = builder.context;
        this.clientKey = builder.clientKey;
        this.environment = builder.environment;
        this.customSetting = builder.customSetting;
        this.enableLog = builder.enableLog;
        instance = this;
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
    public void runUiSdk(@NonNull Context context, @NonNull String checkoutUrl, CheckoutTokenRequest checkoutTokenRequest, @NonNull SdkUiPaymentCallback callback) {
        this.checkoutUrl = checkoutUrl;
        this.checkoutTokenRequest = checkoutTokenRequest;
        Intent intent = new Intent(context, TransactionActivity.class);
        context.startActivity(intent);
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

    public void setTransactionOption(TransactionOption transactionOption) {
        this.transactionOption = transactionOption;
    }

    public void setMerchantLogoUrl(String merchantLogoUrl) {
        this.merchantLogoUrl = merchantLogoUrl;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }


    public static class Builder {
        private Context context;
        private boolean enableLog = true;
        private String clientKey;
        private Environment environment;
        private CustomSetting customSetting;

        public Builder with(Context context) {
            this.context = context.getApplicationContext();
            return this;
        }

        public Builder clientKey(String clientKey) {
            this.clientKey = clientKey;
            return this;
        }

        public Builder environment(Environment environment) {
            this.environment = environment;
            return this;
        }

        public Builder customSetting(CustomSetting customSetting) {
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
