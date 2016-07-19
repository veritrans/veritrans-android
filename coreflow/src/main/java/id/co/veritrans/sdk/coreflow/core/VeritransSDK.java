package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.events.Events;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.models.BBMCallBackUrl;
import id.co.veritrans.sdk.coreflow.models.BBMMoneyRequestModel;
import id.co.veritrans.sdk.coreflow.models.BCABankTransfer;
import id.co.veritrans.sdk.coreflow.models.BCAKlikPayDescriptionModel;
import id.co.veritrans.sdk.coreflow.models.BCAKlikPayModel;
import id.co.veritrans.sdk.coreflow.models.CIMBClickPayModel;
import id.co.veritrans.sdk.coreflow.models.CardTokenRequest;
import id.co.veritrans.sdk.coreflow.models.CardTransfer;
import id.co.veritrans.sdk.coreflow.models.CstoreEntity;
import id.co.veritrans.sdk.coreflow.models.DescriptionModel;
import id.co.veritrans.sdk.coreflow.models.EpayBriTransfer;
import id.co.veritrans.sdk.coreflow.models.IndomaretRequestModel;
import id.co.veritrans.sdk.coreflow.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.coreflow.models.KlikBCADescriptionModel;
import id.co.veritrans.sdk.coreflow.models.KlikBCAModel;
import id.co.veritrans.sdk.coreflow.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayModel;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.coreflow.models.MandiriECashModel;
import id.co.veritrans.sdk.coreflow.models.PaymentMethodsModel;
import id.co.veritrans.sdk.coreflow.models.PermataBankTransfer;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.coreflow.models.UserDetail;
import id.co.veritrans.sdk.coreflow.models.SnapTokenRequestModel;
import id.co.veritrans.sdk.coreflow.utilities.Utils;

/**
 * Created by shivam on 10/19/15.
 */
public class VeritransSDK {

    public static final String BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY = "bill info and item " +
            "details are necessary.";
    private static final String ADD_TRANSACTION_DETAILS = "Add transaction request details.";
    private static final String LOCAL_DATA_PREFERENCES = "local.data";
    private static SharedPreferences mPreferences = null;
    private static VeritransSDK veritransSDK ;
    private static boolean isLogEnabled = true;
    protected boolean isRunning = false;
    ISdkFlow uiflow;
    private MixpanelAnalyticsManager mMixpanelAnalyticsManager;
    private Context context = null;
    private int themeColor;
    private Drawable merchantLogoDrawable = null;
    private String clientKey = null;
    private String merchantServerUrl = null;
    private String defaultText = null;
    private String boldText = null;
    private String semiBoldText = null;
    private String merchantName = null;
    private IScanner externalScanner;
    private TransactionManager mTransactionManager;
    private SnapTransactionManager mSnapTransactionManager;
    private String merchantLogo = null;
    private int merchantLogoResourceId = 0;
    private TransactionRequest transactionRequest = null;
    private ArrayList<PaymentMethodsModel> selectedPaymentMethods = new ArrayList<>();
    private BBMCallBackUrl mBBMCallBackUrl = null;

    private VeritransSDK(@NonNull SdkCoreFlowBuilder sdkBuilder) {
        this.context = sdkBuilder.context;
        this.clientKey = sdkBuilder.clientKey;
        this.merchantServerUrl = sdkBuilder.merchantServerUrl;

        this.merchantName = sdkBuilder.merchantName;
        this.defaultText = sdkBuilder.defaultText;
        this.semiBoldText =sdkBuilder.semiBoldText;
        this.boldText = sdkBuilder.boldText;
        this.uiflow = sdkBuilder.sdkFlow;
        this.externalScanner = sdkBuilder.externalScanner;
        merchantLogoResourceId = sdkBuilder.merchantLogoResourceId;
        themeColor = sdkBuilder.colorThemeResourceId;
        merchantLogo = sdkBuilder.merchantLogo;

        this.mMixpanelAnalyticsManager = new MixpanelAnalyticsManager(VeritransRestAdapter.getMixpanelApi());
        this.mTransactionManager = new TransactionManager(sdkBuilder.context, VeritransRestAdapter.getVeritransApiClient(), VeritransRestAdapter.getMerchantApiClient(merchantServerUrl));
        this.mSnapTransactionManager = new SnapTransactionManager(sdkBuilder.context, VeritransRestAdapter.getSnapRestAPI(), VeritransRestAdapter.getMerchantApiClient(merchantServerUrl));
        this.mTransactionManager.setSDKLogEnabled(isLogEnabled);
        this.mTransactionManager.setAnalyticsManager(this.mMixpanelAnalyticsManager);
        this.mSnapTransactionManager.setAnalyticsManager(this.mMixpanelAnalyticsManager);

        initializeTheme();
        initializeLogo();
        initializeSharedPreferences();

    }


    protected static VeritransSDK getInstance(@NonNull SdkCoreFlowBuilder sdkBuilder) {
        if(sdkBuilder != null){
            veritransSDK = new VeritransSDK(sdkBuilder);
        }else{
            Logger.e("sdk is not initialized.");
        }
        return veritransSDK;
    }

    /**
     * Returns instance of veritrans sdk.
     *
     * @return VeritransSDK instance
     */
    public static VeritransSDK getVeritransSDK() {

        return veritransSDK;
    }

    public static SharedPreferences getmPreferences() {
        return mPreferences;
    }

    static void setmPreferences(SharedPreferences mp) {
        mPreferences = mp;
    }

    private void initializeLogo() {
        if (merchantLogoResourceId != 0) {
            merchantLogoDrawable = context.getResources().getDrawable(merchantLogoResourceId);
        } else if (merchantLogo != null) {
            int resourceImage = context.getResources().getIdentifier(getMerchantLogo(), "drawable", context.getPackageName());
            merchantLogoDrawable = context.getResources().getDrawable(resourceImage);
        }
    }

    private void initializeSharedPreferences() {
        mPreferences = context.getSharedPreferences(LOCAL_DATA_PREFERENCES, Context.MODE_PRIVATE);
    }

    private void initializeTheme() {
        themeColor = context.getResources().getColor(R.color.colorPrimary);
    }

    public   String getDefaultText() {
        return defaultText;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getMerchantLogo() {
        return merchantLogo;
    }

    void setMerchantLogo(String merchantLogo) {
        this.merchantLogo = merchantLogo;
    }

    public int getMerchantLogoResourceId() {
        return merchantLogoResourceId;
    }

    void setMerchantLogoResourceId(int merchantLogoResourceId) {
        this.merchantLogoResourceId = merchantLogoResourceId;
    }

    public Drawable getMerchantLogoDrawable() {
        return merchantLogoDrawable;
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

    /**
     * It will execute an api request to retrieve a token.
     *
     * @param cardTokenRequest token request object
     */
    public void getToken(CardTokenRequest cardTokenRequest) {

        if (cardTokenRequest != null) {
            if( Utils.isNetworkAvailable(context)){
                isRunning = true;
                mTransactionManager.getToken(cardTokenRequest);
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.TOKENIZE));
            }

        } else {
            Logger.e(context.getString(R.string.error_invalid_data_supplied));
            isRunning = false;
        }
    }

    /**
     * It will execute an transaction for permata bank .
     */
    public void paymentUsingPermataBank() {

        if (transactionRequest != null) {
            if(Utils.isNetworkAvailable(context)){
                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER;

                PermataBankTransfer permataBankTransfer = SdkUtil.getPermataBankModel
                        (transactionRequest);

                isRunning = true;
                mTransactionManager.paymentUsingPermataBank(permataBankTransfer, veritransSDK.readAuthenticationToken());
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
            }
        } else {
            isRunning = false;
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will execute an transaction for bca bank .
     */
    public void paymentUsingBcaBankTransfer() {

        if (transactionRequest != null) {
            if(Utils.isNetworkAvailable(context)){
                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER;

                BCABankTransfer bcaBankTransfer = SdkUtil.getBcaBankTransferRequest(transactionRequest);

                isRunning = true;
                mTransactionManager.paymentUsingBCATransfer(bcaBankTransfer, readAuthenticationToken());
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
            }
        } else {
            isRunning = false;
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will execute an transaction using credit card .
     *
     * @param cardTransfer Card transfer details
     */
    public void paymentUsingCard(CardTransfer cardTransfer) {
        if (transactionRequest != null
                && cardTransfer != null) {
            if(Utils.isNetworkAvailable(context)){
                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT;
                isRunning = true;
                mTransactionManager.paymentUsingCard(cardTransfer, readAuthenticationToken());
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
            }
        } else {
            isRunning = false;
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will execute an transaction for mandiri click pay.
     *
     * @param mandiriClickPayModel information about mandiri clickpay
     */
    public void paymentUsingMandiriClickPay(MandiriClickPayModel mandiriClickPayModel) {

        if (transactionRequest != null
                && mandiriClickPayModel != null) {
            if(Utils.isNetworkAvailable(context)){
                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY;

                MandiriClickPayRequestModel mandiriClickPayRequestModel =
                        SdkUtil.getMandiriClickPayRequestModel(transactionRequest,
                                mandiriClickPayModel);
                isRunning = true;
                mTransactionManager.paymentUsingMandiriClickPay(mandiriClickPayRequestModel, veritransSDK.readAuthenticationToken());
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
            }
        } else {
            isRunning = false;
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will execute an transaction for mandiri click pay.
     *
     * @param descriptionModel information about BCA Klikpay
     */
    public void paymentUsingBCAKlikPay(BCAKlikPayDescriptionModel descriptionModel) {
        if (transactionRequest != null && descriptionModel != null) {
            if(Utils.isNetworkAvailable(context)){
                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY;
                BCAKlikPayModel bcaKlikPayModel = SdkUtil.getBCAKlikPayModel(transactionRequest, descriptionModel);
                isRunning = true;
                mTransactionManager.paymentUsingBCAKlikPay(bcaKlikPayModel, veritransSDK.readAuthenticationToken());
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
            }
        } else {
            isRunning = false;
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will execute an transaction for mandiri klik BCA.
     *
     * @param descriptionModel       information about Klik BCA
     */
    public void paymentUsingKlikBCA(KlikBCADescriptionModel descriptionModel) {
        if (transactionRequest != null && descriptionModel != null) {
            if(Utils.isNetworkAvailable(context)){
                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY;
                KlikBCAModel klikBCAModel = SdkUtil.getKlikBCAModel(transactionRequest, descriptionModel);
                isRunning = true;
                mTransactionManager.paymentUsingKlikBCA(klikBCAModel);
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
            }
        } else {
            isRunning = false;
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will execute an transaction for mandiri bill pay.
     */
    public void paymentUsingMandiriBillPay() {
        if (transactionRequest != null) {
            if (transactionRequest.getBillInfoModel() != null
                    && transactionRequest.getItemDetails() != null) {
                if(Utils.isNetworkAvailable(context)){
                    transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT;

                    MandiriBillPayTransferModel mandiriBillPayTransferModel =
                            SdkUtil.getMandiriBillPayModel(transactionRequest);

                    isRunning = true;
                    mTransactionManager.paymentUsingMandiriBillPay(mandiriBillPayTransferModel, veritransSDK.readAuthenticationToken());
                }else{
                    isRunning = false;
                    Logger.e(context.getString(R.string.error_unable_to_connect));
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
                }
            } else {
                isRunning = false;
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY,  Events.PAYMENT));
                Logger.e("Error: " + BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY);
            }
        } else {
            isRunning = false;
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
        }
    }

    /** testing
     * It will execute an transaction for CIMB click pay.
     *
     * @param descriptionModel contains description about the cimb payment
     */

    public void paymentUsingCIMBClickPay(DescriptionModel descriptionModel) {

        if (transactionRequest != null
                && descriptionModel != null) {
            if(Utils.isNetworkAvailable(context)){
                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_CIMB_CLICKS;

                CIMBClickPayModel cimbClickPayModel = SdkUtil.getCIMBClickPayModel
                        (transactionRequest, descriptionModel);
                isRunning = true;
                mTransactionManager.paymentUsingCIMBPay(cimbClickPayModel, veritransSDK.readAuthenticationToken());
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
            }

        } else {
            isRunning = false;
            if (descriptionModel == null) {
                Logger.e(context.getString(R.string.error_description_required));
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
            }
        }
    }

    /**
     * It will execute an transaction for Mandiri E Cash.
     *
     * @param descriptionModel Description about Mandiri E cash payment.
     */

    public void paymentUsingMandiriECash(DescriptionModel descriptionModel) {
        if (transactionRequest != null && descriptionModel != null) {
            if(Utils.isNetworkAvailable(context)){
                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_MANDIRI_ECASH;

                MandiriECashModel mandiriECashModel = SdkUtil.getMandiriECashModel
                        (transactionRequest, descriptionModel);
                isRunning = true;
                mTransactionManager.paymentUsingMandiriECash(mandiriECashModel, veritransSDK.readAuthenticationToken());
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
            }
        } else {
            isRunning = false;
            if (descriptionModel == null) {
                Logger.e(context.getString(R.string.error_description_required));
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
            }
        }
    }

    /**
     * It will execute an transaction for epay bri .
     *
     */
    public void paymentUsingEpayBri() {
        if (transactionRequest != null) {
            if(Utils.isNetworkAvailable(context)){
                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_EPAY_BRI;

            /*PermataBankTransfer permataBankTransfer = SdkUtil.getPermataBankModel
                    (transactionRequest);*/
                EpayBriTransfer epayBriTransfer = SdkUtil.getEpayBriBankModel(transactionRequest);

                isRunning = true;
                mTransactionManager.paymentUsingEpayBri(epayBriTransfer, veritransSDK.readAuthenticationToken());
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
            }

        } else {
            isRunning = false;
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will execute an transaction for permata bank .
     *
     * @param msisdn                registered mobile number of user.
     */
    public void paymentUsingIndosatDompetku( String msisdn) {
        if (transactionRequest != null && msisdn != null) {
            if(Utils.isNetworkAvailable(context)){
                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_INDOSAT_DOMPETKU;

                IndosatDompetkuRequest indosatDompetkuRequest =
                        SdkUtil.getIndosatDompetkuRequestModel(transactionRequest, msisdn);

                isRunning = true;
                mTransactionManager.paymentUsingIndosatDompetku(indosatDompetkuRequest, veritransSDK.readAuthenticationToken());
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
            }

        } else {
            isRunning = false;
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    /**
     * Set transaction information that you want to execute.
     *
     * @param transactionRequest request token object
     */
    public void setTransactionRequest(TransactionRequest transactionRequest) {

        if (!isRunning) {

            if (transactionRequest != null) {
                this.transactionRequest = transactionRequest;
            } else {
                Logger.e(ADD_TRANSACTION_DETAILS);
            }

        } else {
            Logger.e(context.getString(R.string.error_already_running));
        }

    }

    /**
     * This will start actual execution of save card UI flow.
     *
     * @param context current activity.
     */
    public void startRegisterCardUIFlow(@NonNull  Context context) {
        if (uiflow != null) {
            uiflow.runRegisterCard(context);
        }
    }

    /**
     * This will start actual execution of transaction. if you have enabled an ui then it will
     * start activity according to it.
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
                Logger.e(ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(context.getString(R.string.error_already_running));
            }
        }
    }

    /**
     * It will execute an transaction for Indomaret .
     *
     * @param cstoreEntity transaction details
     */
    public void paymentUsingIndomaret(CstoreEntity cstoreEntity) {
        if (transactionRequest != null
                && cstoreEntity != null) {
            if(Utils.isNetworkAvailable(context)){
                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_INDOMARET;

                IndomaretRequestModel indomaretRequestModel =
                        SdkUtil.getIndomaretRequestModel(transactionRequest, cstoreEntity);

                isRunning = true;
                mTransactionManager.paymentUsingIndomaret(indomaretRequestModel, veritransSDK.readAuthenticationToken());
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
            }

        } else {
            isRunning = false;
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /**
     * It will fetch saved cards from merchant server.
     *!!!
     */
    public void getSavedCard() {
        if(Utils.isNetworkAvailable(context)){
            mTransactionManager.getCards(readAuthenticationToken());
        }else{
            isRunning = false;
            Logger.e(context.getString(R.string.error_unable_to_connect));
        }
    }

    /**
     * It will  save cards to merchant server.
     *!!!
     * @param cardTokenRequest card details
     */
    //testing
    public void saveCards(SaveCardRequest cardTokenRequest) {
        if (cardTokenRequest != null) {
            if(Utils.isNetworkAvailable(context)){
                mTransactionManager.saveCards(cardTokenRequest, veritransSDK.readAuthenticationToken());
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.REGISTER_CARD));
            }
        }else{
            isRunning = false;
        }
    }

    /**
     * It will execute an transaction for BBMMoney.
     */
    public void paymentUsingBBMMoney() {
        if (transactionRequest != null) {
            if(Utils.isNetworkAvailable(context)){
                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_BBM_MONEY;

                BBMMoneyRequestModel bbmMoneyRequestModel =
                        SdkUtil.getBBMMoneyRequestModel(transactionRequest);

                isRunning = true;
                mTransactionManager.paymentUsingBBMMoney(bbmMoneyRequestModel, readAuthenticationToken());
            }else{
                isRunning = false;
                Logger.e(context.getString(R.string.error_unable_to_connect));
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.PAYMENT));
            }
        } else {
            isRunning = false;
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied)));
        }
    }

    /*
     * delete card from server
     * !!!
     */
    public void deleteCard(SaveCardRequest creditCard) {
        if(Utils.isNetworkAvailable(context)){
            if (creditCard != null) {
                isRunning = true;
                mTransactionManager.deleteCard(creditCard, veritransSDK.readAuthenticationToken());

            }else{
                isRunning = false;
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_card_unavailable)));
            }
        }else{
            isRunning = false;
            Logger.e(context.getString(R.string.error_unable_to_connect));
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.DELETE_CARD));
        }

    }

    public void cardRegistration(@NonNull String cardNumber,
                                 @NonNull String cardCvv, @NonNull String cardExpMonth,
                                 @NonNull String cardExpYear) {
        if(Utils.isNetworkAvailable(context)){
            mTransactionManager.cardRegistration(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientKey);
            isRunning = true;
        }else{
            isRunning = false;
            Logger.e(context.getString(R.string.error_unable_to_connect));
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_unable_to_connect), Events.CARD_REGISTRATION));
        }
    }

    public BBMCallBackUrl getBBMCallBackUrl() {
        return mBBMCallBackUrl;
    }

    //unused
    public void setBBMCallBackUrl(BBMCallBackUrl BBMCallBackUrl) {
        mBBMCallBackUrl = BBMCallBackUrl;
    }

    /**
     * It will fetch the Offers from merchant server.
     */
    public void getOffersList() {
        if(isNetworkAvailable()){
            isRunning = true;
            mTransactionManager.getOffers(readAuthenticationToken());
        }else{
            isRunning = false;
            Logger.e(context.getString(R.string.error_unable_to_connect));
        }
    }

    /**
     * It will run background task to get authentication token.
     * !!!
     */
    public void getAuthenticationToken() {
        if(Utils.isNetworkAvailable(context)){
            isRunning = true;
            mTransactionManager.getAuthenticationToken();
        }else{
            isRunning = false;
        }

    }

    /**
     * It will run background task to get snap transaction details.
     *
     * @param snapToken Snap authentication token
     */
    public void getSnapTransaction(String snapToken) {
        if (Utils.isNetworkAvailable(context)) {
            isRunning = true;
            mSnapTransactionManager.getSnapTransaction(snapToken);
        } else {
            isRunning = false;
        }
    }

    /**
     * It will run background task to get snap token.
     */
    public void getSnapToken() {
        if (Utils.isNetworkAvailable(context)) {
            isRunning = true;
            SnapTokenRequestModel model = SdkUtil.getSnapTokenRequestModel(transactionRequest);
            mSnapTransactionManager.getSnapToken(model);
        } else {
            isRunning = false;
        }
    }

    public IScanner getExternalScanner() {
        return externalScanner;
    }

    public  TransactionManager getTransactionManager() {
        return mTransactionManager;
    }

    void setTransactionManager(TransactionManager transactionManager) {
        mTransactionManager = transactionManager;
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


}