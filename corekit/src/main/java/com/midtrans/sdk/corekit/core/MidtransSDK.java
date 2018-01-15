package com.midtrans.sdk.corekit.core;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.analytics.MixpanelAnalyticsManager;
import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.callback.BankBinsCallback;
import com.midtrans.sdk.corekit.callback.BanksPointCallback;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.GetTransactionStatusCallback;
import com.midtrans.sdk.corekit.callback.GoPayResendAuthorizationCallback;
import com.midtrans.sdk.corekit.callback.HoldPromoCallback;
import com.midtrans.sdk.corekit.callback.ObtainPromoCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.core.themes.BaseColorTheme;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.MerchantData;
import com.midtrans.sdk.corekit.models.snap.PromoResponse;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.models.snap.params.IndosatDompetkuPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.NewMandiriClickPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.TelkomselCashPaymentParams;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.NewMandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;
import com.midtrans.sdk.corekit.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivam on 10/19/15.
 */
public class MidtransSDK {
    private static final String TAG = "MidtransSDK";
    private static final String ADD_TRANSACTION_DETAILS = "Add transaction request details.";
    private static final String LOCAL_DATA_PREFERENCES = "local.data";

    private static SharedPreferences mPreferences = null;
    private static volatile MidtransSDK midtransSDK;
    private static BaseSdkBuilder sdkBuilder;
    private static boolean sdkNotAvailable = false;

    protected boolean isRunning = false;
    ISdkFlow uiflow;
    private UIKitCustomSetting UIKitCustomSetting;
    private boolean isLogEnabled = false;
    private TransactionFinishedCallback transactionFinishedCallback;
    private MixpanelAnalyticsManager mMixpanelAnalyticsManager;
    private Context context = null;
    private String clientKey = null;
    private String merchantServerUrl = null;
    private String defaultText = null;
    private String boldText = null;
    private String semiBoldText = null;
    private String merchantName = null;
    private IScanner externalScanner;
    private PromoEngineManager promoEngineManager;
    private SnapTransactionManager mSnapTransactionManager;
    private String merchantLogo = null;
    private TransactionRequest transactionRequest = null;
    private ArrayList<PaymentMethodsModel> selectedPaymentMethods = new ArrayList<>();
    private boolean enableBuiltInTokenStorage;
    private String sdkBaseUrl = "";
    private int requestTimeOut = 30;
    private String flow = null;
    private List<PromoResponse> promoResponses = new ArrayList<>();
    private BaseColorTheme colorTheme;
    private Transaction transaction;
    private CardRegistrationCallback cardRegistrationCallback;

    private MidtransSDK() {

    }

    private MidtransSDK(@NonNull BaseSdkBuilder sdkBuilder) {

        this.context = sdkBuilder.context;
        this.clientKey = sdkBuilder.clientKey;
        this.merchantServerUrl = sdkBuilder.merchantServerUrl;
        this.sdkBaseUrl = BuildConfig.SNAP_BASE_URL;
        this.defaultText = sdkBuilder.defaultText;
        this.semiBoldText = sdkBuilder.semiBoldText;
        this.boldText = sdkBuilder.boldText;
        this.uiflow = sdkBuilder.sdkFlow;
        this.transactionFinishedCallback = sdkBuilder.transactionFinishedCallback;
        this.externalScanner = sdkBuilder.externalScanner;
        this.isLogEnabled = sdkBuilder.enableLog;
        Logger.enabled = sdkBuilder.enableLog;
        this.enableBuiltInTokenStorage = sdkBuilder.enableBuiltInTokenStorage;
        this.UIKitCustomSetting = sdkBuilder.UIKitCustomSetting == null ? new UIKitCustomSetting() : sdkBuilder.UIKitCustomSetting;
        this.flow = sdkBuilder.flow;

        // Set custom color theme. This will be prioritized over Snap preferences.
        if (sdkBuilder.colorTheme != null) {
            this.colorTheme = sdkBuilder.colorTheme;
        }

        String deviceType = null;
        if (context != null) {
            mPreferences = context.getSharedPreferences(LOCAL_DATA_PREFERENCES, Context.MODE_PRIVATE);

            if (context instanceof Activity) {
                deviceType = Utils.getDeviceType((Activity) context);
            }
        }

        this.mMixpanelAnalyticsManager = new MixpanelAnalyticsManager(BuildConfig.VERSION_NAME, SdkUtil.getDeviceId(context), merchantName, getFlow(flow), deviceType == null ? "" : deviceType, context);
        this.promoEngineManager = new PromoEngineManager(MidtransRestAdapter.getPromoEngineRestAPI(BuildConfig.PROMO_ENGINE_URL, requestTimeOut));
        this.mSnapTransactionManager = new SnapTransactionManager(MidtransRestAdapter.getSnapRestAPI(sdkBaseUrl, requestTimeOut),
                MidtransRestAdapter.getMerchantApiClient(merchantServerUrl, requestTimeOut),
                MidtransRestAdapter.getVeritransApiClient(BuildConfig.BASE_URL, requestTimeOut));

        this.mSnapTransactionManager.setSDKLogEnabled(isLogEnabled);
    }

    /**
     * get Veritrans SDK instance
     *
     * @param newSdkBuilder SDK Coreflow Builder
     */
    protected static MidtransSDK delegateInstance(@NonNull BaseSdkBuilder newSdkBuilder) {
        if (newSdkBuilder != null) {
            midtransSDK = new MidtransSDK(newSdkBuilder);
            sdkBuilder = newSdkBuilder;
        } else {
            Logger.e("sdk is not initialized.");
        }

        return midtransSDK;
    }

    /**
     * Returns instance of midtrans sdk.
     *
     * @return MidtransSDK instance
     */
    public synchronized static MidtransSDK getInstance() {
        if (midtransSDK == null) {
            synchronized (MidtransSDK.class) {
                if (midtransSDK == null) {
                    if (sdkBuilder != null) {
                        midtransSDK = new MidtransSDK(sdkBuilder);
                    } else {
                        midtransSDK = new MidtransSDK();
                        sdkNotAvailable = true;
                    }
                }
            }
        }

        return midtransSDK;
    }

    /**
     * Get Veritrans SDK share preferences instance
     *
     * @return share preferences instance
     */
    public static SharedPreferences getmPreferences() {
        return mPreferences;
    }

    /**
     * set share preference instance to SDK
     */
    static void setmPreferences(SharedPreferences preferences) {
        mPreferences = preferences;
    }

    public boolean isSdkNotAvailable() {
        return sdkNotAvailable;
    }

    private String getFlow(String flow) {
        if (flow.equalsIgnoreCase(BaseSdkBuilder.CORE_FLOW)) {
            return MixpanelAnalyticsManager.CORE_FLOW;
        } else if (flow.equalsIgnoreCase(BaseSdkBuilder.UI_FLOW)) {
            return MixpanelAnalyticsManager.UI_FLOW;
        } else {
            return MixpanelAnalyticsManager.WIDGET;
        }
    }


    /**
     * get Default text font for SDK
     *
     * @return default text
     */
    public String getDefaultText() {
        return defaultText;
    }

    /**
     * set default text to SDK
     */
    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantLogo() {
        return merchantLogo;
    }

    public void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    public String getBoldText() {
        return boldText;
    }

    public void setBoldText(String boldText) {
        this.boldText = boldText;
    }

    public String getSemiBoldText() {
        return semiBoldText;
    }

    public void setSemiBoldText(String semiBoldText) {
        this.semiBoldText = semiBoldText;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isLogEnabled() {
        return isLogEnabled;
    }

    public String getFlow() {
        return flow;
    }

    public void setFlow(String flow) {
        this.flow = flow;
    }

    public Context getContext() {
        return context;
    }

    public MixpanelAnalyticsManager getmMixpanelAnalyticsManager() {
        if (mSnapTransactionManager == null) {
            String deviceType = null;
            if (context != null && context instanceof Activity) {
                deviceType = Utils.getDeviceType((Activity) context);
            }
            this.mMixpanelAnalyticsManager = new MixpanelAnalyticsManager(BuildConfig.VERSION_NAME, SdkUtil.getDeviceId(context), merchantName, getFlow(flow), deviceType == null ? "" : deviceType, context);
        }

        return mMixpanelAnalyticsManager;
    }


    public String readAuthenticationToken() {
        return LocalDataHandler.readString(Constants.AUTH_TOKEN);
    }

    public String getClientKey() {
        return clientKey;
    }

    public String getMerchantServerUrl() {
        return merchantServerUrl;
    }

    /**
     * this method deprecated since v 1.9.3
     *
     * @return
     */
    @Deprecated
    public ArrayList<PaymentMethodsModel> getSelectedPaymentMethods() {
        return selectedPaymentMethods;
    }

    /**
     * this method deprecated since v 1.9.3
     *
     * @param selectedPaymentMethods
     */
    @Deprecated
    public void setSelectedPaymentMethods(ArrayList<PaymentMethodsModel> selectedPaymentMethods) {
        this.selectedPaymentMethods = selectedPaymentMethods;
    }

    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    /**
     * Set transaction information that you want to execute.
     *
     * @param transactionRequest transaction request  object
     */
    public void setTransactionRequest(TransactionRequest transactionRequest) {

        if (transactionRequest != null) {
            this.transactionRequest = transactionRequest;
        } else {
            Logger.e(TAG, ADD_TRANSACTION_DETAILS);
        }
    }

    /**
     * todo remove old implementation of promo engine
     * It will execute an API request to obtain promo token.
     *
     * @param promoId  promo identifier.
     * @param amount   transaction amount.
     * @param callback callback to be called.
     */

    @Deprecated
    public void obtainPromo(String promoId, double amount, ObtainPromoCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (!TextUtils.isEmpty(promoId) && amount != 0) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                promoEngineManager.obtainPromo(promoId, amount, null, null, null, callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
                Logger.e(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER);
            }
        } else {
            Logger.e(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER);
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will execute an API request to obtain promo list by credit card number.
     *
     * @param promoCode
     * @param amount
     * @param clientKey
     * @param cardNumber
     * @param callback
     */
    public void obtainPromoByCardNumber(String promoCode, double amount, @NonNull String clientKey, @NonNull String cardNumber, @NonNull ObtainPromoCallback callback) {
        obtainPromo(promoCode, amount, clientKey, cardNumber, null, callback);
    }

    /**
     * It will execute an API request to obtain promo list by creditcard token.
     *
     * @param promoCode
     * @param amount
     * @param clientKey
     * @param cardToken
     * @param callback
     */
    public void obtainPromoByCardToken(String promoCode, double amount, @NonNull String clientKey, @NonNull String cardToken, @NonNull ObtainPromoCallback callback) {
        obtainPromo(promoCode, amount, clientKey, null, cardToken, callback);
    }

    private void obtainPromo(String promoCode, double totalAmount, @NonNull String clientKey, String cardNumber, String cardToken, @NonNull ObtainPromoCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (!TextUtils.isEmpty(clientKey)) {
            if (totalAmount != 0) {
                if (Utils.isNetworkAvailable(context)) {
                    isRunning = true;
                    promoEngineManager.obtainPromo(promoCode, totalAmount, clientKey, cardNumber, cardToken, callback);
                } else {
                    isRunning = false;
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
                    Logger.e(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER);
                }
            } else {
                Logger.e(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER);
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
            }
        } else {
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
        }
    }


    public void holdPromo(String promoToken, long totalAmount, String clientKey, HoldPromoCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (!TextUtils.isEmpty(promoToken)) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                promoEngineManager.holdPromo(promoToken, totalAmount, clientKey, callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
                Logger.e(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER);
            }
        } else {
            Logger.e(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED);
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
        }
    }

    /**
     * It will execute an api request to retrieve a authentication token.
     *
     * @param cardTokenRequest get card token  request object
     * @param callback         get card token callback
     */
    public void getCardToken(CardTokenRequest cardTokenRequest, CardTokenCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (cardTokenRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.getToken(readAuthenticationToken(), cardTokenRequest, callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
                Logger.e(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER);
            }

        } else {
            Logger.e(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED);
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
        }
    }

    /**
     * It will run backround task to register card PAPI(Payment API) Backend using uikit sdk
     *
     * @param context  activity context.
     * @param callback Card Registration Callback.
     */

    public void UiCardRegistration(@NonNull Context context, @NonNull CardRegistrationCallback callback) {
        if (uiflow != null) {
            this.cardRegistrationCallback = callback;
            uiflow.runCardRegistration(context, callback);
        } else {
            Log.d(TAG, "uikit sdk is needed to use this feature");
        }
    }

    /**
     * Start payment UI flow.
     *
     * @param context       activity context.
     * @param paymentMethod payment method.
     */
    public void startPaymentUiFlow(Context context, PaymentMethod paymentMethod) {
        if (merchantBaseUrlAvailable()) {
            runDirectPaymentUiSdk(context, paymentMethod, null);
        }
    }

    /**
     * Start payment UI flow by passing snap token.
     *
     * @param context       activity context.
     * @param paymentMethod payment method.
     * @param snapToken     checkout token
     */
    public void startPaymentUiFlow(Context context, PaymentMethod paymentMethod, String snapToken) {
        if (snapTokenAvailable(snapToken)) {
            runDirectPaymentUiSdk(context, paymentMethod, snapToken);
        }
    }

    private void runDirectPaymentUiSdk(Context context, PaymentMethod paymentMethod, String snapToken) {
        if (paymentMethod.equals(PaymentMethod.CREDIT_CARD)) {
            startCreditCardUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER)) {
            startBankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER_BCA)) {
            startBCABankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER_PERMATA)) {
            startPermataBankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER_MANDIRI)) {
            startMandiriBankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER_BNI)) {
            startBniBankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BANK_TRANSFER_OTHER)) {
            startOtherBankTransferUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.GO_PAY)) {
            startGoPayUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.BCA_KLIKPAY)) {
            startBCAKlikPayUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.KLIKBCA)) {
            startKlikBCAUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.MANDIRI_CLICKPAY)) {
            startMandiriClickpayUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.MANDIRI_ECASH)) {
            startMandiriECashUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.EPAY_BRI)) {
            startBRIEpayUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.CIMB_CLICKS)) {
            startCIMBClicksUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.TELKOMSEL_CASH)) {
            startTelkomselCashUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.INDOSAT_DOMPETKU)) {
            startIndosatDompetkuUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.XL_TUNAI)) {
            startXlTunaiUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.INDOMARET)) {
            startIndomaretUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.KIOSON)) {
            startKiosonUIFlow(context, snapToken);
        } else if (paymentMethod.equals(PaymentMethod.GIFT_CARD_INDONESIA)) {
            startGiftCardUIFlow(context, snapToken);
        } else {
            if (TextUtils.isEmpty(snapToken)) {
                startPaymentUiFlow(context);
            } else {
                startPaymentUiFlow(context, snapToken);
            }
        }
    }

    /**
     * This will start actual execution of transaction. if you have enabled an ui then it will start
     * activity according to it.
     *
     * @param context current activity.
     */
    public void startPaymentUiFlow(Context context) {
        if (merchantBaseUrlAvailable()) {
            runUiSdk(context, null);
        }
    }

    public void startPaymentUiFlow(Context context, String snapToken) {
        if (snapTokenAvailable(snapToken)) {
            runUiSdk(context, snapToken);
        }
    }

    private void runUiSdk(Context context, String snapToken) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants
                    .PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runUIFlow(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    private boolean snapTokenAvailable(String snapToken) {
        if (TextUtils.isEmpty(snapToken)) {
            String message = "snap token cannot be null or empty, please checkout transaction to get snapToken";
            Logger.e(TAG, message);

            if (transactionFinishedCallback != null) {
                transactionFinishedCallback.onTransactionFinished(new TransactionResult(TransactionResult.STATUS_INVALID, message));
            }

            return false;
        }
        return true;
    }

    private boolean merchantBaseUrlAvailable() {
        if (TextUtils.isEmpty(merchantServerUrl)) {
            String message = "merchant base url is required if you want to do checkout from SDK, please set merchant base url on Midtrans SDK";
            Logger.e(TAG, message);

            if (transactionFinishedCallback != null) {
                transactionFinishedCallback.onTransactionFinished(new TransactionResult(TransactionResult.STATUS_INVALID, message));
            }

            return false;
        }
        return true;
    }

    /**
     * This will start actual execution of credit card UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startCreditCardUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runCreditCard(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Bank Transfer UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startBankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runBankTransfer(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Bank Transfer UI flow using Permata.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startPermataBankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runPermataBankTransfer(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Bank Transfer UI flow using Mandiri.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startMandiriBankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runMandiriBankTransfer(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Bank Transfer UI flow using BNI.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startBniBankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runBniBankTransfer(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Bank Transfer UI flow using BCA.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startBCABankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runBCABankTransfer(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Bank Transfer UI flow using Other banks.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startOtherBankTransferUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runOtherBankTransfer(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of GO-PAY UI Flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startGoPayUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runGoPay(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of BCA KlikPay UI Flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startBCAKlikPayUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runBCAKlikPay(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Klik BCA UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startKlikBCAUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runKlikBCA(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Mandiri Clickpay UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startMandiriClickpayUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runMandiriClickpay(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Mandiri E-Cash UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startMandiriECashUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runMandiriECash(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of CIMB Clicks UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startCIMBClicksUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runCIMBClicks(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of BRI Epay UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startBRIEpayUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runBRIEpay(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Telkomsel Cash UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startTelkomselCashUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runTelkomselCash(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Indosat Dompetku UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startIndosatDompetkuUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runIndosatDompetku(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Indomaret UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token.
     */
    private void startIndomaretUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runIndomaret(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Kioson UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startKiosonUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runKioson(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of XL Tunai UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startXlTunaiUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runXlTunai(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * This will start actual execution of Gift Card UI flow.
     *
     * @param context   activity context.
     * @param snapToken checkout token
     */
    private void startGiftCardUIFlow(@NonNull Context context, String snapToken) {
        if (transactionRequest != null && !isRunning()) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runGci(context, snapToken);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, Constants.MESSAGE_ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * It will run background task to get snap transaction details.
     *
     * @param authenticationToken Snap authentication token
     * @param callback            transaction option callback
     */
    public void getTransactionOptions(@NonNull String authenticationToken, @NonNull TransactionOptionsCallback callback) {
        if (callback == null) {
            Logger.d(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (!TextUtils.isEmpty(authenticationToken)) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.getTransactionOptions(authenticationToken, callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run background task to  checkout on merchant server to get authentication token
     *
     * @param userId   user identifier
     * @param callback checkout callback
     */
    public void checkout(@NonNull String userId, @NonNull CheckoutCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                TokenRequestModel model = SdkUtil.getSnapTokenRequestModel(transactionRequest);
                if (isEnableBuiltInTokenStorage()) {
                    model.setUserId(userId);
                }
                mSnapTransactionManager.checkout(model, callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run background task to  checkout on merchant server to get authentication token
     *
     * @param callback checkout callback
     */
    public void checkout(@NonNull CheckoutCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                TokenRequestModel model = SdkUtil.getSnapTokenRequestModel(transactionRequest);
                mSnapTransactionManager.checkout(model, callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backgrond task to charge payment using Credit Card
     *
     * @param authenticationToken    authentication token
     * @param creditCardPaymentModel model for creditcard payment
     * @param callback               transaction callback
     */
    public void paymentUsingCard(@NonNull String authenticationToken, CreditCardPaymentModel creditCardPaymentModel, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingCreditCard(authenticationToken,
                        SdkUtil.getCreditCardPaymentRequest(creditCardPaymentModel, transactionRequest), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backgrond task to charge payment using Credit Card with promo (discount)
     *
     * @param authenticationToken    authentication token
     * @param discountToken          discount (promo) token
     * @param discountAmount         discount (promo) amount
     * @param creditCardPaymentModel model for creditcard payment
     * @param callback               transaction callback
     */
    public void paymentUsingCard(@NonNull String authenticationToken, @NonNull String discountToken, Long discountAmount, CreditCardPaymentModel creditCardPaymentModel, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingCreditCard(authenticationToken,
                        SdkUtil.getCreditCardPaymentRequest(discountToken, discountAmount, creditCardPaymentModel, transactionRequest), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using Bank Transfer BCA
     *
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingBankTransferBCA(@NonNull String authenticationToken, @NonNull String email,
                                            @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBankTransferBCA(authenticationToken,
                        SdkUtil.getBankTransferPaymentRequest(email,
                                PaymentType.BCA_VA), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using Bank Transfer BNI
     *
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingBankTransferBni(@NonNull String authenticationToken, @NonNull String email,
                                            @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBankTransferBni(authenticationToken,
                        SdkUtil.getBankTransferPaymentRequest(email,
                                PaymentType.BNI_VA), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using Bank Transfer Permata
     *
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingBankTransferPermata(@NonNull String authenticationToken, @NonNull String email, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBankTransferPermata(authenticationToken,
                        SdkUtil.getBankTransferPaymentRequest(email, PaymentType.PERMATA_VA),
                        callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using Bank Transfer BCA
     *
     * @param authenticationToken authentication token
     * @param userId              user id
     * @param callback            transaction callback
     */
    public void paymentUsingKlikBCA(@NonNull String authenticationToken, @NonNull String userId, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingKlikBCA(authenticationToken, SdkUtil.getKlikBCAPaymentRequest(userId,
                        PaymentType.KLIK_BCA), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using BCA Klik Pay
     *
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingBCAKlikpay(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBCAKlikpay(authenticationToken,
                        new BasePaymentRequest(PaymentType.BCA_KLIKPAY), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using Mandiri Bill Pay
     *
     * @param authenticationToken authentication token
     * @param callback            transactionCallback
     */
    public void paymentUsingMandiriBillPay(@NonNull String authenticationToken, @NonNull String email, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingMandiriBillPay(authenticationToken,
                        SdkUtil.getBankTransferPaymentRequest(email, PaymentType.E_CHANNEL),
                        callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment Mandiri Click Pay
     * <p>
     * Deprecated, please see {@link com.midtrans.sdk.corekit.core.MidtransSDK#paymentUsingMandiriClickPay(String, NewMandiriClickPaymentParams, TransactionCallback)} (String, NewMandiriClickPayPaymentRequest, TransactionCallback)}
     *
     * @param authenticationToken authentication token
     * @param mandiriCardNumber   number of mandiri card
     * @param input3              5 digit generated number
     * @param tokenResponse       token
     * @param callback            transaction callback
     */
    @Deprecated
    public void paymentUsingMandiriClickPay(@NonNull String authenticationToken, @NonNull String mandiriCardNumber,
                                            @NonNull String tokenResponse, @NonNull String input3, TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingMandiriClickPay(authenticationToken, SdkUtil.getMandiriClickPaymentRequest(
                        mandiriCardNumber, tokenResponse, input3, PaymentType.MANDIRI_CLICKPAY), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment Mandiri Click Pay
     *
     * @param snapToken     snap token
     * @param paymentParams mandiri clickpay payment params
     * @param callback      transaction callback
     */
    public void paymentUsingMandiriClickPay(String snapToken, NewMandiriClickPaymentParams paymentParams, TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingMandiriClickPay(snapToken, new NewMandiriClickPayPaymentRequest(PaymentType.MANDIRI_CLICKPAY, paymentParams), callback)
                ;
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using CIMB Click
     *
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingCIMBClick(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingCIMBClick(authenticationToken,
                        new BasePaymentRequest(PaymentType.CIMB_CLICKS), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using mandiri E-Cash
     *
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingMandiriEcash(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingMandiriEcash(authenticationToken,
                        new BasePaymentRequest(PaymentType.MANDIRI_ECASH), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using telkkomsel E-Cash
     *
     * @param authenticationToken authentication token
     * @param customerPhoneNumber user phone number
     * @param callback            transaction callback
     */
    public void paymentUsingTelkomselEcash(@NonNull String authenticationToken, @NonNull String customerPhoneNumber,
                                           @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (isNetworkAvailable()) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingTelkomselCash(authenticationToken,
                        new TelkomselEcashPaymentRequest(PaymentType.TELKOMSEL_CASH, new TelkomselCashPaymentParams(customerPhoneNumber)),
                        callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using XL Tunai
     *
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingXLTunai(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingXLTunai(authenticationToken,
                        new BasePaymentRequest(PaymentType.XL_TUNAI), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using Indomaret
     *
     * @param authenticationToken authentication token
     * @param callback            transction callback
     */
    public void paymentUsingIndomaret(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingIndomaret(authenticationToken,
                        new BasePaymentRequest(PaymentType.INDOMARET), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using indosat dompetku
     *
     * @param authenticationToken authentication token
     * @param msisdn              msisdn number
     * @param callback            transaction callback
     */
    public void paymentUsingIndosatDompetku(@NonNull String authenticationToken, @NonNull String msisdn, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingIndosatDompetku(authenticationToken,
                        new IndosatDompetkuPaymentRequest(PaymentType.INDOSAT_DOMPETKU, new IndosatDompetkuPaymentParams(msisdn)), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using Kiosan
     *
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingKiosan(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingKiosan(authenticationToken,
                        new BasePaymentRequest(PaymentType.KIOSON), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using Epay BRI
     *
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingEpayBRI(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (transactionRequest != null) {
            if (isNetworkAvailable()) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBRIEpay(authenticationToken,
                        new BasePaymentRequest(PaymentType.BRI_EPAY), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using Bank Transfer All Bank
     *
     * @param authenticationToken authentication token
     * @param email               user email
     * @param callback            transaction callback
     */
    public void paymentUsingBankTransferAllBank(@NonNull String authenticationToken, @NonNull String email, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }
        if (transactionRequest != null) {
            if (isNetworkAvailable()) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBankTransferAllBank(authenticationToken,
                        SdkUtil.getBankTransferPaymentRequest(email, PaymentType.ALL_VA),
                        callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * @param authenticationToken Snap token
     * @param cardNumber          Gift Card Number
     * @param password            Gift Card Password / PIN
     * @param callback            transaction callback
     */
    public void paymentUsingGCI(@NonNull String authenticationToken, @NonNull String cardNumber, @NonNull String password,
                                @NonNull TransactionCallback callback) {

        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (isNetworkAvailable()) {
            isRunning = true;
            mSnapTransactionManager.paymentUsingGCI(authenticationToken,
                    SdkUtil.getGCIPaymentRequest(cardNumber, password),
                    callback);
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to charge payment using GoPay
     *
     * @param snapToken
     */
    public void paymentUsingGoPay(String snapToken, TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (isNetworkAvailable()) {
            isRunning = true;
            mSnapTransactionManager.paymentUsingGoPay(snapToken, SdkUtil.getGoPayPaymentRequest(), callback);
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to authorize GoPay Payment
     *
     * @param verificationCode gopay verification code
     * @param snapToken        snap token
     * @param callback         GoPayAuthorizationCallback
     */
    public void authorizeGoPayPayment(String snapToken, String verificationCode, TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (isNetworkAvailable()) {
            isRunning = true;
            mSnapTransactionManager.authorizeGoPayPayment(snapToken, SdkUtil.getGoPayAuthorizationRequest(verificationCode), callback);
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to resend GoPay authorization
     *
     * @param snapToken
     */
    public void resendGopayAuthorization(String snapToken, GoPayResendAuthorizationCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (isNetworkAvailable()) {
            isRunning = true;
            mSnapTransactionManager.resendGoPayAuthorization(snapToken, callback);
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }


    /**
     * It will run backround task to charge payment using Danamon Online
     *
     * @param snapToken
     */
    public void paymentUsingDanamonOnline(String snapToken, TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (isNetworkAvailable()) {
            isRunning = true;
            mSnapTransactionManager.paymentUsingDanamonOnline(snapToken, SdkUtil.getDanamonOnlinePaymentRequest(), callback);
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }


    /**
     * It will run backround task to register card PAPI(Payment API) Backend
     *
     * @param cardNumber   credit card number
     * @param cardCvv      credit card cvv
     * @param cardExpMonth credit card expired month
     * @param cardExpYear  credit card expired year
     * @param callback     Credit card registration callback
     */
    public void cardRegistration(@NonNull String cardNumber,
                                 @NonNull String cardCvv, @NonNull String cardExpMonth,
                                 @NonNull String cardExpYear, @NonNull CardRegistrationCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (isNetworkAvailable()) {
            isRunning = true;
            mSnapTransactionManager.cardRegistration(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientKey,
                    callback);
        } else {
            isRunning = false;
            Logger.e(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER);
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to save card to merchant server
     *
     * @param userId   id user
     * @param requests save card request model
     * @param callback save card callback
     */
    public void saveCards(@NonNull String userId, @NonNull ArrayList<SaveCardRequest> requests,
                          @NonNull SaveCardCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (requests != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.saveCards(userId, requests, callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            }
        } else {
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run backround task to get card from merchant server
     *
     * @param userId   id user
     * @param callback Get credit card callback
     */
    public void getCards(@NonNull String userId, GetCardCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (Utils.isNetworkAvailable(context)) {
            isRunning = true;
            mSnapTransactionManager.getCards(userId, callback);
        } else {
            isRunning = false;
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * @param callback of bank bins;
     */
    public void getBankBins(@NonNull BankBinsCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (Utils.isNetworkAvailable(context)) {
            mSnapTransactionManager.getBankBins(callback);
        } else {
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }


    /**
     * it will get bank points (BNI or Mandiri) from snap backend
     *
     * @param cardToken credit card token
     * @param callback  bni point callback instance
     */
    public void getBanksPoint(String cardToken, @NonNull BanksPointCallback callback) {
        if (callback == null) {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return;
        }

        if (Utils.isNetworkAvailable(context)) {
            mSnapTransactionManager.getBanksPoint(readAuthenticationToken(), cardToken, callback);
        } else {
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run background task to delete saved card from token storage.
     *
     * @param authenticationToken authentication or snap Token.
     * @param maskedCard          masked card.
     * @param callback            delete card callback to be called after background task was
     *                            finished.
     */
    public void deleteCard(@NonNull String authenticationToken, String maskedCard, DeleteCardCallback callback) {
        if (isNetworkAvailable()) {
            mSnapTransactionManager.deleteCard(authenticationToken, maskedCard, callback);
        } else {
            callback.onError(new RuntimeException(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * It will run background task to delete saved card from token storage.
     *
     * @param authenticationToken authentication or snap Token.
     * @param callback            delete card callback to be called after background task was
     *                            finished.
     */
    public void getTransactionStatus(String authenticationToken, GetTransactionStatusCallback callback) {
        if (isNetworkAvailable()) {
            mSnapTransactionManager.getTransactionStatus(authenticationToken, callback);
        } else {
            callback.onError(new RuntimeException(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
        }
    }

    /**
     * it will change SDK configuration
     *
     * @param baseUrl           SDK api base url
     * @param merchantUrl       merchant base url
     * @param merchantClientKey merchant client key
     * @param requestTimeout    is maximum time used to make http request
     */
    public void changeSdkConfig(String baseUrl, String merchantUrl, String merchantClientKey, int requestTimeout) {
        this.sdkBaseUrl = baseUrl;
        this.merchantServerUrl = merchantUrl;
        this.clientKey = merchantClientKey;
        this.requestTimeOut = requestTimeout;

        mSnapTransactionManager = new SnapTransactionManager(MidtransRestAdapter.getSnapRestAPI(sdkBaseUrl, requestTimeout),
                MidtransRestAdapter.getMerchantApiClient(merchantServerUrl, requestTimeout),
                MidtransRestAdapter.getVeritransApiClient(BuildConfig.BASE_URL, requestTimeout));
        mSnapTransactionManager.setSDKLogEnabled(isLogEnabled);
    }

    /**
     * It will notify merchant apps that fransaction has been finished
     *
     * @param result Transaction Result
     */
    public void notifyTransactionFinished(TransactionResult result) {
        if (transactionFinishedCallback != null) {
            transactionFinishedCallback.onTransactionFinished(result);
        } else {
            Logger.e(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
        }
    }

    public IScanner getExternalScanner() {
        return externalScanner;
    }

    void setTransactionManager(SnapTransactionManager snapTransactionManager) {
        this.mSnapTransactionManager = snapTransactionManager;
    }

    public SnapTransactionManager getmSnapTransactionManager() {
        return this.mSnapTransactionManager;
    }

    boolean isNetworkAvailable() {
        return Utils.isNetworkAvailable(context);
    }

    public void releaseResource() {
        this.isRunning = false;
    }

    public String getSdkBaseUrl() {
        return sdkBaseUrl;
    }

    public int getRequestTimeOut() {
        return requestTimeOut;
    }

    public boolean isEnableBuiltInTokenStorage() {
        return enableBuiltInTokenStorage;
    }

    public UIKitCustomSetting getUIKitCustomSetting() {
        if (UIKitCustomSetting == null) {
            this.UIKitCustomSetting = new UIKitCustomSetting();
        }
        return UIKitCustomSetting;
    }

    public void setUIKitCustomSetting(UIKitCustomSetting uiKitCustomSetting) {
        if (uiKitCustomSetting != null) {
            this.UIKitCustomSetting = uiKitCustomSetting;
        }
    }

    public CreditCard getCreditCard() {
        Transaction transaction = getTransaction();
        if (transaction.getCreditCard() == null) {
            transaction.setCreditCard(new CreditCard());
        }

        return transaction.getCreditCard();
    }

    public void setCreditCard(CreditCard creditCard) {
        if (creditCard != null) {
            getTransaction().setCreditCard(creditCard);
        }
    }

    public List<PromoResponse> getPromoResponses() {
        return promoResponses;
    }

    public void setPromoResponses(List<PromoResponse> promoResponses) {
        this.promoResponses = promoResponses;
    }

    public BaseColorTheme getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(BaseColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public ArrayList<String> getBanksPointEnabled() {
        MerchantData merchantData = getMerchantData();
        if (merchantData.getPointBanks() == null) {
            ArrayList<String> newPointBanks = new ArrayList<>();
            merchantData.setPointBanks(newPointBanks);
        }

        return merchantData.getPointBanks();
    }

    public void setTransactionFinishedCallback(TransactionFinishedCallback transactionFinishedCallback) {
        this.transactionFinishedCallback = transactionFinishedCallback;
    }

    public MerchantData getMerchantData() {
        Transaction transaction = getTransaction();
        if (transaction.getMerchantData() == null) {
            transaction.setMerchantData(new MerchantData());
        }
        return transaction.getMerchantData();
    }

    public Transaction getTransaction() {
        if (this.transaction == null) {
            this.transaction = new Transaction();
        }
        return this.transaction;
    }

    public void setTransaction(Transaction transaction) {
        if (transaction != null) {
            this.transaction = transaction;
        }
    }

    public CardRegistrationCallback getUiCardRegistrationCallback() {
        return this.cardRegistrationCallback;
    }
}