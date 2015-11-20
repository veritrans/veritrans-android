package id.co.veritrans.sdk.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;

import java.util.ArrayList;

import id.co.veritrans.sdk.activities.UserDetailsActivity;
import id.co.veritrans.sdk.callbacks.TokenCallBack;
import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.callbacks.UpdateTransactionCallBack;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.models.CardTransfer;
import id.co.veritrans.sdk.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.models.MandiriClickPayModel;
import id.co.veritrans.sdk.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.models.PaymentMethodsModel;
import id.co.veritrans.sdk.models.PermataBankTransfer;
import id.co.veritrans.sdk.models.TransactionMerchant;

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

    private static Context sContext = null;

    private static Typeface typefaceOpenSansRegular = null;
    private static Typeface typefaceOpenSansSemiBold = null;
    private static Typeface typefaceOpenSansBold = null;

    private static VeritransSDK sVeritransSDK = new VeritransSDK();
    private static boolean sIsLogEnabled = true;
    private static String sServerKey = null;
    private static String sClientKey = null;
    protected boolean isRunning = false;
    private TransactionRequest mTransactionRequest = null;
    private ArrayList<PaymentMethodsModel> selectedPaymentMethods = new ArrayList<>();
    private String TRANSACTION_RESPONSE_NOT_AVAILABLE = "Transaction response not available.";

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

    public ArrayList<PaymentMethodsModel> getSelectedPaymentMethods() {
        return selectedPaymentMethods;
    }

    public void setSelectedPaymentMethods(ArrayList<PaymentMethodsModel> selectedPaymentMethods) {
        this.selectedPaymentMethods = selectedPaymentMethods;
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

        isRunning = true;
        if (activity != null && cardTokenRequest != null && tokenCallBack != null) {
            TransactionManager.getToken(activity, cardTokenRequest, tokenCallBack);

        } else {
            if (tokenCallBack != null) {
                tokenCallBack.onFailure(Constants.ERROR_SDK_IS_NOT_INITIALIZED, null);
            }
            Logger.e(Constants.ERROR_INVALID_DATA_SUPPLIED);
            isRunning = false;
        }
    }

    public void paymentUsingPermataBank(Activity activity,
                                        TransactionCallback permataBankTransferStatus) {

        isRunning = true;

        if (mTransactionRequest != null && activity != null
                && permataBankTransferStatus != null) {

            mTransactionRequest.paymentMethod = Constants.PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER;
            mTransactionRequest.activity = activity;

            PermataBankTransfer permataBankTransfer = SdkUtil.getPermataBankModel
                    (mTransactionRequest);
            TransactionManager.paymentUsingPermataBank(mTransactionRequest.getActivity(),
                    permataBankTransfer,
                    permataBankTransferStatus);
        } else {
            isRunning = false;
            showError(mTransactionRequest, permataBankTransferStatus);
        }
    }


    public void paymentUsingCard(Activity activity, CardTransfer cardTransfer,
                                 TransactionCallback cardPaymentTransactionCallback
    ) {

        isRunning = true;

        if (mTransactionRequest != null && activity != null
                && cardTransfer != null && cardPaymentTransactionCallback != null) {

            mTransactionRequest.paymentMethod = Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT;
            mTransactionRequest.activity = activity;

            TransactionManager.paymentUsingCard(mTransactionRequest.getActivity(), cardTransfer,
                    cardPaymentTransactionCallback);
        } else {
            isRunning = false;
            showError(mTransactionRequest, cardPaymentTransactionCallback);
        }
    }

    public void paymentUsingMandiriClickPay(Activity activity, MandiriClickPayModel
            mandiriClickPayModel, TransactionCallback cardPaymentTransactionCallback) {

        isRunning = true;

        if (mTransactionRequest != null && activity != null
                && mandiriClickPayModel != null && cardPaymentTransactionCallback != null) {

            mTransactionRequest.paymentMethod = Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY;
            mTransactionRequest.activity = activity;


            MandiriClickPayRequestModel mandiriClickPayRequestModel =
                    SdkUtil.getMandiriClickPayRequestModel(mTransactionRequest,
                            mandiriClickPayModel);


            TransactionManager.paymentUsingMandiriClickPay(mTransactionRequest.getActivity(),
                    mandiriClickPayRequestModel, cardPaymentTransactionCallback);
        } else {

            isRunning = false;
            showError(mTransactionRequest, cardPaymentTransactionCallback);
        }
    }

    public void paymentUsingMandiriBillPay(Activity activity,
                                           TransactionCallback mandiriBillPayTransferStatus) {

        isRunning = true;

        if (mTransactionRequest != null && activity != null
                && mandiriBillPayTransferStatus != null) {

            if (mTransactionRequest.getBillInfoModel() != null
                    && mTransactionRequest.getItemDetails() != null) {

                mTransactionRequest.paymentMethod = Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT;
                mTransactionRequest.activity = activity;

                MandiriBillPayTransferModel mandiriBillPayTransferModel =
                        SdkUtil.getMandiriBillPayModel(mTransactionRequest);

                TransactionManager.paymentUsingMandiriBillPay(mTransactionRequest.getActivity(),
                        mandiriBillPayTransferModel, mandiriBillPayTransferStatus);

            } else {

                isRunning = false;
                mandiriBillPayTransferStatus.onFailure(BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY,
                        null);
                Logger.e("Error: " + BILL_INFO_AND_ITEM_DETAILS_ARE_NECESSARY);
            }
        } else {
            isRunning = false;
            showError(mTransactionRequest, mandiriBillPayTransferStatus);

        }
    }

    public void updateTransactionStatusMerchant(Context activity,
                                           TransactionMerchant transactionMerchant,
                                                UpdateTransactionCallBack callBack) {

        isRunning = true;

        if ( activity != null && callBack != null) {

            if (transactionMerchant != null
                    ) {
                TransactionManager.transactionUpdateMerchant(activity,transactionMerchant,callBack);

            } else {
                isRunning = false;
                callBack.onFailure(TRANSACTION_RESPONSE_NOT_AVAILABLE,null);
                Logger.e("Error: " + TRANSACTION_RESPONSE_NOT_AVAILABLE);
            }
        } else {
            isRunning = false;
        }
    }

    public TransactionRequest getTransactionRequest() {
        return mTransactionRequest;
    }


    public void setTransactionRequest(TransactionRequest transactionRequest) {

        if (!isRunning) {

            if (transactionRequest != null && transactionRequest.getActivity() != null) {
                mTransactionRequest = transactionRequest;
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


    public void startPaymentUiFlow() {

        if (mTransactionRequest != null && !isRunning) {

            if (mTransactionRequest.getPaymentMethod() == Constants
                    .PAYMENT_METHOD_NOT_SELECTED) {

                mTransactionRequest.enableUi(true);
                Intent userDetailsIntent = new Intent(mTransactionRequest.getActivity(),
                        UserDetailsActivity.class);
                mTransactionRequest.getActivity().startActivity(userDetailsIntent);

            } else {
                // start specific activity depending  on payment type.
            }

        } else {

            if(mTransactionRequest == null ) {
                Logger.e(ADD_TRANSACTION_DETAILS);
            }else {
                Logger.e(Constants.ERROR_ALREADY_RUNNING);
            }
        }
    }

}