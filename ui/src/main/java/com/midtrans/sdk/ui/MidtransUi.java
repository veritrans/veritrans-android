package com.midtrans.sdk.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.midtrans.sdk.core.Environment;
import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenRequest;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;
import com.midtrans.sdk.core.utils.Logger;
import com.midtrans.sdk.ui.themes.BaseColorTheme;
import com.midtrans.sdk.ui.themes.CustomColorTheme;
import com.midtrans.sdk.ui.views.transaction.TransactionActivity;

/**
 * Created by ziahaqi on 2/19/17.
 */

public class MidtransUi {
    private static MidtransUi instance;

    private boolean enableLog;
    private String clientKey;
    private Environment environment;
    private boolean tokenStorageEnabled;

    private BaseColorTheme colorTheme;
    private String defaultFontPath;
    private String semiBoldFontPath;
    private String boldFontPath;

    private CustomSetting customSetting = new CustomSetting();

    private MidtransUiCallback paymentCallback;

    private String checkoutUrl;
    private CheckoutTokenRequest checkoutTokenRequest;
    private String checkoutToken;

    private SnapTransaction transaction;

    private MidtransUi(Builder builder) {
        this.clientKey = builder.clientKey;
        this.environment = builder.environment;
        this.enableLog = builder.enableLog;
        this.defaultFontPath = builder.defaultFontPath;
        this.semiBoldFontPath = builder.semiBoldFontPath;
        this.boldFontPath = builder.boldFontPath;
        this.colorTheme = builder.customColorTheme;
        this.tokenStorageEnabled = builder.tokenStorageEnabled;
        // Set static instance
        instance = this;
        // Init MidtransCore
        initMidtransCore();
    }

    /**
     * Get current SDK instance if already initialized.
     *
     * @return {@link MidtransUi} instance.
     */
    public static MidtransUi getInstance() {
        if (instance == null) {
            throw new RuntimeException("SDK is not initialized. Please initialize it using MidtransUi.Builder().");
        }
        return instance;
    }

    /**
     * @param activity             Activity.
     * @param checkoutUrl          Merchant checkout URL
     * @param checkoutTokenRequest CheckoutTokenRequest
     * @param callback             Callback to be called after transaction was finished.
     */
    public void runUiSdk(@NonNull Activity activity, @NonNull String checkoutUrl, @NonNull CheckoutTokenRequest checkoutTokenRequest, @NonNull MidtransUiCallback callback) {
        if (checkoutTokenRequest.customerDetails == null) {
            String message = "You must set an customerDetails. Please set customerDetails into CheckoutTokenRequest instance";
            RuntimeException runtimeException = new RuntimeException(message);
            Logger.error(message, runtimeException);
            throw runtimeException;
        }
        this.checkoutUrl = checkoutUrl;
        this.checkoutTokenRequest = checkoutTokenRequest;
        this.paymentCallback = callback;

        Intent intent = new Intent(activity, TransactionActivity.class);
        activity.startActivity(intent);
    }

    /**
     * @param activity      Activity.
     * @param checkoutToken Checkout token.
     * @param callback      Callback to be called after transaction was finished.
     */
    public void runUiSdk(@NonNull Activity activity, String checkoutToken, @NonNull MidtransUiCallback callback) {
        if (checkoutTokenRequest.customerDetails == null) {
            String message = "You must set an customerDetails. Please set customerDetails into CheckoutTokenRequest instance";
            RuntimeException runtimeException = new RuntimeException(message);
            Logger.error(message, runtimeException);
            throw runtimeException;
        }
        this.checkoutUrl = checkoutToken;
        this.paymentCallback = callback;
        this.checkoutToken = checkoutToken;

        Intent intent = new Intent(activity, TransactionActivity.class);
        activity.startActivity(intent);
    }

    /**
     * Get custom settings for SDK UI.
     *
     * @return custom settings.
     */
    public CustomSetting getCustomSetting() {
        return customSetting;
    }

    /**
     * Set custom settings for SDK UI.
     *
     * @param customSetting custom setting.
     */
    public void setCustomSetting(CustomSetting customSetting) {
        this.customSetting = customSetting;
    }

    /**
     * Get color theme that used in entire UI SDK.
     *
     * @return color theme.
     */
    public BaseColorTheme getColorTheme() {
        return this.colorTheme;
    }

    /**
     * Set color theme to be used in entire UI SDK.
     *
     * @param baseColorTheme color theme.
     */
    public void setColorTheme(BaseColorTheme baseColorTheme) {
        this.colorTheme = baseColorTheme;
    }

    /**
     * Get default font path from Assets directory.
     *
     * @return font path for default style font.
     */
    public String getDefaultFontPath() {
        return this.defaultFontPath;
    }

    /**
     * Get semi bold font path from Assets directory.
     *
     * @return font path for semi bold style font.
     */
    public String getSemiBoldFontPath() {
        return this.semiBoldFontPath;
    }

    /**
     * Get bold font path from Assets directory.
     *
     * @return font path for bold style font.
     */
    public String getBoldFontPath() {
        return this.boldFontPath;
    }

    /**
     * Get checkout URL to be used for checkout.
     *
     * @return merchant checkout URL.
     */
    public String getCheckoutUrl() {
        return checkoutUrl;
    }

    /**
     * Get checkout token request for checkout.
     *
     * @return merchant checkout request.
     */
    public CheckoutTokenRequest getCheckoutTokenRequest() {
        return checkoutTokenRequest;
    }

    /**
     * Get snap transaction details on SDK instance.
     *
     * @return snap transaction details.
     */
    public SnapTransaction getTransaction() {
        return this.transaction;
    }

    /**
     * Set snap transaction details on SDK instance.
     *
     * @param transaction snap transaction details.
     */
    public void setTransaction(SnapTransaction transaction) {
        this.transaction = transaction;
    }

    /**
     * Get snap checkout token.
     *
     * @return checkout token.
     */
    public String getCheckoutToken() {
        return this.checkoutToken;
    }

    /**
     * Set checkout token obtained on checkout.
     *
     * @param checkoutToken checkout token.
     */
    public void setCheckoutToken(String checkoutToken) {
        this.checkoutToken = checkoutToken;
    }

    /**
     * Get payment callback.
     *
     * @return payment callback.
     */
    public MidtransUiCallback getPaymentCallback() {
        return paymentCallback;
    }

    /**
     * Set payment callback.
     *
     * @param paymentCallback payment callback.
     */
    public void setPaymentCallback(MidtransUiCallback paymentCallback) {
        this.paymentCallback = paymentCallback;
    }

    /**
     * Check if token storage was enabled.
     *
     * @return enabled is token storage enabled.
     */
    public boolean isTokenStorageEnabled() {
        return tokenStorageEnabled;
    }

    private void initMidtransCore() {
        new MidtransCore.Builder()
                .enableLog(true)
                .setEnvironment(environment)
                .setClientKey(clientKey)
                .build();
    }

    /**
     * Clear transaction specific data on SDK instance.
     */
    public void clearTransaction() {
        this.checkoutUrl = null;
        this.checkoutToken = null;
        this.transaction = null;
    }

    public void trackEvent(String eventName) {
    }

    /**
     * MidtransUi builder class.
     */
    public static class Builder {
        private boolean enableLog = true;
        private String clientKey;
        private Environment environment;
        private boolean tokenStorageEnabled = true;
        private CustomColorTheme customColorTheme;
        private String defaultFontPath;
        private String semiBoldFontPath;
        private String boldFontPath;

        /**
         * Add client key into Builder.
         *
         * @param clientKey Midtrans client key.
         * @return {@link Builder} instance.
         */
        public Builder setClientKey(String clientKey) {
            this.clientKey = clientKey;
            return this;
        }

        /**
         * Add environment type into Builder.
         *
         * @param environment environment type.
         * @return {@link Builder} instance.
         */
        public Builder setEnvironment(Environment environment) {
            this.environment = environment;
            return this;
        }

        /**
         * Add log enabled properties into builder.
         *
         * @param enableLog set to true if log was enabled.
         * @return {@link Builder} instance.
         */
        public Builder enableLog(boolean enableLog) {
            this.enableLog = enableLog;
            return this;
        }

        /**
         * Set default font path from Assets directory.
         *
         * @param defaultFontPath default font path.
         * @return {@link Builder} instance.
         */
        public Builder setDefaultFontPath(String defaultFontPath) {
            this.defaultFontPath = defaultFontPath;
            return this;
        }

        /**
         * Set semi bold font path from Assets directory.
         *
         * @param semiBoldFontPath semi bold font path.
         * @return {@link Builder} instance.
         */
        public Builder setSemiBoldFontPath(String semiBoldFontPath) {
            this.semiBoldFontPath = semiBoldFontPath;
            return this;
        }

        /**
         * Set bold font path from Assets directory.
         *
         * @param boldFontPath bold font path.
         * @return {@link Builder} instance.
         */
        public Builder setBoldFontPath(String boldFontPath) {
            this.boldFontPath = boldFontPath;
            return this;
        }

        /**
         * Set custom color theme to be used in entire SDK.
         *
         * @param customColorTheme custom color theme.
         * @return {@link Builder} instance.
         */
        public Builder setColorTheme(CustomColorTheme customColorTheme) {
            this.customColorTheme = customColorTheme;
            return this;
        }

        /**
         * Set token storage enabled mode.
         *
         * @param tokenStorageEnabled token storage mode.
         * @return {@link Builder} instance.
         */
        public Builder setTokenStorageEnabled(boolean tokenStorageEnabled) {
            this.tokenStorageEnabled = tokenStorageEnabled;
            return this;
        }

        /**
         * Build MidtransUi instance using {@link Builder} instance.
         *
         * @return {@link MidtransUi} instance.
         */
        public MidtransUi build() {
            if (clientKey == null || clientKey.equalsIgnoreCase("")) {
                String message = "Client key cannot be null or empty. Please pass the client key using setClientKey()";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
                throw runtimeException;
            }

            if (environment == null) {
                String message = "You must set an environment. Please use setEnvironment(Environment.$ENV)";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
                throw runtimeException;
            }

            return new MidtransUi(this);
        }
    }
}
