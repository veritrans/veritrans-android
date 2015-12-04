package id.co.veritrans.sdk.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import id.co.veritrans.sdk.activities.UserDetailsActivity;
import id.co.veritrans.sdk.callbacks.DeleteCardCallback;
import id.co.veritrans.sdk.callbacks.PaymentStatusCallback;
import id.co.veritrans.sdk.callbacks.SavedCardCallback;
import id.co.veritrans.sdk.callbacks.TokenCallBack;
import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.models.CIMBClickPayModel;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.models.CardTransfer;
import id.co.veritrans.sdk.models.EpayBriTransfer;
import id.co.veritrans.sdk.models.IndomaretRequestModel;
import id.co.veritrans.sdk.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.models.MandiriClickPayModel;
import id.co.veritrans.sdk.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.models.MandiriECashModel;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.models.PermataBankTransfer;
import id.co.veritrans.sdk.models.UserDetail;

/**
 * Created by shivam on 10/19/15.
 */
public class VeritransSDK {

    public static final String BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY = "bill info and item " +
            "details are necessary.";
    private static final String FONTS_OPEN_SANS_BOLD_TTF = "fonts/open_sans_bold.ttf";
    private static final String FONTS_OPEN_SANS_REGULAR_TTF = "fonts/open_sans_regular.ttf";
    private static final String FONTS_OPEN_SANS_SEMI_BOLD_TTF = "fonts/open_sans_semibold.ttf";
    private static final String ADD_TRANSACTION_DETAILS = "Add transaction request details.";

    private static Context context = null;

    private static Typeface typefaceOpenSansRegular = null;
    private static Typeface typefaceOpenSansSemiBold = null;
    private static Typeface typefaceOpenSansBold = null;

    private static VeritransSDK veritransSDK = new VeritransSDK();
    private static boolean isLogEnabled = true;
    /*private static String serverKey = null;*/
    private static String clientKey = null;
    protected boolean isRunning = false;

    private TransactionRequest transactionRequest = null;
    private ArrayList<PaymentMethodsModel> selectedPaymentMethods = new ArrayList<>();
    private String TRANSACTION_RESPONSE_NOT_AVAILABLE = "Transaction response not available.";

    private VeritransSDK() {
    }

    protected static VeritransSDK getInstance(VeritransBuilder veritransBuilder) {

        if (veritransBuilder != null) {
            context = veritransBuilder.context;
            isLogEnabled = veritransBuilder.enableLog;
            /*serverKey = veritransBuilder.serverKey;*/
            clientKey = veritransBuilder.clientKey;

            initializeFonts();
            return veritransSDK;
        } else {
            return null;
        }
    }

    /***
     * It will initialize all fonts that are available in sdk.
     */
    private static void initializeFonts() {
        AssetManager assets = context.getAssets();
        typefaceOpenSansBold = Typeface.createFromAsset(assets, FONTS_OPEN_SANS_BOLD_TTF);
        typefaceOpenSansRegular = Typeface.createFromAsset(assets, FONTS_OPEN_SANS_REGULAR_TTF);
        typefaceOpenSansSemiBold = Typeface.createFromAsset(assets,
                FONTS_OPEN_SANS_SEMI_BOLD_TTF);
    }

    /**
     * Returns instance of veritrans sdk.
     *
     * @return
     */
    public static VeritransSDK getVeritransSDK() {

        /*if (serverKey != null && context != null && clientKey != null) {
            // created to get access of already created instance of sdk.
            // This instance contains information about transaction.
            return veritransSDK;
        }*/
        if (context != null) {
            return veritransSDK;
        }

        Log.e(Constants.TAG, Constants.ERROR_SDK_IS_NOT_INITIALIZED);
        return null;
    }

    public Typeface getTypefaceOpenSansRegular() {
        return typefaceOpenSansRegular;
    }

    public Typeface getTypefaceOpenSansSemiBold() {
        return typefaceOpenSansSemiBold;
    }

    public Typeface getTypefaceOpenSansBold() {
        return typefaceOpenSansBold;
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

    public String getMerchantToken(Context context) {
        StorageDataHandler storageDataHandler = new StorageDataHandler();
        UserDetail userDetail = null;
        try {
            userDetail = (UserDetail) storageDataHandler.readObject(context, Constants
                    .USER_DETAILS);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        String merchantToken = userDetail.getMerchantToken();
        Logger.i("merchantToken:" + merchantToken);
        return merchantToken;
        //return serverKey;
    }

    public String getClientKey() {
        return clientKey;
    }

    public ArrayList<PaymentMethodsModel> getSelectedPaymentMethods() {
        return selectedPaymentMethods;
    }

    public void setSelectedPaymentMethods(ArrayList<PaymentMethodsModel> selectedPaymentMethods) {
        this.selectedPaymentMethods = selectedPaymentMethods;
    }

    protected Activity getActivity() {
        if (transactionRequest != null) {
            return transactionRequest.getActivity();
        }

        return null;
    }

    /**
     * It will execute an api request to retrieve a token.
     *
     * @param activity         instance of an activity.
     * @param cardTokenRequest
     */
    public void getToken(Activity activity, CardTokenRequest cardTokenRequest, TokenCallBack
            tokenCallBack) {

        if (activity != null && cardTokenRequest != null && tokenCallBack != null) {

            isRunning = true;
            TransactionManager.getToken(activity, cardTokenRequest, tokenCallBack);

        } else {
            if (tokenCallBack != null) {
                tokenCallBack.onFailure(Constants.ERROR_SDK_IS_NOT_INITIALIZED, null);
            }
            Logger.e(Constants.ERROR_INVALID_DATA_SUPPLIED);
            isRunning = false;
        }
    }

    /**
     * It will execute an transaction for permata bank .
     *
     * @param activity                  instance of an activity.
     * @param permataBankTransferStatus instance of TransactionCallback.
     */
    public void paymentUsingPermataBank(Activity activity,
                                        TransactionCallback permataBankTransferStatus) {

        if (transactionRequest != null && activity != null
                && permataBankTransferStatus != null) {

            transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER;
            transactionRequest.activity = activity;

            PermataBankTransfer permataBankTransfer = SdkUtil.getPermataBankModel(transactionRequest);

            isRunning = true;
            TransactionManager.paymentUsingPermataBank(transactionRequest.getActivity(),
                    permataBankTransfer,
                    permataBankTransferStatus);
        } else {
            isRunning = false;
            showError(transactionRequest, permataBankTransferStatus);
        }
    }

    /**
     * It will execute an transaction using credit card .
     *
     * @param activity                       instance of an activity.
     * @param cardPaymentTransactionCallback instance of TransactionCallback.
     */
    public void paymentUsingCard(Activity activity, CardTransfer cardTransfer,
                                 TransactionCallback cardPaymentTransactionCallback
    ) {

        if (transactionRequest != null && activity != null
                && cardTransfer != null && cardPaymentTransactionCallback != null) {

            transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT;
            transactionRequest.activity = activity;

            isRunning = true;
            TransactionManager.paymentUsingCard(transactionRequest.getActivity(), cardTransfer,
                    cardPaymentTransactionCallback);
        } else {
            isRunning = false;
            showError(transactionRequest, cardPaymentTransactionCallback);
        }
    }

    /**
     * It will execute an transaction for mandiri click pay.
     *
     * @param activity
     * @param mandiriClickPayModel       information about mandiri clickpay
     * @param paymentTransactionCallback TransactionCallback instance
     */
    public void paymentUsingMandiriClickPay(Activity activity, MandiriClickPayModel
            mandiriClickPayModel, TransactionCallback paymentTransactionCallback) {

        if (transactionRequest != null && activity != null
                && mandiriClickPayModel != null && paymentTransactionCallback != null) {

            transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY;
            transactionRequest.activity = activity;

            MandiriClickPayRequestModel mandiriClickPayRequestModel =
                    SdkUtil.getMandiriClickPayRequestModel(transactionRequest,
                            mandiriClickPayModel);

            isRunning = true;

            TransactionManager.paymentUsingMandiriClickPay(transactionRequest.getActivity(),
                    mandiriClickPayRequestModel, paymentTransactionCallback);
        } else {

            isRunning = false;
            showError(transactionRequest, paymentTransactionCallback);
        }
    }

    /**
     * It will execute an transaction for mandiri bill pay.
     *
     * @param activity
     * @param mandiriBillPayTransferStatus TransactionCallback instance
     */
    public void paymentUsingMandiriBillPay(Activity activity,
                                           TransactionCallback mandiriBillPayTransferStatus) {

        if (transactionRequest != null && activity != null
                && mandiriBillPayTransferStatus != null) {

            if (transactionRequest.getBillInfoModel() != null
                    && transactionRequest.getItemDetails() != null) {

                transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT;
                transactionRequest.activity = activity;

                MandiriBillPayTransferModel mandiriBillPayTransferModel =
                        SdkUtil.getMandiriBillPayModel(transactionRequest);

                isRunning = true;
                TransactionManager.paymentUsingMandiriBillPay(transactionRequest.getActivity(),
                        mandiriBillPayTransferModel, mandiriBillPayTransferStatus);

            } else {

                isRunning = false;
                mandiriBillPayTransferStatus.onFailure(BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY,
                        null);
                Logger.e("Error: " + BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY);
            }
        } else {
            isRunning = false;
            showError(transactionRequest, mandiriBillPayTransferStatus);

        }
    }

    /**
     * It will execute an transaction for CIMB click pay.
     *
     * @param activity
     * @param paymentTransactionCallback TransactionCallback instance
     */

    public void paymentUsingCIMBClickPay(Activity activity, TransactionCallback
            paymentTransactionCallback) {

        if (transactionRequest != null && activity != null
                && paymentTransactionCallback != null) {

            transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_CIMB_CLICKS;
            transactionRequest.activity = activity;

            CIMBClickPayModel cimbClickPayModel = SdkUtil.getCIMBClickPayModel(transactionRequest);

            isRunning = true;

            TransactionManager.paymentUsingCIMBPay(transactionRequest.getActivity(),
                    cimbClickPayModel,
                    paymentTransactionCallback);
        } else {
            isRunning = false;
            showError(transactionRequest, paymentTransactionCallback);
        }
    }

    /**
     * It will execute an transaction for Mandiri E Cash.
     *
     * @param activity
     * @param paymentTransactionCallback TransactionCallback instance
     */

    public void paymentUsingMandiriECash(Activity activity, TransactionCallback
            paymentTransactionCallback) {
        if (transactionRequest != null && activity != null
                && paymentTransactionCallback != null) {

            transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_MANDIRI_ECASH;
            transactionRequest.activity = activity;

            MandiriECashModel mandiriECashModel = SdkUtil.getMandiriECashModel(transactionRequest);

            isRunning = true;

            TransactionManager.paymentUsingMandiriECash(transactionRequest.getActivity(),
                    mandiriECashModel,
                    paymentTransactionCallback);
        } else {
            isRunning = false;
            showError(transactionRequest, paymentTransactionCallback);
        }
    }

    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    /**
     * Set transaction information that you want to execute.
     *
     * @param transactionRequest
     */
    public void setTransactionRequest(TransactionRequest transactionRequest) {

        if (!isRunning) {

            if (transactionRequest != null && transactionRequest.getActivity() != null) {
                this.transactionRequest = transactionRequest;
            } else {
                Logger.e(ADD_TRANSACTION_DETAILS);
            }

        } else {
            Logger.e(Constants.ERROR_ALREADY_RUNNING);
        }

    }

    private void showError(TransactionRequest transactionRequest,
                           TransactionCallback permataBankTransferStatus) {

        if (permataBankTransferStatus != null) {
            permataBankTransferStatus.onFailure(Constants.ERROR_SDK_IS_NOT_INITIALIZED, null);
        }

        if (transactionRequest == null) {
            Logger.e(ADD_TRANSACTION_DETAILS);
        }

        Logger.e(Constants.ERROR_INVALID_DATA_SUPPLIED);
    }

    /**
     * This will start actual execution of transaction. if you have enabled an ui then it will start activity according to it.
     */
    public void startPaymentUiFlow() {

        if (transactionRequest != null && !isRunning) {

            if (transactionRequest.getPaymentMethod() == Constants
                    .PAYMENT_METHOD_NOT_SELECTED) {

                transactionRequest.enableUi(true);

                Intent userDetailsIntent = new Intent(transactionRequest.getActivity(),
                        UserDetailsActivity.class);
                transactionRequest.getActivity().startActivity(userDetailsIntent);

            } else {
                // start specific activity depending  on payment type.
            }

        } else {

            if (transactionRequest == null) {
                Logger.e(ADD_TRANSACTION_DETAILS);
            } else {
                Logger.e(Constants.ERROR_ALREADY_RUNNING);
            }
        }
    }

    /**
     * It will execute an transaction for epay bri .
     *
     * @param activity              instance of an activity.
     * @param eapyBriTransferStatus instance of TransactionCallback.
     */
    public void paymentUsingEpayBri(Activity activity,
                                    TransactionCallback eapyBriTransferStatus) {

        if (transactionRequest != null && activity != null
                && eapyBriTransferStatus != null) {

            transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_EPAY_BRI;
            transactionRequest.activity = activity;

            /*PermataBankTransfer permataBankTransfer = SdkUtil.getPermataBankModel
                    (transactionRequest);*/
            EpayBriTransfer epayBriTransfer = SdkUtil.getEpayBriBankModel(transactionRequest);

            isRunning = true;
            TransactionManager.paymentUsingEpayBri(transactionRequest.getActivity(),
                    epayBriTransfer,
                    eapyBriTransferStatus);
        } else {
            isRunning = false;
            showError(transactionRequest, eapyBriTransferStatus);
        }
    }

    /**
     * It will execute an transaction for permata bank .
     *
     * @param activity              instance of an activity.
     * @param indosatTransferStatus instance of TransactionCallback.
     */
    public void paymentUsingIndosatDompetku(Activity activity,
                                            TransactionCallback indosatTransferStatus, String msisdn) {

        if (transactionRequest != null && activity != null
                && indosatTransferStatus != null) {

            transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_INDOSAT_DOMPETKU;
            transactionRequest.activity = activity;

            IndosatDompetkuRequest indosatDompetkuRequest =
                    SdkUtil.getIndosatDompetkuRequestModel(transactionRequest, msisdn);

            isRunning = true;
            TransactionManager.paymentUsingIndosatDompetku(transactionRequest.getActivity(),
                    indosatDompetkuRequest,
                    indosatTransferStatus);
        } else {
            isRunning = false;
            showError(transactionRequest, indosatTransferStatus);
        }
    }

    public void getPaymentStatus(Activity activity, String transactionId, PaymentStatusCallback
            paymentStatusCallback) {
        if (TextUtils.isEmpty(transactionId)) {
            TransactionManager.getPaymentStatus(activity, transactionId, paymentStatusCallback);
        }
    }

    /**
     * It will execute an transaction for Indomaret .
     *
     * @param activity          instance of an activity.
     * @param indomaretCallback instance of TransactionCallback.
     */
    public void paymentUsingIndomaret(Activity activity,
                                      TransactionCallback indomaretCallback, IndomaretRequestModel.CstoreEntity cstoreEntity) {

        if (transactionRequest != null && activity != null
                && indomaretCallback != null && cstoreEntity != null) {

            transactionRequest.paymentMethod = Constants.PAYMENT_METHOD_INDOSAT_DOMPETKU;
            transactionRequest.activity = activity;

            IndomaretRequestModel indomaretRequestModel =
                    SdkUtil.getIndomaretRequestModel(transactionRequest, cstoreEntity);

            isRunning = true;
            TransactionManager.paymentUsingIndomaret(transactionRequest.getActivity(),
                    indomaretRequestModel,
                    indomaretCallback);
        } else {
            isRunning = false;
            showError(transactionRequest, indomaretCallback);
        }
    }

    /**
     * It will fetch saved cards from merchant server.
     *
     * @param activity instance of an activity.
     * @param callback
     */
    public void getSavedCard(Activity activity,
                             SavedCardCallback callback) {
        if (activity != null) {
            TransactionManager.getCards(activity, callback);
        }
    }

    /**
     * It will  save cards to merchant server.
     *
     * @param activity         instance of an activity.
     * @param cardTokenRequest card details
     * @param cardCallback
     */
    public void saveCards(Activity activity, CardTokenRequest cardTokenRequest,
                          SavedCardCallback cardCallback) {
        if (activity != null && cardTokenRequest != null && cardCallback != null) {
            TransactionManager.saveCards(activity, cardTokenRequest, cardCallback);
        }
    }

    public void deleteCard(Activity activity, CardTokenRequest creditCard,DeleteCardCallback deleteCardCallback) {
        if(activity!=null && creditCard!=null){
            TransactionManager.deleteCard(activity,creditCard,deleteCardCallback);
        }
    }
}