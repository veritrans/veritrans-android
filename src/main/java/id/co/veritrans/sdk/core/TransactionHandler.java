package id.co.veritrans.sdk.core;

import android.app.Activity;

import id.co.veritrans.sdk.callbacks.TokenCallBack;
import id.co.veritrans.sdk.models.TokenRequestModel;

/**
 * Created by shivam on 10/29/15.
 */
class TransactionHandler {


    public static void getToken(Activity activity, TokenRequestModel tokenRequestModel, TokenCallBack tokenCallBack){
        if( activity != null && tokenRequestModel != null && tokenCallBack != null) {
            TransactionManager.getToken(activity, tokenRequestModel, tokenCallBack);
        }else {
            Logger.e("invalid data supplied for getToken api call.");
        }
    }

}