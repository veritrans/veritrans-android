package com.midtrans.sdk.corekit.core;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.snap.Token;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ziahaqi on 3/27/18.
 */

public class MerchantServiceManager extends BaseServiceManager {
    private static final String TAG = MerchantServiceManager.class.getSimpleName();
    private MerchantApiService service;

    public MerchantServiceManager(MerchantApiService service) {
        this.service = service;
    }

    /**
     * This method will get snap token via merchant server.
     *
     * @param request  Transaction details.
     * @param callback Checkout Callback
     */
    public void checkout(final TokenRequestModel request, final CheckoutCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<Token> call = service.checkout(request);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                releaseResources();
                Token token = response.body();
                if (token != null) {
                    if (token.getTokenId() != null && !TextUtils.isEmpty(token.getTokenId())) {
                        callback.onSuccess(token);
                    } else {
                        callback.onFailure(token, Constants.MESSAGE_ERROR_EMPTY_RESPONSE);
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    /**
     * This method is used to save credit cards to merchant server
     *
     * @param cardRequests credit card Request model
     * @param userId       unique id for every user
     * @param callback     save card callback
     */
    public void saveCards(String userId, List<SaveCardRequest> cardRequests, final SaveCardCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        if (cardRequests != null) {
            Call<String> call = service.saveCards(userId, cardRequests);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    releaseResources();

                    SaveCardResponse saveCardResponse = new SaveCardResponse();
                    saveCardResponse.setCode(response.code());
                    saveCardResponse.setMessage(response.message());
                    if (response.code() == 200 || response.code() == 201) {
                        callback.onSuccess(saveCardResponse);
                    } else {
                        callback.onFailure(response.message());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    doOnResponseFailure(t, callback);
                }
            });

        } else {
            doOnInvalidDataSupplied(callback);
        }
    }

    /**
     * this method is used to get saved card list on merchant server
     *
     * @param userId   unique id to each user
     * @param callback Transaction callback
     */
    public void getCards(final String userId, final GetCardCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<List<SaveCardRequest>> call = service.getCards(userId);
        call.enqueue(new Callback<List<SaveCardRequest>>() {
            @Override
            public void onResponse(Call<List<SaveCardRequest>> call, Response<List<SaveCardRequest>> response) {
                releaseResources();

                List<SaveCardRequest> cardsResponses = response.body();
                if (cardsResponses != null && cardsResponses.size() > 0) {
                    if (response.code() == 200 || response.code() == 201) {
                        callback.onSuccess(new ArrayList<>(cardsResponses));
                    } else {
                        callback.onFailure(response.message());
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void onFailure(Call<List<SaveCardRequest>> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    void setService(MerchantApiService service) {
        this.service = service;
    }
}
