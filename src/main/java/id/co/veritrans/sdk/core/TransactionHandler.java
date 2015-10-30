package id.co.veritrans.sdk.core;

import android.app.Activity;

import id.co.veritrans.sdk.callbacks.PermataBankTransferStatus;
import id.co.veritrans.sdk.callbacks.TokenCallBack;
import id.co.veritrans.sdk.models.PermataBankTransfer;
import id.co.veritrans.sdk.models.TokenRequestModel;

/**
 * Created by shivam on 10/29/15.
 */
class TransactionHandler {


    public static final String INVALID_DATA_SUPPLIED = "invalid data " +
            "supplied for getToken api call.";

    public static void getToken(Activity activity, TokenRequestModel tokenRequestModel,
                                TokenCallBack tokenCallBack) {

        if (activity != null && tokenRequestModel != null && tokenCallBack != null) {

            TransactionManager.getToken(activity, tokenRequestModel, tokenCallBack);

        } else {
            Logger.e(Constants.ERROR_INVALID_DATA_SUPPLIED);
        }
    }

    public static void paymentUsingPermataBank(Activity activity, PermataBankTransfer
            permataBankTransfer, PermataBankTransferStatus permataBankTransferCallBack) {

        if (activity != null && permataBankTransfer != null && permataBankTransferCallBack != null) {
            TransactionManager.paymentUsingPermataBank(activity, permataBankTransfer, permataBankTransferCallBack);
        } else {
            Logger.e(INVALID_DATA_SUPPLIED);
        }
    }



}