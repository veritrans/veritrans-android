package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.core.merchant.MerchantApiManager;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.snap.SnapApiManager;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.PaymentInfoResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.corekit.utilities.NetworkHelper;
import com.midtrans.sdk.corekit.utilities.Validation;

import static android.webkit.URLUtil.isValidUrl;
import static com.midtrans.sdk.corekit.utilities.Constants.ERROR_SDK_CLIENT_KEY_AND_CONTEXT_PROPERLY;
import static com.midtrans.sdk.corekit.utilities.Constants.ERROR_SDK_IS_NOT_INITIALIZE_PROPERLY;
import static com.midtrans.sdk.corekit.utilities.Constants.ERROR_SDK_MERCHANT_BASE_URL_PROPERLY;

public class MidtransSdk {

    /**
     * Instance variable.
     */
    private static volatile MidtransSdk SINGLETON_INSTANCE = null;
    private final int apiRequestTimeOut = 30;
    private final String BASE_URL_SANDBOX = "https://api.sandbox.midtrans.com/v2/";
    private final String BASE_URL_PRODUCTION = "https://api.midtrans.com/v2/";
    private final String SNAP_BASE_URL_SANDBOX = "https://app.sandbox.midtrans.com/snap/";
    private final String SNAP_BASE_URL_PRODUCTION = "https://app.midtrans.com/snap/";
    private final String PROMO_BASE_URL_SANDBOX = "https://promo.vt-stage.info/";
    private final String PROMO_BASE_URL_PRODUCTION = "https://promo.vt-stage.info/";
    /**
     * Mandatory property.
     */
    private Context context;
    private String merchantClientId;
    private String merchantBaseUrl;
    /**
     * Optional property.
     */
    private Environment midtransEnvironment;
    /**
     * Mandatory checkoutWithTransaction property.
     */
    private CheckoutTransaction checkoutTransaction = null;
    private MerchantApiManager merchantApiManager;
    private SnapApiManager snapApiManager;

    MidtransSdk(Context context,
                String clientId,
                String merchantUrl,
                Environment environment) {
        this.context = context.getApplicationContext();
        this.merchantClientId = clientId;
        this.merchantBaseUrl = merchantUrl;
        this.midtransEnvironment = environment;
        this.merchantApiManager = NetworkHelper.newMerchantServiceManager(merchantBaseUrl, apiRequestTimeOut);
        String snapBaseUrl;
        if (this.midtransEnvironment == Environment.SANDBOX) {
            snapBaseUrl = SNAP_BASE_URL_SANDBOX;
        } else {
            snapBaseUrl = SNAP_BASE_URL_PRODUCTION;
        }
        this.snapApiManager = NetworkHelper.newSnapServiceManager(snapBaseUrl, apiRequestTimeOut);
    }

    /**
     * Initializer.
     *
     * @param context     Context, mandatory not null.
     * @param clientId    ClientId or Merchant Client Key, mandatory not null.
     * @param merchantUrl MerchantUrl or Merchant Base Url, mandatory not null.
     * @return Builder.
     */
    public static Builder builder(@NonNull Context context,
                                  @NonNull String clientId,
                                  @NonNull String merchantUrl) {

        return new Builder(context,
                clientId,
                merchantUrl);
    }

    /**
     * Returns instance of midtrans sdk.
     *
     * @return MidtransSdk instance.
     */
    public synchronized static MidtransSdk getInstance() {
        if (SINGLETON_INSTANCE == null) {
            String message = "MidtransSdk isn't initialized. Please use MidtransSdk.builder() to initialize.";
            RuntimeException runtimeException = new RuntimeException(message);
            Logger.error(message, runtimeException);
        }
        return SINGLETON_INSTANCE;
    }

    /**
     * @return snap api manager
     */
    public SnapApiManager getSnapApiManager() {
        return snapApiManager;
    }

    /**
     * Returns value of merchant url.
     *
     * @return merchant url value.
     */
    public Environment getEnvironment() {
        return midtransEnvironment;
    }

    /**
     * Returns value of instance context.
     *
     * @return context value.
     */
    public Context getContext() {
        return context;
    }

    /**
     * Returns value of merchant client id.
     *
     * @return merchant client id value.
     */
    public String getMerchantClientId() {
        return merchantClientId;
    }

    /**
     * Returns value of merchant base url.
     *
     * @return merchant base url value.
     */
    public String getMerchantBaseUrl() {
        return merchantBaseUrl;
    }

    /**
     * Returns value of timeout.
     *
     * @return timeout value.
     */
    public int getApiRequestTimeOut() {
        return apiRequestTimeOut;
    }

    /**
     * Returns value of transaction request.
     *
     * @return transaction request object.
     */
    public CheckoutTransaction getCheckoutTransaction() {
        return checkoutTransaction;
    }

    /**
     * Set value to transaction request for begin checkoutWithTransaction.
     */
    public void setCheckoutTransaction(CheckoutTransaction checkoutTransaction) {
        this.checkoutTransaction = checkoutTransaction;
    }

    /**
     * Begin checkoutWithTransaction for all Payment Method that merchant activate in MAP
     * for use this method you have to set checkoutTransaction first.
     *
     * @param callback for receiving callback from request.
     */
    public void checkoutWithTransaction(final MidtransCallback<CheckoutWithTransactionResponse> callback) {
        checkoutWithTransaction(this.checkoutTransaction, callback);
    }

    /**
     * Begin checkoutWithTransaction for all Payment Method that merchant activate in MAP
     * for use this method you must to include checkoutTransaction as parameter.
     *
     * @param checkoutTransaction transaction request for making checkoutWithTransaction.
     * @param callback            for receiving callback from request.
     */
    public void checkoutWithTransaction(@NonNull final CheckoutTransaction checkoutTransaction,
                                        final MidtransCallback<CheckoutWithTransactionResponse> callback) {
        if (Validation.isValidForNetworkCall(context, callback)) {
            merchantApiManager.checkout(checkoutTransaction, callback);
        }
    }

    /**
     * Getting Payment Info including enabled payment method and others information.
     *
     * @param snapToken token after making checkoutWithTransaction.
     * @param callback  for receiving callback from request.
     */
    public void getPaymentInfo(final String snapToken,
                               final MidtransCallback<PaymentInfoResponse> callback) {
        if (Validation.isValidForNetworkCall(context, callback)) {
            snapApiManager.getPaymentInfo(snapToken, callback);
        }
    }

    /**
     * Builder class extends BaseSdkBuilder
     * contain Constructor for set data to BaseSdkBuilder.
     */
    public static class Builder {

        protected String merchantClientId;
        protected Context context;
        protected boolean enableLog = false;
        protected String merchantBaseUrl;
        protected Environment midtransEnvironment = Environment.SANDBOX;

        private Builder(Context context, String clientId, String merchantUrl) {
            this.context = context;
            this.merchantClientId = clientId;
            this.merchantBaseUrl = merchantUrl;
        }

        /**
         * set Logger visible or not.
         */
        public Builder setLogEnabled(boolean logEnabled) {
            this.enableLog = logEnabled;
            Logger.enabled = this.enableLog;
            return this;
        }

        /**
         * set Logger visible or not.
         */
        public Builder setEnvironment(Environment environment) {
            this.midtransEnvironment = environment;
            return this;
        }

        /**
         * This method will start payment flow if you have set useUi field to true.
         *
         * @return it returns fully initialized object of midtrans sdk.
         */
        public MidtransSdk build() {
            if (isValidData()) {
                SINGLETON_INSTANCE = new MidtransSdk(context,
                        merchantClientId,
                        merchantBaseUrl,
                        midtransEnvironment);
                return SINGLETON_INSTANCE;
            } else {
                Logger.error(ERROR_SDK_IS_NOT_INITIALIZE_PROPERLY);
            }
            return null;
        }

        private boolean isValidData() {
            if (merchantClientId == null || context == null) {
                RuntimeException runtimeException = new RuntimeException(ERROR_SDK_CLIENT_KEY_AND_CONTEXT_PROPERLY);
                Logger.error(ERROR_SDK_CLIENT_KEY_AND_CONTEXT_PROPERLY, runtimeException);
            }

            if (TextUtils.isEmpty(merchantBaseUrl) && isValidUrl(merchantBaseUrl)) {
                RuntimeException runtimeException = new RuntimeException(ERROR_SDK_MERCHANT_BASE_URL_PROPERLY);
                Logger.error(ERROR_SDK_MERCHANT_BASE_URL_PROPERLY, runtimeException);
            }
            return true;
        }
    }
}