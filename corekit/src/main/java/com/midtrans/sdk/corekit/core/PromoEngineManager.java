package com.midtrans.sdk.corekit.core;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.callback.HoldPromoCallback;
import com.midtrans.sdk.corekit.callback.ObtainPromoCallback;
import com.midtrans.sdk.corekit.models.promo.HoldPromoResponse;
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

    public PromoEngineManager(Context context, PromoEngineRestAPI promoEngineRestAPI) {
        this.context = context;
        this.promoEngineRestAPI = promoEngineRestAPI;
    }

    /**
     * todo remove this method
     */
//    public void obtainPromo(String promoId, double amount, final ObtainPromoCallback callback) {
//        promoEngineRestAPI.obtainPromo(promoId, amount, MidtransSDK.getInstance().getClientKey(), new Callback<ObtainPromoResponse>() {
//            @Override
//            public void success(ObtainPromoResponse obtainPromoResponse, Response response) {
//                if (obtainPromoResponse.isSuccess()) {
//                    callback.onSuccess(obtainPromoResponse);
//                } else {
//                    callback.onFailure(obtainPromoResponse.getMessage());
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                callback.onError(new Throwable(context.getString(R.string.error_empty_response)));
//            }
//        });
//    }
    public void obtainPromo(String promoCode, double amount, String clientKey, String cardNumber, String cardToken, final ObtainPromoCallback callback) {

        if (TextUtils.isEmpty(cardToken)) {
            promoEngineRestAPI.obtainPromoByCardNumber(promoCode, amount, clientKey, cardNumber, new Callback<PromosResponse>() {
                @Override
                public void success(PromosResponse promosResponse, Response response) {
                    doOnSuccess(promosResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnFailure(error, callback);
                }
            });
        } else {
            promoEngineRestAPI.obtainPromoByCardToken(promoCode, amount, clientKey, cardToken, new Callback<PromosResponse>() {
                @Override
                public void success(PromosResponse promosResponse, Response response) {
                    doOnSuccess(promosResponse, response, callback);
                }

                @Override
                public void failure(RetrofitError error) {
                    doOnFailure(error, callback);
                }
            });
        }
    }

    private void doOnFailure(RetrofitError error, ObtainPromoCallback callback) {
        try {
            if (error.getCause() instanceof SSLHandshakeException || error.getCause() instanceof CertPathValidatorException) {
                Logger.e(TAG, "Error in SSL Certificate. " + error.getMessage());
            }
            callback.onError(new Throwable(error.getMessage(), error.getCause()));
        } catch (RuntimeException e) {
            callback.onError(new Throwable(e.getMessage(), e.getCause()));
        }
    }

    private void doOnSuccess(PromosResponse promosResponse, Response response, ObtainPromoCallback callback) {
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


    public void holdPromo(String promoToken, long amount, String clientKey, final HoldPromoCallback callback) {
        if (callback != null) {
            promoEngineRestAPI.holdPromo(promoToken, amount, clientKey, new Callback<HoldPromoResponse>() {
                @Override
                public void success(HoldPromoResponse holdPromoResponse, Response response) {
                    if (holdPromoResponse != null) {
                        if (holdPromoResponse.isSuccess()) {
                            callback.onSuccess(holdPromoResponse);
                        } else {
                            callback.onFailure(holdPromoResponse.getMessage());
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

}
