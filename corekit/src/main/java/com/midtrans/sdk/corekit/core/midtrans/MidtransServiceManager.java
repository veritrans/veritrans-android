package com.midtrans.sdk.corekit.core.midtrans;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.network.BaseServiceManager;
import com.midtrans.sdk.corekit.core.midtrans.response.CardRegistrationResponse;
import com.midtrans.sdk.corekit.core.midtrans.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.utilities.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ziahaqi on 3/27/18.
 */

public class MidtransServiceManager extends BaseServiceManager {
    // private static final String TAG = MerchantServiceManager.class.getSimpleName();
    private MidtransApiService service;

    public MidtransServiceManager(MidtransApiService service) {
        this.service = service;
    }

    /**
     * It will execute API call to get token from Veritrans that can be used later.
     *
     * @param cardNumber   credit card number
     * @param cardCvv      credit card CVV number
     * @param cardExpMonth credit card expired month
     * @param cardExpYear  credit card expired year
     * @param callback     card transaction callback
     */
    public void cardRegistration(String cardNumber,
                                 String cardCvv,
                                 String cardExpMonth,
                                 String cardExpYear, String clientKey,
                                 final CardRegistrationCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<CardRegistrationResponse> call = service.registerCard(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientKey);
        call.enqueue(new Callback<CardRegistrationResponse>() {
            @Override
            public void onResponse(@NonNull Call<CardRegistrationResponse> call, @NonNull Response<CardRegistrationResponse> response) {
                releaseResources();
                CardRegistrationResponse cardRegistrationResponse = response.body();
                if (cardRegistrationResponse != null) {
                    String statusCode = cardRegistrationResponse.getStatusCode();

                    if (!TextUtils.isEmpty(statusCode) && statusCode.equals(Constants.STATUS_CODE_200)) {
                        callback.onSuccess(cardRegistrationResponse);
                    } else {
                        callback.onFailure(cardRegistrationResponse, cardRegistrationResponse.getStatusMessage());
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void onFailure(Call<CardRegistrationResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }
}
