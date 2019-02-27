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
import com.midtrans.sdk.corekit.core.api.snap.model.payment.PaymentStatusResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.point.PointResponse;
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
    private static final int API_TIMEOUT_DEFAULT = 30;
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
    private boolean isBuiltinStorageEnabled;
    /**
     * Mandatory checkoutWithTransaction property.
     */
    private CheckoutTransaction checkoutTransaction = null;
    private MerchantApiManager merchantApiManager;
    private SnapApiManager snapApiManager;
    private MidtransApiManager midtransApiManager;

    MidtransSdk(
            Context context,
            String clientId,
            String merchantUrl,
            Environment environment,
            int apiRequestTimeOut,
            boolean isLogEnabled,
            boolean isBuiltinStorageEnabled
    ) {
        String snapBaseUrl, midtransBaseUrl;
        Logger.enabled = isLogEnabled;
        this.context = context.getApplicationContext();
        this.merchantClientId = clientId;
        this.merchantBaseUrl = merchantUrl;
        this.midtransEnvironment = environment;
        this.apiRequestTimeOut = apiRequestTimeOut;
        this.isBuiltinStorageEnabled = isBuiltinStorageEnabled;
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
    public static Builder builder(
            final Context context,
            final String clientId,
            final String merchantUrl
    ) {
        return new Builder(
                context,
                clientId,
                merchantUrl
        );
    }

    /**
     * Returns instance of midtrans sdk.
     *
     * @return MidtransSdk instance.
     */
    public synchronized static MidtransSdk getInstance() {
        if (doCheckSdkInitialization(SINGLETON_INSTANCE)) {
            return SINGLETON_INSTANCE;
        }
        return null;
    }

    /**
     * @return snap api manager
     */
    public SnapApiManager getSnapApiManager() {
        if (doCheckSdkInitialization(snapApiManager)) {
            return snapApiManager;
        }
        return null;
    }

    /**
     * @return midtrans service manager
     */
    public MidtransApiManager getMidtransApiManager() {
        if (doCheckSdkInitialization(midtransApiManager)) {
            return midtransApiManager;
        }
        return null;
    }

    /**
     * @return merchant api manager
     */
    public MerchantApiManager getMerchantApiManager() {
        if (doCheckSdkInitialization(merchantApiManager)) {
            return merchantApiManager;
        }
        return null;
    }

    /**
     * Returns value of merchant url.
     *
     * @return merchant url value.
     */
    public Environment getEnvironment() {
        if (doCheckSdkInitialization(midtransEnvironment)) {
            return midtransEnvironment;
        }
        return null;
    }

    /**
     * Returns value of instance context.
     *
     * @return context value.
     */
    public Context getContext() {
        if (doCheckSdkInitialization(context)) {
            return context;
        }
        return null;
    }

    /**
     * Returns value of merchant client id.
     *
     * @return merchant client id value.
     */
    public String getMerchantClientId() {
        if (doCheckSdkInitialization(merchantClientId)) {
            return merchantClientId;
        }
        return null;
    }

    /**
     * Returns value of merchant base url.
     *
     * @return merchant base url value.
     */
    public String getMerchantBaseUrl() {
        if (doCheckSdkInitialization(merchantBaseUrl)) {
            return merchantBaseUrl;
        }
        return null;
    }

    /**
     * Returns value of timeout.
     *
     * @return timeout value.
     */
    public int getApiRequestTimeOut() {
        if (doCheckSdkInitialization(apiRequestTimeOut)) {
            return apiRequestTimeOut;
        }
        return API_TIMEOUT_DEFAULT;
    }

    /**
     * Returns value of builtinstorage
     *
     * @return builtinstorage value
     */
    public boolean isBuiltinStorageEnabled() {
        if (doCheckSdkInitialization(isBuiltinStorageEnabled)) {
            return isBuiltinStorageEnabled;
        }
        return true;
    }

    /**
     * Returns value of transaction request.
     *
     * @return transaction request object.
     */
    public CheckoutTransaction getCheckoutTransaction() {
        if (doCheckSdkInitialization(checkoutTransaction)) {
            return checkoutTransaction;
        }
        return null;
    }

    /**
     * Set value to transaction request for begin checkoutWithTransaction.
     */
    public void setCheckoutTransaction(
            final CheckoutTransaction checkoutTransaction
    ) {
        this.checkoutTransaction = checkoutTransaction;
    }

    /**
     * Begin checkoutWithTransaction for all Payment Method that merchant activate in MAP
     * for use this method you must to include checkoutTransaction as parameter.
     *
     * @param checkoutTransaction transaction request for making checkoutWithTransaction.
     * @param callback            for receiving callback from request.
     */
    public void checkoutWithTransaction(
            final CheckoutTransaction checkoutTransaction,
            final MidtransCallback<CheckoutWithTransactionResponse> callback
    ) {
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
    public void getPaymentInfo(
            final String token,
            final MidtransCallback<PaymentInfoResponse> callback
    ) {
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
    public void getBanksPoint(
            final String token,
            final String cardToken,
            final MidtransCallback<PointResponse> callback
    ) {
        if (isValidForNetworkCall(context, callback)) {
            snapApiManager.getBanksPoint(token, cardToken, callback);
        }
    }

    /**
     * It will get payment status of transaction
     *
     * @param callback bni point callback instance
     */
    public void getPaymentStatus(
            final String token,
            final MidtransCallback<PaymentStatusResponse> callback
    ) {
        if (isValidForNetworkCall(context, callback)) {
            snapApiManager.getPaymentStatus(token, callback);
        }
    }

    /**
     * Builder class extends BaseSdkBuilder
     * contain Constructor for set data to BaseSdkBuilder.
     */
    public static class Builder {

        private String merchantClientId;
        private Context context;
        private String merchantBaseUrl;
        private Environment midtransEnvironment = Environment.SANDBOX;
        private int apiRequestTimeOut = API_TIMEOUT_DEFAULT;
        private boolean isLogEnabled = false;
        private boolean isBuiltinStorageEnabled = true;

        private Builder(
                Context context,
                String clientId,
                String merchantUrl
        ) {
            this.context = context;
            this.merchantClientId = clientId;
            this.merchantBaseUrl = merchantUrl;
        }

        /**
         * set Logger visible or not.
         */
        public Builder setLogEnabled(boolean isLogEnabled) {
            this.isLogEnabled = isLogEnabled;
            return this;
        }

        public Builder setBuiltinStorageEnabled(boolean isBuiltinStorageEnabled) {
            this.isBuiltinStorageEnabled = isBuiltinStorageEnabled;
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
         * set Logger visible or not.
         */
        public Builder setApiRequestTimeOut(int apiRequestTimeOutInSecond) {
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
                SINGLETON_INSTANCE = new MidtransSdk(
                        context,
                        merchantClientId,
                        merchantBaseUrl,
                        midtransEnvironment,
                        apiRequestTimeOut,
                        isLogEnabled,
                        isBuiltinStorageEnabled
                );
                return SINGLETON_INSTANCE;
            } else {
                Logger.error(ERROR_SDK_IS_NOT_INITIALIZE_PROPERLY);
            }
            return null;
        }

        private boolean isValidData() {
            if (merchantClientId == null || context == null) {
                RuntimeException runtimeException = new RuntimeException(ERROR_SDK_CLIENT_KEY_AND_CONTEXT_PROPERLY);
                Logger.error(runtimeException.getMessage());
            }

            if (TextUtils.isEmpty(merchantBaseUrl) && isValidUrl(merchantBaseUrl)) {
                RuntimeException runtimeException = new RuntimeException(ERROR_SDK_MERCHANT_BASE_URL_PROPERLY);
                Logger.error(runtimeException.getMessage());
            }
            return true;
        }
    }

    private static <T> boolean doCheckSdkInitialization(T itemForCheck) {
        if (itemForCheck == null) {
            RuntimeException runtimeException = new RuntimeException(MESSAGE_INSTANCE_NOT_INITALIZE);
            Logger.error(runtimeException.getMessage());
            return false;
        }
        return true;
    }
}