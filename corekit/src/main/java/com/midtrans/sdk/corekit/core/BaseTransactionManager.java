package com.midtrans.sdk.corekit.core;

import android.content.Context;

import com.midtrans.sdk.analytics.MixpanelAnalyticsManager;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TransactionResponse;


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
    protected static final String PAYMENT_TYPE_MANDIRI_BILL_PAY = "mandiri_bill_pay";
    protected static final String PAYMENT_TYPE_TELKOMSEL_ECASH = "telkomsel_ecash";
    protected static final String PAYMENT_TYPE_XL_TUNAI = "xl_tunai";
    protected static final String PAYMENT_TYPE_KIOSAN = "kiosan";
    protected static final String PAYMENT_TYPE_GCI = "gci";

    // Bank transfer type
    protected static final String BANK_PERMATA = "Permata";
    protected static final String BANK_BCA = "BCA";
    protected static final String BANK_MANDIRI = "Mandiri";
    protected static final String ALL_BANK = "Other";

    protected boolean isSDKLogEnabled = false;
    protected MidtransRestAPI midtransPaymentAPI;
    protected MerchantRestAPI merchantPaymentAPI;
    private MixpanelAnalyticsManager analyticsManager;

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

    protected void displayTokenResponse(TokenDetailsResponse tokenDetailsResponse) {
        Logger.d("token response: status code ", "" +
                tokenDetailsResponse.getStatusCode());
        Logger.d("token response: status message ", "" +
                tokenDetailsResponse.getStatusMessage());
        Logger.d("token response: token Id ", "" + tokenDetailsResponse
                .getTokenId());
        Logger.d("token response: redirect url ", "" +
                tokenDetailsResponse.getRedirectUrl());
        Logger.d("token response: bank ", "" + tokenDetailsResponse
                .getBank());
    }

    protected void releaseResources() {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null) {
            midtransSDK.releaseResource();
            Logger.i("released transaction");
        }
    }

    public void setAnalyticsManager(MixpanelAnalyticsManager analyticsManager) {
        this.analyticsManager = analyticsManager;
    }

    public void setMidtransPaymentAPI(MidtransRestAPI midtransPaymentAPI) {
        this.midtransPaymentAPI = midtransPaymentAPI;
    }

    public void setMerchantPaymentAPI(MerchantRestAPI merchantPaymentAPI) {
        this.merchantPaymentAPI = merchantPaymentAPI;
    }

    public void setSDKLogEnabled(boolean SDKLogEnabled) {
        isSDKLogEnabled = SDKLogEnabled;
    }


}
