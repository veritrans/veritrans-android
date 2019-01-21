package com.midtrans.sdk.corekit;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.core.api.merchant.MerchantApiManager;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.api.midtrans.MidtransApiManager;
import com.midtrans.sdk.corekit.core.api.snap.SnapApiManager;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.corekit.utilities.NetworkHelper;

import static android.webkit.URLUtil.isValidUrl;
import static com.midtrans.sdk.corekit.utilities.Constants.ERROR_SDK_CLIENT_KEY_AND_CONTEXT_PROPERLY;
import static com.midtrans.sdk.corekit.utilities.Constants.ERROR_SDK_IS_NOT_INITIALIZE_PROPERLY;
import static com.midtrans.sdk.corekit.utilities.Constants.ERROR_SDK_MERCHANT_BASE_URL_PROPERLY;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_INSTANCE_NOT_INITALIZE;
import static com.midtrans.sdk.corekit.utilities.NetworkHelper.isValidForNetworkCall;

public class MidtransSdk {

    /**
     * Instance variable.
     */
    private static volatile MidtransSdk SINGLETON_INSTANCE = null;
    private final String BASE_URL_SANDBOX = "https://api.sandbox.midtrans.com/v2/";
    private final String BASE_URL_PRODUCTION = "https://api.midtrans.com/v2/";
    private final String SNAP_BASE_URL_SANDBOX = "https://app.sandbox.midtrans.com/snap/";
    private final String SNAP_BASE_URL_PRODUCTION = "https://app.midtrans.com/snap/";
    private final String PROMO_BASE_URL_SANDBOX = "https://promo.vt-stage.info/";
    private final String PROMO_BASE_URL_PRODUCTION = "https://promo.vt-stage.info/";
    private final String APP_MARKET_URL = "market://details?id=";
    private final String APP_PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=";
    private final String CALLBACK_URL = "https://hangout.betas.in/veritrans/api/paymentstatus";
    private final String CALLBACK_STRING = "/token/callback/";

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
    private int apiRequestTimeOut;
    /**
     * Mandatory checkoutWithTransaction property.
     */
    private CheckoutTransaction checkoutTransaction = null;
    private MerchantApiManager merchantApiManager;
    private SnapApiManager snapApiManager;
    private MidtransApiManager midtransApiManager;

    MidtransSdk(final Context context,
                final String clientId,
                final String merchantUrl,
                final Environment environment,
                final int apiRequestTimeOut) {
        this.context = context.getApplicationContext();
        this.merchantClientId = clientId;
        this.merchantBaseUrl = merchantUrl;
        this.midtransEnvironment = environment;
        this.apiRequestTimeOut = apiRequestTimeOut;
        String snapBaseUrl, midtransBaseUrl;
        if (this.midtransEnvironment == Environment.SANDBOX) {
            snapBaseUrl = SNAP_BASE_URL_SANDBOX;
            midtransBaseUrl = BASE_URL_SANDBOX;
        } else {
            snapBaseUrl = SNAP_BASE_URL_PRODUCTION;
            midtransBaseUrl = BASE_URL_PRODUCTION;
        }
        this.merchantApiManager = NetworkHelper.newMerchantServiceManager(merchantBaseUrl, apiRequestTimeOut);
        this.snapApiManager = NetworkHelper.newSnapServiceManager(snapBaseUrl, apiRequestTimeOut);
        this.midtransApiManager = NetworkHelper.newMidtransServiceManager(midtransBaseUrl, apiRequestTimeOut);
    }

    /**
     * Initializer.
     *
     * @param context     Context, mandatory not null.
     * @param clientId    ClientId or Merchant Client Key, mandatory not null.
     * @param merchantUrl MerchantUrl or Merchant Base Url, mandatory not null.
     * @return Builder.
     */
    public static Builder builder(final Context context,
                                  final String clientId,
                                  final String merchantUrl) {

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
            doOnSdkNotInitialize();
        }
        return SINGLETON_INSTANCE;
    }

    /**
     * @return snap api manager
     */
    public SnapApiManager getSnapApiManager() {
        if (snapApiManager == null) {
            doOnSdkNotInitialize();
        }
        return snapApiManager;
    }

    public void setSnapApiManager(final SnapApiManager snapApiManager) {
        this.snapApiManager = snapApiManager;
    }

    /**
     * @return midtrans service manager
     */
    public MidtransApiManager getMidtransApiManager() {
        if (midtransApiManager == null) {
            doOnSdkNotInitialize();
        }
        return midtransApiManager;
    }

    /**
     * @return merchant api manager
     */
    public MerchantApiManager getMerchantApiManager() {
        if (merchantApiManager == null) {
            doOnSdkNotInitialize();
        }
        return merchantApiManager;
    }

    public void setMerchantApiManager(final MerchantApiManager merchantApiManager) {
        this.merchantApiManager = merchantApiManager;
    }

    /**
     * Returns value of merchant url.
     *
     * @return merchant url value.
     */
    public Environment getEnvironment() {
        if (midtransEnvironment == null) {
            doOnSdkNotInitialize();
        }
        return midtransEnvironment;
    }

    /**
     * Returns value of instance context.
     *
     * @return context value.
     */
    public Context getContext() {
        if (context == null) {
            doOnSdkNotInitialize();
        }
        return context;
    }

    /**
     * Returns value of merchant client id.
     *
     * @return merchant client id value.
     */
    public String getMerchantClientId() {
        if (merchantClientId == null) {
            doOnSdkNotInitialize();
        }
        return merchantClientId;
    }

    /**
     * Returns value of merchant base url.
     *
     * @return merchant base url value.
     */
    public String getMerchantBaseUrl() {
        if (merchantBaseUrl == null) {
            doOnSdkNotInitialize();
        }
        return merchantBaseUrl;
    }

    /**
     * Returns value of timeout.
     *
     * @return timeout value.
     */
    public int getApiRequestTimeOut() {
        if (apiRequestTimeOut == 0) {
            doOnSdkNotInitialize();
        }
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
    public void setCheckoutTransaction(final CheckoutTransaction checkoutTransaction) {
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
    public void checkoutWithTransaction(final CheckoutTransaction checkoutTransaction,
                                        final MidtransCallback<CheckoutWithTransactionResponse> callback) {
        if (isValidForNetworkCall(context, callback)) {
            merchantApiManager.checkout(checkoutTransaction, callback);
        }
    }

    /**
     * Getting Payment Info including enabled payment method and others information.
     *
     * @param token    token after making checkoutWithTransaction.
     * @param callback for receiving callback from request.
     */
    public void getPaymentInfo(final String token,
                               final MidtransCallback<PaymentInfoResponse> callback) {
        if (isValidForNetworkCall(context, callback)) {
            snapApiManager.getPaymentInfo(token, callback);
        }
    }

    /**
     * it will get bank points (BNI or Mandiri) from snap backend
     *
     * @param cardToken credit card token
     * @param callback  bni point callback instance
     */
    public void getBanksPoint(final String token,
                              final String cardToken,
                              final MidtransCallback<BasePaymentResponse> callback) {
        if (isValidForNetworkCall(context, callback)) {
            snapApiManager.getBanksPoint(token, cardToken, callback);
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
        protected int apiRequestTimeOut = 30;

        private Builder(final Context context,
                        final String clientId,
                        final String merchantUrl) {
            this.context = context;
            this.merchantClientId = clientId;
            this.merchantBaseUrl = merchantUrl;
        }

        /**
         * set Logger visible or not.
         */
        public Builder setLogEnabled(final boolean logEnabled) {
            this.enableLog = logEnabled;
            Logger.enabled = this.enableLog;
            return this;
        }

        /**
         * set Logger visible or not.
         */
        public Builder setEnvironment(final Environment environment) {
            this.midtransEnvironment = environment;
            return this;
        }

        /**
         * set Logger visible or not.
         */
        public Builder setApiRequestTimeOut(final int apiRequestTimeOutInSecond) {
            this.apiRequestTimeOut = apiRequestTimeOutInSecond;
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
                        midtransEnvironment,
                        apiRequestTimeOut);
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

    private static void doOnSdkNotInitialize() {
        RuntimeException runtimeException = new RuntimeException(MESSAGE_INSTANCE_NOT_INITALIZE);
        Logger.error(runtimeException.getMessage());
    }
}