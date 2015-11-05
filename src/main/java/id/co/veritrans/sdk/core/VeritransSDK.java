package id.co.veritrans.sdk.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;

import id.co.veritrans.sdk.activities.PaymentMethodsActivity;
import id.co.veritrans.sdk.callbacks.TokenCallBack;
import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.models.CardTransfer;
import id.co.veritrans.sdk.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.models.PermataBankTransfer;

/**
 * Created by shivam on 10/19/15.
 */
public class VeritransSDK {

    private static final String FONTS_OPEN_SANS_BOLD_TTF = "fonts/open_sans_bold.ttf";
    private static final String FONTS_OPEN_SANS_REGULAR_TTF = "fonts/open_sans_regular.ttf";
    private static final String FONTS_OPEN_SANS_SEMI_BOLD_TTF = "fonts/open_sans_semibold.ttf";
    public static final String BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY = "bill info and item " +
            "details are necessary.";

    private static final String ADD_TRANSACTION_DETAILS = "Add transaction request details.";

    private static Context sContext = null;

    private static Typeface typefaceOpenSansRegular = null;
    private static Typeface typefaceOpenSansSemiBold = null;
    private static Typeface typefaceOpenSansBold = null;

    private static VeritransSDK sVeritransSDK = new VeritransSDK();
    private static boolean sIsLogEnabled = true;

    private boolean isRunning = false;
    private static String sServerKey = null;
    private static String sClientKey = null;

    private TransactionRequest mTransactionRequest = null;


    private VeritransSDK() {
    }

    protected static VeritransSDK getInstance(VeritransBuilder veritransBuilder) {

        if (veritransBuilder != null) {
            sContext = veritransBuilder.context;
            sIsLogEnabled = veritransBuilder.enableLog;
            sServerKey = veritransBuilder.serverKey;
            sClientKey = veritransBuilder.clientKey;

            initializeFonts();
            return sVeritransSDK;
        } else {
            return null;
        }
    }

    private static void initializeFonts() {
        AssetManager assets = sContext.getAssets();
        typefaceOpenSansBold = Typeface.createFromAsset(assets, FONTS_OPEN_SANS_BOLD_TTF);
        typefaceOpenSansRegular = Typeface.createFromAsset(assets, FONTS_OPEN_SANS_REGULAR_TTF);
        typefaceOpenSansSemiBold = Typeface.createFromAsset(assets,
                FONTS_OPEN_SANS_SEMI_BOLD_TTF);
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

    /**
     * Returns instance of veritrans sdk.
     *
     * @return
     */
    public static VeritransSDK getVeritransSDK() {

        if (sServerKey != null && sContext != null && sClientKey != null) {
            // created to get access of already created instance of sdk.
            // This instance contains information about transaction.
            return sVeritransSDK;
        }

        Log.e(Constants.TAG, Constants.ERROR_SDK_IS_NOT_INITIALIZED);
        return null;
    }

    public boolean isRunning() {
        return isRunning;
    }


    public boolean isLogEnabled() {
        return sIsLogEnabled;
    }


    public Context getContext() {
        return sContext;
    }

    public String getServerKey() {
        return sServerKey;
    }

    public String getClientKey() {
        return sClientKey;
    }

    protected Activity getActivity() {
        if (mTransactionRequest != null) {
            return mTransactionRequest.getActivity();
        }

        return null;
    }

    /**
     * @param activity
     * @param cardTokenRequest
     */

    public void getToken(Activity activity, CardTokenRequest cardTokenRequest, TokenCallBack
            tokenCallBack) {

        if (activity != null && cardTokenRequest != null && tokenCallBack != null) {
            TransactionManager.getToken(activity, cardTokenRequest, tokenCallBack);
        } else {
            if (tokenCallBack != null) {
                tokenCallBack.onFailure(Constants.ERROR_SDK_IS_NOT_INITIALIZED, null);
            }
            Logger.e(Constants.ERROR_INVALID_DATA_SUPPLIED);
        }
    }

    public void paymentUsingPermataBank(Activity activity, PermataBankTransfer permataBankTransfer,
                                        TransactionCallback permataBankTransferStatus) {


        if (mTransactionRequest != null && activity != null
                && permataBankTransfer != null && permataBankTransferStatus != null) {

            mTransactionRequest.paymentMethod = Constants.PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER;
            mTransactionRequest.activity = activity;

            TransactionManager.paymentUsingPermataBank(mTransactionRequest.getActivity(),
                    permataBankTransfer,
                    permataBankTransferStatus);
        } else {

            showError(mTransactionRequest, permataBankTransferStatus);
        }
    }


    public void paymentUsingCard(Activity activity, CardTransfer cardTransfer,
                                 TransactionCallback cardPaymentTransactionCallback
    ) {

        if (mTransactionRequest != null && activity != null
                && cardTransfer != null && cardPaymentTransactionCallback != null) {

            mTransactionRequest.paymentMethod = Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT;
            mTransactionRequest.activity = activity;

            TransactionManager.paymentUsingCard(mTransactionRequest.getActivity(), cardTransfer,
                    cardPaymentTransactionCallback);
        } else {

            showError(mTransactionRequest, cardPaymentTransactionCallback);
        }
    }

    public void paymentUsingMandiriClickPay(Activity activity, MandiriClickPayRequestModel
            mandiriClickPayRequestModel,
                                            TransactionCallback cardPaymentTransactionCallback) {

        if (mTransactionRequest != null && activity != null
                && mandiriClickPayRequestModel != null && cardPaymentTransactionCallback != null) {

            mTransactionRequest.paymentMethod = Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY;
            mTransactionRequest.activity = activity;

            TransactionManager.paymentUsingMandiriClickPay(mTransactionRequest.getActivity(),
                    mandiriClickPayRequestModel, cardPaymentTransactionCallback);
        } else {

            showError(mTransactionRequest, cardPaymentTransactionCallback);
        }
    }

    public void paymentUsingMandiriBillPay(Activity activity, MandiriBillPayTransferModel
            mandiriBillPayTransferModel,
                                           TransactionCallback mandiriBillPayTransferStatus) {

        if (mTransactionRequest != null && activity != null
                && mandiriBillPayTransferModel != null && mandiriBillPayTransferStatus != null) {

            if (mandiriBillPayTransferModel.getBillInfoModel() != null
                    && mandiriBillPayTransferModel.getItemDetails() != null) {

                mTransactionRequest.paymentMethod = Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT;
                mTransactionRequest.activity = activity;

                TransactionManager.paymentUsingMandiriBillPay(mTransactionRequest.getActivity(),
                        mandiriBillPayTransferModel, mandiriBillPayTransferStatus);

            } else {
                mandiriBillPayTransferStatus.onFailure(BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY,
                        null);
                Logger.e("Error: " + BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY);
            }
        } else {

            showError(mTransactionRequest, mandiriBillPayTransferStatus);

        }
    }


    public TransactionRequest getTransactionRequest() {
        return mTransactionRequest;
    }


    public void setTransactionRequest(TransactionRequest transactionRequest) {


        if (getTransactionRequest() == null && !isRunning) {

            if (transactionRequest != null && transactionRequest.getActivity() != null) {
                mTransactionRequest = transactionRequest;
                if (transactionRequest.isUiEnabled()) {

                    if (transactionRequest.getPaymentMethod() == Constants
                            .PAYMENT_METHOD_NOT_SELECTED) {

                        Intent paymentMethods = new Intent(transactionRequest.getActivity(),
                                PaymentMethodsActivity.class);
                        transactionRequest.getActivity().startActivity(paymentMethods);
                    } else {

                        // start specific activity depending  on payment type.
                    }

                } else {
                    //do nothing wait for user to choose payment method.
                }

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

}