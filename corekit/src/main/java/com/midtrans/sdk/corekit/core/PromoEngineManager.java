package com.midtrans.sdk.corekit.core;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.callback.ObtainPromoCallback;
import com.midtrans.sdk.corekit.models.promo.PromosResponse;

import java.security.cert.CertPathValidatorException;

import javax.net.ssl.SSLHandshakeException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by rakawm on 2/3/17.
 */

public class PromoEngineManager extends BaseTransactionManager {

    private static final String TAG = PromoEngineManager.class.getSimpleName();
    private PromoEngineRestAPI promoEngineRestAPI;

    public PromoEngineManager(PromoEngineRestAPI promoEngineRestAPI) {
        this.promoEngineRestAPI = promoEngineRestAPI;
    }


    public void obtainPromo(String promoCode, double amount, String clientKey, String cardNumber, String cardToken, final ObtainPromoCallback callback) {
        promoEngineRestAPI.obtainPromo(promoCode, amount, clientKey, cardNumber, cardToken, new Callback<PromosResponse>() {
            @Override
            public void success(PromosResponse promosResponse, Response response) {
                if (promosResponse != null) {
                    String statusCode = promosResponse.getStatusCode();

                    if (!TextUtils.isEmpty(statusCode) && statusCode.equals(Constants.STATUS_CODE_200)) {
                        callback.onSuccess(promosResponse);
                    } else {
                        callback.onFailure(promosResponse.getStatusCode(), promosResponse.getStatusMessage());
                    }

                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                try {
                    if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                        Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
                    }

                    callback.onError(new Throwable(error.getMessage(), error.getCause()));
                } catch (RuntimeException e) {
                    callback.onError(new Throwable(e.getMessage(), e.getCause()));
                }
            }
        });
    }
}
