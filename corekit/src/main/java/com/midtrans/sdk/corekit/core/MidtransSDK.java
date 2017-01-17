package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.analytics.MixpanelAnalyticsManager;
import com.midtrans.sdk.corekit.BuildConfig;
import com.midtrans.sdk.corekit.R;
import com.midtrans.sdk.corekit.callback.BNIPointsCallback;
import com.midtrans.sdk.corekit.callback.BankBinsCallback;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.models.BBMCallBackUrl;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.corekit.models.snap.CreditCardPaymentModel;
import com.midtrans.sdk.corekit.models.snap.SavedToken;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.corekit.models.snap.params.IndosatDompetkuPaymentParams;
import com.midtrans.sdk.corekit.models.snap.params.TelkomselCashPaymentParams;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;
import com.midtrans.sdk.corekit.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivam on 10/19/15.
 */
public class MidtransSDK {

    public static final String BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY = "bill info and item " +
            "details are necessary.";
    private static final String TAG = "MidtransSDK";
    private static final String ADD_TRANSACTION_DETAILS = "Add transaction request details.";
    private static final String LOCAL_DATA_PREFERENCES = "local.data";
    private static SharedPreferences mPreferences = null;
    private static MidtransSDK midtransSDK;
    protected boolean isRunning = false;
    ISdkFlow uiflow;
    private UIKitCustomSetting UIKitCustomSetting;
    private boolean isLogEnabled = false;
    private TransactionFinishedCallback transactionFinishedCallback;
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
    private List<SavedToken> savedTokens = new ArrayList<>();
    private boolean enableBuiltInTokenStorage;
    private BBMCallBackUrl mBBMCallBackUrl = null;
    private String sdkBaseUrl = "";
    private int requestTimeOut = 10;
    private String flow = null;
    private CreditCard creditCard = new CreditCard();
    private ArrayList<String> pointBanks;

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
        themeColor = sdkBuilder.colorThemeResourceId;
        this.isLogEnabled = sdkBuilder.enableLog;
        this.enableBuiltInTokenStorage = sdkBuilder.enableBuiltInTokenStorage;
        this.UIKitCustomSetting = sdkBuilder.UIKitCustomSetting == null ? new UIKitCustomSetting() : sdkBuilder.UIKitCustomSetting;
        this.flow = sdkBuilder.flow;

        this.mSnapTransactionManager = new SnapTransactionManager(sdkBuilder.context, MidtransRestAdapter.getSnapRestAPI(sdkBaseUrl, requestTimeOut),
                MidtransRestAdapter.getMerchantApiClient(merchantServerUrl, requestTimeOut),
                MidtransRestAdapter.getVeritransApiClient(BuildConfig.BASE_URL, requestTimeOut));
        this.mMixpanelAnalyticsManager = new MixpanelAnalyticsManager(BuildConfig.VERSION_NAME, SdkUtil.getDeviceId(context), clientKey, getFlow(flow));
        this.mSnapTransactionManager.setAnalyticsManager(mMixpanelAnalyticsManager);
        this.mSnapTransactionManager.setSDKLogEnabled(isLogEnabled);

        initializeTheme();
        initializeSharedPreferences();
    }

    /**
     * get Veritrans SDK instance
     *
     * @param sdkBuilder SDK Coreflow Builder
     */
    protected static MidtransSDK delegateInstance(@NonNull BaseSdkBuilder sdkBuilder) {
        if (sdkBuilder != null) {
            midtransSDK = new MidtransSDK(sdkBuilder);
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

    private String getFlow(String flow) {
        if (flow.equalsIgnoreCase(BaseSdkBuilder.CORE_FLOW)) {
            return MixpanelAnalyticsManager.CORE_FLOW;
        } else if (flow.equalsIgnoreCase(BaseSdkBuilder.UI_FLOW)) {
            return MixpanelAnalyticsManager.UI_FLOW;
        } else {
            return MixpanelAnalyticsManager.WIDGET;
        }
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
     */
    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
        this.mMixpanelAnalyticsManager = new MixpanelAnalyticsManager(BuildConfig.VERSION_NAME, SdkUtil.getDeviceId(context), merchantName, getFlow(flow));
        this.mSnapTransactionManager.setAnalyticsManager(mMixpanelAnalyticsManager);
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
        return mMixpanelAnalyticsManager;
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
     * Set transaction information that you want to execute.
     *
     * @param transactionRequest transaction request  object
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
     * It will execute an api request to retrieve a authentication token.
     *
     * @param cardTokenRequest get card token  request object
     * @param callback         get card token callback
     */
    public void getCardToken(CardTokenRequest cardTokenRequest, CardTokenCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (cardTokenRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.getToken(readAuthenticationToken(), cardTokenRequest, callback);
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
     * This will start actual execution of credit card UI flow.
     *
     * @param context activity context.
     */
    public void startCreditCardUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants
                    .PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runCreditCard(context);
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

    /**
     * This will start actual execution of Bank Transfer UI flow.
     *
     * @param context activity context.
     */
    public void startBankTransferUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runBankTransfer(context);
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

    /**
     * This will start actual execution of Bank Transfer UI flow using Permata.
     *
     * @param context activity context.
     */
    public void startPermataBankTransferUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runPermataBankTransfer(context);
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

    /**
     * This will start actual execution of Bank Transfer UI flow using Mandiri.
     *
     * @param context activity context.
     */
    public void startMandiriBankTransferUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runMandiriBankTransfer(context);
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

    /**
     * This will start actual execution of Bank Transfer UI flow using BCA.
     *
     * @param context activity context.
     */
    public void startBCABankTransferUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runBCABankTransfer(context);
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

    /**
     * This will start actual execution of Bank Transfer UI flow using Other banks.
     *
     * @param context activity context.
     */
    public void startOtherBankTransferUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runOtherBankTransfer(context);
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

    /**
     * This will start actual execution of BCA KlikPay UI Flow.
     *
     * @param context activity context.
     */
    public void startBCAKlikPayUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runBCAKlikPay(context);
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

    /**
     * This will start actual execution of Klik BCA UI flow.
     *
     * @param context activity context.
     */
    public void startKlikBCAUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runKlikBCA(context);
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

    /**
     * This will start actual execution of Mandiri Clickpay UI flow.
     *
     * @param context activity context.
     */
    public void startMandiriClickpayUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runMandiriClickpay(context);
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

    /**
     * This will start actual execution of Mandiri E-Cash UI flow.
     *
     * @param context activity context.
     */
    public void startMandiriECashUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runMandiriECash(context);
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

    /**
     * This will start actual execution of CIMB Clicks UI flow.
     *
     * @param context activity context.
     */
    public void startCIMBClicksUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runCIMBClicks(context);
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

    /**
     * This will start actual execution of BRI Epay UI flow.
     *
     * @param context activity context.
     */
    public void startBRIEpayUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runBRIEpay(context);
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

    /**
     * This will start actual execution of Telkomsel Cash UI flow.
     *
     * @param context activity context.
     */
    public void startTelkomselCashUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runTelkomselCash(context);
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

    /**
     * This will start actual execution of Indosat Dompetku UI flow.
     *
     * @param context activity context.
     */
    public void startIndosatDompetkuUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runIndosatDompetku(context);
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

    /**
     * This will start actual execution of Indomaret UI flow.
     *
     * @param context activity context.
     */
    public void startIndomaretUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runIndomaret(context);
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

    /**
     * This will start actual execution of Kioson UI flow.
     *
     * @param context activity context.
     */
    public void startKiosonUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runKioson(context);
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

    /**
     * This will start actual execution of XL Tunai UI flow.
     *
     * @param context activity context.
     */
    public void startXlTunaiUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runXlTunai(context);
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

    /**
     * This will start actual execution of Gift Card UI flow.
     *
     * @param context activity context.
     */
    public void startGiftCardUIFlow(@NonNull Context context) {
        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants.PAYMENT_METHOD_NOT_SELECTED) {
                transactionRequest.enableUi(true);
                if (uiflow != null) {
                    uiflow.runGci(context);
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
     * @param authenticationToken Snap authentication token
     * @param callback            transaction option callback
     */
    public void getTransactionOptions(@NonNull String authenticationToken, @NonNull TransactionOptionsCallback callback) {
        if (callback == null) {
            Logger.d(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (!TextUtils.isEmpty(authenticationToken)) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.getTransactionOptions(authenticationToken, callback);
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
     * It will run background task to  checkout on merchant server to get authentication token
     *
     * @param userId   user identifier
     * @param callback checkout callback
     */
    public void checkout(@NonNull String userId, @NonNull CheckoutCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
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
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will run background task to  checkout on merchant server to get authentication token
     *
     * @param callback checkout callback
     */
    public void checkout(@NonNull CheckoutCallback callback) {
        if (callback == null) {
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
     *
     * @param authenticationToken    authentication token
     * @param creditCardPaymentModel model for creditcard payment
     * @param callback               transaction callback
     */
    public void paymentUsingCard(@NonNull String authenticationToken, CreditCardPaymentModel creditCardPaymentModel, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingCreditCard(authenticationToken,
                        SdkUtil.getCreditCardPaymentRequest(creditCardPaymentModel, transactionRequest), callback);
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
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingBankTransferBCA(@NonNull String authenticationToken, @NonNull String email,
                                            @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
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
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingBankTransferPermata(@NonNull String authenticationToken, @NonNull String email, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
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
     * @param authenticationToken authentication token
     * @param userId              user id
     * @param callback            transaction callback
     */
    public void paymentUsingKlikBCA(@NonNull String authenticationToken, @NonNull String userId, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingKlikBCA(authenticationToken, SdkUtil.getKlikBCAPaymentRequest(userId,
                        PaymentType.KLIK_BCA), callback);
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
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingBCAKlikpay(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBCAKlikpay(authenticationToken,
                        new BasePaymentRequest(PaymentType.BCA_KLIKPAY), callback);
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
     * @param authenticationToken authentication token
     * @param callback            transactionCallback
     */
    public void paymentUsingMandiriBillPay(@NonNull String authenticationToken, @NonNull String email, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
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
     * @param authenticationToken authentication token
     * @param mandiriCardNumber   number of mandiri card
     * @param input3              5 digit generated number
     * @param tokenResponse       token
     * @param callback            transaction callback
     */
    public void paymentUsingMandiriClickPay(@NonNull String authenticationToken, @NonNull String mandiriCardNumber,
                                            @NonNull String tokenResponse, @NonNull String input3, TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingMandiriClickPay(authenticationToken, SdkUtil.getMandiriClickPaymentRequest(
                        mandiriCardNumber, tokenResponse, input3, PaymentType.MANDIRI_CLICKPAY), callback);
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
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingCIMBClick(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingCIMBClick(authenticationToken,
                        new BasePaymentRequest(PaymentType.CIMB_CLICKS), callback);
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
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingMandiriEcash(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingMandiriEcash(authenticationToken,
                        new BasePaymentRequest(PaymentType.MANDIRI_ECASH), callback);
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
     * @param authenticationToken authentication token
     * @param customerPhoneNumber user phone number
     * @param callback            transaction callback
     */
    public void paymentUsingTelkomselEcash(@NonNull String authenticationToken, @NonNull String customerPhoneNumber,
                                           @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingTelkomselCash(authenticationToken,
                        new TelkomselEcashPaymentRequest(PaymentType.TELKOMSEL_CASH, new TelkomselCashPaymentParams(customerPhoneNumber)),
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
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingXLTunai(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingXLTunai(authenticationToken,
                        new BasePaymentRequest(PaymentType.XL_TUNAI), callback);
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
     * @param authenticationToken authentication token
     * @param callback            transction callback
     */
    public void paymentUsingIndomaret(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingIndomaret(authenticationToken,
                        new BasePaymentRequest(PaymentType.INDOMARET), callback);
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
     * @param authenticationToken authentication token
     * @param msisdn              msisdn number
     * @param callback            transaction callback
     */
    public void paymentUsingIndosatDompetku(@NonNull String authenticationToken, @NonNull String msisdn, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingIndosatDompetku(authenticationToken,
                        new IndosatDompetkuPaymentRequest(PaymentType.INDOSAT_DOMPETKU, new IndosatDompetkuPaymentParams(msisdn)), callback);
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
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingKiosan(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingKiosan(authenticationToken,
                        new BasePaymentRequest(PaymentType.KIOSON), callback);
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
     * @param authenticationToken authentication token
     * @param callback            transaction callback
     */
    public void paymentUsingEpayBRI(@NonNull String authenticationToken, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBRIEpay(authenticationToken,
                        new BasePaymentRequest(PaymentType.BRI_EPAY), callback);
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
     * @param authenticationToken authentication token
     * @param email               user email
     * @param callback            transaction callback
     */
    public void paymentUsingBankTransferAllBank(@NonNull String authenticationToken, @NonNull String email, @NonNull TransactionCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }
        if (transactionRequest != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.paymentUsingBankTransferAllBank(authenticationToken,
                        SdkUtil.getBankTransferPaymentRequest(email, PaymentType.ALL_VA),
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
     * @param authenticationToken Snap token
     * @param cardNumber          Gift Card Number
     * @param password            Gift Card Password / PIN
     * @param callback            transaction callback
     */
    public void paymentUsingGCI(@NonNull String authenticationToken, @NonNull String cardNumber, @NonNull String password,
                                @NonNull TransactionCallback callback) {

        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (Utils.isNetworkAvailable(context)) {
            isRunning = true;
            mSnapTransactionManager.paymentUsingGCI(authenticationToken,
                    SdkUtil.getGCIPaymentRequest(cardNumber, password),
                    callback);
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
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
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (Utils.isNetworkAvailable(context)) {
            isRunning = true;
            mSnapTransactionManager.cardRegistration(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientKey,
                    callback);
        } else {
            isRunning = false;
            Logger.e(context.getString(R.string.error_unable_to_connect));
            callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
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
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (requests != null) {
            if (Utils.isNetworkAvailable(context)) {
                isRunning = true;
                mSnapTransactionManager.saveCards(userId, requests, callback);
            } else {
                isRunning = false;
                callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
            }
        } else {
            callback.onError(new Throwable(context.getString(R.string.error_invalid_data_supplied)));
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
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (Utils.isNetworkAvailable(context)) {
            isRunning = true;
            mSnapTransactionManager.getCards(userId, callback);
        } else {
            isRunning = false;
            callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
        }
    }

    /**
     * @param callback of bank bins;
     */
    public void getBankBins(@NonNull BankBinsCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (Utils.isNetworkAvailable(context)) {
            mSnapTransactionManager.getBankBins(callback);
        } else {
            callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
        }
    }

    /**
     * it will get bni points from snap backend
     *
     * @param cardToken credit card token
     * @param callback  bni point callback instance
     */
    public void getBNIPoints(String cardToken, @NonNull BNIPointsCallback callback) {
        if (callback == null) {
            Logger.e(TAG, context.getString(R.string.callback_unimplemented));
            return;
        }

        if (Utils.isNetworkAvailable(context)) {
            mSnapTransactionManager.getBNIPoints(readAuthenticationToken(), cardToken, callback);
        } else {
            callback.onError(new Throwable(context.getString(R.string.error_unable_to_connect)));
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

        mSnapTransactionManager = new SnapTransactionManager(context, MidtransRestAdapter.getSnapRestAPI(sdkBaseUrl, requestTimeout),
                MidtransRestAdapter.getMerchantApiClient(merchantServerUrl, requestTimeout),
                MidtransRestAdapter.getVeritransApiClient(BuildConfig.BASE_URL, requestTimeout));
        mSnapTransactionManager.setAnalyticsManager(mMixpanelAnalyticsManager);
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
            Logger.i(TAG, context.getString(R.string.transaction_finished_callback_unimplemented));
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


    public void setSavedTokens(List<SavedToken> savedTokens) {
        this.savedTokens = savedTokens;
    }

    public boolean isEnableBuiltInTokenStorage() {
        return enableBuiltInTokenStorage;
    }

    public UIKitCustomSetting getUIKitCustomSetting() {
        return UIKitCustomSetting;
    }

    public void setUIKitCustomSetting(UIKitCustomSetting uiKitCustomSetting) {
        this.UIKitCustomSetting = uiKitCustomSetting;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        if (creditCard != null) {
            this.creditCard = creditCard;
        }
    }

    public void setPointBanks(ArrayList<String> pointBanks) {
        this.pointBanks = pointBanks;
    }

    public ArrayList<String> getPointBanks() {
        return pointBanks;
    }
}