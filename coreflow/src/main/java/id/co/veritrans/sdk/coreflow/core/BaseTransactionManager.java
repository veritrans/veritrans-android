package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;

import id.co.veritrans.sdk.coreflow.models.TransactionResponse;


/**
 * Created by ziahaqi on 7/18/16.
 */
public abstract class BaseTransactionManager {

    // Event Name
    protected static final String KEY_TRANSACTION_SUCCESS = "Transaction Success";
    protected static final String KEY_TRANSACTION_FAILED = "Transaction Failed";
    protected static final String KEY_TOKENIZE_SUCCESS = "Tokenize Success";
    protected static final String KEY_TOKENIZE_FAILED = "Tokenize Failed";

    // Payment Name
    protected static final String PAYMENT_TYPE_CIMB_CLICK = "cimb_click";
    protected static final String PAYMENT_TYPE_BCA_KLIKPAY = "bca_klikpay";
    protected static final String PAYMENT_TYPE_MANDIRI_CLICKPAY = "mandiri_clickpay";
    protected static final String PAYMENT_TYPE_MANDIRI_ECASH = "mandiri_ecash";
    protected static final String PAYMENT_TYPE_BANK_TRANSFER = "bank_transfer";
    protected static final String PAYMENT_TYPE_CREDIT_CARD = "cc";
    protected static final String PAYMENT_TYPE_BRI_EPAY = "bri_epay";
    protected static final String PAYMENT_TYPE_BBM_MONEY = "bbm_money";
    protected static final String PAYMENT_TYPE_INDOSAT_DOMPETKU = "indosat_dompetku";
    protected static final String PAYMENT_TYPE_INDOMARET = "indomaret";
    protected static final String PAYMENT_TYPE_KLIK_BCA = "bca_klikbca";
    // Bank transfer type
    protected static final String BANK_PERMATA = "permata";
    protected static final String BANK_BCA = "bca";
    protected static final String BANK_MANDIRI = "mandiri";

    protected Context context;
    protected MixpanelAnalyticsManager analyticsManager;
    protected boolean isSDKLogEnabled = false;
    protected VeritransRestAPI veritransPaymentAPI;
    protected MerchantRestAPI merchantPaymentAPI;

    protected void releaseResources() {
        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null) {
            veritransSDK.releaseResource();
            Logger.i("released transaction");
        }
    }

    public void setAnalyticsManager(MixpanelAnalyticsManager analyticsManager) {
        this.analyticsManager = analyticsManager;
    }



    public void setVeritransPaymentAPI(VeritransRestAPI veritransPaymentAPI) {
        this.veritransPaymentAPI = veritransPaymentAPI;
    }

    public void setMerchantPaymentAPI(MerchantRestAPI merchantPaymentAPI) {
        this.merchantPaymentAPI = merchantPaymentAPI;
    }

    public void setSDKLogEnabled(boolean SDKLogEnabled) {
        isSDKLogEnabled = SDKLogEnabled;
    }


    protected static void displayResponse(TransactionResponse
                                                transferResponse) {
        Logger.d("transfer response: virtual account" +
                " number ", "" +
                transferResponse.getPermataVANumber());

        Logger.d(" transfer response: status message " +
                "", "" +
                transferResponse.getStatusMessage());

        Logger.d(" transfer response: status code ",
                "" + transferResponse.getStatusCode());

        Logger.d(" transfer response: transaction Id ",
                "" + transferResponse
                        .getTransactionId());

        Logger.d(" transfer response: transaction " +
                        "status ",
                "" + transferResponse
                        .getTransactionStatus());
    }



}
