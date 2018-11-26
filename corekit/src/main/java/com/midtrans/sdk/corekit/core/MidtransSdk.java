package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.core.merchant.MerchantApiManager;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.TransactionRequest;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.CheckoutResponse;
import com.midtrans.sdk.corekit.core.snap.SnapApiManager;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.va.CustomerDetailRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.BcaPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.BniPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.OtherPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.PermataPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.PaymentInfoResponse;
import com.midtrans.sdk.corekit.utilities.Constants;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.corekit.utilities.NetworkHelper;

import static android.webkit.URLUtil.isValidUrl;

public class MidtransSdk {

    private static final String TAG = "MidtransSdk";

    /**
     * Instance variable.
     */
    private static volatile MidtransSdk SINGLETON_INSTANCE = null;

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
     * Mandatory checkout property.
     */
    private TransactionRequest transactionRequest = null;

    private MerchantApiManager merchantApiManager;
    private SnapApiManager snapApiManager;

    private final int apiRequestTimeOut = 30;
    private final String BASE_URL_SANDBOX = "https://api.sandbox.midtrans.com/v2/";
    private final String BASE_URL_PRODUCTION = "https://api.midtrans.com/v2/";
    private final String SNAP_BASE_URL_SANDBOX = "https://app.sandbox.midtrans.com/snap/";
    private final String SNAP_BASE_URL_PRODUCTION = "https://app.midtrans.com/snap/";
    private final String PROMO_BASE_URL_SANDBOX = "https://promo.vt-stage.info/";
    private final String PROMO_BASE_URL_PRODUCTION = "https://promo.vt-stage.info/";

    private MidtransSdk(Context context,
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
                Logger.error("Already performing an transaction");
            }
            return null;
        }

        private boolean isValidData() {
            if (merchantClientId == null || context == null) {
                String message = "Client key and context cannot be null or empty. Please set the client key and context";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
            }

            if (TextUtils.isEmpty(merchantBaseUrl) && isValidUrl(merchantBaseUrl)) {
                String message = "Merchant base url cannot be null or empty (required) and must url valid format if you implement your own token storage. Please set your merchant base url to enable your own token storage";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
            }
            return true;
        }
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
    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    /**
     * Set value to transaction request for begin checkout.
     */
    public void setTransactionRequest(TransactionRequest transactionRequest) {
        this.transactionRequest = transactionRequest;
    }

    /**
     * Begin checkout for all Payment Method that merchant activate in MAP
     * for use this method you have to set transactionRequest first.
     *
     * @param callback for receiving callback from request.
     */
    public void checkout(final MidtransCallback<CheckoutResponse> callback) {
        checkout(this.transactionRequest, callback);
    }

    /**
     * Begin checkout for all Payment Method that merchant activate in MAP
     * for use this method you must to include transactionRequest as parameter.
     *
     * @param transactionRequest transaction request for making checkout.
     * @param callback           for receiving callback from request.
     */
    public void checkout(@NonNull final TransactionRequest transactionRequest,
                         final MidtransCallback<CheckoutResponse> callback) {
        if (callback == null) {
            Logger.error(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (isNetworkAvailable()) {
            merchantApiManager.checkout(transactionRequest, callback);
        } else {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * Getting Payment Info including enabled payment method and others information.
     *
     * @param snapToken token after making checkout.
     * @param callback  for receiving callback from request.
     */
    public void getPaymentInfo(final String snapToken,
                               final MidtransCallback<PaymentInfoResponse> callback) {
        if (callback == null) {
            Logger.error(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (isNetworkAvailable()) {
            snapApiManager.getPaymentInfo(snapToken, callback);
        } else {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * Start payment using bank transfer and va with BCA.
     *
     * @param snapToken       token after making checkout.
     * @param customerDetails for putting bank transfer request.
     * @param callback        for receiving callback from request.
     */
    public void paymentUsingBankTransferVaBca(final String snapToken,
                                              final CustomerDetailRequest customerDetails,
                                              final MidtransCallback<BcaPaymentResponse> callback) {
        if (callback == null) {
            Logger.error(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (isNetworkAvailable()) {
            snapApiManager.paymentUsingBankTransferVaBca(snapToken, customerDetails, callback);
        } else {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * Start payment using bank transfer and va with BNI.
     *
     * @param snapToken       token after making checkout.
     * @param customerDetails for putting bank transfer request.
     * @param callback        for receiving callback from request.
     */
    public void paymentUsingBankTransferVaBni(final String snapToken,
                                              final CustomerDetailRequest customerDetails,
                                              final MidtransCallback<BniPaymentResponse> callback) {
        if (callback == null) {
            Logger.error(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (isNetworkAvailable()) {
            snapApiManager.paymentUsingBankTransferVaBni(snapToken, customerDetails, callback);
        } else {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * Start payment using bank transfer and va with PERMATA.
     *
     * @param snapToken       token after making checkout.
     * @param customerDetails for putting bank transfer request.
     * @param callback        for receiving callback from request.
     */
    public void paymentUsingBankTransferVaPermata(final String snapToken,
                                                  final CustomerDetailRequest customerDetails,
                                                  final MidtransCallback<PermataPaymentResponse> callback) {
        if (callback == null) {
            Logger.error(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (isNetworkAvailable()) {
            snapApiManager.paymentUsingBankTransferVaPermata(snapToken, customerDetails, callback);
        } else {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * Start payment using bank transfer and va with Other Bank.
     *
     * @param snapToken       token after making checkout.
     * @param customerDetails for putting bank transfer request.
     * @param callback        for receiving callback from request.
     */
    public void paymentUsingBankTransferVaOther(final String snapToken,
                                                final CustomerDetailRequest customerDetails,
                                                final MidtransCallback<OtherPaymentResponse> callback) {
        if (callback == null) {
            Logger.error(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (isNetworkAvailable()) {
            snapApiManager.paymentUsingBankTransferVaOther(snapToken, customerDetails, callback);
        } else {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * Open utils for checking network status
     *
     * @return boolean based on network status
     */
    public boolean isNetworkAvailable() {
        return NetworkHelper.isNetworkAvailable(this.context);
    }

}