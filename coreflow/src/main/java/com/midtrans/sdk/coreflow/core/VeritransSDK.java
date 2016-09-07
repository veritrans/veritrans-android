package com.midtrans.sdk.coreflow.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.coreflow.callback.CardRegistrationCallback;
import com.midtrans.sdk.coreflow.callback.GetCardCallback;
import com.midtrans.sdk.coreflow.callback.GetCardTokenCallback;
import com.midtrans.sdk.coreflow.callback.TransactionOptionsCallback;
import com.midtrans.sdk.coreflow.models.PaymentMethodsModel;
import com.midtrans.sdk.coreflow.models.SaveCardRequest;
import com.midtrans.sdk.coreflow.models.TokenRequestModel;
import com.midtrans.sdk.coreflow.models.UserDetail;
import com.midtrans.sdk.coreflow.models.snap.payment.TelkomselEcashPaymentRequest;

import java.util.ArrayList;

import com.midtrans.sdk.coreflow.BuildConfig;
import com.midtrans.sdk.coreflow.R;

import com.midtrans.sdk.coreflow.callback.CheckoutCallback;
import com.midtrans.sdk.coreflow.callback.SaveCardCallback;
import com.midtrans.sdk.coreflow.callback.TransactionCallback;
import com.midtrans.sdk.coreflow.callback.TransactionFinishedCallback;
import com.midtrans.sdk.coreflow.models.BBMCallBackUrl;
import com.midtrans.sdk.coreflow.models.CardTokenRequest;
import com.midtrans.sdk.coreflow.models.snap.TransactionResult;
import com.midtrans.sdk.coreflow.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.coreflow.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.coreflow.utilities.Utils;

/**
 * Created by shivam on 10/19/15.
 */
public class VeritransSDK {

    private static final String TAG = "VeritransSDK";
    public static final String BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY = "bill info and item " +
            "details are necessary.";
    private static final String ADD_TRANSACTION_DETAILS = "Add transaction request details.";
    private static final String LOCAL_DATA_PREFERENCES = "local.data";
    private static SharedPreferences mPreferences = null;
    private static VeritransSDK veritransSDK;
    private  boolean isLogEnabled = false;
    private TransactionFinishedCallback transactionFinishedCallback;
    protected boolean isRunning = false;
    ISdkFlow uiflow;
    private MixpanelAnalyticsManager mMixpanelAnalyticsManager;
    private Context context = null;
    private int themeColor;
    private String clientKey = null;
    private String merchantServerUrl = null;
    private String defaultText = null;
    private String boldText = null;
    private String semiBoldText = null;
    private String merchantName = null;
    private IScanner externalScanner;
    private SnapTransactionManager mSnapTransactionManager;
    private String merchantLogo = null;
    private TransactionRequest transactionRequest = null;
    private ArrayList<PaymentMethodsModel> selectedPaymentMethods = new ArrayList<>();
    private BBMCallBackUrl mBBMCallBackUrl = null;
    private String sdkBaseUrl = "";
    private int requestTimeOut = 10;

    private VeritransSDK(@NonNull BaseSdkBuilder sdkBuilder) {
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
        themeColor = sdkBuilder.colorThemeResourceId;
        this.isLogEnabled = sdkBuilder.enableLog;

        this.mMixpanelAnalyticsManager = new MixpanelAnalyticsManager(VeritransRestAdapter.getMixpanelApi(requestTimeOut));

        this.mSnapTransactionManager = new SnapTransactionManager(sdkBuilder.context, VeritransRestAdapter.getSnapRestAPI(sdkBaseUrl, requestTimeOut),
                VeritransRestAdapter.getMerchantApiClient(merchantServerUrl, requestTimeOut),
                VeritransRestAdapter.getVeritransApiClient(BuildConfig.BASE_URL, requestTimeOut));
        this.mSnapTransactionManager.setAnalyticsManager(this.mMixpanelAnalyticsManager);
        this.mSnapTransactionManager.setSDKLogEnabled(isLogEnabled);

        initializeTheme();
        initializeSharedPreferences();
    }

    /**
     * get Veritrans SDK instance
     *
     * @param sdkBuilder SDK Coreflow Builder
     */
    protected static VeritransSDK delegateInstance(@NonNull BaseSdkBuilder sdkBuilder) {
        if (sdkBuilder != null) {
            veritransSDK = new VeritransSDK(sdkBuilder);
        } else {
            Logger.e("sdk is not initialized.");
        }
        return veritransSDK;
    }

    /**
     * Returns instance of midtrans sdk.
     *
     * @return VeritransSDK instance
     */
    public static VeritransSDK getInstance() {
        return veritransSDK;
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
     * @param preferences
     */
    static void setmPreferences(SharedPreferences preferences) {
        mPreferences = preferences;
    }

    private void initializeSharedPreferences() {
        mPreferences = context.getSharedPreferences(LOCAL_DATA_PREFERENCES, Context.MODE_PRIVATE);
    }

    private void initializeTheme() {
        themeColor = context.getResources().getColor(R.color.colorPrimary);
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
     *
     * @param defaultText
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

    public int getThemeColor() {
        return themeColor;
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

    public Context getContext() {
        return context;
    }

    public String getMerchantToken() {
        UserDetail userDetail = null;
        try {
            userDetail = LocalDataHandler.readObject(context.getString(R.string.user_details), UserDetail.class);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        String merchantToken = userDetail.getMerchantToken();
        Logger.i("merchantToken:" + merchantToken);
        return merchantToken;
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

    public ArrayList<PaymentMethodsModel> getSelectedPaymentMethods() {
        return selectedPaymentMethods;
    }

    public void setSelectedPaymentMethods(ArrayList<PaymentMethodsModel> selectedPaymentMethods) {
        this.selectedPaymentMethods = selectedPaymentMethods;
    }

    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    /**
     * It will execute an api request to retrieve a token.
     *
     * @param cardTokenRequest token request object
     * @param callback get card token callback
     */
    public void getCardToken(CardTokenRequest cardTokenRequest, GetCardTokenCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (cardTokenRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.getToken(cardTokenRequest, callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
                Logger.e(context.getString(R.string.error_unable_to_connect));
            }

        } else {
            Logger.e(context.getString(R.string.error_invalid_data_supplied));
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }


    /**
     * Set transaction information that you want to execute.
     *
     * @param transactionRequest transaction request  object
     *
     */
    public void setTransactionRequest(TransactionRequest transactionRequest) {
        if (!isRunning) {

            if (transactionRequest != null) {
                this.transactionRequest = transactionRequest;
            } else {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            }

        } else {
            Logger.e(TAG, context.getString(R.string.error_already_running));
        }
    }

    /**
     * This will start actual execution of save card UI flow.
     *
     * @param context current activity.
     */
    public void startRegisterCardUIFlow(@NonNull Context context) {
        if (uiflow != null) {
            uiflow.runRegisterCard(context);
        }
    }

    /**
     * This will start actual execution of transaction. if you have enabled an ui then it will start
     * activity according to it.
     *
     * @param context current activity.
     */
    public void startPaymentUiFlow(Context context) {

        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants
                    .PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runUIFlow(context);
                }
            }

        } else {
            if (transactionRequest == null) {
                Logger.e(TAG, ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(TAG, context.getString(R.string.error_already_running));
            }
        }
    }

    public BBMCallBackUrl getBBMCallBackUrl() {
        return mBBMCallBackUrl;
    }


    /**
     * It will fetch the Offers from merchant server.
     */
    public void getOffersList() {
        if (isNetworkAvailable()) {
            isRunning = true;
//            mSnapTransactionManager.getOffers(readAuthenticationToken());
        } else {
            isRunning = false;
            Logger.e(TAG, context.getString(R.string.error_unable_to_connect));
        }
    }

    /**
     * It will run background task to get snap transaction details.
     *
     * @param snapToken Snap authentication token
     * @param callback transaction option callback
     */
    public void getTransactionOptions(@NonNull String snapToken, @NonNull TransactionOptionsCallback callback) {
        if(callback == null){
            Logger.d(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (!TextUtils.isEmpty(snapToken)) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.getTransactionOptions(snapToken, callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run background task to  checkout on merchant server.
     * @param callback checkout callback
     */
    public void checkout(@NonNull CheckoutCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                TokenRequestModel model = SdkUtil.getSnapTokenRequestModel(transactionRequest);
                mSnapTransactionManager.checkout(model, callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backgrond task to charge payment using Credit Card
     * @param tokenId authentication token
     * @param cardToken card token form PAPI backend
     * @param saveCard is saving credit card
     * @param callback transaction callback
     */
    public void snapPaymentUsingCard(@NonNull String tokenId, @NonNull String cardToken, boolean saveCard, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingCreditCard(SdkUtil.getCreditCardPaymentRequest(cardToken,
                        saveCard, transactionRequest, tokenId), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using Bank Transfer BCA
     *
     * @param tokenId authentication token
     * @param email user email
     * @param callback transaction callback
     */
    public void snapPaymentUsingBankTransferBCA(@NonNull String tokenId, @NonNull String email,
                                                @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBankTransferBCA(SdkUtil.getBankTransferPaymentRequest(email, tokenId),
                        callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using Bank Transfer Permata
     *
     * @param tokenId authentication token
     * @param email user email
     * @param callback transaction callback
     */
    public void snapPaymentUsingBankTransferPermata(@NonNull String tokenId, @NonNull String email, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBankTransferPermata(SdkUtil.getBankTransferPaymentRequest(email, tokenId),
                        callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using Bank Transfer BCA
     *
     * @param tokenId authentication token
     * @param userId user id
     * @param callback transaction callback
     */
    public void snapPaymentUsingKlikBCA(@NonNull String tokenId, @NonNull String userId, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingKlikBCA(SdkUtil.getKlikBCAPaymentRequest(userId, tokenId),
                        callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using BCA Klik Pay
     *
     * @param tokenId authentication token
     * @param callback transaction callback
     */
    public void snapPaymentUsingBCAKlikpay(@NonNull String tokenId, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBCAKlikpay(new BasePaymentRequest(tokenId), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using Mandiri Bill Pay
     *
     * @param token authentication token
     * @param email user email
     * @param callback
     */
    public void snapPaymentUsingMandiriBillPay(@NonNull String token, @NonNull String email, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingMandiriBillPay(SdkUtil.getBankTransferPaymentRequest(email, token),
                        callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment Mandiri Click Pay
     *
     * @param token authentication token
     * @param mandiriCardNumber  number of mandiri card
     * @param input3 5 digit generated number
     * @param tokenResponse token
     * @param callback transaction callback
     */
    public void snapPaymentUsingMandiriClickPay(@NonNull String token, @NonNull String mandiriCardNumber,
                                                @NonNull String tokenResponse, @NonNull String input3, TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingMandiriClickPay(SdkUtil.getMandiriClickPaymentRequest(token,
                        mandiriCardNumber, tokenResponse, input3), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using CIMB Click
     *
     * @param token authentication token
     * @param callback transaction callback
     */
    public void snapPaymentUsingCIMBClick(@NonNull String token, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingCIMBClick(new BasePaymentRequest(token), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using mandiri E-Cash
     *
     * @param token authentication token
     * @param callback transaction callback
     */
    public void snapPaymentUsingMandiriEcash(@NonNull String token, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingMandiriEcash(new BasePaymentRequest(token), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using telkkomsel E-Cash
     *
     * @param token authentication token
     * @param customerPhoneNumber user phone number
     * @param callback transaction callback
     */
    public void snapPaymentUsingTelkomselEcash(@NonNull String token, @NonNull String customerPhoneNumber,
                                               @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingTelkomselCash(new TelkomselEcashPaymentRequest(token, customerPhoneNumber),
                        callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using XL Tunai
     *
     * @param token authentication token
     * @param callback transaction callback
     */
    public void snapPaymentUsingXLTunai(@NonNull String token, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingXLTunai(new BasePaymentRequest(token), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using Indomaret
     *
     * @param token authentication token
     * @param callback transction callback
     */
    public void snapPaymentUsingIndomaret(@NonNull String token, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingIndomaret(new BasePaymentRequest(token), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using indosat dompetku
     *
     * @param token authentication token
     * @param msisdn msisdn number
     * @param callback transaction callback
     */
    public void snapPaymentUsingIndosatDompetku(@NonNull String token, @NonNull String msisdn, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingIndosatDompetku(new IndosatDompetkuPaymentRequest(token, msisdn), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using Kiosan
     *
     * @param token authentication token
     * @param callback transaction callback
     */
    public void snapPaymentUsingKiosan(@NonNull String token, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingKiosan(new BasePaymentRequest(token), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using Epay BRI
     *
     * @param token authentication token
     * @param callback transaction callback
     */
    public void snapPaymentUsingEpayBRI(@NonNull String token, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBRIEpay(new BasePaymentRequest(token), callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to charge payment using Bank Transfer All Bank
     *
     * @param tokenId authentication token
     * @param email user email
     * @param callback transaction callback
     */
    public void snapPaymentUsingBankTransferAllBank(@NonNull String tokenId, @NonNull String email, @NonNull TransactionCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBankTransferAllBank(SdkUtil.getBankTransferPaymentRequest(email, tokenId),
                        callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to register card PAPI(Payment API) Backend
     *
     * @param cardNumber credit card number
     * @param cardCvv credit card cvv
     * @param cardExpMonth credit card expired month
     * @param cardExpYear credit card expired year
     * @param callback Credit card registration callback
     */
    public void snapCardRegistration(@NonNull String cardNumber,
                                 @NonNull String cardCvv, @NonNull String cardExpMonth,
                                 @NonNull String cardExpYear, @NonNull CardRegistrationCallback callback) {
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if(Utils.isNetworkAvailable(context)){
            isRunning = true;
            mSnapTransactionManager.cardRegistration(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientKey,
                    callback);
        }else{
            isRunning = false;
            Logger.e(context.getString(R.string.error_unable_to_connect));
            callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
        }
    }

    /**
     * It will run backround task to save card to merchant server
     *
     * @param userId id user
     * @param requests  save card request model
     * @param callback save card callback
     */
    public void snapSaveCard(@NonNull String userId, @NonNull ArrayList<SaveCardRequest> requests,
                             @NonNull SaveCardCallback callback){
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if(requests != null){
            if(Utils.isNetworkAvailable(context)){
                isRunning = true;
                mSnapTransactionManager.saveCards(userId, requests, callback);
            }else{
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        }else{
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run backround task to get card from merchant server
     *
     * @param userId id user
     * @param  callback Get credit card callback
     */
    public void snapGetCards(@NonNull String userId, GetCardCallback callback){
        if(callback == null){
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if(Utils.isNetworkAvailable(context)){
            isRunning = true;
            mSnapTransactionManager.getCards(userId, callback);
        }else{
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
        }
    }

    /**
     * it will change SDK configuration
     *
     * @param baseUrl  SDK api base url
     * @param merchantUrl merchant base url
     * @param merchantClientKey merchant client key
     * @param requestTimeout is maximum time used to make http request
     */
    public void changeSdkConfig(String baseUrl, String merchantUrl, String merchantClientKey, int requestTimeout) {
        this.sdkBaseUrl = baseUrl;
        this.merchantServerUrl = merchantUrl;
        this.clientKey = merchantClientKey;
        this.requestTimeOut = requestTimeout;

        mSnapTransactionManager = new SnapTransactionManager(context, VeritransRestAdapter.getSnapRestAPI(sdkBaseUrl, requestTimeout),
                VeritransRestAdapter.getMerchantApiClient(merchantServerUrl, requestTimeout),
                VeritransRestAdapter.getVeritransApiClient(BuildConfig.BASE_URL, requestTimeout));

        mMixpanelAnalyticsManager = new MixpanelAnalyticsManager(VeritransRestAdapter.getMixpanelApi(requestTimeout));
        mSnapTransactionManager.setAnalyticsManager(this.mMixpanelAnalyticsManager);
        mSnapTransactionManager.setSDKLogEnabled(isLogEnabled);
    }

    /**
     * It will notify merchant apps that fransaction has been finished
     *
     * @param result Transaction Result
     */
    public void notifyTransactionFinished(TransactionResult result){
        if(transactionFinishedCallback != null){
            transactionFinishedCallback.onTransactionFinished(result);
        } else{
            Logger.i(TAG, context.getString(R.string.transaction_finished_callback_unimplemented));
        }
    }

    public IScanner getExternalScanner() {
        return externalScanner;
    }

    void setSnapTransactionManager(SnapTransactionManager snapTransactionManager) {
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
}